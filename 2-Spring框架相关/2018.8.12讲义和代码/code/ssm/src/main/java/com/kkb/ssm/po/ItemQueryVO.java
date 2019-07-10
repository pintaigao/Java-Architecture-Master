package com.kkb.ssm.po;

import java.util.List;

public class ItemQueryVO {

	private Item item;

	private List<Item> itemList;
	
	//private Item[] itemList;
	
	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public List<Item> getItemList() {
		return itemList;
	}

	public void setItemList(List<Item> itemList) {
		this.itemList = itemList;
	}

	
}
