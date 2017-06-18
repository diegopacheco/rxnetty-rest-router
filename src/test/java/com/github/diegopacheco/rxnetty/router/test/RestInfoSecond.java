package com.github.diegopacheco.rxnetty.router.test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import rx.Observable;

@Path("/info")
public class RestInfoSecond {
	
	@GET
	@Path("now")
	public String work(){
		return "OK";
	}
	
	@GET
	@Path("ob")
	public Observable<String> workOb(){
		return Observable.just("OK");
	}
	
}
