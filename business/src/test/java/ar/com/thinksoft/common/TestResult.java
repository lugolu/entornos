package ar.com.thinksoft.common;

public class TestResult {

	public static final String FAILED = "FAILED";
	public static final String SUCCESS = "SUCCESS";
	public static final String SKIP_FAILED = "SKIP_FAILED";
	public static final String SKIP_SUCCESS = "SKIP_SUCCESS";
	public static final String SKIP_AGREGAR = "SKIP_AGREGAR";
	public static final String SKIP_GENERAL = "SKIP_GENERAL";
	public static final String SKIP_INSERT = "SKIP_INSERT";
	public static final String SKIP_PAGE = "SKIP_PAGE";
	public static final String SKIP_UPDATE = "SKIP_UPDATE";
	public static final String LOGIN = "LOGIN";

	private int success;
	private int failed;
	private int skipSuccess;
	private int skipFailed;
	private int skipAgregar;
	private int skipGeneral;
	private int skipInsert;
	private int skipPage;
	private int skipUpdate;
	private int login;

	public TestResult() {
	}

	public int getSuccess() {
		return success;
	}

	public void setSuccess(int success) {
		this.success = success;
	}

	public int getFailed() {
		return failed;
	}

	public void setFailed(int failed) {
		this.failed = failed;
	}

	public int getSkipSuccess() {
		return skipSuccess;
	}

	public void setSkipSuccess(int skipSuccess) {
		this.skipSuccess = skipSuccess;
	}

	public int getSkipFailed() {
		return skipFailed;
	}

	public void setSkipFailed(int skipFailed) {
		this.skipFailed = skipFailed;
	}

	public void addFailed () {
		failed++;
	}

	public void addSuccess () {
		success++;
	}

	public void addSkipFailed () {
		skipFailed++;
	}

	public void addSkipSuccess () {
		skipSuccess++;
	}

	public void addSkipAgregar () {
		skipAgregar++;
	}

	public void addSkipGeneral () {
		skipGeneral++;
	}

	public void addSkipInsert () {
		skipInsert++;
	}

	public void addSkipPage () {
		skipPage++;
	}

	public void addSkipUpdate () {
		skipUpdate++;
	}

	public void addLogin () {
		login++;
	}

	public int getTests () {
		return success + failed + skipSuccess + skipFailed + skipAgregar + skipGeneral + skipInsert + skipPage + skipUpdate;
	}

	@Override
	public String toString() {
		return "TestResult [success=" + success + ", failed=" + failed + ", skipSuccess=" + skipSuccess
				+ ", skipFailed=" + skipFailed + ", skipAgregar=" + skipAgregar + ", skipGeneral=" + skipGeneral
				+ ", skipInsert=" + skipInsert + ", skipPage=" + skipPage + ", skipUpdate=" + skipUpdate + ", login="
				+ login + "]";
	}

}
