package com.github.nicolassmith.urlevaluator;

import android.net.Uri;

public class WootcheckEvaluatorTask extends HostSpecificEvaluatorTask {

	private static final String TAG = "WootcheckEvaluatorTask";
	
	public WootcheckEvaluatorTask(EvaluatorTaskCaller passedCaller) {
		super(passedCaller);
	}

	@Override
	public String evaluate(String inputURLString) {
		// these redirectors actually include the URL in the query string,
		// so no need to make a HTTP request
		return Uri.parse(inputURLString).getQueryParameter("url");
	}

	@Override
	public String[] supportedHosts() {
		// define the hosts for this evaluator
		return new String[] {"www.jdoqocy.com","www.kqzyfj.com"};
	}
	
	

}
