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

import de.uni_muenster.cs.sev.lethal.hom.EasyHom;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uni_muenster.cs.sev.lethal.parser.tree.ParseException;
import de.uni_muenster.cs.sev.lethal.parser.tree.TreeParser;
import de.uni_muenster.cs.sev.lethal.states.NamedState;
import de.uni_muenster.cs.sev.lethal.states.State;
import de.uni_muenster.cs.sev.lethal.symbol.common.BiSymbol;
import de.uni_muenster.cs.sev.lethal.symbol.common.RankedSymbol;
import de.uni_muenster.cs.sev.lethal.symbol.common.UnrankedSymbol;
import de.uni_muenster.cs.sev.lethal.symbol.common.Variable;
import de.uni_muenster.cs.sev.lethal.symbol.standard.InnerSymbol;
import de.uni_muenster.cs.sev.lethal.symbol.standard.LeafSymbol;
import de.uni_muenster.cs.sev.lethal.symbol.standard.StdNamedRankedSymbol;
import de.uni_muenster.cs.sev.lethal.symbol.standard.StdNamedUnrankedSymbol;
import de.uni_muenster.cs.sev.lethal.symbol.standard.StdVariable;
import de.uni_muenster.cs.sev.lethal.tests.Util;
import de.uni_muenster.cs.sev.lethal.tree.common.Tree;
import de.uni_muenster.cs.sev.lethal.tree.common.TreeOps;
import de.uni_muenster.cs.sev.lethal.tree.standard.StdTree;
import de.uni_muenster.cs.sev.lethal.tree.standard.StdTreeCreator;
import de.uni_muenster.cs.sev.lethal.treeautomata.common.FTAProperties;
import de.uni_muenster.cs.sev.lethal.treeautomata.easy.EasyFTA;
import de.uni_muenster.cs.sev.lethal.treeautomata.easy.EasyFTAEpsRule;
import de.uni_muenster.cs.sev.lethal.treeautomata.easy.EasyFTAOps;
import de.uni_muenster.cs.sev.lethal.treeautomata.easy.EasyFTARule;
import de.uni_muenster.cs.sev.lethal.treeautomata.generic.GenFTA;
import de.uni_muenster.cs.sev.lethal.treeautomata.generic.GenFTAOps;
import de.uni_muenster.cs.sev.lethal.treeautomata.generic.GenFTARule;
import de.uni_muenster.cs.sev.lethal.utils.Converter;
import de.uni_muenster.cs.sev.lethal.utils.Pair;
import de.uni_muenster.cs.sev.lethal.factories.StateFactory;
import de.uni_muenster.cs.sev.lethal.factories.TreeFactory;

/**
 * Lists all the example used in the third chapter of the manual to check
 * whether they can be applied.
 * 
 * @author Irene
 */
public class CoreExamples {


	//Let F extend StdNamedRankedSymbol<String> and
	// let Q extend NamedState<String>
	private class F extends StdNamedRankedSymbol<String>{
		/**
		 * Creates a new instance of type F with specified name and arity
		 * @param name name of new symbol
		 * @param arity arity of new symbol
		 */
		public F(String name, int arity) {
			super(name, arity);
		}}
	
	private class Q extends NamedState<String>{
		/**
		 * @param newname
		 */
		public Q(String newname) {
			super(newname);
		}}
	
