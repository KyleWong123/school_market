package com.school_market.service;

import java.io.IOException;
import java.util.List;

import com.school_market.entity.HeadLine;

public interface HeadLineService {
	/***
	 * 根据传入的条件查询头条
	 */
	List<HeadLine> getHeadLineList(HeadLine headLineCondition) throws IOException;

}
