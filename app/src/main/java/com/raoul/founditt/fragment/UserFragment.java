package com.raoul.founditt.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
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

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.raoul.founditt.CommentActivity;
import com.raoul.founditt.HomeActivity;
import com.raoul.founditt.R;
import com.raoul.founditt.Timeutillity.TimeAgo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment {
    private List<userSearch> data;
    private SearchListAdapter adapter;
    ListView listview;
    List<ParseUser> ob;
    ProgressDialog mProgressDialog;
    View rootView;
    Typeface font;
    TimeAgo timeAgo;
    Date regtime;

    public UserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_user, container, false);
        listview=(ListView)rootView.findViewById(R.id.user_listView);
        font = Typeface.createFromAsset(getActivity().getAssets(), "Questrial-Regular.ttf");
        timeAgo=new TimeAgo(getActivity());
        return rootView;
    }
    public void loaduser(String retailer){

        new RetaierRemoteDataTask().execute(retailer);


    }
    public class RetaierRemoteDataTask extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected Void doInBackground(String... params) {
            // Create the array
            data = new ArrayList<userSearch>();

            try {
                ParseQuery<ParseUser> photosFromCurrentUserQuery = ParseUser.getQuery();
                photosFromCurrentUserQuery.whereContains("name", params[0]);
                //photosFromCurrentUserQuery.include("user.userphoto");
                //photosFromCurrentUserQuery.whereExists("user");
                ob= photosFromCurrentUserQuery.find();
                for (ParseUser country : ob) {
                    userSearch map = new userSearch();
                    if (!( country.get("userphoto") ==null)){


                        if(!(country.get("facebookId") ==null)){
                            if (country.get("facebook").equals("true")){

                                String url="https://graph.facebook.com/"+country.get("facebookId")+"/picture?type=large";
                                map.setImageurl(url);
                            }
                            else {

                                ParseFile userimage = (ParseFile) country.get("userphoto");
                                map.setImageurl(userimage.getUrl());

                            }

                        }

                        else {

                            ParseFile userimage = (ParseFile) country.get("userphoto");
                            map.setImageurl(userimage.getUrl());

                        }

                    }
                    else{
                        if(!(country.get("facebookId") ==null)){
                            String url="https://graph.facebook.com/"+country.get("facebookId")+"/picture?type=large";
                            map.setImageurl(url);
                        }
                        else {
                            map.setImageurl("");
                        }
                    }
                    regtime=country.getCreatedAt();

                    map.setRegtime(timeAgo.timeAgo(regtime));
                    map.setUserid(country.getObjectId());
                    map.setUsername((String) country.get("name"));


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

            // Pass the results into ListViewAdapter.java
            adapter = new SearchListAdapter(getActivity(),
                    data);
            // Binds the Adapter to the ListView
            listview.setAdapter(adapter);
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    Intent zoom = new Intent(getActivity(), HomeActivity.class);
                    zoom.putExtra("userID",  data.get(position).getUserid());
                    startActivity(zoom);
                    getActivity().overridePendingTransition(R.anim.left_in, R.anim.right_out);
                    // Toast.makeText(HomeActivity.this,worldpopulationlist.get(position).getID(),Toast.LENGTH_SHORT).show();

//                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }
            });
            // Close the progressdialog
//            mProgressDialog.dismiss();
        }
    }





    public class SearchListAdapter extends BaseAdapter {

        Context context;
        LayoutInflater inflater;


        private List<userSearch> worldpopulationlist = null;
        private ArrayList<userSearch> arraylist;
        /**
         * Constructor from a list of items
         */
        public SearchListAdapter(Context context, List<userSearch>  worldpopulationlist) {

            this.context = context;
            this.worldpopulationlist = worldpopulationlist;
            inflater = LayoutInflater.from(context);
            this.arraylist = new ArrayList<userSearch>();
            this.arraylist.addAll(worldpopulationlist);

        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            final ViewHolder holder;
            if (view == null) {
                holder = new ViewHolder();
                view = inflater.inflate(R.layout.user_item_layout, null);
                holder.retailername_textview=(TextView)view.findViewById(R.id.username_textView);
                holder.regdate_textview=(TextView)view.findViewById(R.id.create_textView);
                holder.userphoto_imageview=(ImageView)view.findViewById(R.id.user_imageView);

                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            holder.retailername_textview.setTypeface(font);
            holder.regdate_textview.setTypeface(font);
            holder.retailername_textview.setText(worldpopulationlist.get(position).getUsername());
            holder.regdate_textview.setText(worldpopulationlist.get(position).getRegtime());
            if(!worldpopulationlist.get(position).getImageurl().equals("")){
//                imageLoader.DisplayImage(worldpopulationlist.get(position).getIamgeurl(),holder.userphotoimageview);
                Picasso.with(context).load(worldpopulationlist.get(position).getImageurl()).into(holder.userphoto_imageview);
            }
            else {
                holder.userphoto_imageview.setImageResource(R.drawable.man);
            }
            // Set some view properties


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
            public TextView retailername_textview;
            public ImageView userphoto_imageview;
            public TextView regdate_textview;



        }
    }
    public class userSearch{

        String userid;
        String username;
        String imageurl;
        String regtime;

        public void setRegtime(String regtime) {
            this.regtime = regtime;
        }

        public String getRegtime() {
            return regtime;
        }

        public void setImageurl(String imageurl) {
            this.imageurl = imageurl;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getImageurl() {
            return imageurl;
        }

        public String getUserid() {
            return userid;
        }

        public String getUsername() {
            return username;
        }
    }

}
