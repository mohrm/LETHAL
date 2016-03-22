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


import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.uni_muenster.cs.sev.lethal.parser.tree.TreeParser;
import de.uni_muenster.cs.sev.lethal.states.NamedState;
import de.uni_muenster.cs.sev.lethal.states.State;
import de.uni_muenster.cs.sev.lethal.symbol.common.RankedSymbol;
import de.uni_muenster.cs.sev.lethal.symbol.common.Variable;
import de.uni_muenster.cs.sev.lethal.symbol.special.Nil;
import de.uni_muenster.cs.sev.lethal.symbol.standard.NamedVariable;
import de.uni_muenster.cs.sev.lethal.symbol.standard.StdNamedRankedSymbol;
import de.uni_muenster.cs.sev.lethal.symbol.standard.StdVariable;
import de.uni_muenster.cs.sev.lethal.tree.common.Tree;
import de.uni_muenster.cs.sev.lethal.tree.standard.StdBiTree;
import de.uni_muenster.cs.sev.lethal.tree.standard.StdTree;
import de.uni_muenster.cs.sev.lethal.treeautomata.common.FTAProperties;
import de.uni_muenster.cs.sev.lethal.treeautomata.easy.EasyFTA;
import de.uni_muenster.cs.sev.lethal.treeautomata.easy.EasyFTAOps;
import de.uni_muenster.cs.sev.lethal.treeautomata.generic.GenFTA;
import de.uni_muenster.cs.sev.lethal.treetransducer.EasyTT;
import de.uni_muenster.cs.sev.lethal.treetransducer.EasyTTEpsRule;
import de.uni_muenster.cs.sev.lethal.treetransducer.EasyTTRule;
import de.uni_muenster.cs.sev.lethal.treetransducer.GenTT;
import de.uni_muenster.cs.sev.lethal.treetransducer.TTOps;
import de.uni_muenster.cs.sev.lethal.utils.RandomFTAGenerator;

/**
 * Tests the functionality of tree transducers 
 * with some special hand-made tests and some automatically generated tests.<br>
 * In particular, we test with one tree transducer which sorts the subtrees of a tree alphabetically
 * and with one which deforms the trees. 
 * 
 * @author Irene
 */
public class TTTest {


	/**
	 * Count of different randomize test cases.
	 */
	private static final int count = 10;
	
	/** Used self-created symbols. */
	private static Map<String, RankedSymbol>  alphabet = new HashMap<String, RankedSymbol>();

	/** Used self-created states. */
	private static Map<Integer, State> states = new HashMap<Integer, State>();

	/** Used self-created automata. */
	private static Map<String, EasyTT> testTrans = new HashMap<String,EasyTT>();

	/** Used self-created variable trees. */
	private static Map<String, StdBiTree<RankedSymbol,Variable>> variableTrees = new HashMap<String,StdBiTree<RankedSymbol,Variable>>();

	/** Self-created input trees. */
	private static Map<String, Tree<RankedSymbol>> testTreesSort = new HashMap<String,Tree<RankedSymbol>>();

	/** Self-created input trees for another tree transducer.*/
	private static Map<String, Tree<RankedSymbol>> testTreesDeform = new HashMap<String,Tree<RankedSymbol>>();

	/** Test input trees for random tests. */
	private static Tree<RankedSymbol>[] randomTree = new Tree[count];
	
	/** Test random easy tree transducer. */
	private static EasyTT[] randomTT = new EasyTT[count];
	
	/** Test random easy finite tree automata. */
	private static GenFTA<RankedSymbol,State>[] randomFTA = new GenFTA[count/2];
	
	/** Random destination alphabet.*/
	private RankedSymbol[] destAlph;

	
	
