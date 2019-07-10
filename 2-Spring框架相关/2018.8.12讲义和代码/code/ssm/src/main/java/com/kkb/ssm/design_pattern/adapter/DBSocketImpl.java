package com.kkb.ssm.design_pattern.adapter;

/**
 * 德国插排
 * @author think
 *
 */
public class DBSocketImpl implements DBSocket {

	@Override
	public void charge() {
		System.out.println("使用两眼插孔充电");
	}

}
