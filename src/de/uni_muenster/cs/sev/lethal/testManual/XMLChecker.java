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

import de.uni_muenster.cs.sev.lethal.hedgeautomaton.EasyHAOps;
import de.uni_muenster.cs.sev.lethal.hedgeautomaton.EasyHedgeAutomaton;
import de.uni_muenster.cs.sev.lethal.hedgeautomaton.HAOps;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

import de.uni_muenster.cs.sev.lethal.parser.hedgegrammar.HedgeGrammarParser;
import de.uni_muenster.cs.sev.lethal.parser.hedgegrammar.ParseException;
import de.uni_muenster.cs.sev.lethal.parser.xml.XMLTreeParser;
import de.uni_muenster.cs.sev.lethal.symbol.common.UnrankedSymbol;
import de.uni_muenster.cs.sev.lethal.tree.common.Tree;

/**
 * Example for using lethal on XML-schemas.
 * 
 * @author Philipp
 */
public class XMLChecker {
	
	/**
	 * Reads in an XML-document as hedge and a hedge grammar describing an XML-Schema. Applies the schema on the
	 * XML-document and checks whether it satisfies the schema 
	 * 
	 * @param args input files, first the schema file, then the XML-document
	 * @throws ParserConfigurationException foo
	 * @throws SAXException bar
	 * @throws IOException foobar
	 * @throws ParseException 42 
	 */
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, ParseException {
		if (args.length != 2) {System.err.println("Usage: xmlcheck schema_file xml_file"); System.exit(1);}
		
		File schemaFile = new File(args[0]);
		File xmlfile = new File(args[1]);
		
		Tree<UnrankedSymbol> hedge = XMLTreeParser.parseHedgeFromXML(xmlfile);
		
		FileReader fis = new FileReader(schemaFile);
		char[] cbuf = new char[10240];
		int len = fis.read(cbuf);
		
		EasyHedgeAutomaton ha = HedgeGrammarParser.parseString(new String(cbuf,0, len)).getHA(); 
		
		System.out.println("Accept: " + EasyHAOps.decide(ha, hedge));
	}
	
}
