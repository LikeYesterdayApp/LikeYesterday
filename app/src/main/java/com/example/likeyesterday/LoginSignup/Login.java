package com.example.likeyesterday.LoginSignup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.likeyesterday.MainActivity;
import com.example.likeyesterday.R;
import com.google.android.material.textfield.TextInputLayout;

public class Login extends AppCompatActivity {
    ImageView backButton;
    Button createUser,login;
    TextView titleText;
    TextInputLayout username,password;
    CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        backButton=findViewById(R.id.loginBackButton);
        createUser=findViewById(R.id.loginCreateUser);
        login=findViewById(R.id.loginButton);
        titleText=findViewById(R.id.loginTextView);
        username=findViewById(R.id.loginPhoneNumber);
        password=findViewById(R.id.loginpassword);
        checkBox=findViewById(R.id.rememberMeCheckBox);

    }

    public void  callSignUpScreen(View view){

        Intent intent= new Intent(getApplicationContext(),Signup.class);

        Pair[] pairs= new Pair[4];
        pairs[0]= new Pair<View,String>(backButton,"back_image_transition");
        pairs[1]= new Pair<View,String>(titleText,"createAccount_text_transition");
        pairs[2]= new Pair<View,String>(login,"next_button_transition");
        pairs[3]= new Pair<View,String>(createUser,"login_button_transition");

        ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(Login.this,pairs);
        startActivity(intent,options.toBundle());

    }

    public void login() {
        //move to next activity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void onLogin(View view){

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finishAffinity();
    }
}