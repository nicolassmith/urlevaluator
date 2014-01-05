package com.github.nicolassmith.urlevaluator;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.util.Log;

/** This is the simplest version of the {@link EvaluatorTask} class. **/
public class GeneralEvaluatorTask extends EvaluatorTask {

	private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2";
	private static final int HTTP_OK_RESPONSE = 200;
	private static final String TAG = "GeneralEvaluatorTask";

	public GeneralEvaluatorTask(EvaluatorTaskCaller passedCaller) {
		super(passedCaller);
	}

	@Override
	public String evaluate(String uriString) {
		HttpURLConnection con;
		int responseCode = 0;
		String target = null;
		try {
			// thanks to StackOverflow user inno (question 2659000)
			con = (HttpURLConnection) (new URL(uriString).openConnection());
			con.setInstanceFollowRedirects(false);
			con.setRequestMethod("HEAD");
			// This turns off gzip compression, because some servers lie!
			// And this confuses the HttpEngine decoder.
			con.setRequestProperty("Accept-Encoding", "identity");
			// hide the fact that we are a mobile device, so we don't get a redirection to mobile pages
			con.setRequestProperty("User-Agent", USER_AGENT);
			con.connect();
			responseCode = con.getResponseCode();
			if (responseCode == HTTP_OK_RESPONSE){
				// we are not being redirected
				target = uriString;
			} else {
				target = con.getHeaderField("Location");
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (Log.isLoggable(TAG, Log.DEBUG)) {
			Log.d(TAG, "response code = " + responseCode);
			Log.d(TAG, "Location = " + target);
		}

		return target;
	}

}
