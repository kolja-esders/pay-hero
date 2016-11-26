package edu.pietro.team.payhero;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.widget.Toast;

import java.nio.Buffer;
import java.util.Arrays;
import java.util.List;

import edu.pietro.team.payhero.R;

public class ScanActivity extends AppCompatActivity implements SurfaceHolder.Callback, Handler.Callback {

    static final int MY_PERMISSIONS_REQUEST_CAMERA = 1242;

    private CameraDevice mCameraDevice;

    private CameraCaptureSession mCaptureSession;

    private SurfaceView mPreviewSurfaceView;

    private SurfaceHolder mPreviewSurfaceHolder;

    private Surface mCameraSurface;

    private boolean mSurfaceCreated = false;

    boolean mIsCameraConfigured = false;

    private final Handler mHandler = new Handler(this);

    private static final int MSG_CAMERA_OPENED = 1;

    private static final int MSG_SURFACE_READY = 2;

    private CameraDevice.StateCallback mCameraStateCallback;

    private CameraManager mCameraManager;

    private String mCameraId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Preview
        mPreviewSurfaceView = (SurfaceView) findViewById(R.id.preview);
        mPreviewSurfaceHolder = mPreviewSurfaceView.getHolder();
        mPreviewSurfaceHolder.addCallback(this);

