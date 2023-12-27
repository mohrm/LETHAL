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
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import de.uni_muenster.cs.sev.lethal.languages.RegularTreeLanguage;
import de.uni_muenster.cs.sev.lethal.parser.tree.TreeParser;
import de.uni_muenster.cs.sev.lethal.states.NamedState;
import de.uni_muenster.cs.sev.lethal.states.State;
import de.uni_muenster.cs.sev.lethal.symbol.common.RankedSymbol;
import de.uni_muenster.cs.sev.lethal.symbol.standard.StdNamedRankedSymbol;
import de.uni_muenster.cs.sev.lethal.tree.common.Tree;
import de.uni_muenster.cs.sev.lethal.tree.common.TreeOps;
import de.uni_muenster.cs.sev.lethal.tree.standard.StdTreeCreator;
import de.uni_muenster.cs.sev.lethal.treeautomata.common.FTACreator;
import de.uni_muenster.cs.sev.lethal.treeautomata.common.FTAOps;
import de.uni_muenster.cs.sev.lethal.treeautomata.common.FTAProperties;
import de.uni_muenster.cs.sev.lethal.treeautomata.common.SimpleFTARuleSet;
import de.uni_muenster.cs.sev.lethal.treeautomata.easy.EasyFTA;
import de.uni_muenster.cs.sev.lethal.treeautomata.easy.EasyFTAEpsRule;
import de.uni_muenster.cs.sev.lethal.treeautomata.easy.EasyFTAOps;
import de.uni_muenster.cs.sev.lethal.treeautomata.easy.EasyFTARule;
import de.uni_muenster.cs.sev.lethal.utils.RandomFTAGenerator;



/**
 * A finite tree automata assertion states that some binary relation between two automata
 * holds or does not hold.
 **/
class FTAAssertion {

	/**
	 * First automaton participating at the assertion.
	 */
	private EasyFTA fta1;

	/**
	 * First second participating at the assertion.
	 */
	private EasyFTA fta2;

	/**
	 * String representation of the assertion.
	 */
	private String desc;

	/**
	 * Stores whether the assertion is true or not.
	 */
	private Boolean statement = Boolean.TRUE;


	/**
	 * Constructs the assertion.
	 *
	 * @param fta1 first automaton
	 * @param fta2 second automaton
	 * @param desc description of the assertion - used if the assertion fails
	 * @param b if the assertion is true or not
	 */
	public FTAAssertion(EasyFTA fta1, EasyFTA fta2, String desc, Boolean b) {
		this.fta1 = fta1;
		this.fta2 = fta2;
		this.statement = b;
		this.desc = desc;
	}

	/**
	 * Returns the first automaton participating in the assertion.
	 *
	 * @return the first automaton participating in the assertion
	 */
	public EasyFTA getFirstAutomaton() {
		return fta1;
	}

	/**
	 * Returns the second automaton participating in the assertion.
	 *
	 * @return the second automaton participating in the assertion
	 */
	public EasyFTA getSecondAutomaton() {
		return fta2;
	}

	/**
	 * Returns whether the relation between the two automata holds or does not hold.
	 *
	 * @return true if the relation between the two automata holds, false if it does not hold
	 */
	public Boolean getStatement() {
		return statement;
	}

	/**
	 * @see Object#toString()
	 */
	@Override
	public String toString() {
		return desc;
	}
}

/**
 * Checks whether all the operations we can apply on finite tree automata
 * work as expected.<br>
 * In general we use some random generated test cases and many self-defined tests
 * to check the operations. Here we give a list of the defined automata in form of their language.<br>
 *
 * List of used languages:<br>
 * alphabet = {a, b, c, f(,), g(,,), h()}<br>
 * <ul>
 * <li> L0 = {}</li>
 * <li> L1 = trees with root a,b,c or f, where f can get every symbol a parameter,
 * g can only get a and f. Each of those trees shall be accepted. </li>
 * <li> L2 = like L1, but if g is a parameter of f and another parameter of f is c, then g can only have a as parameter,
 * 	    and c is the only constant that is accepted as tree </li>
 * <li> L3 = {g(a,b,c)} </li>
 * <li> L4 = {f(a,a),f(a,b),f(b,a),f(b,b)} </li>
 * <li> L5 = {f(a,a),f(a,c),f(c,a),f(c,c)} </li>
 * <li> L6 = {f(a,a)} </li>
 * <li> L7 = {f(a,b),f(b,a),f(b,b)} </li>
 * <li> L8 = {g(f(a,a),a,f(a,a)),g(f(a,a),a,f(a,b)), g(f(a,a),a,f(b,a)), g(f(a,a),a,f(b,b))}</li>
 * <li> Lgerade = {h^n(a): n gerade} </li>
 * <li> Lungerade = {h^n(a): n ungerade} </li>
 * <li>  Martin = {f(b,b)} </li>
 * <li> Irene = {a}</li>
 * <li> Doro = {b} </li>
 * </ul>
 * The following assertions hold:
 * <ul>
 * <li>	L2 subset L1.</li>
 * <Li>	L1 cap L3 = L0 </li>
 * <li>	L1 union L2 = L1 </li>
 * <li> L4 cap L5 = L6 </li>
 * <li> L4 setminus L6 = L7 </li>
 * <li> L4 setminus L5 = L7 </li>
 * <li> L8 = varTree(L6,L4)</li>
 * </ul>
 * @author Dorothea, Irene, Martin
 *
 */
public class StdFTAOpTest {

	/** Used symbols: {a, b, c, d, f(,), g(,,), h()}, represented by their string representation. */
	private static Map<String, RankedSymbol>  alphabet = new HashMap<String, RankedSymbol>();

	/**Used states. */
	private static Map<String, State> states = new HashMap<String, State>();

	/**Used automata. */
	private static Map<String, EasyFTA> testAutom = new HashMap<String,EasyFTA>();

	/** Stores for each automaton, if it has finite language. */
	private static Map<String, Boolean> testAutomFinLang = new HashMap<String,Boolean>();

	/** Test trees. */
	private static Map<String, Tree<RankedSymbol>> testTrees = new HashMap<String,Tree<RankedSymbol>>();

	/** Stores the assertions which should hold:
	 *  <ul>
	 * <li>	L2 subset L1.</li>
	 * <Li>	L1 cap L3 = L0 </li>
	 * <li>	L1 union L2 = L1 </li>
	 * <li> L4 cap L5 = L6 </li>
	 * <li> L4 setminus L6 = L7 </li>
	 * <li> L4 setminus L5 = L7 </li>
	 * <li> L8 = varTree(L6,L4)</li>
	 * </ul>*/
	private static Map<String,List<FTAAssertion>> assertions = new HashMap<String,List<FTAAssertion>>();


	/**
	 * Index of the non deterministic automaton with lower bound for states after determinize
	 * look at {@link StdFTAOpTest#setUpReallyNonDetAutomaton()}.
	 */
	private static int nondet_index = 3;


	/**
	 * Creates a finite tree automaton from a very compressed string representation of the rules and final states
	 * and stores it in the global table for test automata.
	 *
	 * @param name name of the new automaton
	 * @param rules collection of string arrays; each of those string arrays represents a rule; first string
	 * is taken as symbol, last string is taken as destination state and the other are taken as source state
	 * @param finals final states of the new automaton - vararg for convenience
	 */
	private static void makeAutomatonFromRules(String name, Iterable<String[]> rules, String... finals) {
		HashSet<EasyFTARule> ruleset = new HashSet<EasyFTARule>();
		for (String[] data: rules) {
			RankedSymbol srcSymbol = alphabet.get(data[0]);
			List<State> srcStates = new LinkedList<State>();
			State destState = states.get(data[data.length-1]);
			for (int i=1; i<data.length-1; i++)
				srcStates.add(states.get(data[i]));
			ruleset.add(new EasyFTARule(srcSymbol,srcStates,destState));
		}
		HashSet<State> finalstat = new HashSet<State>();
		for (String f: finals) {
			finalstat.add(states.get(f));
		}
		testAutom.put(name, new EasyFTA(ruleset,finalstat));
	}

