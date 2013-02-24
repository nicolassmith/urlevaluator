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
    /** Called when the activity is first created. */	
	private static final String TAG = "UrlEvaluatorActivity";
	public static Resources res;
	private EvaluatorTask evaluator = new GeneralEvaluatorTask(this);
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Uri inputUri = getIntent().getData();
        Log.d(TAG, "data = " + inputUri);
        // Toast.makeText(UrlEvaluatorActivity.this, inputUri.toString(), Toast.LENGTH_LONG).show();
        
        evaluator.execute(inputUri.toString());
        
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

	@Override
	public void onTaskCompleted(String output) {
		
		if (output == null) {
			Toast.makeText(UrlEvaluatorActivity.this, "Couldn't evaluate URL.", Toast.LENGTH_LONG).show();
			finish();
			return;
		}
		
		
		String toastText = "Evaluated as: " + output;
		Toast.makeText(UrlEvaluatorActivity.this, toastText, Toast.LENGTH_LONG).show();
		
		this.updateText(output);
		this.makeNewUriIntent(output);
		
		finish();
	}
}