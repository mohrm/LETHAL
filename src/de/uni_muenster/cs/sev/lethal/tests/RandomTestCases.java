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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import de.uni_muenster.cs.sev.lethal.factories.TreeFactory;
import de.uni_muenster.cs.sev.lethal.hedgegrammar.Alternation;
import de.uni_muenster.cs.sev.lethal.hedgegrammar.Concatenation;
import de.uni_muenster.cs.sev.lethal.hedgegrammar.Epsilon;
import de.uni_muenster.cs.sev.lethal.hedgegrammar.Function;
import de.uni_muenster.cs.sev.lethal.hedgegrammar.GrammarExpression;
import de.uni_muenster.cs.sev.lethal.hedgegrammar.GrammarRule;
import de.uni_muenster.cs.sev.lethal.hedgegrammar.HedgeGrammar;
import de.uni_muenster.cs.sev.lethal.hedgegrammar.Nonterminal;
import de.uni_muenster.cs.sev.lethal.hedgegrammar.Range;
import de.uni_muenster.cs.sev.lethal.hedgegrammar.Terminal;
import de.uni_muenster.cs.sev.lethal.hom.EasyHom;
import de.uni_muenster.cs.sev.lethal.states.NamedState;
import de.uni_muenster.cs.sev.lethal.states.State;
import de.uni_muenster.cs.sev.lethal.symbol.common.BiSymbol;
import de.uni_muenster.cs.sev.lethal.symbol.common.RankedSymbol;
import de.uni_muenster.cs.sev.lethal.symbol.common.UnrankedSymbol;
import de.uni_muenster.cs.sev.lethal.symbol.common.Variable;
import de.uni_muenster.cs.sev.lethal.symbol.standard.InnerSymbol;
import de.uni_muenster.cs.sev.lethal.symbol.standard.LeafSymbol;
import de.uni_muenster.cs.sev.lethal.symbol.standard.StdNamedUnrankedSymbol;
import de.uni_muenster.cs.sev.lethal.symbol.standard.StdVariable;
import de.uni_muenster.cs.sev.lethal.tree.common.Tree;
import de.uni_muenster.cs.sev.lethal.treetransducer.EasyTT;
import de.uni_muenster.cs.sev.lethal.treetransducer.EasyTTRule;

/**
 * Generates some random test cases, for example trees and homomorphisms.
 *
 * @author Irene
 *
 */
class RandomTestCases {

	/**Used alphabet.*/
	private UnrankedSymbol[] unrankedSymbols;
	/** Random object to produce randomize objects.*/
	private Random rand;

	/**
	 * Constructor which generates a random object.
	 */
	public RandomTestCases(){
		rand = new Random();
	}

	/**
	 * Generates a tree over the given alphabet.
	 *
	 * @param alphabet alphabet over which the tree shall be produced
	 * @param maxHeight maximal height of the tree
	 * @return generated tree
	 */
	public Tree<RankedSymbol> randomTree(RankedSymbol[] alphabet, int maxHeight){
		if (maxHeight <= 0){
			for (RankedSymbol s: alphabet){
				if (s.getArity() == 0){
					return TreeFactory.getTreeFactory().makeTreeFromSymbol(s);
				}
			}
			throw new IllegalArgumentException("Alphabet must have one symbol with arity 0.");
		}
		else {
			int n = alphabet.length;
			RankedSymbol root = alphabet[rand.nextInt(n)];
			//subtrees erzeugen
			LinkedList<Tree<RankedSymbol>> subtrees = new LinkedList<Tree<RankedSymbol>>();
			for (int i=0; i<root.getArity();i++){
				subtrees.add(randomTree(alphabet,maxHeight-1));
			}
			return TreeFactory.getTreeFactory().makeTreeFromSymbol(root,subtrees);
		}
	}


