/*
 * Copyright 2009 Dorothea Jansen <d.jansen@uni-muenster.de>, Martin Mohr <mohrfrosch@uni-muenster.de>, Irene Thesing <i_thes01@uni-muenster.de>, Anton Reis <antonreis@gmx.de>, Maria Schatz <m_scha17@uni-muenster.de>, Philipp Claves <philipp.claves@uni-muenster.de>, Sezar Jarrous <sezar.jarrous@gmail.com>
 *
 * This file is part of LETHAL.
 *
 * LETHAL is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LETHAL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with LETHAL.  If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * 
 */
package de.uni_muenster.cs.sev.lethal.testManual;

import de.uni_muenster.cs.sev.lethal.states.State;

/**
 * Example for an extension of the states.
 * 
 * @see ExampleOps
 * @author Irene
 */
public class SpecialState implements State {
	/**Number of the state*/  
	protected int nr;
	/**Architecture of the state*/
	protected String arch;


	/**
	 * Constructs a new state with the next number of the StateCounter.
	 * @see StateCounter 
	 */
	public SpecialState(){ 
		nr = StateCounter.getNr();
		arch = Integer.toString(nr); 
	}

	/**
	 * Constructs a new state with the given architecture.
	 * @param str architecture of the new state
	 * @see StateCounter
	 */
	public SpecialState(String str){ 
		nr = StateCounter.getNr();
		arch = str; 
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString(){
		return arch;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + nr;
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final SpecialState other = (SpecialState) obj;
		if (nr != other.nr)
			return false;
		return true;
	}


}
