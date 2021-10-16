package org.openframework.commons.spring.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * This class provides Application Context object.
 * 
 * @author hello
 *
 */
@Component
public class ApplicationContextProvider implements ApplicationContextAware {
    private static volatile ApplicationContext context;

    /**
     * This method returns current application context
     * 
     * @return application context
     */
    public static ApplicationContext getApplicationContext() {
        return context;
    }

    /**
     * This method sets current application context
     *
     */
    @Override
    public void setApplicationContext(ApplicationContext ctx) {
        ApplicationContextProvider.setContext(ctx);
    }

    /**
     * @param context
     *            the context to set
     */
    public static void setContext(ApplicationContext context) {
        ApplicationContextProvider.context = context;
    }
}
