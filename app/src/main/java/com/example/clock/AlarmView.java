package com.example.clock;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TimePicker;

import java.util.Calendar;

public class AlarmView extends LinearLayout {

    private Button btnAddAlarm;
    private ListView lvAlarmList;
    private static final String KEY_ALARM_LIST = "alarmList";
    private ArrayAdapter<AlarmData> adapter;
    //  设置闹钟服务
    private AlarmManager alarmManager;

    public AlarmView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AlarmView(Context context) {
        super(context);
        init();
    }

    private void init(){
    //   获得AlarmManager实例对象
        alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        btnAddAlarm = (Button) findViewById(R.id.btnAddAlarm);
        lvAlarmList = (ListView) findViewById(R.id.lvAlarmList);

        adapter = new ArrayAdapter<AlarmView.AlarmData>(getContext(), android.R.layout.simple_list_item_1);
        lvAlarmList.setAdapter(adapter);
        readSavedAlarmList();

        btnAddAlarm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                addAlarm();
            }
        });
//      触发删除闹钟
        lvAlarmList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {
                new AlertDialog.Builder(getContext()).setTitle("操作选项").setItems(new CharSequence[]{"删除","编辑"}, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                       判断操作选项（删除,编辑）
                        switch (which) {
                            case 0:
                                deleteAlarm(position);
                                break;
                            case 1:
                                editAlarm(position);
                                break;
                            default:
                                break;
                        }

                    }
                }).setNegativeButton("取消", null).show();

            }
        });
    }

    private void editAlarm(int position) {
        AlarmData ad = adapter.getItem(position);
    }

    //删除闹钟
    private void deleteAlarm(int position){
        AlarmData ad = adapter.getItem(position);
        adapter.remove(ad);
        saveAlarmList();
    //删除闹钟服务
        alarmManager.cancel(PendingIntent.getBroadcast(getContext(), ad.getId(), new Intent(getContext(), AlarmReceiver.class), 0));
    }

    //添加闹钟
    private void addAlarm(){
        //TODO
//      设置TimePickerDialog的参数
        Calendar c = Calendar.getInstance();
    //  时间的选择框
        new TimePickerDialog(getContext(), AlertDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
    //                设置的闹钟时间
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

                Calendar currentTime = Calendar.getInstance();
    //                如果设置的时间比当前时间要早，就变为第二天设置的时间
                if (calendar.getTimeInMillis()<=currentTime.getTimeInMillis()) {
                    calendar.setTimeInMillis(calendar.getTimeInMillis()+24*60*60*1000);
                }

                AlarmData ad = new AlarmData(calendar.getTimeInMillis());
                adapter.add(ad);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP,
                            ad.getTime(),
                            PendingIntent.getBroadcast(getContext(), ad.getId(), new Intent(getContext(),
                                    AlarmReceiver.class), 0));
                }


                saveAlarmList();

            }
        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
    }
    //   保存闹钟
    private void saveAlarmList(){
//        调用SharedPreferences的edit()方法来获取一个编辑器对象SharedPreferences.Editor
        SharedPreferences.Editor editor = getContext().getSharedPreferences(AlarmView.class.getName(), Context.MODE_PRIVATE).edit();

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < adapter.getCount(); i++) {
        // 传进来一个位置   getItem(i)
            sb.append(adapter.getItem(i).getTime()).append(",");
        }
        Log.d("alarm_delete", sb.toString());
        //删除闹钟出错解决
        if (sb.length()>1) {
            //  把最后一个","减掉
            String content = sb.toString().substring(0, sb.length()-1);

            editor.putString(KEY_ALARM_LIST, content);

        }else{
            editor.putString(KEY_ALARM_LIST, null);
        }
//        commit是同步的提交到硬件磁盘
        editor.commit();
    }



// 读取存取的闹钟
    private void readSavedAlarmList(){
        SharedPreferences sp = getContext().getSharedPreferences(AlarmView.class.getName(), Context.MODE_PRIVATE);
//        如果preference中不存在该key (KEY_ALARM_LIST)，将返回NULL
        String content = sp.getString(KEY_ALARM_LIST, null);
//        Log.d("alarm", content+"");
        if (content!=null) {
            String[] timeStrings = content.split(",");
            for (String string : timeStrings) {
                adapter.add(new AlarmData(Long.parseLong(string)));
            }
        }
    }




    private static class AlarmData{
//        用来显示闹钟时间
        private String timeLabel="";
//        闹钟响起的时间
        private long time = 0;
        private Calendar date;

        public AlarmData(long time) {
            this.time = time;
//          获取当前时间
            date = Calendar.getInstance();
            date.setTimeInMillis(time);

            timeLabel = String.format("%d月%d日          %02d:%02d",
                    date.get(Calendar.MONTH)+1,
                    date.get(Calendar.DAY_OF_MONTH),
                    date.get(Calendar.HOUR_OF_DAY),
                    date.get(Calendar.MINUTE)
                    );
        }

        public long getTime() {
            return time;
        }

        public String getTimeLabel() {
            return timeLabel;
        }

        @Override
        public String toString() {
            return getTimeLabel();
        }

//      获取闹钟的请求码，保证请求码唯一，从而获取到请求码对应的闹钟
        public int getId(){
            return (int)(getTime()/1000/60);
        }

    }
}
