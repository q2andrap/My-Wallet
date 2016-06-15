package com.dragoninc.mywallet.fragment;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dragoninc.mywallet.R;
import com.dragoninc.mywallet.objects.Category;
import com.dragoninc.mywallet.objects.Item;
import com.dragoninc.mywallet.utils.Utils;

/**
 * Created by quan.le on 6/2/2016.
 */
public class CategoryItemView extends LinearLayout {
    private Context mContext;

    public CategoryItemView(Context context) {
        super(context);
        mContext = context;
    }

    public CategoryItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public CategoryItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    public void loadComponents(){
        mTextTitle = (TextView) this.findViewById(R.id.category_name);
        mIcon = (ImageView) this.findViewById(R.id.catalogIcon);
        mTextValue = (TextView) this.findViewById(R.id.textValue);
        mTextSubValue = (TextView) this.findViewById(R.id.textSubValue);
        mTextCurrency = (TextView) this.findViewById(R.id.textCurrency);
        mItemHolder = (LinearLayout) this.findViewById(R.id.itemHolder);
    }

    public void setCategory(Category ca) {
        mTextTitle.setText(ca.getName());
        mIcon.setImageResource(mContext.getResources().getIdentifier(ca.getIconName(),"drawable", mContext.getPackageName()));
    }

    public LinearLayout getItemHolder() {
        return mItemHolder;
    }

    private TextView mTextTitle;
    private ImageView mIcon;
    private TextView mTextValue;
    private TextView mTextSubValue;
    private TextView mTextCurrency;
    private LinearLayout mItemHolder;
    private int mValue = 0;
    private Item.ITEM_TYPE mType;

    public void setValue(int value) {
        if(mType == Item.ITEM_TYPE.INCOME)
            mValue += value;
        else
            mValue -= value;

        mTextValue.setText(Utils.formatInt(Math.abs(mValue)));
        if (mValue >=0){
            mTextValue.setTextColor(mContext.getResources().getColor(R.color.colorIncome));
            mTextSubValue.setTextColor(mContext.getResources().getColor(R.color.colorIncome));
            mTextCurrency.setTextColor(mContext.getResources().getColor(R.color.colorIncome));
        }else {
            mTextValue.setTextColor(mContext.getResources().getColor(R.color.colorExpense));
            mTextSubValue.setTextColor(mContext.getResources().getColor(R.color.colorExpense));
            mTextCurrency.setTextColor(mContext.getResources().getColor(R.color.colorExpense));
        }
    }

    public void setItemType(Item.ITEM_TYPE itemType) {
        mType = itemType;
    }
}
