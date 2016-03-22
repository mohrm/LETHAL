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

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import de.uni_muenster.cs.sev.lethal.symbol.common.RankedSymbol;
import de.uni_muenster.cs.sev.lethal.tree.common.Tree;
import de.uni_muenster.cs.sev.lethal.treeautomata.common.FTA;
import de.uni_muenster.cs.sev.lethal.treeautomata.common.FTACreator;
import de.uni_muenster.cs.sev.lethal.treeautomata.common.FTAOps;
import de.uni_muenster.cs.sev.lethal.treeautomata.generic.GenFTA;
import de.uni_muenster.cs.sev.lethal.treeautomata.generic.GenFTACreator;
import de.uni_muenster.cs.sev.lethal.utils.Converter;
import de.uni_muenster.cs.sev.lethal.utils.Pair;

/**
 * Example for an extension of the finite tree automata, explained
 * in the manual.
 *
 * @author Irene
 */
public class ExampleOps {

	/**
	 * Example converter, which converts a set of special states in a new special state.
	 *
	 */
	private class SetStateConverter implements Converter<Set<SpecialState>,SpecialState>{
		/**
		 * @see de.uni_muenster.cs.sev.lethal.utils.Converter#convert(java.lang.Object)
		 */
		@Override
		public SpecialState convert(Set<SpecialState> set){
			String arch = set.toString();
			return new SpecialState(arch);
		}
	}

	private class PairStateConverter implements Converter<Pair<SpecialState,SpecialState>,SpecialState>{
		/**
		 * @see de.uni_muenster.cs.sev.lethal.utils.Converter#convert(java.lang.Object)
		 */
		@Override
		public SpecialState convert(Pair<SpecialState,SpecialState> pair){
			String arch = pair.toString();
			return new SpecialState(arch);
		}
	}

	private class FirstStateConverter implements Converter<SpecialState,SpecialState>{
		/**
		 * @see de.uni_muenster.cs.sev.lethal.utils.Converter#convert(java.lang.Object)
		 */
		@Override
		public SpecialState convert(SpecialState state){
			String arch = "(a," + state.toString() + ")";
			return new SpecialState(arch);
		}
	}


	private class SecondStateConverter implements Converter<SpecialState,SpecialState>{
		/**
		 * @see de.uni_muenster.cs.sev.lethal.utils.Converter#convert(java.lang.Object)
		 */
		@Override
		public SpecialState convert(SpecialState state){
			String arch = "(b," + state.toString() + ")";
			return new SpecialState(arch);
		}
	}

	private class StateBuilder implements Converter<Object,SpecialState>{
		/**
		 * @see de.uni_muenster.cs.sev.lethal.utils.Converter#convert(java.lang.Object)
		 */
		@Override
		public SpecialState convert(Object o){
			return new SpecialState();
		}
	}

	private class SymbolConverter<F> implements Converter<F,F>{
		/**
		 * @see de.uni_muenster.cs.sev.lethal.utils.Converter#convert(java.lang.Object)
		 */
		@Override
		public F convert(F f){
			return f;
		}
	}

	private class IntStateConverter implements Converter<Pair<SpecialState,Integer>,SpecialState>{

		/**
		 * @see de.uni_muenster.cs.sev.lethal.utils.Converter#convert(java.lang.Object)
		 */
		@Override
		public SpecialState convert(Pair<SpecialState,Integer> pair){
			String arch = pair.toString();
			return new SpecialState(arch);
		}
	}
	private class IntConverter implements Converter<Integer,SpecialState>{
		/**
		 * @see de.uni_muenster.cs.sev.lethal.utils.Converter#convert(java.lang.Object)
		 */
		@Override
		public SpecialState convert(Integer i){
			String arch = "(" + i.toString() + ")";
			return new SpecialState(arch);
		}
	}
	private class TreeConverter<F extends RankedSymbol> implements Converter<Tree<F>,SpecialState>{
		/**
		 * @see de.uni_muenster.cs.sev.lethal.utils.Converter#convert(java.lang.Object)
		 */
		@Override
		public SpecialState convert(Tree<F> tree){
			String arch = "(" + tree.toString() + ")";
			return new SpecialState(arch);
		}
	}


	/**
	 * Determinizes a finite tree automaton.
	 *
	 * @param <F> type of ranked symbol in the finite tree automaton and in the trees
	 * @param fta finite tree automaton we deal with
	 * @return determinized finite tree automaton
	 * @see FTAOps#determinize(FTA, FTACreator, Converter)
	 */
	public <F extends RankedSymbol> GenFTA<F,SpecialState> determinize(GenFTA<F,SpecialState> fta){
		return FTAOps.determinize(fta,new GenFTACreator<F,SpecialState>(),new SetStateConverter());
	}

	/**
	 * Completes a finite tree automaton.
	 *
	 * @param <F> type of ranked symbol in the finite tree automaton and in the trees
	 * @param fta finite tree automaton we deal with
	 * @return completed finite tree automaton
	 * @see FTAOps#complete
	 */
	public <F extends RankedSymbol> GenFTA<F,SpecialState> complete(GenFTA<F,SpecialState> fta){
		return FTAOps.complete(fta,new SpecialState(),new GenFTACreator<F,SpecialState>());
	}

