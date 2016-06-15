package com.dragoninc.mywallet.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dragoninc.mywallet.MainActivity;
import com.dragoninc.mywallet.R;
import com.dragoninc.mywallet.database.DBHelper;
import com.dragoninc.mywallet.objects.Category;
import com.dragoninc.mywallet.objects.Item;
import com.dragoninc.mywallet.utils.Utils;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by quan.le on 6/2/2016.
 */
public class MoreDetailSummaryFragment extends BaseFragment {

    private LinearLayout mCategoryHolder;
    private ArrayList<Item> mItems;
    private ArrayList<Category> mCategories;
    private ArrayList<CategoryItemView> mCategoryViews = new ArrayList<>();
    private TextView mTextNoData;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((MainActivity)getActivity()).setCurrentFragmentName(this.getClass().getSimpleName());
        mRootView = container.getRootView();
        mParentView = inflater.inflate(R.layout.fragment_moredetail, null);
        return mParentView;
    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initActions() {
        mCategories = ((MainActivity)getActivity()).getCatelories();

        String month = getArguments().getString("month");
        mItems = ((MainActivity)getActivity()).getDBHelper().getAllItemInMonth(month);
        DateFormatSymbols ds = new DateFormatSymbols(Locale.US);
        String monthName = ds.getMonths()[Integer.parseInt(month) - 1];

        setTitle(getString(R.string.string_more_detail) + monthName);
        showButtonLeft(false);
        showButtonRight(false);

        mTextNoData = (TextView) mParentView.findViewById(R.id.textNodata);
        if (mItems.size() == 0)
            mTextNoData.setVisibility(View.VISIBLE);

        mCategoryHolder = (LinearLayout) mParentView.findViewById(R.id.categoryHolder);

        addCategoriesView();

        for (Item item : mItems) {
            View itemView = getItemView(item);
            itemView.setBackgroundResource(R.drawable.underlinebg);

            int categoryID = item.getCatalogID();
            CategoryItemView caview = mCategoryViews.get(categoryID-1);
            caview.setItemType(item.getItemType());
            caview.setValue(item.getItemValue());
            caview.setVisibility(View.VISIBLE);
            caview.getItemHolder().addView(itemView);
        }
    }

    public View getItemView(final Item item) {
        LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
        View rowView = inflater.inflate(R.layout.item_layout, null);

        LinearLayout valueHolder = (LinearLayout) rowView.findViewById(R.id.valueHolder);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)valueHolder.getLayoutParams();
        lp.weight = 2.5f;
        ImageView icon = (ImageView) rowView.findViewById(R.id.item_icon);
        icon.setVisibility(View.GONE);
        TextView name = (TextView) rowView.findViewById(R.id.itemTitlle);
        name.setText(item.getItemName());
        TextView date = (TextView) rowView.findViewById(R.id.itemDate);
        date.setText(item.getItemDate());
        TextView value = (TextView) rowView.findViewById(R.id.itemValue);
        TextView subvalue = (TextView) rowView.findViewById(R.id.itemSubValue);
        value.setText(Utils.formatInt(item.getItemValue()));
        TextView currency = (TextView) rowView.findViewById(R.id.itemCurrency);

        if(item.getItemType() == Item.ITEM_TYPE.INCOME) {
            value.setTextColor(mContext.getResources().getColor(R.color.colorIncome));
            subvalue.setTextColor(mContext.getResources().getColor(R.color.colorIncome));
            currency.setTextColor(mContext.getResources().getColor(R.color.colorIncome));
        } else {
            value.setTextColor(mContext.getResources().getColor(R.color.colorExpense));
            subvalue.setTextColor(mContext.getResources().getColor(R.color.colorExpense));
            currency.setTextColor(mContext.getResources().getColor(R.color.colorExpense));
        }
        LinearLayout editHolder = (LinearLayout) rowView.findViewById(R.id.editHolder);
        editHolder.animate().translationXBy(1000);

        LinearLayout delete = (LinearLayout) rowView.findViewById(R.id.btnDelete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)mContext).getDBHelper().deleteItems(DBHelper.ITEMS_TABLE_NAME, item.getItemID());
            }
        });

        LinearLayout edit = (LinearLayout) rowView.findViewById(R.id.btnEdit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment addItem = new AddItemFragment();
                FragmentTransaction ft = ((MainActivity)mContext).getSupportFragmentManager().beginTransaction();
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

    private void addCategoriesView() {
        for (Category ca : mCategories)
        {
            CategoryItemView view = (CategoryItemView)getActivity().getLayoutInflater().inflate(R.layout.item_moredetail_layout,null);
            view.loadComponents();
            view.setCategory(ca);
            view.setVisibility(View.GONE);
            mCategoryViews.add(view);
            mCategoryHolder.addView(view);
        }
    }
}
