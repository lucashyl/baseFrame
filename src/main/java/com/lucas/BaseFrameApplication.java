package com.lucas;

import com.lucas.admin.util.websocketUtil.center.impl.JobCenter;
import com.spring4all.swagger.EnableSwagger2Doc;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

//@EnableSwagger2Doc
//@EnableSwagger2
@SpringBootApplication
@MapperScan("com.lucas.**.dao")
public class BaseFrameApplication extends SpringBootServletInitializer {

//	@Override
//	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//		return application.sources(MysiteformeApplication.class);
//	}

	public static void main(String[] args) {
		SpringApplication.run(BaseFrameApplication.class, args);
		JobCenter jobCenter = new JobCenter(10);
		jobCenter.start();
}
}
