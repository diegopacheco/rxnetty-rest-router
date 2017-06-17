package com.github.diegopacheco.rxnetty.builder.test;

import com.google.inject.AbstractModule;

public class GuiceModule extends AbstractModule{
	
	@Override
	protected void configure() {
		bind(RestInfo.class).asEagerSingleton();
	}
	
}