	/**
	 * Sets up some assertions which belong to the test finite tree automata.<br>
	 *
	 * Idea: we have two different kinds of assertions to test. First, we have relations which are independent
	 * of the concrete automaton, i.e. set-theoretic laws about the recognized languages. The second kind
	 * depends on the concrete automata and should be tested at one place, so that we don't have to scroll
	 * around all the time, if we change something: We group these assertions by their kind - there are
	 * assertions which state that a subset relation between the languages of two automata holds or does not hold,
	 * there are assertions which states that the languages of two automata are equal, etc.
	 * We encode this information into lists of certain objects, which lead to the right assertions in a natural
	 * way, see below. <br>
	 *
	 * Assertions tested: <br>
	 * <ul>
	 * <li> L2 c L1 </li>.
	 * <li>	L1 cap L3 = L0 </li>
	 * <li>	L1 union L2 = L1 </li>
	 * <li> L4 cap L5 = L6 </li>
	 * <li> L4 - L6 = L7 </li>
	 * <li> L4 - L5 = L7 </li>
	 * <li> L8 = varTree(L6,L4) </li>
	 * </ul>
	 */
	protected static void setUpAssertions() {
		// subset
		List<FTAAssertion> subsetAssertions = new LinkedList<FTAAssertion>();

		// L2 is a proper subset L1
		subsetAssertions.add(new FTAAssertion(testAutom.get("fta_L2"), testAutom.get("fta_L1_1"), "L2_1/L1_1",true));
		subsetAssertions.add(new FTAAssertion(testAutom.get("fta_L2"), testAutom.get("fta_L1_2"),"L2_1/L1_2", true));
		subsetAssertions.add(new FTAAssertion(testAutom.get("fta_L1_1"), testAutom.get("fta_L2"), "L1_1/L2_1",false));
		subsetAssertions.add(new FTAAssertion(testAutom.get("fta_L1_2"), testAutom.get("fta_L2"), "L1_2/L2_1",false));

		List<FTAAssertion> sameAssertions = new LinkedList<FTAAssertion>();
		EasyFTA i113 = EasyFTAOps.intersectionTD(testAutom.get("fta_L1_1"), testAutom.get("fta_L3"));
		EasyFTA i123 = EasyFTAOps.intersectionTD(testAutom.get("fta_L1_2"), testAutom.get("fta_L3"));
		EasyFTA u112 = EasyFTAOps.union(testAutom.get("fta_L1_1"), testAutom.get("fta_L2"));
		EasyFTA u122 = EasyFTAOps.union(testAutom.get("fta_L1_2"), testAutom.get("fta_L2"));
		EasyFTA i45 = EasyFTAOps.intersectionTD(testAutom.get("fta_L4"), testAutom.get("fta_L5"));
		EasyFTA d46 = EasyFTAOps.difference(testAutom.get("fta_L4"), testAutom.get("fta_L6"));
		EasyFTA d45 = EasyFTAOps.difference(testAutom.get("fta_L4"), testAutom.get("fta_L6"));
		EasyFTA c_even = EasyFTAOps.complement(testAutom.get("fta_even"));
		EasyFTA c_odd = EasyFTAOps.complement(testAutom.get("fta_odd"));
		HashMap<RankedSymbol,EasyFTA> languages = new HashMap<RankedSymbol, EasyFTA>();
		languages.put(alphabet.get("d"), testAutom.get("fta_L4"));
		languages.put(alphabet.get("c"), testAutom.get("fta_L6"));
		EasyFTA subst = EasyFTAOps.substitute(testTrees.get("g_subst"), languages);

		// L1 cap L3 = L0
		sameAssertions.add(new FTAAssertion(i113, testAutom.get("fta_L0"), "i113", true));
		sameAssertions.add(new FTAAssertion(i123, testAutom.get("fta_L0"), "i123", true));

		// L1 union L2 = L1
		sameAssertions.add(new FTAAssertion(u112, testAutom.get("fta_L1_1"), "u112", true));
		sameAssertions.add(new FTAAssertion(u122, testAutom.get("fta_L1_1"), "u122", true));
		sameAssertions.add(new FTAAssertion(u112, testAutom.get("fta_L1_2"), "u112", true));
		sameAssertions.add(new FTAAssertion(u122, testAutom.get("fta_L1_2"), "u112", true));

		// L4 cap L5 = L6
		sameAssertions.add(new FTAAssertion(i45, testAutom.get("fta_L6"), "L4 \\cap L5 = L6", true));

		// L4 setminus L5 = L7
		sameAssertions.add(new FTAAssertion(d45, testAutom.get("fta_L7"), "d45", true));

		// L4 setminus L6 = L7
		sameAssertions.add(new FTAAssertion(d46, testAutom.get("fta_L7"), "d46", true));

		// L_even^c = L_odd
		sameAssertions.add(new FTAAssertion(c_even, testAutom.get("fta_odd"), "even_odd_1", true));

		// L_odd^c = L_even
		sameAssertions.add(new FTAAssertion(c_odd, testAutom.get("fta_even"), "even_odd_2", true));

		// L8 = varTree(L6,L4)
		sameAssertions.add(new FTAAssertion(subst, testAutom.get("fta_var"), "subst", true));

		assertions.put("subset",subsetAssertions);
		assertions.put("same", sameAssertions);
	}

	/**
	 * See {@link StdFTAOpTest#setUpAssertions} for explaining assertions.
	 * This methods test them all for correctness.
	 */
	@Test
	public void testAssertions() {
		for (FTAAssertion ass: assertions.get("subset"))
			Assert.assertTrue(ass.toString(),FTAProperties.subsetLanguage(ass.getFirstAutomaton(),ass.getSecondAutomaton())==ass.getStatement());

		for (FTAAssertion ass: assertions.get("same"))
			Assert.assertTrue(ass.toString(), FTAProperties.sameLanguage(ass.getFirstAutomaton(),ass.getSecondAutomaton())==ass.getStatement());
	}



	/**
	 * Sets up the alphabet we use in this test class:<br>
	 * a,b,c,f(,),g(,,),h(),u().
	 *
	 * @throws Exception is (hopefully) never thrown
	 */
	static void setUpAlphabet() throws Exception {
		alphabet.put("a",new StdNamedRankedSymbol<String>("a", 0));
		alphabet.put("b",new StdNamedRankedSymbol<String>("b", 0));
		alphabet.put("c",new StdNamedRankedSymbol<String>("c", 0));
		alphabet.put("d",new StdNamedRankedSymbol<String>("d", 0));
		alphabet.put("f",new StdNamedRankedSymbol<String>("f", 2));
		alphabet.put("g",new StdNamedRankedSymbol<String>("g", 3));
		alphabet.put("h",new StdNamedRankedSymbol<String>("h", 1));
		alphabet.put("u",new StdNamedRankedSymbol<String>("u", 1));
	}

	/**
	 * Sets up all different states we need:<br>
	 * q1,q2,qa,qb,qc,qbc,qf,qg,qga,qgf,qbot,q_ger,q_ung,...
	 */
	static void setUpStates() {
		states.put("q1", new NamedState<String>("q1"));
		states.put("q2", new NamedState<String>("q2"));
		states.put("qa", new NamedState<String>("qa"));
		states.put("qb", new NamedState<String>("qb"));
		states.put("qc", new NamedState<String>("qc"));
		states.put("qbc", new NamedState<String>("qbc"));
		states.put("qf", new NamedState<String>("qf"));
		states.put("qg", new NamedState<String>("qg"));
		states.put("qga", new NamedState<String>("qga"));
		states.put("qgf", new NamedState<String>("qgf"));
		states.put("qbot", new NamedState<String>("qbot"));
		states.put("q_ger", new NamedState<String>("q_ger"));
		states.put("q_ung", new NamedState<String>("q_ung"));
	}

	/**
	 * Sets up some trees for testing in different languages:
	 * <ul>
	 * <li> treeA : a </li>
	 * <li> treeB : b </li>
	 * <li> treeC : c </li>
	 * <li> tree_L2_0 : g(a,f(b,c),a) </li>
	 * <li> tree_L2_1 : f(g(a,a,a),c) </li>
	 * <li> tree_L2_2 : f(g(a,f(b,c),a),c) </li>
	 * <li> tree_L2_3 : f(g(a,f(b,c),a),b) </li>
	 * <li> tree_h3 : h(h(h(a))) </li>
	 * <li> tree_L2_1 : f(b,b) </li>
	 * <li> faa : f(a,a) </li>
	 * <li> fbb : f(b,b) </li>
	 * <li> hc : h(c) </li>
	 * <li> fbc : f(b,c) </li>
	 * <li> g_1 : g(f(d,a),h(f(c,b)),a) </li>
	 * <li> g_subst : g(c,a,d) </li>
	 * <li> g_2 : g(f(a,a),a,f(a,b)) </li>
	 * </ul>
	 *
	 * @throws Exception is never thrown
	 */
	static void setUpTrees() throws Exception {
		testTrees.put("treeA", TreeParser.parseString("a"));
		testTrees.put("treeB",TreeParser.parseString("b"));
		testTrees.put("treeC", TreeParser.parseString("c"));
		testTrees.put("tree_L2_0", TreeParser.parseString("g(a,f(b,c),a)"));
		testTrees.put("tree_L2_1", TreeParser.parseString("f(g(a,a,a),c)"));
		testTrees.put("tree_L2_2", TreeParser.parseString("f(g(a,f(b,c),a),c)"));
		testTrees.put("tree_L2_3", TreeParser.parseString("f(g(a,f(b,c),a),b)"));
		testTrees.put("tree_h3", TreeParser.parseString("h(h(h(a)))"));
		testTrees.put("fbb", TreeParser.parseString("f(b,b)"));
		testTrees.put("faa", TreeParser.parseString("f(a,a)"));
		testTrees.put("hc", TreeParser.parseString("h(c)"));
		testTrees.put("fbc", TreeParser.parseString("f(b,c)"));
		testTrees.put("g_1", TreeParser.parseString("g(f(d,a),h(f(c,b)),a)"));
		testTrees.put("g_subst", TreeParser.parseString("g(c,a,d)"));
		testTrees.put("g_2", TreeParser.parseString("g(f(a,a),a,f(a,b))"));
	}


