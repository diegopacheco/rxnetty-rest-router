package com.github.diegopacheco.rxnetty.router.test;

import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.Assert;
import org.junit.Test;

import com.github.diegopacheco.rxnetty.router.JerseyRouter;

import io.reactivex.netty.protocol.http.server.HttpServer;

@SuppressWarnings("unchecked")
public class JerseyRouterTest {
	
	@Test
	public void testSimple() throws Throwable {
    	   new Thread(new Runnable() {
			public void run() {
				  HttpServer
	               .newServer(8086)
	               .start((req, resp) ->
	                   resp
	                       .setHeader("Content-Lenght", 2)
	                       .writeStringAndFlushOnEach(new JerseyRouter("com.github.diegopacheco.rxnetty.router.test",new GuiceModule()).handle(req, resp))
	               ).awaitShutdown();
			}
    	   }).start();
    	   
    	   Thread.sleep(2000L);
    	   Assert.assertEquals(200, ((HttpURLConnection)new URL("http://127.0.0.1:8086/info/ob").openConnection()).getResponseCode());
    	   Assert.assertEquals(200, ((HttpURLConnection)new URL("http://127.0.0.1:8086/info/now").openConnection()).getResponseCode());
	}
	
}
