package ch.beerpro.presentation.details;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import ch.beerpro.data.repositories.*;
import ch.beerpro.domain.models.Beer;
import ch.beerpro.domain.models.FridgeItem;
import ch.beerpro.domain.models.Rating;
import ch.beerpro.domain.models.Wish;
import com.google.android.gms.tasks.Task;

import java.util.List;

public class DetailsViewModel extends ViewModel implements CurrentUser {

    private final MutableLiveData<String> beerId = new MutableLiveData<>();
    private final LiveData<Beer> beer;
    private final LiveData<List<Rating>> ratings;
    private final LiveData<Wish> wish;
    private final LiveData<FridgeItem> fridgeItem;

    private final LikesRepository likesRepository;
    private final WishlistRepository wishlistRepository;
    private final FridgeRepository fridgeRepository;

    public DetailsViewModel() {
        // TODO We should really be injecting these!
        BeersRepository beersRepository = new BeersRepository();
        RatingsRepository ratingsRepository = new RatingsRepository();
        likesRepository = new LikesRepository();
        wishlistRepository = new WishlistRepository();
        fridgeRepository = new FridgeRepository();

        MutableLiveData<String> currentUserId = new MutableLiveData<>();
        beer = beersRepository.getBeer(beerId);
        wish = wishlistRepository.getMyWishForBeer(currentUserId, getBeer());
        fridgeItem = fridgeRepository.getMyFridgeItemsForBeer(currentUserId, getBeer());

        ratings = ratingsRepository.getRatingsForBeer(beerId);
        currentUserId.setValue(getCurrentUser().getUid());
    }

    public LiveData<Beer> getBeer() {
        return beer;
    }

    public LiveData<Wish> getWish() {
        return wish;
    }

    public LiveData<List<Rating>> getRatings() {
        return ratings;
    }

    public void setBeerId(String beerId) {
        this.beerId.setValue(beerId);
    }

    public void toggleLike(Rating rating) {
        likesRepository.toggleLike(rating);
    }

    public Task<Void> toggleItemInWishlist(String itemId) {
        return wishlistRepository.toggleUserWishlistItem(getCurrentUser().getUid(), itemId);
    }

    public Task<Void> addItemToFridge(String itemId) {
        return fridgeRepository.addUserFridgeListItem(getCurrentUser().getUid(), itemId);
    }
}