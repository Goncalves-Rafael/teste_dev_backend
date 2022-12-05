package com.conexa.test.dev.backend.desafio_2.service;

import com.conexa.test.dev.backend.desafio_2.constant.AppConstant;
import com.conexa.test.dev.backend.desafio_2.entity.FilmEntity;
import com.conexa.test.dev.backend.desafio_2.repository.FilmRepository;
import com.conexa.test.dev.backend.desafio_2.response.FilmResponse;
import com.conexa.test.dev.backend.desafio_2.utils.HttpUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.List;

import static com.conexa.test.dev.backend.desafio_2.constant.AppConstant.LUKE_FILMS_PATH;

@Service
@RequiredArgsConstructor
public class FilmService {

    private static final Logger LOG = LoggerFactory.getLogger(FilmService.class);
    private final HttpUtils httpUtils;
    private final FilmRepository filmRepository;
    private final ObjectMapper objectMapper;

    @Value("classpath:mockFilmResponse.json")
    private Resource mockFilmFile;

    @Value("${config.rest.baseUrl}")
    private String restApiBaseUrl;


    /**
     * This method is responsible from retrieving all films from Challenge 1 Rest API then persisting them into H2 Database
     * Data retrieval process is iterative, going through all available pages while there is a next page to go to
     *
     * @return int - total count of saved films
     * @throws IOException
     * @throws InterruptedException
     */
    public int loadFilmsFromApiIntoDatabase() throws IOException, InterruptedException {
        String nextUri = restApiBaseUrl + LUKE_FILMS_PATH;
        int currentPage = 1;
        int total = 0;

        do {
            CollectionModel<FilmResponse> films = getFilmsAsCollectionModel(nextUri);
            List<FilmEntity> filmEntities = mapResponseAndSave(films);
            printCurrentSavedPage(currentPage, filmEntities);
            if (films.getLink("next").isPresent()) {
                nextUri = films.getLink("next").get().getHref();
            } else {
                nextUri = null;
            }
            currentPage++;
            total += filmEntities.size();
        } while (nextUri != null);

        return total;
    }

    public void insertCustomFilmIntoDatabase() throws IOException {
        FilmEntity filmEntity = objectMapper.readValue(mockFilmFile.getInputStream(), new TypeReference<>() {
        });
        filmEntity.setIsNew(Boolean.TRUE);
        printFilm(filmRepository.save(filmEntity));
    }

    public void printAllSavedFilms() {
        LOG.info("FilmService::printAllSavedFilms -> Start");
        List<FilmEntity> filmEntities = filmRepository.findAll();
        for (FilmEntity filmEntity : filmEntities) {
            printFilm(filmEntity);
        }
        LOG.info("FilmService::printAllSavedFilms -> End");
    }

    private CollectionModel<FilmResponse> getFilmsAsCollectionModel(String uri) throws IOException, InterruptedException {
        HttpResponse<String> httpResponse = httpUtils.executeGetAsString(uri);
        return objectMapper.readValue(httpResponse.body(), new TypeReference<CollectionModel<FilmResponse>>() {
        });
    }

    private List<FilmEntity> mapResponseAndSave(CollectionModel<FilmResponse> films) {
        List<FilmEntity> filmEntities = objectMapper.convertValue(films.getContent(), new TypeReference<>() {
        });
        return filmRepository.saveAll(filmEntities);
    }

    private void printCurrentSavedPage(int currentPage, List<FilmEntity> filmEntities) {
        LOG.info(AppConstant.LogStrings.LOG_PAGE_DATA, currentPage);
        for (FilmEntity filmEntity : filmEntities) {
            printFilm(filmEntity);
        }
    }

    private void printFilm(FilmEntity filmEntity) {
        LOG.info(AppConstant.LogStrings.LOG_FILM_DATA, filmEntity.getTitle(), filmEntity.getDirector(), filmEntity.getEpisodeId(), filmEntity.getReleaseDate(), filmEntity.getIsNew());
    }
}
