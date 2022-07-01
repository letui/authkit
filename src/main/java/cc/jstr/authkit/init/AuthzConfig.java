package cc.jstr.authkit.init;

import org.keycloak.authorization.client.AuthzClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 配置工具类，关联Keycloak的配置文件，负责创建AuthzClient
 */
@Configuration
public class AuthzConfig {
    /**
     *
     * @param realm The name of realm that has created in keycloak administrator console.
     * @param authUrl Authentication url ,see it in application.properties
     * @param clientId ClientId ,It named resource in application.properties
     * @param credentials The secret of client
     * @return AuthClient
     */
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
