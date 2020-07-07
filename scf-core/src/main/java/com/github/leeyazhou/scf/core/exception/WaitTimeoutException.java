package com.github.leeyazhou.scf.core.exception;

public class WaitTimeoutException extends SCFException {
	private static final long serialVersionUID = 1L;

	public WaitTimeoutException() {
		super("客户端等待可用连接超时了");
	}

	public WaitTimeoutException(String message) {
		super(message);
	}

}
