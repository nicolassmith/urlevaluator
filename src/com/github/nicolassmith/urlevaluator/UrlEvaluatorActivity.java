package com.github.nicolassmith.urlevaluator;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;



public class UrlEvaluatorActivity extends Activity implements EvaluatorTaskCaller {
	/** This is the Activity that is called when a short URL intent is thrown by the Android OS **/
	private static final String TAG = "UrlEvaluatorActivity";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		// grab the URL from the intent data
		Uri inputUri = getIntent().getData();
		Log.d(TAG, "data = " + inputUri);

		// send it to an EvaluatorTask
		(new GeneralEvaluatorTask(this)).execute(inputUri.toString());
		// jumps to onTaskCompleted asynchronously
	}

	@Override
	public void onTaskCompleted(String output) {
		/** called by the EvaluatorTask when the Task is done **/
		if (output == null) {
			// nothing returned
			makeToast(getString(R.string.couldnt_evaluate)); // couldn't evaluate
			finish();
			return;
		}

		// a URL was returned
		String toastText = getString(R.string.evaluated_as, output);
		makeToast(toastText);

		updateText(output);
		makeNewUriIntent(output);

		finish();
	}

	public void updateText(String newText) {
		TextView evaluatingUrl = (TextView) this.findViewById(R.id.evaluating_url);
		evaluatingUrl.setText(newText);
	}
	
	public void makeNewUriIntent(String uri){
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(uri));
		startActivity(i);
	}

	private void makeToast(String toastText) {
		Toast.makeText(UrlEvaluatorActivity.this, toastText, Toast.LENGTH_LONG).show();
	}
}