package com.github.nicolassmith.urlevaluator;

import android.net.Uri;

/** Defines an interface for classes that can call an {@link EvaluatorTask}. **/
public interface EvaluatorTaskCaller {
	public void onTaskCompleted(Uri result);
	public void onTaskCanceled();
	public EvaluatorTask chooseEvaluator(String hostName,boolean allowRedirector); 
}
