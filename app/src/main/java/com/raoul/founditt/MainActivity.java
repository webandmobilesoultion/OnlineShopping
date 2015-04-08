package com.raoul.founditt;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.parse.LogInCallback;
import com.parse.ParseACL;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.raoul.founditt.ImageLoadPackge.FileCache;
import com.raoul.founditt.ImageLoadPackge.MemoryCache;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends Activity {
    private Dialog progressDialog;
    String email;
    String name;
    String usernmae;
    String facebookid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageButton loginstartButton = (ImageButton) findViewById(R.id.already_imageButton);
        ImageButton facubookbut = (ImageButton) findViewById(R.id.twiter_imageButton);
        ImageButton singupbut = (ImageButton) findViewById(R.id.create_account_imageButton);
        ParseUser currentUser = ParseUser.getCurrentUser();
        new FileCache(this).clear();
        new MemoryCache().clear();
        if ((currentUser != null) && ParseFacebookUtils.isLinked(currentUser)) {
            // Go to the main photo list view activity
            showHomeListActivity();
        } else if (!(currentUser == null)) {
            showHomeListActivity();
        }
        // For push notifications
        ParseAnalytics.trackAppOpened(getIntent());
        loginstartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;
                intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        });
        facubookbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                onfacebookLoginButtonClicked();
                facebooklobin();
            }
        });
        singupbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SinupActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
    }

    private void onfacebookLoginButtonClicked() {
        MainActivity.this.progressDialog = ProgressDialog.show(
                MainActivity.this, "", "Logging in...", true);
        List<String> permissions = Arrays.asList("email", "public_profile", "user_about_me", "user_friends");
        ParseFacebookUtils.logIn(permissions, this, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                MainActivity.this.progressDialog.dismiss();
                if (user == null) {

                } else {
                    if (user.isNew()) {
//                    makeMeRequest();
//                    showHomeListActivity();
                    //Toast.makeText(MainActivity.this,"hello",Toast.LENGTH_LONG).show();
                    getFaceBookGraphObject();


                } else {
//                    makeMeRequest();
//                    showHomeListActivity();
                  // getFaceBookGraphObject();
                    showHomeListActivity();
                    //Toast.makeText(MainActivity.this,"bye",Toast.LENGTH_LONG).show();
                }
            }
            }
        });
    }
    public void facebooklobin(){

        List<String> permissions = Arrays.asList("email", "public_profile", "user_about_me", "user_friends");
        ParseFacebookUtils.logIn(permissions,
                MainActivity.this, new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
//                        if (isActivityDestroyed()) {
//                            return;
//                        }

                        if (user == null) {

                        } else if (user.isNew()) {
                            Request.newMeRequest(ParseFacebookUtils.getSession(),
                                    new Request.GraphUserCallback() {
                                        @Override
                                        public void onCompleted(GraphUser fbUser,
                                                                Response response) {
                      /*
                        If we were able to successfully retrieve the Facebook
                        user's name, let's set it on the fullName field.
                      */
                                            final ParseUser parseUser = ParseUser.getCurrentUser();
                                            if (fbUser != null && parseUser != null
                                                    && fbUser.getName().length() > 0) {
                                                parseUser.put("name", fbUser.getName());
                                                parseUser.put("facebookId",fbUser.getId());
                                                parseUser.put("displayName","");
                                                parseUser.setEmail((String) fbUser.getProperty("email"));
                                                parseUser.put("facebook","true");
                                                parseUser.saveInBackground(new SaveCallback() {
                                                    @Override
                                                    public void done(ParseException e) {
//                                                        if (e != null) {
//                                                            debugLog(getString(
//                                                                    R.string.com_parse_ui_login_warning_facebook_login_user_update_failed) +
//                                                                    e.toString());
//                                                        }
                                                        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                                                        installation.put("user", parseUser);
                                                        installation.put("notification", "true");
                                                        installation.saveInBackground();
                                                        showHomeListActivity();
                                                    }
                                                });
                                            }
                                            showHomeListActivity();
                                        }
                                    }
                            ).executeAsync();
                        } else {
                            showHomeListActivity();
                        }
                    }
                });

    }


    public void getFaceBookGraphObject(){


        Session session =  ParseFacebookUtils.getSession();

        Request request=Request.newMeRequest(session, new Request.GraphUserCallback() {

            @Override
            public void onCompleted(GraphUser user, Response response) {


                if(user != null){
                      facebookid=user.getId();
                      email=(String) user.getProperty("email");
                      usernmae="unknown";
                      name=user.getName();
                    ParseUser currentUser=ParseUser.getCurrentUser();
                    currentUser.put("facebookId", facebookid);
                    currentUser.put("displayName", name);
                    currentUser.put("name", name);
                    ParseACL rwACL = new ParseACL();
                    rwACL.setReadAccess(currentUser, true); // allow user to do reads
                    rwACL.setWriteAccess(currentUser, true);
                    currentUser.setACL(rwACL);
                    currentUser.setUsername(usernmae);
                    currentUser.setEmail(email);


                    try {
                        currentUser.save();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                    installation.put("user", currentUser);
                    installation.put("notification", "true");
                    installation.saveInBackground();
                    showHomeListActivity();
                    //registerUser(facebookid,name,email,usernmae);


                }

            }
        });
        request.executeAsync();
    }
    private  void registerUser(String facebookid,String name,String email,String usernmae){


        ParseUser currentUser=ParseUser.getCurrentUser();
        currentUser.put("facebookId", facebookid);
        currentUser.put("displayName", name);
        currentUser.put("name", name);
        ParseACL rwACL = new ParseACL();
        rwACL.setReadAccess(currentUser, true); // allow user to do reads
        rwACL.setWriteAccess(currentUser, true);
        currentUser.setACL(rwACL);
        currentUser.setUsername(usernmae);
        currentUser.setEmail(email);


        try {
            currentUser.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("user", currentUser);
        installation.put("notification", "true");
        installation.saveInBackground();
        showHomeListActivity();



    }
    private void showHomeListActivity() {
        //Log.i(AnypicApplication.TAG, "entered showHomeListActivity");
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                |Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish(); // This closes the login screen so it's not on the back stack
    }

}