	/**
	 * Sets up the alphabet for the sorting test:
	 * {a,b,c,d(),e(),f(,,)}<br>
	 * 
	 * Other start alphabet:  {O,+,*,x,y}<br>
	 * Other destination alphabet:  {+,*,x,y}
	 * 
	 * @throws Exception
	 */
	static void setUpAlphabet() throws Exception {
		//first test case
		alphabet.put("a", new  StdNamedRankedSymbol<String>("a", 0));
		alphabet.put("b", new  StdNamedRankedSymbol<String>("b", 0));
		alphabet.put("c", new  StdNamedRankedSymbol<String>("c", 0));
		alphabet.put("d", new  StdNamedRankedSymbol<String>("d", 1));
		alphabet.put("e", new  StdNamedRankedSymbol<String>("e", 2));
		alphabet.put("f", new  StdNamedRankedSymbol<String>("f", 3));

		//second test case
		alphabet.put("op",new StdNamedRankedSymbol<String>("0", 3));
		alphabet.put("plus_1", new StdNamedRankedSymbol<String>("+",0));
		alphabet.put("mult_1", new StdNamedRankedSymbol<String>("*", 0));
		alphabet.put("plus_2", new StdNamedRankedSymbol<String>("+", 2));
		alphabet.put("mult_2", new StdNamedRankedSymbol<String>("*", 2));
		alphabet.put("x", new StdNamedRankedSymbol<String>("x", 0));
		alphabet.put("y", new StdNamedRankedSymbol<String>("y", 0));
	}

	/**
	 * Set up the states used in the tree transducer for sorting.
	 */
	static void setUpStates() {
		states.put(1, new NamedState<String>("qa"));
		states.put(2, new NamedState<String>("qb"));
		states.put(3, new NamedState<String>("qc"));
		states.put(4, new NamedState<String>("qd"));
		states.put(5, new NamedState<String>("qe"));
		states.put(6, new NamedState<String>("qf"));
	}

	/**
	 * Sets up all used variable trees.
	 * @throws Exception
	 */
	static void setUpVariableTrees() throws Exception {
		StdVariable var0 = new StdVariable(0);
		StdVariable var1 = new StdVariable(1);
		StdVariable var2 = new StdVariable(2);
		variableTrees.put("tree_a", Util.leafTree(alphabet.get("a")));
		variableTrees.put("tree_b", Util.leafTree(alphabet.get("b")));
		variableTrees.put("tree_c", Util.leafTree(alphabet.get("c")));
		variableTrees.put("tree_d", new StdBiTree<RankedSymbol,Variable>(alphabet.get("d"), Util.makeList(new StdBiTree<RankedSymbol,Variable>(new StdVariable(0)))));


		variableTrees.put("tree_e_0_1",
				new StdBiTree<RankedSymbol,Variable>(alphabet.get("e"), 
						Util.makeList(new StdBiTree<RankedSymbol,Variable>(var0),
								new StdBiTree<RankedSymbol,Variable>(var1))));
		variableTrees.put("tree_e_1_0",
				new StdBiTree<RankedSymbol,Variable>(alphabet.get("e"), 
						Util.makeList(new StdBiTree<RankedSymbol,Variable>(var1), 
								new StdBiTree<RankedSymbol,Variable>(var0))));
		variableTrees.put("tree_f_0_1_2",
				new StdBiTree<RankedSymbol,Variable>(alphabet.get("f"), 
						Util.makeList(new StdBiTree<RankedSymbol,Variable>(var0),
								new StdBiTree<RankedSymbol,Variable>(var1),
								new StdBiTree<RankedSymbol,Variable>(var2))));
		variableTrees.put("tree_f_0_2_1",
				new StdBiTree<RankedSymbol,Variable>(alphabet.get("f"), 
						Util.makeList(new StdBiTree<RankedSymbol,Variable>(var0),
								new StdBiTree<RankedSymbol,Variable>(var2),
								new StdBiTree<RankedSymbol,Variable>(var1))));
		variableTrees.put("tree_f_1_0_2",
				new StdBiTree<RankedSymbol,Variable>(alphabet.get("f"), 
						Util.makeList(new StdBiTree<RankedSymbol,Variable>(var1),
								new StdBiTree<RankedSymbol,Variable>(var0),
								new StdBiTree<RankedSymbol,Variable>(var2))));
		variableTrees.put("tree_f_1_2_0",
				new StdBiTree<RankedSymbol,Variable>(alphabet.get("f"), 
						Util.makeList(new StdBiTree<RankedSymbol,Variable>(var1),
								new StdBiTree<RankedSymbol,Variable>(var2),
								new StdBiTree<RankedSymbol,Variable>(var0))));
		variableTrees.put("tree_f_2_0_1",
				new StdBiTree<RankedSymbol,Variable>(alphabet.get("f"), 
						Util.makeList(new StdBiTree<RankedSymbol,Variable>(var2),
								new StdBiTree<RankedSymbol,Variable>(var0),
								new StdBiTree<RankedSymbol,Variable>(var1))));
		variableTrees.put("tree_f_2_1_0",
				new StdBiTree<RankedSymbol,Variable>(alphabet.get("f"), 
						Util.makeList(new StdBiTree<RankedSymbol,Variable>(var2),
								new StdBiTree<RankedSymbol,Variable>(var1),
								new StdBiTree<RankedSymbol,Variable>(var0))));
	}

