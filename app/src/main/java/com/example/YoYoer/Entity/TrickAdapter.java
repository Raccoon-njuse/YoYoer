package com.example.YoYoer.Entity;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.YoYoer.R;

import java.util.List;

public class TrickAdapter extends BaseAdapter implements Filterable {
    private Context context;
    private MyFilter myFilter;

    private List<Trick> backList;//backup for 原始数据
    private List<Trick> trickList;//这个数据是会变的

    public TrickAdapter(Context context, List<Trick> trickList) {
        this.context = context;
        this.trickList = trickList;
        backList = trickList;
    }

    @Override
    public int getCount() {
        return trickList.size();
    }

    @Override
    public Object getItem(int position) {
        return trickList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        View v = View.inflate(context, R.layout.trick_layout, null);
        TextView trick_layout_tv_content = v.findViewById(R.id.trick_layout_tv_content);
        TextView trick_layout_tv_time = v.findViewById(R.id.trick_layout_tv_time);

        //Set text fot TextView
        trick_layout_tv_content.setText(trickList.get(position).getContent());
        trick_layout_tv_time.setText(trickList.get(position).getTime());
        v.setTag(trickList.get(position).getId());
        return v;
    }

    @Override
    public Filter getFilter() {
        if (myFilter == null) {
            myFilter = new MyFilter();
        }
        return myFilter;
    }

    class MyFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            return null;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

        }
    }
}
