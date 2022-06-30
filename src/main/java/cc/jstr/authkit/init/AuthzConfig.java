package cc.jstr.authkit.init;

import org.keycloak.authorization.client.AuthzClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class AuthzConfig {
    @Bean
    public AuthzClient authzClient(@Value("${keycloak.realm}") String realm,
                                   @Value("${keycloak.auth-server-url}") String authUrl,
                                   @Value("${keycloak.resource}") String clientId,
                                   @Value("${keycloak.credentials.secret}") String credentials) {

        org.keycloak.authorization.client.Configuration conf = new org.keycloak.authorization.client.Configuration();
        conf.setAuthServerUrl(authUrl);
        conf.setRealm(realm);
        Map<String, Object> map = new HashMap<>();
        map.put("secret", credentials);
        conf.setCredentials(map);
        conf.setResource(clientId);
        AuthzClient authzClient = AuthzClient.create(conf);
        return authzClient;
    }
}
