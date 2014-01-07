package com.github.nicolassmith.urlevaluator;

import android.net.Uri;
import android.util.Log;

public class MultipleRedirectEvaluatorTask extends HostSpecificEvaluatorTask {

	private static final String TAG = "MultipleRedirectEvaluatorTask";
	private EvaluatorTask singleEvaluator;
	
	public MultipleRedirectEvaluatorTask(EvaluatorTaskCaller passedCaller) {
		super(passedCaller);
	}

	@Override
	public String evaluate(String uriStringIn) {
		
		String hostName = Uri.parse(uriStringIn).getHost();

		// get the right evaluator for the host
		singleEvaluator = caller.chooseEvaluator(hostName,false);
		
		// do a single evaluation
		String evaluated = singleEvaluator.evaluate(uriStringIn);
		
		// evaluate recursively until we don't get a redirect
		if(evaluated == null){
			return uriStringIn;
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
