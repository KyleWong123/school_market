package com.school_market.dao;
/***
 * Dao≤„≤‚ ‘¿‡
 * @author wf
 *
 */

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.school_market.BaseTest;
import com.school_market.entity.Area;

public class AreaDaoTest extends BaseTest{
	@Autowired
	private AreaDao areaDao;
	
	@Test
	public void testQuery(){
		List<Area> list = areaDao.query();
	}
	

}
