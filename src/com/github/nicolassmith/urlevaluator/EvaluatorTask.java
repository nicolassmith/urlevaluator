package com.github.nicolassmith.urlevaluator;

import android.os.AsyncTask;

/** Abstract class for evaluator service for various short URL providers **/
abstract class EvaluatorTask extends AsyncTask<String, Void, String> {

	protected final EvaluatorTaskCaller caller;

	public EvaluatorTask(EvaluatorTaskCaller passedCaller) {
		caller = passedCaller;
	}

	public abstract String evaluate(String arg);

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
