package com.ind.csvfile;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class DialogActivity extends Activity
{
	TextView link1, link2, link3;
	Button close;
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info_dialo);
		getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		link1 = (TextView) findViewById(R.id.link1);
		link2 = (TextView) findViewById(R.id.link2);
		link3 = (TextView) findViewById(R.id.link3);
		close = (Button) findViewById(R.id.ok);
		link1.setMovementMethod(LinkMovementMethod.getInstance());
		link2.setMovementMethod(LinkMovementMethod.getInstance());
		link3.setMovementMethod(LinkMovementMethod.getInstance());
		close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
}
