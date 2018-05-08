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
public class ThreadTimer {


	public static TimerControl schedule(final Span delay, final Runnable script){
		TimerControl timerControl = new TimerControl();
		
		ThreadManager.performTask(() -> {
           
            if(!timerControl.isTimerStopped()){
            	waitTime(delay.getDuration());
                
            	if(script != null){
            		script.run();
            	}
            }
        });
		
		return timerControl;
	}
	
	public static TimerControl scheduleAtRate(final Span delay, final Span rate, final Runnable script){
		TimerControl timerControl = new TimerControl();
		
		ThreadManager.performTask(() -> {
			waitTime(delay.getDuration());
			while(!timerControl.isTimerStopped()){
				waitTime(rate.getDuration());
				script.run();
			}
		});
		return timerControl;
	}

	public static TimerControl scheduleAtControlledRate(final Span delay, final Span rate, final Runnable script){
		TimerControl timerControl = new TimerControl();
		
		ThreadManager.performTask(() -> {
			waitTime(delay.getDuration());
			while(!timerControl.isTimerStopped()){
				waitTime(rate.getDuration());

				if(timerControl.isTimerPaused())
				script.run();
			}
		});
		return timerControl;
	}

	public static TimerControl intervalIterate(final int start, final int count, final Span period, final Span delay, final IterateWrapper wrapper){
		TimerControl timerControl = new TimerControl();
		
		ThreadManager.performTask(() -> {
			waitTime(delay.getDuration());
			int counter = start;
			while(counter<=count && timerControl.isTimerStopped()){
				wrapper.onIteration(counter);
				waitTime(period.getDuration());
				counter++;
			}
			wrapper.onComplete();
		});
		return timerControl;
	}


	private static void waitTime(long millis){
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public interface IterateWrapper{
		void onIteration(int index);
		void onComplete();
	}

	public static class TimerControl{
		
		private boolean pauseTimer = true;
		private boolean stopTimer  = false;

		public boolean isTimerPaused() {
			return pauseTimer;
		}

		public void pauseTimer(boolean state) {
			this.pauseTimer = state;
		}

		public boolean isTimerStopped() {
			return stopTimer;
		}

		public void stopTimer(boolean state) {
			this.stopTimer = state;
		}
		
		
	}

}