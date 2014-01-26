package com.github.nicolassmith.urlevaluator;

import android.net.Uri;

public class GoogleQEvaluatorTask extends HostSpecificEvaluatorTask {

	public GoogleQEvaluatorTask(EvaluatorTaskCaller passedCaller) {
		super(passedCaller);
	}

	@Override
	public String[] supportedHosts() {
		return new String[] {"www.google.com"};
	}

	@Override
	public Uri evaluate(Uri uri) {
		return Uri.parse(uri.getQueryParameter("q"));
	}

}
