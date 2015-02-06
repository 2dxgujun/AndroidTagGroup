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
    private TagGroup mTagGroup;
    private TextView mTagPromptText;
    private List<String> mTagList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TagsManager tagsManager = TagsManager.getInstance(getApplicationContext());
        mTagList = tagsManager.getAllTags();

        mTagPromptText = (TextView) findViewById(R.id.tv_prompt_tag);
        mTagPromptText.setVisibility(mTagList.isEmpty() ? View.VISIBLE : View.GONE);
        mTagPromptText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SecondaryActivity.class);
                startActivity(intent);
            }
        });

        mTagGroup = (TagGroup) findViewById(R.id.tag_group);
        if (mTagList != null && !mTagList.isEmpty()) {
            mTagGroup.setTags(mTagList);
        }
        mTagGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SecondaryActivity.class);
                intent.putStringArrayListExtra("tagList", (java.util.ArrayList<String>) mTagList);
                startActivity(intent);
            }
        });
    }
}