package com.dongao.dlna.moduls.avtransport.callback.avtransport;

import com.dongao.dlna.upnp.UpnpActionCallback;

import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.types.InvalidValueException;
import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;
import org.fourthline.cling.support.model.TransportAction;

import java.util.Map;

/**
 * @author hubing
 * @version 1.0.0 2015-4-28
 */

public abstract class GetCurrentTransportActions extends UpnpActionCallback {

	private static String TAG = GetCurrentTransportActions.class.getSimpleName();

	public GetCurrentTransportActions(UnsignedIntegerFourBytes instanceId,
			Service service) {
		super(new ActionInvocation(
				service.getAction("GetCurrentTransportActions")));
		try {
			setInput("InstanceID", instanceId);
		} catch (InvalidValueException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void received(ActionInvocation invocation, Map<String, Object> result) {
		String actionsString = (String) result.get("Actions");
		onSuccess(TransportAction.valueOfCommaSeparatedList(actionsString));
	}

	public abstract void onSuccess(TransportAction[] actions);

}
