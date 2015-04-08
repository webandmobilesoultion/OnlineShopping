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


public class CategoryActivity extends Activity {
    String fileuri;
    String tagling;
    String retailerstr;
    ListView list;
    ImageButton category_save_imagebutton;
    private List<catagorysearchitem> data;
    private catagoryListAdapter adapter;
    Typeface  font;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Intent datagetintent = getIntent();
        fileuri = datagetintent.getStringExtra("serchresult");
        tagling=datagetintent.getStringExtra("tagline");
        retailerstr=datagetintent.getStringExtra("retailer");
        data = new ArrayList<catagorysearchitem>();
        data.add(new catagorysearchitem(1,"Automotive"));
        data.add(new catagorysearchitem(2,"Books & Magazines"));
        data.add(new catagorysearchitem(3,"Business & Office"));
        data.add(new catagorysearchitem(4,"Computing"));
        data.add(new catagorysearchitem(5,"Education"));
        data.add(new catagorysearchitem(6,"Electrical & Electronics"));
        data.add(new catagorysearchitem(7,"Entertainment"));
        data.add(new catagorysearchitem(8,"Fashion & Apparel"));
        data.add(new catagorysearchitem(9,"Financial"));
        data.add(new catagorysearchitem(10,"Food & Grocery"));
        data.add(new catagorysearchitem(11,"Gaming"));
        data.add(new catagorysearchitem(12,"Health & Beauty"));
        data.add(new catagorysearchitem(13,"Home & Garden"));
        data.add(new catagorysearchitem(14,"Internet"));
        data.add(new catagorysearchitem(15,"Mobile"));
        data.add(new catagorysearchitem(16,"Pets"));
        data.add(new catagorysearchitem(17,"Restaurants & Dining"));
        data.add(new catagorysearchitem(18,"Sports & Outdoors"));
        data.add(new catagorysearchitem(19,"Toys & Kids"));
        data.add(new catagorysearchitem(20,"Travel"));
        data.add(new catagorysearchitem(21,"Other"));
          font = Typeface.createFromAsset(getAssets(), "Questrial-Regular.ttf");
        TextView tile=(TextView)findViewById(R.id.category_title_textView);
        tile.setTypeface(font);
        list = (ListView) findViewById(R.id.categorylistView);
        //  list.setSelector(R.drawable.itemselect);
        adapter = new catagoryListAdapter(this, data);
        list.setAdapter(adapter);
        //list.setItemsCanFocus(false);
        category_save_imagebutton=(ImageButton)findViewById(R.id.category_save_imageButton);
        category_save_imagebutton.setOnClickListener(new View.OnClickListener() {
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
                intent = new Intent(CategoryActivity.this, ProductEnter.class);
                intent.putExtra("serchresult",fileuri);
                intent.putExtra("cotagoryresult",ctagory);
                intent.putExtra("tagline",tagling);
                intent.putExtra("retailer",retailerstr);

                startActivity(intent);
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
            }
        });
        ImageButton fillterButton = (ImageButton) findViewById(R.id.categoyr_backbutton);

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
                intent = new Intent(CategoryActivity.this, ProductEnter.class);
                intent.putExtra("serchresult",fileuri);
                intent.putExtra("cotagoryresult",ctagory);
                intent.putExtra("tagline",tagling);
                intent.putExtra("retailer",retailerstr);

                startActivity(intent);
                overridePendingTransition(R.anim.left_in, R.anim.right_out);

            }
        });



        ImageButton home_Button = (ImageButton) findViewById(R.id.home_category_imageButton);

        home_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;
                intent = new Intent(CategoryActivity.this, HomeActivity.class);


                startActivity(intent);

            }
        });

        ImageButton carmeraButton = (ImageButton) findViewById(R.id.camera_category_imageButton);

        carmeraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;
                intent = new Intent(CategoryActivity.this, CameraActivity.class);


                startActivity(intent);

            }
        });
        ImageButton alertButton = (ImageButton) findViewById(R.id.alert_category_imagebutton);

        alertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;
                intent = new Intent(CategoryActivity.this,AlarmActivity.class);


                startActivity(intent);

            }
        });
        ImageButton profileButton = (ImageButton) findViewById(R.id.profile_category_imageButton);

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;
                intent = new Intent(CategoryActivity.this, FavouritsActivity.class);


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
            font = Typeface.createFromAsset(getAssets(), "Questrial-Regular.ttf");
            holder.filtertext.setText("" + item.getCatagory());
            holder.filtertext.setTypeface(font);

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
