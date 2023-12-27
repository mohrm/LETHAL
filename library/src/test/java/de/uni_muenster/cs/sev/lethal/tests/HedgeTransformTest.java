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
package de.uni_muenster.cs.sev.lethal.tests;
/**
 * The class HedgeTransformTest contains the tests
 * for the testing of the HedgeAutomaton.
 */

import de.uni_muenster.cs.sev.lethal.hedgeautomaton.HedgeAutomaton;
import de.uni_muenster.cs.sev.lethal.hedgeautomaton.HedgeRule;
import de.uni_muenster.cs.sev.lethal.hedgeautomaton.HAOps;
import de.uni_muenster.cs.sev.lethal.hedgeautomaton.wordAutomata.WordRule;
import de.uni_muenster.cs.sev.lethal.hedgeautomaton.wordAutomata.WordAutomaton;
import de.uni_muenster.cs.sev.lethal.hedgeautomaton.expressions.BasicExpression;
import de.uni_muenster.cs.sev.lethal.hedgeautomaton.expressions.EmptyExpression;
import de.uni_muenster.cs.sev.lethal.hedgeautomaton.expressions.Expression;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import de.uni_muenster.cs.sev.lethal.states.HedgeState;
import de.uni_muenster.cs.sev.lethal.states.NamedState;
import de.uni_muenster.cs.sev.lethal.states.BiState;
import de.uni_muenster.cs.sev.lethal.symbol.standard.StdNamedUnrankedSymbol;
import de.uni_muenster.cs.sev.lethal.utils.Pair;
import de.uni_muenster.cs.sev.lethal.factories.StdStateFactory;
import de.uni_muenster.cs.sev.lethal.tree.common.Tree;
import de.uni_muenster.cs.sev.lethal.tree.standard.StdTree;

/**
 * @author Anton
 *         tests HedgeTransform
 */
public class HedgeTransformTest extends TestCase {

	/**
	 * @param name standard argument
	 */
	public HedgeTransformTest(String name) {
		super(name);
	}

	/**
	 * @return test suite
	 */
	public static Test suite() {
		return new TestSuite(HedgeTransformTest.class);
	}

	/**
	 * @param args standard argument
	 */
	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	/**
	 * @see TestCase#setUp()
	 */
	@Override
	public void setUp() {
	}

	/**
	 * @see TestCase#tearDown()
	 */
	@Override
	public void tearDown() {
	}

	/**
	 * Makes a hedgeautomaton with the
	 * set of states: {q1},
	 * set of final states: {q1},
	 * and the set of rules: {a() -> q1}. <br>
	 * Makes the complement of this automaton
	 * and the hedge: a(a(nil)).<br>
	 * Checks, whether this hedge is
	 * recognizable by this automaton.
	 */
	@SuppressWarnings("unchecked")
	public void test1() {
		StdNamedUnrankedSymbol<String> a = new StdNamedUnrankedSymbol<String>("a");
		StdStateFactory sf = new StdStateFactory();
		NamedState q1 = sf.makeState("1");
		// set of states of the hedgeautomaton
		Set<NamedState> states = new HashSet<NamedState>();
		states.add(q1);
		// set of final states of the hedgeautomaton
		Set<NamedState> finStates = new HashSet<NamedState>();
		finStates.add(q1);
		// rule: a() -> q1
		HedgeRule<StdNamedUnrankedSymbol, NamedState> r1 = new HedgeRule<StdNamedUnrankedSymbol, NamedState>(a, new EmptyExpression<StdNamedUnrankedSymbol, NamedState>(), q1);
		// set of rules of the hedgeautomaton
		Set<HedgeRule<StdNamedUnrankedSymbol, NamedState>> rules = new HashSet<HedgeRule<StdNamedUnrankedSymbol, NamedState>>();
		rules.add(r1);
		//Namespace n = new Namespace();
		// hedgeautomaton
		HedgeAutomaton<StdNamedUnrankedSymbol, NamedState> autom1 = new HedgeAutomaton<StdNamedUnrankedSymbol, NamedState>(states, finStates, rules);
		// hedge: a(a(nil))
		List<Tree<StdNamedUnrankedSymbol>> list = new LinkedList<Tree<StdNamedUnrankedSymbol>>();
		// a(nil)
		Tree<StdNamedUnrankedSymbol> h1 = new StdTree<StdNamedUnrankedSymbol>(a, list);
		list = new LinkedList<Tree<StdNamedUnrankedSymbol>>();
		list.add(h1);
		// a(a(nil))
		Tree<StdNamedUnrankedSymbol> h2 = new StdTree<StdNamedUnrankedSymbol>(a, list);
		HedgeAutomaton<StdNamedUnrankedSymbol, NamedState<Set<HedgeState<NamedState>>>> ta2 = HAOps.complement(autom1);
		System.out.println(autom1);
		System.out.println(ta2);
		System.out.println(h2);
		assertTrue(HAOps.decide(ta2, h2));
	}

