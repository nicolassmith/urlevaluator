package com.github.nicolassmith.urlevaluator;

import android.net.Uri;
import android.util.Log;

public class MultipleRedirectEvaluatorTask extends HostSpecificEvaluatorTask {

	private static final String TAG = "MultipleRedirectEvaluatorTask";
	private EvaluatorTask singleEvaluator;
	private EvaluatorTaskCaller caller;
	
	public MultipleRedirectEvaluatorTask(EvaluatorTaskCaller passedCaller) {
		super(passedCaller);
		caller = passedCaller;
	}

	@Override
	public String evaluate(String uriStringIn) {
		
		String hostName = Uri.parse(uriStringIn).getHost();

		// get the right evaluator for the host
		singleEvaluator = caller.chooseEvaluator(hostName,false);
		
		// do a single evaluation
		String evaluated = singleEvaluator.evaluate(uriStringIn);
		//Log.d(TAG, "evaluated as = " + evaluated);
		
		// evaluate recursively until the answer doesn't change
		if(evaluated == null || evaluated.equals(uriStringIn)){
			//Log.d(TAG, "found match, returning");
			return evaluated;
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
