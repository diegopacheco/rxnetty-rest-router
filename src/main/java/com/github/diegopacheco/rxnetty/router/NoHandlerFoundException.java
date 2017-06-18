package com.github.diegopacheco.rxnetty.router;

/**
 * NoHandlerFoundException means we could nto find a handler for expecific URI.
 * 
 * @author diegopacheco
 *
 */
public class NoHandlerFoundException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;

	public NoHandlerFoundException() {}
	
	public NoHandlerFoundException(String msg) {
		super(msg);
	}
	
	public NoHandlerFoundException(String msg,Exception e) {
		super(msg,e);
	}
}
