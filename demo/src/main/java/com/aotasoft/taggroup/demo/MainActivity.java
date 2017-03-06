package com.aotasoft.taggroup.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.aotasoft.taggroup.TagGroup;
import com.aotasoft.taggroup.demo.db.TagsManager;


public class MainActivity extends ActionBarActivity {
    private TextView mPromptText;

    private TagGroup mDefaultTagGroup;
    private TagGroup mSmallTagGroup;
    private TagGroup mLargeTagGroup;
    private TagGroup mLargeMiddleTagGroup;
    private TagGroup mLargeRightTagGroup;
    private TagGroup mBeautyTagGroup;
    private TagGroup mBeautyInverseTagGroup;

    private TagsManager mTagsManager;

    private TagGroup.OnTagClickListener mTagClickListener = new TagGroup.OnTagClickListener() {
        @Override
        public void onTagClick(TagGroup tagGroup, String tag, int position) {
            Toast.makeText(MainActivity.this, tag, Toast.LENGTH_SHORT).show();
            tagGroup.setSelectedTag(position, !tagGroup.isSelectedTag(position));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTagsManager = TagsManager.getInstance(getApplicationContext());
        String[] tags = mTagsManager.getTags();

        mPromptText = (TextView) findViewById(R.id.tv_prompt);
        mPromptText.setVisibility((tags == null || tags.length == 0) ? View.VISIBLE : View.GONE);
        mPromptText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchTagEditorActivity();
            }
        });

        mDefaultTagGroup = (TagGroup) findViewById(R.id.tag_group);
        mSmallTagGroup = (TagGroup) findViewById(R.id.tag_group_small);
        mLargeTagGroup = (TagGroup) findViewById(R.id.tag_group_large);
        mLargeMiddleTagGroup = (TagGroup) findViewById(R.id.tag_group_large_middle);
        mLargeRightTagGroup = (TagGroup) findViewById(R.id.tag_group_large_right);
        mBeautyTagGroup = (TagGroup) findViewById(R.id.tag_group_beauty);
        mBeautyInverseTagGroup = (TagGroup) findViewById(R.id.tag_group_beauty_inverse);
        if (tags != null && tags.length > 0) {
            mDefaultTagGroup.setTags(tags);
            mSmallTagGroup.setTags(tags);
            mLargeTagGroup.setTags(tags);
            mBeautyTagGroup.setTags(tags);
            mBeautyInverseTagGroup.setTags(tags);

            mLargeMiddleTagGroup.setTags(tags);
            mLargeMiddleTagGroup.setGravity(TagGroup.TagGravity.MIDDLE);
            mLargeRightTagGroup.setTags(tags);
            mLargeRightTagGroup.setGravity(TagGroup.TagGravity.RIGHT);
        }

        MyTagGroupOnClickListener tgClickListener = new MyTagGroupOnClickListener();

        mDefaultTagGroup.setOnClickListener(tgClickListener);
        mSmallTagGroup.setOnClickListener(tgClickListener);
        mLargeTagGroup.setOnClickListener(tgClickListener);
        mBeautyTagGroup.setOnClickListener(tgClickListener);
        mBeautyInverseTagGroup.setOnClickListener(tgClickListener);

        mDefaultTagGroup.setOnTagClickListener(mTagClickListener);
        mSmallTagGroup.setOnTagClickListener(mTagClickListener);
        mLargeTagGroup.setOnTagClickListener(mTagClickListener);
        mBeautyTagGroup.setOnTagClickListener(mTagClickListener);
        mBeautyInverseTagGroup.setOnTagClickListener(mTagClickListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String[] tags = mTagsManager.getTags();
        mPromptText.setVisibility((tags == null || tags.length == 0) ? View.VISIBLE : View.GONE);
        mDefaultTagGroup.setTags(tags);
        mSmallTagGroup.setTags(tags);
        mLargeTagGroup.setTags(tags);
        mBeautyTagGroup.setTags(tags);
        mBeautyInverseTagGroup.setTags(tags);
        mLargeMiddleTagGroup.setTags(tags);
        mLargeMiddleTagGroup.setGravity(TagGroup.TagGravity.MIDDLE);
        mLargeRightTagGroup.setTags(tags);
        mLargeRightTagGroup.setGravity(TagGroup.TagGravity.RIGHT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_edit) {
            launchTagEditorActivity();
            return true;
        }
        return false;
    }

    protected void launchTagEditorActivity() {
        Intent intent = new Intent(MainActivity.this, TagEditorActivity.class);
        startActivity(intent);
    }

    class MyTagGroupOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            launchTagEditorActivity();
        }
    }
}