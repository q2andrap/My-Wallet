package com.dragoninc.mywallet.objects;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dragoninc.mywallet.R;

import java.util.ArrayList;

/**
 * Created by quan.le on 5/30/2016.
 */
public class CategoryAdapter extends ArrayAdapter<Category>{

    private Context mContext;
    private ArrayList<Category> mList;

    public CategoryAdapter(Context context, ArrayList<Category> list) {
        super(context, android.R.layout.simple_dropdown_item_1line, list);
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
        View view;
        if (convertView != null) {
            view = convertView;
        }else {
            view = inflater.inflate(R.layout.category_layout, null);
        }
        ImageView icon = (ImageView) view.findViewById(R.id.category_icon);
        int id = mContext.getResources().getIdentifier(mList.get(position).getIconName(), "drawable", mContext.getPackageName());
        icon.setImageResource(id);

        TextView name = (TextView) view.findViewById(R.id.category_name);
        name.setText(mList.get(position).getName());
        return view;
    }



    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
        View view;
        if (convertView != null) {
            view = convertView;
        }else {
            view = inflater.inflate(R.layout.category_layout, null);
        }
        ImageView icon = (ImageView) view.findViewById(R.id.category_icon);
        int id = mContext.getResources().getIdentifier(mList.get(position).getIconName(), "drawable", mContext.getPackageName());
        icon.setImageResource(id);

        TextView name = (TextView) view.findViewById(R.id.category_name);
        name.setText(mList.get(position).getName());
        return view;
    }
}
