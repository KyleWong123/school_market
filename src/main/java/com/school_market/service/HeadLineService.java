package com.school_market.service;

import java.io.IOException;
import java.util.List;

import com.school_market.entity.HeadLine;

public interface HeadLineService {
	/***
	 * ���ݴ����������ѯͷ��
	 */
	List<HeadLine> getHeadLineList(HeadLine headLineCondition) throws IOException;

}
