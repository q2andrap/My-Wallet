package com.dragoninc.mywallet.database;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dragoninc.mywallet.objects.Category;
import com.dragoninc.mywallet.objects.Item;
import com.dragoninc.mywallet.utils.Utils;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyWallet.db";

    //Item Table
    public static final String ITEMS_TABLE_NAME = "items";
    public static final String ITEMS_COLUMN_ID = "id";
    public static final String ITEMS_COLUMN_TYPE = "type";
    public static final String ITEMS_COLUMN_VALUE = "value";
    public static final String ITEMS_COLUMN_NOTE = "note";
    public static final String ITEMS_COLUMN_DATE = "date";
    public static final String ITEMS_COLUMN_CATELORY = "category_id";

    //Category Table
    public static final String CATEGORY_TABLE_NAME = "category";
    public static final String CATEGORY_COLUMN_ID = "id";
    public static final String CATEGORY_COLUMN_NAME = "name";
    public static final String CATEGORY_COLUMN_ICON = "icon_name";
    private static final String TAG = DBHelper.class.getSimpleName();

    private HashMap hp;

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table " + CATEGORY_TABLE_NAME +
                        "(id integer primary key, " +
                        "name text," +
                        "icon_name integer)"
        );
        db.execSQL(
                "create table " + ITEMS_TABLE_NAME +
                        "(id integer primary key, " +
                        "type integer," +
                        "value integer," +
                        "note text, " +
                        "date datetime, " +
                        "category_id integer,"+
                        "FOREIGN KEY(category_id) REFERENCES "+ CATEGORY_TABLE_NAME +"(id))"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS"  + ITEMS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS"  + CATEGORY_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertItem  (Integer type, float value, String note, String date, Integer CATEGORY_id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ITEMS_COLUMN_TYPE, type);
        contentValues.put(ITEMS_COLUMN_VALUE, value);
        contentValues.put(ITEMS_COLUMN_NOTE, note);
        contentValues.put(ITEMS_COLUMN_DATE, date);
        contentValues.put(ITEMS_COLUMN_CATELORY, CATEGORY_id);
        db.insert(ITEMS_TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean insertItem  (Item item)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ITEMS_COLUMN_TYPE, item.getItemType().getValue());
        contentValues.put(ITEMS_COLUMN_VALUE, item.getItemValue());
        contentValues.put(ITEMS_COLUMN_NOTE, item.getItemName());
        contentValues.put(ITEMS_COLUMN_DATE, item.getItemDate());
        contentValues.put(ITEMS_COLUMN_CATELORY, item.getCatalogID());
        db.insert(ITEMS_TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean insertCategory (String name, String icon_name)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CATEGORY_COLUMN_NAME, name);
        contentValues.put(CATEGORY_COLUMN_ICON, icon_name);
        db.insert(CATEGORY_TABLE_NAME, null, contentValues);
        return true;
    }

    public Cursor getData(String tablename, Integer id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + tablename + " where id="+id+"", null );
        return res;
    }

    public int numberOfItemTableRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, ITEMS_TABLE_NAME);
        return numRows;
    }

    public int numberOfCategoryTableRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, CATEGORY_TABLE_NAME);
        return numRows;
    }

    public boolean updateItem (Item item)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ITEMS_COLUMN_TYPE, item.getItemType().getValue());
        contentValues.put(ITEMS_COLUMN_VALUE, item.getItemValue());
        contentValues.put(ITEMS_COLUMN_NOTE, item.getItemName());
        contentValues.put(ITEMS_COLUMN_DATE, item.getItemDate());
        contentValues.put(ITEMS_COLUMN_CATELORY, item.getCatalogID());
        db.update(ITEMS_TABLE_NAME, contentValues, "id = ? ", new String[] { Integer.toString(item.getItemID()) } );
        return true;
    }

    public boolean updateCategory (Integer id, String name)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CATEGORY_COLUMN_NAME, name);
        db.update(CATEGORY_TABLE_NAME, contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteItems (String tableName, Integer id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(tableName,
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public ArrayList<Category> getAllCategoryRows()
    {
        ArrayList<Category> array_list = new ArrayList<>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + CATEGORY_TABLE_NAME +"", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            Category category = new Category();
            category.setId(Integer.parseInt(res.getString(res.getColumnIndex(CATEGORY_COLUMN_ID))));
            category.setName(res.getString(res.getColumnIndex(CATEGORY_COLUMN_NAME)));
            category.setIconName(res.getString(res.getColumnIndex(CATEGORY_COLUMN_ICON)));
            array_list.add(category);
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<Item> getAllItemRows()
    {
        ArrayList<Item> array_list = new ArrayList<>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + ITEMS_TABLE_NAME +"", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            Item item = new Item();
            item.setItemID(Integer.parseInt(res.getString(res.getColumnIndex(ITEMS_COLUMN_ID))));
            item.setItemValue(Integer.parseInt(res.getString(res.getColumnIndex(ITEMS_COLUMN_VALUE))));
            int type = Integer.parseInt(res.getString(res.getColumnIndex(ITEMS_COLUMN_TYPE)));
            if (type == Item.ITEM_TYPE.INCOME.getValue())
                item.setItemType(Item.ITEM_TYPE.INCOME);
            else
                item.setItemType(Item.ITEM_TYPE.EXPENSE);
            item.setItemName(res.getString(res.getColumnIndex(ITEMS_COLUMN_NOTE)));
            item.setItemDate(res.getString(res.getColumnIndex(ITEMS_COLUMN_DATE)));
            item.setCatalogID(Integer.parseInt(res.getString(res.getColumnIndex(ITEMS_COLUMN_CATELORY))));
            array_list.add(item);
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<Item> getAllItemInDate(String date)
    {
        ArrayList<Item> array_list = new ArrayList<>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select * from " + ITEMS_TABLE_NAME +" where date='"+date+"'";
        Cursor res =  db.rawQuery(query, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            Item item = new Item();
            item.setItemID(Integer.parseInt(res.getString(res.getColumnIndex(ITEMS_COLUMN_ID))));
            item.setItemValue(Integer.parseInt(res.getString(res.getColumnIndex(ITEMS_COLUMN_VALUE))));
            int type = Integer.parseInt(res.getString(res.getColumnIndex(ITEMS_COLUMN_TYPE)));
            if (type == Item.ITEM_TYPE.INCOME.getValue())
                item.setItemType(Item.ITEM_TYPE.INCOME);
            else
                item.setItemType(Item.ITEM_TYPE.EXPENSE);
            item.setItemName(res.getString(res.getColumnIndex(ITEMS_COLUMN_NOTE)));
            item.setItemDate(res.getString(res.getColumnIndex(ITEMS_COLUMN_DATE)));
            item.setCatalogID(Integer.parseInt(res.getString(res.getColumnIndex(ITEMS_COLUMN_CATELORY))));
            array_list.add(item);
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<Item> getAllItemInMonth(String month) {

        ArrayList<Item> array_list = new ArrayList<>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();

        String start = Utils.getCurrentYear() + "-" + month + "-" + "01";
        String end = Utils.getCurrentYear() + "-" + month + "-" + Utils.getDaysOfMonth(Integer.parseInt(month)-1);
        String query = "select * from " + ITEMS_TABLE_NAME + " where " + "date between date('"+ start + "') and date('" + end + "')";
        Cursor res =  db.rawQuery(query, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            Item item = new Item();
            item.setItemID(Integer.parseInt(res.getString(res.getColumnIndex(ITEMS_COLUMN_ID))));
            item.setItemValue(Integer.parseInt(res.getString(res.getColumnIndex(ITEMS_COLUMN_VALUE))));
            int type = Integer.parseInt(res.getString(res.getColumnIndex(ITEMS_COLUMN_TYPE)));
            if (type == Item.ITEM_TYPE.INCOME.getValue())
                item.setItemType(Item.ITEM_TYPE.INCOME);
            else
                item.setItemType(Item.ITEM_TYPE.EXPENSE);
            item.setItemName(res.getString(res.getColumnIndex(ITEMS_COLUMN_NOTE)));
            item.setItemDate(res.getString(res.getColumnIndex(ITEMS_COLUMN_DATE)));
            item.setCatalogID(Integer.parseInt(res.getString(res.getColumnIndex(ITEMS_COLUMN_CATELORY))));
            array_list.add(item);
            res.moveToNext();
        }
        return array_list;
    }
}