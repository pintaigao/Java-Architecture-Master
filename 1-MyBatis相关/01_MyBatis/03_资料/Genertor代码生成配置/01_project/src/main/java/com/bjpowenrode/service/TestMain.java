package com.bjpowenrode.service;

import org.mybatis.generator.api.ShellRunner;

public class TestMain {

	public static void main(String[] args) {
		  args = new String[] { "-configfile", "src\\main\\resources\\mybatis-generator-config.xml", "-overwrite" };
	        ShellRunner.main(args);

	}

}
