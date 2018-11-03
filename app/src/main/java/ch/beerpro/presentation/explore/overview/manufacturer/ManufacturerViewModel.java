package ch.beerpro.presentation.explore.overview.manufacturer;

import android.util.Pair;

import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import ch.beerpro.data.repositories.BeersRepository;
import ch.beerpro.data.repositories.CurrentUser;
import ch.beerpro.data.repositories.WishlistRepository;
import ch.beerpro.domain.models.Beer;

import static androidx.lifecycle.Transformations.map;
import static ch.beerpro.domain.utils.LiveDataExtensions.zip;

public class ManufacturerViewModel extends ViewModel implements CurrentUser {

    private static final String TAG = "ManufacturerViewModel";
    private final MutableLiveData<String> searchTerm = new MutableLiveData<>();
    private final LiveData<List<Beer>> filteredBeers;
    private final BeersRepository beersRepository;
    private final WishlistRepository wishlistRepository;

    public ManufacturerViewModel() {
        beersRepository = new BeersRepository();
        wishlistRepository = new WishlistRepository();
        filteredBeers = map(zip(searchTerm, getAllBeers()), ManufacturerViewModel::filter);
    }

    public LiveData<List<Beer>> getAllBeers() {
        return beersRepository.getAllBeers();
    }

    private static List<Beer> filter(Pair<String, List<Beer>> input) {
        String searchTerm1 = input.first;
        List<Beer> allBeers = input.second;
        if (Strings.isNullOrEmpty(searchTerm1)) {
            return allBeers;
        }
        if (allBeers == null) {
            return Collections.emptyList();
        }
        ArrayList<Beer> filtered = new ArrayList<>();
        for (Beer beer : allBeers) {
            if (beer.getManufacturer().toLowerCase().contains(searchTerm1.toLowerCase())) {
                filtered.add(beer);
            }
        }
        return filtered;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm.setValue(searchTerm);
    }

    public LiveData<List<Beer>> getFilteredBeers() {
        return filteredBeers;
    }


    public void toggleItemInWishlist(String beerId) {
        wishlistRepository.toggleUserWishlistItem(getCurrentUser().getUid(), beerId);
    }
}