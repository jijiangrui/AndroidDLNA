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

public abstract class ListPresets extends UpnpActionCallback {

	private static String TAG = ListPresets.class.getSimpleName();
	
	public ListPresets(Service service, UnsignedIntegerFourBytes instanceId) {
		super(new ActionInvocation(service.getAction("ListPresets")));
		try {
			setInput("InstanceID", instanceId);
		} catch (InvalidValueException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void received(ActionInvocation invocation, Map<String, Object> result) {
		onSuccess((String) result.get("CurrentPresetNameList"));
	}

	public abstract void onSuccess(String msg);

}
