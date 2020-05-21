package com.github.leeyazhou.scf.core.exception;

public class SCFException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private int errCode;

	public int getErrCode() {
		return errCode;
	}

	public void setErrCode(int errCode) {
		this.errCode = errCode;
	}

	public SCFException(int errCode, String msg) {
		super(msg);
		this.errCode = errCode;
	}

	public SCFException(String msg) {
		this(-1, msg);
	}
}
