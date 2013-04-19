package com.github.nicolassmith.urlevaluator;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import android.util.Log;

/** This is the most general version of the {@link EvaluatorTask} class. **/
public class GeneralEvaluatorTask extends EvaluatorTask {

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
			// thanks to StackExchange user syb0rg
			con = (HttpURLConnection) (new URL(uriString).openConnection());
			con.setInstanceFollowRedirects(false);
			con.setRequestMethod("HEAD");
			con.connect();
			responseCode = con.getResponseCode();
			location = con.getHeaderField("Location");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (Log.isLoggable(TAG, Log.DEBUG)) {
			Log.d(TAG, "response code = " + responseCode);
			Log.d(TAG, "Location = " + location);
		}

		return location;
	}

}
