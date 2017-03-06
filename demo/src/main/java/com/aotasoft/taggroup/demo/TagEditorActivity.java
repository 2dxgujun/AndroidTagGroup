package com.aotasoft.taggroup.demo;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.aotasoft.taggroup.TagGroup;
import com.aotasoft.taggroup.demo.db.TagsManager;


public class TagEditorActivity extends ActionBarActivity {
    final String TAG = "TagEditorActivity";
    private TagGroup mTagGroup;
    private TagsManager mTagsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_editor);

        mTagsManager = TagsManager.getInstance(getApplicationContext());
        String[] tags = mTagsManager.getTags();

        mTagGroup = (TagGroup) findViewById(R.id.tag_group);
        mTagGroup.setGravity(TagGroup.TagGravity.LEFT);
        mTagGroup.setTags(tags);
        mTagGroup.setOnTagInputTextListener(new TagGroup.OnTagInputTextListener() {
            @Override
            public void beforeTextChanged(TagGroup.TagView tv, CharSequence s, int start, int count, int after) {
                Log.i(TAG, "beforeTextChanged="+s.toString());
            }

            @Override
            public void onTextChanged(TagGroup.TagView tv, CharSequence s, int start, int before, int count) {
                Log.i(TAG, "onTextChanged="+s.toString());
            }

            @Override
            public void afterTextChanged(TagGroup.TagView tv, Editable s) {
                Log.i(TAG, "afterTextChanged="+s.toString());
            }
        });

        mTagGroup.setOnSubmitActionListener(new TagGroup.OnSubmitActionListener() {
            @Override
            public boolean onSubmit(TagGroup.TagView tv) {
                mTagGroup.submitTag();
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tag_editor_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mTagsManager.updateTags(mTagGroup.getTags());
            finish();
            return true;
        } else if (item.getItemId() == R.id.action_submit) {
            mTagGroup.submitTag();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        mTagsManager.updateTags(mTagGroup.getTags());
        super.onBackPressed();
    }
}