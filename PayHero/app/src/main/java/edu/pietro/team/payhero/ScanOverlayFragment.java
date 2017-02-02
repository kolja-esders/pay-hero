package edu.pietro.team.payhero;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;

import edu.pietro.team.payhero.event.OnImageCaptureRequested;

public class ScanOverlayFragment extends Fragment {

    private EventBus mDefaultEventBus;

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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDefaultEventBus = EventBus.getDefault();

        View scanOverlay = getActivity().findViewById(R.id.scan_overlay);
        scanOverlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDefaultEventBus.post(new OnImageCaptureRequested());
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnScanOverlayFragmentInteractionListener) {
            mListener = (OnScanOverlayFragmentInteractionListener) activity;
        } else {
            /*throw new RuntimeException(activity.toString()
                    + " must implement OnScanOverlayFragmentInteractionListener");*/
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnScanOverlayFragmentInteractionListener) {
            mListener = (OnScanOverlayFragmentInteractionListener) context;
        } else {
            /*throw new RuntimeException(context.toString()
                    + " must implement OnScanOverlayFragmentInteractionListener");*/
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