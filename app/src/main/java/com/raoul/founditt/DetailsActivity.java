package com.raoul.founditt;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.raoul.founditt.ImageLoadPackge.ImageLoader;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;


public class DetailsActivity extends Activity {
    public TextView usernametextview;
    public TextView expiredatetextview;
    public TextView taglinetextview;
    public TextView liketextview;
    public ImageView productimageview;
    public ImageView userimageview;
    public ImageButton likebutimagebut;
    public ImageButton commentimagebut;
    public ImageButton shareimagebut;
    public ImageButton followmagebut;
    public ImageButton moreimagebut;
    public TextView userrealname_textview;
    public TextView commenttextview;
    ParseObject photo;
    ParseUser touser;
    private boolean flag=true;
    ProgressDialog mProgressDialog;
    private boolean likeflag;
    private boolean followfalg;
    private int likenumber;
    private int commentnumber;
    ParseUser currentuser;
    Typeface font;
    ArrayList<String> likes;
    ArrayList<String> follows;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        likes = new ArrayList<String>();
        follows = new ArrayList<String>();
        currentuser=ParseUser.getCurrentUser();
        usernametextview=(TextView)findViewById(R.id.username_details_textView);
        expiredatetextview=(TextView)findViewById(R.id.expierdate_details_textView);
        taglinetextview=(TextView)findViewById(R.id.nike_details_textView);
        liketextview=(TextView)findViewById(R.id.likenumber_details_textView);
        userrealname_textview=(TextView)findViewById(R.id.name_details_textView);
        commenttextview=(TextView)findViewById(R.id.commentnumber_details_extView);
        productimageview=(ImageView)findViewById(R.id.deal_details_imageView);
        userimageview=(ImageView)findViewById(R.id.userphoto_details_imageView);
        likebutimagebut=(ImageButton)findViewById(R.id.like_details_imageButton);
        commentimagebut=(ImageButton)findViewById(R.id.comment_details_imageButton);
        shareimagebut=(ImageButton)findViewById(R.id.share_details_imageButton);
        followmagebut=(ImageButton)findViewById(R.id.follow_details_imageButton);
        moreimagebut=(ImageButton)findViewById(R.id.more_details_imageButton);
        font = Typeface.createFromAsset(getAssets(), "Questrial-Regular.ttf");
        photo=new Photo();
        TextView details_title=(TextView)findViewById(R.id.details_tiltle_textView);
        details_title.setTypeface(font);
        usernametextview.setTypeface(font);
        expiredatetextview.setTypeface(font);
        taglinetextview.setTypeface(font);
        liketextview.setTypeface(font);
        userrealname_textview.setTypeface(font);
        commenttextview.setTypeface(font);
        Intent getdataintente=getIntent();
//        final ImageButton followButton = (ImageButton) findViewById(R.id.follow_commentimageButton);
        final String photoID=getdataintente.getStringExtra("photoID");
        final ImageLoader imageLoader=new ImageLoader(DetailsActivity.this);
        mProgressDialog = new ProgressDialog(DetailsActivity.this);
        // Set progressdialog title
        mProgressDialog.setTitle("Message");
        // Set progressdialog message
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setIndeterminate(false);
        // Show progressdialog
        mProgressDialog.show();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Photo");
        query.include("user");
        query.getInBackground(photoID, new GetCallback<ParseObject>() {
            public void done(ParseObject country, ParseException e) {
                if (e == null) {
                    ParseFile image=(ParseFile)country.get("image");
                    image.saveInBackground();
                    photo=country;
                    touser=country.getParseUser("user");
                    imageLoader.DisplayImage(image.getUrl(),productimageview);
                    ParseFile userimage = (ParseFile) touser.get("userphoto");
                    imageLoader.DisplayImage(userimage.getUrl(),userimageview);
                    userrealname_textview.setText((String) touser.get("name"));
                    usernametextview.setText(touser.getUsername());
                    expiredatetextview.setText("EXP " + (String) country.get("expiry"));
                    taglinetextview.setText((String) country.get("tagline"));
                    if(!(photo.<String>getList("userlike") ==null)){
                        likes.addAll(photo.<String>getList("userlike"));
                    }

                    //photosFromCurrentUserQuery.include("user.userphoto");
                    //photosFromCurrentUserQuery.whereExists("user");
                    likenumber=likes.size();
                    commentnumber=photo.getInt("commentnumber");

                    liketextview.setText(Integer.toString(likenumber)+" LIKES");



                    //photosFromCurrentUserQuery.include("user.userphoto");
                    //photosFromCurrentUserQuery.whereExists("user");

//                    try {
//                        com_ob = commentsizeQuery .find();
//                    } catch (ParseException e1) {
//                        e1.printStackTrace();
//                    }
//                    if ( com_ob.size() == 0) {
//                        map.setComment("");
//                    }
//                    map.setComment(Integer.toString(com_ob.size()));


                    commenttextview.setText(Integer.toString(commentnumber)+" COMMENTS");


                    ParseQuery<ParseObject> followquery = ParseQuery.getQuery("Follow");
                    followquery.whereEqualTo("user", ParseUser.getCurrentUser());
                    followquery.whereEqualTo("photo", photo);
                    followquery.getFirstInBackground(new GetCallback<ParseObject>() {
                        public void done(ParseObject object, ParseException e) {
                            if (object == null) {
                                followmagebut.setImageResource(R.drawable.favoer);
                                followfalg=false;
                                Log.d("score", "The getFirst request failed.");
                            } else {
                                followmagebut.setImageResource(R.drawable.favoer_press);
                                followfalg=true;
                            }
                        }
                    });
//                    ParseQuery<ParseObject> likequery = ParseQuery.getQuery("Like");
//                    likequery.whereEqualTo("user", ParseUser.getCurrentUser());
//                    likequery.whereEqualTo("photo", photo);
//                    likequery.getFirstInBackground(new GetCallback<ParseObject>() {
//                        public void done(ParseObject object, ParseException e) {
//                            if (object == null) {
//                                likebutimagebut.setImageResource(R.drawable.hearth);
//                                likeflag=false;
//                                Log.d("score", "The getFirst request failed.");
//                            } else {
//                                likebutimagebut.setImageResource(R.drawable.hearth_press);
//                                likeflag=true;
//
//                            }
//                        }
//                    });
                    String likecompare=likes.toString();
                    if(likecompare.contains(currentuser.getObjectId())){
                     likebutimagebut.setImageResource(R.drawable.hearth_press);
                     likeflag=true;
                    }
                    else {
                          likebutimagebut.setImageResource(R.drawable.hearth);
                          likeflag=false;
                    }
                    mProgressDialog.dismiss();
                    //  ok = true;
//                    mProgressDialog.dismiss();
                }

            }
        });

