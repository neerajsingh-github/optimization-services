/**
 * Copyright (c) 2015 Pablo Pavon Mari�o.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Contributors:
 * Pablo Pavon Mari�o - initial API and implementation
 */

/**
 *
 */
package com.jom;

import com.sun.jna.Callback;
import com.sun.jna.Pointer;

/** @author Pablo */
public interface _JNA_IPOPT_CallBack_Eval_Jac_G extends Callback
{
	public boolean callback(int n, Pointer x, boolean new_x, int m, int nele_jac, Pointer iRow, Pointer jCol, Pointer values, Pointer user_data);
}
