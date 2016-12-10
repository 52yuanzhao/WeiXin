package com.wx.demo;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import net.sf.json.JSONObject;

/**
 * 
 * @author KeXin
 * 公众号通过本接口可以上传图片，语音，视频等文件到微信服务器，上传后服务器会返回对应的media_id，
 * 公众号此后可以根据该media_id来获取多媒体。而且madia_id是可以复用得，调用该接口需要http协议
 * 图片：JPG，1M
 * 语音：2M，播放长度不超过60s，支持AMR\MP3
 * 视频：10MB，支持MP4格式
 * 缩略图：64KB，支持JPG格式
 *
 */
public class WeixinMultimediaDemo {
	/*
	 * 获取access_token
	 */
private String getAccess_token(){
	final String appid="";//微信的appid
	final String secret="";//微信的secret
	String urlstr="http://api.weixin.qq.com/cgi-bin/token?grant_type=client_cre dential&appid=APPID&secret=APPSECRET";
	urlstr=urlstr.replace("APPID",appid);
	urlstr=urlstr.replace("APPSECRET",secret);
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
			access_token=String.valueOf(object);
			System.out.println("access_token:"+access_token);
			System.out.println("获取access_token成功!");
		}else{
			System.out.println("获取access_token失败!");
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
 * 上传多媒体，type是类型，filePath是路径
 */
public void upload(String type,String filePath){
	String url="http://api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";
	url=url.replaceAll("ACCESS_TOKEN", getAccess_token());
	url=url.replaceAll("TYPE", type);
	System.out.println(url);
	File file=new File(filePath);
	if(!file.exists()||!file.isFile()){
		return;
	}
	
	URL urlObj;
	try{
		urlObj=new URL(url);
		HttpURLConnection con=(HttpURLConnection)urlObj.openConnection();
		//设置关键值
		con.setRequestMethod("POST");
		con.setDoInput(true);
		con.setDoOutput(true);
		con.setUseCaches(false);//post方式不能使用缓存
		//设置请求头消息
		con.setRequestProperty("Connection", "Keep-Alive");
		con.setRequestProperty("Charset", "UTF-8");
		//设置边界
		String BOUNDARY="------------"+System.currentTimeMillis();
		con.setRequestProperty("Content-Type", "multipart/form-data;boundary="+BOUNDARY);
		//请求正文信息
		//第一部分：
		StringBuilder sb=new StringBuilder();
		sb.append("--");////////必须多两道线
		sb.append(BOUNDARY);
		sb.append("\r\n");
		sb.append("Content-Disposition:form-data;name=\"file\";filename=\""+file.getName()+"\"r\n");
		sb.append("Content-Type:application/octer-stream\r\n\r\n");
		byte[] head=sb.toString().getBytes("utf-8");
		//获得输出流
		OutputStream out=new DataOutputStream(con.getOutputStream());
		out.write(head);
		//文件正文部分
		DataInputStream in=new DataInputStream(new FileInputStream(file));
		int bytes=0;
		byte[] bufferOut=new byte[1024];
		while((bytes=in.read(bufferOut))!=-1){
			out.write(bufferOut,0,bytes);
		}
		in.close();
		//结尾部分
		byte[] foot=("\r\n--"+BOUNDARY+"--\r\n").getBytes("utf-8");//定义最后数据分割线
		out.write(foot);
		out.flush();
		out.close();
		//定义BufferedReader输入流来读取URL的响应
		BufferedReader reader=new BufferedReader(new InputStreamReader(con.getInputStream()));
		String line=null;
		StringBuffer json=new StringBuffer();
		while((line=reader.readLine())!=null){
			json.append(line);
			System.out.println(line);
		}
		System.out.println("json:"+json);
		JSONObject jsonObject=JSONObject.fromObject(json);
		if(jsonObject.get("errcode")==null){
			System.out.println("多媒体上传成功，media_id="+jsonObject.getString("media_id"));
		}else{
			System.out.println("多媒体上传成功");
		}
	}catch(MalformedURLException e){
		e.printStackTrace();
	}catch(IOException e){
		e.printStackTrace();
	}
}
/**
 * 多媒体下载
 */
public void down(String media_id,String filePath){
	String url="http://api.weixin.qq.com/cgi-bin/media/get?access_token=ACCESS_TOKEN&media_id=MEDIA_ID";
	url=url.replaceAll("ACCESS_TOKEN", getAccess_token());
	url=url.replaceAll("MEDIA_ID", media_id);
	File file=new File(filePath);
	try{
		if(!file.exists()){
			file.createNewFile();
		}else{
			file.delete();
			file.createNewFile();
		}
		URL urlObj=new URL(url);
		HttpURLConnection con=(HttpURLConnection)urlObj.openConnection();
		/**
		 * 设置关键字
		 */
		con.setRequestMethod("GET");//用get方式下载文件
		con.setDoInput(true);
		con.setDoOutput(true);
		con.setRequestProperty("Content-Type", "application/x-www-form-url encoded");
		//获取输出流
		con.connect();
		OutputStream out=new FileOutputStream(new File(filePath));
		ByteArrayOutputStream buffer=new ByteArrayOutputStream();
		InputStream in=con.getInputStream();
		int bytes=0;
		byte[] bufferOut=new byte[1024];
		while((bytes=in.read(bufferOut))!=-1){
			buffer.write(bufferOut,0,bytes);
			out.write(bufferOut,0,bytes);
		}
		out.flush();
		out.close();
		in.close();
		System.out.println("ResponseCode!"+con.getResponseCode());
	}catch(IOException e){
		e.printStackTrace();
	}
}
public void main(String[] args){
	new WeixinMultimediaDemo().upload("video","F:\\weixin\\media\\VID_20131023_145906.mp4");
	new WeixinMultimediaDemo().down("S16n4bf92Us-5Vbo6CgHmbFx6DF8O219E6uuZwEv1ccxCd7dJ6M2c-2CH49Tz_3Y","F:\\weixin\\media\\test.mp4");
}
}
