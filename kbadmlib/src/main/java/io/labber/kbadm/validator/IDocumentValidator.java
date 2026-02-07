
// 
// Copyright (c) 2026, John Grundback
// All rights reserved.
// 

package io.labber.kbadm.validator;

import io.labber.kbadm.KBException;
import io.labber.kbadm.model.Document;

/**
 * 
 * @author john
 *
 */
public interface IDocumentValidator {

	/**
	 * 
	 * @return
	 * @throws KBException
	 */
	public ValidationStatus status() throws KBException;

	/**
	 * 
	 * @return
	 * @throws KBException
	 */
	public Document validate() throws KBException;

}
