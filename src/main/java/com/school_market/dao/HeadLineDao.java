package com.school_market.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.school_market.entity.HeadLine;

public interface HeadLineDao {
	/***
	 * 根据传入的条件查询头条
	 */
	public List<HeadLine> queryHeadLine(@Param("headLineCondition") HeadLine headLineCondition );

}
