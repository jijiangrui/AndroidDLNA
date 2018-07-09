package com.example.jjr.dlna;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dongao.dlna.DLNALibrary;
import com.dongao.dlna.moduls.avtransport.bll.MediaControlBiz;
import com.dongao.dlna.moduls.avtransport.bll.MediaEventBiz;
import com.dongao.dlna.moduls.avtransport.bll.RealtimeUpdatePositionInfo;
import com.dongao.dlna.moduls.avtransport.constants.MediaControlWhat;
import com.dongao.dlna.moduls.avtransport.constants.MediaEventWhat;
import com.dongao.dlna.moduls.avtransport.constants.TransportState;
import com.dongao.dlna.moduls.avtransport.entity.AVTransportInfo;
import com.dongao.dlna.moduls.avtransport.entity.RenderingControlInfo;
import com.dongao.dlna.view.SelectDeviceDialog;

import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.support.model.item.Item;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton ibStop;
    protected ImageButton ibPlay;
    protected ImageButton ibPause;
    private ImageButton ibPlaypre;
    private ImageButton ibPlaynext;
    private SeekBar sbPlayback;
    protected TextView tvTotalTime;
    protected TextView tvCurrVol;
    private Button btnAddVol;
    private Button btnSubVol;
    private Item item;
    private ImageView ivMute;

    private Handler handler;
    private long mId; // item播放实例id
    protected MediaControlBiz controlBiz;
    private boolean currentMute;
    private MediaEventBiz eventBiz;
    protected int currVolume;
    private RealtimeUpdatePositionInfo realtimeUpdate;
    private TextView tvCurTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MediaControlWhat.SET_AVTRANSPORT_URI:
                        controlBiz.getVolume();
                        break;
                    case MediaControlWhat.PLAY:
                        updatePlayingState(true);
                        break;
                    case MediaControlWhat.PAUSE:
                        updatePlayingState(false);
                        break;
                    case MediaControlWhat.STOP:
                        updatePlayingState(false);
                        break;
                    case MediaControlWhat.GET_VOLUME:
                        currVolume = msg.arg2;
                        tvCurrVol.setText(getString(R.string.current_volume)
                                + currVolume);
                        break;
                    case MediaControlWhat.SET_VOLUME:
                        tvCurrVol.setText(getString(R.string.current_volume)
                                + currVolume);
                        break;
                    case MediaControlWhat.SET_MUTE:
                        ivMute.setSelected(currentMute);
                        break;
                    case MediaEventWhat.RENDERING_CONTROL:
                        RenderingControlInfo rcInfo = (RenderingControlInfo) msg.obj;
                        HashMap<String, Boolean> vChange = rcInfo.getValueIsChange();
                        if (vChange.get(RenderingControlInfo.MUTE)) {
                            currentMute = rcInfo.isMute();
                            ivMute.setSelected(currentMute);
                        }
                        if (vChange.get(RenderingControlInfo.VOLUME)) {
                            currVolume = rcInfo.getVolume();
                            tvCurrVol.setText(getString(R.string.current_volume)
                                    + currVolume);
                        }
                        break;
                    case MediaEventWhat.AV_TRANSPORT:
                        AVTransportInfo avtInfo = (AVTransportInfo) msg.obj;
                        HashMap<String, Boolean> currStates = avtInfo.getValueIsChange();
                        if (currStates.get(AVTransportInfo.CURRENT_MEDIA_DURATION)) {
                            tvTotalTime.setText(avtInfo.getCurrentMediaDuration());
                        }
                        if (currStates.get(AVTransportInfo.TRANSPORT_STATE)) {
                            String currState = avtInfo.getTransportState();
                            if (TransportState.PLAYING.equals(currState)) {
                                updatePlayingState(true);
                            } else {
                                updatePlayingState(false);
                            }
                        }
                        break;
                }

            }
        };
        initView();
    }

    private void initView() {
        ImageView iv_tv = (ImageView) findViewById(R.id.iv_tv);
        iv_tv.setOnClickListener(this);
        TextView tv_device = (TextView) findViewById(R.id.tv_device);
        tv_device.setOnClickListener(this);

        // 停止
        ibStop = (ImageButton) findViewById(R.id.ib_stop);
        // 播放
        ibPlay = (ImageButton) findViewById(R.id.ib_play);
        // 暂停
        ibPause = (ImageButton) findViewById(R.id.ib_pause);
        // 上一首
        ibPlaypre = (ImageButton) findViewById(R.id.ib_playpre);
        // 下一首
        ibPlaynext = (ImageButton) findViewById(R.id.ib_playnext);
        // 静音
        ivMute = (ImageView) findViewById(R.id.iv_mute);
        // 当前音量大小
        tvCurrVol = (TextView) findViewById(R.id.tv_curr_vol);
        // 增加音量
        btnAddVol = (Button) findViewById(R.id.btn_add_vol);
        // 降低音量
        btnSubVol = (Button) findViewById(R.id.btn_sub_vol);

        // 播放音乐的进度
        sbPlayback = (SeekBar) findViewById(R.id.sb_playback);
        // 当前播放的时间
        tvCurTime = (TextView) findViewById(R.id.tv_curTime);
        // 总时间
        tvTotalTime = (TextView) findViewById(R.id.tv_totalTime);

        ibStop.setOnClickListener(this);
        ibPlay.setOnClickListener(this);
        ibPause.setOnClickListener(this);
        ibPlaypre.setOnClickListener(this);
        ibPlaynext.setOnClickListener(this);
        ivMute.setOnClickListener(this);
        btnAddVol.setOnClickListener(this);
        btnSubVol.setOnClickListener(this);
        sbPlayback.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int prog = seekBar.getProgress();
                String totalTime = tvTotalTime.getText().toString();
                controlBiz.seek(totalTime, prog);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_tv:
                if (DLNALibrary.getInstance().getDeviceDisplay() != null) {
                    Device device = DLNALibrary.getInstance().getDeviceDisplay().getDevice();
                    mId = 0;
                    currentMute = false;
                    controlBiz = new MediaControlBiz(device, handler, mId);
                    eventBiz = new MediaEventBiz(device, handler);

                    // 启动从DMP设备更新播放状态
                    realtimeUpdate = new RealtimeUpdatePositionInfo(controlBiz, sbPlayback,
                            tvCurTime, tvTotalTime);
                    realtimeUpdate.execute();
                    eventBiz.addRenderingEvent();
                    eventBiz.addAVTransportEvent();

                    controlBiz.setPlayUri("http://v.dongaocloud.com/2a86/2a86/12c/7f1/237c3f651d289196ac56186047c8e70a.mp4");
                } else {
                    SelectDeviceDialog dialog = new SelectDeviceDialog();
                    dialog.show(getSupportFragmentManager(), "select_device_dialog");
                }
                break;
            case R.id.tv_device:
                SelectDeviceDialog dialog = new SelectDeviceDialog();
                dialog.show(getSupportFragmentManager(), "select_device_dialog");
                break;
            case R.id.ib_stop:
                controlBiz.stop();
                break;
            case R.id.ib_play:
                controlBiz.play();
                break;
            case R.id.ib_pause:
                controlBiz.pause();
                break;
            case R.id.ib_playpre:
                controlBiz.previous();
                break;
            case R.id.ib_playnext:
                controlBiz.next();
                break;
            case R.id.iv_mute:
                if (currentMute) {
                    currentMute = false;
                } else {
                    currentMute = true;
                }
                controlBiz.setMute(currentMute);
                break;
            case R.id.btn_add_vol:
                currVolume++;
                if (currVolume > 100) {
                    currVolume = 100;
                    Toast.makeText(this, "已经是最大音量了", Toast.LENGTH_SHORT).show();
                    return;
                }
                controlBiz.setVolume(currVolume);
                break;
            case R.id.btn_sub_vol:
                currVolume--;
                if (currVolume < 0) {
                    currVolume = 0;
                    Toast.makeText(this, "已经是最小音量了", Toast.LENGTH_SHORT).show();
                    return;
                }
                controlBiz.setVolume(currVolume);
                break;
            default:
                break;
        }
    }

    protected void updatePlayingState(boolean isPlaying) {
        realtimeUpdate.setPlaying(isPlaying);
        if (isPlaying) {
            ibPlay.setVisibility(View.GONE);
            ibPause.setVisibility(View.VISIBLE);
        } else {
            ibPlay.setVisibility(View.VISIBLE);
            ibPause.setVisibility(View.GONE);
        }
    }
}
