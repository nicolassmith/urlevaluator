package com.github.nicolassmith.urlevaluator;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;

/**
 * This is the {@link Activity} that is called when a short URL intent is thrown
 * by the Android OS.
 **/
public class UrlEvaluatorActivity extends Activity implements
		EvaluatorTaskCaller {
	private static final String TAG = "UrlEvaluatorActivity";

	private Toast evaluatingToast;
	private Toast conclusionToast;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// grab the URL from the intent data
		Uri inputUri = getIntent().getData();

		evaluatingToast = Toast.makeText(this, getString(R.string.evaluating, inputUri), Toast.LENGTH_LONG);
		evaluatingToast.show();

		// send it to an EvaluatorTask
		EvaluatorTask evaluatorTask;
		// check if it's a woot-check url
		if (WootcheckEvaluatorTask.isSupportedHost(inputUri.getHost())) {
			evaluatorTask = new WootcheckEvaluatorTask(this);
		} else {
			evaluatorTask = new GeneralEvaluatorTask(this);
		}
		evaluatorTask.execute(inputUri.toString());
		// jumps to onTaskCompleted asynchronously
	}

	/** Called by the {@link EvaluatorTask} when the task is done. **/
	@Override
	public void onTaskCompleted(String output) {
		evaluatingToast.cancel();

		if (output == null) {
			// nothing returned
			makeToast(getString(R.string.couldnt_evaluate)); // couldn't evaluate
			finish();
			return;
		}

		// a URL was returned
		String toastText = getString(R.string.evaluated_as, output);
		makeToast(toastText);
		makeNewUriIntent(output);
		finish();
	}
	
	public void makeNewUriIntent(String uri) {
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(uri));
		startActivity(i);
	}

	private void makeToast(String toastText) {
		conclusionToast = Toast.makeText(UrlEvaluatorActivity.this, toastText, Toast.LENGTH_LONG);
		// get the toast out of the way of the application select menu
		conclusionToast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
		conclusionToast.show();
	}
}