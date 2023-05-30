package com.myutil.rabbitmq;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author pfzhao
 * @title: RabbitmqSender
 * @projectName myUtil
 * @description: TODO
 * @date 2023/5/30 14:55
 */
@Component
@Slf4j
public class RabbitmqSender implements RabbitTemplate.ConfirmCallback {

    public static String SYNC_OLD_DEVICE_QUEUE = "sync-old-device-queue";

    private final RabbitTemplate rabbitTemplate;
    public static String SYNC_EVENT_BUS_EXCHANGE = "event-bus.exchange";
    public static String MQTT_TO_WEBSOCKET_ROUTING_KEY = "origin-device.client.push";
    public static final String DTEN_DYNAMIC_EXCHANGE = "dten-dynamic-exchange";
    public static final String ADMIN_NETTY_KEY_PREFIX = "admin-netty-key-prefix";
    public static final String STRING_MIDDLE_BAR ="-";
    /**
     * 构造方法注入
     */
    @Autowired
    public RabbitmqSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        rabbitTemplate.setConfirmCallback(this); //rabbitTemplate如果为单例的话，那回调就是最后设置的内容
    }

    public void sendWebSocketMsg(String data) {
        sendMsg(SYNC_EVENT_BUS_EXCHANGE,MQTT_TO_WEBSOCKET_ROUTING_KEY, data);
    }


    public void sendMsg(String exchage, String queue, String msg) {
        CorrelationData correlationId = new CorrelationData(StringUtils.replace(UUID.randomUUID().toString(), STRING_MIDDLE_BAR, StringUtils.EMPTY));
        rabbitTemplate.convertAndSend(exchage, queue, msg, correlationId);
        if (log.isDebugEnabled()) {
            log.debug("operate-service send to mq sync device data, message:{}", msg);
        }
    }

    public void sendMsg(String exchage, String queue, Object msg) {
        CorrelationData correlationId = new CorrelationData(StringUtils.replace(UUID.randomUUID().toString(), STRING_MIDDLE_BAR, StringUtils.EMPTY));
        rabbitTemplate.convertAndSend(exchage, queue, msg, correlationId);
        if (log.isDebugEnabled()) {
            log.debug("console-service send to iot-server sync device data, message:{}", msg);
        }
    }

    public void sendMsg(Object content, String uuid) {
        CorrelationData correlationId = new CorrelationData(uuid);
        rabbitTemplate.convertAndSend(DTEN_DYNAMIC_EXCHANGE, ADMIN_NETTY_KEY_PREFIX, content, correlationId);
        log.warn("console-service send to netty-server, message:{}", JSON.toJSONString(content));
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (log.isDebugEnabled()){
            log.debug("AdminSend.confirm()'s call back id: {}" + JSON.toJSONString(correlationData));
        }
        if (ack) {
            if (log.isDebugEnabled()) {
                log.debug("message consume successful");
            }
        } else {
            log.warn("message consume failed:" + cause);
        }
    }
}
