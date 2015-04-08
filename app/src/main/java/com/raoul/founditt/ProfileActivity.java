package com.raoul.founditt;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.kyleduo.switchbutton.switchbutton.Configuration;
import com.kyleduo.switchbutton.switchbutton.SwitchButton;
import android.widget.TextView;
import android.widget.Toast;

import com.raoul.founditt.ImageLoadPackge.ImageLoader;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;


public class ProfileActivity extends Activity {
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";
    private ProgressDialog progressDialog;
    ProgressDialog mProgressDialog;
    private Uri fileUri; // file url to store image/video
    EditText name_edit;
    EditText email_edit;
    EditText displayname_eidt;
    ImageButton tc_imagebut;
    ImageButton privace_imagebut;
    ImageButton logoutbut;
    ImageButton upgradebut;
    ImageButton saveimagebutton;
    String fileuri;
    ImageButton feedback_imagebut;
    ImageButton back_imagebut;
    ImageButton camera_imagebut;
    SwitchButton notivication_switch;
    SwitchButton facebook_switch;
    String curentusername;
    private ImageView imgPreview;
    String check_state;
    String facebook_checkstate;
    ParseFile image;
    Bitmap bitmap;
    byte[] saveData;
    ParseFile userphoto;
    Typeface font;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mProgressDialog = new ProgressDialog(ProfileActivity.this);
        // Set progressdialog title
        mProgressDialog.setTitle("Message");
        // Set progressdialog message
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setIndeterminate(false);
        Intent datagetintent = getIntent();
        fileuri = datagetintent.getStringExtra("serchresult");

        ParseUser current=ParseUser.getCurrentUser();
        current.saveInBackground();
        font = Typeface.createFromAsset(getAssets(), "Questrial-Regular.ttf");
        TextView title=(TextView)findViewById(R.id.profile_tiltle_textView);
        TextView facebook_title=(TextView)findViewById(R.id.faceook_title_textView);
        facebook_title.setTypeface(font);
        title.setTypeface(font);
        saveimagebutton=(ImageButton)findViewById(R.id.profile_trick_imageButton);
//        imgPreview = (ImageView) findViewById(R.id.userphoto_details_imageView);
        displayname_eidt=(EditText)findViewById(R.id.display_editText);
        displayname_eidt.setTypeface(font);
        name_edit=(EditText)findViewById(R.id.name_profile_editText);
        name_edit.setTypeface(font);
        TextView nametitle=(TextView)findViewById(R.id.name_title_textView);
        nametitle.setTypeface(font);
        email_edit=(EditText)findViewById(R.id.email_profile_editText);
        email_edit.setTypeface(font);
        TextView emailtitle=(TextView)findViewById(R.id.email_title_textView);
        emailtitle.setTypeface(font);
        TextView dis_title_textview=(TextView)findViewById(R.id.display_title_textView);
        dis_title_textview.setTypeface(font);
        notivication_switch=(SwitchButton)findViewById(R.id.notification_switch);
        facebook_switch=(SwitchButton)findViewById(R.id.facebook_switch);
        TextView notytitle=(TextView)findViewById(R.id.notification_title_textView);
        notytitle.setTypeface(font);
        back_imagebut=(ImageButton)findViewById(R.id.back_profile_imageButton);
        tc_imagebut=(ImageButton)findViewById(R.id.tcread_profile_imageButton);
        TextView tctitle=(TextView)findViewById(R.id.term_title_textView);
        tctitle.setTypeface(font);
        privace_imagebut=(ImageButton)findViewById(R.id.priceread_profileimageButton);
        TextView privacetitle=(TextView)findViewById(R.id.privacytextView);
        privacetitle.setTypeface(font);
        logoutbut=(ImageButton)findViewById(R.id.logout_imageButton);
        upgradebut=(ImageButton)findViewById(R.id.upgrade_imageButton);
        TextView feedtitle=(TextView)findViewById(R.id.feedback_textView);
        feedtitle.setTypeface(font);
        feedback_imagebut=(ImageButton)findViewById(R.id.feedback_imageButton);
//        camera_imagebut=(ImageButton)findViewById(R.id.phototake_profile_imageButton);
        final ParseUser currentuser=ParseUser.getCurrentUser();
        if((String)currentuser.get("name")==null){

            name_edit.setText("unknown");

        }
        else {
            name_edit.setText((String)currentuser.get("name"));
        }
        if(currentuser.getEmail()==null){

            email_edit.setText("undefined");

        }
        else {
            email_edit.setText(currentuser.getEmail());
        }
        displayname_eidt.setText((String)currentuser.get("displayName"));
//        email_edit.setText(currentuser.getEmail());
        String notificatoion_state=(String)currentuser.get("notification");
        if(!(notificatoion_state ==null)){
            if(notificatoion_state.equals("true")){

                notivication_switch.setChecked(true);
            }

            if(notificatoion_state.equals("false")){

                notivication_switch.setChecked(false);
            }
        }

