package com.github.nicolassmith.urlevaluator;

import android.net.Uri;

public class WootcheckEvaluatorTask extends HostSpecificEvaluatorTask {

	private static final String TAG = "WootcheckEvaluatorTask";
	
	public WootcheckEvaluatorTask(EvaluatorTaskCaller passedCaller) {
		super(passedCaller);
	}

	@Override
	public Uri evaluate(Uri inputUri) {
		// these redirectors actually include the URL in the query string,
		// so no need to make a HTTP request
		return Uri.parse(inputUri.getQueryParameter("url"));
	}

	@Override
	public String[] supportedHosts() {
		// define the hosts for this evaluator
		return new String[] {"www.jdoqocy.com",
				             "www.kqzyfj.com",
				             "www.dpbolvw.net",
				             "www.tkqlhce.com",
				             };
	}
	
	

}
