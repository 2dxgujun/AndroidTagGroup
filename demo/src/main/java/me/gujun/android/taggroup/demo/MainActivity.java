package me.gujun.android.taggroup.demo;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.gujun.android.taggroup.TagGroup;
import me.gujun.android.taggroup.demo.db.TagsManager;


public class MainActivity extends ActionBarActivity {
    private TextView mPromptText;

    private TagGroup mDefaultTagGroup;
    private TagGroup mSmallTagGroup;
    private TagGroup mLargeTagGroup;
    private TagGroup mBeautyTagGroup;
    private TagGroup mBeautyInverseTagGroup;

    private TagsManager mTagsManager;

    private TagGroup.OnTagClickListener mTagClickListener = new TagGroup.OnTagClickListener() {
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
        List<TagData> tags = new ArrayList<>();
        tags.add(new TagData(0L, "hello"));
        tags.add(new TagData(0L, "how"));
        tags.add(new TagData(0L, "are"));
        tags.add(new TagData(0L, "you"));

        mPromptText = (TextView) findViewById(R.id.tv_prompt);
        mPromptText.setVisibility((tags == null || tags.size() == 0) ? View.VISIBLE : View.GONE);
        mPromptText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchTagEditorActivity();
            }
        });

        mDefaultTagGroup = (TagGroup) findViewById(R.id.tag_group);
        mSmallTagGroup = (TagGroup) findViewById(R.id.tag_group_small);
        mLargeTagGroup = (TagGroup) findViewById(R.id.tag_group_large);
        mBeautyTagGroup = (TagGroup) findViewById(R.id.tag_group_beauty);
        mBeautyInverseTagGroup = (TagGroup) findViewById(R.id.tag_group_beauty_inverse);
//        font/Lobster-Regular.ttf
        mLargeTagGroup.setCustomTypeface(Typeface.createFromAsset(getAssets(), "font/Lobster-Regular.ttf"));
        mBeautyInverseTagGroup.showDeleteBtn(R.drawable.letter_x);
        if (tags != null && tags.size() > 0) {
            mDefaultTagGroup.setTags(tags);
            mSmallTagGroup.setTags(tags);
            mLargeTagGroup.setTags(tags);
            mBeautyTagGroup.setTags(tags);
            mBeautyInverseTagGroup.setTags(tags);
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

        List<TagData> tags = new ArrayList<>();
        tags.add(new TagData(0L, "hello"));
        tags.add(new TagData(0L, "how"));
        tags.add(new TagData(0L, "are"));
        tags.add(new TagData(0L, "you"));
        mPromptText.setVisibility((tags == null || tags.size() == 0) ? View.VISIBLE : View.GONE);
        mDefaultTagGroup.setTags(tags);
        mSmallTagGroup.setTags(tags);
        mLargeTagGroup.setTags(tags);
        mBeautyTagGroup.setTags(tags);
        mBeautyInverseTagGroup.setTags(tags);
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