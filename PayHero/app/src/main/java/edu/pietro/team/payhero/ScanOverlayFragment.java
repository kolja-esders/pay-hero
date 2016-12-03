package edu.pietro.team.payhero;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ScanOverlayFragment extends Fragment {

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

}