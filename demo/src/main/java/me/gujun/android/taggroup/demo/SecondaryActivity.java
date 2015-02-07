package me.gujun.android.taggroup.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import me.gujun.android.taggroup.TagGroup;


public class SecondaryActivity extends ActionBarActivity {
    private TagGroup mTagGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secondary);

        CharSequence[] tags = getIntent().getCharSequenceArrayExtra("tags");
        int color = getIntent().getIntExtra("color", getResources().getColor(R.color.default_green));

        mTagGroup = (TagGroup) findViewById(R.id.tag_group);
        mTagGroup.setTags(tags);
        mTagGroup.setBrightColor(color);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent();
            intent.putExtra("tagsResult", mTagGroup.getTags());
            setResult(110, intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("tagsResult", mTagGroup.getTags());
        setResult(110, intent);
        super.onBackPressed();
    }
}