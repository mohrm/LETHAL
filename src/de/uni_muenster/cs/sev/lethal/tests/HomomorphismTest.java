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

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.uni_muenster.cs.sev.lethal.hom.EasyHom;
import de.uni_muenster.cs.sev.lethal.hom.GenHom;
import de.uni_muenster.cs.sev.lethal.languages.RegularTreeLanguage;
import de.uni_muenster.cs.sev.lethal.parser.fta.FTAParser;
import de.uni_muenster.cs.sev.lethal.parser.homomorphism.HomomorphismParser;
import de.uni_muenster.cs.sev.lethal.states.NamedState;
import de.uni_muenster.cs.sev.lethal.states.State;
import de.uni_muenster.cs.sev.lethal.symbol.common.RankedSymbol;
import de.uni_muenster.cs.sev.lethal.symbol.standard.StdNamedRankedSymbol;
import de.uni_muenster.cs.sev.lethal.tree.common.Tree;
import de.uni_muenster.cs.sev.lethal.tree.standard.StdTree;
import de.uni_muenster.cs.sev.lethal.treeautomata.common.FTAProperties;
import de.uni_muenster.cs.sev.lethal.treeautomata.easy.EasyFTA;
import de.uni_muenster.cs.sev.lethal.treeautomata.easy.EasyFTARule;
import de.uni_muenster.cs.sev.lethal.treeautomata.generic.GenFTA;
import de.uni_muenster.cs.sev.lethal.treeautomata.generic.GenFTAOps;
import de.uni_muenster.cs.sev.lethal.utils.RandomFTAGenerator;

/**
 * Test cases for homomorphisms. <br>
 * The tests are done with some self-created examples and randomize generated homomorphism, trees and tree automata.
 * 
 * @author Irene
 */
public class HomomorphismTest {


	/**alphabet {and(,), or(,), not(), 1, 0}*/
	private Map<String,RankedSymbol> alphabet1 = new HashMap<String,RankedSymbol>();

	/**alphabet2 {a,b(),c(,)}*/
	private Map<String,RankedSymbol> alphabet2 = new HashMap<String,RankedSymbol>();

	/**Homomorphisms for testing.*/
	private HashMap<String,EasyHom> homs = new HashMap<String,EasyHom>();

	/**Automata for testing.*/
	private HashMap<String,EasyFTA> autom = new HashMap<String,EasyFTA>();

	/**Trees for testing.*/
	private HashMap<String,Tree<RankedSymbol>> trees = new HashMap<String,Tree<RankedSymbol>>();


	private final int count = 10;
	private final int countTree = 20;

	/**Generated alphabet.*/
	private RankedSymbol[] randomStartAlph;

	/**Generated alphabet.*/
	private RankedSymbol[] randomDestAlph;

	/**Homomorphisms for testing.*/
	private EasyHom[] randomHom;

	/**Automata for testing.*/
	private EasyFTA[] randomFTA;

	/**Automata for testing.*/
	private EasyFTA[] randomFTADest;

	/**Trees  for testing*/
	private Tree<RankedSymbol>[] randomTree;

	private GenHom<RankedSymbol, RankedSymbol> h1;
	private EasyFTA ta1;

	private void setUpAlphabet(){
		//setUpAlphabet		
		RankedSymbol t = new StdNamedRankedSymbol<String>("1", 0);
		RankedSymbol f = new StdNamedRankedSymbol<String>("0", 0);
		RankedSymbol not = new StdNamedRankedSymbol<String>("not", 1);
		RankedSymbol and = new StdNamedRankedSymbol<String>("and", 2);
		RankedSymbol or = new StdNamedRankedSymbol<String>("or", 2);

		alphabet1.put("t",t); 
		alphabet1.put("f",f); 
		alphabet1.put("not",not);
		alphabet1.put("or",or); 
		alphabet1.put("and",and);

		//setUpAlphabet2		
		RankedSymbol a = new StdNamedRankedSymbol<String>("a", 0);
		RankedSymbol b = new StdNamedRankedSymbol<String>("b", 1);
		RankedSymbol c = new StdNamedRankedSymbol<String>("c", 2);

		alphabet2.put("a",a); alphabet2.put("b",b); alphabet2.put("c",c); 
	}

