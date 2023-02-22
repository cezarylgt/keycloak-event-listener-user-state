package com.cezarylgt.keycloak.userstate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.keycloak.events.admin.AdminEvent;

/**
 * @author <a href="https://github.com/cezarylgt">Cezary Michałkiewicz</a>
 */
public class JsonSerializer {
    public static final Gson gson = new Gson();


    public static String toJsonString(Object object) throws JsonProcessingException {

        return gson.toJson(object);
    }

    public static String toJsonString(AdminEvent event) {
        JsonObject json = gson.toJsonTree(event).getAsJsonObject();
        Object parsed = gson.fromJson(event.getRepresentation(), Object.class);
        json.add("representation", gson.toJsonTree(parsed));
        return gson.toJson(json);

    }

}
