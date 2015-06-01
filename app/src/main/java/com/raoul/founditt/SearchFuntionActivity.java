package com.raoul.founditt;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.raoul.founditt.ImageLoadPackge.ImageLoaderGrid;
import com.raoul.founditt.fragment.PhotoFragment;
import com.raoul.founditt.fragment.RetailerFragment;
import com.raoul.founditt.fragment.UserFragment;
import com.raoul.founditt.listmodel.favourteitem;
import com.soundcloud.android.crop.util.Log;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;


public class SearchFuntionActivity extends ActionBarActivity{
    int size;
    int post;
    ProgressDialog mProgressDialog;
    EditText searchedit;
    Typeface font;
    int selecflag=0;
    GridView gridView;
    List<ParseObject> ob;
    public List<favourteitem> data = null;
    private homeItemListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().hide();
        mProgressDialog = new ProgressDialog(SearchFuntionActivity.this);
        // Set progressdialog title
        mProgressDialog.setTitle("Message");
        // Set progressdialog message
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setIndeterminate(false);
        searchedit=(EditText)findViewById(R.id.search_editText);
        font = Typeface.createFromAsset(getAssets(), "Questrial-Regular.ttf");
        searchedit.setTypeface(font);
        gridView=(GridView)findViewById(R.id.search_gridView);



        ImageButton fillterButton = (ImageButton) findViewById(R.id.back_search_imageButton);

        fillterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;
                intent = new Intent(SearchFuntionActivity.this, HomeActivity.class);


                startActivity(intent);
                overridePendingTransition(R.anim.left_in, R.anim.right_out);

            }
        });


        ImageButton home_Button = (ImageButton) findViewById(R.id.home_search_imageButton);

        home_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;
                intent = new Intent(SearchFuntionActivity.this, HomeActivity.class);


                startActivity(intent);

            }
        });

        ImageButton carmeraButton = (ImageButton) findViewById(R.id.camera_search_imageButton);

        carmeraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;
                intent = new Intent(SearchFuntionActivity.this, CameraActivity.class);


                startActivity(intent);

            }
        });
        ImageButton alertButton = (ImageButton) findViewById(R.id.alert_search_imagebutton);

        alertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;
                intent = new Intent(SearchFuntionActivity.this,AlarmActivity.class);


                startActivity(intent);

            }
        });
        ImageButton profileButton = (ImageButton) findViewById(R.id.profile_search_imageButton);

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;
                intent = new Intent(SearchFuntionActivity.this, FavouritsActivity.class);


                startActivity(intent);

            }
        });



//        searchedit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
////                    mProgressDialog.show();
////
////                    new RemoteDataTask().execute(searchedit.getText().toString());
//                      retailerFragment.loadretailer(searchedit.getText().toString());
////                    searchedit.setText("");
//                }
//                return false;
//            }
//        });

        searchedit.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                String location = searchedit.getText().toString();

                android.util.Log.d("Select Flage",Integer.toString(selecflag));


                loadphoto(location);


//                    new GeocoderTask().execute(location);

            }
        });


    }

    public void loadphoto(String retailer){

        new RetaierRemoteDataTask().execute(retailer);


    }
    public class RetaierRemoteDataTask extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            mProgressDialog.show();


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
                android.util.Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Locate the listview in listview_main.xml

            // Pass the results into ListViewAdapter.java
            adapter = new homeItemListAdapter(SearchFuntionActivity.this,
                    data);
            // Binds the Adapter to the ListView
            gridView.setAdapter(adapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    data.get(position).getImageID();
                    Intent zoom = new Intent(SearchFuntionActivity.this, DetailsActivity.class);
                    zoom.putExtra("photoID", data.get(position).getImageID());
                    startActivity(zoom);
                    // Toast.makeText(HomeActivity.this,worldpopulationlist.get(position).getID(),Toast.LENGTH_SHORT).show();

                    overridePendingTransition(R.anim.left_in, R.anim.right_out);
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
