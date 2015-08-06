package me.gujun.android.taggroup.demo;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import java.util.Random;

import me.gujun.android.taggroup.TagGroup;
import me.gujun.android.taggroup.demo.db.TagsManager;


public class TagEditorActivity extends ActionBarActivity {
    private TagGroup mTagGroup;
    private TagsManager mTagsManager;

    private String[] a = new String[] {"bla", "tra", "gha"};
    private String[] b = new String[] {"blas", "tras", "ghas"};
    private String[] c = new String[] {"blas2", "tras3", "ghas4"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_editor);

        mTagsManager = TagsManager.getInstance(getApplicationContext());
        String[] tags = mTagsManager.getTags();

        mTagGroup = (TagGroup) findViewById(R.id.tag_group);
        mTagGroup.setTags(tags);
        mTagGroup.setOnTagCharEntryListener(new TagGroup.OnTagCharEntryListener() {
            @Override
            public void onCharEntry(String text) {
                Log.e("dsad", text);

                if (text.equals("b")) {
                    mTagGroup.getTagView().setAdapter(new ArrayAdapter<>(TagEditorActivity.this, android.R.layout.simple_list_item_1, new String[] {"bl", "bg", "bd"}));
                } else if (text.equals("bl")) {
                    mTagGroup.getTagView().setAdapter(new ArrayAdapter<>(TagEditorActivity.this, android.R.layout.simple_list_item_1, new String[] {"bla", "bga", "bda"}));
                } else if (text.equals("bla")) {
                    mTagGroup.getTagView().setAdapter(new ArrayAdapter<>(TagEditorActivity.this, android.R.layout.simple_list_item_1, new String[] {"blag", "bgag", "bdag"}));
                } else if (text.equals("blag")) {
                    mTagGroup.getTagView().setAdapter(new ArrayAdapter<>(TagEditorActivity.this, android.R.layout.simple_list_item_1, new String[] {"blaga", "bgagd", "bdagd"}));
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tag_editor_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mTagsManager.updateTags(mTagGroup.getTags());
            finish();
            return true;
        } else if (item.getItemId() == R.id.action_submit) {
            mTagGroup.submitTag();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        mTagsManager.updateTags(mTagGroup.getTags());
        super.onBackPressed();
    }
}