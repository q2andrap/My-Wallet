package com.dragoninc.mywallet;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.dragoninc.mywallet.database.DBHelper;
import com.dragoninc.mywallet.fragment.DateDetailFragment;
import com.dragoninc.mywallet.objects.Category;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private ArrayList<Category> mCatelories;
    private DBHelper mDBHelper;
    private int mPressedCount = 0;
    private String mCurrentFragmentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCatelories = new ArrayList<>();
        mDBHelper = new DBHelper(this);
        getCategories();

        Fragment frag = new DateDetailFragment();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_up,
                R.anim.slide_out_down);
        ft.add(R.id.fragment_container, frag).commit();
    }

    public void setCurrentFragmentName(String name){
        mCurrentFragmentName = name;
    }

    public ArrayList<Category> getCatelories() {
        return mCatelories;
    }

    public DBHelper getDBHelper() {
        return mDBHelper;
    }

    private void getCategories() {
        int numOfRow = mDBHelper.numberOfCategoryTableRows();
        if (numOfRow > 0)
        {
            mCatelories = mDBHelper.getAllCategoryRows();
        }else {
            String[] name = getResources().getStringArray(R.array.catelory_name);
            String[] icon = getResources().getStringArray(R.array.catelory_icon_name);
            for (int i = 0; i < name.length; i++) {
                Category ca = new Category();
                ca.setName(name[i]);
                ca.setIconName(icon[i]);
                mCatelories.add(ca);
                mDBHelper.insertCategory(name[i], icon[i]);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (!mCurrentFragmentName.equals(DateDetailFragment.class.getSimpleName())) {
            super.onBackPressed();
            return;
        }
        mPressedCount++;
        if (mPressedCount == 1){
            Toast.makeText(this, getString(R.string.string_backpressed_exit), Toast.LENGTH_SHORT).show();
            (new Handler()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    mPressedCount = 0;
                }
            }, 2000);
            return;
        }
        if (mPressedCount == 2)
            super.onBackPressed();
    }
}
