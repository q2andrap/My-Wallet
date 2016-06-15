package com.dragoninc.mywallet.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.dragoninc.mywallet.MainActivity;
import com.dragoninc.mywallet.R;
import com.dragoninc.mywallet.listener.EmptyDataCallback;
import com.dragoninc.mywallet.objects.Item;
import com.dragoninc.mywallet.objects.ItemAdapter;
import com.samsistemas.calendarview.widget.CalendarView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by quan.le on 5/26/2016.
 */
public class DateDetailFragment extends BaseFragment implements View.OnClickListener, CalendarView.OnDateClickListener, CalendarView.OnMonthChangedListener, CalendarView.OnDateLongClickListener {

    private static final String TAG = DateDetailFragment.class.getSimpleName();
    private CalendarView mCalendarView;
    private ListView mListView;
    private ImageView mBtnSummary;
    private TextView mNoDataText;
    private ArrayList<Item> mItems;
    private ItemAdapter mAdapter;
    private String mCurrentDate;
    private String mCurrentMonth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((MainActivity)getActivity()).setCurrentFragmentName(this.getClass().getSimpleName());
        mRootView = container.getRootView();
        mParentView = inflater.inflate(R.layout.fragment_datedetail, null);
        return mParentView;
    }

    @Override
    protected void initVariables() {
    }

    @Override
    protected void initActions() {
        //set ToolBar View
        setTitle(getString(R.string.string_titleMain));
        showButtonLeft(false);
        showButtonRight(true);
        mBtnRight.setImageResource(R.drawable.ic_add);
        mBtnRight.setOnClickListener(this);

        mCalendarView = (CalendarView) mParentView.findViewById(R.id.calendar);
        mCalendarView.setFirstDayOfWeek(Calendar.MONDAY);
        mCalendarView.setIsOverflowDateVisible(false);
        mCalendarView.setCurrentDay(new Date(System.currentTimeMillis()));
        mCalendarView.setBackButtonColor(R.color.colorAccent);
        mCalendarView.setNextButtonColor(R.color.colorAccent);
        mCalendarView.refreshCalendar(Calendar.getInstance(Locale.getDefault()));

        if (!TextUtils.isEmpty(mCurrentDate)) {
            try {
                mCalendarView.setDateAsSelected((new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())).parse(mCurrentDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            mItems = ((MainActivity)getActivity()).getDBHelper().getAllItemInDate(mCurrentDate);
        } else {
            Calendar c = Calendar.getInstance();
            int day = c.get(Calendar.DAY_OF_MONTH);
            int month = c.get(Calendar.MONTH);
            int year = c.get(Calendar.YEAR);
            mCurrentMonth = String.format("%02d", month+1);
            mCurrentDate = year + "-"+ String.format("%02d-", month+1) + String.format("%02d", day) ;
            mItems = ((MainActivity)getActivity()).getDBHelper().getAllItemInDate(mCurrentDate);
        }
        mCalendarView.setOnDateClickListener(this);
        mCalendarView.setOnMonthChangedListener(this);
        mCalendarView.setOnDateLongClickListener(this);

        mNoDataText = (TextView) mParentView.findViewById(R.id.noDataText);
        if (mItems.size() == 0)
            mNoDataText.setVisibility(View.VISIBLE);
        else
            mNoDataText.setVisibility(View.GONE);

        mBtnSummary = (ImageView) mParentView.findViewById(R.id.btnSummary);
        mBtnSummary.setOnClickListener(this);

        mListView = (ListView) mParentView.findViewById(R.id.listmoney);

        mAdapter = new ItemAdapter(getActivity(), mItems, new EmptyDataCallback() {
            @Override
            public void onEmpty() {
                mNoDataText.setVisibility(View.VISIBLE);
            }
        });
        mListView.setAdapter(mAdapter);

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, int position, long id) {
                final LinearLayout editHolder = (LinearLayout) view.findViewById(R.id.editHolder);
                final LinearLayout itemHolder = (LinearLayout) view.findViewById(R.id.itemHolder);
                editHolder.animate().x(view.getWidth() - editHolder.getWidth());
                itemHolder.animate().x(- editHolder.getWidth());
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        editHolder.animate().translationXBy(1000);
                        itemHolder.animate().x(0);
                    }
                }, 2000);
                return false;
            }
        });
    }

    private void doAdd(){
        Fragment addItem = new AddItemFragment();
        FragmentTransaction ft = this.getActivity().getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_up,
                R.anim.slide_out_down);
        Bundle b = new Bundle();
        b.putString("date", mCurrentDate);
        addItem.setArguments(b);
        ft.addToBackStack(DateDetailFragment.class.getSimpleName());
        ft.replace(R.id.fragment_container, addItem).commit();
    }

    private void doMonthSummary() {
        SummaryDialog dialog = new SummaryDialog(getActivity());
        dialog.setType(SummaryDialog.SUMMARY_TYPE.MONTH_SUMMARY);
        dialog.setMonth(mCurrentMonth);
        dialog.show();
    }

    private void doDateSummary(Date date) {
        SummaryDialog dialog = new SummaryDialog(getActivity());
        dialog.setType(SummaryDialog.SUMMARY_TYPE.DATE_SUMMARY);
        dialog.setDate(date);
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnRight:
                doAdd();
                break;
            case R.id.btnSummary:
                doMonthSummary();
                break;
        }
    }

    @Override
    public void onDateClick(@NonNull Date selectedDate) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String date = df.format(selectedDate);
        mCurrentDate = date;
        mItems = ((MainActivity)getActivity()).getDBHelper().getAllItemInDate(date);
        if (mItems.size() == 0) {
            mNoDataText.setVisibility(View.VISIBLE);
        } else {
            mNoDataText.setVisibility(View.GONE);
        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.refreshEvents(mItems);
            }
        });
    }

    @Override
    public void onMonthChanged(@NonNull Date monthDate) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.getDefault());
        String monthSelected = df.format(monthDate);

        mCurrentMonth = monthFormat.format(monthDate);

        mCalendarView.setDateAsSelected(monthDate);
        mItems = ((MainActivity)getActivity()).getDBHelper().getAllItemInDate(monthSelected);
        if (mItems.size() == 0) {
            mNoDataText.setVisibility(View.VISIBLE);
        } else {
            mNoDataText.setVisibility(View.GONE);
        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.refreshEvents(mItems);
            }
        });
    }

    @Override
    public void onDateLongClick(@NonNull Date selectedDate) {
        doDateSummary(selectedDate);
    }
}
