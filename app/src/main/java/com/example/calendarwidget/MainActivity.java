package com.example.calendarwidget;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private CustomPopuWindow mTimePop,mSinglePop;

    private CustomPopuWindow mPopuWindow,mPopWindows;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.scrollIndicatorDown).setOnClickListener(view -> start());
        findViewById(R.id.scrollIndicatorDownSingle).setOnClickListener(view -> startSingle());
    }

    private void start(){
        if(mTimePop == null){
            mTimePop = PopUtils.initTimePickPop(this, "请选择起止时间", new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()),
                    null, true, null, (startTime, endTime) -> {
                         }, true, 12, false, 0, 11);
        }
        mTimePop.showAtLocationWithNoMaxAvailableHeight(findViewById(R.id.scrollIndicatorDown));
    }

    private void startSingle(){
        if(mSinglePop == null){
            mSinglePop = PopUtils.initTimePickPopSingle(this, "请选择起止时间", new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()),
                    null, true, null, (startTime, endTime) -> {
                    }, true, 12, false, 0, 11);
        }
        mSinglePop.showAtLocationWithNoMaxAvailableHeight(findViewById(R.id.scrollIndicatorDownSingle));
    }

}
