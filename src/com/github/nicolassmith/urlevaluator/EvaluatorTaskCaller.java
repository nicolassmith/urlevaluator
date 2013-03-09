package com.github.nicolassmith.urlevaluator;

public interface EvaluatorTaskCaller {
	/** defines an interface for Classes that can call an EvaluatorTask **/
	public void onTaskCompleted(String output);
}