	private void setUpTree(){
		//setUpTrees
		Tree<RankedSymbol> tree_0 = new StdTree<RankedSymbol>(alphabet1.get("f"));
		Tree<RankedSymbol> tree_1 = new StdTree<RankedSymbol>(alphabet1.get("t"));
		trees.put("0",tree_0);
		trees.put("1",tree_1);

		trees.put("not_0",new StdTree<RankedSymbol>(alphabet1.get("not"), Util.makeList(tree_0)));

		trees.put("test_a", new StdTree<RankedSymbol>(alphabet1.get("and"), Util.makeList(tree_0, tree_1)));
		trees.put("result_a", new StdTree<RankedSymbol>(alphabet1.get("not"), 
				Util.makeList(new StdTree<RankedSymbol>(alphabet1.get("or"),
						Util.makeList(new StdTree<RankedSymbol>(alphabet1.get("not"), 
								Util.makeList(tree_0)),new StdTree<RankedSymbol>(
										alphabet1.get("not"), Util.makeList(tree_1)))))));
		trees.put("foo_a",new StdTree<RankedSymbol>(alphabet1.get("or"), Util.makeList(tree_0, tree_0)));

		trees.put("test_b",new StdTree<RankedSymbol>(alphabet1.get("or"), Util.makeList(
				new StdTree<RankedSymbol>(alphabet1.get("and"),Util.makeList(tree_1,trees.get("not_0"))),
				new StdTree<RankedSymbol>(alphabet1.get("not"), Util.makeList(tree_1)))));
		Tree<RankedSymbol> help_b = new StdTree<RankedSymbol>(alphabet1.get("not"), 
				Util.makeList(new StdTree<RankedSymbol>(alphabet1.get("or"), 
						Util.makeList(new StdTree<RankedSymbol>(alphabet1.get("not"), 
								Util.makeList(tree_1)),new StdTree<RankedSymbol>(
										alphabet1.get("not"),Util.makeList(trees.get("not_0")))))));

		trees.put("result_b", new StdTree<RankedSymbol>(alphabet1.get("or"), 
				Util.makeList(help_b, new StdTree<RankedSymbol>(alphabet1.get("not"),
						Util.makeList(tree_1)))));

		trees.put("test_d", new StdTree<RankedSymbol>(alphabet2.get("a")));

		trees.put("test_c", new StdTree<RankedSymbol>(alphabet2.get("b"), 
				Util.makeList(new StdTree<RankedSymbol>(alphabet2.get("c"), 
						Util.makeList(trees.get("test_d"), trees.get("test_d"))))));
		Tree<RankedSymbol> tree_c1 = new StdTree<RankedSymbol>(alphabet1.get("not"), 
				Util.makeList(new StdTree<RankedSymbol>(alphabet1.get("or"), 
						Util.makeList(tree_1,tree_1))));	
		trees.put("result_c", new StdTree<RankedSymbol>(alphabet1.get("and"), 
				Util.makeList(tree_c1,tree_c1)));

	}

