package com.school_market.service;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.school_market.BaseTest;
import com.school_market.entity.Area;

public class AreaServiceTest extends BaseTest{
	@Autowired
	private AreaService areaService;
	@Test
	public void testGetQuery(){
		List<Area> list = areaService.getQueryList();
		assertEquals("±¦¼¦",list.get(0).getAreaName());
		System.out.println(list.get(0).getAreaName());
	}

}
