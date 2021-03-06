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
package de.uni_muenster.cs.sev.lethal.utils;

/**
 * Converts an object of type A into an object of type B.
 * This conversion has to be well-defined, that means
 * if you have assignments c = convert(a) and d = convert(a), then
 * c.equals(d).
 *
 * @param <A> type to be converted
 * @param <B> type to be converted into
 *
 * @author Martin
 */
public interface Converter<A, B> {

	/**
	 * Converts an object of type A into an object of type B.
	 *
	 * @param a object to be converted
	 * @return converted version of the given object
	 */
	B convert(A a);
}
