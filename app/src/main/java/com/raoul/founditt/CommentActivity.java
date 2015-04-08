package com.raoul.founditt;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.raoul.founditt.ImageLoadPackge.CommentImageLoader;
import com.raoul.founditt.ImageLoadPackge.FileCache;
import com.raoul.founditt.ImageLoadPackge.ImageLoader;
import com.raoul.founditt.ImageLoadPackge.MemoryCache;
import com.raoul.founditt.Timeutillity.TimeAgo;
import com.raoul.founditt.listmodel.commentitem;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class CommentActivity extends Activity {
    private List<commentitem> data;
    private CommetListAdapter adapter;
    private boolean flag=true;
    private String imageurl;
    public String curentusername;
    private String username;
    private boolean likeflag;
    private boolean followfalg;
    private Date regtime;
    private long diftime;
    ParseObject photo;
    ParseUser touser;
    ListView listview;
    List<ParseObject> ob;
    List<ParseObject> com_ob;
    ProgressDialog mProgressDialog;
    TimeAgo timeAgo;
    Typeface font;
    ArrayList<String> likes;
    ArrayList<String> follows;
    private int likenumber;
    private int commentnumber;
    ParseUser currentuser;
    String uerID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        new FileCache(this).clear();
        new MemoryCache().clear();
        likes = new ArrayList<String>();
        follows = new ArrayList<String>();
        currentuser=ParseUser.getCurrentUser();
        final ImageView commentimageview=(ImageView)findViewById(R.id.product_commentimageView);
//        final ImageButton likeButton = (ImageButton) findViewById(R.id.like_commentimageButton);
        photo=new Photo();
        font = Typeface.createFromAsset(getAssets(), "Questrial-Regular.ttf");
        TextView comment_title=(TextView)findViewById(R.id.commnet_title_textView);
        comment_title.setTypeface(font);
        Intent getdataintente=getIntent();
//        final ImageButton followButton = (ImageButton) findViewById(R.id.follow_commentimageButton);
        final String photoID=getdataintente.getStringExtra("photoID");
        data = new ArrayList<commentitem>();
        final ImageLoader imageLoader=new ImageLoader(CommentActivity.this);
        mProgressDialog = new ProgressDialog(CommentActivity.this);
        // Set progressdialog title
        mProgressDialog.setTitle("Message");
        // Set progressdialog message
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setIndeterminate(false);
        // Show progressdialog
        mProgressDialog.show();
        timeAgo=new TimeAgo(CommentActivity.this);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Photo");
        query.include("user");
        query.getInBackground(photoID, new GetCallback<ParseObject>() {
            public void done(ParseObject country, ParseException e) {
                if (e == null) {
                    ParseFile image = (ParseFile) country.get("image");
                    image.saveInBackground();
                    photo = country;
                    touser = country.getParseUser("user");
                    uerID=touser.getObjectId();
                    //imageLoader.DisplayImage(image.getUrl(), commentimageview);
                    Picasso.with(CommentActivity.this).load(image.getUrl()).into(commentimageview);
//
//
//                    if(!(photo.get("userfollow") ==null)){
//                        follows=(ArrayList<String>)photo.get("userfollow");
//                    }
//                    if(!(photo.get("userlike") ==null)){
//                        likes=(ArrayList<String>)photo.get("userlike");
//                    }
//
//                    //photosFromCurrentUserQuery.include("user.userphoto");
//                    //photosFromCurrentUserQuery.whereExists("user");
//                    likenumber = likes.size();
//
//                    String likecompare = likes.toString();
//                    if (likecompare.contains(currentuser.getObjectId())) {
//                        likeButton.setImageResource(R.drawable.hearth_press);
//                        likeflag = true;
//                    } else {
//                        likeButton.setImageResource(R.drawable.hearth);
//                        likeflag = false;
//                    }
//
//                    String followcompare = follows.toString();
//                    if (followcompare.contains(currentuser.getObjectId())) {
//                        followButton.setImageResource(R.drawable.favoer_press);
//                        followfalg = true;
//                    } else {
//                        followButton.setImageResource(R.drawable.favoer);
//                        followfalg = false;
//                    }

                    new RemoteDataTask().execute();
                    mProgressDialog.dismiss();
                    //  ok = true;
//                    mProgressDialog.dismiss();
                }

            }
        });

        final EditText comment_edit=(EditText)findViewById(R.id.comment_editText);

//        ImageButton shareimagebutton=(ImageButton)findViewById(R.id.share_commentimageButton);
//        shareimagebutton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                commentimageview.buildDrawingCache();
//                Bitmap bm =commentimageview.getDrawingCache();
//
//                Intent imageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                File imagesFolder = new File(Environment.getExternalStorageDirectory(), "Punch");
//                imagesFolder.mkdirs();
//                String fileName = "image"  + ".jpg";
//                File output = new File(imagesFolder, fileName);
//                Uri uriSavedImage = Uri.fromFile(output);
//                imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
//                OutputStream fos = null;
//
//                try {
//                    fos = getContentResolver().openOutputStream(uriSavedImage);
//                    bm.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//                    fos.flush();
//                    fos.close();
//                }
//                catch (FileNotFoundException e)
//                {
//                    e.printStackTrace();
//                }
//                catch (IOException e)
//                {
//                    e.printStackTrace();
//                }
//                finally
//                {}
//
//                Intent intent = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:"));
//                intent.putExtra(Intent.EXTRA_SUBJECT, ParseUser.getCurrentUser().getUsername());
//
//                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(output));
//                intent.setType("image/*");
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
//                startActivity(Intent.createChooser(intent, null));
//            }
//        });
        comment_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    mProgressDialog.show();
                    int commentnumber=photo.getInt("commentnumber");
                    ParseObject comment=new ParseObject("Comment");
                    comment.put("user",ParseUser.getCurrentUser());
                    comment.put("contente",comment_edit.getText().toString());
                    comment.put("photo",photo);
                    comment.saveInBackground(new SaveCallback() {

                        @Override
                        public void done(ParseException e) {

                            if (e == null) {

                            } else {
                                try {

                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                            }
                        }

                    });
                    ParseUser currentuser=ParseUser.getCurrentUser();
                    if (!(currentuser.getObjectId() .equals(uerID))){


                        ParseObject alert=new ParseObject("Alert");
                        alert.put("fromuser",ParseUser.getCurrentUser());
                        alert.put("contente",comment_edit.getText().toString());
                        alert.put("photo",photo);
                        alert.put("touser",touser);
                        alert.saveInBackground();
//                    ParseQuery<ParseObject> commentsizeQuery = ParseQuery.getQuery("Comment");
//                    commentsizeQuery .whereEqualTo("photo", photo);
                        //photosFromCurrentUserQuery.include("user.userphoto");
                        //photosFromCurrentUserQuery.whereExists("user");

                        photo.put("commentnumber",commentnumber+1);

                        photo.saveInBackground();
                        ParsePush push = new ParsePush();
                        push.setChannel("Founditt");

//                    push.setMessage("The Giants just scored! It's now 2-2 against the Mets.");
//                    push.sendInBackground();


                        ParseQuery pushQuery = ParseInstallation.getQuery();
                        pushQuery.whereEqualTo("user", touser);

// Send push notification to query
                        push = new ParsePush();
                        push.setQuery(pushQuery);
// Set our Installation query
                        push.setMessage("From "+(String)ParseUser.getCurrentUser().get("displayName")+" commented:\n"+comment_edit.getText().toString());
                        push.sendInBackground();


                    }

                    new RemoteDataTask().execute();
                    mProgressDialog.dismiss();
                    comment_edit.setText("");
                }
                return false;
            }
        });




