package com.eudycontreras.othello.threading;

/**
 * <H2>Created by</h2> Eudy Contreras
 * <h4> Mozilla Public License 2.0 </h4>
 * Licensed under the Mozilla Public License 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <a href="https://www.mozilla.org/en-US/MPL/2.0/">visit Mozilla Public Lincense Version 2.0</a>
 * <H2>Class description</H2>
 * 
 * @author Eudy Contreras
 */
public interface Manager {

	public void execute();

	public void executeOnSingleThread();
	
	public void setEndScript(ThreadManager.Script script);
	
	public void setTasks(ThreadManager.Task... tasks);
	
	public void addTask(ThreadManager.Task... tasks);

	public void setDelay(Span delay);
	
	public long getDelay();
	
	public boolean isDone();
}