	/**
	 * Makes a wordautomaton wa
	 * with the set of states: {y1},
	 * set of final states: {y1},
	 * set of rules: {q1(y1) -> y1}
	 * and the initial state y1.<br>
	 * Makes 2 hedge automata a1 and a2.
	 * a1:{{q1, q2}, {q2}, {a() -> q1, b(wa) -> q2}}}.
	 * a2:{{q3, q4}, {q4}, {c() -> q3, b(q3*) -> q4}}.<br>
	 * Makes a hedge automaton that recognizes the union
	 * of the languages of a1 and a2.
	 */
	@SuppressWarnings("unchecked")
	public void test2() {
		StdNamedUnrankedSymbol<String> a = new StdNamedUnrankedSymbol<String>("a");
		StdNamedUnrankedSymbol<String> b = new StdNamedUnrankedSymbol<String>("b");
		StdNamedUnrankedSymbol<String> c = new StdNamedUnrankedSymbol<String>("c");
		StdStateFactory sf = new StdStateFactory();
		// states for the hedge automata
		NamedState q1 = sf.makeState("1");
		NamedState q2 = sf.makeState("2");
		NamedState q3 = sf.makeState("3");
		NamedState q4 = sf.makeState("4");
		// state for the word automaton
		NamedState y1 = sf.makeState("11");
		// rule of the word automaton: q1(y1) -> y1
		WordRule<NamedState> wa_rule = new WordRule<NamedState>(q1, y1, y1);
		// set of rules of the word automaton
		Set<WordRule<NamedState>> wa_rules = new HashSet<WordRule<NamedState>>();
		wa_rules.add(wa_rule);
		// set of states of the word automaton
		Set<NamedState> wa_states = new HashSet<NamedState>();
		wa_states.add(y1);
		// word automaton
		WordAutomaton<StdNamedUnrankedSymbol, NamedState> wa = new WordAutomaton<StdNamedUnrankedSymbol, NamedState>(wa_states, wa_rules, y1, wa_states);
		// 1. rule of the 1. hedge automaton: b(wa) -> q2 (wa is the word automaton)
		HedgeRule<StdNamedUnrankedSymbol, NamedState> r1 = new HedgeRule<StdNamedUnrankedSymbol, NamedState>(b, wa, q2);
		// 2. rule of the 1. hedge automaton: a() -> q1
		HedgeRule<StdNamedUnrankedSymbol, NamedState> r2 = new HedgeRule<StdNamedUnrankedSymbol, NamedState>(a, new EmptyExpression<StdNamedUnrankedSymbol, NamedState>(), q1);
		// set of states of the 1. hedge automaton
		Set<NamedState> st = new HashSet<NamedState>();
		st.add(q1);
		st.add(q2);
		// set of rules of the 1. hedge automaton
		Set<HedgeRule<StdNamedUnrankedSymbol, NamedState>> rules = new HashSet<HedgeRule<StdNamedUnrankedSymbol, NamedState>>();
		rules.add(r1);
		rules.add(r2);
		// set of final states of the 1. hedge automaton
		Set<NamedState> finSt = new HashSet<NamedState>();
		finSt.add(q2);
		//Namespace n = new Namespace();
		HedgeAutomaton<StdNamedUnrankedSymbol, NamedState> a1 = new HedgeAutomaton<StdNamedUnrankedSymbol, NamedState>(st, finSt, rules);
		// 1. rule of the 2. hedge automaton
		HedgeRule<StdNamedUnrankedSymbol, NamedState> r3 = new HedgeRule<StdNamedUnrankedSymbol, NamedState>(c, new EmptyExpression<StdNamedUnrankedSymbol, NamedState>(), q3);
		// 2. rule of the 2. hedge automaton
		List<NamedState> base = new LinkedList<NamedState>();
		base.add(q3);
		HedgeRule<StdNamedUnrankedSymbol, NamedState> r4 = new HedgeRule<StdNamedUnrankedSymbol, NamedState>(b, new Expression<StdNamedUnrankedSymbol, NamedState>(0, -1, new BasicExpression<StdNamedUnrankedSymbol, NamedState>(base)), q4);
		// set of states of the 2. hedge automaton
		Set<NamedState> st2 = new HashSet<NamedState>();
		st2.add(q3);
		st2.add(q4);
		// set of rules of the 2. hedge automaton
		Set<HedgeRule<StdNamedUnrankedSymbol, NamedState>> rules2 = new HashSet<HedgeRule<StdNamedUnrankedSymbol, NamedState>>();
		rules2.add(r3);
		rules2.add(r4);
		// set of the final states of the 2. hedge automaton
		Set<NamedState> finSt2 = new HashSet<NamedState>();
		finSt2.add(q4);
		HedgeAutomaton<StdNamedUnrankedSymbol, NamedState> a2 = new HedgeAutomaton<StdNamedUnrankedSymbol, NamedState>(st2, finSt2, rules2);
		HedgeAutomaton<StdNamedUnrankedSymbol, BiState<HedgeState<NamedState>, HedgeState<NamedState>>> a3 = HAOps.union(a1, a2);
		//System.out.println(a3);
	}

	/**
	 * Makes a hedgeautomaton autom1
	 * with the set of states: {q1},
	 * set of final states: {q1},
	 * and the set of rules: {a() -> q1}.<br>
	 * Makes an complete hedge automaton,
	 * which is equivalent to autom1.
	 */
	@SuppressWarnings("unchecked")
	public void test3() {
		StdNamedUnrankedSymbol<String> a = new StdNamedUnrankedSymbol<String>("a");
		StdStateFactory sf = new StdStateFactory();
		// states for the hedge automata
		NamedState q1 = sf.makeState("1");
		// set of states of the hedgeautomaton
		Set<NamedState> states = new HashSet<NamedState>();
		states.add(q1);
		// set of final states of the hedgeautomaton
		Set<NamedState> finStates = new HashSet<NamedState>();
		finStates.add(q1);
		// rule: a() -> q1
		HedgeRule<StdNamedUnrankedSymbol, NamedState> r1 = new HedgeRule<StdNamedUnrankedSymbol, NamedState>(a, new EmptyExpression<StdNamedUnrankedSymbol, NamedState>(), q1);
		// set of rules of the hedgeautomaton
		Set<HedgeRule<StdNamedUnrankedSymbol, NamedState>> rules = new HashSet<HedgeRule<StdNamedUnrankedSymbol, NamedState>>();
		rules.add(r1);
		HedgeAutomaton<StdNamedUnrankedSymbol, NamedState> autom1 = new HedgeAutomaton<StdNamedUnrankedSymbol, NamedState>(states, finStates, rules);
		NamedState q = sf.makeState("2");
		//HedgeAutomaton<StdNamedUnrankedSymbol,NamedState> autom2 = HAOps.complete(autom1, q);
		//System.out.println(autom2);
	}

