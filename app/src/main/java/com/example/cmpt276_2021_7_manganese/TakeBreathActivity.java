package com.example.cmpt276_2021_7_manganese;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cmpt276_2021_7_manganese.R;
import com.example.cmpt276_2021_7_manganese.model.Child;
import com.example.cmpt276_2021_7_manganese.model.ChildManager;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;
/**
 * This class and StartBreathActivity class is used to take breath, first user should choose the times they
 * want to take breath and then after they inhale for at least 3 seconds, they can turn to exhale, the animation
 * changes with the press of button. Hope you can enjoy you spare time.
 * @author Lingjie Li(Larry) Yangyang Liu
 */
public class TakeBreathActivity extends AppCompatActivity {
    private Spinner mSpCount;
    private Button mBtStart;
    private ArrayList<String> countList = new ArrayList<>();
    private ArrayAdapter<String> mSpCoountadapter;
    private int countIndex;
    private ImageView mIvBack;
    private TextView mTvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this).init();
        setContentView(R.layout.activity_take_breath);
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        mBtStart.setOnClickListener(view -> {
            Intent intent = new Intent(TakeBreathActivity.this, StartTakeBreathActivity.class);
            intent.putExtra("count", countList.get(countIndex));
            startActivity(intent);
        });
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initData() {
        for (int i = 1; i < 11; i++) {
            countList.add(String.valueOf(i));
        }
        mSpCoountadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, countList);
        mSpCoountadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpCount.setAdapter(mSpCoountadapter);
        mSpCount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                countIndex = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public static Intent makeLaunchIntent(Context c) {
        return new Intent(c, TakeBreathActivity.class);
    }

    private void initView() {
        mSpCount = (Spinner) findViewById(R.id.sp_count);
        mBtStart = (Button) findViewById(R.id.bt_start);
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
    }
}