//        likeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                if(likeflag){
//                    likeButton.setImageResource(R.drawable.hearth);
//                    photo.removeAll("userlike", likes);
//
//                    try {
//                        photo.save();
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                    ParseUser likeuser=ParseUser.getCurrentUser();
////                    photo.removeAll("userlike",likes);
////                    try {
////                        photo.save();
////                    } catch (ParseException e) {
////                        e.printStackTrace();
////                    }
//                    likes.remove(likeuser.getObjectId());
//                    photo.put("userlike", likes);
//                    photo.saveInBackground();
////                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Like");
////                    query.whereEqualTo("user", ParseUser.getCurrentUser());
////                    query.whereEqualTo("photo", photo);
////
////                    mProgressDialog.show();
////                    query.getFirstInBackground(new GetCallback<ParseObject>() {
////                        public void done(ParseObject object, ParseException e) {
////                            if (object == null) {
////                                Log.d("score", "The getFirst request failed.");
////                            } else {
////                                object.deleteInBackground();
////                            }
////
////                            mProgressDialog.dismiss();
////                        }
////                    });
//
//
//
////                    liketextview.setText(Integer.toString(tempunlike));
//                    //photo.saveInBackground();
//                    ParseObject alert=new ParseObject("Alert");
//                    alert.put("fromuser",ParseUser.getCurrentUser());
//                    alert.put("contente", ParseUser.getCurrentUser().getUsername()+" unlike!");
//                    alert.put("photo",photo);
//                    alert.put("touser",touser);
//                    alert.saveInBackground();
////                    ParsePush push = new ParsePush();
////                    push.setChannel("Founditt");
////
////                    push.setMessage("The Giants just scored! It's now 2-2 against the Mets.");
////                    push.sendInBackground();
//
//
//                    ParseQuery pushQuery = ParseInstallation.getQuery();
//                    pushQuery.whereEqualTo("user", touser);
//                    pushQuery.whereEqualTo("notification","true");
//// Send push notification to query
//                    ParsePush push = new ParsePush();
//                    push.setQuery(pushQuery);
//// Set our Installation query
//                    push.setMessage("From "+ParseUser.getCurrentUser().getUsername()+" Unlike!:\n");
//                    push.sendInBackground();
//                    likeflag=false;
//                }
//                else{
//                    likeButton.setImageResource(R.drawable.hearth_press);
//                    ParseUser user=ParseUser.getCurrentUser();
//                    String userid=user.getObjectId();
////                    if(!(likes ==null)){
////
////                        photo.removeAll("userlike",likes);
////                        try {
////                            photo.save();
////                        } catch (ParseException e) {
////                            e.printStackTrace();
////                        }
////                    }
//
//                    likes.add(userid);
//                    photo.put("userlike", likes);
//                    photo.saveInBackground();
////                    ParseObject follow = new ParseObject("Like");
////                    follow.put("user", ParseUser.getCurrentUser());
////                    follow.put("photo", photo);
////                    follow.saveInBackground();
//                    ParseObject alert=new ParseObject("Alert");
//                    alert.put("fromuser",ParseUser.getCurrentUser());
//                    alert.put("contente",ParseUser.getCurrentUser().getUsername()+" like your product");
//                    alert.put("photo",photo);
//                    alert.put("touser",touser);
//                    alert.saveInBackground();
////                    ParsePush push = new ParsePush();
////                    push.setChannel("Founditt");
////
////                    push.setMessage("The Giants just scored! It's now 2-2 against the Mets.");
////                    push.sendInBackground();
//
////                    int tempunlike=likenumber+1;
////                    photo.put("likenumber",tempunlike);
//
//
////                    liketextview.setText(Integer.toString(tempunlike));
//                    photo.saveInBackground();
//                    ParseQuery pushQuery = ParseInstallation.getQuery();
//                    pushQuery.whereEqualTo("user", touser);
//                    pushQuery.whereEqualTo("notification","true");
//// Send push notification to query
//                    ParsePush push = new ParsePush();
//                    push.setQuery(pushQuery);
//// Set our Installation query
//                    push.setMessage("From "+ParseUser.getCurrentUser().getUsername()+" like your product\n");
//                    push.sendInBackground();
//                    likeflag=true;
//                }
//            }
//        });
//
//
//
//        followButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                if(followfalg){
//                    followButton.setImageResource(R.drawable.favoer);
//                    mProgressDialog.show();
//                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Follow");
//                    query.whereEqualTo("user", ParseUser.getCurrentUser());
//                    query.whereEqualTo("photo", photo);
//                    query.getFirstInBackground(new GetCallback<ParseObject>() {
//                        public void done(ParseObject object, ParseException e) {
//                            if (object == null) {
//                                Log.d("score", "The getFirst request failed.");
//                            } else {
//                                object.deleteInBackground();
//                                mProgressDialog.dismiss();
//                            }
//
//                        }
//                    });
//                    ParseUser likeuser=ParseUser.getCurrentUser();
//                    follows.remove(likeuser.getObjectId());
//                    photo.put("userfollow", follows);
//
//                    try {
//                        photo.save();
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//
////                    photo.removeAll("userlike",likes);
////                    try {
////                        photo.save();
////                    } catch (ParseException e) {
////                        e.printStackTrace();
////                    }
//
//                    ParseObject alert=new ParseObject("Alert");
//                    alert.put("fromuser",ParseUser.getCurrentUser());
//                    alert.put("contente",ParseUser.getCurrentUser().getUsername()+" unfollow you");
//                    alert.put("photo",photo);
//                    alert.put("touser",touser);
//                    alert.saveInBackground();
////                    ParsePush push = new ParsePush();
////                    push.setChannel("Founditt");
////
////                    push.setMessage("The Giants just scored! It's now 2-2 against the Mets.");
////                    push.sendInBackground();
//
//
//                    ParseQuery pushQuery = ParseInstallation.getQuery();
//                    pushQuery.whereEqualTo("user", touser);
//                    pushQuery.whereEqualTo("notification","true");
//// Send push notification to query
//                    ParsePush push = new ParsePush();
//                    push.setQuery(pushQuery);
//// Set our Installation query
//                    push.setMessage("From "+ParseUser.getCurrentUser().getUsername()+" unfollow you\n");
//                    push.sendInBackground();
//
//                    followfalg=flag;
//                }
//                else{
//                    followButton.setImageResource(R.drawable.favoer_press);
//                    String userid=currentuser.getObjectId();
//                    follows.add(userid);
//                    photo.put("userfollow", Arrays.asList(userid));
//                    photo.saveInBackground();
//                    ParseObject follow = new ParseObject("Follow");
//                    follow.put("user", ParseUser.getCurrentUser());
//                    follow.put("photo", photo);
//                    follow.saveInBackground();
//                    ParseObject alert=new ParseObject("Alert");
//                    alert.put("fromuser",ParseUser.getCurrentUser());
//                    alert.put("contente",ParseUser.getCurrentUser().getUsername()+" follow you");
//                    alert.put("photo",photo);
//                    alert.put("touser",touser);
//                    alert.saveInBackground();
////                    ParsePush push = new ParsePush();
////                    push.setChannel("Founditt");
////
////                    push.setMessage("The Giants just scored! It's now 2-2 against the Mets.");
////                    push.sendInBackground();
//
//
//                    ParseQuery pushQuery = ParseInstallation.getQuery();
//                    pushQuery.whereEqualTo("user", touser);
//                    pushQuery.whereEqualTo("notification","true");
//// Send push notification to query
//                    ParsePush push = new ParsePush();
//                    push.setQuery(pushQuery);
//// Set our Installation query
//                    push.setMessage("From "+ParseUser.getCurrentUser().getUsername()+" follow you\n");
//                    push.sendInBackground();
//                    followfalg=true;
//                }
//            }
//        });


        ImageButton backbutton=(ImageButton)findViewById(R.id.comment_back_imageButton);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(CommentActivity.this,HomeActivity.class);
