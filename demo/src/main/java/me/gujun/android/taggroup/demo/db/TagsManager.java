package me.gujun.android.taggroup.demo.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import me.gujun.android.taggroup.demo.R;

/**
 * Manage the tags from SQLite database.
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

    public static TagsManager getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new TagsManager(context);
        }
        return INSTANCE;
    }

    public String[] getTags() {
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
        return tagList.toArray(new String[]{});
    }

    public void updateTags(CharSequence... tags) {
        clearTags();
        for (CharSequence tag : tags) {
            addTag(tag);
        }
    }

    public void addTag(CharSequence tag) {
        ContentValues values = new ContentValues();
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        values.put(TagsTable.TAG, tag.toString());
        db.insert(TagsTable.TABLE_NAME, null, values);
        db.close();
    }

    public void clearTags() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.delete(TagsTable.TABLE_NAME, null, null);
        db.close();
    }
    public String[] getAutoCompleteData(Context context,String _query) {
        //Return countries
        String[] data = context.getResources().getStringArray(R.array.countries_array);
        String query = _query.toLowerCase();
        ArrayList<String> filtredData = new ArrayList<>();

        for (String d : data) {
            if(d.toLowerCase().startsWith(query))
                filtredData.add(d);
        }

        return  filtredData.toArray(new String[filtredData.size()]);
    }
}