package com.github.nicolassmith.urlevaluator;

import android.net.Uri;

public class MultipleRedirectEvaluatorTask extends HostSpecificEvaluatorTask {

	private static final boolean ALLOW_MULTIPLE_REDIRECT_CHOICE = false;
	private static final String TAG = "MultipleRedirectEvaluatorTask";
	
	public MultipleRedirectEvaluatorTask(EvaluatorTaskCaller passedCaller) {
		super(passedCaller);
	}

	@Override
	public Uri evaluate(Uri inputUri) {
		
		// Has this task been canceled?
		if (this.isCancelled()){
			return null;
		}
		
		// get the right evaluator for the host
		EvaluatorTask singleEvaluator = caller.chooseEvaluator(inputUri,ALLOW_MULTIPLE_REDIRECT_CHOICE);
		
		// do a single evaluation
		Uri evaluated = singleEvaluator.evaluate(inputUri);
		
		// evaluate recursively until we don't get a redirect
		if(evaluated == null){
			return inputUri;
		} else {
			return this.evaluate(evaluated);
		}
	}

	@Override
	public String[] supportedHosts() {
		// define the hosts for this evaluator
		return new String[] {"tinyurl.com"};
	}

}
