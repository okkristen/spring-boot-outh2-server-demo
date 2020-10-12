package com.hzys.ssoserver.config.sso;

import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 不同客户端
 */
@Component
public class MyClientDetailsService  implements ClientDetailsService {

    private Map<String, ClientDetails> clientDetailsStore = new HashMap<String, ClientDetails>();
    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        ClientDetails details = clientDetailsStore.get(clientId);
        if (details == null) {
            throw new NoSuchClientException("No client with requested id: " + clientId);
        }
        return details;
    }

    public void setClientDetailsStore(Map<String, ? extends ClientDetails> clientDetailsStore) {
        this.clientDetailsStore = new HashMap<String, ClientDetails>(clientDetailsStore);
    }

}
