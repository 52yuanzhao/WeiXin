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
 * 每个用户对于一个公众号的openid是唯一的，通过本接口获取用户基本信息
 * 包括昵称，头像，性别，所在城市和关注时间
 *
 */
public class WeixinGetUserInfoDemo {
	/**
	 * 获取Access_token
	 * @return
	 */
	public String getAccess_token(){
		final String appid="";//微信号的appid
		final String secret="";//微信号的secret
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
			System.setProperty("sun.net.client.defaultConnectTimeout", "30000");//连接超时30秒
			System.setProperty("sun.net.client.defaultReadTimeout", "30000");//读取超时30秒
			InputStream is=http.getInputStream();
			int size=is.available();
			byte[] buf=new byte[size];
			is.read(buf);
			String resp=new String(buf,"UTF-8");
			System.out.println("getAccess_token resp:"+resp);
			JSONObject jsonObject=JSONObject.fromObject(resp);
			Object object=jsonObject.get("access_token");
			if(object!=null){
				System.out.println("获取access_token成功");
				access_token=String.valueOf(object);
				System.out.println("access_token:"+access_token);
			}else{
				System.out.println("获取access_token失败");
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
	/**
	 * 根据openid获取用户的基本信息
	 * @param openid
	 * @return
	 */
	public String getUserInfo(String openid){
		String userinfourl="http://api.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN&next_openid=OPENID";
		String json="";
		try{
			userinfourl=userinfourl.replaceAll("ACCESS_TOKEN", getAccess_token());
			userinfourl=userinfourl.replaceAll("OPENID", openid);
			URL httpclient=new URL(userinfourl);
			HttpURLConnection conn=(HttpURLConnection)httpclient.openConnection();
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(2000);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.connect();
			InputStream is=conn.getInputStream();
			int size=is.available();
			byte[] jsonBytes=new byte[size];
			is.read(jsonBytes);
			json=new String(jsonBytes,"UTF-8");
			JSONObject rejson=JSONObject.fromObject(json);
			System.out.println("getAccess_token resp:"+rejson);
			return json;
		}catch(MalformedURLException e){
			e.printStackTrace();
			return json;
		}catch(IOException e){
			e.printStackTrace();
			return json;
		}
	}
	public static void main(String[] args){
		new WeixinGetUserInfoDemo().getUserInfo("###################");//替换成自己的openid
	}
	
}
