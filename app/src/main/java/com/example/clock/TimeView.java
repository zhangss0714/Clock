package com.example.clock;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;

public class TimeView extends LinearLayout {

    private TextView tvTime;

    public TimeView(Context context) {
        super(context);
    }

    public TimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

//  当我们的XML布局被加载完后，就会回调onFinshInfalte这个方法
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        tvTime = (TextView) findViewById(R.id.tvTime);
        timeHandler.sendEmptyMessage(0);
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        //当再次切换到这个Tab时,我们就再发送一次这个消息，否者就把所有的消息移除掉
        if (visibility == View.VISIBLE) {
            timeHandler.sendEmptyMessage(0);
        }else{
            timeHandler.removeMessages(0);
        }
    }

    private void refreshTime(){
        //获取当前的时间
        Calendar c = Calendar.getInstance();

        tvTime.setText(String.format("%02d:%02d:%02d", c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE),c.get(Calendar.SECOND)));
    }
    private Handler timeHandler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            refreshTime();
            //处于当前Tab的时候给自己发送信息，可以刷新
            if (getVisibility() == View.VISIBLE) {
                //每隔1s就循环发送消息，what参数用于区分不同的message
                timeHandler.sendEmptyMessageDelayed(0, 1000);
            }
        };
    };
}