	/**
	 * Makes a hedgeautomaton with the
	 * set of states: {q1, q2},
	 * set of final states: {q2},
	 * and the set of rules: {a() -> q1, b(q1) -> q1, b(q1) -> q2}.<br>
	 * Makes this automaton deterministic
	 * and makes the hedge: b(b(b(a(nil)))).<br>
	 * Checks, whether this hedge is
	 * recognizable by this automaton.
	 */
	@SuppressWarnings("unchecked")
	public void test4() {
		StdNamedUnrankedSymbol<String> a = new StdNamedUnrankedSymbol<String>("a");
		StdNamedUnrankedSymbol<String> b = new StdNamedUnrankedSymbol<String>("b");
		StdStateFactory sf = new StdStateFactory();
		// states for the hedge automata
		NamedState q1 = sf.makeState("1");
		NamedState q2 = sf.makeState("2");
		// set of states of the hedgeautomaton
		Set<NamedState> states = new HashSet<NamedState>();
		states.add(q1);
		states.add(q2);
		// set of final states of the hedgeautomaton
		Set<NamedState> finStates = new HashSet<NamedState>();
		finStates.add(q2);
		// 1. rule: a() -> q1
		HedgeRule<StdNamedUnrankedSymbol, NamedState> r1 = new HedgeRule<StdNamedUnrankedSymbol, NamedState>(a, new EmptyExpression<StdNamedUnrankedSymbol, NamedState>(), q1);
		// 2. rule: b(q1) -> q1
		List<NamedState> base = new LinkedList<NamedState>();
		base.add(q1);
		Expression<StdNamedUnrankedSymbol, NamedState> exp = new Expression<StdNamedUnrankedSymbol, NamedState>(1, 1, new BasicExpression<StdNamedUnrankedSymbol, NamedState>(base));
		HedgeRule<StdNamedUnrankedSymbol, NamedState> r2 = new HedgeRule<StdNamedUnrankedSymbol, NamedState>(b, exp, q1);
		// 3. rule: b(q1) -> q2
		HedgeRule<StdNamedUnrankedSymbol, NamedState> r3 = new HedgeRule<StdNamedUnrankedSymbol, NamedState>(b, exp, q2);
		// set of rules of the hedgeautomaton
		Set<HedgeRule<StdNamedUnrankedSymbol, NamedState>> rules = new HashSet<HedgeRule<StdNamedUnrankedSymbol, NamedState>>();
		rules.add(r1);
		rules.add(r2);
		rules.add(r3);
		// hedgeautomaton
		HedgeAutomaton<StdNamedUnrankedSymbol, NamedState> autom1 = new HedgeAutomaton<StdNamedUnrankedSymbol, NamedState>(states, finStates, rules);
		//HedgeAutomaton<StdNamedUnrankedSymbol,NamedState<Set<HedgeState<NamedState>>>> ha2 = HAOps.determinize(autom1);
		// hedge: b(b(b(a(nil))))
		List<Tree<StdNamedUnrankedSymbol>> list = new LinkedList<Tree<StdNamedUnrankedSymbol>>();
		// a(nil)
		Tree<StdNamedUnrankedSymbol> h1 = new StdTree<StdNamedUnrankedSymbol>(a, list);
		list = new LinkedList<Tree<StdNamedUnrankedSymbol>>();
		list.add(h1);
		// b(a(nil))
		Tree<StdNamedUnrankedSymbol> h2 = new StdTree<StdNamedUnrankedSymbol>(b, list);
		list = new LinkedList<Tree<StdNamedUnrankedSymbol>>();
		list.add(h2);
		// b(b(a(nil)))
		Tree<StdNamedUnrankedSymbol> h3 = new StdTree<StdNamedUnrankedSymbol>(b, list);
		list = new LinkedList<Tree<StdNamedUnrankedSymbol>>();
		list.add(h3);
		// b(b(b(a(nil))))
		Tree<StdNamedUnrankedSymbol> h4 = new StdTree<StdNamedUnrankedSymbol>(b, list);
		//assertTrue(HAOps.decide(ha2, h4));
		//System.out.println(ha2);
	}

