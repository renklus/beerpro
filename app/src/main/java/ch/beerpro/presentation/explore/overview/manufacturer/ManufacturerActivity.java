package ch.beerpro.presentation.explore.overview.manufacturer;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.beerpro.presentation.utils.ThemeHelpers;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import ch.beerpro.R;
import ch.beerpro.domain.models.Beer;
import ch.beerpro.presentation.details.DetailsActivity;
import ch.beerpro.presentation.profile.mybeers.OnMyBeerItemInteractionListener;

public class ManufacturerActivity extends AppCompatActivity
        implements ManufacturerOverviewFragment.OnItemSelectedListener, OnMyBeerItemInteractionListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private ManufacturerViewModel manufacturerViewModel;
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeHelpers.setTheme(this);
        setContentView(R.layout.activity_manufacturer);
        ButterKnife.bind(this);

        String manufacturer = getIntent().getExtras().getString("manufacturer");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(manufacturer);

        ViewPager viewPager = findViewById(R.id.viewpager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setSaveFromParentEnabled(false);
        manufacturerViewModel = ViewModelProviders.of(this).get(ManufacturerViewModel.class);

        handleSearch(manufacturer);
            }

    private void handleSearch(String text) {
        manufacturerViewModel.setSearchTerm(text);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onManufacturerOverviewListItemSelected(View animationSource, Beer item) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(DetailsActivity.ITEM_ID, item.getId());
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, animationSource, "image");
        startActivity(intent, options.toBundle());
    }

    @Override
    public void onMoreClickedListener(ImageView photo, Beer item) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(DetailsActivity.ITEM_ID, item.getId());
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, photo, "image");
        startActivity(intent, options.toBundle());
    }

    @Override
    public void onWishClickedListener(Beer item) {
        manufacturerViewModel.toggleItemInWishlist(item.getId());
    }
}
