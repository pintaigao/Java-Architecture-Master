package com.kkb.ssm.design_pattern.adapter;

/***
 * 适配器(将不同类型的GBSocket和DBSocket都适配成GJBZSocket)
 * 
 * @author think
 *
 */
public class SockerAdapter implements GJBZSocket {

	private Object socket;

	public SockerAdapter(Object socket) {
		super();
		this.socket = socket;
	}

	@Override
	public void charge() {
		if (socket instanceof GBSocket) {
			((GBSocket)socket).charge();
		}else if (socket instanceof DBSocket) {
			((DBSocket)socket).charge();
		}
	}
}
