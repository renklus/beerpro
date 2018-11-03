package ch.beerpro.presentation.explore.overview.category;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import ch.beerpro.R;
import ch.beerpro.domain.models.Beer;
import ch.beerpro.presentation.MainActivity;
import ch.beerpro.presentation.details.DetailsActivity;
import ch.beerpro.presentation.explore.overview.ViewPagerAdapter;
import ch.beerpro.presentation.profile.mybeers.OnMyBeerItemInteractionListener;

public class CategoryActivity extends AppCompatActivity
        implements CategoryOverviewFragment.OnItemSelectedListener, OnMyBeerItemInteractionListener {

    private CategoryViewModel categoryViewModel;
    private ViewPagerAdapter adapter;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        ViewPager viewPager = findViewById(R.id.viewpager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setSaveFromParentEnabled(false);
        categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);

        handleSearch(getIntent().getExtras().getString("category"));
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

    @Override
    public void onBackPressed()     {
        Intent intent = NavUtils.getParentActivityIntent(this);
        startActivity(intent);
    }
}
