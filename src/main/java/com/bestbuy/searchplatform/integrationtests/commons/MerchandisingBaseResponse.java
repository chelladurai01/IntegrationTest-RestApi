package com.bestbuy.searchplatform.integrationtests.commons;

import java.util.ArrayList;
import java.util.List;
/**
 * 
 * Class to read the response message 
 *
 * @param <E>
 */
public class MerchandisingBaseResponse<E> {     
	
	private ResponseStatusEnum status = ResponseStatusEnum.SUCCESS;  
	private String message;
	private String errorCode;
	private String successCode;
	private E data;
	private String page ="1";
	
	private List<E> rows = new ArrayList<E>();
	private List<String> generalPurposeMessage;
	

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = "1";
	}

	public List<String> getGeneralPurposeMessage() {
		return generalPurposeMessage;
	}

	public void setGeneralPurposeMessage(List<String> generalPurposeMessage) {
		this.generalPurposeMessage = generalPurposeMessage;
	}

	public ResponseStatusEnum getStatus() {
		return status;
	}

	public void setStatus(ResponseStatusEnum string) {
		this.status = string;
	}

	public String getMessage() {
		return message;
	}
	
	public void setMessage(String string) {
		this.message = string;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {  
		this.errorCode = errorCode;
		status = ResponseStatusEnum.ERROR;
		clearData();  // to be sure that no data is going to the UI when there is an error
	}
	
	public void setErrorCode(String errorCode, String... params) {  
		this.errorCode = errorCode;
		status = ResponseStatusEnum.ERROR;
		clearData();  // to be sure that no data is going to the UI when there is an error
	}	

	public String getSuccessCode() {
		return successCode;
	}

	public void setSuccessCode(String successCode) {
		this.successCode = successCode;
		status = ResponseStatusEnum.SUCCESS;		
	}
	
	public void setSuccessCode(String successCode, String... params) {
		this.successCode = successCode;
		status = ResponseStatusEnum.SUCCESS;		
	}	

	public E getData() {
		return data;
	}

	public void setData(E data) {
		this.data = data;
	}

	public List<E> getRows() {
		return rows;
	}

	public void setRows(List<E> rows) {
		this.rows = rows;
	}
	
	/*public void sortRows(){
		Collections.sort(rows);
	}*/
	
	private void clearData() {
		rows = null;
		data = null;
	}
}