	/**
	 * Makes a wordautomaton wa
	 * with the set of states: {y1},
	 * set of final states: {y1},
	 * set of rules: {q1(y1) -> y1}
	 * and the initial state y1.<br>
	 * Makes 2 hedge automata a1 and a2.
	 * a1:{{q1, q2}, {q2}, {a() -> q1, b(wa) -> q2}}}.
	 * a2:{{q3, q4}, {q4}, {a() -> q3, b(q3^2) -> q4}}.<br>
	 * Makes a hedge automaton that recognizes the intersection
	 * of the languages of a1 and a2.
	 */
	@SuppressWarnings("unchecked")
	public void test5() {
		StdNamedUnrankedSymbol<String> a = new StdNamedUnrankedSymbol<String>("a");
		StdNamedUnrankedSymbol<String> b = new StdNamedUnrankedSymbol<String>("b");
		StdStateFactory sf = new StdStateFactory();
		// states for the hedge automata
		NamedState q1 = sf.makeState("1");
		NamedState q2 = sf.makeState("2");
		NamedState q3 = sf.makeState("3");
		NamedState q4 = sf.makeState("4");
		// state for the word automaton
		NamedState y1 = sf.makeState("11");
		// rule of the word automaton: q1(y1) -> y1
		WordRule<NamedState> wa_rule = new WordRule<NamedState>(q1, y1, y1);
		// set of rules of the word automaton
		Set<WordRule<NamedState>> wa_rules = new HashSet<WordRule<NamedState>>();
		wa_rules.add(wa_rule);
		// set of states of the word automaton
		Set<NamedState> wa_states = new HashSet<NamedState>();
		wa_states.add(y1);
		// word automaton
		WordAutomaton<StdNamedUnrankedSymbol, NamedState> wa = new WordAutomaton<StdNamedUnrankedSymbol, NamedState>(wa_states, wa_rules, y1, wa_states);
		// 1. rule of the 1. hedge automaton: b(wa) -> q2 (wa is the word automaton)
		HedgeRule<StdNamedUnrankedSymbol, NamedState> r1 = new HedgeRule<StdNamedUnrankedSymbol, NamedState>(b, wa, q2);
		// 2. rule of the 1. hedge automaton: a() -> q1
		HedgeRule<StdNamedUnrankedSymbol, NamedState> r2 = new HedgeRule<StdNamedUnrankedSymbol, NamedState>(a, new EmptyExpression<StdNamedUnrankedSymbol, NamedState>(), q1);
		// set of states of the 1. hedge automaton
		Set<NamedState> st = new HashSet<NamedState>();
		st.add(q1);
		st.add(q2);
		// set of rules of the 1. hedge automaton
		Set<HedgeRule<StdNamedUnrankedSymbol, NamedState>> rules = new HashSet<HedgeRule<StdNamedUnrankedSymbol, NamedState>>();
		rules.add(r1);
		rules.add(r2);
		// set of final states of the 1. hedge automaton
		Set<NamedState> finSt = new HashSet<NamedState>();
		finSt.add(q2);
		HedgeAutomaton<StdNamedUnrankedSymbol, NamedState> a1 = new HedgeAutomaton<StdNamedUnrankedSymbol, NamedState>(st, finSt, rules);
		// 1. rule of the 2. hedge automaton
		HedgeRule<StdNamedUnrankedSymbol, NamedState> r3 = new HedgeRule<StdNamedUnrankedSymbol, NamedState>(a, new EmptyExpression<StdNamedUnrankedSymbol, NamedState>(), q3);
		// 2. rule of the 2. hedge automaton
		List<NamedState> base = new LinkedList<NamedState>();
		base.add(q3);
		HedgeRule<StdNamedUnrankedSymbol, NamedState> r4 = new HedgeRule<StdNamedUnrankedSymbol, NamedState>(b, new Expression<StdNamedUnrankedSymbol, NamedState>(2, 2, new BasicExpression<StdNamedUnrankedSymbol, NamedState>(base)), q4);
		// set of states of the 2. hedge automaton
		Set<NamedState> st2 = new HashSet<NamedState>();
		st2.add(q3);
		st2.add(q4);
		// set of rules of the 2. hedge automaton
		Set<HedgeRule<StdNamedUnrankedSymbol, NamedState>> rules2 = new HashSet<HedgeRule<StdNamedUnrankedSymbol, NamedState>>();
		rules2.add(r3);
		rules2.add(r4);
		// set of the final states of the 2. hedge automaton
		Set<NamedState> finSt2 = new HashSet<NamedState>();
		finSt2.add(q4);
		HedgeAutomaton<StdNamedUnrankedSymbol, NamedState> a2 = new HedgeAutomaton<StdNamedUnrankedSymbol, NamedState>(st2, finSt2, rules2);
		HedgeAutomaton<StdNamedUnrankedSymbol, NamedState<Pair<HedgeState<NamedState>, HedgeState<NamedState>>>> a3 = HAOps.intersection(a1, a2);
		//System.out.println(a3);
	}

