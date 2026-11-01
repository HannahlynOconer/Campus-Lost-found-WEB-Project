package com.campus.lostfound.web;

import com.campus.lostfound.exception.DatabaseException;
import com.campus.lostfound.service.AuthService;
import com.campus.lostfound.util.AppLogger;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

/** Runs once when Tomcat starts the application: makes sure the default admin account exists. */
@WebListener
public class AppInitListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent event) {
        AppLogger.log("Web application started");
        try {
            new AuthService().ensureDefaultAdmin();
        } catch (DatabaseException e) {
            AppLogger.log("Could not create the default admin (is MySQL running and schema.sql imported?): " + e.getMessage());
        }
    }
}
