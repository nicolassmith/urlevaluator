package com.github.nicolassmith.urlevaluator;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;



public class UrlEvaluatorActivity extends Activity {
    /** Called when the activity is first created. */	
	private static final String TAG = "UrlEvaluatorActivity";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Uri inputUri = getIntent().getData();
        Log.d(TAG, "data = " + inputUri);
        Toast.makeText(UrlEvaluatorActivity.this, inputUri.toString(), Toast.LENGTH_LONG).show();
        
    }
}