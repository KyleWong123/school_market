package com.school_market.dao;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.school_market.BaseTest;
import com.school_market.entity.HeadLine;

public class HeadLineDaoTest extends BaseTest{
	@Autowired
	private HeadLineDao headLineDao;
	@Test
	public void queryHeadLineTest(){
		HeadLine headLineCondition = new HeadLine();
		headLineDao.queryHeadLine(headLineCondition);
	}
}
