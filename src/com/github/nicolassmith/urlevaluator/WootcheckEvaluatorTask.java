package com.github.nicolassmith.urlevaluator;

import java.util.Arrays;

import android.net.Uri;

public class WootcheckEvaluatorTask extends EvaluatorTask {

	private static final String TAG = "WootcheckEvaluatorTask";
	private static final String[] HOSTS = new String[] {"www.jdoqocy.com"};
	
	public WootcheckEvaluatorTask(EvaluatorTaskCaller passedCaller) {
		super(passedCaller);
	}

	@Override
	public String evaluate(String inputURLString) {
		// these redirectors actually include the URL in the query string,
		// so no need to make a HTTP request
		return Uri.parse(inputURLString).getQueryParameter("url");
	}
	
	public static boolean isSupportedHost(String host) {
		return Arrays.asList(HOSTS).contains(host);
	}

}
