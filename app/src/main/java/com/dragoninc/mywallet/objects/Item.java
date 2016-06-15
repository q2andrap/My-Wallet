package com.dragoninc.mywallet.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by quan.le on 5/27/2016.
 */
public class Item implements Parcelable{

    public enum ITEM_TYPE {
        INCOME(0),
        EXPENSE(1);

        private int mValue;

        ITEM_TYPE(int i) {
            mValue = i;
        }

        public int getValue() {
            return mValue;
        }
    }
    private int itemID;
    private ITEM_TYPE itemType;
    private String itemName;
    private String itemDate;
    private int itemValue;
    private int catalogID;


    public Item(){
        itemType = ITEM_TYPE.INCOME;
        catalogID = -1;
    }
    protected Item(Parcel in) {
        itemID = in.readInt();
        itemName = in.readString();
        itemDate = in.readString();
        itemValue = in.readInt();
        catalogID = in.readInt();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(itemID);
        dest.writeString(itemName);
        dest.writeString(itemDate);
        dest.writeInt(itemValue);
        dest.writeInt(catalogID);
    }

    public int getCatalogID() {
        return catalogID;
    }

    public void setCatalogID(int catalogID) {
        this.catalogID = catalogID;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public ITEM_TYPE getItemType() {
        return itemType;
    }

    public void setItemType(ITEM_TYPE itemType) {
        this.itemType = itemType;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDate() {
        return itemDate;
    }

    public void setItemDate(String itemDate) {
        this.itemDate = itemDate;
    }

    public int getItemValue() {
        return itemValue;
    }

    public void setItemValue(int itemValue) {
        this.itemValue = itemValue;
    }

    @Override
    public String toString() {
        return  "itemID: " + itemID+ "; " +
                "itemType: " + itemType.getValue()+ "; " +
                "itemName: " + itemName+ "; " +
                "itemDate: " + itemDate+ "; " +
                "itemValue: " + itemValue+ "; " +
                "catalogID: " + catalogID;
    }
}
