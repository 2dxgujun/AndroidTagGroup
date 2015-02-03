package me.gujun.android.taggroup.demo;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

import me.gujun.android.taggroup.TagView;


public class MainActivity extends ActionBarActivity {
    private TagView mTagView;
    private Button mNormalBtn;
    private Button mActiveBtn;
    private Button mCheckedBtn;
    private Button mInputBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTagView = (TagView) findViewById(R.id.tag_view);
        mNormalBtn = (Button) findViewById(R.id.btn_normal);
        mActiveBtn = (Button) findViewById(R.id.btn_active);
        mCheckedBtn = (Button) findViewById(R.id.btn_checked);
        mInputBtn = (Button) findViewById(R.id.btn_input);

        mNormalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTagView.setState(TagView.State.NORMAL);
            }
        });

        mActiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTagView.setState(TagView.State.ACTIVE);
            }
        });

        mCheckedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTagView.setState(TagView.State.CHECKED);
            }
        });

        mInputBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTagView.setState(TagView.State.INPUT);
            }
        });
    }
}