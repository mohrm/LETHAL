/**
 *
 */
package de.uni_muenster.cs.sev.lethal.tests;


import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import de.uni_muenster.cs.sev.lethal.languages.RegularTreeLanguage;
import de.uni_muenster.cs.sev.lethal.states.NamedState;
import de.uni_muenster.cs.sev.lethal.states.State;
import de.uni_muenster.cs.sev.lethal.symbol.common.RankedSymbol;
import de.uni_muenster.cs.sev.lethal.symbol.standard.StdNamedRankedSymbol;
import de.uni_muenster.cs.sev.lethal.tree.common.Tree;
import de.uni_muenster.cs.sev.lethal.treeautomata.common.SimpleFTARuleSet;
import de.uni_muenster.cs.sev.lethal.treeautomata.easy.EasyFTA;
import de.uni_muenster.cs.sev.lethal.treeautomata.easy.EasyFTARule;

/**
 * Tests the usage of {@link RegularTreeLanguage}.
 *
 * @author Martin, Doro, Irene
 *
 */
public class RTLTest {

	/** Used symbols: {a, b, c, d, f(,), g(,,), h()}, represented by their string representation. */
	private static Map<String, RankedSymbol>  alphabet = new HashMap<String, RankedSymbol>();

	/** Used states. */
	private static Map<String, State> states = new HashMap<String, State>();

	/** Used automata. */
	private static Map<String, EasyFTA> testAutom = new HashMap<String,EasyFTA>();


	/**
	 * Sets up the alphabet we use in this test class:<br>
	 * a,b,c,f(,),g(,,),h(),u()
	 *
	 * @throws Exception is (hopefully) never thrown
	 */
	static void setUpAlphabet() throws Exception {
		alphabet.put("a",new StdNamedRankedSymbol<String>("a", 0));
		alphabet.put("h",new StdNamedRankedSymbol<String>("h", 1));
		alphabet.put("u",new StdNamedRankedSymbol<String>("u", 1));
	}


	/**
	 * Sets up fta_even, which recognizes the language Leven = {h^n(a): n even}
	 * and fta_odd, which recognizes the language Lodd = {h^n(a): n odd}.
	 *
	 * @throws Exception is (hopefully) never thrown
	 */
	static void setUpFtaEvenOdd() throws Exception {
		states.put("q_ger", new NamedState<String>("q_ger"));
		states.put("q_ung", new NamedState<String>("q_ung"));
		// rules
		SimpleFTARuleSet<RankedSymbol,State,EasyFTARule> rules = new SimpleFTARuleSet<RankedSymbol,State,EasyFTARule>();
		rules.add(new EasyFTARule(alphabet.get("a"), states.get("q_ger")));
		rules.add(new EasyFTARule(alphabet.get("h"), states.get("q_ung"), states.get("q_ger")));
		rules.add(new EasyFTARule(alphabet.get("h"), states.get("q_ger"), states.get("q_ung")));
		// tree automaton
		testAutom.put("fta_even",new EasyFTA(rules, states.get("q_ger")));
		testAutom.put("fta_odd", new EasyFTA(rules, states.get("q_ung")));
	}


	/**
	 * Sets up an automaton with n+1 states, you can proof that the determinized version
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
	}




	/**
	 * Sets up all needed things.
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		setUpAlphabet();
		setUpFtaEvenOdd();
		setUpReallyNonDetAutomaton(5);
	}



	/**
	 * Tests something with regular tree languages. <br>
	 *
	 * Lists the first n trees of a language by constructing the next
	 * tree, and subtracting the corresponding singleton automaton.
	 */
	@Test
	public void testTreeLanguages() {
		int n = 5; // how many trees shall be listed
		EasyFTA A0 = testAutom.get("fta_reallynondet"); // every fta with at least n trees fits here
		RegularTreeLanguage<RankedSymbol> R = new RegularTreeLanguage<RankedSymbol>(A0);
		Set<Tree<RankedSymbol>> trees = new HashSet<Tree<RankedSymbol>>(); // for checking if a tree was already constructed
		for (int i=0; i<n; i++) {
			Assert.assertFalse(R.isEmpty());
			Tree<RankedSymbol> t0 = R.constructWitness();
			Assert.assertNotNull(t0);
			Assert.assertFalse(trees.contains(t0));
			//System.out.println(t0);
			trees.add(t0);
			R.removeTree(t0);
			//System.out.println(R);
		}
	}


	/**
	 * Tests to produce regular tree languages of automata.
	 */
	@Test
	public void testRegularTreeLanguage() {
		RegularTreeLanguage<RankedSymbol> L = new RegularTreeLanguage<RankedSymbol>(Arrays.asList(alphabet.get("a"),alphabet.get("h")));
		RegularTreeLanguage<RankedSymbol> L1 = new RegularTreeLanguage<RankedSymbol>(testAutom.get("fta_even"));
		RegularTreeLanguage<RankedSymbol> L2 = new RegularTreeLanguage<RankedSymbol>(testAutom.get("fta_odd"));
		L.removeTrees(L1);
		L.removeTrees(L2);
		Assert.assertTrue(L.isEmpty());
	}

}
