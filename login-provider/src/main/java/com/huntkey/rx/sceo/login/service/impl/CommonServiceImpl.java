package com.huntkey.rx.sceo.login.service.impl;

import com.huntkey.rx.commons.utils.rest.Result;
import com.huntkey.rx.commons.utils.string.StringUtil;
import com.huntkey.rx.sceo.common.constant.MsgStatusCode;
import com.huntkey.rx.sceo.common.entity.EmailVo;
import com.huntkey.rx.sceo.common.utils.EncryptUtil;
import com.huntkey.rx.sceo.login.service.CommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

/**
 * Created by lulx on 2017/12/25 0025 下午 14:46
 */
@Service
public class CommonServiceImpl implements CommonService {

    private static Logger logger = LoggerFactory.getLogger(CommonServiceImpl.class);

    @Value("${msg.url}")
    String msgUrl;

    @Value("${msg.username}")
    String userName;

    @Value("${msg.password}")
    String passWord;

    @Value("${msg.visa}")
    String visa;

    @Value("${email.host}")
    String host;

    @Value("${email.contenttype}")
    String contentType;

    @Override
    public Result sendMsg(String tel, String content) {
        Result result = new Result();
        if (StringUtil.isNullOrEmpty(tel) || StringUtil.isNullOrEmpty(content)) {
            result.setErrMsg("手机号和短信内容不能为空");
            result.setRetCode(Result.RECODE_ERROR);
            return result;
        }
        String smlgId = UUID.randomUUID().toString();
        String msgResult = "";
        StringBuffer sb = new StringBuffer();
        BufferedReader in = null;
        URLConnection conn = null;
        OutputStreamWriter out = null;

        try {
            sb.append("username=").append(userName)
                    //"cc35670dc059b45f9dab1ccecfaca683"
                    .append("&password=").append(EncryptUtil.msgPassword(userName, passWord))
                    .append("&mobile=").append(tel)
                    .append("&content=").append(URLEncoder.encode(content+visa, "UTF-8"));
                    //短信发送时间 为空时立即发送
                    //.append("&dstime=").append("")
                    //用户自定义扩展（选填）需要和后台人员确认权限
                    //.append("&ext=").append("")
                    //发送消息的任务id 为空时平台端自动生成
                    //.append("&msgid=").append(smlgId)
                    //提交消息的编码格式 为空时默认utf-8
                    //.append("&msgfmt=").append("");
            logger.info("msgUrl : {} ;短信内容 ： {}", msgUrl, sb.toString());
            URL realUrl = new URL(msgUrl);
            conn = realUrl.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            System.setProperty("http.proxyHost", "127.0.0.1");
            System.setProperty("http.proxyPort", "3128");
            out = new OutputStreamWriter(conn.getOutputStream(), "UTF8");
            out.write(sb.toString());
            out.flush();
            in = new BufferedReader(new InputStreamReader(
                    conn.getInputStream(), "UTF8"));
            String line = "";
            while ((line = in.readLine()) != null) {
                msgResult += line;
            }
            logger.info("短信发送结果： {}", msgResult);
            MsgStatusCode msgStatusCode = MsgStatusCode.getMsgStatusCode(Long.valueOf(msgResult));
            result.setRetCode(msgStatusCode.getResultCode());
            result.setErrMsg(msgStatusCode.getErrorMsg());
        } catch (IOException e) {
            logger.error("sendMsg error : " + e.getMessage(), e);
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg(MsgStatusCode.FAILED.getErrorMsg());
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                logger.error("sendMsg error : " + e.getMessage(), e);
                throw new RuntimeException("sendMsg error : " + e.getMessage());
            }
        }
        return result;
    }

    @Override
    public Result sendEmail(EmailVo emailVo) {
        Result result = new Result();
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.auth", "false");
        Session mailSession = Session.getInstance(props, null);

        MimeMessage message = new MimeMessage(mailSession);
        try {
            InternetAddress fromAddress = new InternetAddress("sceo@huntkey.net");
            message.setFrom(fromAddress);
            String[] strings = new String[emailVo.getMailRecipient().size()];
            //收件人
            InternetAddress[] toAddress=new InternetAddress[emailVo.getMailRecipient().size()];
            for(int i=0;i<emailVo.getMailRecipient().size();i++){
                toAddress[i]=new InternetAddress(emailVo.getMailRecipient().get(i));
            }
            message.setRecipients(MimeMessage.RecipientType.TO, toAddress);
            //抄送人
            if(!StringUtil.isNullOrEmpty(emailVo.getMailCopyRecipient()) && emailVo.getMailCopyRecipient().size() > 0){
                InternetAddress[] ccAddress=new InternetAddress[emailVo.getMailCopyRecipient().size()];
                for(int i=0;i<emailVo.getMailCopyRecipient().size();i++){
                    ccAddress[i]=new InternetAddress(emailVo.getMailCopyRecipient().get(i));
                }
                message.setRecipients(MimeMessage.RecipientType.CC, ccAddress);
            }
            Date sendTime= Calendar.getInstance().getTime();
            message.setSentDate(sendTime);
            message.setSubject(emailVo.getMailSubject());
            message.setContent(emailVo.getMailContent(),contentType);
            // 第三步：发送消息
            Transport transport = mailSession.getTransport("smtp");
            transport.connect(host,null);
            transport.send(message);
        } catch (MessagingException e) {
            logger.error("sendEmail error : " + e.getMessage(), e);
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg("sendEmail error : " + e.getMessage());
        }
        return result;
    }
}
