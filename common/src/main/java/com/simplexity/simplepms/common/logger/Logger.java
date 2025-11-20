package com.simplexity.simplepms.common.logger;

import org.slf4j.LoggerFactory;

public class Logger {

    private static org.slf4j.Logger logger;

    public static org.slf4j.Logger getLogger() {
        if (logger == null) logger = LoggerFactory.getLogger("SimplePMs.common");
        return logger;
    }

    public static void setLogger(org.slf4j.Logger logger) {
        Logger.logger = logger;
    }

}