	/**
	 * Sets up all used input trees.
	 * @throws Exception is never thrown
	 */
	static void setUpInputTrees() throws Exception {
		testTreesSort.put("t_a", TreeParser.parseString("a"));
		testTreesSort.put("t_b", TreeParser.parseString("b"));
		testTreesSort.put("t_c", TreeParser.parseString("c"));

		testTreesSort.put("unsorted_1", TreeParser.parseString("e(f(b,c,b),a)"));
		testTreesSort.put("unsorted_3", TreeParser.parseString("f(b,c,b)"));
		testTreesSort.put("sorted_1", TreeParser.parseString("e(a,f(b,b,c))"));
		testTreesSort.put("unsorted_fbcb", TreeParser.parseString("f(b,c,b)"));
		testTreesSort.put("unsorted_2", TreeParser.parseString("f(c,e(a,b),d(a))"));
		testTreesDeform.put("nil", new StdTree<RankedSymbol>(Nil.getNil(), Collections.<Tree<RankedSymbol>>emptyList()));


		//set up test tree
		testTreesDeform.put("tree_x", new StdTree<RankedSymbol>(alphabet.get("x")));
		testTreesDeform.put("tree_y", new StdTree<RankedSymbol>(alphabet.get("y")));
		testTreesDeform.put("tree_plus", new StdTree<RankedSymbol>(alphabet.get("plus_1")));
		testTreesDeform.put("plus_x_y", new StdTree<RankedSymbol>(alphabet.get("op"),
				Util.makeList(testTreesDeform.get("tree_x"), testTreesDeform.get("tree_plus"), testTreesDeform.get("tree_y"))));
		testTreesDeform.put("plus2_x_y", new StdTree<RankedSymbol>(alphabet.get("plus_2"), 
				Util.makeList(testTreesDeform.get("tree_x"), testTreesDeform.get("tree_y"))));


	}

