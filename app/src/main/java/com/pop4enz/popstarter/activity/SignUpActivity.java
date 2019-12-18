package com.pop4enz.popstarter.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.pop4enz.popstarter.R;
import com.pop4enz.popstarter.model.ApiResponse;
import com.pop4enz.popstarter.model.LoginRequest;
import com.pop4enz.popstarter.model.SignUpRequest;
import com.pop4enz.popstarter.model.TokenResponse;
import com.pop4enz.popstarter.retrofit.RetrofitService;
import com.pop4enz.popstarter.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends NavigationActivity implements View.OnClickListener{

    public static final String BAD_CREDENTIALS = "Wrong E-mail or passwordET!";
    public static final String ERROR = "Something went wrong :(";
    public static final String REGISTER_SUCCESS = "User registered successfully! You can log in now!";

    private EditText firstNameET;
    private EditText lastNameET;
    private EditText usernameET;
    private EditText emailET;
    private EditText passwordET;
    private Button signUpButton;

    private SharedPreferences storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_sign_up);
        super.onCreate(savedInstanceState);

        firstNameET = findViewById(R.id.signUpFirstName);
        lastNameET = findViewById(R.id.signUpLastName);
        usernameET = findViewById(R.id.signUpUsername);
        emailET = findViewById(R.id.signUpEmail);
        passwordET = findViewById(R.id.signUpPassword);
        signUpButton = findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(this);

        storage = this.getStorage();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.signUpButton) {

            String firstName = firstNameET.getText().toString();
            String lastName = lastNameET.getText().toString();
            String username = usernameET.getText().toString();
            String email = emailET.getText().toString();
            String password = passwordET.getText().toString();

            if (Utils.isNotEmpty(firstName) && Utils.isNotEmpty(lastName)
                    && Utils.isNotEmpty(username) && Utils.isNotEmpty(email)
                    && Utils.isNotEmpty(password)) {
                try {
                    RetrofitService.getInstance()
                            .getJSONApi().signUpRequest(new SignUpRequest(firstName, lastName,
                            username, email, password))
                            .enqueue(new Callback<ApiResponse>() {
                                @Override
                                public void onResponse(Call<ApiResponse> call,
                                                       Response<ApiResponse> response) {
                                    if (response.raw().isSuccessful()) {
                                        ApiResponse response1 = response.body();
                                        if (response1.getSuccess()) {
                                            handleRegister();
                                        }
                                    } else if (response.raw().code() == 401) {
                                        Utils.Toast(SignUpActivity.this, BAD_CREDENTIALS);
                                    } else {
                                        Utils.Toast(SignUpActivity.this, ERROR);
                                    }
                                }

                                @Override
                                public void onFailure(Call<ApiResponse> call, Throwable t) {
                                    t.printStackTrace();
                                    Utils.Toast(SignUpActivity.this, ERROR);
                                }
                            });
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void handleRegister() {
        Utils.Toast(SignUpActivity.this, REGISTER_SUCCESS);
        Intent intent = new Intent(this, CampaignListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}
