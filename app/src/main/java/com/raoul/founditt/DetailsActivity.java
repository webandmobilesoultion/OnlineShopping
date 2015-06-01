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
import com.raoul.founditt.listmodel.homeitem;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;


public class DetailsActivity extends Activity {
    public TextView retailtextview;
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
    public ImageButton detailsimagebut;
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
    String photouserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        likes = new ArrayList<String>();
        follows = new ArrayList<String>();
        currentuser=ParseUser.getCurrentUser();




        detailsimagebut=(ImageButton)findViewById(R.id.back_details_imageButton);
        expiredatetextview = (TextView)findViewById(R.id.expierdate_hometextView);

        moreimagebut = (ImageButton)findViewById(R.id.more_imageButton);
        taglinetextview=(TextView)findViewById(R.id.niketextView);

        liketextview=(TextView)findViewById(R.id.likenumber_hometextView);
        retailtextview=(TextView)findViewById(R.id.Retailer_textView);
        productimageview=(ImageView)findViewById(R.id.deal_imageView);
        userimageview=(ImageView)findViewById(R.id.userphoto_details_imageView);
        likebutimagebut=(ImageButton)findViewById(R.id.like_home_imageButton);
        commentimagebut=(ImageButton)findViewById(R.id.comment_home_imageButton);
        shareimagebut=(ImageButton)findViewById(R.id.share_home_imageButton);
        followmagebut=(ImageButton)findViewById(R.id.follow_home_imageButton);
        userrealname_textview=(TextView)findViewById(R.id.name_hometextView);

        commenttextview=(TextView)findViewById(R.id.commentnumber_hometextView);