	/**


    /**
	 * Sets up tree transducer tt with rules:
	 * <ul>
	 * <li>a	  -> q_a,a</li>
	 * <li>b	  -> q_b,b</li>
	 * <li>c	  -> q_c,c</li>
	 * <li>d(q_i)	  -> q_d,d(x_0)</li>
	 * <li>e(q_i,q_j) -> q_e,e(q_i,q_j) if i<=j 
	 * 		  -> q_e,e(q_j,q_i) otherwise </li>
	 * <li>f : like e ...</li>
	 * </ul>
	 * This tree transducer sorts the subtrees by alphabetical order.
	 * 
	 * @throws java.lang.Exception is hopefully never thrown
	 */
	static void setUpTtSorting() throws Exception{
		Set<EasyTTRule> rules = new HashSet<EasyTTRule>(); 
		rules.add(new EasyTTRule(alphabet.get("a"),states.get(1),variableTrees.get("tree_a")));	
		rules.add(new EasyTTRule(alphabet.get("b"),states.get(2),variableTrees.get("tree_b")));
		rules.add(new EasyTTRule(alphabet.get("c"),states.get(3),variableTrees.get("tree_c")));
		for (int i = 1; i<= 6; i++){
			rules.add(new EasyTTRule(alphabet.get("d"),states.get(4),variableTrees.get("tree_d"),states.get(i)));
		}
		// e-rules, case i <= j : e(q_i,q_j) -> q_e,e(q_i,q_j)
		for (int i = 1; i <= 6; i++){
			for (int j = i; j <= 6 ; j++) {
				rules.add(new EasyTTRule(alphabet.get("e"),states.get(5),variableTrees.get("tree_e_0_1"),states.get(i), states.get(j)));		
			}
		}
		// e-rules, case i > j : e(q_i,q_j) -> q_e,e(q_j,q_i)
		for (int i = 1; i <= 6; i++){
			for (int j = 1; j < i ; j++) {
				rules.add(new EasyTTRule(alphabet.get("e"),states.get(5),variableTrees.get("tree_e_1_0"),states.get(i), states.get(j)));		
			}
		}
		// n = 6
		// f-rules, case i <= j & j <= k ~~> i <= j <= n, j <= k <= n  
		for (int i = 1; i <= 6; i++){
			for (int j = i; j <= 6; j++){
				for (int k = j; k <= 6; k++){
					rules.add(new EasyTTRule(alphabet.get("f"),states.get(6),variableTrees.get("tree_f_0_1_2"),states.get(i), states.get(j), states.get(k)));
				}
			}
		}
		// f-rules, case i <= k & k < j ~~> i <= j <= n, i <= k < j
		for (int i = 1; i <= 6; i++){
			for (int j = i; j <= 6; j++){
				for (int k = i; k < j; k++){
					rules.add(new EasyTTRule(alphabet.get("f"),states.get(6),variableTrees.get("tree_f_0_2_1"),states.get(i), states.get(j), states.get(k)));
				}
			}
		}
		// f-rules, case j < i & i <= k ~~> 1 <= j < i, i <= k <= n	
		for (int i = 1; i <= 6; i++){
			for (int j = 1; j < i; j++){
				for (int k = i; k <= 6; k++){
					rules.add(new EasyTTRule(alphabet.get("f"),states.get(6),variableTrees.get("tree_f_1_0_2"),states.get(i), states.get(j), states.get(k)));
				}
			}
		}
		// f-rules, case j <= k & k < i ~~> 1 <= j <= n, j <= k < i
		for (int i = 1; i <= 6; i++){
			for (int j = 1; j <= 6; j++){
				for (int k = j; k < i; k++){
					rules.add(new EasyTTRule(alphabet.get("f"),states.get(6),variableTrees.get("tree_f_1_2_0"),states.get(i), states.get(j), states.get(k)));
				}
			}
		}
		// f-rules, case k <= i & i <= j ~~> i <= j <= n, 1 <= k <= i
		for (int i = 1; i <= 6; i++){
			for (int j = i; j <= 6; j++){
				for (int k = 1; k <= i; k++){
					rules.add(new EasyTTRule(alphabet.get("f"),states.get(6),variableTrees.get("tree_f_2_0_1"),states.get(i), states.get(j), states.get(k)));
				}
			}
		}
		// f-rules, case k <= j & j <= i ~~> 1 <= j <= i, 1 <= k <= j
		for (int i = 1; i <= 6; i++){
			for (int j = 1; j <= i; j++){
				for (int k = 1; k <= j; k++){
					rules.add(new EasyTTRule(alphabet.get("f"),states.get(6),variableTrees.get("tree_f_2_1_0"),states.get(i), states.get(j), states.get(k)));
				}
			}
		}

		Set<State> usedStates = new HashSet<State>();
		for (int nr = 1; nr<=6; nr++) {
			usedStates.add(states.get(nr));
		}

		testTrans.put("tt", new EasyTT(usedStates,rules, new HashSet<EasyTTEpsRule>()));
	}