//                startActivity(intent);
                onBackPressed();
//                overridePendingTransition(R.anim.left_in, R.anim.right_out);
            }
        });
        ImageButton searchbut=(ImageButton)findViewById(R.id.search_commentimageButton);
        searchbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CommentActivity.this,SearchActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });


        ImageButton filterbut=(ImageButton)findViewById(R.id.fillter_commentimageButton);
        filterbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CommentActivity.this,FilterActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });



        ImageButton fillterButton = (ImageButton) findViewById(R.id.home_commentimageButton);

        fillterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;
                intent = new Intent(CommentActivity.this, HomeActivity.class);


                startActivity(intent);
                overridePendingTransition(R.anim.left_in, R.anim.right_out);

            }
        });

        ImageButton carmeraButton = (ImageButton) findViewById(R.id.camera_commentimageButton);

        carmeraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;
                intent = new Intent(CommentActivity.this, CameraActivity.class);


                startActivity(intent);

            }
        });
        ImageButton alertButton = (ImageButton) findViewById(R.id.alert_commentimageButton);

        alertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;
                intent = new Intent(CommentActivity.this,AlarmActivity.class);


                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);

            }
        });
        ImageButton profileButton = (ImageButton) findViewById(R.id.profile_commentimageButton);

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;
                intent = new Intent(CommentActivity.this, FavouritsActivity.class);


                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);

            }
        });

    }


    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            // Create a progressdialog
