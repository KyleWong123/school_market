package com.school_market.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.school_market.entity.HeadLine;

public interface HeadLineDao {
	/***
	 * ���ݴ����������ѯͷ��
	 */
	public List<HeadLine> queryHeadLine(@Param("headLineCondition") HeadLine headLineCondition );

}