        shareimagebut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productimageview.buildDrawingCache();
                Bitmap bm =productimageview.getDrawingCache();

                Intent imageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File imagesFolder = new File(Environment.getExternalStorageDirectory(), "Punch");
                imagesFolder.mkdirs();
                String fileName = "image"  + ".jpg";
                File output = new File(imagesFolder, fileName);
                Uri uriSavedImage = Uri.fromFile(output);
                imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
                OutputStream fos = null;

                try {
                    fos = getContentResolver().openOutputStream(uriSavedImage);
                    bm.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                    fos.close();
                }
                catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                finally
                {}

                Intent intent = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_SUBJECT, ParseUser.getCurrentUser().getUsername());

                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(output));
                intent.setType("image/*");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                startActivity(Intent.createChooser(intent, null));
            }
        });

        likebutimagebut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(likeflag){
                    likebutimagebut.setImageResource(R.drawable.hearth);
//                    photo.removeAll("userlike", likes);
//
//                    try {
//                        photo.save();
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
                    likes.remove(currentuser.getObjectId());
                    photo.put("userlike",likes);
                    photo.saveInBackground();
//                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Like");
//                    query.whereEqualTo("user", ParseUser.getCurrentUser());
//                    query.whereEqualTo("photo", photo);
//
//                    mProgressDialog.show();
//                    query.getFirstInBackground(new GetCallback<ParseObject>() {
//                        public void done(ParseObject object, ParseException e) {
//                            if (object == null) {
//                                Log.d("score", "The getFirst request failed.");
//                            } else {
//                                object.deleteInBackground();
//                            }
//
//                            mProgressDialog.dismiss();
//                        }
//                    });

                    int tempunlike=likenumber-1;
                    photo.put("likenumber",tempunlike);


                    liketextview.setText(Integer.toString(tempunlike));
                    photo.saveInBackground();
                    ParseObject alert=new ParseObject("Alert");
                    alert.put("fromuser",ParseUser.getCurrentUser());
                    alert.put("contente", ParseUser.getCurrentUser().getUsername()+" unlike!");
                    alert.put("photo",photo);
                    alert.put("touser",touser);
                    alert.saveInBackground();
//                    ParsePush push = new ParsePush();
//                    push.setChannel("Founditt");
//
//                    push.setMessage("The Giants just scored! It's now 2-2 against the Mets.");
//                    push.sendInBackground();


                    ParseQuery pushQuery = ParseInstallation.getQuery();
                    pushQuery.whereEqualTo("user", touser);
                    pushQuery.whereEqualTo("notification","true");
// Send push notification to query
                    ParsePush push = new ParsePush();
                    push.setQuery(pushQuery);
// Set our Installation query
                    push.setMessage("From "+ParseUser.getCurrentUser().getUsername()+" Unlike!:\n");
                    push.sendInBackground();
                    likeflag=false;
                }
                else{
                    likebutimagebut.setImageResource(R.drawable.hearth_press);
                    String userid=currentuser.getObjectId();
                    photo.add("userlike",Arrays.asList(userid));
                    photo.saveInBackground();
//                    ParseObject follow = new ParseObject("Like");
//                    follow.put("user", ParseUser.getCurrentUser());
//                    follow.put("photo", photo);
//                    follow.saveInBackground();
                    ParseObject alert=new ParseObject("Alert");
                    alert.put("fromuser",ParseUser.getCurrentUser());
                    alert.put("contente",ParseUser.getCurrentUser().getUsername()+" like your product");
                    alert.put("photo",photo);
                    alert.put("touser",touser);
                    alert.saveInBackground();
//                    ParsePush push = new ParsePush();
//                    push.setChannel("Founditt");
//
//                    push.setMessage("The Giants just scored! It's now 2-2 against the Mets.");
//                    push.sendInBackground();

                    int tempunlike=likenumber+1;
                    photo.put("likenumber",tempunlike);


                    liketextview.setText(Integer.toString(tempunlike));
                    photo.saveInBackground();
                    ParseQuery pushQuery = ParseInstallation.getQuery();
                    pushQuery.whereEqualTo("user", touser);
                    pushQuery.whereEqualTo("notification","true");
// Send push notification to query
                    ParsePush push = new ParsePush();
                    push.setQuery(pushQuery);
// Set our Installation query
                    push.setMessage("From "+ParseUser.getCurrentUser().getUsername()+" like your product\n");
                    push.sendInBackground();
                    likeflag=true;
                }
            }
        });



        followmagebut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(followfalg){
                    followmagebut.setImageResource(R.drawable.favoer);
                    mProgressDialog.show();
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Follow");
                    query.whereEqualTo("user", ParseUser.getCurrentUser());
                    query.whereEqualTo("photo", photo);
                    query.getFirstInBackground(new GetCallback<ParseObject>() {
                        public void done(ParseObject object, ParseException e) {
                            if (object == null) {
                                Log.d("score", "The getFirst request failed.");
                            } else {
                                object.deleteInBackground();
                                mProgressDialog.dismiss();
                            }

                        }
                    });


                    ParseObject alert=new ParseObject("Alert");
                    alert.put("fromuser",ParseUser.getCurrentUser());
                    alert.put("contente",ParseUser.getCurrentUser().getUsername()+" unfollow you");
                    alert.put("photo",photo);
                    alert.put("touser",touser);
                    alert.saveInBackground();
//                    ParsePush push = new ParsePush();
//                    push.setChannel("Founditt");
//
//                    push.setMessage("The Giants just scored! It's now 2-2 against the Mets.");
//                    push.sendInBackground();


                    ParseQuery pushQuery = ParseInstallation.getQuery();
                    pushQuery.whereEqualTo("user", touser);
                    pushQuery.whereEqualTo("notification","true");
// Send push notification to query
                    ParsePush push = new ParsePush();
                    push.setQuery(pushQuery);
// Set our Installation query
                    push.setMessage("From "+ParseUser.getCurrentUser().getUsername()+" unfollow you\n");
                    push.sendInBackground();

                    followfalg=flag;
                }
                else{
                    followmagebut.setImageResource(R.drawable.favoer_press);
                    ParseObject follow = new ParseObject("Follow");
                    follow.put("user", ParseUser.getCurrentUser());
                    follow.put("photo", photo);
                    follow.saveInBackground();
                    ParseObject alert=new ParseObject("Alert");
                    alert.put("fromuser",ParseUser.getCurrentUser());
                    alert.put("contente",ParseUser.getCurrentUser().getUsername()+" follow you");
                    alert.put("photo",photo);
                    alert.put("touser",touser);
                    alert.saveInBackground();
//                    ParsePush push = new ParsePush();
//                    push.setChannel("Founditt");
//
//                    push.setMessage("The Giants just scored! It's now 2-2 against the Mets.");
//                    push.sendInBackground();


                    ParseQuery pushQuery = ParseInstallation.getQuery();
                    pushQuery.whereEqualTo("user", touser);
                    pushQuery.whereEqualTo("notification","true");
// Send push notification to query
                    ParsePush push = new ParsePush();
                    push.setQuery(pushQuery);
// Set our Installation query
                    push.setMessage("From "+ParseUser.getCurrentUser().getUsername()+" follow you\n");
                    push.sendInBackground();
                    followfalg=true;
                }
            }
        });

        ImageButton backButton = (ImageButton) findViewById(R.id.back_details_imageButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;
                intent = new Intent(DetailsActivity.this, HomeActivity.class);


                startActivity(intent);
                overridePendingTransition(R.anim.left_in, R.anim.right_out);

            }
        });

        ImageButton fillterButton = (ImageButton) findViewById(R.id.home_details_imageButton);

        fillterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;
                intent = new Intent(DetailsActivity.this, HomeActivity.class);


                startActivity(intent);
                overridePendingTransition(R.anim.left_in, R.anim.right_out);

            }
        });

        ImageButton carmeraButton = (ImageButton) findViewById(R.id.camera_details_imageButton);

        carmeraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;
                intent = new Intent(DetailsActivity.this, CameraActivity.class);


                startActivity(intent);

            }
        });
        ImageButton alertButton = (ImageButton) findViewById(R.id.alert_details_imagebutton);

        alertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;
                intent = new Intent(DetailsActivity.this,AlarmActivity.class);


                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);

            }
        });
        ImageButton profileButton = (ImageButton) findViewById(R.id.profile_details_imageButton);

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;
                intent = new Intent(DetailsActivity.this, FavouritsActivity.class);


                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);

            }
        });


        moreimagebut.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                AlertDialog.Builder builderSingle = new AlertDialog.Builder(
                        DetailsActivity.this);

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                        DetailsActivity.this,
                        android.R.layout.select_dialog_singlechoice);
                arrayAdapter.add("Report inappropriate");
                arrayAdapter.add("Report expired");

                builderSingle.setNegativeButton("cancel",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                builderSingle.setAdapter(arrayAdapter,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final String strName = arrayAdapter.getItem(which);
                                AlertDialog.Builder builderInner = new AlertDialog.Builder(
                                        DetailsActivity.this);
                                builderInner.setMessage(strName);
                                builderInner.setPositiveButton("ageree",
                                        new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int which) {

                                                if(strName.equals("Report expired")){




                                                    ParseUser touser=photo.getParseUser("user");
                                                    ParseObject expire = new ParseObject("Expire");
                                                    expire.put("user", ParseUser.getCurrentUser());
                                                    expire.put("photo", photo);
                                                    expire.saveInBackground();
                                                    photo.put("expiry","EXPIRED");
                                                    photo.saveInBackground();
                                                    ParseObject alert=new ParseObject("Alert");
                                                    alert.put("fromuser",ParseUser.getCurrentUser());
                                                    alert.put("contente",ParseUser.getCurrentUser().getUsername()+" expire your product");
                                                    alert.put("photo",photo);
                                                    alert.put("touser",touser);
                                                    alert.saveInBackground();



                                                    ParseQuery pushQuery = ParseInstallation.getQuery();
                                                    pushQuery.whereEqualTo("user", touser);
                                                    pushQuery.whereEqualTo("notification","true");
// Send push notification to query
                                                    ParsePush push = new ParsePush();
                                                    push.setQuery(pushQuery);
// Set our Installation query
                                                    push.setMessage("From "+ParseUser.getCurrentUser().getUsername()+" expire your product");
                                                    push.sendInBackground();




                                                }
                                                else if(strName.equals("Report inappropriate")){





                                                    ParseUser touser=photo.getParseUser("user");
                                                    ParseObject expire = new ParseObject("Inappropriate");
                                                    expire.put("user", ParseUser.getCurrentUser());
                                                    expire.put("photo", photo);
                                                    expire.saveInBackground();
                                                    ParseQuery<ParseObject> inappequery = ParseQuery.getQuery("Inappropriate");
                                                    inappequery.whereEqualTo("photo", photo);
                                                    try {
                                                        photo.put("inapp",inappequery.count());
                                                    } catch (ParseException e1) {
                                                        e1.printStackTrace();
                                                    }
                                                    photo.saveInBackground();
                                                    ParseObject alert=new ParseObject("Alert");
                                                    alert.put("fromuser",ParseUser.getCurrentUser());
                                                    alert.put("contente",ParseUser.getCurrentUser().getUsername()+" inappropriate your product");
                                                    alert.put("photo",photo);
                                                    alert.put("touser",touser);
                                                    alert.saveInBackground();



                                                    ParseQuery pushQuery = ParseInstallation.getQuery();
                                                    pushQuery.whereEqualTo("user", touser);
                                                    pushQuery.whereEqualTo("notification","true");
// Send push notification to query
                                                    ParsePush push = new ParsePush();
                                                    push.setQuery(pushQuery);
// Set our Installation query
                                                    push.setMessage("From "+ParseUser.getCurrentUser().getUsername()+" inappropriate your product");
                                                    push.sendInBackground();




                                                }
                                                dialog.dismiss();
                                            }
                                        });
                                builderInner.show();
                            }
                        });
                builderSingle.show();


            }
        });

        commentimagebut.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent zoom = new Intent(DetailsActivity.this, CommentActivity.class);
                zoom.putExtra("photoID", photoID);
                startActivity(zoom);
                // Toast.makeText(HomeActivity.this,worldpopulationlist.get(position).getID(),Toast.LENGTH_SHORT).show();

                overridePendingTransition(R.anim.right_in, R.anim.left_out);

            }

        });
    }



}
