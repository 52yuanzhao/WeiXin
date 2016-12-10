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
 * ÿ���û�����һ�����ںŵ�openid��Ψһ�ģ�ͨ�����ӿڻ�ȡ�û�������Ϣ
 * �����ǳƣ�ͷ���Ա����ڳ��к͹�עʱ��
 *
 */
public class WeixinGetUserInfoDemo {
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
	 * ����openid��ȡ�û��Ļ�����Ϣ
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
		new WeixinGetUserInfoDemo().getUserInfo("###################");//�滻���Լ���openid
	}
	
}
