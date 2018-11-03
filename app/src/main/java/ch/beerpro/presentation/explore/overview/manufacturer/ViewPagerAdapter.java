package ch.beerpro.presentation.explore.overview.manufacturer;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import ch.beerpro.presentation.profile.mybeers.MyBeersFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private static final String TAG = "ViewPagerAdapter";

    private Fragment manufacturerOverviewFragment;
    private Fragment myBeersFragment;

    private boolean hasChanged = false;

    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);
        manufacturerOverviewFragment = new ManufacturerOverviewFragment();
        myBeersFragment = new MyBeersFragment();
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                    return manufacturerOverviewFragment;
            case 1:
                return myBeersFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        // https://stackoverflow.com/questions/7746652/fragment-in-viewpager-using-fragmentpageradapter-is-blank-the-second-time-it-is
        if (object instanceof MyBeersFragment) {
            return POSITION_UNCHANGED;
        } else if (hasChanged) {
            this.hasChanged = false;
            return POSITION_NONE;
        } else {
            return POSITION_UNCHANGED;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "ALLE BIERE";
            case 1:
                return "MEINE BIERE";
        }
        return null;
    }
}