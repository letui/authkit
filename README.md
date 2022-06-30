# Authkit
Automatically regist resource to keycloak via annotation

#Prepared

* keycloak server
* keycloak client 
* a web application based on springboot 

You have to ready a keycloak server for using this toolkit.And then you create a spring-boot project.

    keycloak.realm=authkit
    keycloak.auth-server-url=http://localhost:8080/auth
    keycloak.ssl-required=external
    keycloak.resource=app
    keycloak.bearer-only=false
    keycloak.credentials.secret=MCmBTtry9NEfeDJ5fa97srbXw45qNYVm
    keycloak.securityConstraints[0].authRoles[0]=default-roles-authkit
    keycloak.securityConstraints[0].securityCollections[0].name=protected
    keycloak.securityConstraints[0].securityCollections[0].patterns[0]=/*

The application.properties file for SpringBoot like above , make sure the realm/resource/secret... is setting ok on keycloak server