	/**
	 * Generates a homomorphism with the given parameters.
	 *
	 * @param startAlph start alphabet
	 * @param destAlph destination alphabet
	 * @param maxTreeHeight maximum height of the trees in the homomorphism
	 * @param linear whether the homomorphism shall be linear
	 * @return Homomorphism over the given alphabets
	 */
	public EasyHom randomHom(RankedSymbol[] startAlph, RankedSymbol[] destAlph, int maxTreeHeight, boolean linear){
		Map<RankedSymbol, Tree<? extends BiSymbol<RankedSymbol, Variable>>> hom =
			new HashMap<RankedSymbol,Tree<? extends BiSymbol<RankedSymbol,Variable>>>();
		for (RankedSymbol f: startAlph){
			LinkedList<Variable> vars = new LinkedList<Variable>();
			for (int i=0; i<f.getArity();i++){
				vars.add(new StdVariable(i));
			}
			hom.put(f, randomVarTree(destAlph,vars, maxTreeHeight,linear));
		}
		return new EasyHom(hom);
	}

	/**
	 * Generates a tree over ranked symbols and variables. Used to generate a homomorphism.
	 *
	 * @param alphabet alphabet of ranked symbols
	 * @param vars list of variables can occur in the tree
	 * @param maxHeight maximum height of the tree
	 * @param linear whether the generated tree shall be linear
	 * @return generated tree with variables and ranked symbols
	 */
	public Tree<BiSymbol<RankedSymbol,Variable>> randomVarTree(RankedSymbol[] alphabet, List<Variable> vars, int maxHeight, boolean linear){
		if (maxHeight <= 0){
			// get symbols with arity 0
			LinkedList<RankedSymbol> alph = new LinkedList<RankedSymbol>();
			for (int i= 0; i<alphabet.length; i++){
				if (alphabet[i].getArity() == 0)
					alph.add(alphabet[i]);
			}
			// decide whether variable or not
			int par = rand.nextInt(alph.size() + vars.size());
			/*	if (par <= 0){
				throw new IllegalArgumentException("Alphabet must have one symbol with arity 0.");
			}*/
			if (par < vars.size()){
				// produce new tree with one variable
				BiSymbol<RankedSymbol,Variable> root = new LeafSymbol<RankedSymbol,Variable>(vars.get(par));
				if (linear){
					vars.remove(vars.get(par));
				}
				return TreeFactory.getTreeFactory().makeTreeFromSymbol(root);
			} else {
				// produce a tree with a ranked symbol with arity 0
				BiSymbol<RankedSymbol,Variable> root = new InnerSymbol<RankedSymbol,Variable>(alph.get(par-vars.size()));
				return TreeFactory.getTreeFactory().makeTreeFromSymbol(root);
			}
		}
		else {
			int n = alphabet.length;
			RankedSymbol root = alphabet[rand.nextInt(n)];
			// generate subtrees
			LinkedList<Tree<BiSymbol<RankedSymbol, Variable>>> subtrees =
				new LinkedList<Tree<BiSymbol<RankedSymbol, Variable>>>();
			for (int i=0; i<root.getArity();i++){
				subtrees.add(randomVarTree(alphabet,vars,maxHeight-1,linear));
			}
			// produce new tree
			BiSymbol<RankedSymbol,Variable> s =new InnerSymbol<RankedSymbol,Variable>(root);
			return TreeFactory.getTreeFactory().makeTreeFromSymbol(s,subtrees);
		}
	}


	/**
	 * Generates a random tree transducer with the given parameters.
	 *
	 * @param startAlph start alphabet
	 * @param destAlph destination alphabet
	 * @param maxTreeHeight maximum tree height
	 * @param linear whether the tree transducer shall be linear
	 * @param numStates maximal number of states
	 * @param numRules number of rules
	 * @param numFinal number of final states
	 * @return generated tree transducer
	 */
	public EasyTT randomTT(RankedSymbol[] startAlph, RankedSymbol[] destAlph, int maxTreeHeight,
			boolean linear, int numStates, int numRules, int numFinal){
		// States
		State[] states = new State [numStates];
		for (int i=0;i<numStates;++i) {
			states[i] = new NamedState<Integer>(i);
		}

		// Final states
		ArrayList<State> finals = new ArrayList<State>();
		for (int i=0;i<numFinal;++i)
			finals.add(states[i]);

		// Rules
		ArrayList<EasyTTRule> rules = new ArrayList<EasyTTRule>();
		for (int i=0;i<numRules;++i) {
			// Create rule
			RankedSymbol f = startAlph[rand.nextInt(startAlph.length)];
			State dest = states[rand.nextInt(numStates)];
			List<State> source = new ArrayList<State>(f.getArity());
			List<Variable> vars = new ArrayList<Variable>();
			for (int j=0;j<f.getArity();++j){
				source.add(states[rand.nextInt(numStates)]);
				vars.add(new StdVariable(j));
			}
			rules.add(new EasyTTRule(f,source,dest,this.randomVarTree(destAlph, vars, maxTreeHeight, linear)));
			//res.createRule(f,source,dest);
		}
		return new EasyTT(finals,Arrays.asList(startAlph),Arrays.asList(destAlph),rules);
	}


