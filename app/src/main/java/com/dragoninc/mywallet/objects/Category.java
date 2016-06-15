package com.dragoninc.mywallet.objects;

/**
 * Created by quan.le on 5/30/2016.
 */
public class Category {
    private int mId;
    private String mName;
    private String mIconName;

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getIconName() {
        return mIconName;
    }

    public void setIconName(String mIconName) {
        this.mIconName = mIconName;
    }

    @Override
    public String toString() {
        return "id: "+ mId + "; name: " + mName + "; icon: " + mIconName;
    }
}
