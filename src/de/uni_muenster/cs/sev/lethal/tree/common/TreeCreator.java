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
package de.uni_muenster.cs.sev.lethal.tree.common;

import java.util.List;

import de.uni_muenster.cs.sev.lethal.tree.common.Tree;

import de.uni_muenster.cs.sev.lethal.symbol.common.Symbol;

/**
 * Creator object which knows how to create new trees.
 *
 * @param <S> symbol type of tree
 * @param <T> type of tree
 * @author Dorothea, Irene, Martin
 */
public interface TreeCreator<S extends Symbol, T extends Tree<S>> {

	/**
	 * Creates a new tree of the characteristic data, that is a symbol for the root node
	 * and the list of subtrees.
	 *
	 * @param symbol symbol at root
	 * @param subTrees sub trees
	 * @return new tree with the given symbol at root and the given trees as subtrees
	 */
	T makeTree(S symbol, List<T> subTrees);

	/**
	 * Creates a new tree with no subtrees and the given symbol as root symbol.
	 *
	 * @param symbol symbol at root
	 * @return new tree with the given symbol at root and no subtrees
	 */
	T makeTree(S symbol);
}