	/**
	 * Sets up an empty automaton called empty
	 * and one which only contains symbols, called pseudo-empty.
	 */
	static void setUpEmptyAutomaton() {
		testAutom.put("empty", new EasyFTA());
		testAutom.put("pseudo-empty", new EasyFTA());
		testAutom.get("pseudo-empty").addSymbols(Util.makeList(alphabet.get("a"),alphabet.get("f")));
		testAutomFinLang.put("empty", true);
		testAutomFinLang.put("pseudo-empty", true);
	}

	/**
	 * Sets up a non deterministic automaton "fta_nondet", which is trivial.
	 */
	static void setUpNonDetAutomaton() {
		String[][] rules = {{"a","qa"},{"a","qf"},{"f","qf","qa","qf"}};
		makeAutomatonFromRules("fta_nondet", Arrays.asList(rules),"qf");
		testAutomFinLang.put("fta_nondet", false);
	}

	/**
	 * Sets up an automaton with n+1 states. You can prove that its determinized version
	 * has at least 2^(n+1) states.
	 *
	 * @param n n+1 is the number of states of the non deterministic automaton
	 */
	static void setUpReallyNonDetAutomaton(int n) {
		// rules
		SimpleFTARuleSet<RankedSymbol,State,EasyFTARule> rules = new SimpleFTARuleSet<RankedSymbol,State,EasyFTARule>();
		for (Integer i=0; i<=n+1; i++)
			states.put(i.toString(),new NamedState<Integer>(i));

		rules.add(new EasyFTARule(alphabet.get("a"),states.get("0")));
		rules.add(new EasyFTARule(alphabet.get("h"),states.get("0"),states.get("0")));
		Integer i,j;
		for (i=1,j=2; i<=n; i++,j++)
			rules.add(new EasyFTARule(alphabet.get("h"),states.get(Integer.toString(i + 1)),states.get(i.toString())));

		rules.add(new EasyFTARule(alphabet.get("u"),states.get("0"),states.get("0")));
		rules.add(new EasyFTARule(alphabet.get("u"),states.get("1"),states.get("0")));
		for (i=1,j=2; i<=n; i++,j++)
			rules.add(new EasyFTARule(alphabet.get("u"),states.get(j.toString()),states.get(i.toString())));

		testAutom.put("fta_reallynondet",new EasyFTA(rules,states.get(Integer.toString(n + 1))));
		testAutomFinLang.put("fta_reallynondet", false);
	}

	/**
	 * Sets up fta_L0. <br>
	 * This finite tree automaton has empty language.
	 */
	static void setUpFta_L0() {
		String[][] rules = {{"c","q1"},{"f","q1","q2","q2"}};
		makeAutomatonFromRules("fta_L0", Arrays.asList(rules), "q2");
		testAutomFinLang.put("fta_L0", true);
	}

	/**
	 * Sets up fta_L1_1, a finite tree automaton recognizing language L1. <br>
	 * L1 = trees with root a,b,c or f, where f can get every symbol a parameter, g can only get a and f.
	 * Each of those trees shall be accepted.
	 *
	 * @throws Exception is (hopefully) never thrown
	 */
	static void setUpFta_L1_1() throws Exception {
		// rules
		SimpleFTARuleSet<RankedSymbol,State,EasyFTARule> rules = new SimpleFTARuleSet<RankedSymbol,State,EasyFTARule>();
		rules.add(new EasyFTARule(alphabet.get("a"), states.get("qa")));
		rules.add(new EasyFTARule(alphabet.get("b"), states.get("qbc")));
		rules.add(new EasyFTARule(alphabet.get("c"), states.get("qbc")));

		// f may get each pair of states
		String[] myStates = {"qa","qbc","qf","qg"};
		for (String p : myStates) {
			for (String q : myStates)
				rules.add(new EasyFTARule(alphabet.get("f"), states.get("qf"),states.get(p), states.get(q)));
		}
		// g may get all triples consisting qa and/or qf
		String[] statesg = {"qa","qf"};
		for (String p : statesg) {
			for (String q : statesg) {
				for (String r : statesg) {
					// all rules have final state qg
					rules.add(new EasyFTARule(alphabet.get("g"), states.get("qg"), states.get(p), states.get(q), states.get(r)));
				}
			}
		}
		// tree automaton
		testAutom.put("fta_L1_1", new EasyFTA(rules, states.get("qf"), states.get("qa"), states.get("qbc")));
		testAutom.get("fta_L1_1").addSymbols(alphabet.values());
		testAutomFinLang.put("fta_L1_1", false);
	}

	/**
	 * Sets up another finite tree automaton for language L1, called fta_L1_2. <br>
	 * L1 = trees with root a,b,c or f, where f can get every symbol a parameter, g can only get a and f.
	 * Each of those trees shall be accepted.
	 *
	 * @throws Exception
	 */
	static void setUpFta_L1_2() throws Exception {
		// rules
		SimpleFTARuleSet<RankedSymbol,State,EasyFTARule> rules = new SimpleFTARuleSet<RankedSymbol,State,EasyFTARule>();
		rules.add(new EasyFTARule(alphabet.get("a"), states.get("qa")));
		rules.add(new EasyFTARule(alphabet.get("b"), states.get("qb")));
		rules.add(new EasyFTARule(alphabet.get("c"), states.get("qc")));

		// f may get each pair of states
		String[] myStates = {"qa","qb","qc","qf","qg"};
		for (String p : myStates) {
			for (String q : myStates)
				rules.add(new EasyFTARule(alphabet.get("f"), states.get("qf"), states.get(p), states.get(q)));
		}
		// g may get all triples consisting qa and/or qf
		String[] statesg = {"qa", "qf"};
		for (String p : statesg) {
			for (String q : statesg) {
				for (String r : statesg) {
					// all rules have final state qg
					rules.add(new EasyFTARule(alphabet.get("g"), states.get("qg"), states.get(p), states.get(q), states.get(r)));
				}
			}
		}

		// tree automaton
		testAutom.put("fta_L1_2",new EasyFTA(rules, states.get("qa"), states.get("qb"), states.get("qc"), states.get("qf")));
		testAutomFinLang.put("fta_L1_2", false);
	}

	/**
	 * Sets up an finite tree automaton recognizing L2, called fta_L2.<br>
	 *
	 * L1 = trees with root a,b,c or f, where f can get every symbol a parameter,
	 * g can only get a and f. Each of those trees shall be accepted. <br>
	 * L2 = like L1, but if g is a parameter of f and another parameter of f is c, then g can only have a as parameter,
	 *
	 * @throws Exception is (hopefully) never thrown
	 */
	static void setUpFta_L2() throws Exception {
		// rules
		SimpleFTARuleSet<RankedSymbol,State,EasyFTARule> rules = new SimpleFTARuleSet<RankedSymbol,State,EasyFTARule>();
		rules.add(new EasyFTARule(alphabet.get("a"), states.get("qa")));
		rules.add(new EasyFTARule(alphabet.get("b"), states.get("qb")));
		rules.add(new EasyFTARule(alphabet.get("c"), states.get("qc")));

		String[] myStates = {"qa","qb","qc","qf","qga","qgf"};
		// f may get each pair of states except (qc,qgf) and (qgf,qc)
		for (String p : myStates)
			for (String q : myStates)
				// f must neither get the Pair (qc,qgf) nor (qgf,qc)
				if (!(p.equals("qc") &&  q.equals("qgf"))
						&& !(p.equals("qgf") && q.equals("qc"))) {
					// add the rule; final state is always qf
					rules.add(new EasyFTARule(alphabet.get("f"), states.get("qf"), states.get(p), states.get(q)));
				}

		// g may get all triples consisting qa and/or qf
		String[] statesg = {"qa","qf"};
		for (String p : statesg) {
			for (String q : statesg) {
				for (String r : statesg) {
					// g(qa,qa,qa) -> qga; all other rules have final state qgf
					if (p.equals("qa") && q.equals("qa") && r.equals("qa"))
						rules.add(new EasyFTARule(alphabet.get("g"), states.get("qga"), states.get(p), states.get(q), states.get(r)));
					else
						rules.add(new EasyFTARule(alphabet.get("g"), states.get("qgf"), states.get(p), states.get(q), states.get(r)));
				}
			}
		}
		// tree automaton
		testAutom.put("fta_L2", new EasyFTA(rules, states.get("qc"), states.get("qf")));
		testAutomFinLang.put("fta_L2", false);
	}

