package com.github.diegopacheco.rxnetty.router.test;

import com.google.inject.AbstractModule;

public class GuiceModule extends AbstractModule{
	
	@Override
	protected void configure() {
		bind(RestInfoSecond.class).asEagerSingleton();
	}
	
}