	/**
	 * Makes a wordautomaton wa
	 * with the set of states: {y1},
	 * set of final states: {y1},
	 * set of rules: {q1(y1) -> y1}
	 * and the initial state y1.<br>
	 * Makes 2 hedge automata a1 and a2.
	 * a1:{{q1, q2}, {q2}, {a() -> q1, b(wa) -> q2}}}.
	 * a2:{{q3, q4}, {q4}, {a() -> q3, b(q3^2) -> q4}}.<br>
	 * Makes a hedge automaton that recognizes the difference
	 * of the languages of a1 and a2.
	 */
	@SuppressWarnings("unchecked")
	public void test6() {
		StdNamedUnrankedSymbol<String> a = new StdNamedUnrankedSymbol<String>("a");
		StdNamedUnrankedSymbol<String> b = new StdNamedUnrankedSymbol<String>("b");
		StdStateFactory sf = new StdStateFactory();
		// states for the hedge automata
		NamedState q1 = sf.makeState("1");
		NamedState q2 = sf.makeState("2");
		NamedState q3 = sf.makeState("3");
		NamedState q4 = sf.makeState("4");
		// state for the word automaton
		NamedState y1 = sf.makeState("11");
		// rule of the word automaton: q1(y1) -> y1
		WordRule<NamedState> wa_rule = new WordRule<NamedState>(q1, y1, y1);
		// set of rules of the word automaton
		Set<WordRule<NamedState>> wa_rules = new HashSet<WordRule<NamedState>>();
		wa_rules.add(wa_rule);
		// set of states of the word automaton
		Set<NamedState> wa_states = new HashSet<NamedState>();
		wa_states.add(y1);
		// word automaton
		WordAutomaton<StdNamedUnrankedSymbol, NamedState> wa = new WordAutomaton<StdNamedUnrankedSymbol, NamedState>(wa_states, wa_rules, y1, wa_states);
		// 1. rule of the 1. hedge automaton: b(wa) -> q2 (wa is the word automaton)
		HedgeRule<StdNamedUnrankedSymbol, NamedState> r1 = new HedgeRule<StdNamedUnrankedSymbol, NamedState>(b, wa, q2);
		// 2. rule of the 1. hedge automaton: a() -> q1
		HedgeRule<StdNamedUnrankedSymbol, NamedState> r2 = new HedgeRule<StdNamedUnrankedSymbol, NamedState>(a, new EmptyExpression<StdNamedUnrankedSymbol, NamedState>(), q1);
		// set of states of the 1. hedge automaton
		Set<NamedState> st = new HashSet<NamedState>();
		st.add(q1);
		st.add(q2);
		// set of rules of the 1. hedge automaton
		Set<HedgeRule<StdNamedUnrankedSymbol, NamedState>> rules = new HashSet<HedgeRule<StdNamedUnrankedSymbol, NamedState>>();
		rules.add(r1);
		rules.add(r2);
		// set of final states of the 1. hedge automaton
		Set<NamedState> finSt = new HashSet<NamedState>();
		finSt.add(q2);
		HedgeAutomaton<StdNamedUnrankedSymbol, NamedState> a1 = new HedgeAutomaton<StdNamedUnrankedSymbol, NamedState>(st, finSt, rules);
		// 1. rule of the 2. hedge automaton
		HedgeRule<StdNamedUnrankedSymbol, NamedState> r3 = new HedgeRule<StdNamedUnrankedSymbol, NamedState>(a, new EmptyExpression<StdNamedUnrankedSymbol, NamedState>(), q3);
		// 2. rule of the 2. hedge automaton
		List<NamedState> base = new LinkedList<NamedState>();
		base.add(q3);
		HedgeRule<StdNamedUnrankedSymbol, NamedState> r4 = new HedgeRule<StdNamedUnrankedSymbol, NamedState>(b, new Expression<StdNamedUnrankedSymbol, NamedState>(2, 2, new BasicExpression<StdNamedUnrankedSymbol, NamedState>(base)), q4);
		// set of states of the 2. hedge automaton
		Set<NamedState> st2 = new HashSet<NamedState>();
		st2.add(q3);
		st2.add(q4);
		// set of rules of the 2. hedge automaton
		Set<HedgeRule<StdNamedUnrankedSymbol, NamedState>> rules2 = new HashSet<HedgeRule<StdNamedUnrankedSymbol, NamedState>>();
		rules2.add(r3);
		rules2.add(r4);
		// set of the final states of the 2. hedge automaton
		Set<NamedState> finSt2 = new HashSet<NamedState>();
		finSt2.add(q4);
		HedgeAutomaton<StdNamedUnrankedSymbol, NamedState> a2 = new HedgeAutomaton<StdNamedUnrankedSymbol, NamedState>(st2, finSt2, rules2);
		HedgeAutomaton<StdNamedUnrankedSymbol, NamedState<Pair<HedgeState<NamedState>, NamedState<Set<HedgeState<NamedState>>>>>> a3 = HAOps.difference(a1, a2);
		//System.out.println(a3);
	}

	/**
	 * Makes a hedge automaton with the
	 * set of states: {q1, q2},
	 * set of final states: {q2},
	 * and the set of rules: {a() -> q1}.<br>
	 * Checks, whether the language of this automaton is empty
	 */
	@SuppressWarnings("unchecked")
	public void test7() {
		StdNamedUnrankedSymbol<String> a = new StdNamedUnrankedSymbol<String>("a");
		StdStateFactory sf = new StdStateFactory();
		// states for the hedge automata
		NamedState q1 = sf.makeState("1");
		NamedState q2 = sf.makeState("2");
		// set of states of the hedgeautomaton
		Set<NamedState> states = new HashSet<NamedState>();
		states.add(q1);
		states.add(q2);
		// set of final states of the hedgeautomaton
		Set<NamedState> finStates = new HashSet<NamedState>();
		finStates.add(q2);
		// rule: a() -> q1
		HedgeRule<StdNamedUnrankedSymbol, NamedState> r1 = new HedgeRule<StdNamedUnrankedSymbol, NamedState>(a, new EmptyExpression<StdNamedUnrankedSymbol, NamedState>(), q1);
		// set of rules of the hedgeautomaton
		Set<HedgeRule<StdNamedUnrankedSymbol, NamedState>> rules = new HashSet<HedgeRule<StdNamedUnrankedSymbol, NamedState>>();
		rules.add(r1);
		// hedgeautomaton
		HedgeAutomaton<StdNamedUnrankedSymbol, NamedState> autom1 = new HedgeAutomaton<StdNamedUnrankedSymbol, NamedState>(states, finStates, rules);
		assertTrue(HAOps.emptyLanguage(autom1));
	}

