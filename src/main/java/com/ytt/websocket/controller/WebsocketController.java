package com.ytt.websocket.controller;

import com.oracle.tools.packager.Log;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Date;
/**
 * @author yantaotao
 * @date 2018/6/29
 */
@RestController
public class WebsocketController {


    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;



//    //用请求的方式模拟主动推送消息
//    @Scheduled(fixedRate = 1000)
//    //@PostMapping("/notice/{id}")
//    @SendToUser("/queue/callback")
//    public String callback() {
//        //这里定义了订阅消息的路径是"/queue/notice"，客户端请求的路径则为："/user/queue/notice"
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        simpMessagingTemplate.convertAndSend("/queue/callback", df.format(new Date()));
//        return "已发送";
//    }

    //用请求的方式模拟主动推送消息
    @PostMapping("/notice/{id}")
    @SendToUser("/queue/callback")
    public String notice(@PathVariable("id")String id,@RequestParam("data") String data) {
        //这里定义了订阅消息的路径是"/queue/notice"，客户端请求的路径则为："/user/queue/notice"
        //DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        simpMessagingTemplate.convertAndSend("/queue/callback", data);
        return "已发送";
    }

    @MessageMapping("/oncall")
    @SendToUser("/queue/oncall")
    public String oncall(@Payload String msg){
        System.out.println("00000000000000000000000"+"msg"+msg);
        Log.info("oncall result :" + msg);
        return "1";
    }

    @MessageMapping("/play")
    @SendToUser("/queue/oncall")
    public String play(@Payload String meg){
        Log.info("play result :" + meg);
        return "1";
    }


}
