package com.github.diegopacheco.rxnetty.router;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 * AnnotationScanner Scanns Classpath for Specific Anotations.
 * 
 * @author diegopacheco
 *
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class AnnotationScanner {
	
	private String basePackage;
	private Map<String,Method> handlers = new HashMap<>();
	private List<PatternMethod> patternsMethods = new ArrayList<>();;
	
	public AnnotationScanner(String basePackage) {
		this.basePackage = basePackage;
		init();
	}
	
	public Map<String,Method> getHandlers(){
		return handlers;
	}
	
	public List<PatternMethod> getPatternsMethods() {
		return patternsMethods;
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
	            				
	            				if(containsAnnotation(m.getParameters(),PathParam.class)){
	            				
	            					String pattern = ((Path)annotationClass).value() + "/" + ((Path)annotationPath).value() + "/";
	            					String basePath = pattern;
	            					for(Annotation[] ma: m.getParameterAnnotations()){
	            						if (ma[0] instanceof PathParam){
	            							PathParam pp = (PathParam)ma[0];
	            							pattern = pattern.replace("{" + pp.value() + "}", "\\w*");
	            							basePath = basePath.replace("{" + pp.value() + "}/", "");
	            						}
	            					}
	            					patternsMethods.add(new PatternMethod(Pattern.compile(pattern), m, basePath));
	            					
	            				} else {
	            					handlers.put( ((Path)annotationClass).value() + "/" + ((Path)annotationPath).value(), m);
	            				}
	            				
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
	
	private boolean containsAnnotation(Parameter[] source,Class seek){
		for(Parameter p: Arrays.asList(source)){
			List<Annotation> s = Arrays.asList(p.getDeclaredAnnotations());
			for(Annotation a: s){
				if (a.toString().contains(seek.getName()))
					return true;
			}
		}
		return false;
	}
	
}
