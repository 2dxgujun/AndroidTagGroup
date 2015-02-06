package me.gujun.android.taggroup.demo.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * <Please describe the usage of this class>
 *
 * @author Jun Gu (http://2dxgujun.com)
 * @version 1.0
 * @since 2015-2-5 20:22:16
 */
public class TagsManager {
    private static TagsManager INSTANCE;

    private DatabaseHelper mDbHelper;

    private TagsManager(Context context) {
        mDbHelper = new DatabaseHelper(context);
    }

    public void insertTag(String tag) {
        ContentValues values = new ContentValues();
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        values.put(TagsTable.TAG, tag);
        db.insert(TagsTable.TABLE_NAME, null, values);
        db.close();
    }

    public void insertTags(String[] tags) {
        for (String tag : tags) {
            insertTag(tag);
        }
    }

    public List<String> getAllTags() {
        List<String> tagList = new ArrayList<>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor c = db.query(TagsTable.TABLE_NAME,
                new String[]{TagsTable.TAG}, null, null, null, null, null);
        while (c.moveToNext()) {
            String tag = c.getString(c.getColumnIndex(TagsTable.TAG));
            tagList.add(tag);
        }
        c.close();
        db.close();
        return tagList;
    }

    public void deleteTag(String tag) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.delete(TagsTable.TABLE_NAME, TagsTable.TAG + "=?", new String[]{tag});
        db.close();
    }

    public static TagsManager getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new TagsManager(context);
        }
        return INSTANCE;
    }
}