        else {
            notivication_switch.setChecked(true);
        }
        if (currentuser.get("facebookId")==null){
            facebook_switch.setEnabled(false);
        }
        else {
            facebook_switch.setEnabled(true);
            String facebook_state=(String)currentuser.get("facebook");
            if(!(facebook_state ==null)){
                if(facebook_state.equals("true")){

                    facebook_switch.setChecked(true);
                }

                if(facebook_state.equals("false")){

                    facebook_switch.setChecked(false);
                }
            }

            else {
                facebook_switch.setChecked(true);
            }
        }


        ImageLoader imageLoader=new ImageLoader(ProfileActivity.this);
         userphoto=currentuser.getParseFile("userphoto");


//        ImageView userphoto_imageview=(ImageView)findViewById(R.id.userphoto_details_imageView);
//
//        if(bitmap==null){
//
//            if(!(userphoto ==null)){
//                imageLoader.DisplayImage(userphoto.getUrl(), userphoto_imageview);
//            }
//
//        }
//        else {
//            userphoto_imageview.setImageBitmap(bitmap);
//        }
        back_imagebut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;
                intent = new Intent(ProfileActivity.this, FavouritsActivity.class);


                startActivity(intent);
                overridePendingTransition(R.anim.left_in, R.anim.right_out);

            }
        });

//        camera_imagebut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//               Intent intent=new Intent(ProfileActivity.this,CameraActivity.class);
//                intent.putExtra("class","profile");
//               startActivity(intent);
//
//            }
//        });


        upgradebut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setProgressDialog( ProgressDialog.show(ProfileActivity.this, "",
                        "uploading  data ...", true, true) );


//                if(!(fileuri ==null)){
//                    BitmapFactory.Options options = new BitmapFactory.Options();
//                    bitmap = BitmapFactory.decodeFile(fileuri,
//                            options);
//
//                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
//                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
//                    saveData = bos.toByteArray();
//                }


                if(notivication_switch.isChecked()){
                    check_state="true";
                }
                else {
                    check_state="false";
                }



                ParseInstallation installation = ParseInstallation.getCurrentInstallation();


                installation.put("notification", check_state);



                installation.saveInBackground();
                ParseUser user = ParseUser.getCurrentUser();
                user.put("name",name_edit.getText().toString());
                user.setEmail(email_edit.getText().toString());
                user.put("notification",check_state);
                if (!(currentuser.get("facebookId") ==null)){

                    if(facebook_switch.isChecked()){
                        facebook_checkstate="true";
                    }
                    else {
                        facebook_checkstate="false";
                    }
                    user.put("facebook",facebook_checkstate);
                }
                user.put("displayName",displayname_eidt.getText().toString());
//               if(!(saveData ==null)){
//                    image = new ParseFile("photo.jpg", saveData);
//
//                       image.saveInBackground();
//
//                   user.put("userphoto",image);
//               }
//               else {
//                   user.put("userphoto",userphoto);
//              }
                user.saveInBackground(new SaveCallback() {

                    @Override
                    public void done(ParseException e) {
                        getProgressDialog().dismiss();
                       Intent intent=new Intent(ProfileActivity.this,FavouritsActivity.class);
                        //intent.putExtra("serchresult",fileuri);
                        startActivity(intent);
                        overridePendingTransition(R.anim.left_in, R.anim.right_out);
                        if (e == null) {

                        } else {

                        }
                    }

                });

            }
        });

        saveimagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setProgressDialog( ProgressDialog.show(ProfileActivity.this, "",
                        "uploading  data ...", true, true) );

