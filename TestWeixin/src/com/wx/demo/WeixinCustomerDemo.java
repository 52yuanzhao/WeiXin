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
 * 通过本接口响应客户的一些请求，主要用于有人工消息处理环节的功能
 * 方便为用户提供更优质的服务
 *
 */
/**
 * 微信发送客户消息
 */
public class WeixinCustomerDemo {
	/**
	 * 获取acces_token
	 * @return String
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
	 * 发送文本
	 * @param openid
	 * @param contentstr
	 */
	public void sendText(String openid,String contentstr){
		String customerurl="http://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";
		String json="";
		try{
			customerurl=customerurl.replaceAll("ACCESS_TOKEN", getAccess_token());
			//创建发送的文本字符串
			JSONObject resjson=new JSONObject();
			resjson.put("touser", openid);
			resjson.put("msgtype","text");
			JSONObject content=new JSONObject();
			content.put("content", contentstr);
			System.out.println(resjson.toString());
			//给用户推送文本
			URL httpclient;
			httpclient=new URL(customerurl);
			HttpURLConnection conn=(HttpURLConnection)httpclient.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.connect();
			OutputStream os=conn.getOutputStream();
			os.write(resjson.toString().getBytes("UTF-8"));
			//传入参数
			os.flush();
			os.close();
			InputStream is=conn.getInputStream();
			int size=is.available();
			byte[] jsonBytes=new byte[size];
			is.read(jsonBytes);
			json=new String(jsonBytes,"UTF-8");
			System.out.println(json);
		}catch(MalformedURLException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	/**
	 * 发送图片
	 * @param openid
	 * @param mediaid
	 */
	public void sendImage(String openid,String media_id){
		String customerurl="http://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";
		String json="";
		try{
			customerurl=customerurl.replaceAll("ACCESS_TOKEN", getAccess_token());
			//创建发送的图片
			JSONObject resjson=new JSONObject();
			resjson.put("touser", openid);
			resjson.put("msgtype","image");
			JSONObject image=new JSONObject();
			image.put("media_id", media_id);
			resjson.put("image", image);
			System.out.println(resjson.toString());
			//给用户推送文本
			URL httpclient;
			httpclient=new URL(customerurl);
			HttpURLConnection conn=(HttpURLConnection)httpclient.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.connect();
			OutputStream os=conn.getOutputStream();
			os.write(resjson.toString().getBytes("UTF-8"));
			//传入参数
			os.flush();
			os.close();
			InputStream is=conn.getInputStream();
			int size=is.available();
			byte[] jsonBytes=new byte[size];
			is.read(jsonBytes);
			json=new String(jsonBytes,"UTF-8");
			System.out.println(json);
		}catch(MalformedURLException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	/**
	 * 发送语音
	 * @param openid
	 * @param media_id
	 */
	public void sendVoice(String openid,String media_id){
		String customerurl="http://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";
		String json="";
		try{
			customerurl=customerurl.replaceAll("ACCESS_TOKEN", getAccess_token());
			//创建发送的图片
			JSONObject resjson=new JSONObject();
			resjson.put("touser", openid);
			resjson.put("msgtype","voice");
			JSONObject voice=new JSONObject();
			voice.put("media_id", media_id);
			resjson.put("voice", voice);
			System.out.println(resjson.toString());
			//给用户推送文本
			URL httpclient;
			httpclient=new URL(customerurl);
			HttpURLConnection conn=(HttpURLConnection)httpclient.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.connect();
			OutputStream os=conn.getOutputStream();
			os.write(resjson.toString().getBytes("UTF-8"));
			//传入参数
			os.flush();
			os.close();
			InputStream is=conn.getInputStream();
			int size=is.available();
			byte[] jsonBytes=new byte[size];
			is.read(jsonBytes);
			json=new String(jsonBytes,"UTF-8");
			System.out.println(json);
		}catch(MalformedURLException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	/**
	 * 发送视频
	 * @param openid
	 * @param media_id
	 * @param thumb_media_id
	 */
	public void sendVoideo(String openid,String media_id,String thumb_media_id){
		String customerurl="http://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";
		String json="";
		try{
			customerurl=customerurl.replaceAll("ACCESS_TOKEN", getAccess_token());
			//创建发送的图片
			JSONObject resjson=new JSONObject();
			resjson.put("touser", openid);
			resjson.put("msgtype","video");
			JSONObject video=new JSONObject();
			resjson.put("video", video);
			video.put("media_id", media_id);
			video.put("thumb_media_id", thumb_media_id);
			System.out.println(resjson.toString());
			//给用户推送文本
			URL httpclient;
			httpclient=new URL(customerurl);
			HttpURLConnection conn=(HttpURLConnection)httpclient.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.connect();
			OutputStream os=conn.getOutputStream();
			os.write(resjson.toString().getBytes("UTF-8"));
			//传入参数
			os.flush();
			os.close();
			InputStream is=conn.getInputStream();
			int size=is.available();
			byte[] jsonBytes=new byte[size];
			is.read(jsonBytes);
			json=new String(jsonBytes,"UTF-8");
			System.out.println(json);
		}catch(MalformedURLException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	/**
	 * 发送图文
	 * @param openid
	 * 图文发送的图片不需要上传，只传入图片的访问地址就行
	 */
	public void sendImageImage(String openid){
		String customerurl="http://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";
		String json="";
		String url="http://www.baidu.com";//点击跳转链接
		String picurl="";//图片链接
		try{
			customerurl=customerurl.replaceAll("ACCESS_TOKEN", getAccess_token());
			//创建发送的图片
			JSONObject resjson=new JSONObject();
			resjson.put("touser", openid);
			resjson.put("msgtype","news");
			JSONObject news=new JSONObject();
			JSONArray articles=new JSONArray();
			JSONObject article1=new JSONObject();
			article1.put("title", "测试1");
			article1.put("description", "这是测试1");
			article1.put("url", url);
			article1.put("picurl", picurl);
			articles.add(article1);
			news.put("articles", articles);
			resjson.put("news", news);
			System.out.println(resjson.toString());
			URL httpclient;
			httpclient=new URL(customerurl);
			HttpURLConnection conn=(HttpURLConnection)httpclient.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.connect();
			OutputStream os=conn.getOutputStream();
			os.write(resjson.toString().getBytes("UTF-8"));
			//传入参数
			os.flush();
			os.close();
			InputStream is=conn.getInputStream();
			int size=is.available();
			byte[] jsonBytes=new byte[size];
			is.read(jsonBytes);
			json=new String(jsonBytes,"UTF-8");
			System.out.println(json);
		}catch(MalformedURLException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}
