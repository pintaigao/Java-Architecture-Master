package com.kkb.ssm.design_pattern.decorate;

import org.junit.Test;

public class TestDecorate {

	@Test
	public void test() {
		Iphone iphone = new Iphone6();
		iphone.call();
		System.out.println("=======================");
		//装饰之后，依然是一个手机
		Iphone iphone2 = new IphoneDecorate(iphone);
		iphone2.call();
	}
}