	/**
	 * Sets up a tree transducer tt_deformer which calculates the term 
	 * which is represented by the tree:<br>
	 * <ul>
	 * <li> x        -> (q,x)</li>
	 * <li> y        -> (q,y)</li>
	 * <li> plus     -> (p,nil)</li>
	 * <li> mult     -> (m,nil)</li>
	 * <li> O(q,p,q) -> (q,+(x_0,x_2)</li>
	 * <li> O(q,m,q) -> (q,*(x_0,x_2)</li>
	 * </ul>
	 * @throws Exception exception is (hopefully) never thrown
	 */
	static void setUpTtDeformer() throws Exception{
		//set up alphabets
		/*start alphabet {O,+,*,x,y}*/
		HashSet<RankedSymbol> alphabet_start = new HashSet<RankedSymbol>();
		alphabet_start.add(alphabet.get("op"));
		alphabet_start.add(alphabet.get("plus_1"));
		alphabet_start.add(alphabet.get("mult_1"));
		alphabet_start.add(alphabet.get("x"));
		alphabet_start.add(alphabet.get("y"));
		/*destination alphabet: {+,*,x,y} */
		HashSet<RankedSymbol> alphabet_dest = new HashSet<RankedSymbol>();
		alphabet_dest.add(alphabet.get("x"));
		alphabet_dest.add(alphabet.get("y"));
		alphabet_dest.add(alphabet.get("mult_2"));
		alphabet_dest.add(alphabet.get("plus_2"));

		//set up tree transducer
		//create states
		State q = new NamedState<String>("q");
		State p = new NamedState<String>("p");
		State m = new NamedState<String>("m");
		HashSet<State> states = new HashSet<State>();
		states.add(p); states.add(q); states.add(m);
		HashSet<State> finalStates = new HashSet<State>();
		finalStates.add(q);

		//create rules
		/* x        -> (q,x)
		 * y        -> (q,y)
		 * plus     -> (p,nil)
		 * mult     -> (m,nil)
		 * O(q,p,q) -> (q,+(x_0,x_2))
		 * O(q,m,q) -> (q,*(x_0,x_2))
		 */
		HashSet<EasyTTRule> ruleSet = 	new HashSet<EasyTTRule>();
		ruleSet.add(new EasyTTRule(	alphabet.get("x"),new LinkedList<State>(),q, Util.leafTree(alphabet.get("x"))));
		ruleSet.add(new EasyTTRule(	alphabet.get("y"),new LinkedList<State>(),q,Util.leafTree(alphabet.get("y"))));
		ruleSet.add(new EasyTTRule(	alphabet.get("plus_1"),new LinkedList<State>(),p,Util.leafTree(Nil.getNil())));
		ruleSet.add(new EasyTTRule(	alphabet.get("mult_1"),new LinkedList<State>(),m,Util.leafTree(Nil.getNil())));
		LinkedList<State> list = new LinkedList<State>();
		list.add(q); list.add(p); list.add(q);
		StdBiTree<RankedSymbol,Variable> var0 = new StdBiTree<RankedSymbol,Variable>(new NamedVariable<String>("x0",0));
		StdBiTree<RankedSymbol,Variable> var2 = new StdBiTree<RankedSymbol,Variable>(new NamedVariable<String>("x2",2));
		ruleSet.add(new EasyTTRule(alphabet.get("op"),list,q,
				new StdBiTree<RankedSymbol,Variable>(alphabet.get("plus_2"),Util.makeList(var0,var2))));
		LinkedList<State> list1 = new LinkedList<State>();
		list1.clear(); list1.add(q); list1.add(m); list1.add(q);
		ruleSet.add(new EasyTTRule(alphabet.get("op"),list1,q,
				new StdBiTree<RankedSymbol,Variable>(alphabet.get("mult_2"),Util.makeList(var0,var2))));

		//	TTRuleSet<RankedSymbol,RankedSymbol,State> rar = new TTRuleSet<RankedSymbol,RankedSymbol,State>(ruleSet);
		//	System.out.println(rar);
		testTrans.put("tt_deformer", new EasyTT(finalStates, alphabet_start, alphabet_dest, ruleSet));
	}

