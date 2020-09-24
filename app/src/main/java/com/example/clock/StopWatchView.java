package com.example.clock;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class StopWatchView extends LinearLayout {
//	private int tenMSecs = 0;
	private int MSecs = 0;
	private Timer timer = new Timer();
	private TimerTask timerTask = null;
	private TimerTask showTimeTask = null;

	private TextView tvHour,tvMin,tvSec,tvMSec;
	private Button btnStart,btnResume,btnReset,btnPause,btnLap;
	private ListView lvTimeList;
	private ArrayAdapter<String> adapter;
	private static final int MSG_WHAT_SHOW_TIME = 1;



	public StopWatchView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		tvHour = (TextView) findViewById(R.id.timeHour);
		tvHour.setText(String.format("%02d",0));
		tvMin = (TextView) findViewById(R.id.timeMin);
		tvMin.setText(String.format("%02d",0));
		tvSec = (TextView) findViewById(R.id.timeSec);
		tvSec.setText(String.format("%02d",0));
		tvMSec = (TextView) findViewById(R.id.timeMSec);
		tvMSec.setText(String.format("%03d",0));

		btnLap = (Button) findViewById(R.id.btnSWLap);
		btnLap.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				adapter.insert(String.format("%02d"+":"+"%02d"+":"+"%02d"+"."+"%03d",
						MSecs/1000/60/60,MSecs/1000/60%60,MSecs/1000%60,MSecs%1000), 0);
			}
		});
		btnPause = (Button) findViewById(R.id.btnSWPause);
		btnPause.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				stopTimer();

				btnPause.setVisibility(View.GONE);
				btnResume.setVisibility(View.VISIBLE);
				btnLap.setVisibility(View.VISIBLE);
				btnReset.setVisibility(View.VISIBLE);
			}
		});
		btnReset = (Button) findViewById(R.id.btnSWReset);
		btnReset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				stopTimer();
				MSecs = 0;
				adapter.clear();

				btnLap.setVisibility(View.GONE);
				btnPause.setVisibility(View.GONE);
				btnReset.setVisibility(View.GONE);
				btnResume.setVisibility(View.GONE);
				btnStart.setVisibility(View.VISIBLE);
			}
		});
		btnResume = (Button) findViewById(R.id.btnSWResume);
		btnResume.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				startTimer();
				btnResume.setVisibility(View.GONE);
				btnPause.setVisibility(View.VISIBLE);
				btnReset.setVisibility(View.GONE);
				btnLap.setVisibility(View.VISIBLE);
			}
		});
		btnStart = (Button) findViewById(R.id.btnSWStart);
		btnStart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startTimer();

				btnStart.setVisibility(View.GONE);
				btnReset.setVisibility(View.VISIBLE);
				btnPause.setVisibility(View.VISIBLE);
				btnLap.setVisibility(View.VISIBLE);
			}
		});

		btnLap.setVisibility(View.GONE);
		btnPause.setVisibility(View.GONE);
		btnReset.setVisibility(View.GONE);
		btnResume.setVisibility(View.GONE);

		lvTimeList=(ListView) findViewById(R.id.lvWatchTimeList);
		adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1);
		lvTimeList.setAdapter(adapter);

		showTimeTask = new TimerTask() {

			@Override
			public void run() {
				hander.sendEmptyMessage(MSG_WHAT_SHOW_TIME);
			}
		};
//		让屏幕显示的时候，每隔100毫秒显示一次
		timer.schedule(showTimeTask, 100, 100);
	}

	private void startTimer(){
		if (timerTask==null) {
			timerTask = new TimerTask() {

				@Override
				public void run() {
//					tenMSecs++;
					MSecs++;
				}
			};
			timer.schedule(timerTask, 1, 1);
		}
	}

	private void stopTimer(){
		if (timerTask!=null) {
			timerTask.cancel();
			timerTask=null;
		}
	}
	private Handler hander = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_WHAT_SHOW_TIME:
				tvHour.setText(String.format("%02d",MSecs/1000/60/60));
				tvMin.setText(String.format("%02d",MSecs/1000/60%60));
				tvSec.setText(String.format("%02d",MSecs/1000%60));
				tvMSec.setText(String.format("%03d",MSecs%1000));
				break;
			default:
				break;
			}
		};
	};


	public void onDestory() {
		timer.cancel();
	}
}
