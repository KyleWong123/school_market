package com.school_market;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/***
 * ��ʼ��spring���ã�����spring��junit���ϣ�junit����ʱ����springIOC����
 * @author wf
 * ע�⿪��
 */
@RunWith(SpringJUnit4ClassRunner.class)
//����junit spring�����ļ���λ��
@ContextConfiguration({"classpath:spring/spring-dao.xml","classpath:spring/spring-service.xml"})
public class BaseTest {

}
