package vip.sujianfeng.weixin.utils;

import vip.sujianfeng.utils.comm.StringBuilderEx;
import vip.sujianfeng.weixin.model.TemplateData;
import vip.sujianfeng.weixin.model.WxTemplate;
import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信消息通知
 * Created by sujianfeng on 2016/7/29.
 */
public class WeixinNoticeUtils {


    /**
     * 发送模板消息
     * @param t
     * @return
     */
    public static Map sendTempleteMessage(String access_token, WxTemplate t){
        String s = JSON.toJSONString(t);
        //String access_token = WeixinTokenHandler.require_ACCESS_TOKEN();
        return HttpClientUtil.httpRequest("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + access_token, "POST", s);
    }



    /**
     * 新评价提醒通知
     * @param openId 商户创建者的ID
     * @param firstMsg
     * @param userName 评论者名称
     * @param time
     * @param remarkMsg
     * @param shopId
     */
    public static void sendNewCommentNotice(String access_token, String openId, String firstMsg, String userName, String time, String remarkMsg, String shopId){
        System.out.println("===============sendNewCommentNotice=============begin");
        WxTemplate t = new WxTemplate();
        t.setTouser(openId);
        t.setTopcolor("#173177");
        t.setTemplate_id("4sNL8Ga1S3Ssp9-LZeBN-fP2kej7IHquF5l7XGPDA_s");

        StringBuilderEx url = new StringBuilderEx();
        url.append(String.format("http://community.linlishe.cn/xiaoQuShop/shopView.html?shopId=%s", shopId));
        t.setUrl(url.toString());

        Map<String, TemplateData> m = new HashMap<String,TemplateData>();
        TemplateData first = new TemplateData();
        first.setColor("#173177");
        first.setValue(firstMsg);
        m.put("first", first);

        TemplateData keynote1 = new TemplateData();
        keynote1.setColor("#173177");
        keynote1.setValue(userName);
        m.put("keyword1", keynote1);

        TemplateData keynote2 = new TemplateData();
        keynote2.setColor("#173177");
        keynote2.setValue(time);
        m.put("keyword2", keynote2);

        TemplateData remark = new TemplateData();
        remark.setColor("#173177");
        remark.setValue(remarkMsg);
        m.put("remark", remark);

        t.setData(m);
        Map map = sendTempleteMessage(access_token, t);
        System.out.println(JSON.toJSONString(map));
        System.out.println("===============sendNewCommentNotice=============end");
    }

    /**
     * 新商户申请通知
     * @param openId  小区管理员的openId
     * @param firstMsg
     * @param userName 商户申请者名称
     * @param time
     * @param remarkMsg
     * @param shopName
     */
    public static void sendNewShopNotice(String access_token, String openId, String firstMsg, String userName, String time, String remarkMsg, String shopName){
        System.out.println("===============sendNewShopNotice=============begin");
        WxTemplate t = new WxTemplate();
        t.setTouser(openId);
        t.setTopcolor("#173177");
        t.setTemplate_id("ntqeqdom-PP0_Ns5noF-dLntFDZP0p6UHHIr6tPuCDM");

        StringBuilderEx url = new StringBuilderEx();
        url.append(String.format("http://community.linlishe.cn/XiaoQuShopMng/index.html"));
        t.setUrl(url.toString());

        Map<String, TemplateData> m = new HashMap<String,TemplateData>();
        TemplateData first = new TemplateData();
        first.setColor("#173177");
        first.setValue(firstMsg);
        m.put("first", first);

        TemplateData keynote1 = new TemplateData();
        keynote1.setColor("#173177");
        keynote1.setValue(userName);
        m.put("keyword1", keynote1);

        TemplateData keynote2 = new TemplateData();
        keynote2.setColor("#173177");
        keynote2.setValue(shopName);
        m.put("keyword2", keynote2);

        TemplateData keynote3 = new TemplateData();
        keynote3.setColor("#173177");
        keynote3.setValue(time);
        m.put("keyword3", keynote3);

        TemplateData remark = new TemplateData();
        remark.setColor("#173177");
        remark.setValue(remarkMsg);
        m.put("remark", remark);

        t.setData(m);
        Map map = sendTempleteMessage(access_token, t);
        System.out.println(JSON.toJSONString(map));
        System.out.println("===============sendNewShopNotice=============end");
    }

    /**
     * 商户审核结果通知
     * @param openId 申请的商户创建者ID
     * @param firstMsg
     * @param verificationMsg
     * @param time
     * @param remarkMsg
     */
    public static void sendShopVerificationNotice(String access_token, String openId, String firstMsg, String verificationMsg, String time, String remarkMsg, String shopId){
        System.out.println("===============sendShopVerificationNotice=============begin");
        WxTemplate t = new WxTemplate();
        t.setTouser(openId);
        t.setTopcolor("#173177");
        t.setTemplate_id("bvbWuVGuRN3igteo3-TIftbwNG4WOLHrU2-fIJDX2HQ");

        StringBuilderEx url = new StringBuilderEx();
        url.append(String.format("http://community.linlishe.cn/xiaoQuShop/shopView.html?shopId=%s", shopId));
        t.setUrl(url.toString());

        Map<String, TemplateData> m = new HashMap<String,TemplateData>();
        TemplateData first = new TemplateData();
        first.setColor("#173177");
        first.setValue(firstMsg);
        m.put("first", first);

        TemplateData keynote1 = new TemplateData();
        keynote1.setColor("#173177");
        keynote1.setValue(verificationMsg);
        m.put("keyword1", keynote1);

        TemplateData keynote2 = new TemplateData();
        keynote2.setColor("#173177");
        keynote2.setValue(time);
        m.put("keyword2", keynote2);

        TemplateData remark = new TemplateData();
        remark.setColor("#173177");
        remark.setValue(remarkMsg);
        m.put("remark", remark);

        t.setData(m);
        Map map = sendTempleteMessage(access_token, t);
        System.out.println(JSON.toJSONString(map));
        System.out.println("===============sendShopVerificationNotice=============end");
    }

    /**
     * 投诉举报通知
     * @param complainObjectName
     * @param complainFromUserName
     * @param time
     * @param complainRemark
     * @param complainId
     */
    public static void sendComplainNoticeToSuGong(String access_token, String complainObjectName, String complainFromUserName, String time, String complainRemark, String complainId){
        System.out.println("===============sendComplainNotice=============begin");
        WxTemplate t = new WxTemplate();
        t.setTouser("oT4F0sxw7GEt3PrUIAm1bBleTNLE");
        t.setTopcolor("#173177");
        t.setTemplate_id("O2rU4apRuYgiI83VZ68F4lwkBylZsWR4FfoAqHtbOqA");

        StringBuilderEx url = new StringBuilderEx();
        url.append(String.format("http://community.linlishe.cn/complainMng/index.html"));
        t.setUrl(url.toString());

        Map<String, TemplateData> m = new HashMap<String,TemplateData>();
        TemplateData first = new TemplateData();
        first.setColor("#173177");
        String firstMsg ="";
        firstMsg = firstMsg + "投诉对象：" + complainObjectName;
        first.setValue(firstMsg.toString());
        m.put("first", first);

        TemplateData keynote1 = new TemplateData();
        keynote1.setColor("#173177");
        keynote1.setValue(complainFromUserName);
        m.put("keyword1", keynote1);

        TemplateData keynote2 = new TemplateData();
        keynote2.setColor("#173177");
        keynote2.setValue(time);
        m.put("keyword2", keynote2);

        TemplateData remark = new TemplateData();
        remark.setColor("#173177");
        String remarkMsg = "";
        remarkMsg = remarkMsg + "举报原因：" + complainRemark;
        remark.setValue(remarkMsg);
        m.put("remark", remark);

        t.setData(m);
        Map map = sendTempleteMessage(access_token, t);
        System.out.println(JSON.toJSONString(map));
        System.out.println("===============sendComplainNotice=============end");
    }

    public static void sendComplainNoticeToHanYan(String access_token,  String complainObjectName, String complainFromUserName, String time, String complainRemark, String complainId){
        System.out.println("===============sendComplainNotice=============begin");
        WxTemplate t = new WxTemplate();
        t.setTouser("oT4F0s99rmbE-QS-7pQfLIB60yys");
        t.setTopcolor("#173177");
        t.setTemplate_id("O2rU4apRuYgiI83VZ68F4lwkBylZsWR4FfoAqHtbOqA");

        StringBuilderEx url = new StringBuilderEx();
        url.append(String.format("http://community.linlishe.cn/complainMng/index.html"));
        t.setUrl(url.toString());

        Map<String, TemplateData> m = new HashMap<String,TemplateData>();
        TemplateData first = new TemplateData();
        first.setColor("#173177");
        String firstMsg ="";
        firstMsg = firstMsg + "投诉对象：" + complainObjectName;
        first.setValue(firstMsg.toString());
        m.put("first", first);

        TemplateData keynote1 = new TemplateData();
        keynote1.setColor("#173177");
        keynote1.setValue(complainFromUserName);
        m.put("keyword1", keynote1);

        TemplateData keynote2 = new TemplateData();
        keynote2.setColor("#173177");
        keynote2.setValue(time);
        m.put("keyword2", keynote2);

        TemplateData remark = new TemplateData();
        remark.setColor("#173177");
        String remarkMsg = "";
        remarkMsg = remarkMsg + "举报原因：" + complainRemark;
        remark.setValue(remarkMsg);
        m.put("remark", remark);

        t.setData(m);
        Map map = sendTempleteMessage(access_token, t);
        System.out.println(JSON.toJSONString(map));
        System.out.println("===============sendComplainNotice=============end");
    }

}
