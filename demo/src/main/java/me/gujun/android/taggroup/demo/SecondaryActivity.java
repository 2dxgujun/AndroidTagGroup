package me.gujun.android.taggroup.demo;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import java.util.List;

import me.gujun.android.taggroup.TagGroup;
import me.gujun.android.taggroup.demo.db.TagsManager;


public class SecondaryActivity extends ActionBarActivity {
    private TagGroup mTagGroup;
    private TagsManager mTagsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secondary);
        mTagsManager = TagsManager.getInstance(this);

        mTagGroup = (TagGroup) findViewById(R.id.tag_group);
        mTagGroup.setOnTagChangeListener(new TagGroup.OnTagChangeListener() {
            @Override
            public void onAppend(String tag) {
                mTagsManager.insertTag(tag);
            }

            @Override
            public void onDelete(String tag) {
                mTagsManager.deleteTag(tag);
            }
        });

        List<String> tagList = getIntent().getStringArrayListExtra("tagList");
        if (tagList != null) {
            mTagGroup.setTags(tagList);
        }
    }
}