package ch.beerpro.data.repositories;

import android.util.Pair;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import androidx.lifecycle.LiveData;
import ch.beerpro.domain.models.Beer;
import ch.beerpro.domain.models.Entity;
import ch.beerpro.domain.models.FridgeItem;
import ch.beerpro.domain.utils.FirestoreQueryLiveData;
import ch.beerpro.domain.utils.FirestoreQueryLiveDataArray;

import static androidx.lifecycle.Transformations.map;
import static androidx.lifecycle.Transformations.switchMap;
import static ch.beerpro.domain.utils.LiveDataExtensions.combineLatest;

public class FridgeRepository {
    private static LiveData<List<FridgeItem>> getFridgeByUser(String userId) {
        return new FirestoreQueryLiveDataArray<>(FirebaseFirestore.getInstance().collection(FridgeItem.COLLECTION)
                .orderBy(FridgeItem.FIELD_ADDED_AT, Query.Direction.DESCENDING).whereEqualTo(FridgeItem.FIELD_USER_ID, userId),
                FridgeItem.class);
    }

    private static LiveData<FridgeItem> getUserFridgeListFor(Pair<String, Beer> input) {
        String userId = input.first;
        Beer beer = input.second;
        DocumentReference document = FirebaseFirestore.getInstance().collection(FridgeItem.COLLECTION)
                .document(FridgeItem.generateId(userId, beer.getId()));
        return new FirestoreQueryLiveData<>(document, FridgeItem.class);
    }

    public Task<Void> addUserFridgeListItem(String userId, String itemId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String fridgeItemId = FridgeItem.generateId(userId, itemId);

        DocumentReference fridgeEntryQuery = db.collection(FridgeItem.COLLECTION).document(fridgeItemId);

        return fridgeEntryQuery.get().continueWithTask(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                Long amount = task.getResult().getLong(FridgeItem.FIELD_AMOUNT);
                return fridgeEntryQuery.set(new FridgeItem(userId, itemId, amount + 1, new Date()));
            } else if (task.isSuccessful()) {
                return fridgeEntryQuery.set(new FridgeItem(userId, itemId, 1, new Date()));
            } else {
                throw task.getException();
            }
        });
    }

    public Task<Void> removeUserFridgeListItem(String userId, String itemId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String fridgeItemId = FridgeItem.generateId(userId, itemId);

        DocumentReference fridgeEntryQuery = db.collection(FridgeItem.COLLECTION).document(fridgeItemId);

        return fridgeEntryQuery.get().continueWithTask(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                Long amount = task.getResult().getLong(FridgeItem.FIELD_AMOUNT);
                if (amount > 1) {
                    return fridgeEntryQuery.set(new FridgeItem(userId, itemId, amount - 1, new Date()));
                } else {
                    return fridgeEntryQuery.delete();
                }
            } else {
                throw task.getException();
            }
        });
    }

    public LiveData<List<Pair<FridgeItem, Beer>>> getMyFridgeWithBeers(LiveData<String> currentUserId,
                                                                   LiveData<List<Beer>> allBeers) {
        return map(combineLatest(getMyFridgeList(currentUserId), map(allBeers, Entity::entitiesById)), input -> {
            List<FridgeItem> fridgeItems = input.first;
            HashMap<String, Beer> beersById = input.second;

            ArrayList<Pair<FridgeItem, Beer>> result = new ArrayList<>();
            for (FridgeItem fridgeItem : fridgeItems) {
                Beer beer = beersById.get(fridgeItem.getBeerId());
                result.add(Pair.create(fridgeItem, beer));
            }
            return result;
        });
    }

    public LiveData<List<FridgeItem>> getMyFridgeList(LiveData<String> currentUserId) {
        return switchMap(currentUserId, FridgeRepository::getFridgeByUser);
    }


    public LiveData<FridgeItem> getMyFridgeItemsForBeer(LiveData<String> currentUserId, LiveData<Beer> beer) {
        return switchMap(combineLatest(currentUserId, beer), FridgeRepository::getUserFridgeListFor);
    }


}
