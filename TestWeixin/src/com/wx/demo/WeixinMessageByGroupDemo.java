package com.wx.demo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * @author KeXin
 *
 */
public class WeixinMessageByGroupDemo {
	/**
	 * 获取access_token
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
	 * 
	 * @return JSONArray
	 */
	private JSONArray getOpenids(){
		JSONArray array=null;
		String urlstr="http://api.weixin.qq.com/cgi-bin/user/get?access_token=Access_TOKEN &next_openid=NEXT_OPENID";
		urlstr=urlstr.replaceAll("ACCESS-TOKEN", getAccess_token());
		urlstr=urlstr.replaceAll("NEXT_OPENID", "");
		URL url;
		try{
			url=new URL(urlstr);
			HttpURLConnection http=(HttpURLConnection)url.openConnection();
			http.setRequestMethod("GET");
			http.setRequestProperty("Content-Type","application/x-www-form-rurlencoded");
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
		}catch(Exception e){
			return array;
		}
	}
	/**
	 * 
	 */
	public void testsendTextByOpenids(){
		String urlstr="http://api.weixin.qq.com/cgi-bin/message/mass/send?access_token=ACCESS_TOKEN";
		String reqjson=createGroupText(getOpenids());
		try{
			URL httpclient=new URL(urlstr);
			HttpURLConnection conn=(HttpURLConnection)httpclient.openConnection();
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(2000);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.connect();
			OutputStream os=conn.getOutputStream();
			System.out.println("req:"+reqjson);
			os.write(reqjson.getBytes("UTF-8"));
			os.flush();
			os.close();
			InputStream is=conn.getInputStream();
			int size=is.available();
			byte[] jsonBytes=new byte[size];
			is.read(jsonBytes);
			String message=new String(jsonBytes,"UTF-8");
			System.out.println("resp:"+message);
		}catch(MalformedURLException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @param openids
	 * @return
	 */
	private String createGroupText(JSONArray array) {
		JSONObject gjson=new JSONObject();
		gjson.put("touser", array);
		gjson.put("msgtype", "text");
		JSONObject text=new JSONObject();
		text.put("content", "hello from boxer.");
		gjson.put("text", text);
		return gjson.toString();
	}
	public static void main(String[] args){
		new WeixinMessageByGroupDemo().testsendTextByOpenids();
	}
}
