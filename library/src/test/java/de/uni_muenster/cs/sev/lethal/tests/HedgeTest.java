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

import de.uni_muenster.cs.sev.lethal.hedgeautomaton.HAOps;
import de.uni_muenster.cs.sev.lethal.hedgeautomaton.HedgeAutomaton;
import de.uni_muenster.cs.sev.lethal.hedgeautomaton.HedgeRule;
import de.uni_muenster.cs.sev.lethal.hedgeautomaton.expressions.BasicExpression;
import de.uni_muenster.cs.sev.lethal.hedgeautomaton.expressions.ConcatExpression;
import de.uni_muenster.cs.sev.lethal.hedgeautomaton.expressions.EmptyExpression;
import de.uni_muenster.cs.sev.lethal.hedgeautomaton.expressions.Expression;
import de.uni_muenster.cs.sev.lethal.hedgeautomaton.expressions.OrExpression;
import de.uni_muenster.cs.sev.lethal.hedgeautomaton.wordAutomata.WordAutomaton;
import de.uni_muenster.cs.sev.lethal.hedgeautomaton.wordAutomata.WordRule;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import de.uni_muenster.cs.sev.lethal.states.State;
import de.uni_muenster.cs.sev.lethal.symbol.common.UnrankedSymbol;
import de.uni_muenster.cs.sev.lethal.tree.common.Tree;
import de.uni_muenster.cs.sev.lethal.tree.standard.StdTree;

/**
 *
 * Contains the tests
 * for the testing of the HedgeAutomaton.
 *
 * @author anton
 */
public class HedgeTest extends TestCase {

  /**
   * @param name standard argument
   */
  public HedgeTest(String name) {
    super(name);
  }

