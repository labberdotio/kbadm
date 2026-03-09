
// 
// Copyright (c) 2024, 2025, John Grundback
// All rights reserved.
// 

package io.labber.kbadm.synchronize;

import java.io.File;
import java.util.List;

import io.labber.kbadm.KBException;

/**
 * 
 * @author john
 *
 */
public interface IDocumentSynchronizer {

	/**
	 * 
	 * @param file
	 * @throws KBException
	 */
	public void synchronize(
		File file
	) throws KBException;

	/**
	 * 
	 * @param file
	 * @throws KBException
	 */
	public void synchronize(
		String file
	) throws KBException;

	/**
	 * 
	 * @param files
	 * @throws KBException
	 */
	public void synchronize(
		List<File> files
	) throws KBException;

	/**
	 * 
	 * @param files
	 * @throws KBException
	 */
	public void synchronize(
		String[] files
	) throws KBException;

}
