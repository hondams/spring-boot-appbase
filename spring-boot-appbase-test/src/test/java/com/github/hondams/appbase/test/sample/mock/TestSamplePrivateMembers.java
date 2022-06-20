package com.github.hondams.appbase.test.sample.mock;

public class TestSamplePrivateMembers {

	private static String staticField;

	private String instanceField;

	public static void clear() {
		staticField = null;
	}

	@SuppressWarnings("unused")
	private static String invokeStaticMethod(String arg1) {
		return staticField + ":" + arg1;
	}

	@SuppressWarnings("unused")
	private String invokeInstanceMethod(String arg1) {
		return instanceField + ":" + arg1;
	}
}