//                if(!(fileuri ==null)){
//                    BitmapFactory.Options options = new BitmapFactory.Options();
//                    bitmap = BitmapFactory.decodeFile(fileuri,
//                            options);
//
//                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
//                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
//                    saveData = bos.toByteArray();
//                }














                if(notivication_switch.isChecked()){
                    check_state="true";
                }
                else {
                    check_state="false";
                }



                ParseInstallation installation = ParseInstallation.getCurrentInstallation();


                installation.put("notification", check_state);



                installation.saveInBackground();
                ParseUser user = ParseUser.getCurrentUser();
                if (!(currentuser.get("facebookId") ==null)){

                    if(facebook_switch.isChecked()){
                        facebook_checkstate="true";
                    }
                    else {
                        facebook_checkstate="false";
                    }
                    user.put("facebook",facebook_checkstate);
                }
                user.put("name",name_edit.getText().toString());
                user.setEmail(email_edit.getText().toString());
                user.put("notification",check_state);
                user.put("displayName",displayname_eidt.getText().toString());
//                if(!(saveData ==null)){
//                    image = new ParseFile("photo.jpg", saveData);
//
//                        image.saveInBackground();
//
//                    user.put("userphoto",image);
//                }
//               else {
//                   user.put("userphoto",userphoto);
//              }
                user.saveInBackground(new SaveCallback() {

                    @Override
                    public void done(ParseException e) {
                        getProgressDialog().dismiss();
                        Intent intent=new Intent(ProfileActivity.this,FavouritsActivity.class);
                        //intent.putExtra("serchresult",fileuri);
                        startActivity(intent);
                        overridePendingTransition(R.anim.left_in, R.anim.right_out);
                        if (e == null) {

                        } else {

                        }
                    }

                });

            }
        });


        ImageButton home_Button = (ImageButton) findViewById(R.id.home_profile_imageButton);

        home_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;
                intent = new Intent(ProfileActivity.this, HomeActivity.class);


                startActivity(intent);
                overridePendingTransition(R.anim.left_in, R.anim.right_out);

            }
        });

        ImageButton carmeraButton = (ImageButton) findViewById(R.id.camera_profile_imageButton);

        carmeraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;
                intent = new Intent(ProfileActivity.this, CameraActivity.class);


                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);

            }
        });
        ImageButton alertButton = (ImageButton) findViewById(R.id.alert_profile_imagebutton);

        alertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;
                intent = new Intent(ProfileActivity.this,AlarmActivity.class);


                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);

            }
        });
        ImageButton profileButton = (ImageButton) findViewById(R.id.profile_profile_imageButton);

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;
                intent = new Intent(ProfileActivity.this, FavouritsActivity.class);


                startActivity(intent);
                overridePendingTransition(R.anim.left_in, R.anim.right_out);

            }
        });
        logoutbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLogoutButtonClicked();
            }
        });

        ImageButton eamilbutton = (ImageButton) findViewById(R.id.feedback_imageButton);

        eamilbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendEmail();
            }
        });
    }

    protected void sendEmail() {
        Log.i("Send email", "");
        ParseUser user=ParseUser.getCurrentUser();
        String[] TO = {"charlesluo88@gmail.com"};
        String[] CC = {user.getEmail()};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");


        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Finished sending email...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(ProfileActivity.this,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void onLogoutButtonClicked() {
        // close this user's session
        if (!(curentusername == null)) {
            ParseFacebookUtils.getSession().closeAndClearTokenInformation();
            ParseUser.logOut();
        } else {
            ParseUser.logOut();
        }

        // Log the user out

        // Go to the login view
        startLoginActivity();
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public ProgressDialog getProgressDialog(){
        return progressDialog;
    }

    public void setProgressDialog(ProgressDialog pd){
        progressDialog = pd;
    }
    public void showDialog(String msg) throws Exception
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);

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
