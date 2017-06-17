package com.github.diegopacheco.rxnetty.builder;

import com.github.diegopacheco.rxnetty.router.JerseyRouter;
import com.google.inject.Module;

import io.reactivex.netty.protocol.http.server.HttpServer;

@SuppressWarnings({"rawtypes","static-access","unchecked"})
public class RxNettyServerBuilder {
	
	private String packages;
	private Module[] modules;
	private HttpServer server; 
	
	public RxNettyServerBuilder withPort(Integer port){
		server = server.newServer(port);
		return this;
	}
	
	public RxNettyServerBuilder withPackages(String packages){
		this.packages = packages;
        return this;
	}
	
	public RxNettyServerBuilder withModules(Module[] modules){
		this.modules = modules;
        return this;
	}
	
	public void start(){
		server.start((req, resp) ->
    		resp.writeStringAndFlushOnEach(new JerseyRouter(packages,modules).handle(req, resp))
		).awaitShutdown();
	}
	
}
