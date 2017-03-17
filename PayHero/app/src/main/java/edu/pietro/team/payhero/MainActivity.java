package edu.pietro.team.payhero;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.MultiDetector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.LargestFaceFocusingProcessor;
import com.google.android.gms.vision.text.TextRecognizer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import edu.pietro.team.payhero.entities.AmountOfMoney;
import edu.pietro.team.payhero.event.FeedFilterClicked;
import edu.pietro.team.payhero.event.OnErrorDuringDetectionPostProcessing;
import edu.pietro.team.payhero.event.OnImageCaptureRequested;
import edu.pietro.team.payhero.event.OnPaymentInit;
import edu.pietro.team.payhero.event.OnStartDetectionPostProcessing;
import edu.pietro.team.payhero.event.OnStopMessage;
import edu.pietro.team.payhero.helper.DownloadImageTask;
import edu.pietro.team.payhero.helper.ProcessingState;
import edu.pietro.team.payhero.helper.Utils;
import edu.pietro.team.payhero.social.Item;
import edu.pietro.team.payhero.social.MoneyTransfer;
import edu.pietro.team.payhero.social.User;
import edu.pietro.team.payhero.vision.BarcodeTracker;
import edu.pietro.team.payhero.vision.CameraSourcePreview;
import edu.pietro.team.payhero.vision.FaceTracker;
import edu.pietro.team.payhero.vision.FirstFocusingProcessor;
import edu.pietro.team.payhero.vision.ImageFetchingDetector;
import edu.pietro.team.payhero.vision.OcrDetectionProcessor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    /**
     * Tag for the {@link Log}.
     */
    private static final String TAG = "MainActivity";

    private static final int RC_HANDLE_CAMERA_PERM = 2;
    private static final int RC_HANDLE_STORAGE_PERM = 3;

    private CameraSource mCameraSource;

    private Boolean mFeedFilterIsPublic = true;

    private CameraSourcePreview mPreview;

    private CollectionPagerAdapter mCollectionPagerAdapter;

    private ViewPager mViewPager;

    private Object mProcessingLock = new Object();
    private ProcessingState mProcessingState = ProcessingState.NOLOCK;

    private static MainActivity currentActivity = null;

    private MoneyTransfer currentTransfer = null;

    public static MainActivity getCurrentActivity() {
        return currentActivity;
    }

    private static String[] BT_CONTEXT_DEVICES = new String[] {
            "04:59:06:09:52:06",
            "f8:e0:79:4c:ea:e6",
            "b4:ce:f6:22:d7:16",
            "78:02:f8:e7:96:ae"
    };

    private final BroadcastReceiver mBtReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            switch (action){
                case BluetoothDevice.ACTION_ACL_CONNECTED:
                    BluetoothDevice dev = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    Log.d("BLUETOOTH", "connected to " + dev.getAddress());
                    for (String d : BT_CONTEXT_DEVICES) {
                        if (d.equals(dev.getAddress())){
                            User seller = User.DISNEY;
                            Item ticket = new Item(
                                    "Family ticket",
                                    "Disneyland",
                                    "http://www.parkerlebnis.de/wp-content/uploads/2013/11/disneyland-paris.jpg",
                                    new AmountOfMoney(99.0)
                            );
                            EventBus.getDefault().post(new OnPaymentInit(
                                    new MoneyTransfer(seller, ticket, new AmountOfMoney(99.0)),
                                    ProcessingState.NOLOCK
                            ));
                            break;
                        }
                    }
                    break;
                case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                    Log.d("BLUETOOTH", "disconnected");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*String[] permissions = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE"
        };
        ActivityCompat.requestPermissions(this, permissions, RC_HANDLE_STORAGE_PERM);*/

        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        registerReceiver(mBtReceiver, filter);

        mCollectionPagerAdapter =
                new CollectionPagerAdapter(getFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mCollectionPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setCurrentItem(1);
        mPreview = (CameraSourcePreview) findViewById(R.id.preview);

        // Check for the camera permission before accessing the camera.  If the
        // permission is not granted yet, request permission.
        int rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (rc == PackageManager.PERMISSION_GRANTED) {
            createCameraSource();
        } else {
            requestCameraPermission();
        }
    }

    /**
     * Handles the requesting of the camera permission.  This includes
     * showing a "Snackbar" message of why the permission is needed then
     * sending the request.
     */
    private void requestCameraPermission() {
        Log.w(TAG, "Camera permission is not granted. Requesting permission");
        final String[] permissions = new String[]{Manifest.permission.CAMERA};
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this, permissions, RC_HANDLE_CAMERA_PERM);
        }
    }

    /**
     * Creates and starts the camera.
     */
    private void createCameraSource() {
        Context ctx = getApplicationContext();
        Point displaySize = new Point();
        getWindowManager().getDefaultDisplay().getRealSize(displaySize);

        // dummy detector saving the last frame in order to send it to Microsoft in case of face detection
        ImageFetchingDetector imageFetchingDetector = new ImageFetchingDetector();

        // We need to provide at least one detector to the camera :x
        FaceDetector faceDetector = new FaceDetector.Builder(ctx).build();
        faceDetector.setProcessor(
                new LargestFaceFocusingProcessor.Builder(faceDetector, new FaceTracker(imageFetchingDetector))
                        .build());

        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(ctx).build();
        //barcodeDetector.setProcessor(new BarcodeDetectionProcessor());
        barcodeDetector.setProcessor(
                new FirstFocusingProcessor<Barcode>(barcodeDetector, new BarcodeTracker())
        );

        TextRecognizer textRecognizer = new TextRecognizer.Builder(ctx).build();
        textRecognizer.setProcessor(new OcrDetectionProcessor());
        // TODO: Check if the TextRecognizer is operational.

        MultiDetector multiDetector = new MultiDetector.Builder()
                .add(imageFetchingDetector)
                .add(faceDetector)
                .add(barcodeDetector)
                .add(textRecognizer)
                .build();

        mCameraSource = new CameraSource.Builder(ctx, multiDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setAutoFocusEnabled(true)
                .setRequestedFps(5.0f)
                .setRequestedPreviewSize(displaySize.y, displaySize.x)
                .build();
    }

    private void startCameraSource() {
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
                getApplicationContext());
        if (code != ConnectionResult.SUCCESS) {
            Log.e(TAG, "Google Play Services unavailable.");
            return;
        }

        if (mCameraSource != null) {
            try {
                mPreview.start(mCameraSource);
            } catch (IOException e) {
                Log.e(TAG, "Unable to start camera source.", e);
                mCameraSource.release();
                mCameraSource = null;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.friend_feed, menu);
        if (menu.size() > 0) {
            menu.getItem(0).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        }
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorite:
                String newTitle;
                if (mFeedFilterIsPublic) {
                    newTitle = "Show friends purchases";
                } else {
                    newTitle = "Show own purchases";
                }
                mFeedFilterIsPublic ^= true;
                EventBus.getDefault().post(new FeedFilterClicked(!mFeedFilterIsPublic));
                item.setTitle(newTitle);
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startCameraSource();
        currentActivity = this;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPreview.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCameraSource != null) {
            mCameraSource.release();
        }
        unregisterReceiver(mBtReceiver);
    }

    @Override
    public void onBackPressed() {
        Log.d("Back", "I'll be back");
    }

    @Subscribe
    public void showPaymentInit(OnPaymentInit e) {
        final MoneyTransfer moneyTransfer = e.getPurchase();
        final ProcessingState assumedProcessingState = e.getAssumedProcessingState();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                synchronized (mProcessingLock) {
                    if (mViewPager.getCurrentItem() == 1 && moneyTransfer.isValid()
                            && (mProcessingState == assumedProcessingState || mProcessingState == ProcessingState.NOLOCK)) {

                        View view = mCollectionPagerAdapter.getItem(2).getView();
                        populatePaymentInitView(view, moneyTransfer);

                        Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                        v.vibrate(150);
                        EventBus.getDefault().post(new OnStopMessage());

                        mViewPager.setCurrentItem(2);
                        mProcessingState = ProcessingState.NOLOCK;
                    }
                }
            }
        });
    }

    @Subscribe
    public void onMessageEvent(OnImageCaptureRequested e) {
        Log.d("EVENT_BUS", "Image capture requested.");

//        TextView et = (TextView) mCollectionPagerAdapter.getItem(2).getView().findViewById(R.id.nameEdit);
//        et.setText("Test");
//        mViewPager.setCurrentItem(2);

        if (!onTryStartProcessing(ProcessingState.OBJECT_LOCK)){
            return;
        }

        mCameraSource.takePicture(null, new CameraSource.PictureCallback() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onPictureTaken(final byte[] bytes) {
                // @David: Da ist das IMG :)

                EventBus.getDefault().post(new OnStartDetectionPostProcessing("Searching for product..."));


                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            
                            String url = "http://1027cf3f.ngrok.io/obrec/";
                            //String url = "http://d00d8906.ngrok.io/obrec/";
                            URL obj = new URL(url);


                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                            Log.d("BS", bitmap.getWidth() + " x " + bitmap.getHeight());

                            int newHeight = (int) (bitmap.getHeight() * 0.6);

                            int hOffset = (bitmap.getHeight() - newHeight) / 2;

                            bitmap = Bitmap.createBitmap(bitmap, 0, hOffset, bitmap.getWidth(), bitmap.getHeight() - hOffset);
                            bitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, false);

                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            byte[] byteArray = stream.toByteArray();


                            long timeNow = System.currentTimeMillis();

                            String filename = timeNow + ".png";
                            FileOutputStream outputStream;

                            File file = File.createTempFile(filename, null, getCacheDir());


                            try {
                                outputStream = new FileOutputStream(file);
                                outputStream.write(byteArray);
                                outputStream.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

                            RequestBody req = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("userid", "8457851245")
                                    .addFormDataPart("file", filename, RequestBody.create(MEDIA_TYPE_PNG, file)).build();

                            Request request = new Request.Builder()
                                    .url(url)
                                    .post(req)
                                    .build();

                            OkHttpClient client = new OkHttpClient();
                            Response response = client.newCall(request).execute();

                            String prdResp = response.body().string();

                            Log.d("response", "uploadImage:" + prdResp);

                            final JSONObject jsonProd = new JSONObject(prdResp);

                            Item foundProduct = new Item(jsonProd.getString("name"), jsonProd.getString("brand"), jsonProd.getString("display_img_path"), new AmountOfMoney(jsonProd.getDouble("price")));

                            User seller = User.ZALANDO;
                            EventBus.getDefault().post(new OnPaymentInit(
                                    new MoneyTransfer(seller, foundProduct, foundProduct.getRetailPrice()),
                                    ProcessingState.OBJECT_LOCK
                            ));


                        } catch (Exception x) {
                            EventBus.getDefault().post(new OnErrorDuringDetectionPostProcessing("No internet connection"));

                            x.printStackTrace();
                        } finally {
                            onStopProcessing(ProcessingState.OBJECT_LOCK);
                        }
                    }
                });
                thread.start();

            }

        });
    }

    private void populatePaymentInitView(View v, MoneyTransfer mt) {
        boolean isPurchase = mt.getItem() != null;

        this.currentTransfer = mt;

        User recipient = mt.getRecipient();
        String recipientName = recipient.getName();
        String recipientIban = recipient.getIban();
        int recipientImageResourceId = recipient.getImageResourceId();
        String formattedAmount = mt.getAmount().getFormattedAmount();

        EditText nameEdit = (EditText) v.findViewById(R.id.nameEdit);
        EditText ibanEdit = (EditText) v.findViewById(R.id.ibanEdit);
        EditText amountEdit = (EditText) v.findViewById(R.id.amountEdit);
        TextView titleView = (TextView) v.findViewById(R.id.paymentTitleContent);
        EditText message = (EditText) v.findViewById(R.id.purchaseMessage);
        ImageView purchasableView = (ImageView) v.findViewById(R.id.imagePurchasable);
        ImageView recipientImage = (ImageView) v.findViewById(R.id.profileImage);

        if (isPurchase) {
            String productImageUrl = mt.getItem().getImageUrl();
            String productName = mt.getItem().getName();
            purchasableView.setVisibility(View.INVISIBLE);
            new DownloadImageTask(purchasableView).execute(productImageUrl);
            titleView.setText(productName);
            message.setHint("Share your purchase");
            // TODO: Disable editing of name and iban
        } else {
            titleView.setText("Money transfer");
            purchasableView.setImageDrawable(getResources().getDrawable(R.drawable.ic_dollar_bill));
            message.setHint("Enter reference line");
        }

        if (recipientImageResourceId != -1) {
            recipientImage.setImageDrawable(getResources().getDrawable(recipientImageResourceId));
        } else {
            recipientImage.setImageDrawable(getResources().getDrawable(R.drawable.default_user));
        }

        nameEdit.setText(recipientName);
        ibanEdit.setText(Utils.formatIBAN(recipientIban));
        amountEdit.setText(formattedAmount);

        amountEdit.setEnabled(!isPurchase);
        nameEdit.setEnabled(!isPurchase);
        ibanEdit.setEnabled(!isPurchase);
    }

    public void disableScrolling() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mViewPager.setOnTouchListener(new View.OnTouchListener() {

                    public boolean onTouch(View arg0, MotionEvent arg1) {
                        return true;
                    }
                });
            }
        });
    }

    public void resetPaymentView(final boolean switchToMain) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mViewPager.setOnTouchListener(null);

                if (switchToMain)
                    mViewPager.setCurrentItem(1);

                View v = mCollectionPagerAdapter.getItem(2).getView();

                EditText nameEdit = (EditText) v.findViewById(R.id.nameEdit);
                EditText ibanEdit = (EditText) v.findViewById(R.id.ibanEdit);
                EditText amountEdit = (EditText) v.findViewById(R.id.amountEdit);
                TextView titleView = (TextView) v.findViewById(R.id.paymentTitleContent);
                EditText message = (EditText) v.findViewById(R.id.purchaseMessage);
                ImageView purchasableView = (ImageView) v.findViewById(R.id.imagePurchasable);
                ImageView recipientImage = (ImageView) v.findViewById(R.id.profileImage);

                recipientImage.setImageDrawable(getResources().getDrawable(R.drawable.default_user));

                ibanEdit.setEnabled(true);
                nameEdit.setEnabled(true);
                amountEdit.setEnabled(true);

                nameEdit.setText("");
                amountEdit.setText("");
                ibanEdit.setText("");

                amountEdit.clearAnimation();
                titleView.setText("Money transfer");
                purchasableView.setImageDrawable(getResources().getDrawable(R.drawable.ic_dollar_bill));
                message.setHint("Enter reference line");
                message.setText("");

                Button payBtn = (Button) v.findViewById(R.id.payButton);
                payBtn.setText("SEND NOW");
                payBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.right_24dp), null);
                ((ProgressBar) v.findViewById(R.id.payProgress)).setVisibility(View.INVISIBLE);
                ((ImageView) v.findViewById(R.id.paySuccess)).setVisibility(View.INVISIBLE);
            }
        });
    }

    public MoneyTransfer getCurrentTransfer(){
        return this.currentTransfer;
    }

    public void setCurrentTransfer(MoneyTransfer trans){
        this.currentTransfer = trans;
    }

    public boolean onTryStartProcessing(ProcessingState ps) {
        synchronized (mProcessingLock) {
            if (mProcessingState == ProcessingState.NOLOCK && mViewPager.getCurrentItem() == 1) {
                mProcessingState = ps;
                return true;
            }
            return false;
        }
    }

    public void onStopProcessing(ProcessingState ps) {
        synchronized (mProcessingLock) {
            if (mProcessingState != ps)
                Log.e(TAG, "Processing state reset by another process");
            mProcessingState = ProcessingState.NOLOCK;
        }
    }
}