	/**
	 * Makes a wordautomaton wa
	 * with the set of states: {y1},
	 * set of final states: {y1},
	 * set of rules: {q1(y1) -> y1}
	 * and the initial state y1.
	 * Makes 2 hedge automata a1 and a2.
	 * a1:{{q1, q2}, {q2}, {a() -> q1, b(wa) -> q2}}}.
	 * a2:{{q1, q2}, {q2}, {a() -> q1, b(q1*) -> q1}}.
	 * Checks, whether this automata recognize the same language.
	 */
	@SuppressWarnings("unchecked")
	public void test8() {
		StdNamedUnrankedSymbol<String> a = new StdNamedUnrankedSymbol<String>("a");
		StdNamedUnrankedSymbol<String> b = new StdNamedUnrankedSymbol<String>("b");
		StdStateFactory sf = new StdStateFactory();
		// states for the hedge automata
		NamedState q1 = sf.makeState("1");
		NamedState q2 = sf.makeState("2");
		// state for the word automaton
		NamedState y1 = sf.makeState("11");
		// rule of the word automaton: q1(y1) -> y1
		WordRule<NamedState> wa_rule = new WordRule<NamedState>(q1, y1, y1);
		// set of rules of the word automaton
		Set<WordRule<NamedState>> wa_rules = new HashSet<WordRule<NamedState>>();
		wa_rules.add(wa_rule);
		// set of states of the word automaton
		Set<NamedState> wa_states = new HashSet<NamedState>();
		wa_states.add(y1);
		// word automaton
		WordAutomaton<StdNamedUnrankedSymbol, NamedState> wa = new WordAutomaton<StdNamedUnrankedSymbol, NamedState>(wa_states, wa_rules, y1, wa_states);
		// 1. rule of the 1. hedge automaton: b(wa) -> q2 (wa is the word automaton)
		HedgeRule<StdNamedUnrankedSymbol, NamedState> r1 = new HedgeRule<StdNamedUnrankedSymbol, NamedState>(b, wa, q2);
		// 2. rule of the 1. hedge automaton: a() -> q1
		HedgeRule<StdNamedUnrankedSymbol, NamedState> r2 = new HedgeRule<StdNamedUnrankedSymbol, NamedState>(a, new EmptyExpression<StdNamedUnrankedSymbol, NamedState>(), q1);
		// set of states of the 1. hedge automaton
		Set<NamedState> st = new HashSet<NamedState>();
		st.add(q1);
		st.add(q2);
		// set of rules of the 1. hedge automaton
		Set<HedgeRule<StdNamedUnrankedSymbol, NamedState>> rules = new HashSet<HedgeRule<StdNamedUnrankedSymbol, NamedState>>();
		rules.add(r1);
		rules.add(r2);
		// set of final states of the 1. hedge automaton
		Set<NamedState> finSt = new HashSet<NamedState>();
		finSt.add(q2);
		// 1. hedge automaton
		HedgeAutomaton<StdNamedUnrankedSymbol, NamedState> a1 = new HedgeAutomaton<StdNamedUnrankedSymbol, NamedState>(st, finSt, rules);
		// 1. rule of the 2. hedge automaton
		HedgeRule<StdNamedUnrankedSymbol, NamedState> r3 = new HedgeRule<StdNamedUnrankedSymbol, NamedState>(a, new EmptyExpression<StdNamedUnrankedSymbol, NamedState>(), q1);
		// 2. rule of the 2. hedge automaton
		List<NamedState> base = new LinkedList<NamedState>();
		base.add(q1);
		HedgeRule<StdNamedUnrankedSymbol, NamedState> r4 = new HedgeRule<StdNamedUnrankedSymbol, NamedState>(b, new Expression<StdNamedUnrankedSymbol, NamedState>(0, -1, new BasicExpression<StdNamedUnrankedSymbol, NamedState>(base)), q2);
		// set of states of the 2. hedge automaton
		Set<NamedState> st2 = new HashSet<NamedState>();
		st2.add(q1);
		st2.add(q2);
		// set of rules of the 2. hedge automaton
		Set<HedgeRule<StdNamedUnrankedSymbol, NamedState>> rules2 = new HashSet<HedgeRule<StdNamedUnrankedSymbol, NamedState>>();
		rules2.add(r3);
		rules2.add(r4);
		// set of the final states of the 2. hedge automaton
		Set<NamedState> finSt2 = new HashSet<NamedState>();
		finSt2.add(q2);
		// 2. hedge automaton
		HedgeAutomaton<StdNamedUnrankedSymbol, NamedState> a2 = new HedgeAutomaton<StdNamedUnrankedSymbol, NamedState>(st2, finSt2, rules2);
		assertTrue(HAOps.sameLanguage(a1, a2));
		//System.out.println(unionHA);
	}

