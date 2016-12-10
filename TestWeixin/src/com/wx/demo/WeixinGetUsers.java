package com.wx.demo;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * @author KeXin
 * 通过本接口获取关注者列表 由一串Openid组成
 * 一次最多拉取10000个关注者的openid
 * 可通过填写next_openid的值来达到多次拉取的方式获得更多关注者的openid
 * 具体而言就是在调用接口时，将上次调用得到的返回中的next_openid值作为下一次调用next_openid值
 *
 */
public class WeixinGetUsers {
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
	 * 获取当前关注者的所有openid
	 * @return JSONArray,openid数组格式【“OPENID1”,"OPENID2",……】
	 */
	public JSONArray getOpenids(){
		JSONArray array=null;
		String urlstr="http://api.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN&next_openid=NEXT_OPENID";
		urlstr=urlstr.replaceAll("ACCESS_TOKEN", getAccess_token());
		urlstr=urlstr.replace("Next_OPENID", "");
		URL url;
		try{
			url=new URL(urlstr);
			HttpURLConnection http=(HttpURLConnection)url.openConnection();
			http.setRequestMethod("GET");
			http.setRequestProperty("Content-Type", "application/x-www-form-ur lencoded");
			http.setDoInput(true);
			InputStream is=http.getInputStream();
			int size=is.available();
			byte[] buf=new byte[size];
			is.read(buf);
			String resp=new String(buf,"UTF-8");
			JSONObject jsonObject=JSONObject.fromObject(resp);
			System.out.println("resp:"+jsonObject.toString());
			array=jsonObject.getJSONObject("data").getJSONArray("openid");
			return array;
		}catch(MalformedURLException e){
			e.printStackTrace();
			return array;
		}catch(IOException e){
			e.printStackTrace();
			return array;
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
			new WeixinGetUsers().getOpenids();
	}

}
