package com.github.nicolassmith.urlevaluator;

import android.net.Uri;
import android.os.AsyncTask;

/** Abstract class for evaluator service for various short URL providers **/
abstract class EvaluatorTask extends AsyncTask<Uri, Void, Uri> {

	protected final EvaluatorTaskCaller caller;

	public EvaluatorTask(EvaluatorTaskCaller passedCaller) {
		caller = passedCaller;
	}

	public abstract Uri evaluate(Uri arg);

	@Override
	protected void onPostExecute(Uri result) {
		super.onPostExecute(result);
		caller.onTaskCompleted(result);
	}

	@Override
	protected Uri doInBackground(Uri... arg) {
		return evaluate(arg[0]);
	}
}
