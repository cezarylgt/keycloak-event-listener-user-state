package com.cezarylgt.keycloak.userstate;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.keycloak.events.Event;
import org.keycloak.events.EventType;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.events.admin.AuthDetails;
import org.keycloak.events.admin.OperationType;
import org.keycloak.events.admin.ResourceType;

import static com.cezarylgt.keycloak.userstate.JsonSerializer.toJsonString;
import static org.junit.jupiter.api.Assertions.*;

class JsonSerializerTest {
    @SneakyThrows
    @Test
    void AdminEventToJsonString() {
        AuthDetails authDetails = new AuthDetails();
        authDetails.setRealmId("realm-id");
        authDetails.setClientId("client-id");
        authDetails.setUserId("user-id");
        authDetails.setIpAddress("ip-address");

        AdminEvent event = new AdminEvent();
        event.setId("xxxx");
        event.setTime(1000L);
        event.setRealmId("realm-id");
        event.setAuthDetails(authDetails);
        event.setResourceType(ResourceType.USER);
        event.setOperationType(OperationType.UPDATE);
        event.setRepresentation("{\"username\":\"player1\",\"enabled\":true,\"emailVerified\":false,\"firstName\":\"Johny\",\"lastName\":\"Kenobi2\",\"email\":\"player1@mail.com\",\"requiredActions\":[]}");
        event.setError("error-message");
        event.setResourcePath("users/ca3c0b9e-afd5-40b9-a8ff-c981110ecaed");


        String result = toJsonString(event);
        assertTrue(result.contains("\"resourceType\":\"USER\""));
        assertTrue(result.contains("\"operationType\":\"UPDATE\""));
        assertTrue(result.contains("\"resourcePath\":\"users/ca3c0b9e-afd5-40b9-a8ff-c981110ecaed\""));
        assertTrue(result.contains("\"representation\":" + event.getRepresentation()));

    }

    @SneakyThrows
    @Test
    void EventToJsonString() {
        Event event = new Event();
        event.setId("event-id");
        event.setTime(1000L);
        event.setType(EventType.UPDATE_PROFILE);
        event.setRealmId("realm-id");
        event.setClientId("client-id");
        event.setUserId("user-id");
        event.setSessionId("session-id");
        event.setIpAddress("ip-address");
        event.setError("error-message");
        event.setDetails(null);

        String result = toJsonString(event);
        assertTrue(result.contains("\"type\":\"UPDATE_PROFILE\""));
        assertTrue(result.contains("\"time\":1000"));
    }


}