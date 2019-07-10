package com.kkb.ssm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kkb.ssm.po.Item;
import com.kkb.ssm.service.ItemService;

//@RestController相当于@Controller和@ResponseBody的组合
//该类所有方法的返回值都将被@ResponseBody注解给修饰
@RestController
public class RestItemController {

	@Autowired
	private ItemService service;
	
	@RequestMapping("queryItemByIdWithRest")
	public Item queryItemById() {
		Item item = service.queryItemById(1);
		return item;
	}
	
	@RequestMapping("testReturnStringWithRest")
	public String testReturnString() {
		return "ok";
	}
}
