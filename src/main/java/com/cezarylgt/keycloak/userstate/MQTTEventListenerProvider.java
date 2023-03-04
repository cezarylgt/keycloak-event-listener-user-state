

package com.cezarylgt.keycloak.userstate;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmProvider;
import org.keycloak.models.UserModel;

import java.util.Arrays;
import java.util.List;

import static com.cezarylgt.keycloak.userstate.JsonSerializer.toJsonString;
import static com.cezarylgt.keycloak.userstate.MQTTService.toPayload;

/**
 * @author <a href="https://github.com/cezarylgt">Cezary Michałkiewicz</a>
 */
@Slf4j
public class MQTTEventListenerProvider implements EventListenerProvider {
    private final MQTTService mqttService;
    private final KeycloakSession session;
    private final RealmProvider realms;
    private final SpiConfigProperties spiConfig;

    public MQTTEventListenerProvider(KeycloakSession session, SpiConfigProperties config) {
        this.session = session;
        this.realms = session.realms();
        this.spiConfig = config;
        this.mqttService = new MQTTService(config);
    }

    @Override
    @SneakyThrows
    public void onEvent(Event event) {
        if (!spiConfig.getHandledEventTypes().contains(event.getType())) {
            return;
        }
        UserModel user = this.session.users().getUserById(realms.getRealm(event.getRealmId()), event.getUserId());
        _processEvent(toJsonString( new UserModelDto(user)), toJsonString(event), spiConfig.getUserEventTopic());

    }

    @SneakyThrows
    @Override
    public void onEvent(AdminEvent event, boolean includeRepresentation) {
        if (!spiConfig.getHandledResourceTypes().contains(event.getResourceType())) {
            return;
        }
        List<String> splitResourcePath = Arrays.asList(event.getResourcePath().split("/"));
        if (splitResourcePath.size() != 2) {
            log.debug("Event does not provide resourcePath to unique user: {}", event);
            return;
        }
        UserModel user = this.session.users().getUserById(realms.getRealm(event.getRealmId()), splitResourcePath.get(1));
        _processEvent(toJsonString( new UserModelDto(user)), toJsonString(event), spiConfig.getAdminEventTopic());
    }


    public  void _processEvent(String  jsonUser, String jsonEvent, String topic) throws JsonProcessingException {

        String stringMessageDto = String.format("{\"event\":%s,\"user\":%s}", jsonEvent, jsonUser);
        log.debug("Sending message: {}", stringMessageDto);
        MqttMessage payload = toPayload(stringMessageDto);
        try {
            mqttService.send(topic, payload);
        } catch (Exception e) {
            log.error("Caught the following error: {}", e.toString());
            e.printStackTrace();
            return;
        }

    }


    @Override
    public void close() {
    }

}
