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

import de.uni_muenster.cs.sev.lethal.hedgeautomaton.*;
import de.uni_muenster.cs.sev.lethal.hom.*;

import de.uni_muenster.cs.sev.lethal.parser.fta.FTAParser;
import de.uni_muenster.cs.sev.lethal.parser.hedgeautomaton.HedgeAutomatonParser;
import de.uni_muenster.cs.sev.lethal.parser.homomorphism.HomomorphismParser;
import de.uni_muenster.cs.sev.lethal.parser.tree.TreeParser;

import de.uni_muenster.cs.sev.lethal.symbol.common.*;
import de.uni_muenster.cs.sev.lethal.tree.common.*;
import de.uni_muenster.cs.sev.lethal.treeautomata.common.*;
import de.uni_muenster.cs.sev.lethal.treeautomata.easy.*;

/**
 * Builds the examples and solutions of the exercises in the chapter tutorial in the manual
 * with some time between the parts of the example.<br>
 * Prints out the results.
 *
 * @author Irene
 */
public class Tutorial {

	/**Time between actions.*/
	protected static int sleeptime = 1500;

	/**
	 * Builds the examples and solution of examples in the chapter tutorial in the manual.
	 *
	 * @param args in the first argument the time in milliseconds which shall be between the parts
	 * of the examples
	 */
	public static void main(String[] args) {
		try {
			// Creating trees
			System.out.println("Creating example trees: \n + 1 , and(1,0), or(not(0),and(1,0))");
			Tree<RankedSymbol> tree_1 = TreeParser.parseString("1");
			Tree<RankedSymbol> tree_2 = TreeParser.parseString("and(1,0)");
			Tree<RankedSymbol> tree_3 = TreeParser.parseString("or(not(0),and(1,0))");

			Thread.sleep(sleeptime);

			// Creating fta true formulas
			System.out.println("Creating a finite tree automaton recognizing true formulas");
			String ruleString = "";
			ruleString += "1 -> t! \n";
			ruleString += "0 -> f \n";
			ruleString += "not(t) -> f \n";
			ruleString += "not(f) -> t! \n";
			ruleString += "and(t,t) -> t! \n";
			ruleString += "and(t,f) -> f \n";
			ruleString += "and(f,t) -> f \n";
			ruleString += "and(f,f) -> f \n";
			ruleString += "or(t,t) -> t! \n";
			ruleString += "or(t,f) -> t! \n";
			ruleString += "or(f,t) -> t! \n";
			ruleString += "or(f,f) -> f \n";
			EasyFTA fta_trueFormulas = FTAParser.parseString(ruleString);
			System.out.println(fta_trueFormulas);

			Thread.sleep(sleeptime);

			// Creating fta positive formulas
			System.out.println("Creating a finite tree automaton recognizing positive formulas");
			ruleString = "";
			ruleString += "1 -> q! \n";
			ruleString += "0 -> q! \n";
			ruleString += "not(q!) -> f \n";
			ruleString += "and(q!,q!) -> q! \n";
			ruleString += "or(q!,q!) -> q! \n";
			EasyFTA fta_positiveFormulas = FTAParser.parseString(ruleString);
			System.out.println(fta_positiveFormulas);

			Thread.sleep(sleeptime);

			// Creating fta disjunctive normalform
			System.out.println("Creating a finite tree automaton recognizing formulas in disjunctive normalform");
			ruleString = "";
			ruleString += "1 -> p \n";
			ruleString += "0 -> p \n";
			ruleString += "1 -> q \n";
			ruleString += "0 -> q \n";
			ruleString += "not(p) -> q \n";
			ruleString += "and(q,q) -> r \n";
			ruleString += "and(r,q) -> r \n";
			ruleString += "and(q,r) -> r \n";
			ruleString += "and(r,r) -> r \n";
			ruleString += "or(r,r) -> s! \n";
			ruleString += "or(s!,r) -> s! \n";
			ruleString += "or(r,s!) -> s! \n";
			ruleString += "or(s!,s!) -> s!";
			EasyFTA fta_disjunctiveNF = FTAParser.parseString(ruleString);
			System.out.println(fta_disjunctiveNF);

			Thread.sleep(sleeptime);

			System.out.println("tree 1: " + tree_1 + " is accepted by fta_trueFormulas: " +
					fta_trueFormulas.decide(tree_1)); // yields true
			System.out.println("tree_2: " + tree_2 + " is accepted by fta_trueFormulas: " +
					fta_trueFormulas.decide(tree_2)); // yields false
			System.out.println("tree_3: " + tree_3 + " is accepted by fta_trueFormulas: " +
					fta_trueFormulas.decide(tree_3)); // yields true
			Thread.sleep(sleeptime);
			System.out.println("tree_1: " + tree_1 + " is accepted by fta_positiveFormulas: " +
					fta_positiveFormulas.decide(tree_1));
			System.out.println("tree_2: " + tree_2 + " is accepted by fta_positiveFormulas: " +
					fta_positiveFormulas.decide(tree_2));
			System.out.println("tree_3: " + tree_3 + " is accepted by fta_positiveFormulas: " +
					fta_positiveFormulas.decide(tree_3));
			Thread.sleep(sleeptime);
			System.out.println("tree_1: " + tree_1 + " is accepted by fta_disjunctiveNF: " +
					fta_disjunctiveNF.decide(tree_1));
			System.out.println("tree_2: " + tree_2 + " is accepted by fta_disjunctiveNF: " +
					fta_disjunctiveNF.decide(tree_2));
			System.out.println("tree_3: " + tree_3 + " is accepted by fta_disjunctiveNF: " +
					fta_disjunctiveNF.decide(tree_3));

			Thread.sleep(sleeptime);


			System.out.println("fta_trueFormulas is deterministic: " +
					FTAProperties.checkDeterministic(fta_trueFormulas));
			System.out.println("fta_positiveFormulas is complete: " +
					FTAProperties.checkComplete(fta_positiveFormulas));
			EasyFTA fta_positiveFormulasComplete = EasyFTAOps.complete(fta_positiveFormulas);
			System.out.println("Completed fta_positiveFormulas: \n" + fta_positiveFormulasComplete);
			EasyFTA fta_positiveFormulasReduced = EasyFTAOps.reduceFull(fta_positiveFormulas);
			System.out.println("Reduced fta_positiveFormulas: \n" + fta_positiveFormulasReduced);


			Thread.sleep(sleeptime);

			if (FTAProperties.checkDeterministic(fta_disjunctiveNF)){
				System.out.println("fta_disjunctiveNF is deterministic.");
			} else {
				System.out.println("fta_disjunctiveNF is not deterministic, the deterministic version:");
				EasyFTA fta_deterministic = EasyFTAOps.determinize(fta_disjunctiveNF);
				System.out.println(EasyFTAOps.determinize(fta_deterministic));
			}

			if (FTAProperties.checkComplete(fta_disjunctiveNF)){
				System.out.println("fta_disjunctiveNF is complete.");
			} else {
				System.out.println("fta_disjunctiveNF is not complete, the complete version:");
				System.out.println(EasyFTAOps.complete(fta_disjunctiveNF));
			}

			Thread.sleep(sleeptime);

			EasyFTA fta_falseFormulas = EasyFTAOps.complement(fta_trueFormulas);
			EasyFTA fta_trueConjuctiveNF = EasyFTAOps.intersectionTD(fta_trueFormulas,fta_disjunctiveNF);
			EasyFTA fta_truePositiveFormulas = EasyFTAOps.intersectionTD(fta_positiveFormulas,fta_trueFormulas);
			EasyFTA fta_allFormulas = EasyFTAOps.union(fta_trueFormulas,fta_falseFormulas);
			System.out.println("fta_falseFormulas:" + fta_falseFormulas);
			System.out.println("fta_trueConjuctiveNF:" + fta_trueConjuctiveNF);
			System.out.println("fta_truePositiveFormulas:" + fta_truePositiveFormulas);
			System.out.println("fta_allFormulas:" + fta_allFormulas);

			Thread.sleep(sleeptime);

			System.out.println("The language of fta_trueConjunctiveNF is a subset of the language of fta_trueFormulas: "
					+ FTAProperties.subsetLanguage(fta_trueConjuctiveNF,fta_trueFormulas)); // supplys true
			System.out.println("The language of fta_positiveFormulas is a subset of the language of fta_trueFormulas: "
					+ FTAProperties.subsetLanguage(fta_positiveFormulas,fta_trueFormulas)); // supplys false
			System.out.println("The language of fta_falseFormulas is a subset of the language of fta_allFormulas: "
					+ FTAProperties.subsetLanguage(fta_falseFormulas,fta_allFormulas)); // supplys true
			System.out.println("The language of fta_falseFormulas is the same as the language of fta_positiveFormulas: " +
					FTAProperties.sameLanguage(fta_falseFormulas,fta_positiveFormulas)); // supplys false
			System.out.println("The language of fta_positiveFormulas is finite: " +
					FTAProperties.finiteLanguage(fta_positiveFormulas)); // supplys false
			System.out.println("The complement of the language of fta_allFormulas is empty: " +
					FTAProperties.emptyLanguage(EasyFTAOps.complement(fta_allFormulas))); // supplys true

			Thread.sleep(sleeptime);
			System.out.println("Creating hom_elimAnd \n =====================================");
			Thread.sleep(sleeptime);

			String homMap = "";
			homMap += "0 -> 0 \n";
			homMap += "1 -> 1 \n";
			homMap += "not(x) -> not(x) \n";
			homMap += "or(x,y) -> or(x,y) \n";
			homMap += "and(x,y) -> not(or(not(x),not(y)))";
			EasyHom hom_elimAnd = HomomorphismParser.parseString(homMap);
			System.out.println(hom_elimAnd);

			Thread.sleep(sleeptime);

			System.out.println("Apply to tree_1: hom_elimAnd("+tree_1+") = " + hom_elimAnd.apply(tree_1));
			System.out.println("Apply to tree_2: hom_elimAnd("+tree_2+") = " + hom_elimAnd.apply(tree_2));
			System.out.println("Apply to tree_3: hom_elimAnd("+tree_2+") = " + hom_elimAnd.apply(tree_3));

			if (hom_elimAnd.isLinear()){
				System.out.println("hom_elim is linear, so it can be applied on automata");
				Thread.sleep(sleeptime);
				EasyFTA fta_true_elimAnd = hom_elimAnd.applyOnAutomaton(fta_trueFormulas); // accepts all true formulas over B-{and}
				EasyFTA fta_foo = hom_elimAnd.applyOnAutomaton(fta_positiveFormulas); // the formulas accepted by this automaton are not long
				System.out.println("hom_elim(fta_trueFormulas) \n" + fta_true_elimAnd);
				System.out.println("hom_elim(fta_positiveFormulas) \n" + fta_foo);
			}

			Thread.sleep(sleeptime);
			System.out.println("Creating hedge trees and automata.\n ========================================");
			Thread.sleep(sleeptime);

			Tree<UnrankedSymbol> tree_4 = TreeParser.parseStringAsHedge("ANDALL(not(0),0,ORALL(1))");
			Tree<UnrankedSymbol> tree_5 = TreeParser.parseStringAsHedge("ORALL(ANDALL(1,not(0)),ANDALL(0),ANDALL(not(1),not(0),1))");
			System.out.println("Example trees for the hedge automaton: ANDALL(not(0),0,ORALL(1)) and ORALL(ANDALL(1,not(0)),ANDALL(0),ANDALL(not(1),not(0),1))");

			Thread.sleep(sleeptime);
			String haString = "";
			haString += "1 -> p \n";
			haString += "0 -> p \n";
			haString += "1 -> q \n";
			haString += "0 -> q \n";
			haString += "not(0) -> q \n";
			haString += "not(1) -> q \n";
			haString += "ANDALL((q)*) -> r \n";
			haString += "ORALL((r)*) -> s!";
			EasyHedgeAutomaton ha = HedgeAutomatonParser.parseString(haString);
			System.out.println("Hedge automaton ha :\n" + ha);

			Thread.sleep(sleeptime);

			System.out.println("tree_4 is accepted by ha: " + EasyHAOps.decide(ha,tree_4)); // supplys false
			System.out.println("tree_5 is accepted by ha: " + EasyHAOps.decide(ha,tree_5)); // supplys true
			System.out.println("ha has a finite language: " + EasyHAOps.finiteLanguage(ha)); // supplys false

			Thread.sleep(sleeptime);

			EasyHedgeAutomaton ha_complement = EasyHAOps.complement(ha);
			System.out.println("Complement of ha: \n" + ha_complement);

			System.out.println("LETHAL is ready. What's with you?");

		} catch (Exception e) {}
	}

}
