package com.kkb.ssm.controller;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.kkb.ssm.exception.CustomExcetion;
import com.kkb.ssm.po.Item;
import com.kkb.ssm.po.ItemQueryVO;
import com.kkb.ssm.service.ItemService;

@RequestMapping(value = "item", produces = "application/json;charset=utf8")
@Controller
public class ItemController {

	@Autowired
	private ItemService service;

	/*
	 * @RequestMapping("queryItem") public ModelAndView queryItem() {
	 * 
	 * // 查询数据库，用静态数据模拟 List<Item> itemList = service.queryItemList();
	 * 
	 * ModelAndView mvAndView = new ModelAndView();
	 * 
	 * mvAndView.addObject("itemList", itemList);
	 * 
	 * // 设置视图(逻辑路径) mvAndView.setViewName("item/item-list");
	 * 
	 * return mvAndView; }
	 */

	/*
	 * @RequestMapping("queryItem") public String queryItem(HttpServletRequest
	 * request, Model model) {
	 * 
	 * // 查询数据库，用静态数据模拟 List<Item> itemList = service.queryItemList();
	 * 
	 * // 使用Request的API // request.setAttribute("itemList", itemList); //
	 * 使用Model接口的API model.addAttribute("itemList", itemList);
	 * System.out.println("代码走到这了...");
	 * 
	 * // return "item/item-list";
	 * 
	 * // 转发和重定向使用到的代码 request.setAttribute("id", 1); // return
	 * "redirect:testRedirect"; return "forward:testForward"; }
	 */

	// 请求重定向测试
	@RequestMapping("testRedirect")
	public String testRedirect(HttpServletRequest request) {
		String id = (String) request.getAttribute("id");
		System.out.println("request域的数据：" + id);

		return "";
	}

	// 请求转发测试
	@RequestMapping("testForward")
	public String testForward(HttpServletRequest request) {
		Integer id = (Integer) request.getAttribute("id");
		System.out.println("request域的数据：" + id);

		// ModelAndView中实质上就是进行视图转发。
		return "";
	}

	@RequestMapping("queryItemById")
	@ResponseBody
	public Item queryItemById() {
		Item item = service.queryItemById(1);
		return item;
	}

	// 设置响应体编码格式
	@RequestMapping(value = "testReturnString", produces = "application/json;charset=utf8", method = RequestMethod.POST)
	// 分别针对该方法测试带注解的和不带注解的
	@ResponseBody
	public String testReturnString() {
		return "开课吧";
	}

	// 设置响应体编码格式
	@RequestMapping(value = "testReturnString", produces = "application/json;charset=utf8", method = RequestMethod.GET)
	// 分别针对该方法测试带注解的和不带注解的
	@ResponseBody
	public String testReturnString2() {
		return "开课吧222";
	}

	/*
	 * @RequestMapping("findItem")
	 * 
	 * @ResponseBody public String findItem(Integer id) { return "接收到的请求参数是：" + id;
	 * }
	 */

	// @RequestParam 相当于Request.getParameter(请求参数key)
	/*@RequestMapping("findItem")
	@ResponseBody
	public String findItem(@RequestParam(value = "itemid", required = true, defaultValue = "3") Integer id) {
		return "接收到的请求参数是：" + id;
	}*/

	@RequestMapping("updateItem")
	@ResponseBody
	public Item updateItem(Integer id, String name, Float price, Item item, MultipartFile pictureFile) throws Exception{
		if (pictureFile != null) {
			//获取上传文件名称
			String originalFilename = pictureFile.getOriginalFilename();
			if (originalFilename != null && !"".contentEquals(originalFilename)) {
				//获取扩展名
				String extName = originalFilename.substring(originalFilename.lastIndexOf("."));
				//重新生成一个文件名称
				String newFileName = UUID.randomUUID().toString()+extName;
				//指定存储文件的根目录
				String baseDir="E:\\07-upload\\temp\\";
				File dirFile=new File(baseDir);
				if (!dirFile.exists()) {
					dirFile.mkdirs();
				}
				//将上传的文件复制到新的文件(完整路径)中
				pictureFile.transferTo(new File(baseDir + newFileName));
				
				//保存文件路径
				item.setPic(newFileName);
			}
		}
		
		//商品修改
		service.updateItem(item);
		
		return item;
	}

	/*
	 * @RequestMapping("queryItem")
	 * 
	 * @ResponseBody public Item queryItem(ItemQueryVO vo) { return vo.getItem(); }
	 */

	@RequestMapping("deleteItem")
	@ResponseBody
	public String[] deleteItem(String[] id) {
		return id;
	}

	@RequestMapping("batchUpdateItem")
	@ResponseBody
	public List<Item> batchUpdateItem(List<Item> itemList, ItemQueryVO vo) {
		return vo.getItemList();
	}

	@RequestMapping("queryItem")
	public String queryItem(ItemQueryVO vo, Model model) throws CustomExcetion {
		// 查询数据库，用静态数据模拟
		List<Item> itemList = service.queryItemList();

		if (itemList.size() < 10) {
			throw new CustomExcetion("我是自定义异常类");
		}
		// 使用Request的API
		// request.setAttribute("itemList", itemList);
		// 使用Model接口的API
		model.addAttribute("itemList", itemList);
		System.out.println("代码走到这了...");

		return "item/item-list";
	}

	@RequestMapping("saveItem")
	@ResponseBody
	public Date saveItem(@DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
		return date;
	}
	
	@RequestMapping("showEdit")
	public String showEdit(Integer id,Model model) {
		Item item = service.queryItemById(id);
		
		model.addAttribute("item", item);
		
		return "item/item-edit";
	}
	
	@RequestMapping("findItem")
	@ResponseBody
	public Item findItem( Integer id) {
		return service.queryItemById(id);
	}
}
