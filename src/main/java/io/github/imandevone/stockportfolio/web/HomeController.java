package io.github.imandevone.stockportfolio.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringBootVersion;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class HomeController {

    private static final Logger log = LoggerFactory.getLogger(HomeController.class);

    private final JdbcClient jdbc;

    HomeController(JdbcClient jdbc) {
        this.jdbc = jdbc;
    }

    @GetMapping("/")
    String home(Model model) {
        model.addAttribute("javaVersion", System.getProperty("java.version"));
        model.addAttribute("springBootVersion", SpringBootVersion.getVersion());
        model.addAttribute("databaseVersion", databaseVersion());
        return "index";
    }

    private String databaseVersion() {
        try {
            return jdbc.sql("show server_version").query(String.class).single();
        } catch (DataAccessException ex) {
            log.warn("Database not reachable: {}", ex.getMessage());
            return null;
        }
    }
}
