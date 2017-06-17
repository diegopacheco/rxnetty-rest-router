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
		new Thread(new Runnable() {
				public void run() {
					new RxNettyServerBuilder()
						.withPort(9090)
						.withPackages("com.github.diegopacheco.rxnetty.builder.test")
						.withModules(new Module[]{new GuiceModule()})
						.start();
		}}).start();
		
	    Thread.sleep(2000L);
 	    Assert.assertEquals(200, ((HttpURLConnection)new URL("http://127.0.0.1:9090/info/ob").openConnection()).getResponseCode());
 	    Assert.assertEquals(200, ((HttpURLConnection)new URL("http://127.0.0.1:9090/info/now").openConnection()).getResponseCode());
	}
	
}
