package com.example.clock;

import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class TimerView extends LinearLayout {
	private static final int MSG_WHAT_TIME_IS_UP = 1;
	private static final int MSG_WHAT_TIME_TICK = 2;

	private int allTimerCount = 0;
	private Timer timer=new Timer();
	private TimerTask timerTask = null;
	private Button btnStart,btnPause,btnResume,btnReset;
	private EditText etHour,etMin,etSec;


	public TimerView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TimerView(Context context) {
		super(context);
	}


	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		btnPause = (Button) findViewById(R.id.btnPause);
		btnPause.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				stopTimer();
				btnPause.setVisibility(View.GONE);
				btnResume.setVisibility(View.VISIBLE);
			}
		});
		btnReset = (Button) findViewById(R.id.btnReset);
		btnReset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				stopTimer();

				etHour.setText("00");
				etMin.setText("00");
				etSec.setText("00");

				btnReset.setVisibility(View.GONE);
				btnResume.setVisibility(View.GONE);
				btnPause.setVisibility(View.GONE);
				btnStart.setVisibility(View.VISIBLE);
			}
		});
		btnResume = (Button) findViewById(R.id.btnResume);
		btnResume.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				startTimer();

				btnResume.setVisibility(View.GONE);
				btnPause.setVisibility(View.VISIBLE);
			}
		});
		btnStart = (Button) findViewById(R.id.btnStart);
		btnStart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startTimer();

				btnStart.setVisibility(View.GONE);
				btnPause.setVisibility(View.VISIBLE);
				btnReset.setVisibility(View.VISIBLE);
			}
		});


		etHour = (EditText) findViewById(R.id.etHour);
		etMin = (EditText) findViewById(R.id.etMin);
		etSec = (EditText) findViewById(R.id.etSec);



		etHour.setText("00");
		etHour.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (!TextUtils.isEmpty(s)) {
					int value = Integer.parseInt(s.toString());

					if (value>59) {
						etHour.setText("59");
					}else if (value<0) {
						etHour.setText("00");
					}
				}
				checkToEnableBtnStart();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		etMin.setText("00");
		etMin.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (!TextUtils.isEmpty(s)) {
					int value = Integer.parseInt(s.toString());

					if (value>59) {
						etMin.setText("59");
					}else if (value<0) {
						etMin.setText("00");
					}
				}
				checkToEnableBtnStart();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		etSec.setText("00");
		etSec.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
//				如果我们把编辑框上的数字全部删除，就会出错，这里的if视为了避免这个问题
				if (!TextUtils.isEmpty(s)) {
					int value = Integer.parseInt(s.toString());

					if (value>59) {
						etSec.setText("59");
					}else if (value<0) {
						etSec.setText("00");
					}
				}
				checkToEnableBtnStart();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		btnStart.setVisibility(View.VISIBLE);
		btnStart.setEnabled(false);
		btnPause.setVisibility(View.GONE);
		btnResume.setVisibility(View.GONE);
		btnReset.setVisibility(View.GONE);
	}
//  如果时分秒大于0，则启用start按钮
	private void checkToEnableBtnStart(){
		btnStart.setEnabled((!TextUtils.isEmpty(etHour.getText())&&Integer.parseInt(etHour.getText().toString())>0)||
				(!TextUtils.isEmpty(etMin.getText())&&Integer.parseInt(etMin.getText().toString())>0)||
				(!TextUtils.isEmpty(etSec.getText())&&Integer.parseInt(etSec.getText().toString())>0));
	}


	private void startTimer(){
		if (timerTask==null) {
			allTimerCount = Integer.parseInt(etHour.getText().toString())*60*60
					+Integer.parseInt(etMin.getText().toString())*60
					+Integer.parseInt(etSec.getText().toString());
			timerTask = new TimerTask() {

				@Override
				public void run() {
					allTimerCount--;
					handler.sendEmptyMessage(MSG_WHAT_TIME_TICK);

					if (allTimerCount<=0) {
						handler.sendEmptyMessage(MSG_WHAT_TIME_IS_UP);
						stopTimer();
					}
				}
			};
			//使用了三个参数的schedule方法用来指定task 过一秒后每隔一秒执行一次
			timer.schedule(timerTask, 1000, 1000);
		}
	}

	private void stopTimer(){
		if (timerTask!=null) {
			timerTask.cancel();
			timerTask = null;
		}
	}
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_WHAT_TIME_TICK:

				int hour = allTimerCount/60/60;
				int min = (allTimerCount/60)%60;
				int sec = allTimerCount%60;
				etHour.setText(String.format("%02d",hour));
				etMin.setText(String.format("%02d",min));
				etSec.setText(String.format("%02d",sec));
				break;
			case MSG_WHAT_TIME_IS_UP:
				new AlertDialog.Builder(getContext()).setTitle("Time is up").setMessage("Time is up").setNegativeButton("Cancel", null).show();
				
				btnReset.setVisibility(View.GONE);
				btnResume.setVisibility(View.GONE);
				btnPause.setVisibility(View.GONE);
				btnStart.setVisibility(View.VISIBLE);
				
				break;
			default:
				break;
			}
		};
	};


}
