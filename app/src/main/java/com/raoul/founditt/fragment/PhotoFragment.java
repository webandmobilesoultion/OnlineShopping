package com.raoul.founditt.fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.raoul.founditt.CommentActivity;
import com.raoul.founditt.DetailsActivity;
import com.raoul.founditt.FavouritsActivity;
import com.raoul.founditt.HomeActivity;
import com.raoul.founditt.ImageLoadPackge.ImageLoaderGrid;
import com.raoul.founditt.R;
import com.raoul.founditt.Timeutillity.TimeAgo;
import com.raoul.founditt.listmodel.favourteitem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhotoFragment extends Fragment {

    View rootView;
    GridView gridView;
    List<ParseObject> ob;
    public List<favourteitem> data = null;
    private homeItemListAdapter adapter;
    public PhotoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_photo, container, false);
        gridView=(GridView)rootView.findViewById(R.id.photo_gridView);

        return rootView;
    }
    public void loadphoto(String retailer){

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
            data = new ArrayList<favourteitem>();

            try {
                ParseQuery<ParseObject> photosFromCurrentUserQuery = ParseQuery.getQuery("Photo");
                photosFromCurrentUserQuery.whereContains("tagline", params[0]);
                photosFromCurrentUserQuery.whereExists("image");
                photosFromCurrentUserQuery.whereEqualTo("expflag","false");
                //photosFromCurrentUserQuery.include("user.userphoto");
                //photosFromCurrentUserQuery.whereExists("user");
                ob = photosFromCurrentUserQuery.find();
                for (ParseObject country : ob) {
                    // Locate images in flag column


                    ParseFile image = (ParseFile) country.get("image");
                    favourteitem map = new favourteitem();

                    map.setImageID(country.getObjectId());


                    map.setInmageurl(image.getUrl());







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
            adapter = new homeItemListAdapter(getActivity(),
                    data);
            // Binds the Adapter to the ListView
            gridView.setAdapter(adapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    data.get(position).getImageID();
                    Intent zoom = new Intent(getActivity(), DetailsActivity.class);
                    zoom.putExtra("photoID", data.get(position).getImageID());
                    startActivity(zoom);
                    // Toast.makeText(HomeActivity.this,worldpopulationlist.get(position).getID(),Toast.LENGTH_SHORT).show();

                    getActivity().overridePendingTransition(R.anim.left_in, R.anim.right_out);
                }
            });
            // Close the progressdialog
//            mProgressDialog.dismiss();
        }
    }





    public class homeItemListAdapter extends BaseAdapter {
        boolean flag = true;
        Context context;
        LayoutInflater inflater;
        ImageLoaderGrid imageLoader;
        private ParseFile image;
        private List<favourteitem> worldpopulationlist = null;
        private ArrayList<favourteitem> arraylist;

        /**
         * Constructor from a list of items
         */
        public homeItemListAdapter(Context context, List<favourteitem> worldpopulationlist) {

            this.context = context;
            this.worldpopulationlist = worldpopulationlist;
            inflater = LayoutInflater.from(context);
            this.arraylist = new ArrayList<favourteitem>();
            this.arraylist.addAll(worldpopulationlist);
            imageLoader = FavouritsActivity.imageLoader;
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            final ViewHolder holder;

            if (view == null) {
                holder = new ViewHolder();
                view = inflater.inflate(R.layout.favourits_item_layout, null);

                holder.productimageview = (ImageView) view.findViewById(R.id.product_portfolio_imageView);


                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }


//            imageLoader.DisplayImage(worldpopulationlist.get(position).getInmageurl(), holder.productimageview);
            Picasso.with(context).load(worldpopulationlist.get(position).getInmageurl()).into(holder.productimageview);



            // Restore the checked state properly
            final GridView lv = (GridView) parent;
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

        }
    }

}