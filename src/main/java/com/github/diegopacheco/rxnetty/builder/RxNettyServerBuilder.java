package com.github.diegopacheco.rxnetty.builder;

import org.apache.log4j.Logger;

import com.github.diegopacheco.rxnetty.router.JerseyRouter;
import com.google.inject.Module;

import io.netty.buffer.ByteBuf;
import io.reactivex.netty.RxNetty;
import io.reactivex.netty.protocol.http.server.HttpServer;
import io.reactivex.netty.protocol.http.server.HttpServerRequest;
import io.reactivex.netty.protocol.http.server.HttpServerResponse;
import io.reactivex.netty.protocol.http.server.RequestHandler;
import rx.Observable;

/**
 * Builder to create RxNetty Server with Jersery Router.
 * 
 * @author diegopacheco
 *
 */
@SuppressWarnings({ "rawtypes" })
public class RxNettyServerBuilder {

	private final static Logger logger = Logger.getLogger(RxNettyServerBuilder.class);

	private Integer port;
	private String packages;
	private Module[] modules;
	private HttpServer server;
	private JerseyRouter router;

	public RxNettyServerBuilder withPort(Integer port) {
		this.port = port;
		return this;
	}

	public RxNettyServerBuilder withPackages(String packages) {
		this.packages = packages;
		return this;
	}

	public RxNettyServerBuilder withModules(Module[] modules) {
		this.modules = modules;
		return this;
	}

	public void start() {
		logger.info("Starint RxNetty Server on 127.0.0.1:" + port);

		this.router = new JerseyRouter(packages, modules);

		server = RxNetty.createHttpServer(port, new RequestHandler<ByteBuf, ByteBuf>() {
			@Override
			public Observable<Void> handle(HttpServerRequest<ByteBuf> request, HttpServerResponse<ByteBuf> response) {
				return response.writeStringAndFlush(router.handle(request, response).toBlocking().first().toString());
			}
		});

		server.startAndWait();
	}
	
	public void shutdown(){
		try {
			server.shutdown();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
