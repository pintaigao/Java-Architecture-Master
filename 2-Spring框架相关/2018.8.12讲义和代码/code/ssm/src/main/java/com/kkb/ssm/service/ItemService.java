package com.kkb.ssm.service;

import java.util.List;

import com.kkb.ssm.po.Item;

public interface ItemService {

	List<Item> queryItemList();
	
	Item queryItemById(Integer id);
	
	void updateItem(Item item);
}