	/**
	 * Sets up an empty tree transducer and one accepting the empty language.
	 */
	static void setUpEmptyTT(){
		testTrans.put("empty", new EasyTT(new HashSet<State>(),new HashSet<RankedSymbol>(),
				new HashSet<RankedSymbol>(),new HashSet<EasyTTRule>()));
		State q = new NamedState<String>("q");
		//some rules
		HashSet<EasyTTRule> ruleSet = new HashSet<EasyTTRule>();
		HashSet<EasyTTEpsRule> epsSet = new HashSet<EasyTTEpsRule>();

		ruleSet.add(new EasyTTRule(alphabet.get("x"),new LinkedList<State>(),q, Util.leafTree(alphabet.get("x"))));
		epsSet.add(new EasyTTEpsRule(q,q,Util.leafTree(alphabet.get("x"))));

		testTrans.put("pseudo-empty", new EasyTT(new HashSet<State>(),alphabet.values(),
				alphabet.values(),ruleSet,epsSet));

	}
	
	/**
	 * Sets up {@link TTTest#count} many random generated test cases.
	 */
	void setUpRandom(){
		RandomTestCases generator = new RandomTestCases();
		//int numStates,int numSymbols,int maxArity,int numRules,int numFinal
		
		//generate alphabets
		RandomFTAGenerator ftaGeneratorStart = new RandomFTAGenerator(6,6,2,20,1);
		ftaGeneratorStart.generateAlphabet();
		RandomFTAGenerator ftaGeneratorDest = new RandomFTAGenerator(6,6,2,20,1);
		ftaGeneratorDest.generateAlphabet();
		RankedSymbol[] startAlph = ftaGeneratorStart.getSymbols();
		destAlph = ftaGeneratorDest.getSymbols();
		
		//generate tree transducer and trees
		int numRules = 30; // number of rules per tree transducer
		int numStates = 7; // number of states per tree transducer
		int numFinals = 1; // number of final states
		int deepth = 4; // maximal deepth of variable trees
		for (int i=0; i<count/2;i++){
			randomTT[i] = generator.randomTT(startAlph, destAlph, deepth, true, numStates, numRules, numFinals);
		}
		for (int i=count/2; i<count;i++){
			randomTT[i] = generator.randomTT(startAlph, destAlph, deepth, false, numStates, numRules, numFinals);
		}
		//generate trees
		for (int i=0; i<count;i++){
			randomTree[i] = generator.randomTree(startAlph, 6);
		}
		
		//generate FTA
		for (int i=0; i< count/2; i++){
			EasyFTA easy = ftaGeneratorStart.generateReduced();
			randomFTA[i] = new GenFTA<RankedSymbol,State>(easy.getRules(),easy.getFinalStates());
		}
	}

	/**
	 * Sets up all the needed things.
	 * 
	 * @throws java.lang.Exception if something goes wrong
	 */
	@Before
	public void setUp() throws Exception {
		//set up things for the sorting test
		setUpAlphabet();
		setUpStates();
		setUpVariableTrees();
		setUpInputTrees();
		setUpTtSorting();
		setUpTtDeformer();
		setUpEmptyTT();
		setUpRandom();
	}



