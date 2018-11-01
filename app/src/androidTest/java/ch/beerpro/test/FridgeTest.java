package ch.beerpro.test;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.annotation.IdRes;
import androidx.appcompat.app.ActionBar;
import androidx.test.InstrumentationRegistry;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.runner.AndroidJUnit4;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.Until;
import ch.beerpro.R;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class FridgeTest {
    private static final String PACKAGE = "ch.beerpro";
    private static final int TIMEOUT = 20_000;
    private UiDevice device;

    @Before
    public void startMainActivityFromHomeScreen() {
        // Initialize UiDevice instance
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        // Start from the home screen
        device.pressHome();

        // Wait for launcher
        final String launcherPackage = device.getLauncherPackageName();
        assertThat(launcherPackage, notNullValue());
        device.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), 5000);

        // Launch the app
        Context context = ApplicationProvider.getApplicationContext();
        final Intent intent = context.getPackageManager().getLaunchIntentForPackage(PACKAGE);
        // Clear out any previous instances
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);

        // Wait for the app to appear
        device.wait(Until.hasObject(By.pkg(PACKAGE).depth(0)), 5000);
    }

    /**
     * Adds a beer to the fridge, then increases the counter of the beer.
     */
    @Test
    public void addBeersToFridge() throws InterruptedException {
        clickOn("Bier suchen");
        clickOn("Quöllfrisch");
        clickOn("Quöllfrisch Hell");
        clickOn("AKTIONEN");
        clickOn("IN DEN KÜHLSCHRANK");
        pressBack();
        pressBack();

        clickOnMyProfile();
        clickOn("Kühlschrank");

        // The fridge now should contain the added item
        device.wait(Until.hasObject(By.text("Quöllfrisch Hell")), TIMEOUT);

        clickOnAddOneButton("Quöllfrisch Hell");
        device.wait(Until.hasObject(By.text("2")), TIMEOUT);
        pressBack();

        assertEquals("1", textOf(R.id.myFridgeCount));
    }

    private void clickOn(String text) {
        device.wait(Until.hasObject(By.text(text)), TIMEOUT);
        device.findObject(By.text(text)).click();
    }

    private void clickOnMyProfile() {
        Resources resources = ApplicationProvider.getApplicationContext().getResources();
        String tabLayout = resources.getResourceName(R.id.tablayout);
        device.wait(Until.hasObject(By.res(tabLayout)), TIMEOUT);
        device.findObject(By.res(tabLayout))
                .findObjects(By.clazz(ActionBar.Tab.class)).get(2)
                .click();
    }

    private void clickOnAddOneButton(String beerName) {
        UiObject2 listEntry = device.findObject(By.text(beerName)).getParent().getParent();
        Resources resources = ApplicationProvider.getApplicationContext().getResources();
        String addOneToFridge = resources.getResourceName(R.id.addOneToFridge);
        listEntry.findObject(By.res(addOneToFridge)).click();
    }

    private void clickOn(@IdRes int id) {
        String res = ApplicationProvider.getApplicationContext().getResources().getResourceName(id);
        device.wait(Until.hasObject(By.res(res)), TIMEOUT);
        device.findObject(By.res(res)).click();
    }

    private String textOf(@IdRes int id) {
        String res = ApplicationProvider.getApplicationContext().getResources().getResourceName(id);
        device.wait(Until.hasObject(By.res(res)), TIMEOUT);
        return device.findObject(By.res(res)).getText();
    }

    private void pressBack() throws InterruptedException {
        // Ensure that the emulator isn't overworked
        Thread.sleep(5_000);

        device.pressBack();
    }
}
