package com.github.nicolassmith.urlevaluator;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

public class UrlEvaluatorActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
        
        Uri inputUri = getIntent().getData();
        
        Toast.makeText(UrlEvaluatorActivity.this, inputUri.toString(), Toast.LENGTH_LONG);
        
    }
}