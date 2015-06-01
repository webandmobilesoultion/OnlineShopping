package com.raoul.founditt.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Toast;

import com.parse.GetCallback;
import com.raoul.founditt.CommentActivity;
import com.raoul.founditt.FavouritsActivity;
import com.raoul.founditt.ImageLoadPackge.ImageLoader;
import com.raoul.founditt.ImageLoadPackge.ImageLoaderGrid;
import com.raoul.founditt.R;
import com.raoul.founditt.listmodel.favourteitem;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class PortfolioActivity extends Fragment {
    ProgressDialog mProgressDialog;
    public List<favourteitem> data = null;
    private homeItemListAdapter adapter;
    GridView gridview;
    List<ParseObject> ob;
    View rootView;
         @Override
    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_portfolio);
             super.onCreate(savedInstanceState);
             setRetainInstance(true);
        new RemoteDataTask().execute();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_portfolio, container, false);
//        activity = (TextTabActivity) getActivity();
        gridview=(GridView)rootView.findViewById(R.id.follow_gridView);
        return rootView;
    }
    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(getActivity());
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
            data = new ArrayList<favourteitem>();

            try {
                ParseQuery<ParseObject> photosFromCurrentUserQuery = ParseQuery.getQuery("Photo");
                photosFromCurrentUserQuery.whereEqualTo("user", ParseUser.getCurrentUser());
                photosFromCurrentUserQuery.whereExists("image");
                ob = photosFromCurrentUserQuery.find();
                for (ParseObject country : ob) {
                    // Locate images in flag column
                    ParseFile image = (ParseFile) country.get("image");

                    final favourteitem map = new favourteitem();

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
            gridview.setAdapter(adapter);
            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    data.get(position).getImageID();
                    Intent zoom = new Intent(getActivity(), CommentActivity.class);
                    zoom.putExtra("photoID", data.get(position).getImageID());
                    startActivity(zoom);

                    // Toast.makeText(HomeActivity.this,worldpopulationlist.get(position).getID(),Toast.LENGTH_SHORT).show();

//                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }
            });




            gridview.setOnItemLongClickListener( new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View v, final int position, long id) {


                    new AlertDialog.Builder(getActivity())
                            .setTitle("Delete entry")
                            .setMessage("Are you sure you want to delete this photo?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Photo");
                                    query.getInBackground(data.get(position).getImageID(), new GetCallback<ParseObject>() {
                                                public void done(ParseObject country, ParseException e) {
                                                    if (e == null) {


                                                        try {
                                                            country.delete();
                                                        } catch (ParseException e1) {
                                                            e1.printStackTrace();
                                                        }
                                                        new RemoteDataTask().execute();
//

                                                    }
                                                }
                                            }
                                    );  // continue with delete
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();



                    return true;
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
