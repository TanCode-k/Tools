package com.simple.middleman.lanmitm.service;

import com.simple.middleman.R;
import com.simple.middleman.lanmitm.AppContext;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class BaseService extends Service {

	public static final int HTTP_SERVER_NOTICE = 1;
	public static final int HIJACK_NOTICE = 2;
	public static final int SNIFFER_NOTICE = 3;
	public static final int INJECT_NOTICE = 4;
	public static final int KILL_NOTICE = 5;
	
	public static final int ARPSPOOF_NOTICE = 0;

	protected int my_notice_id = -1;
	protected String my_ticker_text = null;
	protected Class<?> cls = null;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		notice();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		clearNotice();
		super.onDestroy();
	}

	@SuppressWarnings("deprecation")
	protected void notice() {
		NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(this);

        Intent intent = new Intent(this, cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent contentIntent = PendingIntent.getActivity(this,
                R.string.app_name, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setSmallIcon(R.drawable.ic_launch_notice)
                .setTicker(getString(R.string.app_name))
                .setWhen(System.currentTimeMillis())
//                .setFlag(Notification.FLAG_NO_CLEAR,true)
                .setContentIntent(contentIntent);

        nm.notify(my_notice_id,builder.build());


//		n.setLatestEventInfo(this, this.getString(R.string.app_name),
//				my_ticker_text, contentIntent);
//		nm.notify(my_notice_id, n);
	}

	protected void clearNotice() {
		NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		nm.cancel(my_notice_id);
	}

	protected void stopArpService() {
		if (!AppContext.isHijackRunning && !AppContext.isInjectRunning
				&& !AppContext.isTcpdumpRunning && !AppContext.isKillRunning) {
			stopService(new Intent(this, ArpService.class));
		}
	}

	protected void startArpService() {
		if (!AppContext.isHijackRunning && !AppContext.isInjectRunning
				&& !AppContext.isTcpdumpRunning && !AppContext.isKillRunning) {
			Intent intent = new Intent(this, ArpService.class);
			intent.putExtra("arp_cheat_way", ArpService.ONE_WAY_HOST);
			startService(intent);
		}
	}
	
}
