package com.wx.servlet;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;

/**
 * 
 * @author KeXin
 * ���ӷ�����
 *
 */

public class WeixinServlet extends HttpServlet{
	private Logger log=Logger.getLogger(this.getClass().getName());
	private static final long serialVersionUID=1L;
	private String Token;
	private String echostr;
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			connect(request, response);
	}
	//����������Ч��֤
	private void connect(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
			log.info("RemoteAddr:"+request.getRemoteAddr());
			log.info("WueryString:"+request.getQueryString());
			if(!accessing(request,response)){
				log.info("����������ʧ��......");
				return;
			}
			String echostr=getEchostr();
			if(echostr!=null&&!"".equals(echostr)){
				log.info("��������Ч......");
				response.getWriter().print(echostr);//����໥��֤
			}
	}
	//��������΢�Ź���ƽ̨����֤
	private boolean accessing(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException{
			String signature=request.getParameter("signature");
			String timestamp=request.getParameter("timestamp");
			String nonce=request.getParameter("nonce");
			String echostr=request.getParameter("echostr");
			if(isEmpty(signature)){
				return false;
			}
			if(isEmpty(timestamp)){
				return false;
			}
			if(isEmpty(nonce)){
				return false;
			}
			if(isEmpty(echostr)){
				return false;
			}
			String[] ArrTmp={
					Token,timestamp,nonce,echostr
			};
			Arrays.sort(ArrTmp);
			StringBuffer sb=new StringBuffer();
			for(int i=0;i<ArrTmp.length;i++){
				sb.append(ArrTmp[i]);
			}
			String pwd=Encrypt(sb.toString());
			log.info("signature:"+signature+"timetamp:"+timestamp+"nonce:"+nonce+"echostr:"+echostr);
			if(trim(pwd).equals(trim(signature))){
				this.echostr=echostr;
				return true;
			}else{
				return false;
			}
	}
	
	private String Encrypt(String strSrc) {
			MessageDigest md=null;
			String strDes=null;
			byte[] bt=strSrc.getBytes();
			try{
				md=MessageDigest.getInstance("SHA-1");
				md.update(bt);
				strDes=bytes2Hex(md.digest());
			}catch(NoSuchAlgorithmException e){
				System.out.println("Invalid algorithm.");
				return null;
			}
			return strDes;
	}
	public String bytes2Hex(byte[] bts) {
		String des="";
		String tmp=null;
		for(int i=0;i<bts.length;i++){
			tmp=(Integer.toHexString(bts[i]&0xFF));
			if(tmp.length()==1){
				des+="0";
			}
			des+=tmp;
		}
		return des;
	}
	public String getEchostr(){
		return echostr;
	}
	//�ж��ַ����Ƿ�Ϊ��
	private boolean isEmpty(String str) {
			return null==str||"".equals(str)?true:false;
	}
	private String trim(String str){
		return null!=str?str.trim():str;
	}
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			message(request, response);
	}
	private void message(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
			InputStream is=request.getInputStream();
			//ȡHTTP�������ĳ���
			int size=request.getContentLength();
			//���ڻ���ÿ�ζ�ȡ������
			byte[] buffer=new byte[size];
			//���ڴ�Ž��������
			byte[] xmldataByte=new byte[size];
			int count=0;
			int rbyte=0;
			//ѭ����ȡ
			while(count<size){
				//ÿ��ʵ�ʶ�ȡ�ĳ��ȴ���rbyte��
				rbyte=is.read(buffer);
				for(int i=0;i<rbyte;i++){
					xmldataByte[count+i]=buffer[i];
				}
				count+=rbyte;
			}
			is.close();
			String requestStr=new String(xmldataByte,"UTF-8");
			try{
				manageMessage(requestStr,request,response);
			}catch(Exception e){
				e.printStackTrace();
			}
	}
	//ҵ��ת�����
	private void manageMessage(String requestStr, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException{
			String responseStr;
			XMLSerializer xmlSerializer=new XMLSerializer();
			JSONObject jsonObject=(JSONObject)xmlSerializer.read(requestStr);
			jsonObject.put("content", "�뿪��ҵ���߼�");
			try{
				responseStr=createRevertText(jsonObject);//����XML
				log.info("responseStr:"+responseStr);
				OutputStream os=response.getOutputStream();
				os.write(responseStr.getBytes("UTF-8"));
			}catch(Exception e){
				e.printStackTrace();
			}
	}
	//����XML
	private String createRevertText(JSONObject jsonObject) {
		StringBuffer revert=new StringBuffer();
		revert.append("<xml>");
		revert.append("<ToUserName><![CDATA["+jsonObject.get("ToUserName")+"]]></ToUserName>");
		revert.append("<FromUserName><![CDATA["+jsonObject.get("FromUserName")+"]]></FromUserName>");
		revert.append("<CreateTime>"+jsonObject.get("CreateTime")+"</CreateTime>");
		revert.append("<MsgType><!CDATA[text]]></MsgType>");
		revert.append("<Content><!CDATA["+jsonObject.get("Content")+"]]></Content>");
		revert.append("<FuncFlag>0</FuncFlag>");
		revert.append("</xml>");
		return revert.toString();
	}
//	@Override
//	public void init() throws ServletException {
//		Token="qianxunweixin";
//	}		
//	
	
}
