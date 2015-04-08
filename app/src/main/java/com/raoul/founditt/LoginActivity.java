package com.raoul.founditt;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;


public class LoginActivity extends Activity {
    EditText usernameedittext;
    EditText passwordedittext;
    private String username;
    private String password;
    private Dialog progerss;
    Typeface font;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usernameedittext=(EditText)findViewById(R.id.username_logineditText);
        passwordedittext=(EditText)findViewById(R.id.password_logineditText);
        font = Typeface.createFromAsset(getAssets(), "Questrial-Regular.ttf");
        usernameedittext.setTypeface(font);

        ImageButton loginstartButton = (ImageButton) findViewById(R.id.login_loginimageButton);
        loginstartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.this.progerss = ProgressDialog.show(
                LoginActivity.this, "", "Loging in...", true);
                username=usernameedittext.getText().toString();
                password=passwordedittext.getText().toString();
                ParseUser.logInInBackground(username, password, new LogInCallback() {
                    public void done(ParseUser user, ParseException e) {
                        LoginActivity.this.progerss.dismiss();
                        if (user != null) {
                            LoginActivity.this.progerss.dismiss();
                            Intent intent;
                            intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                        } else {
                            try {
                                showDialog("Current user don't exit for our app!");
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                            // Signup failed. Look at the ParseException to see what happened.
                        }
                    }
                });


            }
        });
    }


    public void showDialog(String msg) throws Exception
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);

        builder.setMessage(msg);

//        builder.setPositiveButton("Ring", new DialogInterface.OnClickListener()
//        {
//            @Override
//            public void onClick(DialogInterface dialog, int which)
//            {
////                Intent callIntent = new Intent(Intent.ACTION_DIAL);// (Intent.ACTION_CALL);
////
////                callIntent.setData(Uri.parse("tel:" + phone));
////
////                startActivity(callIntent);
//
//                dialog.dismiss();
//            }
//        });

        builder.setNegativeButton("close", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });

        builder.show();
    }



}
