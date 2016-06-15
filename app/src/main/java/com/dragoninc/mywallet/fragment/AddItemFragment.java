package com.dragoninc.mywallet.fragment;

import android.animation.Animator;
import android.app.DatePickerDialog;
import android.app.Service;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dragoninc.mywallet.MainActivity;
import com.dragoninc.mywallet.R;
import com.dragoninc.mywallet.listener.SoftKeyboard;
import com.dragoninc.mywallet.objects.CategoryAdapter;
import com.dragoninc.mywallet.objects.Item;
import com.dragoninc.mywallet.utils.Utils;
import java.util.Calendar;

/**
 * Created by quan.le on 5/27/2016.
 */
public class AddItemFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = AddItemFragment.class.getSimpleName();

    private Item.ITEM_TYPE mType = Item.ITEM_TYPE.INCOME;
    //declare properties to handle textValue
    private TextView mTextValue, mTextSubValue, mTextCurrency;

    //declare properties handle tab button
    private TextView mBtnTabIncome, mBtnTabExpense;
    private LinearLayout mTabColor;
    private boolean mTabInLeft = true;

    //declare properties handle note
    private EditText mNoteEdit;
    private LinearLayout mKeyboardHolder;
    private SoftKeyboard mSoftKeyboard;

    //declare properties handle input fields
    private EditText mTextDate;
    private ImageView mBtnDatePicker;
    private int year, month, day;
    private Calendar calendar;

    //declare properties to handle select category
    private ImageView mBtnCategory;
    private Spinner mSpinner;

    //declare to handle keyboard
    private TextView mBtn0, mBtn1, mBtn2, mBtn3, mBtn4, mBtn5, mBtn6, mBtn7, mBtn8, mBtn9, mBtnComma, mBtnOK;
    private RelativeLayout mBtnClear;
    private Item mItem = null;
    private boolean mIsEditing = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((MainActivity)getActivity()).setCurrentFragmentName(this.getClass().getSimpleName());
        mRootView = container.getRootView();
        mParentView = inflater.inflate(R.layout.fragment_additem, null);
        InputMethodManager im = (InputMethodManager) mContext.getSystemService(Service.INPUT_METHOD_SERVICE);
        mSoftKeyboard = new SoftKeyboard(container, im);
        return mParentView;
    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initActions() {
        setTitle(getString(R.string.string_add_new));
        showButtonLeft(false);
        showButtonRight(false);

        String date = "";
        if (getArguments()!= null) {
            mItem = getArguments().getParcelable("item");
            if (mItem == null)
                date = getArguments().getString("date");
        }

        mBtnTabIncome = (TextView) mParentView.findViewById(R.id.btnTabIncome);
        mBtnTabExpense = (TextView) mParentView.findViewById(R.id.btnTabExpense);
        mTextValue = (TextView) mParentView.findViewById(R.id.textValue);
        mTextSubValue = (TextView) mParentView.findViewById(R.id.textSubValue);
        mTextCurrency = (TextView) mParentView.findViewById(R.id.textCurrency);
        mTabColor = (LinearLayout) mParentView.findViewById(R.id.tabColor);
        mBtnTabIncome.setOnClickListener(this);
        mBtnTabExpense.setOnClickListener(this);

        mTabColor.animate().setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (mTabInLeft) {
                    mType = Item.ITEM_TYPE.EXPENSE;
                    mBtnTabIncome.setTextColor(Color.BLACK);
                    mTabColor.setBackgroundResource(R.drawable.rounded_tabbuttonexpense);
                    mBtnTabExpense.setTextColor(Color.WHITE);
                    mTextValue.setTextColor(getResources().getColor(R.color.colorExpense));
                    mTextSubValue.setTextColor(getResources().getColor(R.color.colorExpense));
                    mTextCurrency.setTextColor(getResources().getColor(R.color.colorExpense));
                    mTabInLeft = false;
                } else {
                    mType = Item.ITEM_TYPE.INCOME;
                    mBtnTabIncome.setTextColor(Color.WHITE);
                    mTabColor.setBackgroundResource(R.drawable.rounded_tabbuttonincome);
                    mBtnTabExpense.setTextColor(Color.BLACK);
                    mTextValue.setTextColor(getResources().getColor(R.color.colorIncome));
                    mTextSubValue.setTextColor(getResources().getColor(R.color.colorIncome));
                    mTextCurrency.setTextColor(getResources().getColor(R.color.colorIncome));
                    mTabInLeft = true;
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        //init note editext
        mKeyboardHolder = (LinearLayout) mParentView.findViewById(R.id.keyboardHolder);
        mNoteEdit = (EditText) mParentView.findViewById(R.id.editNote);
        mNoteEdit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mSoftKeyboard.openSoftKeyboard();
                return false;
            }
        });
        mSoftKeyboard.setSoftKeyboardCallback(new SoftKeyboard.SoftKeyboardChanged()
        {

            @Override
            public void onSoftKeyboardHide()
            {
                mKeyboardHolder.animate().translationYBy(-1000);
            }

            @Override
            public void onSoftKeyboardShow()
            {
                mKeyboardHolder.animate().translationYBy(1000);
            }
        });

        mTextDate = (EditText) mParentView.findViewById(R.id.editDate);
        mTextDate.setText(date);
        mBtnDatePicker = (ImageView) mParentView.findViewById(R.id.datepicker);
        mBtnDatePicker.setOnClickListener(this);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        mBtnCategory = (ImageView) mParentView.findViewById(R.id.catelorypicker);
        mBtnCategory.setOnClickListener(this);
        mSpinner = (Spinner) mParentView.findViewById(R.id.spinnerCatelory);
        CategoryAdapter adt = new CategoryAdapter(getActivity(), ((MainActivity)getActivity()).getCatelories());
        mSpinner.setAdapter(adt);

        if (mItem != null) {
            setTitle(getString(R.string.string_edit_log));
            mIsEditing = true;
            setItemValue();
        }

        initKeyBoardButtons();
    }

    private void setItemValue() {
        mTextValue.setText(Utils.formatInt(mItem.getItemValue()));
        mNoteEdit.setText(mItem.getItemName());
        mTextDate.setText(mItem.getItemDate());
        mSpinner.setSelection(mItem.getCatalogID()-1);
        final ViewTreeObserver vto = mTabColor.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
               if (mItem.getItemType() == Item.ITEM_TYPE.EXPENSE)
                   moveTabRight();
                if (Build.VERSION.SDK_INT >= 16)
                    vto.removeOnGlobalLayoutListener(this);
                else
                    vto.removeGlobalOnLayoutListener(this);
            }
        });
    }

    private void initKeyBoardButtons() {
        mBtn1 = (TextView) mParentView.findViewById(R.id.btn1);
        mBtn1.setOnClickListener(this);
        mBtn2 = (TextView) mParentView.findViewById(R.id.btn2);
        mBtn2.setOnClickListener(this);
        mBtn3 = (TextView) mParentView.findViewById(R.id.btn3);
        mBtn3.setOnClickListener(this);
        mBtn4 = (TextView) mParentView.findViewById(R.id.btn4);
        mBtn4.setOnClickListener(this);
        mBtn5 = (TextView) mParentView.findViewById(R.id.btn5);
        mBtn5.setOnClickListener(this);
        mBtn6 = (TextView) mParentView.findViewById(R.id.btn6);
        mBtn6.setOnClickListener(this);
        mBtn7 = (TextView) mParentView.findViewById(R.id.btn7);
        mBtn7.setOnClickListener(this);
        mBtn8 = (TextView) mParentView.findViewById(R.id.btn8);
        mBtn8.setOnClickListener(this);
        mBtn9 = (TextView) mParentView.findViewById(R.id.btn9);
        mBtn9.setOnClickListener(this);
        mBtn0 = (TextView) mParentView.findViewById(R.id.btn0);
        mBtn0.setOnClickListener(this);
        mBtnComma = (TextView) mParentView.findViewById(R.id.btncomma);
        mBtnComma.setOnClickListener(this);
        mBtnClear = (RelativeLayout) mParentView.findViewById(R.id.btnClear);
        mBtnClear.setOnClickListener(this);
        mBtnOK = (TextView) mParentView.findViewById(R.id.btnOk);
        mBtnOK.setOnClickListener(this);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        mSoftKeyboard.unRegisterSoftKeyboardCallback();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnTabIncome:
                moveTabLeft();
                break;
            case R.id.btnTabExpense:
                moveTabRight();
                break;
            case R.id.datepicker:
                showDatePicker();
                break;
            case R.id.catelorypicker:
                showCategoryDialog();
                break;
            case R.id.btn1:
            case R.id.btn2:
            case R.id.btn3:
            case R.id.btn4:
            case R.id.btn5:
            case R.id.btn6:
            case R.id.btn7:
            case R.id.btn8:
            case R.id.btn9:
            case R.id.btn0:
                int value = Integer.parseInt(Utils.formatString(mTextValue.getText().toString()));
                int clickValue = Integer.parseInt(((TextView)v).getText().toString());
                int newvalue = value * 10 + clickValue;
                mTextValue.setText(Utils.formatInt(newvalue));
                break;
            case R.id.btnClear:
                int l = Integer.parseInt(Utils.formatString(mTextValue.getText().toString())) / 10;
                mTextValue.setText(Utils.formatInt(l));
                break;
            case R.id.btncomma:
                break;
            case R.id.btnOk:
                int _Value = Integer.parseInt(Utils.formatString(mTextValue.getText().toString()));
                String _Note = mNoteEdit.getText().toString();
                String _Date = mTextDate.getText().toString();
                if (_Value == 0) {
                    Toast.makeText(getActivity(), "Please type your Money", Toast.LENGTH_SHORT).show();
                    break;
                } else if  (TextUtils.isEmpty(_Note)) {
                    Toast.makeText(getActivity(), "Please type your Note", Toast.LENGTH_SHORT).show();
                    break;
                }else if (TextUtils.isEmpty(_Date)) {
                    Toast.makeText(getActivity(), "Please pick your Date", Toast.LENGTH_SHORT).show();
                    break;
                }

                Item item = mItem;
                if (item == null)
                    item = new Item();
                item.setItemValue(_Value);
                item.setItemType(mType);
                item.setItemName(_Note);
                item.setItemDate(_Date);
                item.setCatalogID(mSpinner.getSelectedItemPosition() + 1);

                if (!mIsEditing)
                    ((MainActivity)getActivity()).getDBHelper().insertItem(item);
                else
                    ((MainActivity)getActivity()).getDBHelper().updateItem(item);

                this.getActivity().getSupportFragmentManager().popBackStackImmediate();
                break;
        }
    }

    private void showDatePicker() {
        new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String date = year + "-"+ String.format("%02d-", monthOfYear+1) + String.format("%02d", dayOfMonth) ;
                mTextDate.setText(date);
            }
        }, year, month, day).show();
    }

    private void showCategoryDialog() {
        mSpinner.performClick();
    }

    private void moveTabLeft() {
        if (!mTabInLeft)
            mTabColor.animate().translationXBy(-mTabColor.getWidth());
    }

    private void moveTabRight() {
        if (mTabInLeft)
            mTabColor.animate().translationXBy(mTabColor.getWidth());
    }
}
