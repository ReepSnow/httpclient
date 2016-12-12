package cn.snow.httpclient;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/applicationContext-httpclient.xml"})
public class HttpClientTest {
	
	@Autowired
	private ApiService apiService;
	@Test
	public void test1() throws ClientProtocolException, IOException{
		String result = apiService.doGet("http://www.baidu.com/s?wd=java");
		System.out.println(result);
	}
}
