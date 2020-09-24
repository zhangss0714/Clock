package com.example.clock;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TabHost;

public class MainActivity extends AppCompatActivity {

    private TabHost tabHost;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabHost=(TabHost)findViewById(R.id.tabHost);
        tabHost.setup();

//      这里显示的组件的排列
        //在TabHost创建标签，然后设置：标题／图标／标签页布局
        tabHost.addTab(tabHost.newTabSpec("tabAlarm").setIndicator("闹钟").setContent(R.id.tabAlarm));
        tabHost.addTab(tabHost.newTabSpec("tabTime").setIndicator("时钟").setContent(R.id.tabTime));
        tabHost.addTab(tabHost.newTabSpec("tabTimer").setIndicator("计时器").setContent(R.id.tabTimer));
        tabHost.addTab(tabHost.newTabSpec("tabStopWatch").setIndicator("秒表").setContent(R.id.tabStopWatch));
    }


}