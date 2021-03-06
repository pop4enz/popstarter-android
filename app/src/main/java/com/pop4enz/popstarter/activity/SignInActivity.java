package com.pop4enz.popstarter.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.pop4enz.popstarter.R;
import com.pop4enz.popstarter.model.LoginRequest;
import com.pop4enz.popstarter.model.TokenResponse;
import com.pop4enz.popstarter.retrofit.RetrofitService;
import com.pop4enz.popstarter.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends NavigationActivity implements View.OnClickListener {

    private EditText usernameET;
    private EditText passwordET;
    private SharedPreferences storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_sign_in);
        super.onCreate(savedInstanceState);
        usernameET = findViewById(R.id.signUpFirstName);
        passwordET = findViewById(R.id.signUpLastName);
        Button signInButton = findViewById(R.id.signInButton);
        signInButton.setOnClickListener(this);
        storage = this.getStorage();
    }

    @Override
    public void onClick(View v) {
        if (R.id.signInButton == v.getId()) {

            String username = usernameET.getText().toString();
            String password = passwordET.getText().toString();

            if (Utils.isNotEmpty(username) && Utils.isNotEmpty(password)) {
                try {
                    RetrofitService.getInstance()
                            .getApiRequests().signInRequest(new LoginRequest(username, password))
                            .enqueue(new Callback<TokenResponse>() {
                                @Override
                                public void onResponse(@NonNull Call<TokenResponse> call,
                                                       @NonNull Response<TokenResponse> response) {
                                    if (response.raw().isSuccessful()) {
                                        TokenResponse token = response.body();
                                        storage.edit().putString(NavigationActivity.TOKEN,
                                                token.getAccessToken()).apply();
                                        redirectHome();
                                    } else if (response.raw().code() == 401) {
                                        Utils.Toast(SignInActivity.this, Utils.BAD_CREDENTIALS);
                                    } else {
                                        Utils.Toast(SignInActivity.this, Utils.ERROR);
                                    }
                                }

                                @Override
                                public void onFailure(@NonNull Call<TokenResponse> call, @NonNull Throwable t) {
                                    t.printStackTrace();
                                    Utils.Toast(SignInActivity.this, Utils.ERROR);
                                }
                            });
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void redirectHome() {
        Utils.Toast(SignInActivity.this, Utils.LOGIN_SUCCESS);
        this.getUserInfo();
        this.finish();
    }

}
