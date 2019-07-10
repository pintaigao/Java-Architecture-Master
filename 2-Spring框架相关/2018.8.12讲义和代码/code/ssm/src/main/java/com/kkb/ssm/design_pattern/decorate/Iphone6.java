package com.kkb.ssm.design_pattern.decorate;

/**
 * 被装饰类
 * @author think
 *
 */
public class Iphone6 implements Iphone {

	@Override
	public void call() {
		System.out.println("使用iphone6打电话，性能刚刚滴");
	}
}
