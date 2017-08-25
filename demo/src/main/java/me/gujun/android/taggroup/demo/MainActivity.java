package me.gujun.android.taggroup.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;

import me.gujun.android.taggroup.TagGroup;


public class MainActivity extends AppCompatActivity {
    private TagGroup mTagGroup;
    //TODO REMOVE suggestion string when backend is ready
    private String[] autoFakeServerCompleteTag = { "Tag1", "Tag2", "Tag3", "Tag4", "Tag5"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTagGroup = (TagGroup) findViewById(R.id.tag_group);

        //get Fake Autocomplete server string array

        mTagGroup.setOnTagCharEntryListener(new TagGroup.OnTagCharEntryListener() {
            @Override
            public void onCharEntry(String text) {

                mTagGroup.getTagView().setAdapter(new ArrayAdapter<>(MainActivity.this, R.layout.taglist, autoFakeServerCompleteTag));
            }
        });
    }
}