package com.github.diegopacheco.rxnetty.router;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;

import com.github.diegopacheco.rxnetty.router.test.RestInfo;

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
public class JerseyRouter{
	
	private String basePackage;
	private List<Class> classes;
	private Map<String,Class> handlers = new HashMap<>();
	
	public JerseyRouter(String basePackage){
		this.basePackage = basePackage;
		init();
		System.out.println("Classes: " + classes);
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
	    this.classes = Arrays.asList(classes.toArray(new Class[classes.size()]));
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
					Annotation annotation = c.getDeclaredAnnotation(Path.class);
	            	if (annotation!=null){
	            		handlers.put(((Path)annotation).value(), c);
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
		System.out.println(handlers);
		System.out.println(req.getUri());
		return new RestInfo().workOb();
	}	
	
}