        font = Typeface.createFromAsset(getAssets(), "Questrial-Regular.ttf");
        photo=new Photo();
        TextView details_title=(TextView)findViewById(R.id.details_tiltle_textView);
        details_title.setTypeface(font);
        retailtextview.setTypeface(font);
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
        detailsimagebut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Photo");
        query.include("user");
        query.getInBackground(photoID, new GetCallback<ParseObject>() {
            public void done(ParseObject country, ParseException e) {
                if (e == null) {
                    photo=country;
                    final homeitem map = new homeitem();
                    int inappcount=country.getInt("inapp");
                    if (inappcount<4){

                        Date today=new Date(System.currentTimeMillis());
                        Date c = (Date) country.get("expiry");


                        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");

                        String formattedDate = df.format(c);
                        if(today.compareTo(c)==0){
                            expiredatetextview.setText("EXPIRED ");
                            ParseFile image = (ParseFile) country.get("image");
                            ParseUser homeuser=country.getParseUser("user");



                            photouserID=homeuser.getObjectId();
                            if(!((String) homeuser.get("name") ==null)){
                                userrealname_textview.setText((String) homeuser.get("name"));
                            }
                            else {
                                userrealname_textview.setText("");
                            }



                            taglinetextview.setText((String) country.get("tagline"));





                            if (!( homeuser.get("userphoto") ==null)){


                                if(!(homeuser.get("facebookId") ==null)){
                                    if (homeuser.get("facebook").equals("true")){

                                        String url="https://graph.facebook.com/"+homeuser.get("facebookId")+"/picture?type=large";
                                        Picasso.with(DetailsActivity.this).load(url).into(userimageview);
                                    }
                                    else {

                                        ParseFile userimage = (ParseFile) homeuser.get("userphoto");

                                        Picasso.with(DetailsActivity.this).load(userimage.getUrl()).into(userimageview);

                                    }

                                }

                                else {

                                    ParseFile userimage = (ParseFile) homeuser.get("userphoto");
                                    Picasso.with(DetailsActivity.this).load(userimage.getUrl()).into(userimageview);

                                }

                            }
                            else{
                                if(!(homeuser.get("facebookId") ==null)){
                                    String url="https://graph.facebook.com/"+homeuser.get("facebookId")+"/picture?type=large";
                                    Picasso.with(DetailsActivity.this).load(url).into(userimageview);
                                }
                                else {

                                }
                            }


                            Picasso.with(DetailsActivity.this).load(image.getUrl()).into(productimageview);

                            retailtextview.setText((String) country.get("retail"));
                            if(!(country.<String>getList("userlike") ==null)){
                                likes=(ArrayList<String>)country.get("userlike");
                            }
                            if(!(country.<String>getList("userfollow") ==null)){
                                follows=(ArrayList<String>)country.get("userfollow");

                            }
                            commenttextview.setText(Integer.toString(country.getInt("commentnumber")));
                            liketextview.setText(Integer.toString(likes.size()));
                            likenumber=likes.size();
                            String likecompare=likes.toString();
                            if(likecompare.contains(currentuser.getObjectId())){
                                 likeflag=true;
                                 likebutimagebut.setImageResource(R.drawable.like_banner_press);
                            }
                            else {
                                likebutimagebut.setImageResource(R.drawable.like_banner);
                                likeflag=false;
                            }
                            likes.clear();

                            String followcompare=follows.toString();
                            if(followcompare.contains(currentuser.getObjectId())){
                                followmagebut.setImageResource(R.drawable.favourites_press);
                                followfalg=true;
                            }
                            else {
                                followmagebut.setImageResource(R.drawable.favourites);
                                followfalg=false;
                            }
                            follows.clear();

                        }
                        else if(today.compareTo(c)<0) {

                            expiredatetextview.setText("EXP: " + formattedDate);
                            ParseFile image = (ParseFile) country.get("image");
                            ParseUser homeuser=country.getParseUser("user");


                            photouserID=homeuser.getObjectId();
                            if(!((String) homeuser.get("name") ==null)){
                                userrealname_textview.setText((String) homeuser.get("name"));
                            }
                            else {
                                userrealname_textview.setText("");
                            }



                            taglinetextview.setText((String) country.get("tagline"));





                            if (!( homeuser.get("userphoto") ==null)){


                                if(!(homeuser.get("facebookId") ==null)){
                                    if (homeuser.get("facebook").equals("true")){

                                        String url="https://graph.facebook.com/"+homeuser.get("facebookId")+"/picture?type=large";
                                        Picasso.with(DetailsActivity.this).load(url).into(userimageview);

                                    }
                                    else {

                                        ParseFile userimage = (ParseFile) homeuser.get("userphoto");
                                        Picasso.with(DetailsActivity.this).load(userimage.getUrl()).into(userimageview);


                                    }

                                }

                                else {

                                    ParseFile userimage = (ParseFile) homeuser.get("userphoto");
                                    Picasso.with(DetailsActivity.this).load(userimage.getUrl()).into(userimageview);

                                }

                            }
                            else{
                                if(!(homeuser.get("facebookId") ==null)){
                                    String url="https://graph.facebook.com/"+homeuser.get("facebookId")+"/picture?type=large";
                                    Picasso.with(DetailsActivity.this).load(url).into(userimageview);
                                }
                                else {

                                }
                            }




                            Picasso.with(DetailsActivity.this).load(image.getUrl()).into(productimageview);

                            retailtextview.setText((String) country.get("retail"));
                            if(!(country.<String>getList("userlike") ==null)){
                                likes=(ArrayList<String>)country.get("userlike");
                            }
                            if(!(country.<String>getList("userfollow") ==null)){
                                follows=(ArrayList<String>)country.get("userfollow");

                            }
                            commenttextview.setText(Integer.toString(country.getInt("commentnumber")));
                            liketextview.setText(Integer.toString(likes.size()));
                            String likecompare=likes.toString();
                            if(likecompare.contains(currentuser.getObjectId())){
                                likebutimagebut.setImageResource(R.drawable.like_banner_press);
                            }
                            else {
                                likebutimagebut.setImageResource(R.drawable.like_banner);
                            }
                            likes.clear();

                            String followcompare=follows.toString();
                            if(followcompare.contains(currentuser.getObjectId())){
                                followmagebut.setImageResource(R.drawable.favourites_press);
                            }
                            else {
                                followmagebut.setImageResource(R.drawable.favourites);
                            }
                            follows.clear();


                        }


                    }
                    //  ok = true;
                    mProgressDialog.dismiss();
                }

            }
        });