	private void setUpHom(){
		//setUpHom
		HashMap<RankedSymbol,Tree<RankedSymbol>> h = new HashMap<RankedSymbol,Tree<RankedSymbol>>();
		h.put(alphabet1.get("t"),new StdTree<RankedSymbol>(alphabet1.get("t"), 
				new LinkedList<Tree<RankedSymbol>>()));
		h.put(alphabet1.get("f"),new StdTree<RankedSymbol>(alphabet1.get("f"),
				new LinkedList<Tree<RankedSymbol>>()));

		RankedSymbol var0 = new StdNamedRankedSymbol<String>("x_0", 0);
		RankedSymbol var1 = new StdNamedRankedSymbol<String>("x_1", 0);
		//	alphabet.add(var0); alphabet.add(var1);
		alphabet2.put("var0",var0); alphabet2.put("var1",var1);

		Tree<RankedSymbol> x0 = new StdTree<RankedSymbol>(var0);
		Tree<RankedSymbol> x1 = new StdTree<RankedSymbol>(var1);
		h.put(alphabet1.get("not"),new StdTree<RankedSymbol>(alphabet1.get("not"),
				Util.makeList(x0)));
		h.put(alphabet1.get("or"),new StdTree<RankedSymbol>(alphabet1.get("or"),
				Util.makeList(x0,x1)));

		Tree<RankedSymbol> andTree = 
			new StdTree<RankedSymbol>(
					alphabet1.get("not"), Util.makeList(new StdTree<RankedSymbol>(
							alphabet1.get("or"), Util.makeList(new StdTree<RankedSymbol>(
									alphabet1.get("not"),Util.makeList(x0)),new StdTree<RankedSymbol>(
											alphabet1.get("not"),Util.makeList(x1))))));
		h.put(alphabet1.get("and"),andTree);

		HashMap<RankedSymbol,Integer> toInts = new HashMap<RankedSymbol,Integer>();
		toInts.put(var0, new Integer(0));
		toInts.put(var1, new Integer(1));

		homs.put("hom", new EasyHom(toInts,h));

		//setUpFoo	
		HashMap<RankedSymbol,Tree<RankedSymbol>> hf = new HashMap<RankedSymbol,Tree<RankedSymbol>>();
		hf.put(alphabet1.get("t"),new StdTree<RankedSymbol>(alphabet1.get("t"), new LinkedList<Tree<RankedSymbol>>()));
		hf.put(alphabet1.get("f"),new StdTree<RankedSymbol>(alphabet1.get("f"), new LinkedList<Tree<RankedSymbol>>()));

		hf.put(alphabet1.get("not"),new StdTree<RankedSymbol>(alphabet1.get("not"),Util.makeList(x0)));
		hf.put(alphabet1.get("or"),new StdTree<RankedSymbol>(alphabet1.get("and"),Util.makeList(x1,x1)));
		hf.put(alphabet1.get("and"), new StdTree<RankedSymbol>(alphabet1.get("or"), Util.makeList(x0, x0)));

		homs.put("foo", new EasyHom(toInts,hf));

		//setUpAbc
		HashMap<RankedSymbol,Tree<RankedSymbol>> ha = new HashMap<RankedSymbol,Tree<RankedSymbol>>();
		ha.put(alphabet2.get("a"),new StdTree<RankedSymbol>(alphabet1.get("t")));
		ha.put(alphabet2.get("b"),new StdTree<RankedSymbol>(
				alphabet1.get("and"), Util.makeList(new StdTree<RankedSymbol>(
						alphabet1.get("not"), Util.makeList(x0)), new StdTree<RankedSymbol>(
								alphabet1.get("not"), Util.makeList(x0)))));
		ha.put(alphabet2.get("c"),new StdTree<RankedSymbol>(alphabet1.get("or"),Util.makeList(x0,x1)));

		homs.put("abc", new EasyHom(toInts,ha));

		//setUpFormulasToSymbols
		/*
		 * 0         -> a
		 * 1         -> b(a)
		 * not(x0)   -> c(b(x0),a)
		 * or(x0,x1) -> b(x1)
		 */
		HashMap<RankedSymbol,Tree<RankedSymbol>> hb = new HashMap<RankedSymbol,Tree<RankedSymbol>>();
		Tree<RankedSymbol> tree_a = new StdTree<RankedSymbol>(alphabet2.get("a"));
		hb.put(alphabet1.get("f"), tree_a);
		hb.put(alphabet1.get("t"), new StdTree<RankedSymbol>(alphabet2.get("b"),Util.makeList(tree_a)));
		hb.put(alphabet1.get("not"), new StdTree<RankedSymbol>(alphabet2.get("c"), Util.makeList(
				new StdTree<RankedSymbol>(alphabet2.get("b"),Util.makeList(x0)),tree_a)));
		hb.put(alphabet1.get("or"),new StdTree<RankedSymbol>(alphabet2.get("b"), Util.makeList(x1)));

		homs.put("formulasToSymbols", new EasyHom(toInts,hb));

	}

