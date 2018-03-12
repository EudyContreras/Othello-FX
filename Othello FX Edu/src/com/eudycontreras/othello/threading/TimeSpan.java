package com.eudycontreras.othello.threading;

import java.util.concurrent.TimeUnit;

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
public class TimeSpan {

	public static final Span ZERO = new Zero();
	
	public static final Span hours(int time){
		return new Hour(time);
	}

	public static final Span minutes(int time){
		return new Minute(time);
	}

	public static final Span seconds(int time){
		return new Second(time);
	}

	public static final Span millis(int time){
		return new MilliSecond(time);
	}

	private static class Hour implements Span {

		private long time;

		public Hour(long time) {
			this.time = time;
		}

		@Override
		public long getDuration() {
			return getAbsoluteTime(TimeUnit.HOURS,time);
		}

	}

	private static class Minute implements Span {
		private long time;

		public Minute(long time) {
			this.time = time;
		}

		@Override
		public long getDuration() {
			return getAbsoluteTime(TimeUnit.MINUTES,time);
		}

	}

	private static class Second implements Span {
		private long time;

		public Second(long time) {
			this.time = time;
		}

		@Override
		public long getDuration() {
			return getAbsoluteTime(TimeUnit.SECONDS,time);
		}

	}

	private static class MilliSecond implements Span {
		private long time;

		public MilliSecond(long time) {
			this.time = time;
		}

		@Override
		public long getDuration() {
			return getAbsoluteTime(TimeUnit.MILLISECONDS,time);
		}

	}

	private static class Zero implements Span {
		private long time = 0;

		public Zero() {
		}

		@Override
		public long getDuration() {
			return getAbsoluteTime(TimeUnit.MILLISECONDS,time);
		}

	}
 
	private static final long getAbsoluteTime(TimeUnit unit, long time){
		switch(unit){
			case HOURS:
				return ((time*1000)*60)*60;
			case MINUTES:
				return ((time*1000)*60);
			case SECONDS:
				return (time*1000);
			case MILLISECONDS:
				return time;
			default:
				break;
		}
		return time;
	}
}
