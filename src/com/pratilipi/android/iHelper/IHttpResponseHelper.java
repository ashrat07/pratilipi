package com.pratilipi.android.iHelper;

/**
 * Generic interface as every request will have a success and failure*/
public interface IHttpResponseHelper {

	void responseSuccess();

	void responseFailure(String failureMessage);
	
	/**
	 *This is called to make any request so as object of this interface be available*/
	void makeRequest();
}

