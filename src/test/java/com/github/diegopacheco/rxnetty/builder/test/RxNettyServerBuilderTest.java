package com.github.diegopacheco.rxnetty.builder.test;

import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.Assert;
import org.junit.Test;

import com.github.diegopacheco.rxnetty.builder.RxNettyServerBuilder;
import com.google.inject.Module;

public class RxNettyServerBuilderTest {
	
	@Test
	public void testServer() throws Throwable{
		
		final RxNettyServerBuilder server = new RxNettyServerBuilder()
				.withPort(9090)
				.withPackages("com.github.diegopacheco.rxnetty.builder.test")
				.withModules(new Module[]{new GuiceModule()});
		
		new Thread(new Runnable() {
				public void run() {
					server.start();
		}}).start();
		
	    Thread.sleep(2000L);
 	    Assert.assertEquals(200, ((HttpURLConnection)new URL("http://127.0.0.1:9090/info/ob").openConnection()).getResponseCode());
 	    Assert.assertEquals(200, ((HttpURLConnection)new URL("http://127.0.0.1:9090/info/now").openConnection()).getResponseCode());
 	    Assert.assertEquals(200, ((HttpURLConnection)new URL("http://127.0.0.1:9090/info/rr").openConnection()).getResponseCode());
 	    Assert.assertEquals(200, ((HttpURLConnection)new URL("http://127.0.0.1:9090/info/path/10/6").openConnection()).getResponseCode());
 	    
 	    server.shutdown();
	}
	
}
