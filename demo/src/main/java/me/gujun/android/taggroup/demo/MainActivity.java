package me.gujun.android.taggroup.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import me.gujun.android.taggroup.TagGroup;


public class MainActivity extends ActionBarActivity {
    private TagGroup mTagGroup;
    private String[] mTags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTags = new String[]{"朋友", "同学"};

        mTagGroup = (TagGroup) findViewById(R.id.tag_group);
        mTagGroup.setTags(mTags);
        mTagGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SecondaryActivity.class);
                intent.putExtra("tags", mTags);
                startActivity(intent);
            }
        });
    }
}