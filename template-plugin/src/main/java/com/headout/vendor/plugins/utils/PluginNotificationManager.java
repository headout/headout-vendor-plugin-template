package com.headout.vendor.plugins.utils;

import rx.functions.Func3;
import tourlandish.common.enums.slack.SlackChannel;

public class PluginNotificationManager {
	private static Func3<SlackChannel, String, Throwable, Void> taskDelegator = null;

	public static boolean notify(SlackChannel channel , String message, Throwable ex) {
		if (taskDelegator != null) {
			taskDelegator.call(channel, message, ex);
			return true;
		}
		return false;
	}

	public static boolean setDelegator(Func3<SlackChannel, String, Throwable, Void> delegator) {
		if (taskDelegator == null) {
			taskDelegator = delegator;
			return true;
		}
		throw new RuntimeException("taskDelegator already set by VendorPluginManager");
	}

}