  /**
   * @return Testsuite
   */
  public static Test suite() {
    return new TestSuite(HedgeTest.class);
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
   * makes a hedgeautomaton a
   * with the set of states: {q1},
   * the set of final states: {q1},
   * and the set of rules: {s() -> q1},
   * makes the hedge s(nil)
   * and checks, whether this hedge is
   * recognizable by this hedgeautomaton
   */
  public void test1() {

    UnrankedSymbol s = new UnrankedSymbol() {
    };
    State q1 = new State(){};
    // set of states
    Set<State> st = new HashSet<State>();
    st.add(q1);
    // set of final states
    Set<State> finSt = new HashSet<State>();
    finSt.add(q1);
    // rule: s() -> q1
    HedgeRule<UnrankedSymbol, State> r = new HedgeRule<UnrankedSymbol, State>(s, new EmptyExpression<UnrankedSymbol, State>(), q1);
    // set of rules
    Set<HedgeRule<UnrankedSymbol, State>> rules = new HashSet<HedgeRule<UnrankedSymbol, State>>();
    rules.add(r);

    // hedgeautomaton
    HedgeAutomaton<UnrankedSymbol, State> a = new HedgeAutomaton<UnrankedSymbol, State>(st, finSt, rules);
    // hedge -> s(nil)
    List<Tree<UnrankedSymbol>> subs = new LinkedList<Tree<UnrankedSymbol>>();
    Tree<UnrankedSymbol> h = new StdTree<UnrankedSymbol>(s, subs);
    assertTrue(HAOps.decide(a, h));
  }

  /**
   * makes a hedgeautomaton autom
   * with the set of states: {q1, q2},
   * the set of final states: {q2},
   * and the set of rules: {a() -> q1, b(q1*) -> q2},
   * makes the hedge b(a(nil), a(nil), a(nil))
   * and checks, whether this hedge is
   * recognizable by this hedgeautomaton
   */
  public void test2() {
    UnrankedSymbol a = new UnrankedSymbol() {
    };
    UnrankedSymbol b = new UnrankedSymbol() {
    };
    State q1 = new State(){};
    State q2 = new State(){};
    // set of states
    Set<State> st = new HashSet<State>();
    st.add(q1);
    st.add(q2);
    // set of final states
    Set<State> finSt = new HashSet<State>();
    finSt.add(q2);
    // rules
    List<State> states = new LinkedList<State>();
    states.add(q1);
    BasicExpression<UnrankedSymbol, State> exp = new BasicExpression<UnrankedSymbol, State>(states);
    // a() -> q1
    HedgeRule<UnrankedSymbol, State> r1 = new HedgeRule<UnrankedSymbol, State>(a, new EmptyExpression<UnrankedSymbol, State>(), q1);
    // b(q1*) -> q2
    HedgeRule<UnrankedSymbol, State> r2 = new HedgeRule<UnrankedSymbol, State>(b, new Expression<UnrankedSymbol, State>(0, -1, exp), q2);
    Set<HedgeRule<UnrankedSymbol, State>> rules = new HashSet<HedgeRule<UnrankedSymbol, State>>();
    rules.add(r1);
    rules.add(r2);

    // hedgeautomaton
    HedgeAutomaton<UnrankedSymbol, State> autom = new HedgeAutomaton<UnrankedSymbol, State>(st, finSt, rules);
    // hedge: b(a(nil), a(nil), a(nil))
    List<Tree<UnrankedSymbol>> subs = new LinkedList<Tree<UnrankedSymbol>>();
    Tree<UnrankedSymbol> h1 = new StdTree<UnrankedSymbol>(a, subs);
    List<Tree<UnrankedSymbol>> subs1 = new LinkedList<Tree<UnrankedSymbol>>();
    subs1.add(h1);
    subs1.add(h1);
    subs1.add(h1);
    Tree<UnrankedSymbol> h2 = new StdTree<UnrankedSymbol>(b, subs1);
    assertTrue(HAOps.decide(autom, h2));
    //System.out.println(ta);
  }

  /**
   * makes a hedgeautomaton autom
   * with the set of states: {q1, q2, q3},
   * the set of final states: {q3},
   * and the set of rules: {a() -> q1, b() -> q2, c(q1*|q2*) -> q3},
   * makes the hedge c(a(nil), a(nil), a(nil), a(nil), a(nil))
   * and checks, whether this hedge is
   * recognizable by this hedgeautomaton
   */
  public void test3() {
    UnrankedSymbol a = new UnrankedSymbol() {
    };
    UnrankedSymbol b = new UnrankedSymbol() {
    };
    UnrankedSymbol c = new UnrankedSymbol() {
    };
    State q1 = new State(){};
    State q2 = new State(){};
    State q3 = new State(){};
    // set of states
    Set<State> st = new HashSet<State>();
    st.add(q1);
    st.add(q2);
    st.add(q3);
    // set of final states
    Set<State> finSt = new HashSet<State>();
    finSt.add(q3);
    // Rules
    // 1: a() -> q1
    HedgeRule<UnrankedSymbol, State> r1 = new HedgeRule<UnrankedSymbol, State>(a, new EmptyExpression<UnrankedSymbol, State>(), q1);
    // 2 b() -> q2
    HedgeRule<UnrankedSymbol, State> r2 = new HedgeRule<UnrankedSymbol, State>(b, new EmptyExpression<UnrankedSymbol, State>(), q2);
    // 3 c(q1*|q2*) -> q3
    List<State> states = new LinkedList<State>();
    states.add(q1);
    BasicExpression<UnrankedSymbol, State> exp1 = new BasicExpression<UnrankedSymbol, State>(states);
    states = new LinkedList<State>();
    states.add(q2);
    BasicExpression<UnrankedSymbol, State> exp2 = new BasicExpression<UnrankedSymbol, State>(states);
    OrExpression<UnrankedSymbol, State> exp = new OrExpression<UnrankedSymbol, State>(new Expression<UnrankedSymbol, State>(0, -1, exp1), new Expression<UnrankedSymbol, State>(0, -1, exp2));
    HedgeRule<UnrankedSymbol, State> r3 = new HedgeRule<UnrankedSymbol, State>(c, new Expression<UnrankedSymbol, State>(1, 1, exp), q3);
    // set of rules
    Set<HedgeRule<UnrankedSymbol,State>> rules = new HashSet<HedgeRule<UnrankedSymbol,State>>();
    rules.add(r1);
    rules.add(r2);
    rules.add(r3);

    // hedgeautomaton
    HedgeAutomaton<UnrankedSymbol,State> autom = new HedgeAutomaton<UnrankedSymbol,State>(st, finSt, rules);
    // hedge: c(a(nil), a(nil), a(nil), a(nil), a(nil))
    List<Tree<UnrankedSymbol>> subs = new LinkedList<Tree<UnrankedSymbol>>();
    Tree<UnrankedSymbol> ha = new StdTree<UnrankedSymbol>(a, subs);
    List<Tree<UnrankedSymbol>> subs1 = new LinkedList<Tree<UnrankedSymbol>>();
    subs1.add(ha);
    subs1.add(ha);
    subs1.add(ha);
    subs1.add(ha);
    subs1.add(ha);
    Tree<UnrankedSymbol> h = new StdTree<UnrankedSymbol>(c, subs1);
    assertTrue(HAOps.decide(autom, h));
    //System.out.println(ta);
  }

  /**
   * makes a hedgeautomaton autom
   * with the set of states: {q1, q2, q3},
   * the set of final states: {q3},
   * and the set of rules: {a() -> q1, b() -> q2, c((q1*|q2*)*) -> q3},
   * makes the hedge c(b(nil), a(nil), a(nil), b(nil), a(nil), b(nil), a(nil), b(nil))
   * and checks, whether this hedge is
   * recognizable by this hedgeautomaton
   */
  public void test4() {
    UnrankedSymbol a = new UnrankedSymbol() {
    };
    UnrankedSymbol b = new UnrankedSymbol() {
    };
    UnrankedSymbol c = new UnrankedSymbol() {
    };
    State q1 = new State(){};
    State q2 = new State(){};
    State q3 = new State(){};
    // set of states
    Set<State> st = new HashSet<State>();
    st.add(q1);
    st.add(q2);
    st.add(q3);
    // set of final states
    Set<State> finSt = new HashSet<State>();
    finSt.add(q3);
    // Rules
    // 1: a() -> q1
    HedgeRule<UnrankedSymbol,State> r1 = new HedgeRule<UnrankedSymbol,State>(a, new EmptyExpression<UnrankedSymbol,State>(), q1);
    // 2: b() -> q2
    HedgeRule<UnrankedSymbol,State> r2 = new HedgeRule<UnrankedSymbol,State>(b, new EmptyExpression<UnrankedSymbol,State>(), q2);
    // 3: c((q1*|q2*)*) -> q3
    List<State> states = new LinkedList<State>();
    states.add(q1);
    BasicExpression<UnrankedSymbol,State> exp1 = new BasicExpression<UnrankedSymbol,State>(states);
    states = new LinkedList<State>();
    states.add(q2);
    BasicExpression<UnrankedSymbol,State> exp2 = new BasicExpression<UnrankedSymbol,State>(states);
    OrExpression<UnrankedSymbol,State> exp = new OrExpression<UnrankedSymbol,State>(new Expression<UnrankedSymbol,State>(0, -1, exp1), new Expression<UnrankedSymbol,State>(0, -1, exp2));
    HedgeRule<UnrankedSymbol,State> r3 = new HedgeRule<UnrankedSymbol,State>(c, new Expression<UnrankedSymbol,State>(0, -1, exp), q3);
    // set of rules
    Set<HedgeRule<UnrankedSymbol,State>> rules = new HashSet<HedgeRule<UnrankedSymbol,State>>();
    rules.add(r1);
    rules.add(r2);
    rules.add(r3);

    // hedgeautomaton
    HedgeAutomaton<UnrankedSymbol,State> autom = new HedgeAutomaton<UnrankedSymbol,State>(st, finSt, rules);
    // hedge: c(b(nil), a(nil), a(nil), b(nil), a(nil), b(nil), a(nil), b(nil))
    List<Tree<UnrankedSymbol>> subs = new LinkedList<Tree<UnrankedSymbol>>();
    Tree<UnrankedSymbol> ha = new StdTree<UnrankedSymbol>(a, subs);
    Tree<UnrankedSymbol> hb = new StdTree<UnrankedSymbol>(b, subs);
    List<Tree<UnrankedSymbol>> subs1 = new LinkedList<Tree<UnrankedSymbol>>();
    subs1.add(hb);
    subs1.add(ha);
    subs1.add(ha);
    subs1.add(hb);
    subs1.add(ha);
    subs1.add(hb);
    subs1.add(ha);
    subs1.add(hb);
    Tree<UnrankedSymbol> h = new StdTree<UnrankedSymbol>(c, subs1);
    assertTrue(HAOps.decide(autom, h));
    //System.out.println(ta);
  }

  /**
   * makes a hedgeautomaton autom
   * with the set of states: {q1, q2, q3},
   * the set of final states: {q3},
   * and the set of rules: {a() -> q1, b() -> q2, a(q2?q1q2^2) -> q3},
   * makes the hedge a(b(nil), a(nil), b(nil), b(nil))
   * and checks, whether this hedge is
   * recognizable by this hedgeautomaton
   */
  public void test5() {
    UnrankedSymbol a = new UnrankedSymbol() {
    };
    UnrankedSymbol b = new UnrankedSymbol() {
    };
    State q1 = new State(){};
    State q2 = new State(){};
    State q3 = new State(){};
    // set of states
    Set<State> st = new HashSet<State>();
    st.add(q1);
    st.add(q2);
    st.add(q3);
    // set of final states
    Set<State> finSt = new HashSet<State>();
    finSt.add(q3);
    // Rules
    // 1: a() -> q1
    HedgeRule<UnrankedSymbol,State> r1 = new HedgeRule<UnrankedSymbol,State>(a, new EmptyExpression<UnrankedSymbol,State>(), q1);
    // 2: b() -> q2
    HedgeRule<UnrankedSymbol,State> r2 = new HedgeRule<UnrankedSymbol,State>(b, new EmptyExpression<UnrankedSymbol,State>(), q2);
    // 3: a(q2?q1q2^2) -> q3
    List<State> states = new LinkedList<State>();
    states.add(q2);
    BasicExpression<UnrankedSymbol,State> exp1 = new BasicExpression<UnrankedSymbol,State>(states);
    states = new LinkedList<State>();
    states.add(q1);
    BasicExpression<UnrankedSymbol,State> exp2 = new BasicExpression<UnrankedSymbol,State>(states);
    ConcatExpression<UnrankedSymbol,State> exp3 = new ConcatExpression<UnrankedSymbol,State>(new Expression<UnrankedSymbol,State>(0, 1, exp1), new Expression<UnrankedSymbol,State>(1, 1, exp2));
    ConcatExpression<UnrankedSymbol,State> exp = new ConcatExpression<UnrankedSymbol,State>(new Expression<UnrankedSymbol,State>(1, 1, exp3), new Expression<UnrankedSymbol,State>(2, 2, exp1));
    HedgeRule<UnrankedSymbol,State> r3 = new HedgeRule<UnrankedSymbol,State>(a, new Expression<UnrankedSymbol,State>(1, 1, exp), q3);
    // set of rules
    Set<HedgeRule<UnrankedSymbol,State>> rules = new HashSet<HedgeRule<UnrankedSymbol,State>>();
    rules.add(r1);
    rules.add(r2);
    rules.add(r3);

    // hedgeautomaton
    HedgeAutomaton<UnrankedSymbol,State> autom = new HedgeAutomaton<UnrankedSymbol,State>(st, finSt, rules);
    // hedge: a(b(nil), a(nil), b(nil), b(nil))
    List<Tree<UnrankedSymbol>> subs = new LinkedList<Tree<UnrankedSymbol>>();
    Tree<UnrankedSymbol> ha = new StdTree<UnrankedSymbol>(a, subs);
    Tree<UnrankedSymbol> hb = new StdTree<UnrankedSymbol>(b, subs);
    List<Tree<UnrankedSymbol>> subs1 = new LinkedList<Tree<UnrankedSymbol>>();
    subs1.add(hb);
    subs1.add(ha);
    subs1.add(hb);
    subs1.add(hb);
    Tree<UnrankedSymbol> h1 = new StdTree<UnrankedSymbol>(a, subs1);
    assertTrue(HAOps.decide(autom, h1));
    //System.out.println(ta);
  }

  /**
   * makes a hedgeautomaton autom
   * with the set of states: {q1, q2, q3},
   * the set of final states: {q3},
   * and the set of rules: {a(q2*) -> q1, b(q1*) -> q2, a(q2*) -> q3, b(q1*) -> q3},
   * makes the hedge a(b(a(b(nil))), b(nil), b(nil), b(nil))
   * and checks, whether this hedge is
   * recognizable by this hedgeautomaton
   */
  public void test6() {
    UnrankedSymbol a = new UnrankedSymbol() {
    };
    UnrankedSymbol b = new UnrankedSymbol() {
    };
    State q1 = new State(){};
    State q2 = new State(){};
    State q3 = new State(){};
    // set of states
    Set<State> st = new HashSet<State>();
    st.add(q1);
    st.add(q2);
    st.add(q3);
    // set of final states
    Set<State> finSt = new HashSet<State>();
    finSt.add(q3);
    // Rules
    // 1: a(q2*) -> q1
    List<State> states = new LinkedList<State>();
    states.add(q2);
    Expression<UnrankedSymbol,State> exp1 = new Expression<UnrankedSymbol,State>(0, -1, new BasicExpression<UnrankedSymbol,State>(states));
    HedgeRule<UnrankedSymbol,State> r1 = new HedgeRule<UnrankedSymbol,State>(a, exp1, q1);
    // 2: b(q1*) -> q2
    states = new LinkedList<State>();
    states.add(q1);
    Expression<UnrankedSymbol,State> exp2 = new Expression<UnrankedSymbol,State>(0, -1, new BasicExpression<UnrankedSymbol,State>(states));
    HedgeRule<UnrankedSymbol,State> r2 = new HedgeRule<UnrankedSymbol,State>(b, exp2, q2);
    // 3: a(q2*) -> q3
    HedgeRule<UnrankedSymbol,State> r3 = new HedgeRule<UnrankedSymbol,State>(a, exp1, q3);
    // 4: b(q1*) -> q3
    HedgeRule<UnrankedSymbol,State> r4 = new HedgeRule<UnrankedSymbol,State>(b, exp2, q3);
    // set of rules
    Set<HedgeRule<UnrankedSymbol,State>> rules = new HashSet<HedgeRule<UnrankedSymbol,State>>();
    rules.add(r1);
    rules.add(r2);
    rules.add(r3);
    rules.add(r4);

    // hedgeautomaton
    HedgeAutomaton<UnrankedSymbol,State> autom = new HedgeAutomaton<UnrankedSymbol,State>(st, finSt, rules);
    // hedge: a(b(a(b(nil))), b(nil), b(nil), b(nil))
    List<Tree<UnrankedSymbol>> subs = new LinkedList<Tree<UnrankedSymbol>>();
    // b(nil)
    Tree<UnrankedSymbol> hb = new StdTree<UnrankedSymbol>(b, subs);
    List<Tree<UnrankedSymbol>> subs1 = new LinkedList<Tree<UnrankedSymbol>>();
    subs1.add(hb);
    // a(b(nil))
    Tree<UnrankedSymbol> h1 = new StdTree<UnrankedSymbol>(a, subs1);
    subs1 = new LinkedList<Tree<UnrankedSymbol>>();
    subs1.add(h1);
    // b(a(b(nil)))
    Tree<UnrankedSymbol> h2 = new StdTree<UnrankedSymbol>(b, subs1);
    subs1 = new LinkedList<Tree<UnrankedSymbol>>();
    subs1.add(h2);
    subs1.add(hb);
    subs1.add(hb);
    subs1.add(hb);
    // a(b(a(b(nil))), b(nil), b(nil), b(nil))
    Tree<UnrankedSymbol> h3 = new StdTree<UnrankedSymbol>(a, subs1);
    assertTrue(HAOps.decide(autom, h3));
    //System.out.println(ta);
  }

  /**
   * makes a hedgeautomaton autom
   * with the set of states: {q1, q2, q3},
   * the set of final states: {q3},
   * and the set of rules: {a() -> q1, b(q1^2) -> q2, c(q1^(3-4)) -> q2, a(q2^2) -> q3},
   * makes the hedge a(b(a(b(nil))), b(nil), b(nil), b(nil))
   * and checks, whether this hedge is
   * recognizable by this hedgeautomaton
   */
  public void test7() {
    UnrankedSymbol a = new UnrankedSymbol() {
    };
    UnrankedSymbol b = new UnrankedSymbol() {
    };
    UnrankedSymbol c = new UnrankedSymbol() {
    };
    State q1 = new State(){};
    State q2 = new State(){};
    State q3 = new State(){};
    // set of states
    Set<State> st = new HashSet<State>();
    st.add(q1);
    st.add(q2);
    st.add(q3);
    // set of final states
    Set<State> finSt = new HashSet<State>();
    finSt.add(q3);
    // Rules
    List<State> states = new LinkedList<State>();
    states.add(q1);
    BasicExpression<UnrankedSymbol,State> b1 = new BasicExpression<UnrankedSymbol,State>(states);
    states = new LinkedList<State>();
    states.add(q2);
    BasicExpression<UnrankedSymbol,State> b2 = new BasicExpression<UnrankedSymbol,State>(states);
    // 1: a() -> q1
    HedgeRule<UnrankedSymbol,State> r1 = new HedgeRule<UnrankedSymbol,State>(a, new EmptyExpression<UnrankedSymbol,State>(), q1);
    // 2: b(q1^2) -> q2
    HedgeRule<UnrankedSymbol,State> r2 = new HedgeRule<UnrankedSymbol,State>(b, new Expression<UnrankedSymbol,State>(2, 2, b1), q2);
    // 3: c(q1^(3-4)) -> q2
    HedgeRule<UnrankedSymbol,State> r3 = new HedgeRule<UnrankedSymbol,State>(c, new Expression<UnrankedSymbol,State>(3, 4, b1), q2);
    // 4: a(q2^2) -> q3
    HedgeRule<UnrankedSymbol,State> r4 = new HedgeRule<UnrankedSymbol,State>(a, new Expression<UnrankedSymbol,State>(2, 2, b2), q3);
    // set of rules
    Set<HedgeRule<UnrankedSymbol,State>> rules = new HashSet<HedgeRule<UnrankedSymbol,State>>();
    rules.add(r1);
    rules.add(r2);
    rules.add(r3);
    rules.add(r4);

    // hedgeautomaton
    HedgeAutomaton<UnrankedSymbol,State> autom = new HedgeAutomaton<UnrankedSymbol,State>(st, finSt, rules);
    // hedge: a(b(a(nil), a(nil)), c(a(nil), a(nil), a(nil), a(nil)))
    List<Tree<UnrankedSymbol>> subs = new LinkedList<Tree<UnrankedSymbol>>();
    // a(nil)
    Tree<UnrankedSymbol> ha = new StdTree<UnrankedSymbol>(a, subs);
    List<Tree<UnrankedSymbol>> subs1 = new LinkedList<Tree<UnrankedSymbol>>();
    subs1.add(ha);
    subs1.add(ha);
    // b(a(nil), a(nil))
    Tree<UnrankedSymbol> h1 = new StdTree<UnrankedSymbol>(b, subs1);
    subs1 = new LinkedList<Tree<UnrankedSymbol>>();
    subs1.add(ha);
    subs1.add(ha);
    subs1.add(ha);
    subs1.add(ha);
    // c(a(nil), a(nil), a(nil), a(nil))
    Tree<UnrankedSymbol> h2 = new StdTree<UnrankedSymbol>(c, subs1);
    subs1 = new LinkedList<Tree<UnrankedSymbol>>();
    subs1.add(h1);
    subs1.add(h2);
    // a(b(a(nil), a(nil)), c(a(nil), a(nil), a(nil), a(nil)))
    Tree<UnrankedSymbol> h3 = new StdTree<UnrankedSymbol>(a, subs1);
    assertTrue(HAOps.decide(autom, h3));
    //System.out.println(ta);
  }

  /**
   * makes a hedgeautomaton autom
   * with the set of states: {x1, xa, xb, xc, xt},
   * the set of final states: {x1, xa},
   * and the set of rules: {
   * a(xt*) -> xt
   * b(xt*) -> xt
   * c(xt*) -> xt
   * a(xt*(x1|xa)xt*) -> x1
   * a(xb xc) -> xa
   * b(xt*) -> xb
   * c(xt*) -> xc},
   * makes the hedge a(a(b(nil), c(nil)), a(b(nil)), a(b(nil), c(nil)))
   * and checks, whether this hedge is
   * recognizable by this hedgeautomaton
   */
  public void test8() {
    UnrankedSymbol a = new UnrankedSymbol() {
    };
    UnrankedSymbol b = new UnrankedSymbol() {
    };
    UnrankedSymbol c = new UnrankedSymbol() {
    };
    State x1 = new State(){};
    State xa = new State(){};
    State xb = new State(){};
    State xc = new State(){};
    State xt = new State(){};
    // set of states
    Set<State> st = new HashSet<State>();
    st.add(x1);
    st.add(xa);
    st.add(xb);
    st.add(xc);
    st.add(xt);
    // set of final states
    Set<State> finSt = new HashSet<State>();
    finSt.add(x1);
    finSt.add(xa);
    // rules
    List<State> states = new LinkedList<State>();
    states.add(xt);
    BasicExpression<UnrankedSymbol,State> bt = new BasicExpression<UnrankedSymbol,State>(states);
    // 1: a(xt*) -> xt
    HedgeRule<UnrankedSymbol,State> r1 = new HedgeRule<UnrankedSymbol,State>(a, new Expression<UnrankedSymbol,State>(0, -1, bt), xt);
    // 2: b(xt*) -> xt
    HedgeRule<UnrankedSymbol,State> r2 = new HedgeRule<UnrankedSymbol,State>(b, new Expression<UnrankedSymbol,State>(0, -1, bt), xt);
    // 3: c(xt*) -> xt
    HedgeRule<UnrankedSymbol,State> r3 = new HedgeRule<UnrankedSymbol,State>(c, new Expression<UnrankedSymbol,State>(0, -1, bt), xt);
    // 4: a(xt*(x1|xa)xt*) -> x1
    states = new LinkedList<State>();
    states.add(x1);
    BasicExpression<UnrankedSymbol,State> b1 = new BasicExpression<UnrankedSymbol,State>(states);
    states = new LinkedList<State>();
    states.add(xa);
    BasicExpression<UnrankedSymbol,State> ba = new BasicExpression<UnrankedSymbol,State>(states);
    OrExpression<UnrankedSymbol,State> orExp = new OrExpression<UnrankedSymbol,State>(new Expression<UnrankedSymbol,State>(1, 1, b1), new Expression<UnrankedSymbol,State>(1, 1, ba));
    ConcatExpression<UnrankedSymbol,State> conExp1 = new ConcatExpression<UnrankedSymbol,State>(new Expression<UnrankedSymbol,State>(0, -1, bt), new Expression<UnrankedSymbol,State>(1, 1, orExp));
    ConcatExpression<UnrankedSymbol,State> conExp2 = new ConcatExpression<UnrankedSymbol,State>(new Expression<UnrankedSymbol,State>(1, 1, conExp1), new Expression<UnrankedSymbol,State>(0, -1, bt));

    HedgeRule<UnrankedSymbol,State> r4 = new HedgeRule<UnrankedSymbol,State>(a, new Expression<UnrankedSymbol,State>(1, 1, conExp2), x1);
    // 5: a(xb xc) -> xa
    states = new LinkedList<State>();
    states.add(xb);
    states.add(xc);
    BasicExpression<UnrankedSymbol,State> bbc = new BasicExpression<UnrankedSymbol,State>(states);
    HedgeRule<UnrankedSymbol,State> r5 = new HedgeRule<UnrankedSymbol,State>(a, new Expression<UnrankedSymbol,State>(1, 1, bbc), xa);
    // 6: b(xt*) -> xb
    HedgeRule<UnrankedSymbol,State> r6 = new HedgeRule<UnrankedSymbol,State>(b, new Expression<UnrankedSymbol,State>(0, -1, bt), xb);
    // 7: c(xt*) -> xc
    HedgeRule<UnrankedSymbol,State> r7 = new HedgeRule<UnrankedSymbol,State>(c, new Expression<UnrankedSymbol,State>(0, -1, bt), xc);
    // set of rules
    Set<HedgeRule<UnrankedSymbol,State>> rules = new HashSet<HedgeRule<UnrankedSymbol,State>>();
    rules.add(r1);
    rules.add(r2);
    rules.add(r3);
    rules.add(r4);
    rules.add(r5);
    rules.add(r6);
    rules.add(r7);

    // hedgeautomaton
    HedgeAutomaton<UnrankedSymbol,State> autom = new HedgeAutomaton<UnrankedSymbol,State>(st, finSt, rules);
    // hedge: a(a(b(nil), c(nil)), a(b(nil)), a(b(nil), c(nil)))
    List<Tree<UnrankedSymbol>> subs = new LinkedList<Tree<UnrankedSymbol>>();
    // b(nil)
    Tree<UnrankedSymbol> hb = new StdTree<UnrankedSymbol>(b, subs);
    // c(nil)
    Tree<UnrankedSymbol> hc = new StdTree<UnrankedSymbol>(c, subs);
    subs = new LinkedList<Tree<UnrankedSymbol>>();
    subs.add(hb);
    subs.add(hc);
    // a(b(nil), c(nil))
    Tree<UnrankedSymbol> h1 = new StdTree<UnrankedSymbol>(a, subs);
    //  a(b(nil), c(nil))
    Tree<UnrankedSymbol> h2 = new StdTree<UnrankedSymbol>(a, subs);
    subs = new LinkedList<Tree<UnrankedSymbol>>();
    subs.add(hb);
    // a(b(nil))
    Tree<UnrankedSymbol> h3 = new StdTree<UnrankedSymbol>(a, subs);
    subs = new LinkedList<Tree<UnrankedSymbol>>();
    subs.add(h1);
    subs.add(h3);
    subs.add(h2);
    // a(a(b(nil), c(nil)), a(b(nil)), a(b(nil), c(nil)))
    Tree<UnrankedSymbol> h4 = new StdTree<UnrankedSymbol>(a, subs);
    assertTrue(HAOps.decide(autom, h4));
    //System.out.println(ta);
  }

  /**
   * makes a wordautomaton wa
   * with the set of states: {wa_s},
   * set of final states: {wa_s},
   * set of rules: {q1(wa_s) -> wa_s}
   * and the initial state wa_s.
   * Makes a hedgeautomaton a1
   * with the set of states: {q1, q2},
   * the set of final states: {q2},
   * and the set of rules: {a() -> q1, b(wa) -> q2} (wa is the wordautomaton).
   * Makes the hedge b(a(nil), a(nil), a(nil), a(nil), a(nil))
   * and checks, whether this hedge is
   * recognizable by this hedgeautomaton
   */
  public void test9() {
    UnrankedSymbol a = new UnrankedSymbol() {
    };
    UnrankedSymbol b = new UnrankedSymbol() {
    };
    // states for the hedgeautomaton
    State q1 = new State(){};
    State q2 = new State(){};
    // state for the wordautomaton
    State wa_s = new State(){};
    // rule of the wordautomaton: q1(wa_s) -> wa_s
    WordRule<State> wa_rule = new WordRule<State>(q1, wa_s, wa_s);
    // set of rules of the wordautomaton
    Set<WordRule<State>> wa_rules = new HashSet<WordRule<State>>();
    wa_rules.add(wa_rule);
    // set of states of the wordautomaton
    Set<State> wa_states = new HashSet<State>();
    wa_states.add(wa_s);
    // wordautomaton
    WordAutomaton<UnrankedSymbol,State> wa = new WordAutomaton<UnrankedSymbol,State>(wa_states, wa_rules, wa_s, wa_states);
    // 1. rule of the hedgeautomaton: b(wa) -> q2 (wa is the wordautomaton)
    HedgeRule<UnrankedSymbol,State> r1 = new HedgeRule<UnrankedSymbol,State>(b, wa, q2);
    // 2. rule of the hedgeautomaton: a() -> q1
    HedgeRule<UnrankedSymbol,State> r2 = new HedgeRule<UnrankedSymbol,State>(a, new EmptyExpression<UnrankedSymbol,State>(), q1);
    // set of states of the hedgeautomaton
    Set<State> st = new HashSet<State>();
    st.add(q1);
    st.add(q2);
    // set of rules of the hedgeautomaton
    Set<HedgeRule<UnrankedSymbol,State>> rules = new HashSet<HedgeRule<UnrankedSymbol,State>>();
    rules.add(r1);
    rules.add(r2);

    // set of final states of the hedgeautomaton
    Set<State> finSt = new HashSet<State>();
    finSt.add(q2);
    // hedgeautomaton
    HedgeAutomaton<UnrankedSymbol,State> a1 = new HedgeAutomaton<UnrankedSymbol,State>(st, finSt, rules);
    // hedge: b(a(nil), a(nil), a(nil), a(nil), a(nil))
    List<Tree<UnrankedSymbol>> subs = new LinkedList<Tree<UnrankedSymbol>>();
    // a(nil)
    Tree<UnrankedSymbol> h = new StdTree<UnrankedSymbol>(a, subs);
    List<Tree<UnrankedSymbol>> subs1 = new LinkedList<Tree<UnrankedSymbol>>();
    subs1.add(h);
    subs1.add(h);
    subs1.add(h);
    subs1.add(h);
    // b(a(nil), a(nil), a(nil), a(nil))
    Tree<UnrankedSymbol> h1 = new StdTree<UnrankedSymbol>(b, subs1);
    assertTrue(HAOps.decide(a1, h1));
  }

  /**
   * makes 3 wordautomata: wa1, wa2 and wa3
   * wa1 contains:
   * set of states: {y3, y4, y4},
   * set of final states: {y5},
   * set of rules: {xb(y3) -> y4, xc(y4) -> y5}
   * and the initial state y3.
   * wa2 contains:
   * set of states: {y6, y7},
   * set of final states: {y6, y7},
   * set of rules: {xt(y6) -> y7, xt(y7) -> y7}
   * and the initial state y6.
   * wa3 contains:
   * set of states: {y8, y9, y10, y11, y12},
   * set of final states: {y10, y11, y12},
   * set of rules:
   * {x1(y8) -> y10, xt(y8) -> y9, xa(y8) -> y11,
   * xt(y9) -> y9, x1(y9) -> y10, xa(y9) -> y11,
   * xt(y10) -> y12, xt(y11) -> y12, xt(y12) -> y12}
   * and the initial state y8.
   * Makes a hedgeautomaton a1
   * with the set of states: {x1, xa, xb, xc, xt},
   * the set of final states: {x1, xa},
   * and the set of rules:
   * {a(xt*) -> xt,
   * b(xt*) -> xt,
   * c(xt*) -> xt,
   * a(xt*(x1|xa)xt*) -> x1,
   * a(xb xc) -> xa,
   * b(xt*) -> xb,
   * c(xt*) -> xc},
   * makes the hedge a(a(b(nil), c(nil)), a(b(nil)), a(b(nil), c(nil)))
   * and checks, whether this hedge is
   * recognizable by this hedgeautomaton
   */
  public void test10() {
    // states of the hedgeautomaton
    State x1 = new State(){};
    State xa = new State(){};
    State xb = new State(){};
    State xc = new State(){};
    State xt = new State(){};
    // states of the 1. wordautomaton
    State y3 = new State(){};
    State y4 = new State(){};
    State y5 = new State(){};
    // states of the 2. wordautomaton
    State y6 = new State(){};
    State y7 = new State(){};
    // states of the 3. wordautomaton
    State y8 = new State(){};
    State y9 = new State(){};
    State y10 = new State(){};
    State y11 = new State(){};
    State y12 = new State(){};
    // set of states of the 1. wordautomaton
    Set<State> wStates1 = new HashSet<State>();
    wStates1.add(y3);
    wStates1.add(y4);
    wStates1.add(y5);
    // set of final states of the 1. wordautomaton
    Set<State> wFinStates1 = new HashSet<State>();
    wFinStates1.add(y5);
    // 1. rule of the 1. wordautomaton
    WordRule<State> wRule11 = new WordRule<State>(xb, y3, y4);
    // 2. rule of the 1. wordautomaton
    WordRule<State> wRule12 = new WordRule<State>(xc, y4, y5);
    // set of rules of the 1. wordautomaton
    Set<WordRule<State>> wRules1 = new HashSet<WordRule<State>>();
    wRules1.add(wRule11);
    wRules1.add(wRule12);
    // 1. wordautomaton
    WordAutomaton<UnrankedSymbol,State> wa1 = new WordAutomaton<UnrankedSymbol,State>(wStates1, wRules1, y3, wFinStates1);
    // set of states of the 2. wordautomaton
    Set<State> wStates2 = new HashSet<State>();
    wStates2.add(y6);
    wStates2.add(y7);
    // set of final states of the 2. wordautomaton
    Set<State> wFinStates2 = new HashSet<State>();
    wFinStates2.add(y6);
    wFinStates2.add(y7);
    // 1. rule of the 2. wordautomaton
    WordRule<State> wRule21 = new WordRule<State>(xt, y6, y7);
    // 2. rule of the 2. wordautomaton
    WordRule<State> wRule22 = new WordRule<State>(xt, y7, y7);
    // set of rules of the 2. wordautomaton
    Set<WordRule<State>> wRules2 = new HashSet<WordRule<State>>();
    wRules2.add(wRule21);
    wRules2.add(wRule22);
    // 2. wordautomaton
    WordAutomaton<UnrankedSymbol,State> wa2 = new WordAutomaton<UnrankedSymbol,State>(wStates2, wRules2, y6, wFinStates2);
    // set of states of the 3. wordautomaton
    Set<State> wStates3 = new HashSet<State>();
    wStates3.add(y8);
    wStates3.add(y9);
    wStates3.add(y10);
    wStates3.add(y11);
    wStates3.add(y12);
    // set of final states of the 3. wordautomaton
    Set<State> wFinStates3 = new HashSet<State>();
    wFinStates3.add(y10);
    wFinStates3.add(y11);
    wFinStates3.add(y12);
    // 1. rule of the 3. wordautomaton
    WordRule<State> wRule31 = new WordRule<State>(x1, y8, y10);
    // 2. rule of the 3. wordautomaton
    WordRule<State> wRule32 = new WordRule<State>(xt, y8, y9);
    // 3. rule of the 3. wordautomaton
    WordRule<State> wRule33 = new WordRule<State>(xa, y8, y11);
    // 4. rule of the 3. wordautomaton
    WordRule<State> wRule34 = new WordRule<State>(xt, y9, y9);
    // 5. rule of the 3. wordautomaton
    WordRule<State> wRule35 = new WordRule<State>(x1, y9, y10);
    // 6. rule of the 3. wordautomaton
    WordRule<State> wRule36 = new WordRule<State>(xa, y9, y11);
    // 7. rule of the 3. wordautomaton
    WordRule<State> wRule37 = new WordRule<State>(xt, y10, y12);
    // 8. rule of the 3. wordautomaton
    WordRule<State> wRule38 = new WordRule<State>(xt, y11, y12);
    // 9. rule of the 3. wordautomaton
    WordRule<State> wRule39 = new WordRule<State>(xt, y12, y12);
    // set of rules of the 3. wordautomaton
    Set<WordRule<State>> wRules3 = new HashSet<WordRule<State>>();
    wRules3.add(wRule31);
    wRules3.add(wRule32);
    wRules3.add(wRule33);
    wRules3.add(wRule34);
    wRules3.add(wRule35);
    wRules3.add(wRule36);
    wRules3.add(wRule37);
    wRules3.add(wRule38);
    wRules3.add(wRule39);
    // 3. wordautomaton
    WordAutomaton<UnrankedSymbol,State> wa3 = new WordAutomaton<UnrankedSymbol,State>(wStates3, wRules3, y8, wFinStates3);
    // symbols of the hedgeautomaton
    UnrankedSymbol a = new UnrankedSymbol() {
    };
    UnrankedSymbol b = new UnrankedSymbol() {
    };
    UnrankedSymbol c = new UnrankedSymbol() {
    };
    // set of states of the hedgeautomaton
    Set<State> hStates = new HashSet<State>();
    hStates.add(x1);
    hStates.add(xa);
    hStates.add(xb);
    hStates.add(xc);
    hStates.add(xt);
    // set of final states of the hedgeautomaton
    Set<State> hFinStates = new HashSet<State>();
    hFinStates.add(x1);
    hFinStates.add(xa);
    // 1. rule of the hedgeautomaton
    HedgeRule<UnrankedSymbol,State> hRule1 = new HedgeRule<UnrankedSymbol,State>(a, wa2, xt);
    // 2. rule of the hedgeautomaton
    HedgeRule<UnrankedSymbol,State> hRule2 = new HedgeRule<UnrankedSymbol,State>(b, wa2, xt);
    // 3. rule of the hedgeautomaton
    HedgeRule<UnrankedSymbol,State> hRule3 = new HedgeRule<UnrankedSymbol,State>(c, wa2, xt);
    // 4. rule of the hedgeautomaton
    HedgeRule<UnrankedSymbol,State> hRule4 = new HedgeRule<UnrankedSymbol,State>(a, wa3, x1);
    // 5. rule of the hedgeautomaton
    HedgeRule<UnrankedSymbol,State> hRule5 = new HedgeRule<UnrankedSymbol,State>(a, wa1, xa);
    // 6. rule of the hedgeautomaton
    HedgeRule<UnrankedSymbol,State> hRule6 = new HedgeRule<UnrankedSymbol,State>(b, wa2, xb);
    // 7. rule of the hedgeautomaton
    HedgeRule<UnrankedSymbol,State> hRule7 = new HedgeRule<UnrankedSymbol,State>(c, wa2, xc);
    // set of rules of the hedgeautomaton
    Set<HedgeRule<UnrankedSymbol,State>> hRules = new HashSet<HedgeRule<UnrankedSymbol,State>>();
    hRules.add(hRule1);
    hRules.add(hRule2);
    hRules.add(hRule3);
    hRules.add(hRule4);
    hRules.add(hRule5);
    hRules.add(hRule6);
    hRules.add(hRule7);

    // hedgeautomaton
    HedgeAutomaton<UnrankedSymbol,State> ha = new HedgeAutomaton<UnrankedSymbol,State>(hStates, hFinStates, hRules);
    // hedge: a(a(b(nil), c(nil)), a(b(nil)), a(b(nil), c(nil)))
    List<Tree<UnrankedSymbol>> subs = new LinkedList<Tree<UnrankedSymbol>>();
    // b(nil)
    Tree<UnrankedSymbol> hb = new StdTree<UnrankedSymbol>(b, subs);
    // c(nil)
    Tree<UnrankedSymbol> hc = new StdTree<UnrankedSymbol>(c, subs);
    subs = new LinkedList<Tree<UnrankedSymbol>>();
    subs.add(hb);
    subs.add(hc);
    // a(b(nil), c(nil))
    Tree<UnrankedSymbol> h1 = new StdTree<UnrankedSymbol>(a, subs);
    //  a(b(nil), c(nil))
    Tree<UnrankedSymbol> h2 = new StdTree<UnrankedSymbol>(a, subs);
    subs = new LinkedList<Tree<UnrankedSymbol>>();
    subs.add(hb);
    // a(b(nil))
    Tree<UnrankedSymbol> h3 = new StdTree<UnrankedSymbol>(a, subs);
    subs = new LinkedList<Tree<UnrankedSymbol>>();
    subs.add(h1);
    subs.add(h3);
    subs.add(h2);
    // a(a(b(nil), c(nil)), a(b(nil)), a(b(nil), c(nil)))
    Tree<UnrankedSymbol> h4 = new StdTree<UnrankedSymbol>(a, subs);
    assertTrue(HAOps.decide(ha, h4));
  }
}
