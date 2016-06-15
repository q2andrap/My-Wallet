package com.dragoninc.mywallet.objects;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dragoninc.mywallet.MainActivity;
import com.dragoninc.mywallet.R;
import com.dragoninc.mywallet.database.DBHelper;
import com.dragoninc.mywallet.fragment.AddItemFragment;
import com.dragoninc.mywallet.fragment.DateDetailFragment;
import com.dragoninc.mywallet.listener.EmptyDataCallback;
import com.dragoninc.mywallet.utils.Utils;

import java.util.ArrayList;

/**
 * Created by quan.le on 5/27/2016.
 */
public class ItemAdapter extends ArrayAdapter<Item> {
    private final static int MIN_DISTANCE = 200;
    private ArrayList<Item> mItems = new ArrayList<>();
    private Context m_Context;
    private EmptyDataCallback mListener;
    public ItemAdapter(Context context, ArrayList<Item> objects, EmptyDataCallback listener) {
        super(context, android.R.layout.simple_list_item_1);
        m_Context = context;
        mItems = objects;
        mListener = listener;
    }

    public int getCount() {
        if (mItems != null)
            return mItems.size();
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity)m_Context).getLayoutInflater();
        View rowView;
        if (convertView == null)
            rowView = inflater.inflate(R.layout.item_layout, null);
        else
            rowView = convertView;

        final Item item = mItems.get(position);

        ImageView icon = (ImageView) rowView.findViewById(R.id.item_icon);
        int id = m_Context.getResources().getIdentifier(((MainActivity)m_Context).getCatelories().get(item.getCatalogID() - 1).getIconName(), "drawable", m_Context.getPackageName());
        icon.setImageResource(id);
        TextView name = (TextView) rowView.findViewById(R.id.itemTitlle);
        name.setText(item.getItemName());
        TextView date = (TextView) rowView.findViewById(R.id.itemDate);
        date.setText(item.getItemDate());
        TextView value = (TextView) rowView.findViewById(R.id.itemValue);
        TextView subvalue = (TextView) rowView.findViewById(R.id.itemSubValue);
        value.setText(Utils.formatInt(item.getItemValue()));
        TextView currency = (TextView) rowView.findViewById(R.id.itemCurrency);

        if(mItems.get(position).getItemType() == Item.ITEM_TYPE.INCOME) {
            value.setTextColor(m_Context.getResources().getColor(R.color.colorIncome));
            subvalue.setTextColor(m_Context.getResources().getColor(R.color.colorIncome));
            currency.setTextColor(m_Context.getResources().getColor(R.color.colorIncome));
        } else {
            value.setTextColor(m_Context.getResources().getColor(R.color.colorExpense));
            subvalue.setTextColor(m_Context.getResources().getColor(R.color.colorExpense));
            currency.setTextColor(m_Context.getResources().getColor(R.color.colorExpense));
        }
        LinearLayout editHolder = (LinearLayout) rowView.findViewById(R.id.editHolder);
        editHolder.animate().translationXBy(1000);

        LinearLayout delete = (LinearLayout) rowView.findViewById(R.id.btnDelete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)m_Context).getDBHelper().deleteItems(DBHelper.ITEMS_TABLE_NAME, item.getItemID());
                mItems.remove(position);
                notifyDataSetChanged();

                if (mItems.size() == 0)
                    mListener.onEmpty();
            }
        });

        LinearLayout edit = (LinearLayout) rowView.findViewById(R.id.btnEdit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment addItem = new AddItemFragment();
                FragmentTransaction ft = ((MainActivity)m_Context).getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.slide_in_up,
                        R.anim.slide_out_down);
                Bundle b = new Bundle();
                b.putParcelable("item", item);
                addItem.setArguments(b);
                ft.addToBackStack(DateDetailFragment.class.getSimpleName());
                ft.replace(R.id.fragment_container, addItem).commit();
            }
        });

        return rowView;
    }

    public void refreshEvents(ArrayList<Item> items) {
        this.mItems.clear();
        this.mItems.addAll(items);
        notifyDataSetChanged();
    }
}
