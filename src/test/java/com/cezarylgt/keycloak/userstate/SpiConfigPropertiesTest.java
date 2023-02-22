package com.cezarylgt.keycloak.userstate;

import org.junit.jupiter.api.Test;
import org.keycloak.Config;
import org.keycloak.events.EventType;
import org.keycloak.events.admin.ResourceType;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class SpiConfigPropertiesTest {
    @Test
    void fromConfgScope_initializesWithValuesFromSystemProperties() {
        String prefix = "PREFIX-";

        System.setProperty(prefix + "serverUri", "custom-serverUri");
        System.setProperty(prefix + "username", "custom-username");
        System.setProperty(prefix + "password", "custom-password");
        System.setProperty(prefix + "userEventTopic", "custom-user-topic");
        System.setProperty(prefix + "adminEventTopic", "custom-admin-topic");
        System.setProperty(prefix + "usePersistence", "true");
        System.setProperty(prefix + "publisherId", "custom-publisher-id");
        System.setProperty(prefix + "connectionTimeout", "1000");
        System.setProperty(prefix + "handledEventTypes", "REGISTER,UPDATE_EMAIL");
        System.setProperty(prefix + "handledResourceTypes", "USER, AUTHENTICATOR_CONFIG");

        Config.Scope scope = new Config.SystemPropertiesScope(prefix);
        SpiConfigProperties spiConfig =  SpiConfigProperties.fromConfgScope(scope);

        assertEquals(spiConfig.getServerUri(), "custom-serverUri");
        assertEquals(spiConfig.getUsername(), "custom-username");
        assertEquals(spiConfig.getPassword(), "custom-password");
        assertEquals(spiConfig.getUserEventTopic(), "custom-user-topic");
        assertEquals(spiConfig.getAdminEventTopic(), "custom-admin-topic");
        assertTrue(spiConfig.isUsePersistence());
        assertEquals(spiConfig.getPublisherId(), "custom-publisher-id");
        assertEquals(spiConfig.getConnectionTimeout(), 1000);
        assertEquals(spiConfig.getHandledEventTypes(), new HashSet<>(Arrays.asList(EventType.REGISTER, EventType.UPDATE_EMAIL)));
        assertEquals(spiConfig.getHandledResourceTypes(), new HashSet<>(Arrays.asList(ResourceType.USER, ResourceType.AUTHENTICATOR_CONFIG)));

    }

}