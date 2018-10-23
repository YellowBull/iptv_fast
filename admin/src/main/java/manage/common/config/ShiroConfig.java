package manage.common.config;

import manage.modules.sys.shiro.RedisShiroSessionDAO;
import manage.modules.sys.shiro.UserRealm;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Shiro的配置文件
 */
@Configuration
public class ShiroConfig {

    @Bean("sessionManager")
    public SessionManager sessionManager(RedisShiroSessionDAO redisShiroSessionDAO,
                                         @Value("${manage.redis.open}") boolean redisOpen,
                                         @Value("${manage.shiro.redis}") boolean shiroRedis){
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        //设置session过期时间为1小时(单位：毫秒)，默认为30分钟
        sessionManager.setGlobalSessionTimeout(60 * 60 * 1000);
        sessionManager.setSessionValidationSchedulerEnabled(true);
        sessionManager.setSessionIdUrlRewritingEnabled(false);

        //如果开启redis缓存且renren.shiro.redis=true，则shiro session存到redis里
        if(redisOpen && shiroRedis){
            sessionManager.setSessionDAO(redisShiroSessionDAO);
        }
        return sessionManager;
    }

    /**
     *  创建securityManager对象
     * @param userRealm
     * @param sessionManager
     * @return
     */
    @Bean("securityManager")
    public SecurityManager securityManager(UserRealm userRealm, SessionManager sessionManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 设置自定义权限/认证处理
        securityManager.setRealm(userRealm);
        // 设置sessionManager
        securityManager.setSessionManager(sessionManager);
        return securityManager;
    }

//    @Bean("cookieRememberMeManager")
//    public CookieRememberMeManager cookieRememberMeManager(){
//        CookieRememberMeManager cookieRememberMeManager =new CookieRememberMeManager();
//        SimpleCookie simpleCookie = new SimpleCookie();
//        simpleCookie.setMaxAge(30*24*60*60);
//        cookieRememberMeManager.setCookie(simpleCookie);
//        return cookieRememberMeManager;
//    }


    /**
     * 创建shiroFilter
     * @param securityManager
     * @return
     */
    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        // 配置securityManager
        shiroFilter.setSecurityManager(securityManager);
        // 配置登录页面
        shiroFilter.setLoginUrl("/login.html");
        // 未认证的跳转页面
        shiroFilter.setUnauthorizedUrl("/");

        // 过滤器链
        Map<String, String> filterMap = new LinkedHashMap<>();
        // anon 不需要通过认证就可以访问
        // swagger
        filterMap.put("/swagger/**", "anon");
        filterMap.put("/v2/api-docs", "anon");
        filterMap.put("/swagger-ui.html", "anon");
        filterMap.put("/webjars/**", "anon");
        filterMap.put("/swagger-resources/**", "anon");

        // static
        filterMap.put("/statics/**", "anon");
        filterMap.put("/login.html", "anon");
        filterMap.put("/sys/login", "anon");
        filterMap.put("/favicon.ico", "anon");
        filterMap.put("/captcha.jpg", "anon");

        // authc 需要通过认证才能访问的页面
        filterMap.put("/**", "authc");

        shiroFilter.setFilterChainDefinitionMap(filterMap);

        return shiroFilter;
    }

    /**
     * shiro内部执行对象
     * @return
     */
    @Bean("lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * 代理认证
     * @return
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator();
        proxyCreator.setProxyTargetClass(true);
        return proxyCreator;
    }

    /**
     * 代理注解权限认证
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }
}
