package com.github.nicolassmith.urlevaluator;

import java.util.ArrayList;
import java.util.List;

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
	private static final boolean ALLOW_TASK_INTERRUPT = true;
	private static final boolean ALLOW_MULTIPLE_REDIRECT_CHOICE = true;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// grab the URL from the intent data
		Uri inputUri = getIntent().getData();

		final EvaluatorTask task = chooseEvaluator(inputUri.getHost(),ALLOW_MULTIPLE_REDIRECT_CHOICE);
		
		evaluatingDialog = ProgressDialog.show(this, null, getString(R.string.evaluating, inputUri), true, true, new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialogInterface) {
				task.cancel(ALLOW_TASK_INTERRUPT);
				finish();
			}
		});

		// send it to an EvaluatorTask
		task.execute(inputUri);
		// jumps to onTaskCompleted asynchronously
	}

	/** Called by the {@link EvaluatorTask} when the task is done. **/
	@Override
	public void onTaskCompleted(Uri output) {
		evaluatingDialog.dismiss();
	
		if (output == null) {
			// nothing returned
			makeToast(getString(R.string.couldnt_evaluate)); // couldn't evaluate
			finish();
			return;
		}
	
		// a URL was returned
		String toastText = getString(R.string.evaluated_as, output);
		try {
			makeNewUriIntent(output);
		} catch (android.content.ActivityNotFoundException e) {
			// URL not recognized by Android
			toastText = getString(R.string.app_not_found, output);
		}
		makeToast(toastText);
		finish();
	}

	@Override
	public void onTaskCanceled() {
		finish();
	}

	public EvaluatorTask chooseEvaluator(String hostName,boolean allowRedirector) {
		// the default choice for the evaluator task
		EvaluatorTask taskChoice = new GeneralEvaluatorTask(this);
		
		// list of host-specific evaluators
		List<HostSpecificEvaluatorTask> hostEvaluators = new ArrayList<HostSpecificEvaluatorTask>();
		
		hostEvaluators.add(new WootcheckEvaluatorTask(this));
		hostEvaluators.add(new TinyUrlRedirectEvaluatorTask(this));

		// we want to make sure we don't give a MRET if we are being called by a MRET
		if (allowRedirector){
			hostEvaluators.add(new MultipleRedirectEvaluatorTask(this));
		}
		
		// look for a match in the available host specific evaluators
		for(HostSpecificEvaluatorTask hostEvaluator : hostEvaluators){
			if(hostEvaluator.isSupportedHost(hostName)){
				// we've found a matching host, set it as our choice and break out of loop
				taskChoice = hostEvaluator;
				break;
			}
		}
		return taskChoice;
	}

	public void makeNewUriIntent(Uri uri) {
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(uri);
		startActivity(i);
	}

	private void makeToast(String toastText) {
		Toast conclusionToast = Toast.makeText(UrlEvaluatorActivity.this, toastText, Toast.LENGTH_LONG);
		// get the toast out of the way of the application select menu
		conclusionToast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
		conclusionToast.show();
	}
}