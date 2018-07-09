package com.dongao.dlna.upnp;

import android.util.Log;

import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.meta.LocalDevice;
import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.registry.DefaultRegistryListener;
import org.fourthline.cling.registry.Registry;

/**
 * @author hubing
 * @version 1.0.0 2015-4-29
 */

public abstract class UpnpRegistryListener extends DefaultRegistryListener {

	@Override
	public void remoteDeviceDiscoveryStarted(Registry registry, RemoteDevice device) {
		deviceAdded(device);
	}

	@Override
	public void remoteDeviceDiscoveryFailed(Registry registry, RemoteDevice device, Exception ex) {
		Log.d("", "Discovery failed of '"
				+ device.getDisplayString()
				+ "': "
				+ (ex != null ? ex.toString()
						: "Couldn't retrieve device/service descriptors"));
		deviceRemoved(device);
	}

	@Override
	public void remoteDeviceAdded(Registry registry, RemoteDevice device) {
		deviceAdded(device);
	}

	@Override
	public void remoteDeviceUpdated(Registry registry, RemoteDevice device) {
		deviceAdded(device);
	}

	@Override
	public void remoteDeviceRemoved(Registry registry, RemoteDevice device) {
		deviceRemoved(device);
	}

	@Override
	public void localDeviceAdded(Registry registry, LocalDevice device) {
		deviceAdded(device);
	}

	@Override
	public void localDeviceRemoved(Registry registry, LocalDevice device) {
		deviceRemoved(device);
	}

	/**
	 * 添加设备
	 * 
	 * @param device
	 */
	public abstract void deviceAdded(Device device);

	/**
	 * 删除设备
	 * 
	 * @param device
	 */
	public abstract void deviceRemoved(Device device);

}
