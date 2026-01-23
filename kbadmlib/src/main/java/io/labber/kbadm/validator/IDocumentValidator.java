package io.labber.kbadm.validator;

import io.labber.kbadm.KBException;
import io.labber.kbadm.model.Status;

public interface IDocumentValidator {

	/**
	 * 
	 * @return
	 * @throws KBException
	 */
	public Status validate() throws KBException;

}
