package com.dongao.dlna.moduls.searchdevice.listener;

import android.os.Handler;
import android.os.Message;

import com.dongao.dlna.moduls.searchdevice.entity.DeviceDisplay;
import com.dongao.dlna.upnp.UpnpRegistryListener;
import com.dongao.dlna.upnp.constants.HandlerWhat;

import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.registry.Registry;

/**
 * @author hubing
 * @version 1.0.0 2015-4-16
 */

public class DeviceRegistryListener extends UpnpRegistryListener {

	private Handler handler;
	
	public DeviceRegistryListener(Handler handler) {
		this.handler = handler;
	}
	
	/**
	 * 添加设备
	 * @param device
	 */
	@Override
	public void deviceAdded(Device device) {
		if (handler == null) {
			return;
		}
		DeviceDisplay d = new DeviceDisplay(device);
		Message msg = Message.obtain(handler);
		msg.what = HandlerWhat.ADD;
		msg.obj = d;
		msg.sendToTarget();
	}

	/**
	 * 删除设备
	 * @param device
	 */
	@Override
	public void deviceRemoved(Device device) {
		if (handler == null) {
			return;
		}
		Message msg = Message.obtain(handler);
		msg.what = HandlerWhat.REMOVE;
		msg.obj = new DeviceDisplay(device);
		msg.sendToTarget();
	}

	@Override
	public void beforeShutdown(Registry registry) {
	}

	@Override
	public void afterShutdown() {
	}

}
