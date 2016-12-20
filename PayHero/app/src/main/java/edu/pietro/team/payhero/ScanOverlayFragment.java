package edu.pietro.team.payhero;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ScanOverlayFragment extends Fragment {

    private OnScanOverlayFragmentInteractionListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scan_overlay, container, false);
    }

    public static ScanOverlayFragment newInstance() {
        Bundle args = new Bundle();
        ScanOverlayFragment fragment = new ScanOverlayFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnScanOverlayFragmentInteractionListener) {
            mListener = (OnScanOverlayFragmentInteractionListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement OnScanOverlayFragmentInteractionListener");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnScanOverlayFragmentInteractionListener) {
            mListener = (OnScanOverlayFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnScanOverlayFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (mListener != null) {
            mListener.onScannerVisibilityChange(isVisibleToUser);
        }
    }

    public interface OnScanOverlayFragmentInteractionListener {
        /**
         * Gets called when there is a visibility change for this fragment.
         */
        void onScannerVisibilityChange(boolean isVisible);
    }

}