
// 
// Copyright (c) 2026, John Grundback
// All rights reserved.
// 

package io.labber.kbadm.model;

import java.sql.Date;

/**
 * 
 * @author john
 *
 */
public class Document {

	// [id, name, description]

	protected String id;
	protected String name;
	protected String description;

	protected int chunks;
	protected int total;
	protected String status;
	protected String reason;

	protected Date created;
	protected Date modified;
	protected Date timestamp;

	/**
	 * 
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * 
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 
	 * @return
	 */
	public int getChunks() {
		return chunks;
	}

	/**
	 * 
	 * @param chunks
	 */
	public void setChunks(int chunks) {
		this.chunks = chunks;
	}

	/**
	 * 
	 * @return
	 */
	public int getTotal() {
		return total;
	}

	/**
	 * 
	 * @param total
	 */
	public void setTotal(int total) {
		this.total = total;
	}

	/**
	 * 
	 * @return
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * 
	 * @param status
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * 
	 * @return
	 */
	public String getReason() {
		return reason;
	}

	/**
	 * 
	 * @param reason
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}

	/**
	 * 
	 * @return
	 */
	public Date getCreated() {
		return created;
	}

	/**
	 * 
	 * @param created
	 */
	public void setCreated(Date created) {
		this.created = created;
	}

	/**
	 * 
	 * @return
	 */
	public Date getModified() {
		return modified;
	}

	/**
	 * 
	 * @param modified
	 */
	public void setModified(Date modified) {
		this.modified = modified;
	}

	/**
	 * 
	 * @return
	 */
	public Date getTimestamp() {
		return timestamp;
	}

	/**
	 * 
	 * @param timestamp
	 */
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public Document withId(String id) {
		this.id = id;
		return this;
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	public Document withName(String name) {
		this.name = name;
		return this;
	}

	/**
	 * 
	 * @param description
	 * @return
	 */
	public Document withDescription(String description) {
		this.description = description;
		return this;
	}

	/**
	 * 
	 * @param chunks
	 * @return
	 */
	public Document withChunks(int chunks) {
		this.chunks = chunks;
		return this;
	}

	/**
	 * 
	 * @param total
	 * @return
	 */
	public Document withTotal(int total) {
		this.total = total;
		return this;
	}

	/**
	 * 
	 * @param status
	 * @return
	 */
	public Document withStatus(String status) {
		this.status = status;
		return this;
	}

	/**
	 * 
	 * @param reason
	 * @return
	 */
	public Document withReason(String reason) {
		this.reason = reason;
		return this;
	}

	/**
	 * 
	 * @param created
	 * @return
	 */
	public Document withCreated(Date created) {
		this.created = created;
		return this;
	}

	/**
	 * 
	 * @param modified
	 * @return
	 */
	public Document withModified(Date modified) {
		this.modified = modified;
		return this;
	}

	/**
	 * 
	 * @param timestamp
	 * @return
	 */
	public Document withTimestamp(Date timestamp) {
		this.timestamp = timestamp;
		return this;
	}

}
