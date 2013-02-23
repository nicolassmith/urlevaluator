package com.github.nicolassmith.urlevaluator;

import android.net.Uri;
import android.os.AsyncTask;

abstract class EvaluatorTask extends AsyncTask<String,Void,String>{
	/** Abstract class for evaluator service for various short URL providers **/
	
	EvaluatorTaskCaller caller;
	
	public EvaluatorTask(EvaluatorTaskCaller passedCaller){
		caller = passedCaller;
	}
	
	public abstract String evaluate(String arg);

	//@Override
	//protected String doInBackground(String... arg[0]) {		
	//	return evaluate(arg0[0]);
	//}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		caller.onTaskCompleted(result);
	}

	@Override
	protected String doInBackground(String... arg) {
		return evaluate(arg[0]);
	}
	
	
}