	/**
	 * Sets up a finite tree automaton for the language L3 = {g(a,b,c)}, called fta_L3.
	 *
	 * @throws Exception is (hopefully) never thrown
	 */
	static void setUpFta_L3() throws Exception {
		String[][] rules = {{"a","qa"},{"b","qb"},{"c","qc"},{"g","qa","qb","qc","qg"},{"g","qf","qf","qf","qf"}};
		makeAutomatonFromRules("fta_L3", Arrays.asList(rules),"qf","qg");
		testAutomFinLang.put("fta_L3", true);
	}

	/**
	 * Sets up a finite tree automaton for language L4, called fta_L4. <br>
	 * L4 = {f(a,a),f(a,b),f(b,a),f(b,b)}
	 *
	 * @throws Exception is (hopefully) never thrown
	 */
	static void setUpFta_L4() throws Exception {
		SimpleFTARuleSet<RankedSymbol,State,EasyFTARule> rules = new SimpleFTARuleSet<RankedSymbol,State,EasyFTARule>();
		rules.add(new EasyFTARule(alphabet.get("a"), states.get("qa")));
		rules.add(new EasyFTARule(alphabet.get("b"), states.get("qa")));
		// f may get each pair of states except (qc,qgf) and (qgf,qc)
		rules.add(new EasyFTARule(alphabet.get("f"),states.get("qf"), states.get("qa"),states.get("qa")));

		// tree automaton
		testAutom.put("fta_L4", new EasyFTA(rules, states.get("qf")));
		testAutom.get("fta_L4").addSymbols(alphabet.values());
		testAutomFinLang.put("fta_L4", true);
		//System.out.println("fta_L4: \n"+TreeAutomataOperations.reduce(fta_L4));
	}

	/**
	 * Set up an finite tree automaton for language L5, called fta_L5. <br>
	 * L5 = {f(a,a),f(a,c),f(c,a),f(c,c)}
	 *
	 * @throws Exception is (hopefully) never thrown
	 */
	static void setUpFta_L5() throws Exception {
		// rules
		SimpleFTARuleSet<RankedSymbol,State,EasyFTARule> rules = new SimpleFTARuleSet<RankedSymbol,State,EasyFTARule>();
		rules.add(new EasyFTARule(alphabet.get("a"), states.get("qa")));
		rules.add(new EasyFTARule(alphabet.get("c"), states.get("qa")));
		// f may get each pair of states
		rules.add(new EasyFTARule(alphabet.get("f"), states.get("qf"), states.get("qa"),states.get("qa")));

		// tree automaton
		testAutom.put("fta_L5", new EasyFTA(rules, states.get("qf")));
		testAutom.get("fta_L5").addSymbols(alphabet.values());
		testAutomFinLang.put("fta_L5", true);
	}

	/**
	 * Set up fta_L6, a finite tree automaton recognizing language L6, called fta_L6. <br>
	 * L6 = {f(a,a)}
	 *
	 * @throws Exception
	 */
	static void setUpFta_L6() throws Exception {
		// rules
		SimpleFTARuleSet<RankedSymbol,State,EasyFTARule> rules = new SimpleFTARuleSet<RankedSymbol,State,EasyFTARule>();
		rules.add(new EasyFTARule(alphabet.get("a"), states.get("qa")));
		// f may get each pair of states except (qc,qgf) and (qgf,qc)
		String[] myStates = {"qa","qf","qbot"};
		for (String p : myStates) {
			for (String q : myStates) {
				// f(qa,qa)->qf, other rules have dest state qbot
				if (p.equals("qa") && q.equals("qa"))
					rules.add(new EasyFTARule(alphabet.get("f"), states.get("qf"), states.get(p), states.get(q)));
				else
					rules.add(new EasyFTARule(alphabet.get("f"), states.get("qbot"), states.get(p), states.get(q)));
			}
		}
		// tree automaton
		testAutom.put("fta_L6", new EasyFTA(rules, states.get("qf")));
		testAutomFinLang.put("fta_L6", true);
	}

	/**
	 * Sets up fta_L7, recognizing the language L7 = {f(a,b),f(b,a),f(b,b)}, called fta_L7.
	 *
	 * @throws Exception
	 */
	static void setUpFta_L7() throws Exception {
		// rules
		SimpleFTARuleSet<RankedSymbol,State,EasyFTARule> rules = new SimpleFTARuleSet<RankedSymbol,State,EasyFTARule>();
		rules.add(new EasyFTARule(alphabet.get("a"), states.get("qa")));
		rules.add(new EasyFTARule(alphabet.get("b"), states.get("qb")));
		rules.add(new EasyFTARule(alphabet.get("f"), states.get("qbot"), states.get("qa"), states.get("qa")));
		rules.add(new EasyFTARule(alphabet.get("f"), states.get("qf"), states.get("qa"), states.get("qb")));
		rules.add(new EasyFTARule(alphabet.get("f"), states.get("qf"), states.get("qb"), states.get("qa")));
		rules.add(new EasyFTARule(alphabet.get("f"), states.get("qf"), states.get("qb"), states.get("qb")));
		// tree automaton
		testAutom.put("fta_L7",new EasyFTA(rules, states.get("qf")));
		testAutom.get("fta_L7").addSymbols(alphabet.values());
		testAutomFinLang.put("fta_L7", true);
	}

	/**
	 * Set up fta_Martin, which recognizes the language {f(b,b)}.
	 *
	 * @throws Exception is (hopefully) never thrown
	 */
	static void setUpFtaMartin() throws Exception {
		// rules
		SimpleFTARuleSet<RankedSymbol,State,EasyFTARule> rules = new SimpleFTARuleSet<RankedSymbol,State,EasyFTARule>();
		rules.add(new EasyFTARule(alphabet.get("b"), states.get("qb")));
		rules.add(new EasyFTARule(alphabet.get("f"), states.get("qf"), states.get("qb"), states.get("qb")));

		// tree automaton
		testAutom.put("fta_Martin", new EasyFTA(rules, states.get("qf")));
		testAutom.get("fta_Martin").addSymbols(alphabet.values());
		testAutomFinLang.put("fta_Martin", true);
	}

	/**
	 * Set up fta_Doro, which recognizes the language {b} and fta_Irene,
	 * which recognizes the language {a}.
	 *
	 * @throws Exception is (hopefully) never thrown
	 */
	static void setUpFtaDoroIrene() throws Exception {
		// create fta_Doro
		List<EasyFTARule> rulesDoro = new LinkedList<EasyFTARule>();
		rulesDoro.add(new EasyFTARule(alphabet.get("b"), states.get("qb")));

		// tree automaton
		testAutom.put("fta_Doro", new EasyFTA(rulesDoro, states.get("qb")));
		testAutom.get("fta_Doro").addSymbols(alphabet.values());
		testAutomFinLang.put("fta_Doro", true);

		// create fta_Irene
		List<EasyFTARule> rulesIrene = new LinkedList<EasyFTARule>();
		rulesIrene.add(new EasyFTARule(alphabet.get("a"), states.get("qa")));

		// tree automaton
		testAutom.put("fta_Irene",new EasyFTA(rulesIrene, states.get("qa")));
		testAutom.get("fta_Irene").addSymbols(alphabet.values());
		testAutomFinLang.put("fta_Irene", true);

	}

	/**
	 * Sets up fta_even, which recognizes the language Leven = {h^n(a): n even}
	 * and fta_odd, which recognizes the language Lodd = {h^n(a): n odd}.
	 *
	 * @throws Exception is (hopefully) never thrown
	 */
	static void setUpFtaEvenOdd() throws Exception {
		// rules
		SimpleFTARuleSet<RankedSymbol,State,EasyFTARule> rules = new SimpleFTARuleSet<RankedSymbol,State,EasyFTARule>();
		rules.add(new EasyFTARule(alphabet.get("a"), states.get("q_ger")));
		rules.add(new EasyFTARule(alphabet.get("h"), states.get("q_ung"), states.get("q_ger")));
		rules.add(new EasyFTARule(alphabet.get("h"), states.get("q_ger"), states.get("q_ung")));
		// tree automaton
		testAutom.put("fta_even",new EasyFTA(rules, states.get("q_ger")));
		testAutom.put("fta_odd", new EasyFTA(rules, states.get("q_ung")));
		testAutomFinLang.put("fta_even", false);
		testAutomFinLang.put("fta_odd", false);
	}