	/**
	 * Reduces a finite tree automaton.
	 *
	 * @param <F> type of ranked symbol in the finite tree automaton and in the trees
	 * @param fta finite tree automaton we deal with
	 * @return reduced finite tree automaton
	 * @see FTAOps#reduceFull(FTA, FTACreator)
	 */
	public <F extends RankedSymbol> GenFTA<F,SpecialState> reduceFull(GenFTA<F,SpecialState> fta){
		return FTAOps.reduceFull(fta,new GenFTACreator<F,SpecialState>());
	}

	/**
	 * @param <F> type of ranked symbol in the finite tree automaton and in the trees
	 * @param fta finite tree automaton we deal with
	 * @return minimized finite tree automaton
	 * @see FTAOps#minimize
	 */
	public <F extends RankedSymbol> GenFTA<F,SpecialState> minimize(GenFTA<F,SpecialState> fta){
		GenFTACreator<F,SpecialState> fc = new GenFTACreator<F,SpecialState>();
		return FTAOps.minimize(fta,new SpecialState(),fc,new SetStateConverter(),fc);
	}

	/**
	 * Calculates the intersection of two finite tree automata.
	 *
	 * @param <F> type of ranked symbol in the finite tree automaton and in the trees
	 * @param fta1 first finite tree automaton we deal with
	 * @param fta2 second finite tree automaton we deal with
	 * @return finite tree automaton recognizing the intersection
	 * @see FTAOps#intersectionTD(FTA, FTA, Converter, FTACreator)
	 */
	public <F extends RankedSymbol> GenFTA<F,SpecialState> intersection(GenFTA<F,SpecialState> fta1, GenFTA<F,SpecialState> fta2){
		return FTAOps.intersectionTD(fta1,fta2,new PairStateConverter(), new GenFTACreator<F,SpecialState>());
	}

	/**
	 * Calculates the union of two finite tree automata.
	 *
	 * @param <F> type of ranked symbol in the finite tree automaton and in the trees
	 * @param fta1 first finite tree automaton we deal with
	 * @param fta2 second finite tree automaton we deal with
	 * @return finite tree automaton recognizing the union
	 * @see FTAOps#union
	 */
	public <F extends RankedSymbol> GenFTA<F,SpecialState> union(GenFTA<F,SpecialState> fta1, GenFTA<F,SpecialState> fta2){
		SymbolConverter<F> sc = new SymbolConverter<F>();
		return FTAOps.union(fta1,fta2,new FirstStateConverter(), new SecondStateConverter(),sc,sc,new GenFTACreator<F,SpecialState>());
	}

	/**
	 * Calculates the complement of a finite tree automaton.
	 *
	 * @param <F> type of ranked symbol in the finite tree automaton and in the trees
	 * @param fta  finite tree automaton we deal with
	 * @return finite tree automaton recognizing the complement
	 */
	public <F extends RankedSymbol> GenFTA<F,SpecialState> complement(GenFTA<F,SpecialState> fta){
		return FTAOps.complement(fta,new SetStateConverter(), new GenFTACreator<F,SpecialState>());
	}

	/**
	 * Calculates the difference of two finite tree automata.
	 *
	 * @param <F> type of ranked symbol in the finite tree automaton and in the trees
	 * @param fta1 first finite tree automaton we deal with
	 * @param fta2 second finite tree automaton we deal with
	 * @return finite tree automaton recognizing the difference
	 */
	public <F extends RankedSymbol> GenFTA<F,SpecialState> difference(GenFTA<F,SpecialState> fta1, GenFTA<F,SpecialState> fta2){
		GenFTACreator<F,SpecialState> fc = new GenFTACreator<F,SpecialState>();
		return FTAOps.difference(fta1,fta2,new SetStateConverter(), fc, new PairStateConverter(),fc);
	}

	/**
	 * Computes a finite tree automaton recognizing exactly the given tree
	 *
	 * @param <F> type of ranked symbol in the finite tree automaton and in the trees
	 * @param t tree we deal with
	 * @return finite tree automaton recognizing exactly t
	 * @see FTAOps#computeSingletonFTA(Tree, FTACreator, Converter)
	 */
	public <F extends RankedSymbol> GenFTA<F,SpecialState> computeSingletonFTA(Tree<F> t){
		return FTAOps.computeSingletonFTA(t, new GenFTACreator<F,SpecialState>(), new StateBuilder());
	}

	/**
	 * Computes a finite tree automaton recognizing all trees over the given alphabet.
	 *
	 * @param <F> type of ranked symbol in the finite tree automaton and in the trees
	 * @param alphabet alphabet for the finite tree automaton
	 * @return finite tree automaton recognizing all trees over the given alphabet
	 * @see FTAOps#computeAlphabetFTA
	 */
	public <F extends RankedSymbol> GenFTA<F,SpecialState> computeAlphabetFTA(Collection<F> alphabet){
		return FTAOps.computeAlphabetFTA(alphabet, new SpecialState(),new GenFTACreator<F,SpecialState>());
	}

	/**
	 * Substitutes some constants in a tree by regular tree languages.
	 *
	 * @param <F> type of ranked symbol in the finite tree automaton and in the trees
	 * @param tree where something shall be replace
	 * @param languages what shall be substituted
	 * @return finite tree automaton recognizing the substituion language
	 * @see FTAOps#substitute(Tree, Map, Converter, Converter, Converter, FTACreator)
	 */
	public <F extends RankedSymbol> GenFTA<F,SpecialState> substitute(Tree<F> tree, Map<F,GenFTA<F,SpecialState>> languages){
		return FTAOps.substitute(tree, languages, new IntStateConverter(),new IntConverter(),new TreeConverter<F>(), new GenFTACreator<F,SpecialState>());
	}


}
