package com.pop4enz.popstarter.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.pop4enz.popstarter.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import info.androidhive.fontawesome.FontDrawable;

public abstract class NavigationActivity extends AppCompatActivity {

    private static final int HOME_ID = 2;
    public static final int PROFILE_ID = 1;
    public static final int LOGOUT_ID = 3;
    public static final int SIGN_IN_ID = 4;
    public static final int SIGN_UP_ID = 5;

    private Drawer drawer;
    private Boolean isUser;
    private Toolbar toolbar;
    private SharedPreferences storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        storage = getApplicationContext()
                .getSharedPreferences("POPSTARTER", 0);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    private void setIsUser() {
        isUser = storage.getString(getString(R.string.TOKEN), null) != null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setIsUser();
        buildDrawer();
    }

    private void removeToken() {
        storage.edit().remove(getString(R.string.TOKEN)).apply();
    }

    private void logoutClickHandler() {
        if (isUser) {
            removeToken();
            isUser = false;
        }
        buildDrawer();
    }

    private Drawer buildDrawer() {

        List<IDrawerItem> items = loadItems();

        drawer = new DrawerBuilder().withActivity(this)
                .withToolbar(toolbar).withAccountHeader(buildDrawerHeader())
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    switch ((int) drawerItem.getIdentifier()) {
                        case PROFILE_ID:

                            break;
                        case HOME_ID:
                            homeClickHandler();
                            break;
                        case LOGOUT_ID:
                            logoutClickHandler();
                            break;
                        case SIGN_IN_ID:
                            signInClickHandler();
                            break;
                        case SIGN_UP_ID:
                            signUpClickHandler();
                            break;
                        default:
                            return false;
                    }
                    return false;
                }).build();

        for (IDrawerItem item : items) {
            drawer.addItem(item);
        }

        return drawer;
    }

    private AccountHeader buildDrawerHeader() {
        AccountHeader header;
        AccountHeaderBuilder builder = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.background6);
        if (isUser) {
            header = builder
                    .addProfiles(buildProfile()).build();
        } else {
            header = builder.build();
        }
        return header;
    }

    private ProfileDrawerItem buildProfile() {
        return new ProfileDrawerItem()
                .withName(storage.getString("userFirstName", "Pavel")
                        + " " + storage.getString("userLastName", "Lomako"))
                .withEmail(storage.getString("userEmail",
                        "Pavlik2012konez@gmail.com"));
    }


    private List<IDrawerItem> loadItems() {
        List<IDrawerItem> items = new ArrayList<>();
        items.add(buildPrimaryItem(R.string.drawer_item_home, R.string.fa_home_solid, HOME_ID));
        if (isUser) {
            items.add(buildPrimaryItem(R.string.drawer_item_profile,
                    R.string.fa_users_solid, PROFILE_ID));
            items.add(buildPrimaryItem(R.string.drawer_item_logout,
                    R.string.fa_door_open_solid, LOGOUT_ID));
        } else {
            items.add(buildPrimaryItem(R.string.drawer_item_sign_in,
                    R.string.fa_sign_in_alt_solid, SIGN_IN_ID));
            items.add(buildPrimaryItem(R.string.drawer_item_sign_up,
                    R.string.fa_user_plus_solid, SIGN_UP_ID));
        }
        return items;
    }

    private void signUpClickHandler() {
        if (!(this instanceof SignUpActivity)) {
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
        }
    }

    private void homeClickHandler() {
        if (!(this instanceof CampaignListActivity)) {
            Intent intent = new Intent(this, CampaignListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    private void signInClickHandler() {
        if (!(this instanceof SignInActivity)) {
            Intent intent = new Intent(this, SignInActivity.class);
            startActivity(intent);
        }
    }

    protected SharedPreferences getStorage() {
        return storage;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen()) {
            drawer.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    private PrimaryDrawerItem buildPrimaryItem(int nameId, int iconId, int id) {
        return new PrimaryDrawerItem()
                .withName(getString(nameId))
                .withIcon(getFADrawable(iconId)).withIdentifier(id);
    }

    private Drawable getFADrawable(int resourceId) {
        return new FontDrawable(this, resourceId, true, false);
    }
}
