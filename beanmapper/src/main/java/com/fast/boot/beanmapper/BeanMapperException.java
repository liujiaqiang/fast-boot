package com.fast.boot.beanmapper;

/**
 * Created by junqing.li on 17/2/28.
 */
public class BeanMapperException extends RuntimeException {

	public BeanMapperException() {
	}

	public BeanMapperException(String message) {
		super(message);
	}

	public BeanMapperException(String message, Throwable cause) {
		super(message, cause);
	}

	public BeanMapperException(Throwable cause) {
		super(cause);
	}
}
