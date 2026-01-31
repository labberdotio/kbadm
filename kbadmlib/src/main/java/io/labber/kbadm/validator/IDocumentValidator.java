package io.labber.kbadm.validator;

import org.apache.commons.lang3.tuple.Pair;

import io.labber.kbadm.KBException;
import io.labber.kbadm.model.Document;
import io.labber.kbadm.model.Status;

public interface IDocumentValidator {

	/**
	 * 
	 * @return
	 * @throws KBException
	 */
	public Pair<Status, String> status() throws KBException;

	/**
	 * 
	 * @return
	 * @throws KBException
	 */
	public Document validate() throws KBException;

}
