package com.cezarylgt.keycloak.userstate;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * @author <a href="https://github.com/cezarylgt">Cezary Michałkiewicz</a>
 */
public class MQTTService {

    private final SpiConfigProperties spiConfig;


    public MQTTService(SpiConfigProperties spiConfig) {
        this.spiConfig = spiConfig;
    }

    public void send(String topic, MqttMessage payload) throws MqttException {
        MemoryPersistence persistence = null;
        if (spiConfig.isUsePersistence()) {
            persistence = new MemoryPersistence();
        }
        MqttClient client = new MqttClient(spiConfig.getServerUri() ,spiConfig.getPublisherId(), persistence);
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setConnectionTimeout(spiConfig.getConnectionTimeout());
        if (spiConfig.getUsername() != null && spiConfig.getPassword() != null) {
            options.setUserName(spiConfig.getUsername());
            options.setPassword(spiConfig.getPassword().toCharArray());
        }
        client.connect(options);
        payload.setQos(0);
        payload.setRetained(true);
        client.publish(topic, payload);
        client.disconnect();
    }

    public static MqttMessage toPayload(String s ) {
        return new MqttMessage(s.getBytes());
    }


}
