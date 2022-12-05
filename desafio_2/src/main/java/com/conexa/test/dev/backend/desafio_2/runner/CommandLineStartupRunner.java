package com.conexa.test.dev.backend.desafio_2.runner;

import com.conexa.test.dev.backend.desafio_2.service.FilmService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Profile("!test")
@Component
public class CommandLineStartupRunner implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(CommandLineStartupRunner.class);

    @Autowired
    private FilmService filmService;

    @Override
    public void run(String... args) throws IOException, InterruptedException {
        LOG.info("CommandLineStartupRunner::run -> Start");

        int totalLoaded = filmService.loadFilmsFromApiIntoDatabase();
        LOG.info("CommandLineStartupRunner::run -> Loaded {} films from RestAPI into H2 database.", totalLoaded);

        filmService.insertCustomFilmIntoDatabase();
        LOG.info("CommandLineStartupRunner::run -> Inserted new film into H2 database.");

        filmService.printAllSavedFilms();
        LOG.info("CommandLineStartupRunner::run -> End");
    }


}
