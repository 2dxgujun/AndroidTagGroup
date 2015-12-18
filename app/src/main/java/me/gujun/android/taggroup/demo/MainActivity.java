package me.gujun.android.taggroup.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import me.gujun.android.taggroup.AndroidTagGroup;
import me.gujun.android.taggroup.demo.db.TagsManager;


public class MainActivity extends ActionBarActivity {
    private TextView mPromptText;

    private AndroidTagGroup mDefaultAndroidTagGroup;
    private AndroidTagGroup mSmallAndroidTagGroup;
    private AndroidTagGroup mLargeAndroidTagGroup;
    private AndroidTagGroup mBeautyAndroidTagGroup;
    private AndroidTagGroup mBeautyInverseAndroidTagGroup;

    private TagsManager mTagsManager;

    private AndroidTagGroup.OnTagClickListener mTagClickListener = new AndroidTagGroup.OnTagClickListener() {
        @Override
        public void onTagClick(String tag) {
            Toast.makeText(MainActivity.this, tag, Toast.LENGTH_SHORT).show();
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

        mDefaultAndroidTagGroup = (AndroidTagGroup) findViewById(R.id.androidTagGroup);
        mSmallAndroidTagGroup = (AndroidTagGroup) findViewById(R.id.tag_group_small);
        mLargeAndroidTagGroup = (AndroidTagGroup) findViewById(R.id.tag_group_large);
        mBeautyAndroidTagGroup = (AndroidTagGroup) findViewById(R.id.tag_group_beauty);
        mBeautyInverseAndroidTagGroup = (AndroidTagGroup) findViewById(R.id.tag_group_beauty_inverse);
        if (tags != null && tags.length > 0) {
            mDefaultAndroidTagGroup.setTags(tags);
            mSmallAndroidTagGroup.setTags(tags);
            mLargeAndroidTagGroup.setTags(tags);
            mBeautyAndroidTagGroup.setTags(tags);
            mBeautyInverseAndroidTagGroup.setTags(tags);
        }

        MyTagGroupOnClickListener tgClickListener = new MyTagGroupOnClickListener();

        mDefaultAndroidTagGroup.setOnClickListener(tgClickListener);
        mSmallAndroidTagGroup.setOnClickListener(tgClickListener);
        mLargeAndroidTagGroup.setOnClickListener(tgClickListener);
        mBeautyAndroidTagGroup.setOnClickListener(tgClickListener);
        mBeautyInverseAndroidTagGroup.setOnClickListener(tgClickListener);

        mDefaultAndroidTagGroup.setOnTagClickListener(mTagClickListener);
        mSmallAndroidTagGroup.setOnTagClickListener(mTagClickListener);
        mLargeAndroidTagGroup.setOnTagClickListener(mTagClickListener);
        mBeautyAndroidTagGroup.setOnTagClickListener(mTagClickListener);
        mBeautyInverseAndroidTagGroup.setOnTagClickListener(mTagClickListener);
    }

    protected void launchTagEditorActivity() {
        Intent intent = new Intent(MainActivity.this, TagEditorActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String[] tags = mTagsManager.getTags();
        mPromptText.setVisibility((tags == null || tags.length == 0) ? View.VISIBLE : View.GONE);
        mDefaultAndroidTagGroup.setTags(tags);
        mSmallAndroidTagGroup.setTags(tags);
        mLargeAndroidTagGroup.setTags(tags);
        mBeautyAndroidTagGroup.setTags(tags);
        mBeautyInverseAndroidTagGroup.setTags(tags);
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

    class MyTagGroupOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            launchTagEditorActivity();
        }
    }
}