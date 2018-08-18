package com.school_market.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.school_market.dao.AreaDao;
import com.school_market.entity.Area;
import com.school_market.service.AreaService;
@Service
public class AreaServiceImpl implements AreaService{
	@Autowired
	private AreaDao areaDao;

	@Override
	public List<Area> getQueryList() {
		// TODO Auto-generated method stub
		return areaDao.query();
	}

}