	private void setUpFTA(){
		//setUpFtaTrueFormulas
		HashSet<State> states = new HashSet<State>();
		State q0 = new NamedState<String>("q0");
		State q1 = new NamedState<String>("q1");
		states.add(q0); states.add(q1);

		HashSet<State> finalStates = new HashSet<State>();
		finalStates.add(q1);

		Set<EasyFTARule> rules = new HashSet<EasyFTARule>();
		rules.add(new EasyFTARule(alphabet1.get("t"),q1));
		rules.add(new EasyFTARule(alphabet1.get("f"),q0));
		rules.add(new EasyFTARule(alphabet1.get("not"),q0,q1));
		rules.add(new EasyFTARule(alphabet1.get("not"),q1,q0));
		rules.add(new EasyFTARule(alphabet1.get("or"),q0,q0,q0));
		rules.add(new EasyFTARule(alphabet1.get("or"),q1,q0,q1));
		rules.add(new EasyFTARule(alphabet1.get("or"),q1,q1,q0));
		rules.add(new EasyFTARule(alphabet1.get("or"),q1,q1,q1));

		autom.put("fta_true_formulas_2",new EasyFTA(rules,finalStates));

		rules.add(new EasyFTARule(alphabet1.get("and"),q0,q0,q0));
		rules.add(new EasyFTARule(alphabet1.get("and"),q0,q0,q1));
		rules.add(new EasyFTARule(alphabet1.get("and"),q0,q1,q0));
		rules.add(new EasyFTARule(alphabet1.get("and"),q1,q1,q1));

		autom.put("fta_true_formulas",new EasyFTA(rules,finalStates));

		//setUpFtaNotFormulas
		rules.clear(); 
		rules.add(new EasyFTARule(alphabet1.get("t"),q1));
		rules.add(new EasyFTARule(alphabet1.get("f"),q1));
		rules.add(new EasyFTARule(alphabet1.get("not"),q1,q1));
		rules.add(new EasyFTARule(alphabet1.get("not"),q1,q1));

		autom.put("fta_not_formulas",new EasyFTA(rules,finalStates));

		//setUpFtaNotOrFormulas
		rules.add(new EasyFTARule(alphabet1.get("or"),q1,q1,q1));
		autom.put("fta_notOr_formulas",new EasyFTA(rules,finalStates));

		//setUpFtaEmpty
		rules.clear();
		states.clear();
		finalStates.clear();
		autom.put("fta_empty",new EasyFTA(rules,finalStates));
	}

	/** Generates some random test cases.*/
	private void setUpRandom(){
		// int numStates,int numSymbols,int maxArity,int numRules,int numFinal){
		RandomFTAGenerator ftaGeneratorStart = new RandomFTAGenerator(3,4,3,40,1);
		RandomFTAGenerator ftaGeneratorDest = new RandomFTAGenerator(5,5,2,50,1);
		RandomTestCases generator = new RandomTestCases();

		// generate alphabets
		ftaGeneratorStart.generateAlphabet();
		ftaGeneratorDest.generateAlphabet();
		randomStartAlph = ftaGeneratorStart.getSymbols();
		randomDestAlph = ftaGeneratorDest.getSymbols();

		// generate homomorphisms and tree automata

		randomHom = new EasyHom[count];
		randomFTA = new EasyFTA[count];
		randomFTADest = new EasyFTA[count];
		randomTree = new Tree[countTree];
		for (int i= 0; i<count; i++){
			// generate linear homomorphisms
			if (i< count-2){
				randomHom[i] = generator.randomHom(randomStartAlph, randomDestAlph, 3, true);
				randomFTA[i] = ftaGeneratorStart.generateReduced();
				randomFTADest[i] = ftaGeneratorDest.generateReduced();
			} else { //nonlinear
				randomHom[i] = generator.randomHom(randomStartAlph, randomDestAlph, 3, false);
				randomFTA[i] = ftaGeneratorStart.generateRaw();
				randomFTADest[i] = ftaGeneratorDest.generateRaw();
			}
		}

		// generate trees
		for (int i=0; i<countTree; i++){
			randomTree[i] = generator.randomTree(randomStartAlph, 5);
		}
	}

