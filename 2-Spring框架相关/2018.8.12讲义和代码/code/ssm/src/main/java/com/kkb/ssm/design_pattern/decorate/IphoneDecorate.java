package com.kkb.ssm.design_pattern.decorate;

/**
 * 装饰类（增强功能）
 * @author think
 *
 */
public class IphoneDecorate implements Iphone{

	//被装饰的目标类
	private Iphone iphone;
	
	//通过构造参数将被装饰的类，传入过来
	public IphoneDecorate(Iphone iphone) {
		super();
		this.iphone = iphone;
	}

	@Override
	public void call() {
		System.out.println("人猿泰山music。。。。。。");
		iphone.call();
	}
	
}
