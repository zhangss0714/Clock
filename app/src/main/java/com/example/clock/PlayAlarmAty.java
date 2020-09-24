package com.example.clock;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;

import java.util.Calendar;

//播放闹钟的界面
public class PlayAlarmAty extends Activity{


	private MediaPlayer mp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarm_player_aty);
		Calendar calendar = Calendar.getInstance();
		mp = MediaPlayer.create(this, R.raw.music);
		mp.start();
		//创建一个闹钟提醒的对话框,点击确定关闭铃声与页面

		new AlertDialog.Builder(this).setTitle("闹钟").setMessage(String.format("%02d:%02d",calendar
				.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE)))
				.setPositiveButton("关闭闹铃", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						mp.stop();
						PlayAlarmAty.this.finish();
					}
				}).show();

	}

	@Override
	protected void onPause() {
		super.onPause();

		finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mp.release();
		mp=null;
	}
}
