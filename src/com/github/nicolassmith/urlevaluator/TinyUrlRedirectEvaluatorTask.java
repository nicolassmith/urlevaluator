package com.github.nicolassmith.urlevaluator;

import android.net.Uri;

public class TinyUrlRedirectEvaluatorTask extends HostSpecificEvaluatorTask {

	public TinyUrlRedirectEvaluatorTask(EvaluatorTaskCaller passedCaller) {
		super(passedCaller);
	}

	@Override
	public String[] supportedHosts() {
		// define the hosts for this evaluator
		return new String[] {"redirect.tinyurl.com"};
	}

	@Override
	public String evaluate(String inputURLString) {
		return Uri.parse(inputURLString).getQueryParameter("out");
	}

}