	/**
	 * Sets up all the needed things: the alphabet, the trees, the homomorphism, the finite tree automata
	 * and the randomize generated objects.
	 * 
	 * @throws java.lang.Exception if something goes wrong
	 */
	@Before
	public void setUp() throws Exception {
		setUpAlphabet();
		setUpTree();
		setUpHom();
		setUpFTA();
		setUpRandom();

		// set up h1
		h1 = HomomorphismParser.parseString("f(x,y) -> g(n(x),y) \n a->a \n  b->b");
		ta1 = FTAParser.parseString("a -> 1 \n  b -> 2 \n f(2) -> 3 " +
		"\n f(3) -> 3 \n  g(3,3) -> 4!");

	}

	/**
	 * Tries to apply several homomorphisms on different trees.<br>
	 * For the self-created examples it is checked, whether the result is correct.
	 */
	@Test
	public void testApplyOnTree() {
		Tree<RankedSymbol> result_0 = homs.get("hom").apply(trees.get("0"));
		Tree<RankedSymbol> result_1 = homs.get("hom").apply(trees.get("1"));
		Tree<RankedSymbol> result_hom_a = homs.get("hom").apply(trees.get("test_a"));
		Tree<RankedSymbol> result_hom_b = homs.get("hom").apply(trees.get("test_b"));
		Tree<RankedSymbol> result_foo_a = homs.get("foo").apply(trees.get("test_a"));
		Tree<RankedSymbol> result_abc_c = homs.get("abc").apply(trees.get("test_c"));
		Tree<RankedSymbol> result_abc_d = homs.get("abc").apply(trees.get("test_d"));

		Assert.assertTrue(result_0.equals(trees.get("0")));
		Assert.assertTrue(result_1.equals(trees.get("1")));
		Assert.assertTrue(result_hom_a.equals(trees.get("result_a")));

		Assert.assertTrue(result_hom_b.equals(trees.get("result_b")));
		Assert.assertTrue(result_foo_a.equals(trees.get("foo_a")));
		Assert.assertFalse(result_0.equals(trees.get("not_0")));

		Assert.assertTrue(result_abc_c.equals(trees.get("result_c")));
		Assert.assertTrue(result_abc_d.equals(trees.get("1")));

		// test random cases
		for (int i= 0; i<count;i++){
			for (int j= 0; j<countTree; j++){
				randomHom[i].apply(randomTree[j]);
			}
		}
	}


	/**
	 * Tests the method {@link EasyHom#isLinear()}.
	 */
	@Test
	public void testIsLinear() {
		//  System.out.println(hom);
		Assert.assertTrue(homs.get("hom").isLinear());
		Assert.assertFalse(homs.get("abc").isLinear());
		Assert.assertFalse(homs.get("foo").isLinear());
		for (int i = 0; i< count-2; i++){
			Assert.assertTrue(randomHom[i].isLinear());
		}
	}


