package com.wx.demo;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import net.sf.json.JSONObject;

/**
 * 
 * @author KeXin
 *��ȡaccess_token
 */
public class WeixinAccessTokenDemo {
public String getAccess_token(){
	final String appid="";//΢�źŵ�appid
	final String secret="";//΢�źŵ�secret
	String urlstr="http://api.weixin.qq.com/cgi-bin/token?grant_type=client_cre dential&appid=APPID&secret=APPSECRET";
	urlstr=urlstr.replace("APPID", appid);
	urlstr=urlstr.replace("APPSECRET", secret);
	URL url;
	String access_token="";
	try{
		url=new URL(urlstr);
		HttpURLConnection http=(HttpURLConnection)url.openConnection();
		http.setRequestMethod("GET");
		http.setRequestProperty("Content-Type", "application/x-www-form-ur lencoded");
		http.setDoInput(true);
		System.setProperty("sun.net.client.defaultConnectTimeout", "30000");//���ӳ�ʱ30��
		System.setProperty("sun.net.client.defaultReadTimeout", "30000");//��ȡ��ʱ30��
		InputStream is=http.getInputStream();
		int size=is.available();
		byte[] buf=new byte[size];
		is.read(buf);
		String resp=new String(buf,"UTF-8");
		System.out.println("getAccess_token resp:"+resp);
		JSONObject jsonObject=JSONObject.fromObject(resp);
		Object object=jsonObject.get("access_token");
		if(object!=null){
			System.out.println("��ȡaccess_token�ɹ�");
			access_token=String.valueOf(object);
			System.out.println("access_token:"+access_token);
		}else{
			System.out.println("��ȡaccess_tokenʧ��");
		}
		return access_token;
	}catch(MalformedURLException e){
		e.printStackTrace();
		return access_token;
	}catch(IOException e){
		e.printStackTrace();
		return access_token;
	}
}
public void main(String[] args){
	new WeixinAccessTokenDemo().getAccess_token();
}
}