	/**
	 * Generates a language with unranked symbols for hedge automata.
	 *
	 * @param numSymbols number of symbols which shall be generated.
	 */
	public void generateUnrankedLanguage(int numSymbols){
		unrankedSymbols = new UnrankedSymbol[numSymbols];
		for (int i=0; i<numSymbols; i++){
			unrankedSymbols[i]= new StdNamedUnrankedSymbol<Integer>(i);
		}
	}

	/**
	 * Generates a hedge grammar with the given parameters.
	 *
	 * @param numNonterminals number of nonterminals occuring
	 * @param numRules number of rules occuring
	 * @param maxDeepthExp maximal deepth of regular expressions
	 * @return generated hedge grammar
	 */
	public HedgeGrammar<UnrankedSymbol> randomHedgeGrammar(int numNonterminals, int numRules, int maxDeepthExp){
		if (unrankedSymbols == null)
			generateUnrankedLanguage(10);

		HedgeGrammar<UnrankedSymbol> schema =  new HedgeGrammar<UnrankedSymbol>();

		//generate nonterminals and start symbol
		Nonterminal<UnrankedSymbol>[] nonterminals = new Nonterminal[numNonterminals];
		for (int i=0; i<numNonterminals; i++){
			nonterminals[i] = new Nonterminal<UnrankedSymbol>("n"+i);
			if (rand.nextInt(3)==0)
				schema.addStart(nonterminals[i]);
		}

		// generate terminals
		Terminal<UnrankedSymbol>[] terminals = new Terminal[unrankedSymbols.length];
		for (int i=0; i<unrankedSymbols.length; i++){
			terminals[i] = new Terminal<UnrankedSymbol>(unrankedSymbols[i]);
		}

		// generate rules
		for (int i=0; i<numRules;i++){
			GrammarExpression<UnrankedSymbol> exp = randomExpression(nonterminals,terminals,maxDeepthExp);
			schema.add(new GrammarRule<UnrankedSymbol>(
					nonterminals[rand.nextInt(numNonterminals)],
					terminals[rand.nextInt(terminals.length)], exp));
		}

		return schema;
	}

	private GrammarExpression<UnrankedSymbol> randomExpression(
			Nonterminal<UnrankedSymbol>[] nonterminals,
			Terminal<UnrankedSymbol>[] terminals, int maxDeepth) {
		// leaf expression
		if (maxDeepth <= 0){
			int c = rand.nextInt(nonterminals.length+1);
			if (c == 0)
				return new Epsilon<UnrankedSymbol>();
			else
				return nonterminals[c-1];
		} else {
			int c = rand.nextInt(4);
			if (c==0)
				return new Alternation<UnrankedSymbol>(randomExpression(nonterminals,terminals,maxDeepth-1),
						randomExpression(nonterminals,terminals,maxDeepth-1));
			else if (c==1)
				return new Concatenation<UnrankedSymbol>(randomExpression(nonterminals,terminals,maxDeepth-1),
						randomExpression(nonterminals,terminals,maxDeepth-1));
			else if (c==2)
				return new Function<UnrankedSymbol>(terminals[rand.nextInt(terminals.length)],
						randomExpression(nonterminals,terminals,maxDeepth-1));
			else {
				int high = rand.nextInt();
				int low = 0;
				if (high <0)
					low = rand.nextInt(high);
				else low = rand.nextInt(high);
				return new Range<UnrankedSymbol>(low, high,
						randomExpression(nonterminals,terminals,maxDeepth-1));
			}
		}
	}


}


