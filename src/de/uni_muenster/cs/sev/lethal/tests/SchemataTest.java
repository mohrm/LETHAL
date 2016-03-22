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


import de.uni_muenster.cs.sev.lethal.hedgeautomaton.HAOps;
import de.uni_muenster.cs.sev.lethal.hedgeautomaton.HedgeAutomaton;
import de.uni_muenster.cs.sev.lethal.hedgegrammar.Concatenation;
import de.uni_muenster.cs.sev.lethal.hedgegrammar.Epsilon;
import de.uni_muenster.cs.sev.lethal.hedgegrammar.Function;
import de.uni_muenster.cs.sev.lethal.hedgegrammar.GrammarExpression;
import de.uni_muenster.cs.sev.lethal.hedgegrammar.GrammarRule;
import de.uni_muenster.cs.sev.lethal.hedgegrammar.HedgeGrammar;
import de.uni_muenster.cs.sev.lethal.hedgegrammar.Nonterminal;
import de.uni_muenster.cs.sev.lethal.hedgegrammar.Range;
import de.uni_muenster.cs.sev.lethal.hedgegrammar.Terminal;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import de.uni_muenster.cs.sev.lethal.states.State;
import de.uni_muenster.cs.sev.lethal.symbol.common.UnrankedSymbol;
import de.uni_muenster.cs.sev.lethal.symbol.standard.StdNamedUnrankedSymbol;
import de.uni_muenster.cs.sev.lethal.tree.common.Tree;
import de.uni_muenster.cs.sev.lethal.tree.standard.StdTree;

/**
 * Tests the use of LETHAL with a schema for XML-documents on an example.
 * 
 * @author Irene
 */
public class SchemataTest {

	/**
	 * Represents an xml schema representing a document with header, body and lists
	 * like this: <br>
	 * (doc) <br>
	 *   (head) (title)(/title) (/head) <br>
	 *   (body) (list)(entry)(/entry)(entry)(/entry)(/list)  (/body) <br>
	 * (/doc) 
	 */
	public HedgeGrammar<UnrankedSymbol> schema_document;

	/**Example document doc(body,head) .*/
	private Tree<UnrankedSymbol> document_0;
	/**Example document doc(head(title),body(list)).*/
	private Tree<UnrankedSymbol> document_1;

	/**
	 * Sets up the unranked symbols we use and the schema in 
	 * {@link SchemataTest#schema_document}. <br>
	 * Additionally two example documents are set: <br>
	 * doc(body,head) <br>
	 * doc(head(title),body(list)) 
	 * 
	 * @throws java.lang.Exception if something goes wrong
	 */
	@Before
	public void setUp() throws Exception {
		schema_document = new HedgeGrammar<UnrankedSymbol>();
		Nonterminal<UnrankedSymbol> n_s = new Nonterminal<UnrankedSymbol>("s");
		Nonterminal<UnrankedSymbol> n_b = new Nonterminal<UnrankedSymbol>("b");
		schema_document.addStart(n_s);

		UnrankedSymbol s_doc = new StdNamedUnrankedSymbol<String>("doc");
		UnrankedSymbol s_body = new StdNamedUnrankedSymbol<String>("body");
		UnrankedSymbol s_head = new StdNamedUnrankedSymbol<String>("head");
		UnrankedSymbol s_list = new StdNamedUnrankedSymbol<String>("list");
		UnrankedSymbol s_entry = new StdNamedUnrankedSymbol<String>("entry");
		UnrankedSymbol s_title = new StdNamedUnrankedSymbol<String>("title");

		Terminal<UnrankedSymbol> t_doc = new Terminal<UnrankedSymbol>(s_doc);
		Terminal<UnrankedSymbol> t_body = new Terminal<UnrankedSymbol>(s_body);
		Terminal<UnrankedSymbol> t_head = new Terminal<UnrankedSymbol>(s_head);
		Terminal<UnrankedSymbol> t_list = new Terminal<UnrankedSymbol>(s_list);
		Terminal<UnrankedSymbol> t_entry = new Terminal<UnrankedSymbol>(s_entry);
		Terminal<UnrankedSymbol> t_title = new Terminal<UnrankedSymbol>(s_title);
		Epsilon<UnrankedSymbol> eps = new Epsilon<UnrankedSymbol>();
		
		GrammarExpression<UnrankedSymbol> exp_help = new Function<UnrankedSymbol>(t_title, eps);
		GrammarExpression<UnrankedSymbol> exp_doc  = new Concatenation<UnrankedSymbol>(new Function<UnrankedSymbol>(t_head,exp_help),n_b);
		GrammarRule<UnrankedSymbol> rule_doc = new GrammarRule<UnrankedSymbol>(n_s,t_doc,exp_doc);
		schema_document.add(rule_doc);

		GrammarExpression<UnrankedSymbol> exp_body = new Function<UnrankedSymbol>(t_list,new Range<UnrankedSymbol>(0,-1,new Function<UnrankedSymbol>(t_entry, eps)));
		GrammarRule<UnrankedSymbol> rule_body = new GrammarRule<UnrankedSymbol>(n_b,t_body,exp_body);
		schema_document.add(rule_body);

		//build up example documents
		document_0 = new StdTree<UnrankedSymbol>(s_doc, Util.makeList(new StdTree<UnrankedSymbol>(s_body), new StdTree<UnrankedSymbol>(s_head)));
		Tree<UnrankedSymbol> doc_help = new StdTree<UnrankedSymbol>(s_body, Util.makeList(new StdTree<UnrankedSymbol>(s_list)));
		document_1 = new StdTree<UnrankedSymbol>(s_doc, Util.makeList(new StdTree<UnrankedSymbol>(s_head,Util.makeList(new StdTree<UnrankedSymbol>(s_title))),doc_help));
	}


	/**
	 * Tests on two examples whether the decide method is correct.
	 */
	@Test
	public void testSchema() {
		System.out.println("schema_document: __________ \n"+ schema_document);
		HedgeAutomaton<UnrankedSymbol,State> ha_doc = schema_document.getHA();

		System.out.println(document_0);
		Assert.assertFalse(HAOps.decide(ha_doc, document_0));

		System.out.println(document_1);
		Assert.assertFalse(HAOps.emptyLanguage(ha_doc));
		Assert.assertTrue(HAOps.decide(ha_doc, document_1));

	}


}
