package com.wx.demo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * @author KeXin
 * 发送被动响应消息
 *
 */
/**
 * 关于重试的消息拍重时，有msgid的消息使用msgid排重，事件类型消息使用FromUserName+CreateTime排重
 * 加入服务器不能保证5s之内处理并回复，可以直接回复空字符串，微信服务器不会对此做任何处理，并且不会发起重试，可以使用客服消息进行异步回复
 */
public class WeixinReverDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JSONObject text=new JSONObject();
		text.put("Content", "测试文本回复");
		System.out.println(createRevertText(text));
		//String resposeStr=createRevertText(text);
		//最后接入微信入口用response写入到微信代理就行了
		//OutputStream os=response.getOutputStream();
		//os.write(responseStr.getBytes("UTF-8"));
	}
	static private String ToUserName="";
	static private String FromUserName="";
	static private String CreateTime=String.valueOf(System.currentTimeMillis());
	/**
	 * 构建回复文本，回复的消息内容，长度不超过2048字节
	 * @param JSONObject
	 * @return String
	 */
	public static String createRevertText(JSONObject jsonObject) {
		StringBuffer revert=new StringBuffer();
		revert.append("<xml>");
		revert.append("<ToUserName><![CDATA["+ToUserName+"]]></ToUserName>");
		revert.append("<FromUserName><![CDATA["+FromUserName+"]]></FromUserName>");
		revert.append("<CreateTime>"+CreateTime+"</CreateTime>");
		revert.append("<MsgType><![CDATA[text]]></MsgType>");
		revert.append("<Content><![CDATA["+jsonObject.get("Content")+"]]></Content>");
		revert.append("<FuncFlag>0</FuncFlag>");
		revert.append("</xml>");
		return revert.toString();
	}
	/**
	 * 构建回复音乐
	 * @return String
	 * @param JSONObject
	 */
	public static String createRevertMusic(JSONObject jsonObject){
		StringBuffer revert=new StringBuffer();
		JSONObject music=(JSONObject)jsonObject.get("Music");
		revert.append("<xml>");
		revert.append("<ToUserName><![CDATA["+ToUserName+"]]></ToUserName>");
		revert.append("<FromUserName><![CDATA["+FromUserName+"]]></FromUserName>");
		revert.append("<CreateTime>"+CreateTime+"</CreateTime>");
		revert.append("<MsgType><![CDATA[music]]></MsgType>");
		revert.append("<Music>");
		revert.append("<Title><![CDATA["+music.get("Title")+"]]></Title>");
		revert.append("<Description><![CDATA["+music.get("Description")+"]]></Description>");
		revert.append("<MusicUrl><![CDATA["+music.getString("MusicUrl")+"]]></MusicUrl>");
		revert.append("<HQMusicUrl><![CDATA["+music.getString("HQMusicUrl")+"]]></HQMusicUrl>");
		revert.append("</Music>");
		revert.append("<FuncFlag>0</FuncFlag>");
		revert.append("</xml>");
		return revert.toString();
	}
	/**
	 * 构建回复图文 限为10条之内
	 * @return String
	 * @param JSONObject
	 */
	public static String createRevertNews(JSONObject jsonObject){
		StringBuffer revert=new StringBuffer();
		JSONArray jsonArray=(JSONArray)jsonObject.get("Articles");
		revert.append("<xml>");
		revert.append("<ToUserName><![CDATA["+ToUserName+"]]></ToUserName>");
		revert.append("<FromUserName><![CDATA["+FromUserName+"]]></FromUserName>");
		revert.append("<CreateTime>"+CreateTime+"</CreateTime>");
		revert.append("<MsgType><![CDATA[news]]></MsgType>");
		revert.append("<ArticleCount>"+jsonArray.size()+"</ArticleCount>");
		revert.append("<Articles>");
		for(int i=0;i<jsonArray.size();i++){
			JSONObject item=(JSONObject)jsonArray.get(i);
			revert.append("<item>");
			revert.append("<Title>+<![CDATA["+item.get("Title")+"]]></Title>");
			revert.append("<Description><![CDATA["+item.get("Description")+"]]></Description>");
			revert.append("<PicUrl><![CDATA["+item.get("PicUrl")+"]]></PicUrl>");
			revert.append("<Url><![CDATA["+item.get("Url")+"]]></Url>");
			revert.append("</item>");
		}
		revert.append("</Articles>");
		revert.append("<FuncFlag>1</FuncFlag>)");
		revert.append("</xml>");
		return revert.toString();
	}
	/**
	 * 构建回复图片
	 * @return String
	 * @param JSONObject
	 */
	public static String createRevertImage(JSONObject jsonObject){
		StringBuffer revert=new StringBuffer();
		revert.append("<xml>");
		revert.append("<ToUserName><![CDATA["+ToUserName+"]]></ToUserName>");
		revert.append("<FromUserName><![CDATA["+FromUserName+"]]></FromUserName>");
		revert.append("<CreateTime>"+CreateTime+"</CreateTime>");
		revert.append("<MsgType><![CDATA[image]]></MsgType>");
		revert.append("<Image><MediaId><![CDATA[").append(jsonObject.getString("MediaId")).append("]]></MediaId></Image>");
		revert.append("<FuncFlag>0</FuncFlag>)");
		revert.append("</xml>");
		return revert.toString();
	}
	/**
	 * 构建回复语音
	 * @return String
	 * @param JSONObject
	 */
	public static String createRevertVoice(JSONObject jsonObject){
		StringBuffer revert=new StringBuffer();
		revert.append("<xml>");
		revert.append("<ToUserName><![CDATA["+ToUserName+"]]></ToUserName>");
		revert.append("<FromUserName><![CDATA["+FromUserName+"]]></FromUserName>");
		revert.append("<CreateTime>"+CreateTime+"</CreateTime>");
		revert.append("<MsgType><![CDATA[voice]]></MsgType>");
		revert.append("<Voice><MediaId><![CDATA[").append(jsonObject.getString("MediaId")).append("]]></MediaId></Voice>");
		revert.append("<FuncFlag>0</FuncFlag>)");
		revert.append("</xml>");
		return revert.toString();
	}
	/**
	 * 构建回复视频
	 * @return String
	 * @param JSONObject
	 */
	public static String createRevertVideo(JSONObject jsonObject){
		StringBuffer revert=new StringBuffer();
		revert.append("<xml>");
		revert.append("ToUserName><![CDATA[").append(jsonObject.get("ToUserName")).append("]]></ToUserName>");
		revert.append("FromUserName><![CDATA[").append(jsonObject.get("FromUserName")).append("]]></FromUserName>");
		revert.append("<CreateTime>").append(jsonObject.getString("CreateTime")).append("]]></CreateTime>");
		revert.append("<MsgType><![CDATA[video]]></MsgType>");
		revert.append("<Video>");
		revert.append("<MediaId><![CDATA[").append(jsonObject.getString("MediaId")).append("]]></MediaId>");
		revert.append("<ThumbMediaId><![CDATA[").append("]]></ThumbMediaId>");
		revert.append("Video");
		revert.append("<FuncFlag>0</FuncFlag>)");
		revert.append("</xml>");
		return revert.toString();
	}
}
