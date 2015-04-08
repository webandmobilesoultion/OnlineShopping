package com.raoul.founditt;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.parse.ParseACL;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;
import com.parse.SaveCallback;
import com.raoul.founditt.ImageLoadPackge.FileCache;
import com.raoul.founditt.ImageLoadPackge.ImageLoader;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.raoul.founditt.ImageLoadPackge.ImageLoaderGrid;
import com.raoul.founditt.ImageLoadPackge.MemoryCache;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;


public class FavouritsActivity extends ActionBarActivity implements MaterialTabListener
{
    /** Called when the activity is first created. */
    int size;
    int post;
    Typeface font;
    MaterialTabHost tabHost;
    ViewPager pager;
    ViewPagerAdapter adapter;
    SavedActivity followactivity;
    PortfolioActivity postactivity;
    String fileuri;
    Bitmap bitmap;
    ImageView userphoto_imageview;
    ParseFile images;
    private ProgressDialog progressDialog;
    byte[] saveData;
   public static ImageLoaderGrid imageLoader;
    ImageLoader imageLoaderprofile;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourits);
        getSupportActionBar().hide();
        new FileCache(this).clear();
        new MemoryCache().clear();
        imageLoader=new ImageLoaderGrid(FavouritsActivity.this);
        imageLoaderprofile=new ImageLoader(FavouritsActivity.this);
        ParseUser currentuser=ParseUser.getCurrentUser();
        ParseFile image=currentuser.getParseFile("userphoto");
        Intent intent=getIntent();
        fileuri=intent.getStringExtra("serchresult");


        userphoto_imageview=(ImageView)findViewById(R.id.userphoto_details_imageView);


        if(!(fileuri ==null)){

            setProgressDialog( ProgressDialog.show(FavouritsActivity.this, "",
                    "uploading  data ...", true, true) );


                BitmapFactory.Options options = new BitmapFactory.Options();
                bitmap = BitmapFactory.decodeFile(fileuri,
                        options);

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                saveData = bos.toByteArray();
                ParseUser user = ParseUser.getCurrentUser();
                if(!(saveData ==null)){
                images = new ParseFile("photo.jpg", saveData);

                    images.saveInBackground();

                user.put("userphoto",images);
            }

            user.saveInBackground(new SaveCallback() {

                @Override
                public void done(ParseException e) {
                    getProgressDialog().dismiss();

                    if (e == null) {

                    } else {

                    }
                }

            });

                userphoto_imageview.setImageBitmap(bitmap);

        }
        else if(!(image ==null)){
            if (currentuser.get("facebook").equals("true")){
                Session session = ParseFacebookUtils.getSession();
                if (session != null && session.isOpened()) {
                    makeMeRequest();
                }

            }
            else {
//                imageLoaderprofile.DisplayImage(image.getUrl(), userphoto_imageview);
                Picasso.with(this).load(image.getUrl()).into(userphoto_imageview);
            }

        }

        else if (!(currentuser.get("facebook") ==null)){
            Session session = ParseFacebookUtils.getSession();
            if (session != null && session.isOpened()) {
                makeMeRequest();
            }
        }

        followactivity=new SavedActivity();
        postactivity=new PortfolioActivity();
        tabHost = (MaterialTabHost) this.findViewById(R.id.tabHost);
        pager = (ViewPager) this.findViewById(R.id.pager );


        // init view pager
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // when user do a swipe the selected tab change
                tabHost.setSelectedNavigationItem(position);

            }
        });

        // insert all tabs from pagerAdapter data
        for (int i = 0; i < adapter.getCount(); i++) {
            tabHost.addTab(
                    tabHost.newTab()
                            .setText(adapter.getPageTitle(i))
                            .setTabListener(this)
            );

        }
        font = Typeface.createFromAsset(getAssets(), "Questrial-Regular.ttf");
        TextView fave_fitle=(TextView)findViewById(R.id.fave_title_textView);
        fave_fitle.setTypeface(font);
        ImageButton editButton = (ImageButton) findViewById(R.id.setting_imageButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;
                intent = new Intent(FavouritsActivity.this, ProfileActivity.class);
                if (!(fileuri ==null)){
                    intent.putExtra("serchresult",fileuri);
                }

                startActivity(intent);

            }
        });
        //

        TextView username=(TextView)findViewById(R.id.username_favtextView);
        username.setTypeface(font);
        if((String)currentuser.get("name")==null){
            username.setText("unknown");
        }
        else {
            username.setText((String)currentuser.get("name"));
        }

        //

        ParseQuery<ParseObject> photosFromCurrentUserQuery = ParseQuery.getQuery("Follow");
        photosFromCurrentUserQuery.whereEqualTo("user", ParseUser.getCurrentUser());
        photosFromCurrentUserQuery.include("photo");
        try {
           size=photosFromCurrentUserQuery.count();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        TextView favortites=(TextView)findViewById(R.id.favour_favtextView);
        favortites.setTypeface(font);
        favortites.setText(Integer.toString(size)+" FAVORITES");
        //

        ParseQuery<ParseObject> postquary = ParseQuery.getQuery("Photo");
        postquary.whereEqualTo("user", ParseUser.getCurrentUser());
        postquary.include("photo");
        try {
            post=postquary.count();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //
        TextView posttextview=(TextView)findViewById(R.id.post_favtextView);
        posttextview.setTypeface(font);
        posttextview.setText(Integer.toString(post)+" POSTS");
        ImageButton hoemButton = (ImageButton) findViewById(R.id.home_fav_imageButton);
        hoemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;
                intent = new Intent(FavouritsActivity.this, HomeActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.left_in, R.anim.right_out);

            }
        });




        userphoto_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FavouritsActivity.this,CameraActivity.class);
                intent.putExtra("class","profile");
               startActivity(intent);
            }
        });



        ImageButton cameraButton = (ImageButton) findViewById(R.id.camera_fav_imageButton);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;
                intent = new Intent(FavouritsActivity.this, CameraActivity.class);
                startActivity(intent);

            }
        });
        ImageButton alertButton = (ImageButton) findViewById(R.id.alert_favimagebutton);
        alertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;
                intent = new Intent(FavouritsActivity.this, AlarmActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);

            }
        });









    }
    private void makeMeRequest() {
        Session session =  ParseFacebookUtils.getSession();

                            Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {

                                @Override
                                public void onCompleted(final GraphUser user, Response response) {


                                    if(user != null){
//                                          imageLoader.DisplayImage("https://graph.facebook.com/"+user.getId()+"/picture?type=large",userphoto_imageview);
                                        AsyncTask<Void, Void, Bitmap> t = new AsyncTask<Void, Void, Bitmap>(){
                                            protected Bitmap doInBackground(Void... p) {
                                                Bitmap bm = null;
                                                try {
                                                    URL aURL = new URL("https://graph.facebook.com/"+user.getId()+"/picture?type=large");
                                                    URLConnection conn = aURL.openConnection();
                                                    conn.setUseCaches(true);
                                                    conn.connect();
                                                    InputStream is = conn.getInputStream();
                                                    BufferedInputStream bis = new BufferedInputStream(is);
                                                    bm = BitmapFactory.decodeStream(bis);
                                                    bis.close();
                                                    is.close();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                                return bm;
                                            }

                                            protected void onPostExecute(Bitmap bm){

//                                                Drawable drawable = new BitmapDrawable(getResources(), bm);

                                                userphoto_imageview.setImageBitmap(bm);


                                            }
                                        };
                                        t.execute();




                                    }

                                }
                            });

    }

    @Override
    public void onTabSelected(MaterialTab tab) {
        pager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(MaterialTab tab) {

    }

    @Override
    public void onTabUnselected(MaterialTab tab) {

    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);

        }

        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return postactivity;
                case 1:
                    return followactivity;

            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "post";
                case 1:
                    return "favourites";

            }
            return null;
        }

    }



    public ProgressDialog getProgressDialog(){
        return progressDialog;
    }

    public void setProgressDialog(ProgressDialog pd){
        progressDialog = pd;
    }
    public void showDialog(String msg) throws Exception
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(FavouritsActivity.this);

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