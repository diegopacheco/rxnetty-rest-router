# rxnetty-rest-router

This project is a simple sample of a JAX-RS Jesery router for RxNetty Server.

## Features

* RxNetty Builder
* Jersey Router
* Converts results to observables if they are not Observables. 
* Inject Request and Response parameters if method siognature present.

## Sample

ServerApp.java
```java
public class ServerApp{
   public static void main(String[] args){
       new RxNettyServerBuilder()
        .withPort(9090)
        .withPackages("com.github.diegopacheco.rxnetty.builder.test")
        .withModules(new Module[]{new GuiceModule()})
        .start();
   }
}
```

GuiceModule 
```java
import com.google.inject.AbstractModule;

public class GuiceModule extends AbstractModule{
	
	@Override
	protected void configure() {
		bind(RestInfo.class).asEagerSingleton();
	}
	
}
```

RestInfo.java
```java
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import io.netty.buffer.ByteBuf;
import io.reactivex.netty.protocol.http.server.HttpServerRequest;
import io.reactivex.netty.protocol.http.server.HttpServerResponse;
import rx.Observable;

@Path("/info")
public class RestInfo {
  
        @GET
	@Path("rr")
	public Observable<String> workObRes(HttpServerRequest<ByteBuf> req,HttpServerResponse<ByteBuf> resp){
		resp.setHeader("CREATED_BY", "DIEGO");
		return Observable.just("OKReqResp");
	}
  
        @GET
	@Path("now")
	public String work(){
		return "OK";
	}
}
```

Testing
```java
curl -v http://127.0.0.1:9090/info/now
curl -v http://127.0.0.1:9090/info/rr
```

Cheers, <BR>
Diego Pacheco
