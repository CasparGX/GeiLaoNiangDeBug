package com.casparx.geilaoniangdebug;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity {


    @Bind(R.id.login_progress)
    ProgressBar loginProgress;
    @Bind(R.id.password)
    EditText password;
    @Bind(R.id.email_sign_in_button)
    Button emailSignInButton;
    @Bind(R.id.email_login_form)
    LinearLayout emailLoginForm;
    @Bind(R.id.login_form)
    ScrollView loginForm;

    @OnClick(R.id.email_sign_in_button) void login(){
        if (password.getText().toString().equals("950320")){
            Intent intent = new Intent();
            intent.setClass(this,MainActivity.class);
            startActivity(intent);
            finish();
        } else {

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

    }

}

