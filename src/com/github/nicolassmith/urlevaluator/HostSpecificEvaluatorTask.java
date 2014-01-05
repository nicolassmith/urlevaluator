package com.github.nicolassmith.urlevaluator;

import java.util.Arrays;

public abstract class HostSpecificEvaluatorTask extends EvaluatorTask {
	
	public HostSpecificEvaluatorTask(EvaluatorTaskCaller passedCaller) {
		super(passedCaller);
	}
	
	public abstract String[] supportedHosts();
	
	public boolean isSupportedHost(String host) {
		return Arrays.asList(this.supportedHosts()).contains(host);
	}
	
}
