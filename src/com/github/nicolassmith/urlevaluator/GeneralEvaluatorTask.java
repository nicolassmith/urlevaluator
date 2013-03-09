package com.github.nicolassmith.urlevaluator;

import java.io.IOException;
import java.net.*;
import android.util.Log;

public class GeneralEvaluatorTask extends EvaluatorTask {
	/** this is the most general version of the EvaluatorTask class **/
	
	private static final String TAG = "GeneralEvaluatorTask";

	public GeneralEvaluatorTask(EvaluatorTaskCaller passedCaller) {
		super(passedCaller);
	}

	@Override
	public String evaluate(String uriString) {
		HttpURLConnection con;
		int responseCode = 0;
		String location = null;
		try {
			con = (HttpURLConnection)(new URL( uriString ).openConnection());
			con.setInstanceFollowRedirects( false );
			con.connect();
			responseCode = con.getResponseCode();
			location  = con.getHeaderField( "Location" );
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.d(TAG,"response code = " + responseCode);
		Log.d(TAG,"Location = " + location);

        return location;
	}

}
