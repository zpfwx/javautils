package com.myutil.rabbitmq;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author pfzhao
 * @title: RabbitmqRcvListener
 * @projectName myUtil
 * @description: TODO
 * @date 2023/5/30 15:05
 */
@Component
@Slf4j
public class RabbitmqRcvListener {

    @RabbitListener(queues= "sync-upgrade-status-queue")
    public void handleMqttInstallStatusMessage(String message) {
        log.info("receive mqtt InstallStatus message:" + message);
        try {
            Map<String,Object> map = JSON.parseObject(message, Map.class);
            String deviceId = String.valueOf(map.get("deviceId"));
            Integer installStatus = (Integer) map.get("installStatus");
           /// deviceBaseInfoService.operateInstallStatus(deviceId,installStatus);
        } catch (Exception e){
            log.error("Exception happened during syncMqttToWebSocket with detail:", e);
        }
    }
}