	/**
	 * Makes a wordautomaton wa
	 * with the set of states: {y1},
	 * set of final states: {y1},
	 * set of rules: {q1(y1) -> y1}
	 * and the initial state y1.
	 * Makes 2 hedge automata a1 and a2.
	 * a1:{{q1, q2}, {q2}, {a() -> q1, b(wa) -> q2}}}.
	 * a2:{{q3, q4}, {q4}, {a() -> q3, b(q3^2) -> q4}}.<br>
	 * Checks, whether the language of a1 contains the language of a2.
	 */
	@SuppressWarnings("unchecked")
	public void test9() {
		StdNamedUnrankedSymbol<String> a = new StdNamedUnrankedSymbol<String>("a");
		StdNamedUnrankedSymbol<String> b = new StdNamedUnrankedSymbol<String>("b");
		StdStateFactory sf = new StdStateFactory();
		// states for the hedge automata
		NamedState q1 = sf.makeState("1");
		NamedState q2 = sf.makeState("2");
		NamedState q3 = sf.makeState("3");
		NamedState q4 = sf.makeState("4");
		// state for the word automaton
		NamedState y1 = sf.makeState("11");
		// rule of the word automaton: q1(y1) -> y1
		WordRule<NamedState> wa_rule = new WordRule<NamedState>(q1, y1, y1);
		// set of rules of the word automaton
		Set<WordRule<NamedState>> wa_rules = new HashSet<WordRule<NamedState>>();
		wa_rules.add(wa_rule);
		// set of states of the word automaton
		Set<NamedState> wa_states = new HashSet<NamedState>();
		wa_states.add(y1);
		// word automaton
		WordAutomaton<StdNamedUnrankedSymbol, NamedState> wa = new WordAutomaton<StdNamedUnrankedSymbol, NamedState>(wa_states, wa_rules, y1, wa_states);
		// 1. rule of the 1. hedge automaton: b(wa) -> q2 (wa is the word automaton)
		HedgeRule<StdNamedUnrankedSymbol, NamedState> r1 = new HedgeRule<StdNamedUnrankedSymbol, NamedState>(b, wa, q2);
		// 2. rule of the 1. hedge automaton: a() -> q1
		HedgeRule<StdNamedUnrankedSymbol, NamedState> r2 = new HedgeRule<StdNamedUnrankedSymbol, NamedState>(a, new EmptyExpression<StdNamedUnrankedSymbol, NamedState>(), q1);
		// set of states of the 1. hedge automaton
		Set<NamedState> st = new HashSet<NamedState>();
		st.add(q1);
		st.add(q2);
		// set of rules of the 1. hedge automaton
		Set<HedgeRule<StdNamedUnrankedSymbol, NamedState>> rules = new HashSet<HedgeRule<StdNamedUnrankedSymbol, NamedState>>();
		rules.add(r1);
		rules.add(r2);
		// set of final states of the 1. hedge automaton
		Set<NamedState> finSt = new HashSet<NamedState>();
		finSt.add(q2);
		// 1. hedge automaton
		HedgeAutomaton<StdNamedUnrankedSymbol, NamedState> a1 = new HedgeAutomaton<StdNamedUnrankedSymbol, NamedState>(st, finSt, rules);
		// 1. rule of the 2. hedge automaton
		HedgeRule<StdNamedUnrankedSymbol, NamedState> r3 = new HedgeRule<StdNamedUnrankedSymbol, NamedState>(a, new EmptyExpression<StdNamedUnrankedSymbol, NamedState>(), q3);
		// 2. rule of the 2. hedge automaton
		List<NamedState> base = new LinkedList<NamedState>();
		base.add(q3);
		HedgeRule<StdNamedUnrankedSymbol, NamedState> r4 = new HedgeRule<StdNamedUnrankedSymbol, NamedState>(b, new Expression<StdNamedUnrankedSymbol, NamedState>(2, 2, new BasicExpression<StdNamedUnrankedSymbol, NamedState>(base)), q4);
		// set of states of the 2. hedge automaton
		Set<NamedState> st2 = new HashSet<NamedState>();
		st2.add(q3);
		st2.add(q4);
		// set of rules of the 2. hedge automaton
		Set<HedgeRule<StdNamedUnrankedSymbol, NamedState>> rules2 = new HashSet<HedgeRule<StdNamedUnrankedSymbol, NamedState>>();
		rules2.add(r3);
		rules2.add(r4);
		// set of the final states of the 2. hedge automaton
		Set<NamedState> finSt2 = new HashSet<NamedState>();
		finSt2.add(q4);
		// 2. hedge automaton
		HedgeAutomaton<StdNamedUnrankedSymbol, NamedState> a2 = new HedgeAutomaton<StdNamedUnrankedSymbol, NamedState>(st2, finSt2, rules2);
		assertTrue(HAOps.subsetLanguage(a2, a1));
		//System.out.println(ha);
	}

	/**
	 * Makes a hedgeautomaton autom1
	 * with the set of states: {q1, q2, q3},
	 * set of final states: {q2},
	 * and the set of rules: {a() -> q1, b(q1) -> q1, b(q1) -> q2, a() -> q3}.<br>
	 * Makes an minimal hedge automaton,
	 * which is equivalent to autom1.
	 */
	@SuppressWarnings("unchecked")
	public void test10() {
		StdNamedUnrankedSymbol<String> a = new StdNamedUnrankedSymbol<String>("a");
		StdNamedUnrankedSymbol<String> b = new StdNamedUnrankedSymbol<String>("b");
		StdStateFactory sf = new StdStateFactory();
		// states for the hedge automata
		NamedState q1 = sf.makeState("1");
		NamedState q2 = sf.makeState("2");
		NamedState q3 = sf.makeState("3");
		// set of states of the hedgeautomaton
		Set<NamedState> states = new HashSet<NamedState>();
		states.add(q1);
		states.add(q2);
		states.add(q3);
		// set of final states of the hedgeautomaton
		Set<NamedState> finStates = new HashSet<NamedState>();
		finStates.add(q2);
		// 1. rule: a() -> q1
		HedgeRule<StdNamedUnrankedSymbol, NamedState> r1 = new HedgeRule<StdNamedUnrankedSymbol, NamedState>(a, new EmptyExpression<StdNamedUnrankedSymbol, NamedState>(), q1);
		// 2. rule: b(q1) -> q1
		List<NamedState> base = new LinkedList<NamedState>();
		base.add(q1);
		Expression<StdNamedUnrankedSymbol, NamedState> exp = new Expression<StdNamedUnrankedSymbol, NamedState>(1, 1, new BasicExpression<StdNamedUnrankedSymbol, NamedState>(base));
		HedgeRule<StdNamedUnrankedSymbol, NamedState> r2 = new HedgeRule<StdNamedUnrankedSymbol, NamedState>(b, exp, q1);
		// 3. rule: b(q1) -> q2
		HedgeRule<StdNamedUnrankedSymbol, NamedState> r3 = new HedgeRule<StdNamedUnrankedSymbol, NamedState>(b, exp, q2);
		// 4. rule: a() -> q3
		HedgeRule<StdNamedUnrankedSymbol, NamedState> r4 = new HedgeRule<StdNamedUnrankedSymbol, NamedState>(a, new EmptyExpression<StdNamedUnrankedSymbol, NamedState>(), q3);
		// set of rules of the hedgeautomaton
		Set<HedgeRule<StdNamedUnrankedSymbol, NamedState>> rules = new HashSet<HedgeRule<StdNamedUnrankedSymbol, NamedState>>();
		rules.add(r1);
		rules.add(r2);
		rules.add(r3);
		rules.add(r4);
		// hedgeautomaton
		HedgeAutomaton<StdNamedUnrankedSymbol, NamedState> autom1 = new HedgeAutomaton<StdNamedUnrankedSymbol, NamedState>(states, finStates, rules);
		//HedgeAutomaton<StdNamedUnrankedSymbol,NamedState<Set<NamedState<Set<HedgeState<NamedState>>>>>> ha2 = HAOps.minimize(autom1);
		//System.out.println(ha2);
		System.out.println(HAOps.constructTreeFrom(autom1));
	}


