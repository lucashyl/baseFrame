package com.lucas.admin;

import com.alibaba.fastjson.JSONObject;
import com.lucas.admin.entity.Menu;
import com.lucas.admin.dao.MenuDao;
import com.lucas.admin.redis.RedisService;
import org.apache.commons.lang3.StringUtils;
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
	private RedisService redisService;

	@Test
	public void contextLoads() {
		int i=0;
		while(true) {//redis锁，防止脏读
			i++;
			if (i > 5) {//没有取到锁，返回超时
				System.out.println("请稍后再试");
			}
			Long memberId = 6l;

			//获取锁
			String identifier = redisService.acquireLockWithTimeout(memberId,10000l, 50000l);
			if (!StringUtils.isBlank(identifier)) {
				if ("err".equals(identifier)) {//redis 出错
					System.out.println("网络异常,请稍后再试");
				}
				redisService.releaseLock(memberId,identifier);
				break;
			}
		}
	}

}
