package com.dongao.dlna.moduls.avtransport.bll;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

import com.dongao.dlna.moduls.avtransport.entity.PositionInfo;

import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;

/**
 * @author hubing
 * @version 1.0.0 2015-4-28
 */

public class RealtimeUpdatePositionInfo extends AsyncTask<Void, PositionInfo, PositionInfo> {

	private static String TAG = RealtimeUpdatePositionInfo.class.getSimpleName();
	
	private boolean isPlaying; // 是否正在播放
	
	private SeekBar sbPlayback;
	private TextView tvCurTime;
	private TextView tvTotalTime;
	private MediaControlBiz controlBiz;
	
	public RealtimeUpdatePositionInfo(MediaControlBiz controlBiz, SeekBar sbPlayback, TextView tvCurTime, TextView tvTotalTime) {
		this.sbPlayback = sbPlayback;
		this.tvCurTime = tvCurTime;
		this.tvTotalTime = tvTotalTime;
		this.controlBiz = controlBiz;
		
		sbPlayback.setMax(100);
		isPlaying = true;
	}
	
	@Override
	protected PositionInfo doInBackground(Void... params) {
		PositionInfo info = null;
		while (!isCancelled()) {
			while (isPlaying) {
				try {
					Thread.sleep(1000);
					controlBiz.getPositionInfo(new MediaControlBiz.GetPositionInfoListerner() {
						
						@Override
						public void onSuccess(PositionInfo positionInfo) {
							publishProgress(positionInfo);
						}
						
						@Override
						public void failure(ActionInvocation invocation, UpnpResponse operation,
                                            String defaultMsg) {
							Log.d(TAG, "Get position info failure:" + defaultMsg);
						}
					});
				} catch (InterruptedException e) {
					Log.d(TAG, "Get position info failure:" + e.getMessage());
				}
			}
		}
		return info;
	}

	@Override
	protected void onProgressUpdate(PositionInfo... values) {
		PositionInfo info = values[0];
		sbPlayback.setProgress(info.getElapsedPercent());
		tvCurTime.setText(info.getRelTime());
		tvTotalTime.setText(info.getTrackDuration());
	}
	
	public boolean isPlaying() {
		return isPlaying;
	}

	/**
	 * 设置是否播放
	 * @param isPlaying
	 */
	public void setPlaying(boolean isPlaying) {
		this.isPlaying = isPlaying;
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
		sbPlayback = null;
		tvCurTime = null;
		tvTotalTime = null;
		controlBiz = null;
	}
	
}

