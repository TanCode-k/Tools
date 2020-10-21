package com.simple.middleman.lanmitm.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.simple.middleman.R;
import com.simple.middleman.lanmitm.ActionBarActivity;
import com.simple.middleman.lanmitm.AppContext;
import com.simple.middleman.lanmitm.service.HijackService;
import com.simple.middleman.lanmitm.service.InjectService;
import com.simple.middleman.lanmitm.service.KillService;
import com.simple.middleman.lanmitm.service.SnifferService;

/**
 *
 * @author oinux
 *
 */
public class KillActivity extends ActionBarActivity {

	private static final String TAG = "KillActivity";

	private CheckBox killCheckBox;
	private View headerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, R.layout.kill_activity);

		setBarTitle(Html.fromHtml("<b>" + getString(R.string.prohibit_internet)
				+ "</b> - <small>" + AppContext.getTarget().getIp() + "</small>"));

		headerView = findViewById(R.id.header_view);

		killCheckBox = (CheckBox) findViewById(R.id.kill_check_box);
		if (AppContext.isKillRunning) {
			killCheckBox.setChecked(true);
		} else {
			killCheckBox.setChecked(false);
		}
		killCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				Intent intent = new Intent(KillActivity.this, KillService.class);
				if (isChecked) {
					if (AppContext.isHijackRunning)
						stopService(new Intent(KillActivity.this,
								HijackService.class));
					if (AppContext.isInjectRunning)
						stopService(new Intent(KillActivity.this,
								InjectService.class));
					if (AppContext.isTcpdumpRunning)
						stopService(new Intent(KillActivity.this,
								SnifferService.class));
					headerView.setVisibility(View.VISIBLE);
					startService(intent);
				} else {
					headerView.setVisibility(View.GONE);
					stopService(intent);
				}
			}
		});
	}

	@Override
	public void onBackPressed() {
		finish();
		overridePendingTransition(R.anim.slide_right, R.anim.slide_right_out);
	}
}
