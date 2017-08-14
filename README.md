# rxnetty-rest-router

This project is a simple sample of a JAX-RS Jesery router for RxNetty Server.

## Features

* RxNetty Builder
* Jersey Router
* Support for @PathParam annotation in class and method level.
* Converts results to observables if they are not Observables.
* Guice 4 Integration and Support
* Inject Request and Response parameters if method signature present.

## Future Work(Pending)

* Add Suport for @QueryParam
* Add Support other typez like Long, Double and Boolean

## Usage

You can find the jar at sonatype central repository https://oss.sonatype.org/service/local/repositories/releases/content/com/github/diegopacheco/rxnetty-rest-router/0.2/

Maven
```xml
<dependency>
  <groupId>com.github.diegopacheco</groupId>
  <artifactId>rxnetty-rest-router</artifactId>
  <version>0.2</version>
</dependency>
```

Gradle
```groovy
dependencies {
	compile([
	       'com.github.diegopacheco:rxnetty-rest-router:0.2'
        ])
}
```

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

  @GET
  @Path("path/{a}/{b}")
  public String query(@PathParam("a") String a, @PathParam("b") String b){
       return "Path: " +  a + " - " + b;
  }

}
```

Testing
```java
curl -v http://127.0.0.1:9090/info/now
curl -v http://127.0.0.1:9090/info/rr
curl -v http://127.0.0.1:9090/info/path/10/6
```

## Release

#### 0.2

* Support for @PathParam annotation in class and method level.

#### 0.1

* RxNetty Builder
* Jersey Router
* Converts results to observables if they are not Observables.
* Guice 4 Integration and Support
* Inject Request and Response parameters if method signature present.

Cheers, <BR>
Diego Pacheco
