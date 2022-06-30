# Authkit
Automatically register resource to keycloak via annotation

# Prepared
* jdk8+
* keycloak server
* keycloak client 
* a web application based on springboot 

You have to ready a keycloak server for using this toolkit. 
Make sure the client config [Authorization Enabled] and [Service Accounts Enabled] is turn on.
Then you should create a spring-boot project with configuration file like under.

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

Create a main class 

    @SpringBootApplication
    @ComponentScan(basePackages = {"cc.jstr.authkit","your-base-package"})
    public class AuthkitApplication {
    
        public static void main(String[] args) {
            SpringApplication.run(AuthkitApplication.class, args);
        }
    
    }

Create a demo Controller

    @RestController
    @AuthzEntry
    @RequestMapping("/authkit")
    public class HomeController {

        @RequestMapping("/home")
        @ResponseBody
        @AuthzResource(name = "首页", displayName = "首页访问地址", category = "公开")
        @AuthzScope(name = "无需授权", displayName = "无需登录，可公开访问")
        public String home() {
            return "HelloWorld";
        }
    }

Now,congratulations. Your web application will automatically register resource and scope to keycloak on every startup . if you change
the value of @AuthzScope or @AuthzResource,no worry , it will update all of them you changed to keycloak on next startup.