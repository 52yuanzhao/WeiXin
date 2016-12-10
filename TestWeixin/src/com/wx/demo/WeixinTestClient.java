package com.wx.demo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 
 * @author KeXin
 * 微信客户端模拟调试
 *
 */
public class WeixinTestClient {
	
	public void main(String[] args){
		new WeixinTestClient().testWeixin1();
		new WeixinTestClient().testWeixin2();
	}
/**
 * 模拟click事件
 */
	public void testWeixin1() {
		try{
			URL url=new URL("http://127.0.0.1:8080/TestWeixin/weixin");
			HttpURLConnection http=(HttpURLConnection)url.openConnection();
			http.setRequestProperty("Content-Type", "aplication/x-www-form-ur lencoded");
			http.setRequestMethod("GET");
			http.setDoOutput(true);
			http.setDoInput(true);
			OutputStream os=http.getOutputStream();
			String repXML=createRequestClick("1011");//模拟click事件
			os.write(repXML.getBytes("UTF-8"));
			os.flush();
			os.close();
			InputStream is=http.getInputStream();
			int size=is.available();
			byte[] jsonBytes=new byte[size];
			is.read(jsonBytes);
			String message=new String(jsonBytes,"UTF-8");
			System.out.println("长度："+message.getBytes().length);
		}catch(MalformedURLException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	//創建click的XML
	private String createRequestClick(String key) {
		StringBuffer revert=new StringBuffer();
		revert.append("<xml>");
		revert.append("<ToUserName><![CDATA[公众号]]></ToUserName>");
		revert.append("<FromUserName><![CDATA[关注者openid]]></FromUserName>");
		revert.append("<CreateTime>1376383328</CreateTime>");
		revert.append("<MsgType><!CDATA[event]]></MsgType>");
		revert.append("<Event><![CDATA[CLICK]]></Event");
		revert.append("<EventKey><![CDATA["+key+"]]></EventKey");
		revert.append("</xml>");
		return revert.toString();
}
/**
 * 模拟文本输入
 */
	private void testWeixin2() {
		try{
			URL url=new URL("http://127.0.0.1:8080/TestWeixin/weixin");
			HttpURLConnection http=(HttpURLConnection)url.openConnection();
			http.setRequestProperty("Content-Type", "aplication/x-www-form-ur lencoded");
			http.setRequestMethod("GET");
			http.setDoOutput(true);
			http.setDoInput(true);
			OutputStream os=http.getOutputStream();
			String repXML=createRequestTest("查询");//模拟查询事件
			os.write(repXML.getBytes("UTF-8"));
			os.flush();
			os.close();
			InputStream is=http.getInputStream();
			int size=is.available();
			byte[] jsonBytes=new byte[size];
			is.read(jsonBytes);
			String message=new String(jsonBytes,"UTF-8");
			System.out.println("长度："+message.getBytes().length);
		}catch(MalformedURLException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	//創建text的XML
private String createRequestTest(String text) {
			StringBuffer revert=new StringBuffer();
			revert.append("<xml>");
			revert.append("<ToUserName><![CDATA[公众号]]></ToUserName>");
			revert.append("<FromUserName><![CDATA[关注者openid]]></FromUserName>");
			revert.append("<CreateTime>1376383328</CreateTime>");
			revert.append("<MsgType><!CDATA[text]]></MsgType>");
			revert.append("<Content><![CDATA["+text+"]]></Content");
			revert.append("<MsgId>5911521380519646812</MsgId>");
			revert.append("</xml>");
			return revert.toString();
}
}
