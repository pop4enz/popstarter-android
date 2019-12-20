package com.pop4enz.popstarter.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.widget.Toolbar;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.pop4enz.popstarter.R;
import com.pop4enz.popstarter.model.UserInfo;
import com.pop4enz.popstarter.retrofit.RetrofitService;
import com.pop4enz.popstarter.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import info.androidhive.fontawesome.FontDrawable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class NavigationActivity extends AppCompatActivity {

    private static final int HOME_ID = 2;
    private static final int PROFILE_ID = 1;
    private static final int LOGOUT_ID = 3;
    private static final int SIGN_IN_ID = 4;
    private static final int SIGN_UP_ID = 5;
    private static final int CREATE_CAMPAIGN = 6;
    public static final String LOGOUT_SUCCESS = "You successfully logged out!";
    public static final String USER_NAME = "userName";
    public static final String USER_EMAIL = "userEmail";
    public static final String USER_FNAME = "userFName";
    public static final String USER_LNAME = "userLName";
    public static final String USER_ID = "userId";
    public static final String TOKEN = "token";

    private Drawer drawer;
    protected Boolean isUser;
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

    protected String getToken() {
        return Utils.buildToken(storage.getString(TOKEN, null));
    }

    private void setIsUser() {
        isUser = storage.getString(TOKEN, null) != null;
        if (isUser && storage.getString(USER_NAME, null) == null) {
            getUserInfo();
        }
    }

    protected void getUserInfo() {
        RetrofitService.getInstance().getApiRequests().getUserInfo(getToken())
                .enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(@NonNull Call<UserInfo> call, @NonNull Response<UserInfo> response) {
                UserInfo user = response.body();
                if (response.isSuccessful()) {
                    storage.edit().putInt(USER_ID, user.getId()).apply();
                    storage.edit().putString(USER_NAME, user.getUsername()).apply();
                    storage.edit().putString(USER_FNAME, user.getFirst_name()).apply();
                    storage.edit().putString(USER_LNAME, user.getLast_name()).apply();
                    storage.edit().putString(USER_EMAIL, user.getEmail()).apply();
                    buildDrawer();
                    Log.d(Utils.TAG, Utils.USER_GET_SUCCESS);
                } else {
                    Log.e(Utils.TAG, Utils.ERROR);
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserInfo> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setIsUser();
        buildDrawer();
    }

    private void buildDrawer() {

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
                        case CREATE_CAMPAIGN:
                            createCampaignClickHandler();
                            break;
                        default:
                            return false;
                    }
                    return false;
                }).build();

        for (IDrawerItem item : items) {
            drawer.addItem(item);
        }

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
                .withName(storage.getString(USER_NAME, "undefined"))
                .withEmail(storage.getString(USER_EMAIL,
                        "undefined"))
                .withIdentifier(0);
    }


    private List<IDrawerItem> loadItems() {
        List<IDrawerItem> items = new ArrayList<>();
        items.add(buildPrimaryItem(R.string.drawer_item_home, R.string.fa_home_solid, HOME_ID));
        if (isUser) {
            items.add(buildPrimaryItem(R.string.drawer_item_create_campaign,
                    R.string.fa_plus_solid, CREATE_CAMPAIGN));
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

    private void createCampaignClickHandler() {
        if (!(this instanceof EditCampaignActivity)) {
            Intent intent = new Intent(this, EditCampaignActivity.class);
            startActivity(intent);
        }
    }

    private void signUpClickHandler() {
        if (!(this instanceof SignUpActivity)) {
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
        }
    }

    private void homeClickHandler() {
        if (!(this instanceof HomeActivity)) {
            Intent intent = new Intent(this, HomeActivity.class);
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

    private void logoutClickHandler() {
        if (isUser) {
            storage.edit().clear().apply();
            isUser = false;
            Utils.Toast(this, LOGOUT_SUCCESS);
            Intent intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
