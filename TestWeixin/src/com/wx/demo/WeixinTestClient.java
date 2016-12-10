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
 * ΢�ſͻ���ģ�����
 *
 */
public class WeixinTestClient {
	
	public void main(String[] args){
		new WeixinTestClient().testWeixin1();
		new WeixinTestClient().testWeixin2();
	}
/**
 * ģ��click�¼�
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
			String repXML=createRequestClick("1011");//ģ��click�¼�
			os.write(repXML.getBytes("UTF-8"));
			os.flush();
			os.close();
			InputStream is=http.getInputStream();
			int size=is.available();
			byte[] jsonBytes=new byte[size];
			is.read(jsonBytes);
			String message=new String(jsonBytes,"UTF-8");
			System.out.println("���ȣ�"+message.getBytes().length);
		}catch(MalformedURLException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	//����click��XML
	private String createRequestClick(String key) {
		StringBuffer revert=new StringBuffer();
		revert.append("<xml>");
		revert.append("<ToUserName><![CDATA[���ں�]]></ToUserName>");
		revert.append("<FromUserName><![CDATA[��ע��openid]]></FromUserName>");
		revert.append("<CreateTime>1376383328</CreateTime>");
		revert.append("<MsgType><!CDATA[event]]></MsgType>");
		revert.append("<Event><![CDATA[CLICK]]></Event");
		revert.append("<EventKey><![CDATA["+key+"]]></EventKey");
		revert.append("</xml>");
		return revert.toString();
}
/**
 * ģ���ı�����
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
			String repXML=createRequestTest("��ѯ");//ģ���ѯ�¼�
			os.write(repXML.getBytes("UTF-8"));
			os.flush();
			os.close();
			InputStream is=http.getInputStream();
			int size=is.available();
			byte[] jsonBytes=new byte[size];
			is.read(jsonBytes);
			String message=new String(jsonBytes,"UTF-8");
			System.out.println("���ȣ�"+message.getBytes().length);
		}catch(MalformedURLException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	//����text��XML
private String createRequestTest(String text) {
			StringBuffer revert=new StringBuffer();
			revert.append("<xml>");
			revert.append("<ToUserName><![CDATA[���ں�]]></ToUserName>");
			revert.append("<FromUserName><![CDATA[��ע��openid]]></FromUserName>");
			revert.append("<CreateTime>1376383328</CreateTime>");
			revert.append("<MsgType><!CDATA[text]]></MsgType>");
			revert.append("<Content><![CDATA["+text+"]]></Content");
			revert.append("<MsgId>5911521380519646812</MsgId>");
			revert.append("</xml>");
			return revert.toString();
}
}
