package nz.co.trineo.model;

import java.util.HashMap;
import java.util.Map;

public class Case {
	public static final String PREFIX = "500";

	public static final Map<String, String> fieldToPageField = new HashMap<>();

	static {
		fieldToPageField.put("caseOrigin", "cas11");
		fieldToPageField.put("recordTypeId", "p3");
		fieldToPageField.put("status", "cas7");
	}

	private String caseOrigin;
	private String recordTypeId;
	private String status;

	@SuppressWarnings("unused")
	private Case() {
	}

	public Case(final String caseOrigin) {
		this.caseOrigin = caseOrigin;
	}

	public String getCaseOrigin() {
		return caseOrigin;
	}

	public void setCaseOrigin(String caseOrigin) {
		this.caseOrigin = caseOrigin;
	}

	public String getRecordTypeId() {
		return recordTypeId;
	}

	public void setRecordTypeId(String recordTypeId) {
		this.recordTypeId = recordTypeId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
