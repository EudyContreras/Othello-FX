package com.eudycontreras.othello.tooling;
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
public class FXRepeater{

	public static final IRepeater iterations(int times){
		return new Repetitions(times);
	}
 
	public static void repeat(int times, RepeatWrapper repeater){
		for(int i = 0; i<times; repeater.repeat(i), i++);
	}

	public static void repeat(int start, int end, RepeatWrapper repeater){
		for(int i = start; i<end; repeater.repeat(i), i++);
	}

	public interface RepeatWrapper{
		public void repeat(int index);
	}

	private static class Repetitions implements IRepeater{
		private int times;

		public Repetitions(int times){
			this.times = times;
		}

		@Override
		public int getRepeats() {
			return times;
		}
	}
	
	public interface IRepeater {

		public int getRepeats();

	}
}