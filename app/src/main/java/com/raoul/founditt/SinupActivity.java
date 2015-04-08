package com.raoul.founditt;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class SinupActivity extends Activity {
    EditText usernameedittext;
    EditText paswordeidttext;
    ImageButton singupbut;
    EditText useremail_edittext;
    private String username;
    private String password;
    private String useremail;
    private Dialog progerss;
    Typeface font;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sinup);
        font = Typeface.createFromAsset(getAssets(), "Questrial-Regular.ttf");
        useremail_edittext=(EditText)findViewById(R.id.mail_signeditText);
        usernameedittext=(EditText)findViewById(R.id.username_signeditText);
        paswordeidttext=(EditText)findViewById(R.id.password_signeditText);
        useremail_edittext.setTypeface(font);
        usernameedittext.setTypeface(font);

        singupbut=(ImageButton)findViewById(R.id.signup_button);
        singupbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username=usernameedittext.getText().toString();
                password=paswordeidttext.getText().toString();
                useremail=useremail_edittext.getText().toString();
                Log.d("username",username);
                Log.d("pawwrod",password);
                setSignup(username, password,useremail);
            }
        });

    }

    public void setSignup(String username,String password,String useremail) {
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(useremail);
        user.put("displayName",username);
        user.put("notification","true");
       // user.setEmail("email@example.com");
//
//// other fields can be set just like with ParseObject
//        user.put("phone", "650-253-0000");
        SinupActivity.this.progerss = ProgressDialog.show(
                SinupActivity.this, "", "Siging in...", true);
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    SinupActivity.this.progerss.dismiss();
                    // Hooray! Let them use the app now.
                    ParseInstallation installation = ParseInstallation.getCurrentInstallation();


                    installation.put("notification","true");




                    installation.saveInBackground();
                    Intent intent=new Intent(SinupActivity.this,HomeActivity.class);

                    startActivity(intent);
                } else {
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                }
            }
        });
    }
}