         likebutimagebut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (likeflag==true) {
                    likebutimagebut.setImageResource(R.drawable.like_banner);

                    final String like = Integer.toString(likenumber - 1);
                    likenumber=likenumber-1;
                    liketextview.setText(like);
                    likeflag=false;
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Photo");
                    query.getInBackground(photoID, new GetCallback<ParseObject>() {
                        public void done(ParseObject photo_origin, ParseException e) {
                            if (e == null) {

                                photo = photo_origin;
                                if(!(photo.get("userlike") ==null)){
                                    likes=(ArrayList<String>)photo.get("userlike");
                                }
                                ParseUser touserlike=photo_origin.getParseUser("user");
                                ParseUser likeuser=ParseUser.getCurrentUser();
                                Log.d("asdfasdfasdfadf",likes.toString());
                                likes.remove(likeuser.getObjectId());
                                Log.d("asdfasdfasdfadf",likes.toString());
                                photo.put("userlike", likes);
                                try {
                                    photo.save();
                                } catch (ParseException e1) {
                                    e1.printStackTrace();
                                }

                                ParseUser currentuser=ParseUser.getCurrentUser();
                                if( !(currentuser.getObjectId() .equals(photouserID))) {



                                    ParseObject alert=new ParseObject("Alert");
                                    alert.put("fromuser",ParseUser.getCurrentUser());
                                    alert.put("contente", (String)ParseUser.getCurrentUser().get("displayName")+" unlike!");
                                    alert.put("photo",photo);
                                    alert.put("touser",touserlike);
                                    alert.saveInBackground();
                                    ParseQuery pushQuery = ParseInstallation.getQuery();
                                    pushQuery.whereEqualTo("user", touserlike);
                                    pushQuery.whereEqualTo("notification","true");

// Send push notification to query
                                    ParsePush push = new ParsePush();
                                    push.setQuery(pushQuery);
// Set our Installation query
                                    push.setMessage("From "+(String)ParseUser.getCurrentUser().get("displayName")+" Unlike!:\n");
                                    push.sendInBackground();



                                }






                            }
                        }
                    });

                } else {
                    likebutimagebut.setImageResource(R.drawable.like_banner_press);
                    likeflag=true;
                    final String like = Integer.toString(likenumber + 1);
                    likenumber=likenumber+1;
                    liketextview.setText(like);
//                        mProgressDialog.show();


                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Photo");
                    query.getInBackground(photoID, new GetCallback<ParseObject>() {
                        public void done(ParseObject photo_origin, ParseException e) {
                            if (e == null) {

                                photo = photo_origin;

                                ParseUser touserlike=photo_origin.getParseUser("user");
                                ParseUser user=ParseUser.getCurrentUser();
                                String userid=user.getObjectId();
                                if(!(photo.get("userlike") ==null)){
                                    likes=(ArrayList<String>)photo.get("userlike");
                                }
                                likes.add(userid);
                                photo.put("userlike",likes);
                                try {
                                    photo.save();
                                } catch (ParseException e1) {
                                    e1.printStackTrace();
                                }
                                Log.d("adfadfasdfasdfadsfasdf", (String) photo.get("tagline"));


                                ParseUser currentuser=ParseUser.getCurrentUser();
                                if( !(currentuser.getObjectId() .equals(photouserID))) {
                                    ParseObject alert = new ParseObject("Alert");
                                    alert.put("fromuser", ParseUser.getCurrentUser());
                                    alert.put("contente", (String) ParseUser.getCurrentUser().get("displayName") + " like your product");
                                    alert.put("photo", photo);
                                    alert.put("touser", touserlike);
                                    alert.saveInBackground();
                                    ParseQuery pushQuery = ParseInstallation.getQuery();
                                    pushQuery.whereEqualTo("user", touserlike);
                                    pushQuery.whereEqualTo("notification", "true");
// Send push notification to query
                                    ParsePush push = new ParsePush();
                                    push.setQuery(pushQuery);
// Set our Installation query
                                    push.setMessage("From " + (String) ParseUser.getCurrentUser().get("displayName") + " like your product\n");
                                    push.sendInBackground();
                                }
                            }


                            // new RemoteDataTask().execute();


                        }


                    });


                }

            }
        });
         followmagebut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (followfalg) {
                     followmagebut.setImageResource(R.drawable.favourites);
//                        mProgressDialog.show();
//                        follows.clear();
                     followfalg=false;
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Photo");
                    query.getInBackground(photoID, new GetCallback<ParseObject>() {
                        public void done(ParseObject photo_origin, ParseException e) {
                            if (e == null) {

                                photo = photo_origin;
                                if(!(photo.get("userfollow") ==null)){
                                    follows=(ArrayList<String>)photo.get("userfollow");
                                }
                                ParseUser likeuser=ParseUser.getCurrentUser();
                                follows.remove(likeuser.getObjectId());
                                photo.put("userfollow",follows);

                                try {
                                    photo.save();
                                } catch (ParseException ee) {
                                    ee.printStackTrace();
                                }

//                    photo.removeAll("userlike",likes);
//                    try {
//                        photo.save();
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }

                                ParseUser touser=photo_origin.getParseUser("user");
                                ParseQuery<ParseObject> query = ParseQuery.getQuery("Follow");
                                query.whereEqualTo("user", ParseUser.getCurrentUser());
                                query.whereEqualTo("photo", photo);
                                query.getFirstInBackground(new GetCallback<ParseObject>() {
                                    public void done(ParseObject object, ParseException e) {
                                        if (object == null) {
                                            Log.d("score", "The getFirst request failed.");
                                        } else {
                                            object.deleteInBackground();
                                        }
//                                            new RemoteDataTask().execute();



                                    }
                                });
                                ParseUser currentuser=ParseUser.getCurrentUser();
                                if( !(currentuser.getObjectId() .equals(photouserID))) {
                                    ParseObject alert = new ParseObject("Alert");
                                    alert.put("fromuser", ParseUser.getCurrentUser());
                                    alert.put("contente", (String) ParseUser.getCurrentUser().get("displayName") + " unfollow you");
                                    alert.put("photo", photo);
                                    alert.put("touser", touser);
                                    alert.saveInBackground();
//                    ParsePush push = new ParsePush();
//                    push.setChannel("Founditt");
//
//                    push.setMessage("The Giants just scored! It's now 2-2 against the Mets.");
//                    push.sendInBackground();


                                    ParseQuery pushQuery = ParseInstallation.getQuery();
                                    pushQuery.whereEqualTo("user", touser);
                                    pushQuery.whereEqualTo("notification", "true");
// Send push notification to query
                                    ParsePush push = new ParsePush();
                                    push.setQuery(pushQuery);
// Set our Installation query
                                    push.setMessage("From " + (String) ParseUser.getCurrentUser().get("displayName") + " unfollow you\n");
                                    push.sendInBackground();
                                }

                            }
                        }
                    });


                } else {
                     followmagebut.setImageResource(R.drawable.favourites_press);
                     followfalg=true;

//                        mProgressDialog.show();
//                        follows.clear();

//                        adapter = new homeItemListAdapter(HomeActivity.this,
//                                data);
//                        listview.setAdapter(adapter);
//                        listview.setSelectionFromTop(position, 0);
//                        mProgressDialog.dismiss();
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Photo");
                    query.getInBackground(photoID, new GetCallback<ParseObject>() {
                        public void done(ParseObject photo_origin, ParseException e) {
                            if (e == null) {

                                photo = photo_origin;
                                if(!(photo.get("userfollow") ==null)){
                                    follows=(ArrayList<String>)photo.get("userfollow");
                                }
                                String userid=currentuser.getObjectId();
                                follows.add(userid);
                                photo.put("userfollow", follows);
                                try {
                                    photo.save();
                                } catch (ParseException e1) {
                                    e1.printStackTrace();
                                }

                                ParseUser touser=photo_origin.getParseUser("user");
                                ParseObject follow = new ParseObject("Follow");
                                follow.put("user", ParseUser.getCurrentUser());
                                follow.put("photo", photo);
                                follow.saveInBackground();
                                ParseUser currentuser=ParseUser.getCurrentUser();
                                if( !(currentuser.getObjectId() .equals(photouserID))) {
                                    ParseObject alert = new ParseObject("Alert");
                                    alert.put("fromuser", ParseUser.getCurrentUser());
                                    alert.put("contente", (String) ParseUser.getCurrentUser().get("displayName") + " follow you");
                                    alert.put("photo", photo);
                                    alert.put("touser", touser);
                                    alert.saveInBackground();
//                    ParsePush push = new ParsePush();
//                    push.setChannel("Founditt");
//
//                    push.setMessage("The Giants just scored! It's now 2-2 against the Mets.");
//                    push.sendInBackground();


                                    ParseQuery pushQuery = ParseInstallation.getQuery();
                                    pushQuery.whereEqualTo("user", touser);
                                    pushQuery.whereEqualTo("notification", "true");
// Send push notification to query
                                    ParsePush push = new ParsePush();
                                    push.setQuery(pushQuery);
// Set our Installation query
                                    push.setMessage("From " + (String) ParseUser.getCurrentUser().get("displayName") + " follow you\n");
                                    push.sendInBackground();
                                }

                            }
                            // new RemoteDataTask().execute();
//                                homeitem dd = data.get(position);
//                                dd.setFollowflag("true");
//                                adapter = new homeItemListAdapter(HomeActivity.this,
//                                        data);
//                                listview.setAdapter(adapter);
                        }
                    });


                }
                flag = true;
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
//           holder.productimageview.setOnClickListener(new View.OnClickListener() {
//               @Override
//               public void onClick(View v) {
////                   Intent zoom = new Intent(HomeActivity.this, CommentActivity.class);
////                   zoom.putExtra("photoID", worldpopulationlist.get(position).getID());
////                   startActivity(zoom);
////                   // Toast.makeText(HomeActivity.this,worldpopulationlist.get(position).getID(),Toast.LENGTH_SHORT).show();
////
////                   overridePendingTransition(R.anim.right_in, R.anim.left_out);
//
//
//
//                   Intent zoom = new Intent(HomeActivity.this, CommentActivity.class);
//                   zoom.putExtra("photoID", worldpopulationlist.get(position).getID());
//                   startActivity(zoom);
//                   // Toast.makeText(HomeActivity.this,worldpopulationlist.get(position).getID(),Toast.LENGTH_SHORT).show();
//
//                   overridePendingTransition(R.anim.right_in, R.anim.left_out);
//
//               }
//           });
         shareimagebut.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                    Intent intent = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:"));
