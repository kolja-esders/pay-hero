package edu.pietro.team.payhero;


import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

public class CollectionPagerAdapter extends FragmentPagerAdapter {

    public CollectionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        if (i == 0) {
            return FriendFeedFragment.newInstance(1);
        } else if (i == 1) {
            return ScanOverlayFragment.newInstance();
        } else if (i == 2) {
            return PaymentInitFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

}
