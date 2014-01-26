package com.github.nicolassmith.urlevaluator;

import java.util.List;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.util.Log;

public class MultipleRedirectEvaluatorTask extends HostSpecificEvaluatorTask {

	private static final String URL_EVALUATOR_ACTIVITY_NAME = "com.github.nicolassmith.urlevaluator.UrlEvaluatorActivity";
	private static final boolean ALLOW_MULTIPLE_REDIRECT_CHOICE = false;
	private static final String TAG = "MultipleRedirectEvaluat"; // 23 character limit
	
	public MultipleRedirectEvaluatorTask(EvaluatorTaskCaller passedCaller) {
		super(passedCaller);
	}

	@Override
	public Uri evaluate(Uri inputUri) {
		
		if (debugLogEnabled()) { Log.d(TAG, "Multiple redirect evaluation of " + inputUri.toString()); }
		
		// Has this task been canceled?
		if (this.isCancelled()){
			return null;
		}
		
		if (!isSupportedUrl(inputUri)){
			if (debugLogEnabled()) { Log.d(TAG, "Uri not handled, stop recursion"); }
			return inputUri;
		}
		
		// get the right evaluator for the host
		EvaluatorTask singleEvaluator = caller.chooseEvaluator(inputUri,ALLOW_MULTIPLE_REDIRECT_CHOICE);
		
		// do a single evaluation
		Uri evaluated = singleEvaluator.evaluate(inputUri);

		if (debugLogEnabled()) { Log.d(TAG, "evaluated as: " + evaluated + " now going deeper!"); }
		return this.evaluate(evaluated);
	}

	private boolean debugLogEnabled() {
		//return Log.isLoggable(TAG, Log.DEBUG);
		return true;
	}

	private boolean isSupportedUrl(Uri inputUri) {
		// Use PackageManager to see if UrlEvaluator can handle the URI
		final PackageManager packageManager = caller.getContext().getPackageManager();
		final Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(inputUri);
		List<ResolveInfo> resolveList = packageManager.queryIntentActivities(intent,PackageManager.MATCH_DEFAULT_ONLY);
		boolean UriIsSupported = false;
		for (ResolveInfo resolveInfo : resolveList) {
			if(resolveInfo.activityInfo.name.equals(URL_EVALUATOR_ACTIVITY_NAME)){
				UriIsSupported = true;
				if (debugLogEnabled()) { Log.d(TAG,"confirmed supported uri"); }
			}
		}
		return UriIsSupported;
	}

	@Override
	public String[] supportedHosts() {
		// define the hosts for this evaluator
		return new String[] {"tinyurl.com"};
	}

}
