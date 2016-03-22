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
package de.uni_muenster.cs.sev.lethal.tests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.uni_muenster.cs.sev.lethal.symbol.common.RankedSymbol;
import de.uni_muenster.cs.sev.lethal.symbol.common.Variable;
import de.uni_muenster.cs.sev.lethal.tree.standard.StdBiTree;

/**
 * This class provides some methods to create lists from very small numbers of elements
 * to reduce the annoying list creation code in the tests.<p>
 * We cannot rely on vararg factory methods, since the use of them leads to warnings
 * caused by (implicit) generic array creations.
 */
public class Util {
    
    /**
     * Creates a list out of one element.
     * @param <T> type of element
     * @param el1 first and only element
     * @return list which contains exactly the given element
     */
	public static <T> List<T> makeList(T el1) {
	ArrayList<T> ret = new ArrayList<T>();
	ret.add(el1);
	return ret;
    }
    
    /**
     * Creates a list out of two elements.
     * @param <T> type of elements
     * @param el1 first element
     * @param el2 second element
     * @return list which contains exactly the two given elements in the specified order
     */
    public static <T> List<T> makeList(T el1, T el2) {
	ArrayList<T> ret = new ArrayList<T>();
	ret.add(el1);
	ret.add(el2);
	return ret;
    }
    
    /**
     * Creates a list out of three elements.
     * @param <T> type of elements
     * @param el1 first element
     * @param el2 second element
     * @param el3 third element
     * @return list which contains exactly the three given elements in the specified order
     */
    public static <T> List<T> makeList(T el1, T el2, T el3) {
	ArrayList<T> ret = new ArrayList<T>();
	ret.add(el1);
	ret.add(el2);
	ret.add(el3);
	return ret;
    }
    
    /**
     * Creates a tree out of a ranked symbol. Just to save typing.
     * @param leaf root symbol of the new tree
     * @return BiTree with the ranked symbol as root and empty subtree list
     */
    public static StdBiTree<RankedSymbol,Variable> leafTree(RankedSymbol leaf) {
    	return new StdBiTree<RankedSymbol,Variable>(leaf, Collections.<StdBiTree<RankedSymbol,Variable>>emptyList());
    }
}
