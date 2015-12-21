package me.gujun.android.taggroup.demo;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Filter;

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
        mTagGroup.setAutoCompleteAdapter(new TagAutoCompleteAdapter(this));
        mTagGroup.setTags(tags);
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

    private class TagAutoCompleteAdapter extends ArrayAdapter<String>  {
        public TagAutoCompleteAdapter(Context context) {
            super(context, R.layout.autocomplete_row);
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected Filter.FilterResults performFiltering(CharSequence constraint) {
                    Filter.FilterResults result = new Filter.FilterResults();
                    result.count = 0;
                    if(constraint!=null) {
                        String[] data = mTagsManager.getAutoCompleteData(TagEditorActivity.this, constraint.toString());
                        result.values = data;
                        result.count = data.length;
                    }
                    return result;
                }

                @Override
                protected void publishResults(CharSequence constraint, Filter.FilterResults results) {
                    clear();
                    if (results.count > 0) {
                        for (String s : (String[]) results.values) {
                            add(s);
                        }
                    }
                    notifyDataSetChanged();
                }
            };
        }

    }
}