package com.cezarylgt.keycloak.userstate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.keycloak.Config;
import org.keycloak.events.EventType;
import org.keycloak.events.admin.ResourceType;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
/**
 * @author <a href="https://github.com/cezarylgt">Cezary Michałkiewicz</a>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpiConfigProperties {
    private String serverUri;
    private String username;
    private String password;
    private String userEventTopic;
    private String adminEventTopic;
    private boolean usePersistence;
    private String publisherId;
    private int connectionTimeout;
    private Set<EventType> handledEventTypes;
    private Set<ResourceType> handledResourceTypes;


    public static SpiConfigProperties fromConfgScope(Config.Scope configScope) {
        Set<EventType> handledUserEvents = new HashSet<>(Arrays.asList(
                EventType.REGISTER, EventType.UPDATE_EMAIL,
                EventType.UPDATE_PROFILE
        ));
        if (configScope.get("handledEventTypes") != null)
            handledUserEvents = Arrays.stream(configScope.getArray("handledEventTypes")).map(EventType::valueOf).collect(Collectors.toSet());

        Set<ResourceType> handledAdminResourceTypes = new HashSet<>(
                Arrays.asList(ResourceType.USER)
        );

        if (configScope.get("handledResourceTypes") != null)
            handledAdminResourceTypes = Arrays.stream(configScope.getArray("handledResourceTypes")).map(ResourceType::valueOf).collect(Collectors.toSet());

        return new SpiConfigProperties(
                configScope.get("serverUri", "tcp://localhost:1883"),
                configScope.get("username", null),
                configScope.get("password", null),
                configScope.get("userEventTopic", "keycloak/event-user"),
                configScope.get("adminEventTopic", "keycloak/event-admin"),
                configScope.getBoolean("usePersistence", false),
                configScope.get("publisherId", "keycloak"),
                configScope.getInt("connectionTimeout", 100),
                handledUserEvents,
                handledAdminResourceTypes

        );

    }


}
