package com.eudycontreras.othello.threading;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 <p>
 <h4> Mozilla Public License 2.0 </h4>
 Licensed under the Mozilla Public License 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 <a href="https://www.mozilla.org/en-US/MPL/2.0/">visit Mozilla Public Lincense Version 2.0</a>
 </p>
 <p>
 <H2>Created by</h2> Eudy Contreras
 </p>
 <H2>Class description</H2>
 * @author Eudy Contreras
 */
public class ThreadManager implements Manager {

	public static final int LOW = Thread.MIN_PRIORITY;
	public static final int HIGH = Thread.MAX_PRIORITY;
	public static final int NORMAL = Thread.NORM_PRIORITY;

	public static final int DEFAULT_THROTTLE_TIME = 5;
	public static final int DEFAULT_TASK_THRESHOLD = 4;

	public static final ThreadManager INSTANCE = new ThreadManager();

	private static TaskQueue taskQueue = null;
	
	private Span delay = TimeSpan.ZERO;
	
	private Script endScript = null;

	private List<Task> tasks = new LinkedList<>();

	private AtomicInteger counter = null;
	
	private int priority = Thread.NORM_PRIORITY;

	public static TaskQueue getTaskQueueInstance(int maxTaskThreshold){
		if(taskQueue == null){
			taskQueue = new ThreadManager.TaskQueue(maxTaskThreshold);
		}
		return taskQueue;
	}

	public ThreadManager(Task... tasks) {
		this(NORMAL, tasks);
	}
	
	public ThreadManager(int priority, Task... tasks) {
		this(priority, null, tasks);
	}
	
	public ThreadManager(Script endScript, Task... tasks) {
		this(NORMAL, endScript, tasks);
	}

	public ThreadManager(int priority, Script endScript, Task... tasks) {
		this.counter = new AtomicInteger(0);
		this.tasks = Arrays.asList(tasks);
		this.endScript = endScript;
		this.priority = priority;
	}

	@Override
	public void setEndScript(Script script) {
		this.endScript = script;
	}
	
	@Override
	public void setTasks(Task...tasks){
		this.tasks = Arrays.asList(tasks);
	}
	
	@Override
	public void addTask(Task...tasks){
		this.tasks.addAll(Arrays.asList(tasks));
	}
	
	@Override
	public boolean isDone() {
		return counter.get() == tasks.size();
	}

	@Override
	public void setDelay(Span span){
		this.delay = span;
	}
	
	@Override
	public long getDelay(){
		if(delay != null){
			return delay.getDuration();
		}
		return 0L;
	}

	@Override
	public void executeOnSingleThread(){
		counter.set(0);

		Worker worker = new WorkerThread(delay, ()->{
			createWorkerThread(tasks.toArray(new Task[tasks.size()]));
		});

		worker.setDaemon(true);
		worker.start();
	}

	@Override
	public void execute() {
		counter.set(0);
		
		Worker worker = new WorkerThread(delay, ()->{
			tasks.parallelStream().forEach(this::createWorkerThread);
		});
		
		worker.setDaemon(true);
		worker.start();
	}
	
	private void createWorkerThread(Task... task){
		Worker worker = new WorkerThread(null,task);
		
		worker.setPriority(priority);
		worker.addCompleteListener((thread)->{
			increaseCounter();
		});
		
		worker.setDaemon(true);
		worker.start();
	}

	private synchronized void increaseCounter(){
		counter.set(counter.get() + 1);	

		checkComplete();
	}
	
	private void checkComplete(){
		if(isDone()){
			if(endScript != null){
				System.out.println("RUNNING END SCRIPT!");
				endScript.onFinish(true);
			}
		}
	}
	