//                    intent.putExtra(Intent.EXTRA_SUBJECT, ParseUser.getCurrentUser().getUsername());
//
//                    intent.putExtra(Intent.EXTRA_STREAM, getImageUri(getApplicationContext(), bitmap));
//                    intent.setType("image/*");
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
//                    startActivity(Intent.createChooser(intent, null));



                 productimageview.buildDrawingCache();
                Bitmap bm = productimageview.getDrawingCache();

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
                                builderInner.setPositiveButton("Submit",
                                        new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int which) {

                                                if(strName.equals("Report expired")){


                                                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Photo");
                                                    query.getInBackground(photoID, new GetCallback<ParseObject>() {
                                                        public void done(ParseObject photo_origin, ParseException e) {
                                                            if (e == null) {
//                                                                expiredatetextview.setText("EXPIRED");

                                                                photo = photo_origin;
                                                                Date today=new Date(System.currentTimeMillis());
                                                                photo.put("expiry",today);
//                                                                    photo.put("expflag","true");
                                                                photo.saveInBackground();
                                                                ParseUser touser=photo_origin.getParseUser("user");
                                                                ParseUser currentuser=ParseUser.getCurrentUser();
                                                                if( !(currentuser.getObjectId() .equals(photouserID))) {
                                                                    ParseObject alert = new ParseObject("Alert");
                                                                    alert.put("fromuser", ParseUser.getCurrentUser());
                                                                    alert.put("contente", (String) ParseUser.getCurrentUser().get("displayName") + " expire your product");
                                                                    alert.put("photo", photo);
                                                                    alert.put("touser", touser);
                                                                    alert.saveInBackground();


                                                                    ParseQuery pushQuery = ParseInstallation.getQuery();
                                                                    pushQuery.whereEqualTo("user", touser);
                                                                    pushQuery.whereEqualTo("notification", "true");
// Send push notification to query
                                                                    ParsePush push = new ParsePush();
                                                                    push.setQuery(pushQuery);
// Set our Installation query
                                                                    push.setMessage("From " + (String) ParseUser.getCurrentUser().get("displayName") + " expire your product");
                                                                    push.sendInBackground();
                                                                }

                                                            }

                                                        }
                                                    });



                                                }
                                                else if(strName.equals("Report inappropriate")){


                                                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Photo");
                                                    query.getInBackground(photoID, new GetCallback<ParseObject>() {
                                                        public void done(ParseObject photo_origin, ParseException e) {
                                                            if (e == null) {

                                                                photo = photo_origin;
                                                                ParseUser touser=photo_origin.getParseUser("user");
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
                                                                ParseUser currentuser=ParseUser.getCurrentUser();
                                                                if( !(currentuser.getObjectId() .equals(photouserID))) {
                                                                    ParseObject alert = new ParseObject("Alert");
                                                                    alert.put("fromuser", ParseUser.getCurrentUser());
                                                                    alert.put("contente", (String) ParseUser.getCurrentUser().get("displayName") + " inappropriate your product");
                                                                    alert.put("photo", photo);
                                                                    alert.put("touser", touser);
                                                                    alert.saveInBackground();


                                                                    ParseQuery pushQuery = ParseInstallation.getQuery();
                                                                    pushQuery.whereEqualTo("user", touser);
                                                                    pushQuery.whereEqualTo("notification", "true");
// Send push notification to query
                                                                    ParsePush push = new ParsePush();
                                                                    push.setQuery(pushQuery);
// Set our Installation query
                                                                    push.setMessage("From " + (String) ParseUser.getCurrentUser().get("displayName") + " inappropriate your product");
                                                                    push.sendInBackground();
                                                                }
                                                            }

                                                        }
                                                    });



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




    }








}
