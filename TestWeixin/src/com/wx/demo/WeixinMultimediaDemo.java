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
 * ���ں�ͨ�����ӿڿ����ϴ�ͼƬ����������Ƶ���ļ���΢�ŷ��������ϴ���������᷵�ض�Ӧ��media_id��
 * ���ںŴ˺���Ը��ݸ�media_id����ȡ��ý�塣����madia_id�ǿ��Ը��õã����øýӿ���ҪhttpЭ��
 * ͼƬ��JPG��1M
 * ������2M�����ų��Ȳ�����60s��֧��AMR\MP3
 * ��Ƶ��10MB��֧��MP4��ʽ
 * ����ͼ��64KB��֧��JPG��ʽ
 *
 */
public class WeixinMultimediaDemo {
	/*
	 * ��ȡaccess_token
	 */
private String getAccess_token(){
	final String appid="";//΢�ŵ�appid
	final String secret="";//΢�ŵ�secret
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
			access_token=String.valueOf(object);
			System.out.println("access_token:"+access_token);
			System.out.println("��ȡaccess_token�ɹ�!");
		}else{
			System.out.println("��ȡaccess_tokenʧ��!");
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
 * �ϴ���ý�壬type�����ͣ�filePath��·��
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
		//���ùؼ�ֵ
		con.setRequestMethod("POST");
		con.setDoInput(true);
		con.setDoOutput(true);
		con.setUseCaches(false);//post��ʽ����ʹ�û���
		//��������ͷ��Ϣ
		con.setRequestProperty("Connection", "Keep-Alive");
		con.setRequestProperty("Charset", "UTF-8");
		//���ñ߽�
		String BOUNDARY="------------"+System.currentTimeMillis();
		con.setRequestProperty("Content-Type", "multipart/form-data;boundary="+BOUNDARY);
		//����������Ϣ
		//��һ���֣�
		StringBuilder sb=new StringBuilder();
		sb.append("--");////////�����������
		sb.append(BOUNDARY);
		sb.append("\r\n");
		sb.append("Content-Disposition:form-data;name=\"file\";filename=\""+file.getName()+"\"r\n");
		sb.append("Content-Type:application/octer-stream\r\n\r\n");
		byte[] head=sb.toString().getBytes("utf-8");
		//��������
		OutputStream out=new DataOutputStream(con.getOutputStream());
		out.write(head);
		//�ļ����Ĳ���
		DataInputStream in=new DataInputStream(new FileInputStream(file));
		int bytes=0;
		byte[] bufferOut=new byte[1024];
		while((bytes=in.read(bufferOut))!=-1){
			out.write(bufferOut,0,bytes);
		}
		in.close();
		//��β����
		byte[] foot=("\r\n--"+BOUNDARY+"--\r\n").getBytes("utf-8");//����������ݷָ���
		out.write(foot);
		out.flush();
		out.close();
		//����BufferedReader����������ȡURL����Ӧ
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
			System.out.println("��ý���ϴ��ɹ���media_id="+jsonObject.getString("media_id"));
		}else{
			System.out.println("��ý���ϴ��ɹ�");
		}
	}catch(MalformedURLException e){
		e.printStackTrace();
	}catch(IOException e){
		e.printStackTrace();
	}
}
/**
 * ��ý������
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
		 * ���ùؼ���
		 */
		con.setRequestMethod("GET");//��get��ʽ�����ļ�
		con.setDoInput(true);
		con.setDoOutput(true);
		con.setRequestProperty("Content-Type", "application/x-www-form-url encoded");
		//��ȡ�����
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