	/**
	 * Tries to apply several tree transducers, test method for {@link GenTT#doARun}.<br>
	 * Some defined examples are tested, furthermore some automatically generated 
	 * tree transducers are tried to run on some generated trees.
	 */
	@Test
	public void testDoARun() {

		Set<Tree<RankedSymbol>> ret = testTrans.get("tt").doARun(testTreesSort.get("unsorted_3"));
		//System.out.println(testTreesSort.get("sorted_3"));
		for (int i= 1; i<3; i++){
			testTrans.get("tt").doARun(testTreesSort.get("unsorted_3"));
		}
		try{
		Assert.assertTrue(testTrans.get("tt").doARun(testTreesSort.get("unsorted_3")).contains(
				TreeParser.parseString("f(b,b,c)")));
		}catch(Exception e){}

		Assert.assertTrue((testTrans.get("tt").doARun(testTreesSort.get("unsorted_1"))).
				contains(testTreesSort.get("sorted_1")));

		Assert.assertFalse(ret.contains(testTreesSort.get("unsorted_1")));

		Assert.assertTrue(testTrans.get("tt_deformer").decide(testTreesDeform.get("plus_x_y")));
		Tree<RankedSymbol> plxy =  testTrans.get("tt_deformer").doARun(
				testTreesDeform.get("plus_x_y")).iterator().next();
		Assert.assertTrue(plxy.getSymbol().equals(testTreesDeform.get("plus2_x_y").getSymbol()));
		List<? extends Tree<RankedSymbol>> subtrees1 = plxy.getSubTrees();
		List<? extends Tree<RankedSymbol>> subTrees2 = testTreesDeform.get("plus2_x_y").getSubTrees();
		Assert.assertEquals(subtrees1.get(0).getSymbol(),subTrees2.get(0).getSymbol());
		Assert.assertEquals(subtrees1.get(0),subTrees2.get(0));
		Assert.assertEquals(plxy,testTreesDeform.get("plus2_x_y"));
		
		//random cases
		EasyFTA all = EasyFTAOps.computeAlphabetFTA(Arrays.asList(destAlph));
		for (int i=0; i< count; i++){
			for (int j= 0; j<count; j++){
				//System.out.println("Testing tt "+i + " with tree " +j);
				Collection<Tree<RankedSymbol>> col= randomTT[i].doARun(randomTree[j]);
				for (Tree<RankedSymbol> tree: col){
					Assert.assertTrue(randomTT[i] + " \n angewandt auf " + randomTree[j],
						all.decide(tree));
				}
			}
		}

	}




	/** 
	 * A test for the decide-method of a tree transducer. <br>
	 * It is checked whether the method based on doARun gives the same result 
	 * as the method in {@link FTAProperties}. 
	 * Furthermore, some constructed examples are checked.
	 */
	@Test
	public void testDecide(){
		//	System.out.println(testTrans.get("tt"));
		Assert.assertTrue(testTrans.get("tt").decide(testTreesSort.get("t_a")));

		//deformer tests
		//	System.out.println(testTrans.get("tt_deformer"));

		Assert.assertTrue(testTrans.get("tt_deformer").decide(testTreesDeform.get("tree_x")));
		Assert.assertTrue(testTrans.get("tt_deformer").decide(testTreesDeform.get("tree_y")));
		Assert.assertFalse(testTrans.get("tt_deformer").decide(testTreesDeform.get("tree_plus")));

		// tests with empty transducers
		for (String t: testTreesDeform.keySet()){
			Assert.assertFalse(testTrans.get("empty").decide(testTreesDeform.get(t)));
			Assert.assertFalse(t,testTrans.get("pseudo-empty").decide(testTreesDeform.get(t)));
			// check if fta properties and the method in tt supply the same
			for (String s: testTrans.keySet()){
				Assert.assertTrue(s +" angewandt auf " +t,testTrans.get(s).decide(testTreesDeform.get(t))
						== FTAProperties.decide(testTrans.get(s),testTreesDeform.get(t)));
			}
		}

		// tests with empty transducers
		for (String t: testTreesSort.keySet()){
			Assert.assertFalse(testTrans.get("empty").decide(testTreesSort.get(t)));
			Assert.assertFalse(t,testTrans.get("pseudo-empty").decide(testTreesSort.get(t)));
			// check if fta properties and the method in tt supply the same
			for (String s: testTrans.keySet()){
				Assert.assertTrue(s +" angewandt auf " +t,testTrans.get(s).decide(testTreesSort.get(t))
						== FTAProperties.decide(testTrans.get(s),testTreesSort.get(t)));
			}
			Assert.assertTrue("All trees are accepted by the sorter, not "+testTreesSort.get(t),
					testTrans.get("tt").decide(testTreesSort.get(t)));
		}
		
		for (int i=0; i<count; i++){
			for (int j= 0; j<count;j++){
				Assert.assertTrue(randomTT[i].decide(randomTree[j]) == FTAProperties.decide(randomTT[i],randomTree[j]));
			}
		}

	}

