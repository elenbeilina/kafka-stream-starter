package com.aqualen.kafkastreamstarter.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class SpringContext implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    /**
     * Returns the Spring managed bean instance of the given class type if it exists.
     * Returns null otherwise.
     *
     * @return bean
     */
    public static <T> T getBean(Class<T> beanClass) {
        return applicationContext.getBean(beanClass);
    }

    @Override
    @SuppressWarnings("squid:S2696")
    public void setApplicationContext(@NonNull ApplicationContext context) throws BeansException {
        // store ApplicationContext reference to access required beans later on
        applicationContext = context;
    }
}