package com.sample;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.seekcircle.SeekCircle;
import com.seekcircle.sample.R;

public class SampleActivity extends Activity
{
	TextView textProgress;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		SeekCircle seekCircle = (SeekCircle)findViewById(R.id.seekCircle);
		textProgress = (TextView) findViewById(R.id.textProgress);
		textProgress.setOnClickListener(new MyListener());
		seekCircle.setOnSeekCircleChangeListener(new SeekCircle.OnSeekCircleChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekCircle seekCircle)
			{}
			
			@Override
			public void onStartTrackingTouch(SeekCircle seekCircle)
			{}
			
			@Override
			public void onProgressChanged(SeekCircle seekCircle, int progress, boolean fromUser)
			{
				updateText();
			}


		});

		updateText();


	}
	class MyListener implements View.OnClickListener{

		boolean check;
		@Override
		public void onClick(View view) {
		//	Toast.makeText(getApplicationContext(), "클릭", Toast.LENGTH_LONG ).show();
			textProgress.setTextColor(getResources().getColor(R.color.Red));
			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				public void run() {
					textProgress.setTextColor(getResources().getColor(R.color.White));
				}
			}, 2000);

		}
	}


	private void updateText()
	{
		SeekCircle seekCircle = (SeekCircle)findViewById(R.id.seekCircle);
		TextView textProgress = (TextView)findViewById(R.id.textProgress);

		if (textProgress != null && seekCircle != null)
		{
			int progress = seekCircle.getProgress();
			textProgress.setText(Integer.toString(progress));
		}
	}

}