	@SuppressWarnings("unchecked")
	public void test11() {
		/*-- Automat 1 --*/
		StdNamedUnrankedSymbol<String> a = new StdNamedUnrankedSymbol<String>("a");
		StdNamedUnrankedSymbol<String> f = new StdNamedUnrankedSymbol<String>("f");
		StdStateFactory sf = new StdStateFactory();
		NamedState s1 = sf.makeState("1");
		NamedState s2 = sf.makeState("2");

		Expression<StdNamedUnrankedSymbol<String>, NamedState> eE =
			new EmptyExpression<StdNamedUnrankedSymbol<String>, NamedState>();

		HedgeRule<StdNamedUnrankedSymbol<String>, NamedState> r1 =
			new HedgeRule<StdNamedUnrankedSymbol<String>, NamedState>(a, eE, s1);

		List<NamedState> a_exp = new LinkedList<NamedState>();
		a_exp.add(s1);

		Expression<StdNamedUnrankedSymbol<String>, NamedState> a_st =
			new Expression<StdNamedUnrankedSymbol<String>, NamedState>(
					0, -1,
					new BasicExpression<StdNamedUnrankedSymbol<String>, NamedState>(a_exp));

		HedgeRule<StdNamedUnrankedSymbol<String>, NamedState> r2 =
			new HedgeRule<StdNamedUnrankedSymbol<String>, NamedState>(f, a_st, s2);


		Set<NamedState> ha1_states = new HashSet<NamedState>();
		ha1_states.add(s1);
		ha1_states.add(s2);
		Set<NamedState> ha1_finstates = new HashSet<NamedState>();
		ha1_finstates.add(s2);
		Set<HedgeRule<StdNamedUnrankedSymbol<String>, NamedState>> ha1_rules =
			new HashSet<HedgeRule<StdNamedUnrankedSymbol<String>, NamedState>>();
		ha1_rules.add(r1);
		ha1_rules.add(r2);
		HedgeAutomaton<StdNamedUnrankedSymbol<String>, NamedState> ha1 =
			new HedgeAutomaton<StdNamedUnrankedSymbol<String>, NamedState>(ha1_states, ha1_finstates, ha1_rules);

		/*-- Automat 2 --*/
		NamedState ha2_s1 = sf.makeState("ha2_1");
		NamedState ha2_s2 = sf.makeState("ha2_2");

		HedgeRule<StdNamedUnrankedSymbol<String>, NamedState> ha2_r1 =
			new HedgeRule<StdNamedUnrankedSymbol<String>, NamedState>(a, eE, ha2_s1);

		List<NamedState> ha2_a_exp = new LinkedList<NamedState>();
		ha2_a_exp.add(ha2_s1);

		Expression<StdNamedUnrankedSymbol<String>, NamedState> ha2_a_st =
			new Expression<StdNamedUnrankedSymbol<String>, NamedState>(
					1, -1,
					new BasicExpression<StdNamedUnrankedSymbol<String>, NamedState>(ha2_a_exp));

		HedgeRule<StdNamedUnrankedSymbol<String>, NamedState> ha2_r2 =
			new HedgeRule<StdNamedUnrankedSymbol<String>, NamedState>(f, ha2_a_st, ha2_s2);


		Set<NamedState> ha2_states = new HashSet<NamedState>();
		ha2_states.add(ha2_s1);
		ha2_states.add(ha2_s2);
		Set<NamedState> ha2_finstates = new HashSet<NamedState>();
		ha2_finstates.add(ha2_s2);
		Set<HedgeRule<StdNamedUnrankedSymbol<String>, NamedState>> ha2_rules =
			new HashSet<HedgeRule<StdNamedUnrankedSymbol<String>, NamedState>>();
		ha2_rules.add(ha2_r1);
		ha2_rules.add(ha2_r2);
		HedgeAutomaton<StdNamedUnrankedSymbol<String>, NamedState> ha2 =
			new HedgeAutomaton<StdNamedUnrankedSymbol<String>, NamedState>(ha2_states, ha2_finstates, ha2_rules);
		//System.out.println(HAOps.subsetLanguage(ha1,ha2));

		//Assert.assertTrue(FTAProperties.decide(fta, testTree.getTree()));

		HAOps.difference(ha2, ha1);

	}
}
