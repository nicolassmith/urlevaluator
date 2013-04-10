package com.github.nicolassmith.urlevaluator;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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

	private ProgressDialog evaluatingDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// grab the URL from the intent data
		Uri inputUri = getIntent().getData();

		final EvaluatorTask task = new GeneralEvaluatorTask(this);
		evaluatingDialog = ProgressDialog.show(this, null, getString(R.string.evaluating, inputUri), true, true, new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialogInterface) {
				task.cancel(true);
				finish();
			}
		});

		// send it to an EvaluatorTask
		task.execute(inputUri.toString());
		// jumps to onTaskCompleted asynchronously
	}

	/** Called by the {@link EvaluatorTask} when the task is done. **/
	@Override
	public void onTaskCompleted(String output) {
		evaluatingDialog.dismiss();

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
		Toast conclusionToast = Toast.makeText(UrlEvaluatorActivity.this, toastText, Toast.LENGTH_LONG);
		// get the toast out of the way of the application select menu
		conclusionToast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
		conclusionToast.show();
	}
}