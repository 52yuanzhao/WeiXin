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
 * ͨ�����ӿڻ�ȡ��ע���б� ��һ��Openid���
 * һ�������ȡ10000����ע�ߵ�openid
 * ��ͨ����дnext_openid��ֵ���ﵽ�����ȡ�ķ�ʽ��ø����ע�ߵ�openid
 * ������Ծ����ڵ��ýӿ�ʱ�����ϴε��õõ��ķ����е�next_openidֵ��Ϊ��һ�ε���next_openidֵ
 *
 */
public class WeixinGetUsers {
	/**
	 * ��ȡAccess_token
	 * @return
	 */
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
	/**
	 * ��ȡ��ǰ��ע�ߵ�����openid
	 * @return JSONArray,openid�����ʽ����OPENID1��,"OPENID2",������
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
