package com.github.diegopacheco.rxnetty.builder;

import org.apache.log4j.Logger;

import com.github.diegopacheco.rxnetty.builder.test.GuiceModule;
import com.github.diegopacheco.rxnetty.router.JerseyRouter;
import com.google.inject.Module;

import io.reactivex.netty.protocol.http.server.HttpServer;

/**
 * Builder to create RxNetty Server with Jersery Router.
 * @author diegopacheco
 *
 */
@SuppressWarnings({"rawtypes","static-access","unchecked"})
public class RxNettyServerBuilder {
	
	private final static Logger logger = Logger.getLogger(RxNettyServerBuilder.class);
	
	private Integer port;
	private String packages;
	private Module[] modules;
	private HttpServer server; 
	private JerseyRouter router;
	
	public RxNettyServerBuilder withPort(Integer port){
		this.port = port;
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
		logger.info("Starint RxNetty Server on 127.0.0.1:" + port);
		
		this.router = new JerseyRouter(packages,modules);

		server.start((req, resp) ->
    		resp.writeStringAndFlushOnEach(router.handle(req, resp))
		).awaitShutdown();
	}
	
	public static void main(String[] args) {
		new RxNettyServerBuilder()
		  .withPort(9090)
		  .withPackages("com.github.diegopacheco.rxnetty.builder.test")
		  .withModules(new Module[]{new GuiceModule()})
		  .start();
	}
}