	/**
	 * Tries to apply several homomorphisms on a tree automaton, tests
	 * {@link EasyHom#applyOnAutomaton(EasyFTA)}.
	 */
	@Test
	public void testApplyOnAutomaton() {
		EasyFTA fta_result = homs.get("hom").applyOnAutomaton(autom.get("fta_true_formulas"));
		boolean exc = false;
		try{
			fta_result = homs.get("foo").applyOnAutomaton(autom.get("fta_true_formulas"));
		} catch (Exception e){
			exc = true;
		}	
		Assert.assertTrue("non linear homomorphism cannot be applied on automata", exc);
		//System.out.println(fta_not_formulas);
		EasyFTA fta_result1 = homs.get("hom").applyOnAutomaton(autom.get("fta_not_formulas"));
		//System.out.println(fta_result1);

		Assert.assertFalse(fta_result.decide(trees.get("test_a")));
		Assert.assertTrue(autom.get("fta_not_formulas").decide(trees.get("not_0")));
		Assert.assertTrue(fta_result1.decide(trees.get("not_0")));
		//		System.out.println(TreeAutomataOperations.reduce(fta_result));
		Assert.assertTrue(fta_result.decide(trees.get("1")));	

		Assert.assertTrue(autom.get("fta_notOr_formulas").decide(homs.get("hom").apply(
				trees.get("test_a"))));
		//test again whether fta_result ist subset of fta_notOr_formulas 
		Assert.assertTrue(FTAProperties.subsetLanguage(fta_result, autom.get("fta_notOr_formulas")));

		//test empty automaton
		Assert.assertTrue(FTAProperties.emptyLanguage(homs.get(
		"hom").applyOnAutomaton(autom.get("fta_empty"))));

		try{
			h1.applyOnAutomaton(ta1);
		} catch (Exception e){}
	}


	/**
	 * Tries to apply the inverse homomomorphism on a tree automaton, tests
	 * {@link EasyHom#applyInverseOnAutomaton(EasyFTA)}.<br>
	 * Furthermore it is tested if applying the homomorphism and then the inverse homomorphism
	 * on a finite tree automaton supplies a regular tree language which encloses the original tree 
	 * language. 
	 */
	@Test
	public void testApplyInverseOnAutomaton(){
		System.out.println(homs.get("hom"));
		EasyFTA fta_result = homs.get("hom").applyInverseOnAutomaton(autom.get("fta_not_formulas"));
		Assert.assertTrue(fta_result.decide(trees.get("not_0")));
		EasyFTA fta_result_abc = homs.get("foo").applyInverseOnAutomaton(autom.get("fta_true_formulas"));
		Assert.assertFalse("the result should not be empty",FTAProperties.emptyLanguage(fta_result_abc));
		Assert.assertTrue("tree automaton stays empty", FTAProperties.emptyLanguage(homs.get(
		"foo").applyInverseOnAutomaton(autom.get("fta_empty"))));

		EasyFTA fta_true_formulas_3 = homs.get("hom").applyInverseOnAutomaton(autom.get("fta_true_formulas_2"));
		System.out.println(autom.get("fta_true_formulas") +"\n");
		System.out.println("fta_true_formulas_3:" + fta_true_formulas_3);

		Assert.assertTrue("true formulas stay true",FTAProperties.sameLanguage(fta_true_formulas_3, 
				autom.get("fta_true_formulas")));

		//same language is in this context right, because variables on left and right side are the same
		Assert.assertTrue("first apply hom on tree automaton, than inverse",
				FTAProperties.sameLanguage(autom.get("fta_true_formulas"), homs.get(
				"hom").applyInverseOnAutomaton(homs.get("hom").applyOnAutomaton(autom.get("fta_true_formulas")))));
		Assert.assertTrue("first apply inverse hom on tree automaton, than hom",FTAProperties.sameLanguage(autom.get("fta_true_formulas_2"),
				homs.get("hom").applyOnAutomaton(homs.get("hom").applyInverseOnAutomaton(
						autom.get("fta_true_formulas_2")))));

		EasyFTA fta_something1 = homs.get("formulasToSymbols").applyOnAutomaton(autom.get("fta_true_formulas_2"));
		EasyFTA fta_something = homs.get("formulasToSymbols").applyInverseOnAutomaton(fta_something1);
		//System.out.println(fta_something1);
		Assert.assertTrue("first apply hom on tree automaton, than inverse",
				FTAProperties.subsetLanguage(autom.get("fta_true_formulas_2"), fta_something));

		// test random cases
		for (String hs: homs.keySet()){
			for (String as: autom.keySet()){
				if (homs.get(hs).isLinear() && homs.get(hs).getSrcAlphabet().containsAll(autom.get(as).getAlphabet())){
					System.out.println("applyOnAutom...");
					EasyFTA fta1 = homs.get(hs).applyOnAutomaton(autom.get(as));
									
					RegularTreeLanguage<RankedSymbol> rtl = new RegularTreeLanguage<RankedSymbol>(fta1);
					GenFTA<RankedSymbol,? extends State> fta1gen = rtl.getFTA();
					System.out.println("applyInverse...");
					GenFTA<RankedSymbol, NamedState<? extends Object>> fta2 = 
						homs.get(hs).applyInverseOnAutomaton(GenFTAOps.reduceFull(fta1gen));
	
					System.out.println("subsetLanguage...");
					String err = "Homomorphismus : ================ \n" + homs.get(hs);
					err += "\n FTA: =================== \n"+autom.get(as);
					err += "\n HomFTA ================ \n" +fta1gen;
					err += "\n InvHomHomFTA ================= \n" +fta2;
					boolean errb = FTAProperties.subsetLanguage(autom.get(as),fta2);
					if (!errb){
						System.out.println(err);
					}
					Assert.assertTrue(err,errb);
				
				}
			}
		}
	}


