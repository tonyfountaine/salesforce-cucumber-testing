package nz.co.trineo.model;

import java.util.HashMap;
import java.util.Map;

public class Case {
	public static final String PREFIX = "500";

	public static final Map<String, String> fieldToPageField = new HashMap<>();

	static {
		fieldToPageField.put("caseOrigin", "cas11");
	}

	private String caseOrigin;

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
}