	/**
	 * Sets up fta_var, which recognizes the language which is gained by substituting
	 * L6 and L4 in the varTree. <br>
	 * L = {g(f(a,a),a,f(a,a)),g(f(a,a),a,f(a,b)), g(f(a,a),a,f(b,a)), g(f(a,a),a,f(b,b))}
	 *
	 * @throws Exception
	 */
	static void setUpFta_Var() throws Exception {
		// rules
		SimpleFTARuleSet<RankedSymbol,State,EasyFTARule> rules = new SimpleFTARuleSet<RankedSymbol,State,EasyFTARule>();
		rules.add(new EasyFTARule(alphabet.get("a"), states.get("q2")));
		rules.add(new EasyFTARule(alphabet.get("b"), states.get("q2")));
		rules.add(new EasyFTARule(alphabet.get("a"), states.get("qa")));
		rules.add(new EasyFTARule(alphabet.get("f"), states.get("q1"), states.get("q2"),states.get("q2")));
		rules.add(new EasyFTARule(alphabet.get("f"), states.get("qf"), states.get("qa"),states.get("qa")));
		rules.add(new EasyFTARule(alphabet.get("g"), states.get("qg"), states.get("qf"),states.get("qa"),states.get("q1")));

		// tree automaton
		testAutom.put("fta_var", new EasyFTA(rules, states.get("qg")));
		testAutomFinLang.put("fta_var", true);
	}


	/**
	 * Sets up ftaEpsFA {@link FTACreator#createFTA} to check the method
	 * for eliminating epsilon rules.<br>
	 *
	 * Rules: <br>
	 * <ul>
	 * <li>  p1 -> q </li>
	 * <li> a -> p1 </li>
	 * <li> p2 -> q </li>
	 * <li> q -> p20 </li>
	 * <li> d(p20)->p2 </li>
	 * </ul>
	 * and q is the only final state.<br>.
	 *
	 * @throws Exception is (hopefully) never thrown.
	 */
	static void setUpEpsFTA() throws Exception {

		HashSet<EasyFTARule> rules = new HashSet<EasyFTARule>();
		HashSet<EasyFTAEpsRule> epsRules = new HashSet<EasyFTAEpsRule>();

		epsRules.add(new EasyFTAEpsRule(states.get("q1"), states.get("qf")));
		rules.add(new EasyFTARule(alphabet.get("a"), states.get("q1")));
		epsRules.add(new EasyFTAEpsRule(states.get("q2"), states.get("qf")));
		epsRules.add(new EasyFTAEpsRule(states.get("qf"), states.get("qa")));

		rules.add(new EasyFTARule(alphabet.get("d"), states.get("pq"), states.get("qa")));

		testAutom.put("epsAutom", new EasyFTA(rules,epsRules, new HashSet<State>(Util.makeList(states.get("q")))));
	}


	/**
	 * Sets up some bigger random finite tree automata and some trees over the same alphabet.
	 *
	 * @throws Exception
	 */
	static void setUpRandom() throws Exception{
		//RandomFTAGenerator(int states,int symbols,int arity,int rules,int finals);
		RandomFTAGenerator rand = new RandomFTAGenerator(7,7,2,30,2);
		System.out.println("Producing random automata ...");
		for (int i=0; i<3; i++){
			testAutom.put("random"+i, rand.generateRaw());
		}
		for (int i=0; i<5; i++){
			testAutom.put("reduced"+i, rand.generateReduced());
		}

		//produce random trees
		System.out.println("Producing random trees ...");
		RandomTestCases random = new RandomTestCases();
		for (int i=0; i<10; i++){
			testTrees.put("random"+i, random.randomTree(rand.getSymbols(), 10));
		}
	}

