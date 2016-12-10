package com.wx.demo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sun.rmi.runtime.Log;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;

/**
 * 
 * @author KeXin
 * 
 * 自定义菜单
 * 
 * 最多三个一级菜单（四个汉字），每个一级菜单最多5个二级菜单（7个汉字）
 *click:
 *用户点击click类型按钮后，微信服务器会通过消息接口推送消息类型为event的结构给开发者
 *view：
 *用户点击view类型按钮后，微信客户端会打开开发者在按钮中填写的url值（网页链接），达到打开网页的目的
 *可与网页授权获取用户基本信息接口结合，获得用户的登入个人信息
 *
 */
public class WeixinMenuDemo {
	private Logger log=Logger.getLogger(this.getClass().getName());
/**
 * 1.创建自定义菜单的字符串
 * 2.获取access_token
 * 3.post提交字符串
 */
	/*
	 * 创建生成菜单的json字符串
	 */
	public String getMenuString(){
		//创建二级菜单节点
		JSONArray sub_button=new JSONArray();
		//二级菜单的内容
		JSONObject button1=new JSONObject();
		button1.put("type", "view");
		button1.put("name", "搜索");
		button1.put("url", "http://www.soso.com/");
		JSONObject button2=new JSONObject();
		button2.put("type", "click");
		button2.put("name", "赞一下我们");
		//Eventkey定义，当用户点击按钮时微信平台会给微信接口推送xml数据包：<EventKey><![CDATA[V1001_GOOD]]></EventKey>
		button2.put("key", "V1001_GOOD");
		//添加到二级菜单
		sub_button.add(button1);
		sub_button.add(button2);
		//创建第一个一级菜单
		JSONObject menu1=new JSONObject();
		//创建子菜单
		menu1.put("name", "今日歌曲");
		menu1.put("sub_button", sub_button);
		//创建第二个一级菜单
		JSONObject menu2=new JSONObject();
		//创建子菜单
		menu2.put("name", "精彩活动");
		menu2.put("sub_button", sub_button);
		//创建第三个一级菜单
		JSONObject menu3=new JSONObject();
		menu3.put("name", "歌手简介");
		menu3.put("sub__button", sub_button);
		//菜单数组
		JSONArray array=new JSONArray();
		array.add(menu1);
		array.add(menu2);
		array.add(menu3);
		//创建根节点
		JSONObject root=new JSONObject();
		root.put("button", array);
		System.out.println("button json:"+root.toString());
		return root.toString();
	}
	/**
	 * 业务转发组件
	 * @param requestStr
	 * @param request
	 * @param response
	 */
	public void manageMessage(String requestStr,HttpServletRequest request,HttpServletResponse response)
	throws ServletException,IOException{
		String responseStr;
		try{
			XMLSerializer xmlSerializer=new XMLSerializer();
			JSONObject jsonObject=(JSONObject)xmlSerializer.read(requestStr);
			String event=jsonObject.getString("Event");
			String msgtype=jsonObject.getString("MsgType");
			if("CLICK".equals(event)&&"event".equals(msgtype)){
				//菜单click事件
				String eventkey=jsonObject.getString("EventKey");
				if("V1001_GOOD".equals(eventkey)){
					//V1001_GOOD是赞一下我们的eventkey
					jsonObject.put("Content", "欢迎使用赞一下我们的click按钮");
				}
			}
			//创建xml
			responseStr=new WeixinReverDemo().createRevertText(jsonObject);
			log.info("responseStr:"+responseStr);
			ServletOutputStream os=response.getOutputStream();
			os.write(responseStr.getBytes("UTF-8"));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
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
	 * 创建微信自定义菜单
	 */
	public void initWeixinMenu(){
		String menustr=getMenuString();
		String access_token=getAccess_token();
		String action="http://api.weixin.qq.com/cgi-bin/menu/create?access_token="+access_token;
		try{
			URL url=new URL(action);
			HttpURLConnection http=(HttpURLConnection)url.openConnection();
			http.setRequestMethod("POST");
			http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			http.setDoInput(true);
			http.setDoOutput(true);
			System.setProperty("sun.net.client.defaultConnectTimeout", "30000");
			System.setProperty("sun.net.client.defaultReadTimeout", "30000");
			http.connect();
			OutputStream os=http.getOutputStream();
			os.write(menustr.getBytes("UTF-8"));
			os.flush();
			os.close();
			InputStream is=http.getInputStream();
			int size=is.available();
			byte[] jsonBytes=new byte[size];
			is.read(jsonBytes);
			String message=new String(jsonBytes,"UTF-8");
			System.out.println("resp:"+message);
			JSONObject json=JSONObject.fromObject(message);
			if("0".equals(json.getString("errcode"))){
				System.out.println("自定义菜单创建成功！");
			}else{
				System.out.println("自定义菜单创建失败！");
			}
		}catch(MalformedURLException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new WeixinMenuDemo().initWeixinMenu();
	}

}
