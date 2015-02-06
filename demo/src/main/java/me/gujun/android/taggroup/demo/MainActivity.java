package me.gujun.android.taggroup.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import java.util.List;

import me.gujun.android.taggroup.TagGroup;
import me.gujun.android.taggroup.demo.db.TagsManager;


public class MainActivity extends ActionBarActivity {
    private TagGroup mTagGroup;
    private List<String> mTagList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TagsManager tagsManager = TagsManager.getInstance(getApplicationContext());
        mTagList = tagsManager.getAllTags();

        mTagGroup = (TagGroup) findViewById(R.id.tag_group);
        if (mTagList == null) {
            Intent intent = new Intent(MainActivity.this, SecondaryActivity.class);
            startActivity(intent);
        } else {
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