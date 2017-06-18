package com.github.diegopacheco.rxnetty.builder.test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import io.netty.buffer.ByteBuf;
import io.reactivex.netty.protocol.http.server.HttpServerRequest;
import io.reactivex.netty.protocol.http.server.HttpServerResponse;
import rx.Observable;

@Path("/info")
public class RestInfo {
	
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
	
	@GET
	@Path("res")
	public Observable<String> workObReq(HttpServerRequest<ByteBuf> req){
		return Observable.just("OKReq");
	}
	
	@GET
	@Path("resp")
	public Observable<String> workObRes(HttpServerResponse<ByteBuf> resp){
		return Observable.just("OKRes");
	}
	
	@GET
	@Path("rr")
	public Observable<String> workObRes(HttpServerRequest<ByteBuf> req,HttpServerResponse<ByteBuf> resp){
		return Observable.just("OKReqResp");
	}
	
}
