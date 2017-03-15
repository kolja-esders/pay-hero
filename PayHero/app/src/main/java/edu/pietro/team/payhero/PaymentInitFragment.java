package edu.pietro.team.payhero;

import android.animation.ObjectAnimator;
import android.app.DownloadManager;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import edu.pietro.team.payhero.helper.PostHelper;
import edu.pietro.team.payhero.social.Stories;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PaymentInitFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PaymentInitFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PaymentInitFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Handler mHandler = new Handler();

    public PaymentInitFragment() {
        // Required empty public constructor
    }


    public static PaymentInitFragment newInstance() {
        Bundle args = new Bundle();
        PaymentInitFragment fragment = new PaymentInitFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_payment_init, container, false);
        final FloatingActionButton confirm = (FloatingActionButton) v.findViewById(R.id.payButton);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.getCurrentActivity().disableScrolling();
                // Hide button and show loading
                ((FloatingActionButton) v.findViewById(R.id.payButton)).setVisibility(View.INVISIBLE);
                ((ProgressBar) v.findViewById(R.id.payProgress)).setVisibility(View.VISIBLE);

                String name = ((TextView) v.findViewById(R.id.nameEdit)).getText().toString();
                EditText ibanEdit = (EditText) v.findViewById(R.id.ibanEdit);
                final EditText amountEdit = (EditText) v.findViewById(R.id.amountEdit);
                ibanEdit.setEnabled(false);
                amountEdit.setEnabled(false);
                String iban = ibanEdit.getText().toString();
                String amount = amountEdit.getText().toString();

                // Add Story (might not be right here !!!!)
                String purchaseString = ((EditText)v.findViewById(R.id.purchaseMessage)).getText().toString();
                Stories.Story buyStory = new Stories.Story(( (MainActivity) getActivity()).getCurrentTransfer(), purchaseString);
                Stories.DISPLAYED_ITEMS.add(buyStory);

                new AsyncTask<String[], Void, Boolean>() {
                    @Override
                    protected Boolean doInBackground(String[]... strings) {
                        Log.d("HELP", strings.toString());
                        try {
                            PostHelper.transfer(strings[0][0].replace(" ", ""), strings[0][1], strings[0][2].replace(",", ".").replace("€", "").replace(" ", ""));
                            return true;
                        } catch (Exception e) {
                            Log.e("PAYMENT", "Payment failed :/", e);
                        }
                        return false;
                    }

                    @Override
                    protected void onPostExecute(Boolean transerSuccessful) {
                        super.onPostExecute(transerSuccessful);
                        if (transerSuccessful) {
                            //Looper.prepare();

                            Animation anim = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.slide);
                            anim.setInterpolator(new AnticipateInterpolator(1.2f));
                            anim.setFillAfter(true);
                            amountEdit.startAnimation(anim);

                            /*Intent intent = new Intent(ValidationActivity.this, PaymentSuccessActivity.class);
                            intent.putExtra("name", ValidationActivity.this.mName);
                            intent.putExtra("amount", ValidationActivity.this.mAmount);
                            startActivity(intent);*/
                            ((ProgressBar) v.findViewById(R.id.payProgress)).setVisibility(View.INVISIBLE);
                            ((ImageView) v.findViewById(R.id.paySuccess)).setVisibility(View.VISIBLE);
                            mHandler.removeMessages(0);
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    MainActivity.getCurrentActivity().resetPaymentView(true);
                                }
                            }, 2000);
                        } else {
                            /*EditText ibanEdit = (EditText) v.findViewById(R.id.ibanEdit);
                            EditText amountEdit = (EditText) v.findViewById(R.id.amountEdit);
                            ibanEdit.setEnabled(true);
                            amountEdit.setEnabled(true);
                            ((FloatingActionButton) v.findViewById(R.id.payButton)).setVisibility(View.VISIBLE);*/
                            MainActivity.getCurrentActivity().resetPaymentView(false);
                        }
                    }
                }.execute(new String[][]{{iban, name, amount}});
            }
        });
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
