package com.dothings.training.trainingsearch;

import com.dothings.training.bean.EsAggBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TrainingSearchApplicationTests {

	@Test
	public void contextLoads() {
		EsAggBean esAggBean=new EsAggBean();
		esAggBean.setName("35634563");
		System.out.println(esAggBean);
	}

}
