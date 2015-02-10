package me.gujun.android.taggroup.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;

import me.gujun.android.taggroup.TagGroup;
import me.gujun.android.taggroup.demo.db.TagsManager;


public class MainActivity extends ActionBarActivity {
    private TextView mPromptText;

    private TagGroup mDefaultTagGroup;
    private TagGroup mSmallTagGroup;
    private TagGroup mLargeTagGroup;

    private int default_green;
    private int beauty_red;
    private int holo_dark;
    private int light_blue;
    private int indigo;

    private TagsManager mTagsManager;

    private String[] mTags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTagsManager = TagsManager.getInstance(getApplicationContext());
        mTags = mTagsManager.getTags();

        mPromptText = (TextView) findViewById(R.id.tv_prompt);
        mPromptText.setVisibility((mTags == null || mTags.length == 0) ? View.VISIBLE : View.GONE);
        mPromptText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchSecondaryActivity();
            }
        });

        mDefaultTagGroup = (TagGroup) findViewById(R.id.tag_group);
        mSmallTagGroup = (TagGroup) findViewById(R.id.tag_group_small);
        mLargeTagGroup = (TagGroup) findViewById(R.id.tag_group_large);
        if (mTags != null && mTags.length > 0) {
            mDefaultTagGroup.setTags(mTags);
            mSmallTagGroup.setTags(mTags);
            mLargeTagGroup.setTags(mTags);
        }

        MyTagGroupOnClickListener tgClickListener = new MyTagGroupOnClickListener();

        mDefaultTagGroup.setOnClickListener(tgClickListener);
        mSmallTagGroup.setOnClickListener(tgClickListener);
        mLargeTagGroup.setOnClickListener(tgClickListener);

        default_green = getResources().getColor(R.color.default_green);
        beauty_red = getResources().getColor(R.color.beauty_red);
        holo_dark = getResources().getColor(R.color.holo_dark);
        light_blue = getResources().getColor(R.color.light_blue);
        indigo = getResources().getColor(R.color.indigo);
    }

    public void onPickColor(View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.cp_default: {
                mDefaultTagGroup.setBrightColor(default_green);
                mSmallTagGroup.setBrightColor(default_green);
                mLargeTagGroup.setBrightColor(default_green);
                break;
            }
            case R.id.cp_beauty_red: {
                mDefaultTagGroup.setBrightColor(beauty_red);
                mSmallTagGroup.setBrightColor(beauty_red);
                mLargeTagGroup.setBrightColor(beauty_red);
                break;
            }
            case R.id.cp_holo_dark: {
                mDefaultTagGroup.setBrightColor(holo_dark);
                mSmallTagGroup.setBrightColor(holo_dark);
                mLargeTagGroup.setBrightColor(holo_dark);
                break;
            }
            case R.id.cp_light_blue: {
                mDefaultTagGroup.setBrightColor(light_blue);
                mSmallTagGroup.setBrightColor(light_blue);
                mLargeTagGroup.setBrightColor(light_blue);
                break;
            }
            case R.id.cp_indigo: {
                mDefaultTagGroup.setBrightColor(indigo);
                mSmallTagGroup.setBrightColor(indigo);
                mLargeTagGroup.setBrightColor(indigo);
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 110 && resultCode == 110) {
            mTags = data.getStringArrayExtra("tagsResult");
            mTagsManager.updateTags(mTags);
            mPromptText.setVisibility((mTags == null || mTags.length == 0) ? View.VISIBLE : View.GONE);
            mDefaultTagGroup.setTags(mTags);
            mSmallTagGroup.setTags(mTags);
            mLargeTagGroup.setTags(mTags);
        }
    }

    protected void launchSecondaryActivity() {
        Intent intent = new Intent(MainActivity.this, SecondaryActivity.class);
        intent.putExtra("tags", mTags);
        intent.putExtra("color", mDefaultTagGroup.getBrightColor());
        startActivityForResult(intent, 110);
    }

    class MyTagGroupOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            launchSecondaryActivity();
        }
    }
}