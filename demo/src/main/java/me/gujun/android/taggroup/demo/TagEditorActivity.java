package me.gujun.android.taggroup.demo;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import me.gujun.android.taggroup.TagGroup;
import me.gujun.android.taggroup.demo.db.TagsManager;


public class TagEditorActivity extends ActionBarActivity {
    private TagGroup mTagGroup;
    private TagsManager mTagsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_editor);

        mTagsManager = TagsManager.getInstance(getApplicationContext());
        String[] tags = mTagsManager.getTags();

        mTagGroup = (TagGroup) findViewById(R.id.tag_group);
        mTagGroup.setTags(tags);
        mTagGroup.setOnTagTextEntryListener(new TagGroup.OnTagTextEntryListener() {
            @Override
            public void onTextEntry(AutoCompleteTextView tagView, String text) {
                if (text.equals("A")) {
                    tagView.setAdapter(new ArrayAdapter<>(TagEditorActivity.this, android.R.layout.simple_list_item_1, new String[]{"Au", "Bu", "Cy"}));
                } else if (text.equals("Au")) {
                    tagView.setAdapter(new ArrayAdapter<>(TagEditorActivity.this, android.R.layout.simple_list_item_1, new String[]{"Aus", "Bul", "Cyp"}));
                } else if (text.equals("Aus")) {
                    tagView.setAdapter(new ArrayAdapter<>(TagEditorActivity.this, android.R.layout.simple_list_item_1, new String[]{"Aust", "Bulg", "Cypr"}));
                } else if (text.equals("Aust")) {
                    tagView.setAdapter(new ArrayAdapter<>(TagEditorActivity.this, android.R.layout.simple_list_item_1, new String[]{"Australia", "Bulgaria", "Cyprus"}));
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