	/**
	 * Implements all examples from the core-part of the manual.
	 * 
	 * @param args
	 */
	public static void main(String[] args){
		

		// Creating symbols
		RankedSymbol sym1 = new StdNamedRankedSymbol<String>("s",1);
		UnrankedSymbol sym2 = new StdNamedUnrankedSymbol<String>("s");
		BiSymbol<StdNamedRankedSymbol<String>,Integer> plus = new InnerSymbol<StdNamedRankedSymbol<String>,Integer>(new StdNamedRankedSymbol<String>("+",2));
		BiSymbol<StdNamedRankedSymbol<String>,Integer> mult = new InnerSymbol<StdNamedRankedSymbol<String>,Integer>(new StdNamedRankedSymbol<String>("*",2));
		BiSymbol<StdNamedRankedSymbol<String>,Integer> x = new InnerSymbol<StdNamedRankedSymbol<String>,Integer>(new StdNamedRankedSymbol<String>("x",0));
		BiSymbol<StdNamedRankedSymbol<String>,Integer> y = new InnerSymbol<StdNamedRankedSymbol<String>,Integer>(new StdNamedRankedSymbol<String>("y",0));
		BiSymbol<StdNamedRankedSymbol<String>,Integer> i1 = new LeafSymbol<StdNamedRankedSymbol<String>,Integer>(new Integer(1));
		BiSymbol<StdNamedRankedSymbol<String>,Integer> i42 = new LeafSymbol<StdNamedRankedSymbol<String>,Integer>(new Integer(42));

		System.out.println("Symbols: " + sym1+ "," + sym2+ "," +  plus + 
				"," + mult + "," + x + "," + y + "," + i1 + "," + i42);

		// Creating Trees
		try {
			Tree<RankedSymbol> tree_2 = TreeParser.parseString("x");
			Tree<RankedSymbol> tree_3 = TreeParser.parseString("+(*(x,y),y)");
			// calculates height
			int height = TreeOps.getHeight(tree_3);
			// calculates all symbols occurring in the tree
			Set<RankedSymbol> symbols = TreeOps.getAllContainedSymbols(tree_3);
			// checks whether a symbol is contained in the tree
			boolean f = TreeOps.containsSymbol(tree_2, new StdNamedRankedSymbol<String>("x",0));
			System.out.println(tree_2 + " height: " + height);
			System.out.println(tree_3 + " contained symbols: " + symbols);
			System.out.println(tree_2 + " contains symbol x: " + f);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Converter<RankedSymbol,BiSymbol<RankedSymbol,Integer>> conv  = 
			new Converter<RankedSymbol,BiSymbol<RankedSymbol,Integer>>(){
			@Override
			public BiSymbol<RankedSymbol,Integer> convert(RankedSymbol a) {
				return new InnerSymbol<RankedSymbol,Integer>(a);
			}
		};

		RankedSymbol  x1 = new StdNamedRankedSymbol<String>("x",0);
		Tree<RankedSymbol> tree_x = TreeFactory.getTreeFactory().makeTreeFromSymbol(x1);

		// converts a tree over the first symbol type into a tree over the second symbol type
		Tree<BiSymbol<RankedSymbol,Integer>> bitree_x = TreeOps.convert(tree_x, conv, 
				new StdTreeCreator<BiSymbol<RankedSymbol,Integer>>());


		// Creating states
		StateFactory stateFact = StateFactory.getStateFactory();
		State s1 = stateFact.makeState();
		State s2 = stateFact.makeState("s2");
		State s3 = stateFact.makeState(new Pair<State,State>(s1,s2));

		// careful with named states
		LinkedList<Integer> name = new LinkedList<Integer>();
		name.add(42);
		name.add(17);
		State q = new NamedState<List<Integer>>(name);
		name.add(23);

		// Creating rules
		RankedSymbol plus_1 = new StdNamedRankedSymbol<String>("+",2);
		RankedSymbol x_1 = new StdNamedRankedSymbol<String>("x",0);
		RankedSymbol y_1 = new StdNamedRankedSymbol<String>("y",0);
		EasyFTARule r1 = new EasyFTARule(x_1,s1);
		EasyFTARule r2 = new EasyFTARule(plus_1,s2,s1,s1);
		List<State> src = new LinkedList<State>();
		src.add(s1); 
		src.add(s1);
		GenFTARule<RankedSymbol,State> r3 = new GenFTARule<RankedSymbol,State>(plus_1,src,s2);

		// Creating an EasyFTA
		Set<EasyFTARule> rules = new HashSet<EasyFTARule>();
		rules.add(r1); rules.add(r2);
		Set<State> states = new HashSet<State>();
		states.add(s1); states.add(s2);
		Set<State> finalStates = new HashSet<State>();
		finalStates.add(s2);
		Set<RankedSymbol> alphabet = new HashSet<RankedSymbol>();
		alphabet.add(plus_1); alphabet.add(x_1);

		// possiblities to create EasyFTA
		EasyFTA fta1 = new EasyFTA(alphabet,states,finalStates,rules);
		EasyFTA fta2 = new EasyFTA(rules, finalStates);
		EasyFTA fta3 = new EasyFTA(rules,finalStates);
		EasyFTA fta5 = new EasyFTA(rules, s2);
		EasyFTA fta4 = new EasyFTA(fta1);
		System.out.println(fta1);

		// Create GenFTA
		StdNamedRankedSymbol<String> plus_2 = new StdNamedRankedSymbol<String>("+",2);
		StdNamedRankedSymbol<String> x_2 = new StdNamedRankedSymbol<String>("x",0);
		StdNamedRankedSymbol<String> y_2 = new StdNamedRankedSymbol<String>("y",0);
	
		GenFTARule<StdNamedRankedSymbol<String>,State> r4 = new GenFTARule<StdNamedRankedSymbol<String>,State>(x_2,new LinkedList<State>(),s1);
		List<State> src2 = new LinkedList<State>();
		src2.add(s1); src2.add(s1);
		GenFTARule<StdNamedRankedSymbol<String>,State> r5 = new GenFTARule<StdNamedRankedSymbol<String>,State>(plus_2, src2, s2);

		Set<GenFTARule<StdNamedRankedSymbol<String>,State>> rules2 = new HashSet<GenFTARule<StdNamedRankedSymbol<String>,State>>();
		rules2.add(r4); rules2.add(r5);
		Set<State> states2 = new HashSet<State>();
		states2.add(s1); states2.add(s2);
		Set<State> finalStates2 = new HashSet<State>();
		finalStates.add(s2);
		Set<StdNamedRankedSymbol<String>> alphabet2 = new HashSet<StdNamedRankedSymbol<String>>();
		alphabet.add(plus_2); alphabet.add(x_2);

		// possiblities to create GenFTA
		GenFTA<StdNamedRankedSymbol<String>,State> ftag1 = new GenFTA<StdNamedRankedSymbol<String>,State>(alphabet2,states2,finalStates2,rules2);
		GenFTA<StdNamedRankedSymbol<String>,State> ftag2 = new GenFTA<StdNamedRankedSymbol<String>,State>(rules2,finalStates2);
		GenFTA<StdNamedRankedSymbol<String>,State> ftag3 = new GenFTA<StdNamedRankedSymbol<String>,State>(rules2,finalStates2);
		GenFTA<StdNamedRankedSymbol<String>,State> ftag4 = new GenFTA<StdNamedRankedSymbol<String>,State>(ftag1);

		// epsilon finite tree automata
		// s1 and s2 are some earlier defined states, plus and x earlier defined RankedSymbols
		EasyFTARule r6 = new EasyFTARule(x_1,s1);
		EasyFTARule r7 = new EasyFTARule(plus_1, s2,s1,s1);
		EasyFTAEpsRule r8 =  new EasyFTAEpsRule(s1,s2);

		Set<EasyFTARule> rules3 = new HashSet<EasyFTARule>();
		rules3.add(r6); rules3.add(r7);
		Set<EasyFTAEpsRule> epsRules = new HashSet<EasyFTAEpsRule>();
		epsRules.add(r8);
		Set<State> states3 = new HashSet<State>();
		states3.add(s1); states3.add(s2);
		Set<State> finalStates3 = new HashSet<State>();
		finalStates3.add(s2);
		Set<RankedSymbol> alphabet3 = new HashSet<RankedSymbol>();
		alphabet3.add(plus_1); alphabet3.add(x_1);

		// creating a finite tree automaton with epsilon rules
		// conversion is done directly
		EasyFTA ftae1 = new EasyFTA(rules, epsRules, finalStates);
		EasyFTA ftae2 = new EasyFTA(rules, epsRules, finalStates);
		for (State q0: states)
			ftae2.addState(q0);


		// operations

		// let fta1 be an automaton over trees of arithmetic expressions,
		// which recognizes all trees containing no y.
		Tree<RankedSymbol> tree_1 = null; 
		Tree<RankedSymbol> tree_2 = null;
		try {
			tree_1 = TreeParser.parseString("+(*(x,y),y)");
			tree_2 = TreeParser.parseString("+(x,x)");
		} catch (ParseException e) {
			e.printStackTrace();
		}

		FTAProperties.decide(fta1,tree_1); // should yield false
		fta1.decide(tree_2); // should yield true


		if (!FTAProperties.checkDeterministic(fta1)){
			EasyFTA detfta1 = EasyFTAOps.determinize(fta1);
		}
		if (!FTAProperties.checkDeterministic(ftag2)){
			GenFTA<StdNamedRankedSymbol<String>, NamedState<Set<State>>> detfta2 = GenFTAOps.determinize(ftag2);
		}

		if (!FTAProperties.checkComplete(fta1)){
			EasyFTA detfta1 = EasyFTAOps.complete(fta1);
		}
		GenFTA<StdNamedRankedSymbol<String>,State> detftag2 = GenFTAOps.complete(ftag1, new NamedState<String>("qbot"));
		FTAProperties.checkComplete(detftag2);
		
		EasyFTA ftar = EasyFTAOps.reduceBottomUp(fta1);
		EasyFTA ftar2 = EasyFTAOps.reduceTopDown(ftar);
		EasyFTA ftar3 = EasyFTAOps.reduceFull(fta1);

		GenFTA<StdNamedRankedSymbol<String>,State>  ftar4 = GenFTAOps.reduceBottomUp(ftag2);
		GenFTA<StdNamedRankedSymbol<String>,State>  ftar5 = GenFTAOps.reduceTopDown(ftar4);
		GenFTA<StdNamedRankedSymbol<String>,State>  ftar6 = GenFTAOps.reduceFull(ftag2);
		
		
		EasyFTA fta1m = EasyFTAOps.minimize(fta1);

		GenFTA<StdNamedRankedSymbol<String>, NamedState<Set<State>>>  fta2m = GenFTAOps.minimize(ftag2, new NamedState<String>("qnew"));
	
		FTAProperties.subsetLanguage(fta1,fta2);
		FTAProperties.sameLanguage(fta1,fta2);

		if (!FTAProperties.emptyLanguage(fta3)){
		  if (FTAProperties.finiteLanguage(fta3)){
		    System.out.println("The regular tree language is finite, but not emtpy.");
		  }
		}
		
		EasyFTA fta1c = EasyFTAOps.complement(fta1);
		Set<RankedSymbol> alphabet6 = new HashSet<RankedSymbol>();
		alphabet6.add(plus_1); alphabet6.add(x_1); alphabet6.add(y_1);
		EasyFTA fta1ca = EasyFTAOps.complementAlphabet(fta1,alphabet6);
		
		
		EasyFTA ftaUnion = EasyFTAOps.union(fta1,fta2);
		EasyFTA ftaIntersection = EasyFTAOps.intersectionTD(fta1,fta2);
		EasyFTA ftaDifference = EasyFTAOps.difference(fta1,fta2);
		
		
		Tree<RankedSymbol> witness = EasyFTAOps.constructTreeFrom(fta1);
		EasyFTA ftaSingleton = EasyFTAOps.computeSingletonFTA(witness);

		Set<StdNamedRankedSymbol<String>> alphabet7 = new HashSet<StdNamedRankedSymbol<String>>();
		alphabet7.add(plus_2); alphabet7.add(x_2); alphabet7.add(y_2);
		GenFTA<StdNamedRankedSymbol<String>,State> ftaAlphg = GenFTAOps.computeAlphabetFTA(alphabet7);

		
		// Let F extend StdNamedRankedSymbol<String> and
		// let Q extend NamedState<String>
		// Both shall be equipped with appropriate constructors.
		// let list() be a method which takes arbitrarily many objects of a certain type 
		// and returns a list of these objects
		CoreExamples blubbs = new CoreExamples();
		
		F x_3 = blubbs.new F("x",0);
		F y_3 = blubbs.new F("y",0);
		F plus_3 = blubbs.new F("+",2);

		// fta1 accepts everything containing no x or y, i.e. nothing
		GenFTA<F,Q> fta1s = new GenFTA<F,Q>();
		// fta2 accepts everything containg no y
		Q stat = blubbs.new Q("string");
		Set<Q> finalStates4 = new HashSet<Q>();
		finalStates4.add(stat);
		Set<GenFTARule<F,Q>> rules4 = new HashSet<GenFTARule<F,Q>>();
		rules4.add(new GenFTARule<F,Q>(y_3,new LinkedList<Q>(), stat));
		rules4.add(new GenFTARule<F,Q>(plus_3, Util.makeList(stat,stat), stat));
		
		GenFTA<F,Q> fta2s = new GenFTA<F,Q>(rules4,finalStates4);

		Map<F,GenFTA<F,Q>> languages = new HashMap<F,GenFTA<F,Q>>();
		languages.put(x_3,fta1s); 
		languages.put(y_3,fta2s);

		Tree<F> t = new StdTree<F>(plus_3, Util.makeList(new StdTree<F>(x_3),new StdTree<F>(y_3)));

		GenFTA<F, NamedState<?>> ftasubs = GenFTAOps.substitute(t,languages);
		
		
		
		
		
		// Homomorphism
		
		// creating the used symbols
		RankedSymbol t1 = new StdNamedRankedSymbol<String>("1",0);
		RankedSymbol f = new StdNamedRankedSymbol<String>("0",0);
		RankedSymbol not = new StdNamedRankedSymbol<String>("not",1);
		RankedSymbol and = new StdNamedRankedSymbol<String>("and",2);
		RankedSymbol or = new StdNamedRankedSymbol<String>("or",2);
		
		// creating used variables
		Variable v0 = new StdVariable(0);
		Variable v1 = new StdVariable(0);
		
		// as BiSymbols
		BiSymbol<RankedSymbol,Variable> bt1 = new InnerSymbol<RankedSymbol,Variable>(t1);
		BiSymbol<RankedSymbol,Variable> bf = new InnerSymbol<RankedSymbol,Variable>(f);
		BiSymbol<RankedSymbol,Variable> bnot = new InnerSymbol<RankedSymbol,Variable>(not);
		BiSymbol<RankedSymbol,Variable> band = new InnerSymbol<RankedSymbol,Variable>(and);
		BiSymbol<RankedSymbol,Variable> bor = new InnerSymbol<RankedSymbol,Variable>(or);
		BiSymbol<RankedSymbol,Variable> bv0 = new LeafSymbol<RankedSymbol,Variable>(v0);
		BiSymbol<RankedSymbol,Variable> bv1 = new LeafSymbol<RankedSymbol,Variable>(v1);
		
		// for creating trees
		TreeFactory tf = TreeFactory.getTreeFactory();
		Tree<BiSymbol<RankedSymbol,Variable>> vtree = tf.makeTreeFromSymbol(bnot,
				Util.makeList(tf.makeTreeFromSymbol(bor,
						Util.makeList(tf.makeTreeFromSymbol(bnot, Util.makeList(tf.makeTreeFromSymbol(bv0))),
								tf.makeTreeFromSymbol(bnot,Util.makeList(tf.makeTreeFromSymbol(bv1)))))));

		// build up, possibility 1
		Map<RankedSymbol, Tree<? extends BiSymbol<RankedSymbol, Variable>>> map1 = new HashMap<RankedSymbol, Tree<? extends BiSymbol<RankedSymbol, Variable>>>();
		map1.put(t1,tf.makeTreeFromSymbol(bt1));
		map1.put(f,tf.makeTreeFromSymbol(bf));
		map1.put(not,tf.makeTreeFromSymbol(bnot,Util.makeList(tf.makeTreeFromSymbol(bv0))));
		map1.put(and,vtree);
		EasyHom hom1 = new EasyHom(map1);

		// build up, possibility 2 (only in EasyHom)
		Set<RankedSymbol> alphabet9 = new HashSet<RankedSymbol>();
		alphabet9.add(t1);
		alphabet9.add(f);
		alphabet9.add(not);
		Map<RankedSymbol, Tree<? extends BiSymbol<RankedSymbol, Variable>>> map2 = new HashMap<RankedSymbol, Tree<? extends BiSymbol<RankedSymbol, Variable>>>();
		map2.put(and,vtree);
		EasyHom hom = new EasyHom(map2, alphabet);

		
		RankedSymbol wv0 = new StdNamedRankedSymbol<String>("v0",0);
		RankedSymbol wv1 = new StdNamedRankedSymbol<String>("v1",0);

		// for creating trees
		Tree<RankedSymbol> vtree3 = tf.makeTreeFromSymbol(not, Util.makeList(tf.makeTreeFromSymbol(or,
						Util.makeList(tf.makeTreeFromSymbol(not, Util.makeList(tf.makeTreeFromSymbol(wv0))),
								tf.makeTreeFromSymbol(not,Util.makeList(tf.makeTreeFromSymbol(wv1)))))));
		
		// build up
		Map<RankedSymbol,Integer> toInts = new HashMap<RankedSymbol,Integer>();
		toInts.put(wv0,new Integer(0));
		toInts.put(wv1,new Integer(1));
		Map<RankedSymbol,Tree<RankedSymbol>> map3 = new HashMap<RankedSymbol,Tree<RankedSymbol>>();
		map3.put(t1,tf.makeTreeFromSymbol(t1));
		map3.put(f,tf.makeTreeFromSymbol(f));
		map3.put(not,tf.makeTreeFromSymbol(not,Util.makeList(tf.makeTreeFromSymbol(wv0))));
		map3.put(and,vtree3);
		EasyHom hom2 = new EasyHom(toInts,map3);
		
		
		// apply to trees
		Tree<RankedSymbol> t3 = tf.makeTreeFromSymbol(t1);
		Tree<RankedSymbol> t2 = tf.makeTreeFromSymbol(and,Util.makeList(tf.makeTreeFromSymbol(not,
				Util.makeList(tf.makeTreeFromSymbol(f))),tf.makeTreeFromSymbol(t1)));
		Tree<RankedSymbol> th1 = hom1.apply(t3);
		Tree<RankedSymbol> th2 = hom1.apply(t2);

		// apply to tree automaton
		EasyFTA ftah1 = new EasyFTA(); // build up a tree automaton accepting true formulas over {and,not,0,1}
		if (hom1.isLinear()){
		  EasyFTA ftah3 = hom1.applyOnAutomaton(ftah1);
		}
		// apply to inverse automaton
		EasyFTA ftah2 = new EasyFTA();  // build up a tree automaton working on alphabet {or,not,0,1}
		EasyFTA ftah4 = hom1.applyInverseOnAutomaton(ftah2);
	
	}
	
	
	
}
