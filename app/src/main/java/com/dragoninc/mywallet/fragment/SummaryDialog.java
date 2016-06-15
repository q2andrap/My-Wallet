package com.dragoninc.mywallet.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dragoninc.mywallet.MainActivity;
import com.dragoninc.mywallet.R;
import com.dragoninc.mywallet.objects.Item;
import com.dragoninc.mywallet.utils.Utils;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by quan.le on 6/1/2016.
 */
public class SummaryDialog extends Dialog implements View.OnClickListener {

    private ArrayList<Item> mItems;

    public enum SUMMARY_TYPE{
        MONTH_SUMMARY,
        DATE_SUMMARY
    }
    private SUMMARY_TYPE mType;
    private TextView mTextTitle, mTextValueBalance, mTextValueIncome, mTextValueExpense;
    private ImageView mBtnMoreDetail;
    private Context mContext;
    private String mMonth;
    private Date mDate;

    public void setMonth(String month) {
        this.mMonth = month;
    }

    public void setDate(Date mDate) {
        this.mDate = mDate;
    }

    public SummaryDialog(Context context) {
        super(context, R.style.SumDialogTheme);
        this.setContentView(R.layout.dialog_summary);

        mContext = context;

        this.getWindow().setLayout((int)(Utils.getScreenWidth(context)*0.9f), (int)(Utils.getScreenWidth(context)*0.6f));

        mTextTitle = (TextView) this.findViewById(R.id.summary_title);
        mTextValueBalance = (TextView) this.findViewById(R.id.textValueBalance);
        mTextValueIncome = (TextView) this.findViewById(R.id.textValueIncome);
        mTextValueExpense = (TextView) this.findViewById(R.id.textValueExpense);
        mBtnMoreDetail = (ImageView) this.findViewById(R.id.btnMoreDetail);
        mBtnMoreDetail.setOnClickListener(this);
    }

    public void setType(SUMMARY_TYPE type) {
        this.mType = type;
    }

    public void doSummary (){
        int income=0, expense=0, balance;

        if (this.mType == SUMMARY_TYPE.MONTH_SUMMARY) {
            DateFormatSymbols ds = new DateFormatSymbols(Locale.US);
            String monthName = ds.getMonths()[Integer.parseInt(mMonth) - 1];
            mTextTitle.setText(mContext.getString(R.string.string_summary_month_title) + monthName);
            mItems = ((MainActivity) mContext).getDBHelper().getAllItemInMonth(mMonth);
        } else {
            mBtnMoreDetail.setVisibility(View.GONE);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String date = df.format(mDate);
            mTextTitle.setText(mContext.getString(R.string.string_summary_date_title) + date);
            mItems = ((MainActivity) mContext).getDBHelper().getAllItemInDate(date);
        }
        for (Item item : mItems){
            if (item.getItemType() == Item.ITEM_TYPE.INCOME)
                income += item.getItemValue();
            else
                expense += item.getItemValue();
        }
        balance = income - expense;

        mTextValueBalance.setText(Utils.formatInt(balance));
        mTextValueIncome.setText(Utils.formatInt(income));
        mTextValueExpense.setText(Utils.formatInt(expense));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.dismiss();
    }

    @Override
    public void show() {
        doSummary();
        super.show();
    }

    @Override
    public void onClick(View v) {
        Fragment moreDetail = new MoreDetailSummaryFragment();
        FragmentTransaction ft = ((AppCompatActivity)mContext).getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_up,
                R.anim.slide_out_down);
        ft.addToBackStack(DateDetailFragment.class.getSimpleName());
        Bundle b = new Bundle();
        b.putString("month", mMonth);
        moreDetail.setArguments(b);
        ft.replace(R.id.fragment_container, moreDetail).commit();
        dismiss();
    }
}
