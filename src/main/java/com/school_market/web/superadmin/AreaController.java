package com.school_market.web.superadmin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.school_market.entity.Area;
import com.school_market.service.AreaService;

@Controller
@RequestMapping("/superadmin")
public class AreaController {
	@Autowired
	private AreaService areaService;
	@RequestMapping(value="/listArea",method=RequestMethod.GET)
	@ResponseBody//将返回值自动转为json格式
	private Map<String,Object> listArea(){
		//存放方法的返回值
		Map<String,Object> modelMap = new HashMap<String,Object>();
		List<Area> arealist = new ArrayList<Area>();
		try {
			arealist = areaService.getQueryList();
			//打印查询结果
			modelMap.put("rows", arealist);
			modelMap.put("total", arealist.size());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return modelMap;
		
	}
}
