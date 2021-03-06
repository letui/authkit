package cc.jstr.authkit.init;

import cc.jstr.authkit.annotation.AuthzEntry;
import cc.jstr.authkit.annotation.AuthzResource;
import cc.jstr.authkit.annotation.AuthzScope;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.resource.ProtectedResource;
import org.keycloak.representations.idm.authorization.ResourceRepresentation;
import org.keycloak.representations.idm.authorization.ScopeRepresentation;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 自动注册类，Spring加载后负责将所有标识了AuthEntry的类扫描并注册到Keycloak的授权中心里
 */
@Configuration
public class AuthzRegister implements ApplicationContextAware {

    /**
     *
     * @param applicationContext Spring application context that contains all of beans you write.
     * @throws BeansException if can not find the beans , this exception will occur
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        AuthzClient authzClient = applicationContext.getBean(AuthzClient.class);
        ProtectedResource resource = authzClient.protection().resource();
        Map<String, Object> authzEntries = applicationContext.getBeansWithAnnotation(AuthzEntry.class);
        authzEntries.forEach((key, item) -> {
            Controller ctrl = item.getClass().getAnnotation(Controller.class);
            RequestMapping ctrlPath = item.getClass().getAnnotation(RequestMapping.class);
            String prefix = "";
            if (ctrl != null && ctrlPath != null) {
                prefix = ctrlPath.value()[0];
            }

            Method[] methods = item.getClass().getMethods();
            String finalPrefix = prefix;
            Arrays.stream(methods).forEach(func -> {

                AuthzScope[] scopes = func.getAnnotationsByType(AuthzScope.class);
                Set<ScopeRepresentation> scopeSet=new HashSet<>();

                for(AuthzScope scope:scopes){
                    ScopeRepresentation scprpt=new ScopeRepresentation();
                    scprpt.setName(scope.name());
                    scprpt.setDisplayName(scope.displayName());
                    scopeSet.add(scprpt);
                }


                AuthzResource annotation = func.getAnnotation(AuthzResource.class);
                if (annotation != null) {
                    ResourceRepresentation byName = resource.findByName(annotation.name());
                    ResourceRepresentation repst = new ResourceRepresentation();
                    repst.setName(annotation.name());
                    repst.setDisplayName(annotation.displayName());
                    repst.setType(annotation.category());
                    repst.setIconUri(annotation.iconUri());
                    repst.setScopes(scopeSet);

                    RequestMapping mappingUri = func.getAnnotation(RequestMapping.class);

                    if (mappingUri != null) {
                        repst.setUri(finalPrefix + mappingUri.value()[0]);
                    }

                    if (byName == null) {
                        resource.create(repst);
                    } else {
                        repst.setId(byName.getId());
                        resource.update(repst);
                    }
                }
            });
        });

    }
}
