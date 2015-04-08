/**
 *
 */
package com.raoul.founditt;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.raoul.founditt.listmodel.searchitem;

import java.net.URI;
import java.util.List;

/**
 * Adapter that allows us to render a list of items
 *
 * @author marvinlabs
 */
public class SearchItemListAdapter extends ArrayAdapter<searchitem> {

    private LayoutInflater li;
    private URI uri;
    private Context mContext;
    private boolean flag=true;
    /**
     * Constructor from a list of items
     */
    public SearchItemListAdapter(Context context, List<searchitem> items) {
        super(context, 0, items);
        mContext=context;
        li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // The item we want to get the view for
        // --
        final searchitem item = getItem(position);

        // Re-use the view if possible
        // --
        final ViewHolder holder;
        if (convertView == null) {
            convertView = li.inflate(R.layout.search_itemlayout, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(R.id.holder, holder);
        } else {
            holder = (ViewHolder) convertView.getTag(R.id.holder);
        }

        // Set some view properties
        holder.searchitemtextview.setText("" + item.getCatagory());

        holder.itemcheckbut.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent zoom=new Intent(mContext, SearchresultActivity.class);
                searchitem item = (searchitem)getItem(position);
                String catagory_comunication=item.getCatagory();
                String content_comunication=item.getContente();
                zoom.putExtra("catagory", catagory_comunication);
                zoom.putExtra("contente", content_comunication);
                mContext.startActivity(zoom)  ;

            }
        });


        // Restore the checked state properly
        final ListView lv = (ListView) parent;
//        holder.layout.setChecked(lv.isItemChecked(position));

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

    private static class ViewHolder {
        public TextView searchitemtextview;

        public ImageButton itemcheckbut;

        public ViewHolder(View root) {
            searchitemtextview = (TextView) root.findViewById(R.id.search_itemtextView);
            itemcheckbut=(ImageButton)root.findViewById(R.id.search_itemimageButton);

//            layout = (CheckableRelativeLayout) root.findViewById(R.id.layout);
        }
    }
}
