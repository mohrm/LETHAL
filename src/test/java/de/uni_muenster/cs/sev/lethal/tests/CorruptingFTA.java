/**
 *
 */
package de.uni_muenster.cs.sev.lethal.tests;

import java.util.Collections;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import de.uni_muenster.cs.sev.lethal.factories.TreeFactory;
import de.uni_muenster.cs.sev.lethal.states.State;
import de.uni_muenster.cs.sev.lethal.symbol.common.RankedSymbol;
import de.uni_muenster.cs.sev.lethal.symbol.standard.StdNamedRankedSymbol;
import de.uni_muenster.cs.sev.lethal.tree.common.Tree;
import de.uni_muenster.cs.sev.lethal.treeautomata.easy.EasyFTA;

/**
 * This test demonstrates why it is not a good idea to manipulate
 * a finite tree automaton indirectly.<br>
 * For this purpose, an EasyFTA foo with one rule a->q and
 * only final state q is instanciated. The state q is instance of a
 * special state type called "CorrState". This class has only one
 * attribute, from which the hash code is calulated and a method
 * manipulate() which increments this attribute, changing the hash code.
 * Since the EasyFTA organizes its final states as a hash set, calling
 * manipulate() on q causes, that q is not found in the final states of
 * foo anymore. Thus, the tree a, which should be accepted by foo, is
 * discarded.
 *
 */
public class CorruptingFTA {


	/**
	 * Special state, which can be manipulated.
	 * @author Martin
	 *
	 */
	static class CorrState implements State {
		int data=0;

		void manipulate() {
			data++;
		}

		/**
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + data;
			return result;
		}

		/**
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			CorrState other = (CorrState) obj;
			if (data != other.data)
				return false;
			return true;
		}
	}

	RankedSymbol a = new StdNamedRankedSymbol<String>("a",0);
	CorrState q = new CorrState();
	EasyFTA foo = new EasyFTA();
	Tree<RankedSymbol> t = TreeFactory.getTreeFactory().makeTreeFromSymbol(a);

	/**
	 * Sets up the easy example.
	 */
	@Before
	public void setUp() {
		a = new StdNamedRankedSymbol<String>("a",0);
		q = new CorrState();
		foo = new EasyFTA();
		foo.addRule(a, Collections.<State>emptyList(), q);
		foo.addToFinals(q);
	}

	/**
	 * The test. It works as follows:<br>
	 * - foo.decide(t) should yield true<br>
	 * - manipulate only final state q<br>
	 * - foo.decide(t) should yield false
	 */
	@Test
	public void testCorruption() {
		/* before the manipulation of the only final state, everything works
		 * as expected */
		Assert.assertTrue(foo.decide(t));
		q.manipulate();
		/*
		 * after the manipulation the hash code of q is different. Thus, it is
		 * not found in foo.getFinalStates() anymore and t is not accepted by foo.
		 */
		Assert.assertFalse(foo.decide(t));
	}
}