	/**
	 * Tests some property functions of homomorphism except the linearity
	 * with a self-created example.
	 */
	@Test
	public void testProperties(){
		Assert.assertFalse(homs.get("hom").isAlphabetic());
		Assert.assertTrue(homs.get("hom").isComplete());
		Assert.assertFalse(homs.get("hom").isDelabeling());
		Assert.assertTrue(homs.get("hom").isEpsilonFree());
		Assert.assertFalse(homs.get("hom").isSymbolToSymbol());
	}

	/** 
	 * Some more special random tests to test consistency.<br>
	 * First the homomorphism is applied on an automaton and then inverse if it is linear and 
	 * otherwise the other way round.
	 * It is tested whether the trees accepted before are accepted by the new automaton,
	 * which must be true.
	 */
	@Test
	public void testRandomCases(){
		// test inverse properties
		// test random cases
		for (int i= 0; i<count;i++){
			for (int j= 0; j<count; j++){
				System.out.println("Consider hom "+i+" with automaton " + j);
				System.out.println("applyOnTrees...");
				for (int k= 0; k<countTree;k++){				
					if (randomFTA[j].decide(randomTree[k])){
						if (randomHom[i].isLinear()) {
							Assert.assertTrue(FTAProperties.decide(
								randomHom[i].applyOnAutomaton(randomFTA[j]),
								randomHom[i].apply(randomTree[k])));
						}
					}
				}

				//try{
				if (randomHom[i].isLinear()){
					System.out.println("applyOnAutom...");
					EasyFTA fta1 = randomHom[i].applyOnAutomaton(randomFTA[j]);
					RegularTreeLanguage<RankedSymbol> rtl = new RegularTreeLanguage<RankedSymbol>(fta1);
					GenFTA<RankedSymbol,? extends State> fta1gen = rtl.getFTA();
					System.out.println("applyInverse...");
					GenFTA<RankedSymbol, NamedState<? extends Object>> fta2 = 
						randomHom[i].applyInverseOnAutomaton(GenFTAOps.reduceFull(fta1gen));
					System.out.println("subsetLanguage...");
					String err = "Homomorphismus : ================ \n" + randomHom[i];
					err += "\n FTA: =================== \n"+randomFTA[j];
					err += "\n HomFTA ================ \n" +fta1gen;
					err += "\n InvHomHomFTA ================= \n" +fta2;
					boolean errb = FTAProperties.subsetLanguage(randomFTA[j],fta2);
					if (!errb){
						System.out.println(err);
					}
					Assert.assertTrue(err,errb);
				
				}
				System.out.println("applyInverse...");
				EasyFTA fta1 = randomHom[i].applyInverseOnAutomaton(randomFTADest[j]);
				System.out.println("applyOnAutom...");
				EasyFTA fta2 = randomHom[i].applyOnAutomaton(fta1);
				System.out.println("subsetLanguage...");
				Assert.assertTrue(FTAProperties.subsetLanguage(fta2,randomFTADest[j]));
			}
		}
	}

}
