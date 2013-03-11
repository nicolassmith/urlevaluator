package com.github.nicolassmith.urlevaluator;

/** Defines an interface for classes that can call an {@link EvaluatorTask}. **/
public interface EvaluatorTaskCaller {
	public void onTaskCompleted(String output);
}
