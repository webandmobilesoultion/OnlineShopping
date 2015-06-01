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
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.raoul.founditt.HomeActivity;
import com.raoul.founditt.R;
import com.raoul.founditt.SearchActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RetailerFragment extends Fragment {
    ListView list;
    private List<search> data;
    private SearchListAdapter adapter;
    ListView listview;
    List<ParseObject> ob;
    ProgressDialog mProgressDialog;
    View rootView;
    Typeface font;

    public RetailerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_retailer, container, false);
        listview=(ListView)rootView.findViewById(R.id.retailer_listView);
        font = Typeface.createFromAsset(getActivity().getAssets(), "Questrial-Regular.ttf");
        return rootView;
    }
    public void loadretailer(String retailer){

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

            // Pass the results into ListViewAdapter.java
            adapter = new SearchListAdapter(getActivity(),
                    data);
            // Binds the Adapter to the ListView
            listview.setAdapter(adapter);
            // Close the progressdialog
//            mProgressDialog.dismiss();
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
            if(display.length==2&& !(display[0]=="")){holder.retailername.setText(Html.fromHtml(display[0] + "<b>" + "<font size='10' color='#337744'>" + worldpopulationlist.get(position).getSearchstr() + "</font>" + "</b>" + display[1]));}
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
                    Intent intent=new Intent(getActivity(),HomeActivity.class);
                    intent.putExtra("retailer",worldpopulationlist.get(position).getRetaqilsername());
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.left_in, R.anim.right_out);

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
