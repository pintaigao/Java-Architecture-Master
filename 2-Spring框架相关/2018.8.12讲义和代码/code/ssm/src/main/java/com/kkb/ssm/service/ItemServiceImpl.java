package com.kkb.ssm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kkb.ssm.mapper.ItemMapper;
import com.kkb.ssm.po.Item;
import com.kkb.ssm.po.ItemExample;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private ItemMapper mapper;

	@Override
	public List<Item> queryItemList() {
		// 使用逆向工程代码完成持久层查询
		ItemExample example = new ItemExample();
		// Criteria criteria = example.createCriteria();
		// criteria.andIdEqualTo(1);
		List<Item> list = mapper.selectByExample(example);

		return list;
	}
	
	@Override
	public Item queryItemById(Integer id) {
		return id == null ? null : mapper.selectByPrimaryKey(id);
	}
	
	@Override
	public void updateItem(Item item) {
		if (item == null) {
			return;
		}
		if (item.getId() == null) {
			return ;
		}
		
		mapper.updateByPrimaryKeySelective(item);
	}

}
