package me.gujun.android.taggroup.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import me.gujun.android.taggroup.TagGroup;
import me.gujun.android.taggroup.demo.db.TagsManager;


public class MainActivity extends ActionBarActivity {
    private TextView mPromptText;

    private TagGroup mDefaultTagGroup;
    private TagGroup mSmallTagGroup;
    private TagGroup mLargeTagGroup;

    private List<String> mTagList;

    private int default_green;
    private int beauty_red;
    private int holo_dark;
    private int light_blue;
    private int indigo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TagsManager tagsManager = TagsManager.getInstance(getApplicationContext());
        mTagList = tagsManager.getAllTags();

        mPromptText = (TextView) findViewById(R.id.tv_prompt);
        mPromptText.setVisibility(mTagList.isEmpty() ? View.VISIBLE : View.GONE);
        mPromptText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SecondaryActivity.class);
                startActivity(intent);
            }
        });

        mDefaultTagGroup = (TagGroup) findViewById(R.id.tag_group_default);
        mSmallTagGroup = (TagGroup) findViewById(R.id.tag_group_small);
        mLargeTagGroup = (TagGroup) findViewById(R.id.tag_group_large);
        if (mTagList != null && !mTagList.isEmpty()) {
            mDefaultTagGroup.setTags(mTagList);
            mSmallTagGroup.setTags(mTagList);
            mLargeTagGroup.setTags(mTagList);
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

    class MyTagGroupOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, SecondaryActivity.class);
            intent.putStringArrayListExtra("tagList", (java.util.ArrayList<String>) mTagList);
            intent.putExtra("brightColor", mDefaultTagGroup.getBrightColor());
            startActivity(intent);
        }
    }
}