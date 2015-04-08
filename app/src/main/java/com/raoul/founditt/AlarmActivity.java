package com.raoul.founditt;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.raoul.founditt.ImageLoadPackge.CommentImageLoader;
import com.raoul.founditt.ImageLoadPackge.FileCache;
import com.raoul.founditt.ImageLoadPackge.ImageLoader;
import com.raoul.founditt.ImageLoadPackge.MemoryCache;
import com.raoul.founditt.Timeutillity.TimeAgo;
import com.raoul.founditt.listmodel.alarmitem;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class AlarmActivity extends Activity {
    ListView list;
    private List<alarmitem> data=null;

    ProgressDialog mProgressDialog;

    private homeItemListAdapter adapter;
    ListView listView;
    List<ParseObject> ob;
    TimeAgo timeAgo;
    TextView titletextview;
    Typeface font;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        new FileCache(this).clear();
        new MemoryCache().clear();
        ImageButton hoemButton = (ImageButton) findViewById(R.id.home_alert_imageButton);
         font = Typeface.createFromAsset(getAssets(), "Questrial-Regular.ttf");
        titletextview=(TextView)findViewById(R.id.aler_title_textView);
        titletextview.setTypeface(font);
        timeAgo=new TimeAgo(AlarmActivity.this);
        hoemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;
                intent = new Intent(AlarmActivity.this, HomeActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.left_in, R.anim.right_out);

            }
        });
        ImageButton cameraButton = (ImageButton) findViewById(R.id.camera_alert_imageButton);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;
                intent = new Intent(AlarmActivity.this, CameraActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.left_in, R.anim.right_out);

            }
        });
        ImageButton alertButton = (ImageButton) findViewById(R.id.profile_alert_imageButton);
        alertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;
                intent = new Intent(AlarmActivity.this, FavouritsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.left_in, R.anim.right_out);

            }
        });
        ImageButton backButton = (ImageButton) findViewById(R.id.back_alert_imageButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;
                intent = new Intent(AlarmActivity.this, HomeActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.left_in, R.anim.right_out);

            }
        });
        new RemoteDataTask().execute();
    }

    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(AlarmActivity.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Message");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // Create the array
            data = new ArrayList<alarmitem>();
            Date regtime;
            long diftime;
            String diftimestr = null;
            int diftimeint;
            Date curenttime=new Date(System.currentTimeMillis());
            try {
                ParseQuery<ParseObject> photosFromCurrentUserQuery = ParseQuery.getQuery("Alert");
                photosFromCurrentUserQuery.whereEqualTo("touser", ParseUser.getCurrentUser());
                photosFromCurrentUserQuery.include("fromuser");
                photosFromCurrentUserQuery.include("photo");
                ob = photosFromCurrentUserQuery.find();
                for (ParseObject country : ob) {
                    // Locate images in flag column
                    ParseUser fromuser=country.getParseUser("fromuser");
                    ParseObject photo=country.getParseObject("photo");
                    ParseFile productimage=(ParseFile)photo.get("image");
                    ParseFile image = (ParseFile) fromuser.get("userphoto");

                    final alarmitem map = new alarmitem();
//                    if(!(image ==null)){
//                        map.setUserimageurl(image.getUrl());
//                    }
//
//                    else {
//                        map.setUserimageurl("");
//                    }
//                    //



                    if (!( fromuser.get("userphoto") ==null)){


                        if(!(fromuser.get("facebookId") ==null)){
                            if (fromuser.get("facebook").equals("true")){

                                String url="https://graph.facebook.com/"+fromuser.get("facebookId")+"/picture?type=large";
                                map.setUserimageurl(url);
                            }
                            else {

                                ParseFile userimage = (ParseFile) fromuser.get("userphoto");
                                map.setUserimageurl(userimage.getUrl());

                            }

                        }

                        else {

                            ParseFile userimage = (ParseFile) fromuser.get("userphoto");
                            map.setUserimageurl(userimage.getUrl());

                        }

                    }
                    else{
                        if(!(fromuser.get("facebookId") ==null)){
                            String url="https://graph.facebook.com/"+fromuser.get("facebookId")+"/picture?type=large";
                            map.setUserimageurl(url);
                        }
                        else {
                            map.setUserimageurl("");
                        }
                    }


                    //
                    map.setUsername((String) fromuser.get("displayName"));
                     map.setProductimageurl(productimage.getUrl());
                    map.setID(photo.getObjectId());
                    map.setComment((String)country.get("contente"));
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
//                        diftimeint=(int)(diftime/1000 / 60 / 60 / 24);//days
//                        diftimestr=Integer.toString(diftimeint)+" days";
//                    }
                    diftimestr=timeAgo.timeAgo(regtime);
                   map.setTime(diftimestr);







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
            listView = (ListView) findViewById(R.id.alert_listiview);
            // Pass the results into ListViewAdapter.java
            adapter = new homeItemListAdapter(AlarmActivity.this,
                    data);
            // Binds the Adapter to the ListView
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    data.get(position).getID();
                    Intent zoom = new Intent(AlarmActivity.this, CommentActivity.class);
                    zoom.putExtra("photoID", data.get(position).getID());
                    startActivity(zoom);
                    // Toast.makeText(HomeActivity.this,worldpopulationlist.get(position).getID(),Toast.LENGTH_SHORT).show();

                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }
            });
            // Close the progressdialog
            mProgressDialog.dismiss();
        }
    }


    public class homeItemListAdapter extends BaseAdapter {
        boolean flag = true;
        Context context;
        LayoutInflater inflater;
        CommentImageLoader imageLoader;
        private ParseFile image;
        private List<alarmitem> worldpopulationlist = null;
        private ArrayList<alarmitem> arraylist;

        /**
         * Constructor from a list of items
         */
        public homeItemListAdapter(Context context, List<alarmitem> worldpopulationlist) {

            this.context = context;
            this.worldpopulationlist = worldpopulationlist;
            inflater = LayoutInflater.from(context);
            this.arraylist = new ArrayList<alarmitem>();
            this.arraylist.addAll(worldpopulationlist);
            imageLoader = new CommentImageLoader(context);
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            final ViewHolder holder;

            if (view == null) {
                holder = new ViewHolder();
                view = inflater.inflate(R.layout.alert_item, null);

                holder.productimageview = (ImageView) view.findViewById(R.id.product_alertimageView);
                holder.userphotoimageview=(ImageView) view.findViewById(R.id.user_alertimageView);
                holder.usernametextview=(TextView)view.findViewById(R.id.username_alerttextView);
                holder.contenttextview=(TextView)view.findViewById(R.id.commet_alerttextView);
                holder.timetextview=(TextView)view.findViewById(R.id.time_alertextView);

                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }


//            imageLoader.DisplayImage(worldpopulationlist.get(position).getProductimageurl(), holder.productimageview);
            Picasso.with(context).load(worldpopulationlist.get(position).getProductimageurl()).into(holder.productimageview);
            if(!worldpopulationlist.get(position).getUserimageurl().equals("")){
//                imageLoader.DisplayImage(worldpopulationlist.get(position).getUserimageurl(), holder.userphotoimageview);
                Picasso.with(context).load(worldpopulationlist.get(position).getUserimageurl()).into(holder.userphotoimageview);
            }
            else {
                holder.userphotoimageview.setImageResource(R.drawable.man);
            }
            holder.usernametextview.setText(worldpopulationlist.get(position).getUsername());
            holder.usernametextview.setTypeface(font);
            holder.contenttextview.setText(worldpopulationlist.get(position).getComment());
            holder.contenttextview.setTypeface(font);
            holder.timetextview.setText(worldpopulationlist.get(position).getTime());
            holder.timetextview.setTypeface(font);


            // Restore the checked state properly
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

        private class ViewHolder {

            public ImageView productimageview;
            public ImageView userphotoimageview;
            public TextView  usernametextview;
            public TextView  contenttextview;
            public TextView  timetextview;

        }
    }

}


