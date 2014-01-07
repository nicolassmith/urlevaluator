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
	public Uri evaluate(Uri inputUri) {
		return Uri.parse(inputUri.getQueryParameter("out"));
	}

}