	/**
	 * Checks complete and deterministic-checks in {@link FTAProperties} used 
	 * with tree transducers.<br>
	 * Checks linearity of automatically generated tree transducers.
	 */
	@Test
	public void testCheckProperties(){
		EasyTT sort = testTrans.get("tt");
		
		// the sorting tree transducer is complete
		Assert.assertTrue(FTAProperties.checkComplete(testTrans.get("tt").getFTAPart()));
		Assert.assertTrue("The sorting tree transducer is complete.",
				FTAProperties.checkComplete(testTrans.get("tt")));
		Assert.assertTrue("The sorting tree transducer is detmerinistic.",
				FTAProperties.checkDeterministic(testTrans.get("tt")));
		Assert.assertTrue("The deformer tree transducer is detmerinistic.",
				FTAProperties.checkDeterministic(testTrans.get("tt_deformer")));
		Assert.assertFalse("The deformer tree transducer is not complete.",
				FTAProperties.checkComplete(testTrans.get("tt_deformer")));
		
		//random test cases, check linearity
		for (int i=0; i<count/2;i++){
			Assert.assertTrue("Tree transducer is not linear: \n"+randomTT[i],randomTT[i].isLinear());
		}
		
	}
	
	
	/**
	 * Checks the method {@link TTOps#union} with automatically generated tree transducers.<br>
	 * Test method: If one tree is accepted by both tree transducers, the union tree transducer
	 * must accept the tree, too. If one tree is generated by one tree transducer, the union
	 * tree transducer must generate it, too. The union of the finite tree automata parts
	 * must be accept the same language as the union of the tree transducers. 
	 */
	@Test
	public void testUnion(){
		for (int i=0; i<count;i++){
			for (int j=0;j<count;j++){
				System.out.println("Testing union tree transducer: "+i + " union " +j);
				EasyTT union = TTOps.convertToEasyTT(TTOps.union(randomTT[i],randomTT[j]));
				EasyFTA unionFTA = EasyFTAOps.union(randomTT[i].getFTAPart(), randomTT[j].getFTAPart());
				
				for (int k=0; k<count;k++){
					if ((FTAProperties.decide(randomTT[i], randomTree[k])) ||
							(FTAProperties.decide(randomTT[j], randomTree[k]))){
						Assert.assertTrue(unionFTA.decide(randomTree[k]));	
						Assert.assertTrue("Problem to union tree transducer \n"+ randomTT[i] + "\n with tree transducer \n"
								+ randomTT[j],FTAProperties.decide(union, randomTree[k]));
					}
					for (Tree<RankedSymbol> tree: randomTT[i].doARun(randomTree[k])){
						Assert.assertTrue(union.doARun(randomTree[k]).contains(tree));
					}
					Assert.assertTrue(union.doARun(randomTree[k]).containsAll(
							randomTT[j].doARun(randomTree[k])));
				}
				Assert.assertTrue(FTAProperties.sameLanguage(union, unionFTA));
			}
		}
	}
	
	/**
	 * Tests the method {@link TTOps#runOnAutomaton(GenTT, GenFTA)}.<br>
	 * Therefore apply random generated linear tree transducers on finite tree automata.
	 * Check whether an tree, which is accepted by both, tree transducer and automaton
	 * is accepted by the gained finite tree automaton, too.
	 */
	@Test
	public void testRunOnAutomaton(){
		for (int i=0; i<count/2;i++){
			for (int j=0;j<count/2;j++){
				System.out.println("Testing tt "+i + " with fta " +j);
				GenFTA<RankedSymbol, State> run = TTOps.runOnAutomaton(randomTT[i], randomFTA[j]);	
				for (int k=0; k<count; k++){
					if (randomFTA[j].decide(randomTree[k]) && randomTT[i].decide(randomTree[k])){
						for (Tree<RankedSymbol> t: randomTT[i].doARun(randomTree[k]))
							Assert.assertTrue(run.decide(t));
					}
				}
			}
		}
		
	}



}
