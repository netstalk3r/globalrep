package com.akvelon.ets.verifier.util;

public class Constants {

	// Request parameters
	public static final String ORDER_BY = "orderby";
	public static final String ORDER = "order";
	public static final String CONTROL = "control";
	public static final String ACCOUNT = "account";
	public static final String PROJECT = "project";
	public static final String STATUS = "status";
	public static final String TYPE = "type";
	public static final String DATE_FROM = "datefrom";
	public static final String DATE_TO = "dateto";
	public static final String CHANGE_PERIOD = "changeperiod";
	
	// Default options for request parameters
	public static final String CONTROL_DEFAULT_OPTION = "filterform.status";
	public static final String PROJECT_DEFAULT_OPTION = "all";
	public static final String STATUS_DEFAULT_OPTION = "prepared";
	public static final String TYPE_DEFAULT_OPTION = "all";
	public static final String CHANGE_PERIOD_DEFAULT_OPTION = "month"; // could be day, week
	
	public static final String STATUS_ACCEPTED_OPTION = "closed";
	
	public static final String REPORTED_HOURS_URL = "https://ua.ets.akvelon.com/accountreport.ets";
	
	public static final String EMPTY_PARAM = "";
	
	public static final int RESPONSE_CODE_OK = 200;
	
	public static final String RECIPIENT_TO = "to";
	public static final String RECIPIENT_CC = "cc";
	
	public static final String LOGIN = "login";
	public static final String PASSWORD = "password";
	
	public static final String PROJECT_VERSION = "project.version";
	public static final String BUILD_NUMBER = "build.number";
	
}