        // Camera
        mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        mCameraId =  getBackFacingCameraId(mCameraManager);
        mCameraStateCallback = new CameraDevice.StateCallback() {
            @Override
            public void onOpened(CameraDevice camera) {
                Toast.makeText(getApplicationContext(), "onOpened", Toast.LENGTH_SHORT).show();
                mCameraDevice = camera;
                mHandler.sendEmptyMessage(MSG_CAMERA_OPENED);
            }
            @Override
            public void onDisconnected(CameraDevice camera) {
                Toast.makeText(getApplicationContext(), "onDisconnected", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(CameraDevice camera, int error) {
                Toast.makeText(getApplicationContext(), "onError", Toast.LENGTH_SHORT).show();
            }
        };


        /*if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)) {
            try {
                // Find correct camera
                String frontCameraId = getFrontFacingCameraId(mCameraManager);
                CameraCharacteristics cameraCharacteristics = mCameraSurface.getCameraCharacteristics(frontCameraId);
                StreamConfigurationMap streamConfigs = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

                // Get sizes
                Size[] jpegSizes = streamConfigs.getOutputSizes(ImageFormat.JPEG);
                //Size[] rawSizes = streamConfigs.getOutputSizes(ImageFormat.RAW_SENSOR);

                // Get the actual image content
                ImageReader rawImageReader = ImageReader.newInstance(rawSizes[0].getWidth(), rawSizes[0].getHeight(), ImageFormat.RAW_SENSOR, 1);
                rawImageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
                    @Override
                    public void onImageAvailable(ImageReader reader) {
                        // save raw
                    }
                }, null);
                ImageReader jpegImageReader = ImageReader.newInstance(jpegSizes[0].getWidth(), jpegSizes[0].getHeight(), ImageFormat.JPEG, 1);
                jpegImageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
                    @Override
                    public void onImageAvailable(ImageReader reader) {
                        // save jpeg
                    }
                }, null);


                Surface previewSurface = new Surface(mPreviewSurfaceTexture);
                Surface rawCaptureSurface = rawImageReader.getSurface();
                Surface jpegCaptureSurface = jpegImageReader.getSurface();

                try {
                    CaptureRequest.Builder request = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                    request.addTarget(mCameraSurface);
                    // set capture options: fine-tune manual focus, white balance, etc.
                    mCaptureSession.setRepeatingRequest(request.build(), new CameraCaptureSession.CaptureCallback() {
                        @Override
                        public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
                            // updated values can be found here
                        }
                    }, null);
                } catch (CameraAccessException e) {
                    throw new RuntimeException("fuck this");
                }


                List<Surface> surfaces = Arrays.asList(previewSurface, jpegCaptureSurface);
                mCameraDevice.createCaptureSession(surfaces, new CameraCaptureSession.StateCallback() {
                    @Override
                    public void onConfigured(CameraCaptureSession session) {
                        mCaptureSession = session;
                    }
                    @Override
                    public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {
                    }
                }, null);




            } catch (CameraAccessException e) {
                throw new RuntimeException("No camera access :(");
            }
        } else {
            throw new RuntimeException("Missing camera permissions :(");
        }*/
    }

    @Override
    protected void onStart() {
        super.onStart();

        //requesting permission
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
                Toast.makeText(getApplicationContext(), "request permission", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "PERMISSION_ALREADY_GRANTED", Toast.LENGTH_SHORT).show();
            try {
                mCameraManager.openCamera(mCameraId, mCameraStateCallback, new Handler());
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            if (mCaptureSession != null) {
                mCaptureSession.stopRepeating();
                mCaptureSession.close();
                mCaptureSession = null;
            }

            mIsCameraConfigured = false;
        } catch (final CameraAccessException e) {
            // Doesn't matter, closing device anyway
            e.printStackTrace();
        } catch (final IllegalStateException e2) {
            // Doesn't matter, closing device anyway
            e2.printStackTrace();
        } finally {
            if (mCameraDevice != null) {
                mCameraDevice.close();
                mCameraDevice = null;
                mCaptureSession = null;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                    try {
                        mCameraManager.openCamera(mCameraId, mCameraStateCallback, new Handler());
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                break;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mCameraSurface = holder.getSurface();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mCameraSurface = holder.getSurface();
        mSurfaceCreated = true;
        mHandler.sendEmptyMessage(MSG_SURFACE_READY);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mSurfaceCreated = false;
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_CAMERA_OPENED:
            case MSG_SURFACE_READY:
                // if both surface is created and camera device is opened
                // - ready to set up preview and other things
                if (mSurfaceCreated && (mCameraDevice != null)
                        && !mIsCameraConfigured) {
                    configureCamera();
                }
                break;
        }

        return true;
    }

    private void configureCamera() {
        try {
            mCameraDevice.createCaptureSession(Arrays.asList(mCameraSurface, mPreviewSurfaceHolder.getSurface()),
                    new CaptureSessionListener(), null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        mIsCameraConfigured = true;
    }



    private class CaptureSessionListener extends
            CameraCaptureSession.StateCallback {
        @Override
        public void onConfigureFailed(final CameraCaptureSession session) {
            Log.d("SCAN", "CaptureSessionConfigure failed");
        }

        @Override
        public void onConfigured(final CameraCaptureSession session) {
            Log.d("SCAN", "CaptureSessionConfigure onConfigured");
            mCaptureSession = session;

            try {
                CaptureRequest.Builder previewRequestBuilder = mCameraDevice
                        .createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                previewRequestBuilder.addTarget(mCameraSurface);
                mCaptureSession.setRepeatingRequest(previewRequestBuilder.build(),
                        null, null);
            } catch (CameraAccessException e) {
                Log.d("SCAN", "setting up preview failed");
                e.printStackTrace();
            }
        }
    }


    private String getFrontFacingCameraId(CameraManager cameraManager) {
        String frontCameraId = null;
        try {
            for (String id : cameraManager.getCameraIdList()) {
                CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(id);
                Integer lensFacingCharacteristics = cameraCharacteristics.get(CameraCharacteristics.LENS_FACING);
                if (lensFacingCharacteristics != null && lensFacingCharacteristics == CameraCharacteristics.LENS_FACING_FRONT) {
                    frontCameraId = id;
                }
            }
        } catch (CameraAccessException e) {
            throw new RuntimeException("No camera access :(");
        }

        return frontCameraId;
    }

    private String getBackFacingCameraId(CameraManager cameraManager) {
        String frontCameraId = null;
        try {
            for (String id : cameraManager.getCameraIdList()) {
                CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(id);
                Integer lensFacingCharacteristics = cameraCharacteristics.get(CameraCharacteristics.LENS_FACING);
                if (lensFacingCharacteristics != null && lensFacingCharacteristics == CameraCharacteristics.LENS_FACING_BACK) {
                    frontCameraId = id;
                }
            }
        } catch (CameraAccessException e) {
            throw new RuntimeException("No camera access :(");
        }

        return frontCameraId;
    }
}
