/** 
 * (C) Copyright 2018 BioCliq Technologies Pvt. Ltd India. All Rights Reserved
 * 
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential 
 *
 * Author k.raja@biocliq.com
 */

package com.zitlab.palmyra.client.pojo;

public final class Action {
	
	private Action() {}
	
	public static final String CREATE_S = "create";
	public static final String UPDATE_S = "update";
	public static final String DELETE_S = "delete";
	public static final String SAVE_S = "save";
	public static final String NONE_S = "none";

	public static final int PARENT_ACTION = 0;
	public static final int UPDATE = 1;
	public static final int CREATE = 2;
	public static final int DELETE = -1;
	public static final int NONE = -2;
	public static final int SAVE = 3;
	
	public static String getString(int action){
		switch(action) {
		case CREATE:
			return CREATE_S;
		case UPDATE:
			return UPDATE_S;
		case DELETE:
			return DELETE_S;
		case NONE:
			return NONE_S;
		case SAVE:
			return SAVE_S;
		default:
			return null;
		}
	}
	
	public static Integer getInt(String action) {
		switch (action) {
		case CREATE_S:
			return CREATE;
		case DELETE_S:		
			return DELETE;
		case UPDATE_S:
			return UPDATE;
		case NONE_S:
			return NONE;
		case SAVE_S:
			return SAVE;
		default:
			return null;
		}
	}
	
	public static Integer getRelInt(String action) {
		switch (action) {
		case CREATE_S:
			return CREATE;
		case DELETE_S:		
			return DELETE;
		case UPDATE_S:
			return UPDATE;
		case SAVE_S:
			return SAVE;
		default:
			return PARENT_ACTION;
		}
	}
}
