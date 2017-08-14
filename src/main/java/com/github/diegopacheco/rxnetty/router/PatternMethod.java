package com.github.diegopacheco.rxnetty.router;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

public class PatternMethod {
	
	private Pattern pattern;
	private Method method;
	private String basePath;
	
	public PatternMethod() {}
	
	public PatternMethod(Pattern pattern, Method method) {
		super();
		this.pattern = pattern;
		this.method = method;
	}
	
	public PatternMethod(Pattern pattern, Method method, String basePath) {
		super();
		this.pattern = pattern;
		this.method = method;
		this.basePath = basePath;
	}

	public Pattern getPattern() {
		return pattern;
	}

	public void setPattern(Pattern pattern) {
		this.pattern = pattern;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}
	
	public String getBasePath() {
		return basePath;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((basePath == null) ? 0 : basePath.hashCode());
		result = prime * result + ((method == null) ? 0 : method.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PatternMethod other = (PatternMethod) obj;
		if (basePath == null) {
			if (other.basePath != null)
				return false;
		} else if (!basePath.equals(other.basePath))
			return false;
		if (method == null) {
			if (other.method != null)
				return false;
		} else if (!method.equals(other.method))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PatternMethod [pattern=" + pattern + ", method=" + method + ", basePath=" + basePath + "]";
	}


}
