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
 * �Զ���˵�
 * 
 * �������һ���˵����ĸ����֣���ÿ��һ���˵����5�������˵���7�����֣�
 *click:
 *�û����click���Ͱ�ť��΢�ŷ�������ͨ����Ϣ�ӿ�������Ϣ����Ϊevent�Ľṹ��������
 *view��
 *�û����view���Ͱ�ť��΢�ſͻ��˻�򿪿������ڰ�ť����д��urlֵ����ҳ���ӣ����ﵽ����ҳ��Ŀ��
 *������ҳ��Ȩ��ȡ�û�������Ϣ�ӿڽ�ϣ�����û��ĵ��������Ϣ
 *
 */
public class WeixinMenuDemo {
	private Logger log=Logger.getLogger(this.getClass().getName());
/**
 * 1.�����Զ���˵����ַ���
 * 2.��ȡaccess_token
 * 3.post�ύ�ַ���
 */
	/*
	 * �������ɲ˵���json�ַ���
	 */
	public String getMenuString(){
		//���������˵��ڵ�
		JSONArray sub_button=new JSONArray();
		//�����˵�������
		JSONObject button1=new JSONObject();
		button1.put("type", "view");
		button1.put("name", "����");
		button1.put("url", "http://www.soso.com/");
		JSONObject button2=new JSONObject();
		button2.put("type", "click");
		button2.put("name", "��һ������");
		//Eventkey���壬���û������ťʱ΢��ƽ̨���΢�Žӿ�����xml���ݰ���<EventKey><![CDATA[V1001_GOOD]]></EventKey>
		button2.put("key", "V1001_GOOD");
		//��ӵ������˵�
		sub_button.add(button1);
		sub_button.add(button2);
		//������һ��һ���˵�
		JSONObject menu1=new JSONObject();
		//�����Ӳ˵�
		menu1.put("name", "���ո���");
		menu1.put("sub_button", sub_button);
		//�����ڶ���һ���˵�
		JSONObject menu2=new JSONObject();
		//�����Ӳ˵�
		menu2.put("name", "���ʻ");
		menu2.put("sub_button", sub_button);
		//����������һ���˵�
		JSONObject menu3=new JSONObject();
		menu3.put("name", "���ּ��");
		menu3.put("sub__button", sub_button);
		//�˵�����
		JSONArray array=new JSONArray();
		array.add(menu1);
		array.add(menu2);
		array.add(menu3);
		//�������ڵ�
		JSONObject root=new JSONObject();
		root.put("button", array);
		System.out.println("button json:"+root.toString());
		return root.toString();
	}
	/**
	 * ҵ��ת�����
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
				//�˵�click�¼�
				String eventkey=jsonObject.getString("EventKey");
				if("V1001_GOOD".equals(eventkey)){
					//V1001_GOOD����һ�����ǵ�eventkey
					jsonObject.put("Content", "��ӭʹ����һ�����ǵ�click��ť");
				}
			}
			//����xml
			responseStr=new WeixinReverDemo().createRevertText(jsonObject);
			log.info("responseStr:"+responseStr);
			ServletOutputStream os=response.getOutputStream();
			os.write(responseStr.getBytes("UTF-8"));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * ��ȡaccess_token
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
	 * ����΢���Զ���˵�
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
				System.out.println("�Զ���˵������ɹ���");
			}else{
				System.out.println("�Զ���˵�����ʧ�ܣ�");
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
