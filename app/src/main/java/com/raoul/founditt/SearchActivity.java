package com.raoul.founditt;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class SearchActivity extends Activity {
    ListView list;
    private List<search> data;
    private SearchListAdapter adapter;
    ListView listview;
    List<ParseObject> ob;
    ProgressDialog mProgressDialog;
    EditText searchedit;
    Typeface font;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mProgressDialog = new ProgressDialog(SearchActivity.this);
        // Set progressdialog title
        mProgressDialog.setTitle("Message");
        // Set progressdialog message
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setIndeterminate(false);
        searchedit=(EditText)findViewById(R.id.search_editText);
        font = Typeface.createFromAsset(getAssets(), "Questrial-Regular.ttf");
        searchedit.setTypeface(font);
        ImageButton fillterButton = (ImageButton) findViewById(R.id.back_search_imageButton);

        fillterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;
                intent = new Intent(SearchActivity.this, HomeActivity.class);


                startActivity(intent);
                overridePendingTransition(R.anim.left_in, R.anim.right_out);

            }
        });


        ImageButton home_Button = (ImageButton) findViewById(R.id.home_search_imageButton);

        home_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;
                intent = new Intent(SearchActivity.this, HomeActivity.class);


                startActivity(intent);

            }
        });

        ImageButton carmeraButton = (ImageButton) findViewById(R.id.camera_search_imageButton);

        carmeraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;
                intent = new Intent(SearchActivity.this, CameraActivity.class);


                startActivity(intent);

            }
        });
        ImageButton alertButton = (ImageButton) findViewById(R.id.alert_search_imagebutton);

        alertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;
                intent = new Intent(SearchActivity.this,AlarmActivity.class);


                startActivity(intent);

            }
        });
        ImageButton profileButton = (ImageButton) findViewById(R.id.profile_search_imageButton);

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;
                intent = new Intent(SearchActivity.this, FavouritsActivity.class);


                startActivity(intent);

            }
        });



        searchedit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    mProgressDialog.show();

                    new RemoteDataTask().execute(searchedit.getText().toString());
                    searchedit.setText("");
                }
                return false;
            }
        });




    }


    private class RemoteDataTask extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected Void doInBackground(String... params) {
            // Create the array
            data = new ArrayList<search>();

            try {
                ParseQuery<ParseObject> photosFromCurrentUserQuery = ParseQuery.getQuery("Photo");
                photosFromCurrentUserQuery.whereContains("retail", params[0]);
                //photosFromCurrentUserQuery.include("user.userphoto");
                //photosFromCurrentUserQuery.whereExists("user");
                ob= photosFromCurrentUserQuery.find();
                for (ParseObject country : ob) {


                    search map = new search();
                    map.setRetailerid(country.getObjectId());
                    map.setRetaqilsername((String)country.get("retail"));
                    map.setSearchstr(params[0]);

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
            listview = (ListView) findViewById(R.id.search_listView);
            // Pass the results into ListViewAdapter.java
            adapter = new SearchListAdapter(SearchActivity.this,
                    data);
            // Binds the Adapter to the ListView
            listview.setAdapter(adapter);
            // Close the progressdialog
            mProgressDialog.dismiss();
        }
    }





    public class SearchListAdapter extends BaseAdapter {

        Context context;
        LayoutInflater inflater;


        private List<search> worldpopulationlist = null;
        private ArrayList<search> arraylist;
        /**
         * Constructor from a list of items
         */
        public SearchListAdapter(Context context, List<search>  worldpopulationlist) {

            this.context = context;
            this.worldpopulationlist = worldpopulationlist;
            inflater = LayoutInflater.from(context);
            this.arraylist = new ArrayList<search>();
            this.arraylist.addAll(worldpopulationlist);

        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            final ViewHolder holder;
            if (view == null) {
                holder = new ViewHolder();
                view = inflater.inflate(R.layout.search_itemlayout, null);
                holder.retailername=(TextView)view.findViewById(R.id.search_itemtextView);
                holder.selectimagebut=(ImageButton)view.findViewById(R.id.search_itemimageButton);

                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            holder.retailername.setTypeface(font);
            // Set some view properties
            String[]display=worldpopulationlist.get(position).getRetaqilsername().split(worldpopulationlist.get(position).getSearchstr());
            if(display.length==2&& !(display[0]=="")){holder.retailername.setText(Html.fromHtml(display[0]+"<b>" +"<font size='10' color='#337744'>"+worldpopulationlist.get(position).getSearchstr()+"</font>"+ "</b>"+display[1]));}
            if(display.length==2&& display[0]==""){holder.retailername.setText(Html.fromHtml("<b>" + worldpopulationlist.get(position).getSearchstr()+ "</b>"+display[1]));}
            if(display.length==1){
                holder.retailername.setText(Html.fromHtml(display[0]+"<b>" +"<font size='10' color='#337744'>"+ worldpopulationlist.get(position).getSearchstr()+"</font>"+ "</b>"));
            }
            if(display.length==0){
                holder.retailername.setText(Html.fromHtml("<b>" + worldpopulationlist.get(position).getSearchstr()+ "</b>"));
            }
            if(display.length>2){
                holder.retailername.setText(worldpopulationlist.get(position).getRetaqilsername());
            }
//            holder.retailername.setText(Html.fromHtml("<b><u>" + worldpopulationlist.get(position).getSearchstr()+ "<b><u>"));
            holder.selectimagebut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(SearchActivity.this,HomeActivity.class);
                    intent.putExtra("retailer",worldpopulationlist.get(position).getRetaqilsername());
                    startActivity(intent);
                    overridePendingTransition(R.anim.left_in, R.anim.right_out);

                }
            });

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
            public TextView retailername;
            public ImageButton selectimagebut;


        }
    }

    public class search{

        String retailerid;
        String retaqilsername;
        String searchstr;

        public void setSearchstr(String searchstr) {
            this.searchstr = searchstr;
        }

        public String getSearchstr() {
            return searchstr;
        }

        public void setRetailerid(String retailerid) {
            this.retailerid = retailerid;
        }

        public void setRetaqilsername(String retaqilsername) {
            this.retaqilsername = retaqilsername;
        }

        public String getRetailerid() {
            return retailerid;
        }

        public String getRetaqilsername() {
            return retaqilsername;
        }
    }


}
