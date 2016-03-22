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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import de.uni_muenster.cs.sev.lethal.grammars.generic.GenRTG;
import de.uni_muenster.cs.sev.lethal.languages.RegularTreeLanguage;
import de.uni_muenster.cs.sev.lethal.parser.ftagrammar.FTAGrammarParser;
import de.uni_muenster.cs.sev.lethal.parser.ftagrammar.ParseException;
import de.uni_muenster.cs.sev.lethal.states.NamedState;
import de.uni_muenster.cs.sev.lethal.states.State;
import de.uni_muenster.cs.sev.lethal.symbol.common.RankedSymbol;
import de.uni_muenster.cs.sev.lethal.symbol.standard.StdNamedRankedSymbol;
import de.uni_muenster.cs.sev.lethal.tree.common.Tree;
import de.uni_muenster.cs.sev.lethal.tree.common.TreeOps;
import de.uni_muenster.cs.sev.lethal.tree.standard.StdBiTree;
import de.uni_muenster.cs.sev.lethal.tree.standard.StdTreeCreator;
import de.uni_muenster.cs.sev.lethal.treeautomata.common.FTAOps;
import de.uni_muenster.cs.sev.lethal.treeautomata.easy.EasyFTA;

/**
 * Tests the usage of regular tree grammars.
 * 
 * @author Martin
 */
public class RTGTest {
	
	static GenRTG<RankedSymbol,State> grammar1;

	static EasyFTA grammar2;
	
	/** Cache for used ranked symbols. */
	static Map<String,RankedSymbol> alph = new HashMap<String,RankedSymbol>();
	
	/** Sets up the alphabet.*/
	static {
		alph.put("g", new StdNamedRankedSymbol<String>("g",2));
		alph.put("f", new StdNamedRankedSymbol<String>("f",1));
		alph.put("a", new StdNamedRankedSymbol<String>("a",0));
	}
	
	/** cache for used non-terminal symbols */
	static Map<String,State> nts = new HashMap<String,State>();
	
	static {
		nts.put("S", new NamedState<String>("S"));
		nts.put("A", new NamedState<String>("A"));
		nts.put("B", new NamedState<String>("B"));
	}
	
	/** Cache for used grammar trees. */
	static Map<String,StdBiTree<RankedSymbol,State>> trees = new HashMap<String, StdBiTree<RankedSymbol,State>>(); 
	
	
	static {
		trees.put("a", new StdBiTree<RankedSymbol,State>(alph.get("a"),Collections.<StdBiTree<RankedSymbol,State>>emptyList()));
		trees.put("S", new StdBiTree<RankedSymbol,State>(nts.get("S")));
		trees.put("f(S)", new StdBiTree<RankedSymbol,State>(alph.get("f"), Util.makeList(trees.get("S"))));
	}
	
	/**
	 * Sets up everything once before all tests.
	 * @throws Exception if something goes wrong
	 */
	@BeforeClass
	public static void setUp() throws Exception {
		setUpGrammar1();
		setUpGrammar2();
	}
	
	/**
	 * Sets up the grammar with the rules: <br>
	 * S -> a <br>
	 * S -> f(S) <br>
	 * and the only start symbol S using the tables initialized above.
	 */
	public static void setUpGrammar1() {
		System.out.println("setting up grammar1...");
		ArrayList<State> start = new ArrayList<State>();
		start.add(nts.get("S"));
		grammar1 = new GenRTG<RankedSymbol,State>(start);
		grammar1.addRule(nts.get("S"), trees.get("a"));
		grammar1.addRule(nts.get("S"),trees.get("f(S)"));
	}
	
	/**
	 * Sets up a grammar with some pretty nested rules using the parser.
	 * Since the grammar is constructed out of a string, it is unnecessary,
	 * superfluous and redundant to list the rules here as well. 
	 * It is enough to say that S is the only start symbol.
	 */
	public static void setUpGrammar2() {
		StringBuffer buf = new StringBuffer();
		buf.append("#S\n");
		buf.append("#S::=f(a,#S,b)\n");
		buf.append("#S::=g(c)\n");
		buf.append("#S::=#A\n");
		buf.append("#A::=h(k(#S),#S,l(#B))\n");
		buf.append("#B::=b\n");
		buf.append("#B::=f(#B,#A,#S)");
		try {
			grammar2 = FTAGrammarParser.parseString(buf.toString());
		}
		catch (ParseException e) {
			System.out.println(e);
		}
	}
	
	
	
	
	/**
	 * The first grammar should generate all trees over {f(),a}.
	 * So, it should not be empty and should be equal to the language
	 * new RegularTreeLanguage<RankedSymbol>(Arrays.asList(alph.get("f"), alph.get("a")));
	 */
	@Test
	public void test1() {
		RegularTreeLanguage<RankedSymbol> lang1 = new RegularTreeLanguage<RankedSymbol>(grammar1);
		RegularTreeLanguage<RankedSymbol> lang_ref = new RegularTreeLanguage<RankedSymbol>(Arrays.asList(alph.get("f"), alph.get("a")));
		Assert.assertFalse(lang1.isEmpty());
		Assert.assertTrue(lang1.sameAs(lang_ref));
		listSomeTrees(lang1, 10);
	}
	
	/**
	 * Restricts a given regular tree language to the trees of the specified height and then lists
	 * all these trees. This method terminates in every case, since it is asserted, that the restricted
	 * language is finite. Thus, if it is infinite, the method is aborted, and if it is finite, only
	 * finitely many trees are listed. Each of these trees should be contained in both the specified
	 * language and its restriction - this is also asserted and could lead to abortion
	 * @param lang language to be restricted and then listed partially
	 * @param n maximum height for the restricted language
	 */
	public void listSomeTrees(RegularTreeLanguage<RankedSymbol> lang, int n) {
		Set<Tree<RankedSymbol>> trees = new HashSet<Tree<RankedSymbol>>();
		for (int i=0; i<n; i++) {
			Tree<RankedSymbol> t = FTAOps.constructTreeWithMinHeightFrom(lang.getFTA(), new StdTreeCreator<RankedSymbol>(), i, false);
			if (t==null) {
				Assert.assertTrue(lang.isFinite());
				break;
			}
			System.out.println("height >="+i+"... ");
			Assert.assertTrue(TreeOps.getHeight(t)>=i);
			Assert.assertTrue(lang.contains(t));
			trees.add(t);
		}
	}
	
	
	
	/**
	 * The second test consists in listing the trees not higher than a certain height, which
	 * are contained in the language generated by the second grammar.
	 * @see RTGTest#setUpGrammar2
	 */
	@Test
	public void test2() {
		long time1 = System.currentTimeMillis();
		RegularTreeLanguage<RankedSymbol> lang2 = new RegularTreeLanguage<RankedSymbol>(grammar2);
		System.out.println(lang2);
		listSomeTrees(lang2, 30);
		long time2 = System.currentTimeMillis();
		System.out.println(time2-time1);
	}
	
}
