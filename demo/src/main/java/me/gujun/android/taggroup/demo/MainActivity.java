package me.gujun.android.taggroup.demo;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Button;

import me.gujun.android.taggroup.TagGroup;


public class MainActivity extends ActionBarActivity {
    private TagGroup mTagGroup;
    private Button mNormalBtn;
    private Button mActiveBtn;
    private Button mCheckedBtn;
    private Button mInputBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTagGroup = (TagGroup) findViewById(R.id.tag_group);

        mNormalBtn = (Button) findViewById(R.id.btn_normal);
        mActiveBtn = (Button) findViewById(R.id.btn_active);
        mCheckedBtn = (Button) findViewById(R.id.btn_checked);
        mInputBtn = (Button) findViewById(R.id.btn_input);
    }
}