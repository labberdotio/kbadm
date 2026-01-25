package io.labber.kbadm.validator;

import io.labber.kbadm.KBException;
import io.labber.kbadm.model.Document;
import io.labber.kbadm.model.Status;

public interface IDocumentValidator {

	/**
	 * 
	 * @return
	 * @throws KBException
	 */
	public Status status() throws KBException;

	/**
	 * 
	 * @return
	 * @throws KBException
	 */
	public Document validate() throws KBException;

}
