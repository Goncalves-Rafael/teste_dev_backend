package com.conexa.test.dev.backend.desafio_2.constant;

public class AppConstant {
    private AppConstant() {}
    public static final String LUKE_FILMS_PATH = "/luke/films";

    public static class LogStrings {
        private LogStrings() {}

        public static final String LOG_FILM_DATA = "Title: {}, Director: {}, Episode Id: {}, Release Date: {}, Is new: {}";
        public static final String LOG_PAGE_DATA = "Page: {}";
    }
}
