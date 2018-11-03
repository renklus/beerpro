package ch.beerpro.presentation.explore.overview.category;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.beerpro.R;
import ch.beerpro.domain.models.Beer;
import ch.beerpro.presentation.details.DetailsActivity;
import ch.beerpro.presentation.explore.overview.ViewPagerAdapter;
import ch.beerpro.presentation.profile.mybeers.OnMyBeerItemInteractionListener;
import ch.beerpro.presentation.utils.ThemeHelpers;

public class CategoryActivity extends AppCompatActivity
        implements CategoryOverviewFragment.OnItemSelectedListener, OnMyBeerItemInteractionListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private CategoryViewModel categoryViewModel;
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeHelpers.setTheme(this);
        setContentView(R.layout.activity_category);
        ButterKnife.bind(this);

        String category = getIntent().getExtras().getString("category");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(category);

        ViewPager viewPager = findViewById(R.id.viewpager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setSaveFromParentEnabled(false);
        categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);

        handleSearch(category);
    }

    private void handleSearch(String text) {
        categoryViewModel.setSearchTerm(text);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCategoryOverviewListItemSelected(View animationSource, Beer item) {
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
        categoryViewModel.toggleItemInWishlist(item.getId());
    }
}
