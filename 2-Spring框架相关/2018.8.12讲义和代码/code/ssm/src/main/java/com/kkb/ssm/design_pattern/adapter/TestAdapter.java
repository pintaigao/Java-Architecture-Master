package com.kkb.ssm.design_pattern.adapter;

import org.junit.Test;

/***
 * 适配器模式： 将不同类型的对象可以通过适配模式，在一起工作
 * 
 * @author think
 *
 */
public class TestAdapter {

	@Test
	public void test() {
		DBSocket socket = new DBSocketImpl();
		socket.charge();

		System.out.println("==========");

		GBSocket gbSocket = new GBSocketImpl();
		SockerAdapter adapter1 = new SockerAdapter(gbSocket);
		adapter1.charge();

		System.out.println("==========");

		SockerAdapter adapter2 = new SockerAdapter(socket);
		adapter2.charge();
	}
}
