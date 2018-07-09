package com.dongao.dlna.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dongao.dlna.DLNALibrary;
import com.dongao.dlna.R;
import com.dongao.dlna.adapter.GeneralAdapter;
import com.dongao.dlna.moduls.searchdevice.entity.DeviceDisplay;
import com.dongao.dlna.moduls.searchdevice.listener.DeviceRegistryListener;
import com.dongao.dlna.upnp.UpnpServiceBiz;
import com.dongao.dlna.upnp.constants.HandlerWhat;

/**
 * @author jjr
 * @date 2018/7/1
 */
public class SelectDeviceDialog extends DialogFragment implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private TextView find_tv;
    private ProgressBar find_pb;
    private TextView help_tv;
    private ImageView close_iv;
    private ListView list_lv;
    private TextView wifi_tv;
    private GeneralAdapter<DeviceDisplay> adapter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            DeviceDisplay dd = (DeviceDisplay) msg.obj;
            switch (msg.what) {
                case HandlerWhat.ADD:
                    int p = adapter.getPosition(dd);
                    if (adapter.getPosition(dd) >= 0) {
                        // Device already in the list, re-set new value at same position
                        adapter.remove(dd);
                        adapter.insert(dd, p);
                    } else {
                        adapter.add(dd);
                    }
                    break;
                case HandlerWhat.REMOVE:
                    adapter.remove(dd);
                    break;
            }
        }
    };
    private UpnpServiceBiz mUpnpServiceBiz;
    private DeviceRegistryListener mListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //使用不带Theme的构造器, 获得的dialog边框距离屏幕仍有几毫米的缝隙。
        Dialog dialog = new Dialog(getActivity(), R.style.BottomDialog);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dlna_select_device_dialog);
        dialog.setCanceledOnTouchOutside(true);

        //设置宽度为屏宽, 靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);

        init(dialog);

        return dialog;
    }

    private void init(Dialog dialog) {
        find_tv = (TextView) dialog.findViewById(R.id.dlna_select_device_find_tv);
        find_pb = (ProgressBar) dialog.findViewById(R.id.dlna_select_device_find_pb);
        help_tv = (TextView) dialog.findViewById(R.id.dlna_select_device_help_tv);
        close_iv = (ImageView) dialog.findViewById(R.id.dlna_select_device_close_iv);
        list_lv = (ListView) dialog.findViewById(R.id.dlna_select_device_list_lv);
        wifi_tv = (TextView) dialog.findViewById(R.id.dlna_select_device_wifi_tv);
        help_tv.setOnClickListener(this);
        close_iv.setOnClickListener(this);
        adapter = new GeneralAdapter<DeviceDisplay>(getActivity(), android.R.layout.simple_list_item_1, null) {

            @Override
            public void convert(GeneralAdapter.ViewHolder holder, DeviceDisplay item, int position) {
                String text = item.toString();
                holder.setText(android.R.id.text1, text);
            }
        };
        list_lv.setAdapter(adapter);
        list_lv.setOnItemClickListener(this);
        list_lv.setOnItemLongClickListener(this);

        mUpnpServiceBiz = UpnpServiceBiz.newInstance();
        mListener = new DeviceRegistryListener(handler);
        mUpnpServiceBiz.addListener(mListener);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.dlna_select_device_help_tv) {
            Toast.makeText(getActivity(), "打开帮助页面", Toast.LENGTH_LONG).show();
        } else if (i == R.id.dlna_select_device_close_iv){
            dismiss();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DeviceDisplay dd = adapter.getItem(position);
        DLNALibrary.getInstance().setDeviceDisplay(dd);
        dismiss();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        DeviceDisplay deviceDisplay = adapter.getItem(position);
        return showDeviceDetails(deviceDisplay);
    }

    /**
     * 显示设备详细信息
     *
     * @param deviceDisplay
     * @return
     * @author hubing
     */
    protected boolean showDeviceDetails(DeviceDisplay deviceDisplay) {
        AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
        dialog.setTitle("设备详情");
        dialog.setMessage(deviceDisplay.getDetailsMsg());
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dialog.show();
        TextView textView = (TextView) dialog.findViewById(android.R.id.message);
        textView.setTextSize(12);
        return dialog.isShowing();
    }
}
