package com.dongao.dlna.moduls.avtransport.callback.renderingcontrol;

import com.dongao.dlna.upnp.UpnpActionCallback;

import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.types.InvalidValueException;
import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;

import java.util.Map;

/**
 * @author hubing
 * @version 1.0.0 2015-4-29
 */

public abstract class SelectPreset extends UpnpActionCallback {

	private static String TAG = SelectPreset.class.getSimpleName();
	
	public SelectPreset(Service service, UnsignedIntegerFourBytes instanceId, String presetName) {
		super(new ActionInvocation(service.getAction("SelectPreset")));
		try {
			setInput("InstanceID", instanceId);
			setInput("PresetName", presetName);
		} catch (InvalidValueException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void received(ActionInvocation invocation, Map<String, Object> result) {
		onSuccess("SelectPreset successed");
	}

	public abstract void onSuccess(String msg);

}

