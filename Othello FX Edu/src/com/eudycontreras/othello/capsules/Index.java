package com.eudycontreras.othello.capsules;

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
public class Index{

	private final int row;
	private final int col;

	public Index(int row, int col){
		this.row = row;
		this.col = col;
	}

	public final int getRow() {
		return row;
	}

	public final int getCol() {
		return col;
	}

	@Override
	public String toString(){
		return ""+row+","+col;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + col;
		result = prime * result + row;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		Index other = (Index) obj;
		if (col != other.col)
			return false;
		if (row != other.row)
			return false;
		return true;
	}

	
}