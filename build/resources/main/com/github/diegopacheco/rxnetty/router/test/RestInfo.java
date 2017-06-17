package com.github.diegopacheco.rxnetty.router.test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import rx.Observable;

@Path("/info/now")
public class RestInfo {
	
	@GET
	public String work(){
		return "OK";
	}
	
	@GET
	@Path("ob")
	public Observable<String> workOb(){
		return Observable.just("OK");
	}
	
}