	/**
	 * Sets up all used things.
	 */
	@BeforeClass
	public static void setUp() {
		try {
			setUpAlphabet();
			setUpStates();
			setUpTrees();
			setUpEmptyAutomaton();
			setUpNonDetAutomaton();
			setUpFta_L0();
			setUpFta_L1_1();
			setUpFta_L1_2();
			setUpFta_L2();
			setUpFta_L3();
			setUpFta_L4();
			setUpFta_L5();
			setUpFta_L6();
			setUpFta_L7();
			setUpFta_Var();
			setUpFtaMartin();
			setUpFtaDoroIrene();
			setUpFtaEvenOdd();
			setUpReallyNonDetAutomaton(nondet_index);
			setUpRandom();
			setUpAssertions();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test method for {@link EasyFTAOps#complement}.<br>
	 * Tests whether intersection with the complement automaton is empty and whether complement of complement is the automaton itself.
	 */
	@Test
	public void testComplement() {
		for (EasyFTA A: testAutom.values()) {
			System.out.println("Producing complement of:"+A);
			// The intersection of A and the complement of A should have empty language.
			EasyFTA cA = EasyFTAOps.complement(A);
			EasyFTA iAcA = EasyFTAOps.intersectionTD(A, cA);
			EasyFTA riAcA = EasyFTAOps.reduceBottomUp(iAcA);
			Assert.assertTrue("The intersection of A and the complement of A should have empty language", riAcA.getFinalStates().isEmpty());

			// A should have the same language as the complement of the complement of A.
			EasyFTA ccA = EasyFTAOps.complement(cA);
			Assert.assertTrue("A should have the same language as the complement of the complement of A.", FTAProperties.sameLanguage(A, ccA));

		}
		// test complement of empty and full automata
		Assert.assertTrue("The complement of an empty automaton should accept the language given by all trees consisting of symbols of its alphabet.",
				FTAProperties.sameLanguage(EasyFTAOps.complement(testAutom.get("empty")),
						EasyFTAOps.computeAlphabetFTA(testAutom.get("empty").getAlphabet())));
		Assert.assertTrue("The complement of an empty language automaton should accept the language given by all trees consisting of symbols of its alphabet.",
				FTAProperties.sameLanguage(EasyFTAOps.complement(testAutom.get("pseudo-empty")),
						EasyFTAOps.computeAlphabetFTA(testAutom.get("pseudo-empty").getAlphabet())));
		Assert.assertTrue("The complement of an full automaton should be an empty one.",
				FTAProperties.sameLanguage(EasyFTAOps.complement(EasyFTAOps.computeAlphabetFTA(testAutom.get("pseudo-empty").getAlphabet())),
						testAutom.get("empty")));
		// test some special cases
		Assert.assertTrue("f(a,a) should be contained in the complement of L7",
				FTAProperties.decide(EasyFTAOps.complement(testAutom.get("fta_L7")),
						testTrees.get("faa")));
		Assert.assertFalse("f(b,b) should not be contained in the complement of L7",
				FTAProperties.decide(EasyFTAOps.complement(testAutom.get("fta_L7")),
						testTrees.get("fbb")));
		Assert.assertTrue("a should be contained in the complement of L7",
				FTAProperties.decide(EasyFTAOps.complement(testAutom.get("fta_L7")),
						testTrees.get("treeA")));
	}

	/**
	 * Test method for {@link EasyFTAOps#intersectionTD}.<br>
	 * Tests whether intersection is symmetric and always the subset of the given automata. Intersection with the empty automaton must be empty.
	 */
	@Test
	public void testIntersection() {
		for (String A1Name: testAutom.keySet()){
			EasyFTA A1 = testAutom.get(A1Name);
			for (String A2Name: testAutom.keySet()) {
				EasyFTA A2 = testAutom.get(A2Name);
				EasyFTA i12 = EasyFTAOps.intersectionTD(A1,A2);

				Assert.assertTrue(A1Name+"/"+A2Name+": Intersection must always be the subset of the operand languages",
						FTAProperties.subsetLanguage(i12, A1));
				Assert.assertTrue(A1Name+"/"+A2Name+": Intersection must always be the subset of the operand languages",
						FTAProperties.subsetLanguage(i12, A2));

				Assert.assertTrue(A1Name+"/"+A2Name+": Intersection is symmetric.",
						FTAProperties.sameLanguage(i12, EasyFTAOps.intersectionTD(A2, A1)));
				for (Tree<RankedSymbol> t: testTrees.values()){
					if (FTAProperties.decide(A1, t)&&FTAProperties.decide(A2, t)){
						Assert.assertTrue(FTAProperties.decide(i12, t));
					} else {
						Assert.assertFalse(FTAProperties.decide(i12, t));
					}
				}

			}
			// intersection with the complement must be empty
			Assert.assertTrue(A1Name+": Intersection with the complement must be empty",
					FTAProperties.emptyLanguage(EasyFTAOps.intersectionTD(EasyFTAOps.complement(A1),A1)));
			// intersection with the empty automaton must be empty
			Assert.assertTrue(A1Name+": Intersection with an emtpy language must be empty",
					FTAProperties.emptyLanguage(EasyFTAOps.intersectionTD(testAutom.get("empty"),A1)));
			Assert.assertTrue(A1Name+": Intersection with an emtpy language must be empty",
					FTAProperties.emptyLanguage(EasyFTAOps.intersectionTD(testAutom.get("pseudo-empty"),A1)));
		}
		// some special cases
		// L1 cap L3 = L0
		// L4 cap L5 = L6
		// are testet in the testAssertionFunction

		Assert.assertTrue(EasyFTAOps.intersectionTD(testAutom.get("fta_L4"), testAutom.get("fta_L5")).decide(
				testTrees.get("faa")));
		Assert.assertFalse(EasyFTAOps.intersectionTD(testAutom.get("fta_L4"), testAutom.get("fta_L5")).decide(
				testTrees.get("fbb")));

		Assert.assertTrue(FTAProperties.sameLanguage(testAutom.get("fta_L2"),
				EasyFTAOps.intersectionTD(testAutom.get("fta_L2"), testAutom.get("fta_L1_1"))));

	}

	/**
	 * Test method for {@link EasyFTAOps#union}.<br>
	 * Test whether the union is symmetric and contains the language of both finite tree automata, once
	 * with using the method sameLanguage() and once using test trees. Furthermore it is checked,
	 * whether the union of an automaton with its complement is the whole language and whether the union
	 * with an empty automaton does not change the language.
	 */
	@Test
	public void testUnion() {
		for (EasyFTA A1: testAutom.values()){
			for (EasyFTA A2: testAutom.values()) {
				EasyFTA union12 = EasyFTAOps.union(A1, A2);
				//int result = FTAProperties.checkUnion(union12,A1,A2);
				//Assert.assertEquals(A1.getName()+"/"+A2.getName(), 0, result);
				Assert.assertTrue(FTAProperties.subsetLanguage(A1, union12));
				Assert.assertTrue(FTAProperties.subsetLanguage(A2, union12));
				Assert.assertTrue("Union is symmetric.",
						FTAProperties.sameLanguage(union12, EasyFTAOps.union(A2, A1)));
				// logical test for trees
				for (Tree<RankedSymbol> t: testTrees.values()){
					if (FTAProperties.decide(A1, t)||FTAProperties.decide(A2, t)){
						Assert.assertTrue(FTAProperties.decide(union12, t));
					} else {
						Assert.assertFalse(FTAProperties.decide(union12, t));
					}
				}
			}
			Assert.assertTrue("Union with the complement must be the whole language.",
					FTAProperties.sameLanguage(EasyFTAOps.union(EasyFTAOps.complement(A1),A1),
							EasyFTAOps.computeAlphabetFTA(A1.getAlphabet())));
			Assert.assertTrue("Union with an emtpy language must be origin language.",
					FTAProperties.sameLanguage(EasyFTAOps.union(testAutom.get("empty"),A1),A1));
			Assert.assertTrue("Union with an emtpy language must be origin language.",
					FTAProperties.sameLanguage(EasyFTAOps.union(testAutom.get("pseudo-empty"),A1),A1));
		}
		//some specific tests are done in the assertions.
	}


	/**
	 * Tests {@link EasyFTAOps#difference}.<br>
	 * Some special cases are tested in {@link StdFTAOpTest#testAssertions}.<br>
	 * Tests whether the difference with an emtpy automaton does not change the language.
	 * Tests with trees whether if a tree is accepted by the difference automaton it is not
	 * accepted by the second automaton, but accepted by the first.
	 */
	@Test
	public void testDifference() {
		for (String name: testAutom.keySet()){
			// difference with empty automaton
			Assert.assertTrue(name + " - emptyLanguage: " , FTAProperties.sameLanguage(testAutom.get(name),
					EasyFTAOps.difference(testAutom.get(name),testAutom.get("empty"))));
			Assert.assertTrue(name + " - emptyLanguage: " , FTAProperties.sameLanguage(
					EasyFTAOps.difference(testAutom.get(name),testAutom.get("pseudo-empty")),testAutom.get(name)));
			Assert.assertTrue("EmptyLanguage - " + name, FTAProperties.emptyLanguage(
					EasyFTAOps.difference(testAutom.get("empty"),testAutom.get(name))));
			Assert.assertTrue("EmptyLanguage - " + name, FTAProperties.emptyLanguage(
					EasyFTAOps.difference(testAutom.get("pseudo-empty"),testAutom.get(name))));
			// difference with other automata
			for (String name2:testAutom.keySet()){
				EasyFTA dif = EasyFTAOps.difference(testAutom.get(name), testAutom.get(name2));
				Assert.assertTrue("Subset: " + name + name2, FTAProperties.subsetLanguage(dif, testAutom.get(name)));
				if (FTAProperties.subsetLanguage(testAutom.get(name), testAutom.get(name2))){
					Assert.assertTrue(FTAProperties.emptyLanguage(dif));
				}
				// test with trees
				for (Tree<RankedSymbol> t: testTrees.values()){
					if (dif.decide(t)){
						Assert.assertTrue("Tree in " + name, testAutom.get(name).decide(t) );
						Assert.assertFalse("Tree not in " + name2,testAutom.get(name2).decide(t));
					}
				}
			}
		}
	}

	/**
	 * Test method for {@link EasyFTAOps#determinize}.<br>
	 *
	 * The result finite tree automaton should have the same language as the given one
	 * and be deterministic.
	 */
	@Test
	public void testDeterminize() {
		EasyFTA D = EasyFTAOps.determinize(testAutom.get("fta_reallynondet"));
		Assert.assertEquals(D.getStates().size(),1<<(1+nondet_index));
		Assert.assertTrue(FTAProperties.sameLanguage(D, testAutom.get("fta_reallynondet")));
		Assert.assertTrue(FTAProperties.checkDeterministic(D));
		for (EasyFTA A: testAutom.values()) {
			if (!A.equals(testAutom.get("fta_reallynondet")))
				continue;
			EasyFTA dA = EasyFTAOps.determinize(A);
			Assert.assertTrue("Must be deterministic.",FTAProperties.checkDeterministic(dA));
			Assert.assertTrue("Must have same language.",FTAProperties.sameLanguage(A, dA));
		}
	}

	/**
	 * Test method for {@link EasyFTAOps#complete} and
	 * {@link FTAProperties#checkComplete}. <br>
	 *
	 * The result finite tree automaton should have the same language as the given one
	 * and be complete.
	 */
	@Test
	public void testComplete() {
		// test check complete
		Assert.assertTrue(FTAProperties.checkComplete(testAutom.get("fta_L6")));
		Assert.assertFalse(FTAProperties.checkComplete(testAutom.get("fta_L1_2")));
		// test complete
		for (EasyFTA A: testAutom.values()) {
			EasyFTA compA = EasyFTAOps.complete(A);
			Assert.assertTrue("compA must have same language.",FTAProperties.sameLanguage(A, compA));
			Assert.assertTrue("compA must be complete.", FTAProperties.checkComplete(compA));
		}
		//Assert.assertEquals(testAutom.get("fta_L6").getRules(),
		//	EasyFTAOps.complete(testAutom.get("fta_L6")).getRules());
	}


	/**
	 * Test method for {@link EasyFTAOps#minimize}. <br>
	 *
	 * The result finite tree automaton should have the same language as the given one and
	 * have minimal size of states after determinizing and completing.<br>
	 * An error might be due to complete, reduce or determinize.
	 */
	@Test
	public void testMinimize() {
		for (EasyFTA A: testAutom.values()){
			EasyFTA detA = EasyFTAOps.determinize(A);
			EasyFTA minA = EasyFTAOps.minimize(detA);
			Assert.assertTrue(FTAProperties.sameLanguage(A,minA));
			//one state extra for completing
			Assert.assertTrue(minA.getStates().size() <= detA.getStates().size()+1);
		}
	}

	/**
	 * Test method for {@link EasyFTAOps#reduceBottomUp} and
	 * {@link EasyFTAOps#reduceTopDown} . <br>
	 *
	 * By reducing the language must not change, we test this which different ways of reducing.
	 * The second property of reducing is that the count of the rules must decrise.
	 */
	@Test
	public void testReduce() {
		for (EasyFTA A: testAutom.values()){
			Assert.assertTrue(FTAProperties.sameLanguage(A, EasyFTAOps.reduceTopDown(A)));
			Assert.assertTrue(FTAProperties.sameLanguage(A, EasyFTAOps.reduceBottomUp(A)));
			Assert.assertTrue(EasyFTAOps.reduceBottomUp(A).getRules().size() <= A.getRules().size());
			Assert.assertTrue(EasyFTAOps.reduceTopDown(A).getRules().size() <= A.getRules().size());
			EasyFTA redBuA = EasyFTAOps.reduceBottomUp(A);
			for (State q: redBuA.getStates()) {
				Assert.assertNotNull(FTAOps.constructTreeAcceptedInState(A, new StdTreeCreator<RankedSymbol>(), q,0, true));
			}
		}
	}

	/**
	 * Test method for {@link FTAProperties#sameLanguage}. <br>
	 * Tests the reflexivity, symmetry and transitivity of the sameLanguage-relation. If two automata accept
	 * the same language, this is examined by all test trees.<br>
	 * An error might be due to subsetLanguage. <br>
	 * Some special cases are found in {@link StdFTAOpTest#setUpAssertions()}.
	 */
	@Test
	public void testSameLanguage() {
		for (EasyFTA A: testAutom.values()) {
			EasyFTA cA = EasyFTAOps.complement(A);
			Assert.assertTrue("reflexivity", FTAProperties.sameLanguage(A,A));
			Assert.assertFalse("complement",!A.getAlphabet().isEmpty() && FTAProperties.sameLanguage(A,cA));
			for (EasyFTA B: testAutom.values()) {
				boolean sameAB = FTAProperties.sameLanguage(A,B);
				boolean sameBA = FTAProperties.sameLanguage(B,A);
				Assert.assertEquals("symmetry", sameAB, sameBA);
				if (sameAB){
					for (Tree<RankedSymbol> t: testTrees.values()){
						Assert.assertEquals(FTAProperties.decide(A, t),FTAProperties.decide(B, t));
					}
				}
				for (EasyFTA C: testAutom.values()) {
					boolean sameBC = FTAProperties.sameLanguage(B,C);
					boolean sameAC = FTAProperties.sameLanguage(A,C);
					if (sameAB && sameBC)
						Assert.assertTrue("transitivity",sameAC);
				}
			}
		}
	}

	/**
	 * Test method for {@link FTAProperties#subsetLanguage}. <br>
	 * We test the reflexivity and transitivity of the relation and implications on trees.
	 * Some special cases are found in StdFTAOpTest#setUpAssertions() <br>
	 * An error might be due to complement, intersection oder emptyLanguage
	 * the failure trace indicates, that intersection is an problem,
	 * so repair intersection first!
	 */
	@Test
	public void testSubsetLanguage() {
		EasyFTA A1,A2,A3;
		for (String nameA1: testAutom.keySet()) {
			A1 = testAutom.get(nameA1);
			Assert.assertTrue("Reflexivity " + nameA1,FTAProperties.subsetLanguage(A1, A1));
			for (String nameA2: testAutom.keySet()) {
				A2 = testAutom.get(nameA2);
				boolean subset12 = FTAProperties.subsetLanguage(A1, A2);
				if (subset12){
					for (Tree<RankedSymbol> t: testTrees.values()){
						if (FTAProperties.decide(A1, t))
							Assert.assertTrue("Implications on trees: ",FTAProperties.decide(A2, t));
					}
				}
				for (String nameA3: testAutom.keySet()) {
					A3 = testAutom.get(nameA3);
					boolean subset23 = FTAProperties.subsetLanguage(A2, A3);
					boolean subset13 = FTAProperties.subsetLanguage(A1, A3);
					Assert.assertFalse("Transitivity: " + nameA1+"/"+nameA2+"/"+nameA3, subset12&&subset23&&!subset13);
				}
			}
		}
	}

	/**
	 * Test method for {@link FTAProperties#emptyLanguage}. <br>
	 * Tests whether the language is empty for some empty and non empty automata.
	 */
	@Test
	public void testEmptyLanguage() {
		Assert.assertFalse("fta_L2 has empty language, but should'nt.",
				FTAProperties.emptyLanguage(testAutom.get("fta_L2")));
		Assert.assertTrue("fta_L0 hasn't empty language, but should.",
				FTAProperties.emptyLanguage(testAutom.get("fta_L0")));
		Assert.assertFalse("fta_L1_2 has empty language, but should'nt.",
				FTAProperties.emptyLanguage(testAutom.get("fta_L1_2")));
		Assert.assertTrue("Empty automaton: ",
				FTAProperties.emptyLanguage(testAutom.get("empty")));
		Assert.assertTrue("Empty automaton: ",
				FTAProperties.emptyLanguage(testAutom.get("pseudo-empty")));
		Assert.assertFalse("Non deterministic automaton: ",
				FTAProperties.emptyLanguage(testAutom.get("fta_reallynondet")));
	}


	/**
	 * Test method for {@link FTAProperties#finiteLanguage}. <br>
	 *
	 * Tests for every automaton if the given language is finite. This is stored for the self-created automata.
	 */
	@Test
	public void testFiniteLanguage() {
		for (String name: testAutom.keySet()) {
			//System.out.print(name+": ");
			boolean finLang = FTAProperties.finiteLanguage(testAutom.get(name));
			//System.out.println((finLang?"finite":"infinite"));
			if (testAutomFinLang.containsKey(name))
				Assert.assertEquals(name, testAutomFinLang.get(name),finLang);
			Set<Tree<RankedSymbol>> trees = new HashSet<Tree<RankedSymbol>>();
			if (finLang) {
				//System.out.print("Trees:");
				RegularTreeLanguage<RankedSymbol> L = new RegularTreeLanguage<RankedSymbol>(testAutom.get(name));
				while (!L.isEmpty()) {
					//System.out.println(L);
					Tree<RankedSymbol> next = L.constructWitness();
					Assert.assertTrue(L.contains(next));
					Assert.assertFalse(trees.contains(next));
					trees.add(next);
					//System.out.println(next);
					L.removeTree(next);
					Assert.assertFalse(L.contains(next));
				}
				//	System.out.println(trees);
			}
		}
	}


	/**
	 * Tests {@link FTAProperties#decide} with some trees and some automata. <br>
	 *
	 */
	@Test
	public void testDecide() {

		//test some normal input cases
		Assert.assertTrue(testAutom.get("fta_L2").decide(testTrees.get("treeC")));
		Assert.assertFalse(testAutom.get("fta_L2").decide(testTrees.get("treeA")));
		Assert.assertTrue(testAutom.get("fta_L2").decide(testTrees.get("tree_L2_1")));
		Assert.assertFalse(testAutom.get("fta_L2").decide(testTrees.get("tree_L2_2")));

		Assert.assertEquals(testTrees.get("tree_L2_3").toString(), "f(g(a,f(b,c),a),b)");
		//Assert.assertTrue(testAutom.get("fta_L2").decide(testTrees.get("tree_L2_3")));

		Assert.assertTrue(testAutom.get("fta_L1_1").decide(testTrees.get("treeC")));
		Assert.assertTrue(testAutom.get("fta_L1_1").decide(testTrees.get("treeA")));
		Assert.assertTrue(testAutom.get("fta_L1_1").decide(testTrees.get("tree_L2_1")));
		Assert.assertTrue(testAutom.get("fta_L1_1").decide(testTrees.get("tree_L2_2")));
		Assert.assertTrue(testAutom.get("fta_L1_1").decide(testTrees.get("tree_L2_3")));

		Assert.assertTrue(testAutom.get("fta_L1_2").decide(testTrees.get("treeC")));
		Assert.assertTrue(testAutom.get("fta_L1_2").decide(testTrees.get("treeA")));
		Assert.assertTrue(testAutom.get("fta_L1_2").decide(testTrees.get("tree_L2_1")));
		Assert.assertTrue(testAutom.get("fta_L1_2").decide(testTrees.get("tree_L2_2")));
		Assert.assertTrue(testAutom.get("fta_L1_2").decide(testTrees.get("tree_L2_3")));

		Map<Tree<RankedSymbol>,Set<State>> accStates =
			testAutom.get("fta_L1_1").annotateTreeWithStates(testTrees.get("tree_L2_2"));
		Stack<Tree<RankedSymbol>> toDo = new Stack<Tree<RankedSymbol>>();
		toDo.add(testTrees.get("tree_L2_2"));
		while (!toDo.isEmpty()) {
			Tree<RankedSymbol> t = toDo.pop();
			Assert.assertTrue(t.toString(),accStates.containsKey(t));
			for (Tree<RankedSymbol> s: t.getSubTrees())
				toDo.push(s);
		}

		EasyFTA fta = EasyFTAOps.computeAlphabetFTA(alphabet.values());
		//empty automata should not accept anything
		for (Tree<RankedSymbol> t: testTrees.values()){
			Assert.assertFalse("Empty automata should not accept anything",
					testAutom.get("empty").decide(t));
			Assert.assertFalse("Empty automata should not accept anything.",
					testAutom.get("pseudo-empty").decide(t));

			if (fta.getAlphabet().containsAll(TreeOps.getAllContainedSymbols(t)))
				Assert.assertTrue("AlphabetFTA should accept everything, but did not accept "+t,fta.decide(t));
		}

	}


	/**
	 * Tests {@link EasyFTAOps#constructTreeFrom the tree construction algorithm}.<br>
	 * Constructs a witness for all test automata and checks if it is in the language of the automaton.
	 */
	@Test
	public void testConstructTree() {
		Tree<RankedSymbol> t = EasyFTAOps.constructTreeFrom(testAutom.get("fta_L1_1"));
		Assert.assertNotNull(t);
		Assert.assertTrue(testAutom.get("fta_L1_1").decide(t));
		EasyFTA fta_t = EasyFTAOps.computeSingletonFTA(t);
		EasyFTA fta0 = EasyFTAOps.difference(testAutom.get("fta_L1_1"), fta_t);
		Assert.assertFalse(fta0.decide(t));
		Tree<RankedSymbol> t2 = EasyFTAOps.constructTreeFrom(fta0);
		Assert.assertNotNull(t2);
		Assert.assertTrue(testAutom.get("fta_L1_1").decide(t2));
		Assert.assertTrue(testAutom.get("fta_L1_2").decide(t2));

		for (EasyFTA A : testAutom.values()){
			Tree<RankedSymbol> tA = EasyFTAOps.constructTreeFrom(A);
			if (tA != null)
				Assert.assertTrue(FTAProperties.decide(A, tA));
		}
	}

	/**
	 * Tests the {@link EasyFTAOps#substitute substitution method}. <br>
	 * Cases we test:
	 * <ul>
	 * <li> Empty map as substitution map </li>
	 * <li> Symbols in the map with arity > 0 </li>
	 * <li> Replacing a constant in a tree that consists exactly of this constant by any automaton </li>
	 * <li> Replacing constants by automata which have empty language </li>
	 * <li> Two normal test cases </li>
	 * </ul>
	 */
	@Test
	public void testSubstitution(){
		// test what happens if the map is empty
		HashMap<RankedSymbol,EasyFTA> languages = new HashMap<RankedSymbol,EasyFTA>();
		for (String s: testTrees.keySet()){
			Assert.assertTrue("For an empty map substitution is the same as construct an automaton which recognises exactly the given tree.",
					FTAProperties.sameLanguage(EasyFTAOps.computeSingletonFTA(testTrees.get(s)),
							EasyFTAOps.substitute(testTrees.get(s), languages)));
		}
		// what happens if in the map are symbols which have an arity > 0?
		try{
			languages.put(alphabet.get("f"), testAutom.get("fta_L0"));
			EasyFTAOps.substitute(testTrees.get("hc"), languages);
			Assert.assertFalse(true);
		} catch (IllegalArgumentException e){
			Assert.assertTrue(true);
		}

		// replacing in a tree which consists of only one constant shall do nothing
		for (String s: testAutom.keySet()){
			languages.clear();
			languages.put(alphabet.get("b"), testAutom.get(s));
			Assert.assertTrue("Replacing in a tree which consists of only one constant shall do nothing.",
					FTAProperties.sameLanguage(testAutom.get(s),
							EasyFTAOps.substitute(testTrees.get("treeB"), languages)));
		}

		/*
		 * Replacing a constant by the empty language shall give the empty language, if the constant occurs in the tree,m
		 * and give an automaton that accepts exactly the given tree otherwise
		 */
		languages.clear();
		RankedSymbol c = alphabet.get("c");
		languages.put(c, testAutom.get("empty"));
		HashMap<RankedSymbol,EasyFTA> languages1 = new HashMap<RankedSymbol,EasyFTA>();
		HashMap<RankedSymbol,EasyFTA> languages2 = new HashMap<RankedSymbol,EasyFTA>();
		languages1.put(c, testAutom.get("pseudo-empty"));
		languages2.put(c, testAutom.get("fta_L0"));
		for (String s: testTrees.keySet()){
			Tree<RankedSymbol> t = testTrees.get(s);
			if (TreeOps.containsSymbol(t,c)){
				Assert.assertTrue("Replacing an occuring variable by the empty language shall give the empty language.",
						FTAProperties.emptyLanguage(EasyFTAOps.substitute(t, languages)));
				Assert.assertTrue("Replacing an occuring variable by the empty language shall give the empty language.",
						FTAProperties.emptyLanguage(EasyFTAOps.substitute(t, languages1)));
				Assert.assertTrue("Replacing an occuring variable by the empty language shall give the empty language.",
						FTAProperties.emptyLanguage(EasyFTAOps.substitute(t, languages2)));
			} else {
				Assert.assertTrue("Replacing an object in a tree which does not occur shall give an automaton that accepts exactly the given tree.",
						FTAProperties.sameLanguage(EasyFTAOps.computeSingletonFTA(t),
								EasyFTAOps.substitute(t, languages)));
				Assert.assertTrue("Replacing an object in a tree which does not occur shall give an automaton that accepts exactly the given tree.",
						FTAProperties.sameLanguage(EasyFTAOps.computeSingletonFTA(t),
								EasyFTAOps.substitute(t, languages1)));
				Assert.assertTrue("Replacing an object in a tree which does not occur shall give an automaton that accepts exactly the given tree.",
						FTAProperties.sameLanguage(EasyFTAOps.computeSingletonFTA(t),
								EasyFTAOps.substitute(t, languages2)));

			}
		}

		//test a normal input
		languages.clear();
		languages.put(alphabet.get("c"), testAutom.get("fta_even"));
		EasyFTA subst = EasyFTAOps.substitute(testTrees.get("hc"), languages );
		Assert.assertTrue("Substituting c in h(c) by fta_even shall give fta_odd.",
				FTAProperties.sameLanguage(testAutom.get("fta_odd"),
						subst));
		languages.clear();
		languages.put(alphabet.get("c"), testAutom.get("fta_odd"));
		Assert.assertFalse("Substituting c in h(c) by fta_odd shall not give fta_even.",
				FTAProperties.sameLanguage(testAutom.get("fta_even"),
						EasyFTAOps.substitute(testTrees.get("hc"), languages )));

		Assert.assertTrue("The difference of fta_even and the automaton given by substituting c in h(c) by fta_odd shall accept exactly the tree that consists only of a.",
				FTAProperties.sameLanguage(
						EasyFTAOps.difference(testAutom.get("fta_even"), EasyFTAOps.substitute(testTrees.get("hc"), languages )),
						EasyFTAOps.computeSingletonFTA(testTrees.get("treeA"))));


		//test a second normal input
		languages.clear();
		languages.put(alphabet.get("d"), testAutom.get("fta_L4"));
		languages.put(alphabet.get("c"), testAutom.get("fta_L6"));

		Assert.assertTrue(FTAProperties.decide(testAutom.get("fta_var"),testTrees.get("g_2")));
		Assert.assertTrue(FTAProperties.decide(EasyFTAOps.substitute(
				testTrees.get("g_subst"),languages),testTrees.get("g_2")));

		Assert.assertTrue("Testing a normal input.",FTAProperties.sameLanguage(testAutom.get("fta_var"),
				EasyFTAOps.substitute(testTrees.get("g_subst"), languages )));
	}

	/**
	 * Some random test with some big finite tree automata on the bottom up intersection.
	 */
	@Test
	public void testRandomIntersectionBU() {
		RandomFTAGenerator rfc = new RandomFTAGenerator(1000, 8, 3, 1000, 2);
		int n = 10;
		for (int i=0; i<n; i++) {
			System.out.println("Round "+i+"...");

			System.out.println("creating first automaton...");
			EasyFTA fta1 = rfc.generateRaw();
			System.out.println("creating second automaton...");
			EasyFTA fta2 = rfc.generateRaw();
			System.out.println("first automaton: "+fta1.getStates().size()+" states, "+fta1.getRules().size()+" rules");
			System.out.println("second automaton: "+fta2.getStates().size()+" states, "+fta2.getRules().size()+" rules");
			System.out.println("intersecting...");
			EasyFTA i12 = EasyFTAOps.intersectionBU(fta1,fta2);
			if (!FTAProperties.emptyLanguage(i12)) {
				Tree<RankedSymbol> t = EasyFTAOps.constructTreeFrom(i12);
				Assert.assertTrue(fta1.decide(t));
				Assert.assertTrue(fta2.decide(t));
				System.out.println("intersection is not empty!");
			}

		}
	}



}
