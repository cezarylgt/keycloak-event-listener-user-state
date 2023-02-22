
package com.cezarylgt.keycloak.userstate;

import org.keycloak.Config;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventListenerProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

/**
 * @author <a href="https://github.com/cezarylgt">Cezary Michałkiewicz</a>
 */
public class MQTTEventListenerProviderFactory implements EventListenerProviderFactory {
    private SpiConfigProperties spiConfig;

    @Override
    public EventListenerProvider create(KeycloakSession session) {
        return new MQTTEventListenerProvider(session, spiConfig);
    }

    @Override
    public void init(Config.Scope config) {
      spiConfig = SpiConfigProperties.fromConfgScope(config);

    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {

    }

    @Override
    public void close() {
    }

    @Override
    public String getId() {
        return "user-state";
    }

}
