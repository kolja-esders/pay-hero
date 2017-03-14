package edu.pietro.team.payhero;


import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

public class CollectionPagerAdapter extends FragmentPagerAdapter {

    FriendFeedFragment fff = FriendFeedFragment.newInstance(1);
    ScanOverlayFragment scf = ScanOverlayFragment.newInstance();
    PaymentInitFragment pif = PaymentInitFragment.newInstance();

    public CollectionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        if (i == 0) {
            return fff;
        } else if (i == 1) {
            return scf;
        } else if (i == 2) {
            return pif;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

}
