package com.raoul.founditt;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.raoul.founditt.listmodel.catagorysearchitem;

import java.util.ArrayList;
import java.util.List;


public class LocationActivity extends Activity {
    String fileuri;
    String categorys;
    String tagling;
    String retailerstr;
    ListView list;
    ImageButton location_saveimagebut;
    private List<catagorysearchitem> data;
    private catagoryListAdapter adapter;
    TextView search_title;
    Typeface font;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);


        Intent datagetintent = getIntent();
        fileuri = datagetintent.getStringExtra("serchresult");
        categorys = datagetintent.getStringExtra("cotagoryresult");
        tagling=datagetintent.getStringExtra("tagline");
        retailerstr=datagetintent.getStringExtra("retailer");
        search_title=(TextView)findViewById(R.id.search_title_textView);
        search_title.setTypeface(font);
        font = Typeface.createFromAsset(getAssets(), "Questrial-Regular.ttf");
        search_title.setTypeface(font);
        data = new ArrayList<catagorysearchitem>();
        data.add(new catagorysearchitem(1,"Melbourne"));
        data.add(new catagorysearchitem(2,"Sydney"));
        data.add(new catagorysearchitem(3,"Perth"));
        data.add(new catagorysearchitem(4,"Brisbane"));
        data.add(new catagorysearchitem(5,"Adelaide"));
        data.add(new catagorysearchitem(6,"Darwin"));
        data.add(new catagorysearchitem(7,"Hobart"));
        data.add(new catagorysearchitem(8,"Gold Coast"));
        data.add(new catagorysearchitem(9,"Canberra"));
        data.add(new catagorysearchitem(10,"Online"));



        list = (ListView) findViewById(R.id.location_listView);
        //  list.setSelector(R.drawable.itemselect);
        adapter = new catagoryListAdapter(this, data);
        list.setAdapter(adapter);
        //list.setItemsCanFocus(false);
        ImageButton fillterButton = (ImageButton) findViewById(R.id.back_location_button);
        location_saveimagebut=(ImageButton)findViewById(R.id.location_save_imageButton);
        fillterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final StringBuffer sb = new StringBuffer();
                final SparseBooleanArray checkedItems = list.getCheckedItemPositions();

                boolean isFirstSelected = true;
                String ctagory = null;
                final int checkedItemsCount = checkedItems.size();
                for (int i = 0; i < checkedItemsCount; ++i) {
                    final int position = checkedItems.keyAt(i);

                    // This tells us the item status at the above position
                    // --
                    final boolean isChecked = checkedItems.valueAt(i);

                    if (isChecked) {

                        ctagory=data.get(position).getCatagory();
                        isFirstSelected = false;
                    }
                }
                Intent intent;
                intent = new Intent(LocationActivity.this, ProductEnter.class);
                intent.putExtra("serchresult",fileuri);
                intent.putExtra("location",ctagory);
                intent.putExtra("cotagoryresult",categorys);
                intent.putExtra("tagline",tagling);
                intent.putExtra("retailer",retailerstr);
                startActivity(intent);
                overridePendingTransition(R.anim.left_in, R.anim.right_out);

            }
        });

       location_saveimagebut.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {


               final StringBuffer sb = new StringBuffer();
               final SparseBooleanArray checkedItems = list.getCheckedItemPositions();

               boolean isFirstSelected = true;
               String ctagory = null;
               final int checkedItemsCount = checkedItems.size();
               for (int i = 0; i < checkedItemsCount; ++i) {
                   final int position = checkedItems.keyAt(i);

                   // This tells us the item status at the above position
                   // --
                   final boolean isChecked = checkedItems.valueAt(i);

                   if (isChecked) {

                       ctagory=data.get(position).getCatagory();
                       isFirstSelected = false;
                   }
               }
               Intent intent;
               intent = new Intent(LocationActivity.this, ProductEnter.class);
               intent.putExtra("serchresult",fileuri);
               intent.putExtra("location",ctagory);
               intent.putExtra("cotagoryresult",categorys);
               intent.putExtra("tagline",tagling);
               intent.putExtra("retailer",retailerstr);
               startActivity(intent);
               overridePendingTransition(R.anim.left_in, R.anim.right_out);

           }
       });

        ImageButton home_Button = (ImageButton) findViewById(R.id.home_location_imageButton);

        home_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;
                intent = new Intent(LocationActivity.this, HomeActivity.class);


                startActivity(intent);

            }
        });

        ImageButton carmeraButton = (ImageButton) findViewById(R.id.camera_location_imageButton);

        carmeraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;
                intent = new Intent(LocationActivity.this, CameraActivity.class);


                startActivity(intent);

            }
        });
        ImageButton alertButton = (ImageButton) findViewById(R.id.alert_location_imagebutton);

        alertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;
                intent = new Intent(LocationActivity.this,AlarmActivity.class);


                startActivity(intent);

            }
        });
        ImageButton profileButton = (ImageButton) findViewById(R.id.profile_location_imageButton);

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;
                intent = new Intent(LocationActivity.this, FavouritsActivity.class);


                startActivity(intent);

            }
        });


    }

    public class catagoryListAdapter extends ArrayAdapter<catagorysearchitem> {



        private LayoutInflater li;

        /**
         * Constructor from a list of items
         */
        public catagoryListAdapter(Context context, List<catagorysearchitem> items) {
            super(context, 0, items);
            li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // The item we want to get the view for
            // --
            final catagorysearchitem item = getItem(position);

            // Re-use the view if possible
            // --
            ViewHolder holder;
            if (convertView == null) {
                convertView = li.inflate(R.layout.catagory_itemlayout, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(R.id.holder, holder);
            } else {
                holder = (ViewHolder) convertView.getTag(R.id.holder);
            }

            // Set some view properties
            holder.filtertext.setTypeface(font);
            holder.filtertext.setText("" + item.getCatagory());


            // Restore the checked state properly
            final ListView lv = (ListView) parent;
            holder.layout.setChecked(lv.isItemChecked(position));

            return convertView;
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).getId();
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        private  class ViewHolder {
            public TextView filtertext;
            public CheckableRelativeLayout layout;

            public ViewHolder(View root) {
                filtertext= (TextView) root.findViewById(R.id.filter_textView);

                layout = (CheckableRelativeLayout) root.findViewById(R.id.layout);
            }
        }
    }

}
