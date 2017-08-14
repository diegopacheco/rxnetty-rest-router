package com.github.diegopacheco.rxnetty.builder.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegTest {
	public static void main(String[] args) {
		String line = "/info/path/10/6";
		
		String pattern = "/info/path/{a}/{b}"
											 .replaceAll("\\{a}", "\\\\w*")
											 .replaceAll("\\{b}", "\\\\w*");
		
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(line);
		
		if (m.find()) {
			System.out.println(m.group());
		} else {
			System.out.println("not found");
		}
	}
}
