package com.lucas.admin;

import com.alibaba.fastjson.JSONObject;
import com.lucas.admin.entity.Menu;
import com.lucas.admin.dao.MenuDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BaseFrameApplicationTests {

	private static final Logger LOGGER = LoggerFactory.getLogger(BaseFrameApplicationTests.class);

	@Autowired
	private MenuDao menuDao;

	@Test
	public void contextLoads() {
		List<Menu> list = menuDao.getMenus(null);
		LOGGER.info(JSONObject.toJSONString(list));
	}

}
