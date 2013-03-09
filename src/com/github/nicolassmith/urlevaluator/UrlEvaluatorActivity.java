package com.github.nicolassmith.urlevaluator;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;



public class UrlEvaluatorActivity extends Activity implements EvaluatorTaskCaller {
	private static final String COLON_SPACE = ": ";
	private static final String TAG = "UrlEvaluatorActivity";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Uri inputUri = getIntent().getData();
		Log.d(TAG, "data = " + inputUri);

		(new GeneralEvaluatorTask(this)).execute(inputUri.toString());
		// jumps to onTaskCompleted asynchronously
	}

	@Override
	public void onTaskCompleted(String output) {

		if (output == null) {
			// nothing returned
			makeToast(getString(R.string.couldnt_eval)); // couldn't evaluate
			finish();
			return;
		}

		// a URL was returned
		String toastText = getString(R.string.evaluated_as) + COLON_SPACE + output;
		makeToast(toastText);

		updateText(output);
		makeNewUriIntent(output);

		finish();
	}


	public void updateText(String newText) {
		TextView helloText = (TextView) this.findViewById(R.id.helloText);
		helloText.setText(newText);
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