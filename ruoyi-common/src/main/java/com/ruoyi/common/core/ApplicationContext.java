package com.ruoyi.common.core;

import org.springframework.core.env.Environment;

import java.util.Map;

/**
 * @author Eric
 * @date 2017/2/22
 */
public class ApplicationContext {

    private final static ApplicationContext INSTANCE = new ApplicationContext();

    private org.springframework.context.ApplicationContext context;
    private Environment environment;

    private ApplicationContext() {
    }

    public static ApplicationContext getInstance() {
        return INSTANCE;
    }

    public org.springframework.context.ApplicationContext getContext() {
        return context;
    }

    public void setContext(org.springframework.context.ApplicationContext context) {
        this.context = context;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public <T> T getBean(String name) {
        if (context == null) {
            return null;
        }

        Object bean = context.getBean(name);

        if (bean == null) {
            return null;
        }

        return (T) bean;
    }

    public <T> T getBean(Class type) {
        if (context == null) {
            return null;
        }

        Object bean = context.getBean(type);

        if (bean == null) {
            return null;
        }

        return (T) bean;
    }

    public <T> Map<String, T> getBeansOfType(Class<T> clazz) {
        return context.getBeansOfType(clazz);
    }
}
