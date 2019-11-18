package cn.snow.httpclient;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/applicationContext-httpclient.xml"})
public class HttpClientTest {
	
	@Autowired
	private ApiService apiService;
	@Test
	public void test1() throws ClientProtocolException, IOException{
		String url = "http://ip:port/service";

		JSONArray jsonArrayGoodIds = new JSONArray();
		jsonArrayGoodIds.add(69422510164L);

		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("activityId",7834164);
		jsonObject.put("isActivityExpire",false);
		jsonObject.put("pid",56101164);
		jsonObject.put("activityType",3);
		jsonObject.put("storeId",200025164);
		jsonObject.put("goodsIdList",jsonArrayGoodIds);
		jsonArray.add(jsonObject);
		JSONObject json = new JSONObject();
		json.put("serviceName","activityGoodsUpdateExportService");
		json.put("methodName","batchDeleteActivityGoods");
		json.put("paramterInput",jsonArray);

		String paramStr = json.toJSONString();
		System.out.println(paramStr);
		HttpResult  httpResult = apiService.doPostJson(url, URLDecoder.decode(paramStr));
		System.out.println(JSON.toJSONString(httpResult));

	}

	/**
	 * 用于订正数据，构造表单请求参数
	 * @throws ClientProtocolException
	 * @throws IOException
	 */

	@Test
	public void test2() throws ClientProtocolException, IOException{

		String goodIds = FileUtils.readFileToString(new File("D:\\goodIds2.txt"));
		String goodIdArr[] = goodIds.split(",");
		int countNum = goodIdArr.length;
		if(0 == countNum){
			return;
		}
		List<Long> goodIdList = new ArrayList<>();
		for(String goodId : goodIdArr){
			if(StringUtils.isBlank(goodId)){
				continue;
			}
			goodIdList.add(Long.valueOf(goodId));
		}
		int timeNum = countNum%20==0?(countNum/20-1): countNum/20;
		for(int time=0;time <=timeNum ; time++){
			int start = time *20;
			int end = (time+1)*20>= countNum ? countNum : (time+1)*20;
			getResult(goodIdList.subList(start,end));
		}

	}

	private void getResult(List<Long> goodIds) throws IOException {
		String url = "http://ip:port/service";
		JSONArray jsonArrayGoodIds = new JSONArray();

		for(long goodId : goodIds){
			jsonArrayGoodIds.add(goodId);
		}
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("activityId",7834164);
		jsonObject.put("isActivityExpire",false);
		jsonObject.put("pid",56101164);
		jsonObject.put("activityType",3);
		jsonObject.put("storeId",200025164);
		jsonObject.put("goodsIdList",jsonArrayGoodIds);
		jsonArray.add(jsonObject);
		Map paramMap = new HashMap();
		paramMap.put("serviceName","activityGoodsUpdateExportService");
		paramMap.put("methodName","batchDeleteActivityGoods");
		paramMap.put("paramterInput",jsonArray.toJSONString());

		String paramStr = JSON.toJSONString(paramMap);
		System.out.println(paramStr);
		HttpResult httpResult = apiService.doPost(url,paramMap);
		System.out.println(JSON.toJSONString(httpResult));
	}


}