	public static final void pause(long duration){
		try {
			Thread.sleep(duration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static final void pause(Span span){
		try {
			Thread.sleep(span.getDuration());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	public static final Manager performTask(Task... tasks) {
		return performTask(NORMAL, tasks);
	}
	
	public static final Manager performTask(int priority, Task... tasks) {
		return performTask(priority,TimeSpan.ZERO, tasks);
	}

	public static final Manager performTask(Script endScript, Task... tasks) {
		return performTask(NORMAL, TimeSpan.ZERO, endScript, tasks);
	}
	
	public static final Manager performTask(int priority, Span delay, Task... tasks) {
		return performTask(priority, delay, null, tasks);
	}
	
	public static final Manager performTask(Span delay, Script endScript, Task... tasks) {
		return performTask(NORMAL, delay, endScript, tasks);
	}
	
	public static final Manager performTask(int priority, Span delay, Script endScript, Task... tasks) {

		Manager manager = new ThreadManager(priority, endScript, tasks);
		manager.setDelay(delay);
		manager.execute();
		
		return manager;
	}

	public static void execute(Task task) {
		if(task != null){
			Worker worker = ThreadManager.INSTANCE.new WorkerThread(null, task);
			worker.setDaemon(true);
			worker.start();
		}
	}
	
	public static void schedule(Span waitTime, Task task) {
		if(task != null){
			Worker worker = ThreadManager.INSTANCE.new WorkerThread(waitTime, task);
			worker.setDaemon(true);
			worker.start();
		}
	}

	public static void throttle(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static class TaskQueue {

		private Buffer<Task> buffer = new ThreadBuffer<>();

		private TaskQueueWorkThread worker;

		private int maxTaskThreshold = DEFAULT_TASK_THRESHOLD;

		private TaskQueue(int maxTaskThreshold){
			this.maxTaskThreshold = maxTaskThreshold;
		}

		public void start() {
			if(worker==null) {
				worker = new TaskQueueWorkThread();
				worker.start();
			}
		}

		public void stop() {
			if(worker!=null) {
				worker.interrupt();
				worker=null;
			}
		}

		public final boolean isRunning() {
			return worker.isAlive();
		}

		public TaskQueue execute(Task task){
			buffer.add(task);
			return this;
		}

		public TaskQueue execute(Task... tasks){
			for (Task task : tasks){
				buffer.add(task);
			}
			return this;
		}

		private class TaskQueueWorkThread extends Thread {
			public void run() {
				while(worker!=null) {
					throttle(DEFAULT_THROTTLE_TIME);

					try {
						buffer.get().doWork();
					} catch (InterruptedException e) {
						worker=null;
					}
				}
			}
		}
	}


	public class WorkerThread extends Worker{

		private Task[] task;
		private Span delay;
		
		public WorkerThread(Span delay, Task... task){
			this.task = task;
			this.delay = delay;
		}
		
		@Override
		public void runTask() {
			applyDelay();

			if(task != null){
				if(task.length == 1){
					task[0].doWork();
				}else{
					tasks.parallelStream().forEach(Task::doWork);
				}
			}
		}
		
		private void applyDelay(){
			if(delay != null){
				try {
					Thread.sleep(delay.getDuration());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static class ThreadBuffer<T> implements Buffer<T> {
  
		private Queue<T> buffer = new LinkedList<>();
		private int size;

		public ThreadBuffer(){
			this(Integer.MAX_VALUE);
		}

		public ThreadBuffer(int size) {
			this.size = size;
		}

		@Override
		public synchronized void add(T element) {
			buffer.add(element);

			if(size()>size){
				buffer.poll();
			}
			notifyAll();
		}

		@Override
		public synchronized T get() throws InterruptedException {
			while(buffer.isEmpty()) {
				wait();
			}
			return buffer.poll();
		}

		@Override
		public int size() {
			return buffer.size();
		}
	}

	public abstract class Worker extends Thread {

		private List<CompleteListener> listeners = new LinkedList<>();

		public void addCompleteListener(CompleteListener listener) {
			listeners.add(listener);
		}

		public void removeCompleteListener(CompleteListener listener) {
			listeners.remove(listener);
		}
		
		private void notifyListeners(){
			if(listeners.isEmpty())
				return;

			listeners.stream().parallel().forEach(l -> l.complete(this));
		}

		public abstract void runTask();
		
		@Override
		public void run() {
			throttle(DEFAULT_THROTTLE_TIME);
			
			try{
				runTask();
			}finally{
				notifyListeners();
			}
		}

	}

	public interface Script{
		void onFinish(boolean success);
	}
	
	public interface Task{
		void doWork();
	}
	
	public interface CompleteListener {
		void complete(Thread thread);
	}

}
