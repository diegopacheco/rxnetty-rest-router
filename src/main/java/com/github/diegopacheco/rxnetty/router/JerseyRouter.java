package com.github.diegopacheco.rxnetty.router;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;

import org.apache.log4j.Logger;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import io.netty.buffer.ByteBuf;
import io.reactivex.netty.protocol.http.server.HttpServerRequest;
import io.reactivex.netty.protocol.http.server.HttpServerResponse;
import rx.Observable;

/**
 * JerseyRouter is a simple router for @Path REST classes into RxNetty.
 * 
 * @author diegopacheco
 *
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class JerseyRouter {
	
	private final static Logger logger = Logger.getLogger(JerseyRouter.class);
	
	private String basePackage;
	private Map<String,Method> handlers = new HashMap<>();
	private Injector injector;
	
	public JerseyRouter(String basePackage,Module... modules){
		this.injector = Guice.createInjector(modules);
		this.basePackage = basePackage;
		logger.info("Scanning base packages: " + basePackage); 
		init();
	}
	
	private void init(){
	    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	    assert classLoader != null;
	    
	    String path = basePackage.replace('.', '/');
	    Enumeration<URL> resources;
		try {
			resources = classLoader.getResources(path);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	    List<File> dirs = new ArrayList<File>();
	    while (resources.hasMoreElements()) {
	        URL resource = resources.nextElement();
	        dirs.add(new File(resource.getFile()));
	    }
	    ArrayList<Class> classes = new ArrayList<Class>();
	    for (File directory : dirs) {
	        classes.addAll(findClasses(directory, basePackage));
	    }
	}
	
	private List<Class> findClasses(File directory, String packageName){
	    List<Class> classes = new ArrayList<Class>();
	    if (!directory.exists()) {
	        return classes;
	    }
	    File[] files = directory.listFiles();
	    for (File file : files) {
	        if (file.isDirectory()) {
	            assert !file.getName().contains(".");
	            classes.addAll(findClasses(file, packageName + "." + file.getName()));
	        } else if (file.getName().endsWith(".class")) {
	            try {
	            	Class c = Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6));
					Annotation annotationClass = c.getDeclaredAnnotation(Path.class);
	            	if (annotationClass!=null){
	            		for(Method m : c.getDeclaredMethods()){
	            			Annotation annotationPath = m.getDeclaredAnnotation(Path.class);
	            			if(annotationPath!=null){
	            				handlers.put( ((Path)annotationClass).value() + "/" + ((Path)annotationPath).value(), m);
	            			}
	            		}
	            		classes.add(c);
	            	}
				} catch (ClassNotFoundException e) {
					throw new RuntimeException(e); 
				}
	        }
	    }
	    return classes;
	}
	
	public Observable handle(HttpServerRequest<ByteBuf> req, HttpServerResponse<ByteBuf> resp) {
		logger.info("Processing URI: " + req.getUri());
		
		Method m = handlers.get(req.getUri());
		if(m==null){
			if ("/favicon.ico".equals(req.getUri())) return Observable.empty();
			throw new NoHandlerFoundException("No Handler found for URI: " + req.getUri());
		}
		
		Object insatance = injector.getInstance(m.getDeclaringClass());
		Object result = null;
		try {
			AnnotatedType[] args =  m.getAnnotatedParameterTypes();
			if(args.length==1){
				if ("io.reactivex.netty.protocol.http.server.HttpServerRequest<io.netty.buffer.ByteBuf>".equals(args[0].getType().getTypeName()) ){
					result = m.invoke(insatance,req);
				}else if("io.reactivex.netty.protocol.http.server.HttpServerResponse<io.netty.buffer.ByteBuf>".equals(args[0].getType().getTypeName()) ){
					result = m.invoke(insatance,resp);
				}
			} else if(args.length==2){
				if ("io.reactivex.netty.protocol.http.server.HttpServerRequest<io.netty.buffer.ByteBuf>".equals(args[0].getType().getTypeName()) && 
				    "io.reactivex.netty.protocol.http.server.HttpServerResponse<io.netty.buffer.ByteBuf>".equals(args[1].getType().getTypeName())){
					result = m.invoke(insatance,req,resp);
				}
			} else{
				result = m.invoke(insatance);
			}
			if(!(result instanceof Observable)){
				result = Observable.just(result);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return (Observable)result;
	}
	
}