//            mProgressDialog = new ProgressDialog(CommentActivity.this);
//            // Set progressdialog title
//            mProgressDialog.setTitle("Message");
//            // Set progressdialog message
//            mProgressDialog.setMessage("Loading...");
//            mProgressDialog.setIndeterminate(false);
//            // Show progressdialog

        }

        @Override
        protected Void doInBackground(Void... params) {
            // Create the array
            data = new ArrayList<commentitem>();
            Date curenttime=new Date(System.currentTimeMillis());
            String diftimestr = null;
            int diftimeint = 0;
            try {
                ParseQuery<ParseObject> photosFromCurrentUserQuery = ParseQuery.getQuery("Comment");
                photosFromCurrentUserQuery.whereEqualTo("photo", photo);
                photosFromCurrentUserQuery.include("user");
                //photosFromCurrentUserQuery.include("user.userphoto");
                //photosFromCurrentUserQuery.whereExists("user");
                com_ob= photosFromCurrentUserQuery.find();
                for (ParseObject country : com_ob) {
                    // Locate images in flag column
                    regtime=country.getCreatedAt();
//                    diftime=curenttime.getTime()-regtime.getTime();
//                    if(diftime>60000&&diftime<=3600000){
//                        diftimeint= (int) (diftime/(60*1000));//min
//                        diftimestr=Integer.toString(diftimeint)+" min";
//                    }
//                    if(diftime>3600000&&diftime<=86400000){
//                        diftimeint= (int) (diftime/(3600*1000));//hours
//                        diftimestr=Integer.toString(diftimeint)+" hours";
//                    }
//                    if(diftime>86400000){
//                        diftimeint=(int)(diftime/ 1000 / 60 / 60 / 24);//days
//                        diftimestr=Integer.toString(diftimeint)+" days";
//                    }
                    diftimestr=timeAgo.timeAgo(regtime);
                    ParseUser user=country.getParseUser("user");
                    ParseFile image = (ParseFile)user.get("userphoto");

                    commentitem map = new commentitem();
//                    if(!(image ==null)){
//                        map.setIamgeurl(image.getUrl());
//                    }
//                    else {
//                        map.setIamgeurl("");
//                    }


                    ///



                    if (!( user.get("userphoto") ==null)){


                        if(!(user.get("facebookId") ==null)){
                            if (user.get("facebook").equals("true")){

                                String url="https://graph.facebook.com/"+user.get("facebookId")+"/picture?type=large";
                                map.setIamgeurl(url);
                            }
                            else {

                                ParseFile userimage = (ParseFile) user.get("userphoto");
                                map.setIamgeurl(userimage.getUrl());

                            }

                        }

                        else {

                            ParseFile userimage = (ParseFile) user.get("userphoto");
                            map.setIamgeurl(userimage.getUrl());

                        }

                    }
                    else{
                        if(!(user.get("facebookId") ==null)){
                            String url="https://graph.facebook.com/"+user.get("facebookId")+"/picture?type=large";
                            map.setIamgeurl(url);
                        }
                        else {
                            map.setIamgeurl("");
                        }
                    }




                    ///






                    map.setDiftime(diftimestr);

                    map.setComment((String)country.get("contente"));
                    map.setusername((String) user.get("displayName"));


                    data.add(map);
                }
            } catch (ParseException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Locate the listview in listview_main.xml
            listview = (ListView) findViewById(R.id.comment_listView);
            // Pass the results into ListViewAdapter.java
            adapter = new CommetListAdapter(CommentActivity.this,
                    data);
            // Binds the Adapter to the ListView
            listview.setAdapter(adapter);
            // Close the progressdialog
            mProgressDialog.dismiss();
        }
    }





    public class CommetListAdapter extends BaseAdapter {
        boolean flag=true;
        Context context;
        LayoutInflater inflater;
        CommentImageLoader imageLoader;
        private ParseFile image;
        private List<commentitem> worldpopulationlist = null;
        private ArrayList<commentitem> arraylist;
        /**
         * Constructor from a list of items
         */
        public CommetListAdapter(Context context, List<commentitem>  worldpopulationlist) {

            this.context = context;
            this.worldpopulationlist = worldpopulationlist;
            inflater = LayoutInflater.from(context);
            this.arraylist = new ArrayList<commentitem>();
            this.arraylist.addAll(worldpopulationlist);
            imageLoader = new CommentImageLoader(context);
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            final ViewHolder holder;
            if (view == null) {
                holder = new ViewHolder();
                view = inflater.inflate(R.layout.commet_viewitem, null);
                holder.usernametextview = (TextView) view.findViewById(R.id.username_contenttextView);
                holder.commenttextview=(TextView)view.findViewById(R.id.content_commet_textView);
                holder.userphotoimageview=(ImageView)view.findViewById(R.id.star_userimagebutton);
                holder.timeview=(TextView)view.findViewById(R.id.time_textView);

                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            // Set some view properties
            holder.usernametextview.setText("" + worldpopulationlist.get(position).getusername());
            holder.usernametextview.setTypeface(font);
            holder.commenttextview.setText(worldpopulationlist.get(position).getComment());
            holder.commenttextview.setTypeface(font);
            holder.timeview.setText(worldpopulationlist.get(position).getDiftime());
            holder.timeview.setTypeface(font);
            if(!worldpopulationlist.get(position).getIamgeurl().equals("")){
//                imageLoader.DisplayImage(worldpopulationlist.get(position).getIamgeurl(),holder.userphotoimageview);
                Picasso.with(context).load(worldpopulationlist.get(position).getIamgeurl()).into(holder.userphotoimageview);
            }
            else {
                holder.userphotoimageview.setImageResource(R.drawable.man);
            }
            final ListView lv = (ListView) parent;
//        holder.layout.setChecked(lv.isItemChecked(position));

            return view;
        }

        @Override
        public int getCount() {
            return worldpopulationlist.size();
        }

        @Override
        public Object getItem(int position) {
            return worldpopulationlist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        private   class ViewHolder {
            public TextView usernametextview;
            public TextView commenttextview;
            public TextView timeview;
            public ImageView userphotoimageview;


        }
    }


}
