package com.conexa.test.dev.backend.service;

import com.conexa.test.dev.backend.exception.SWApiException;
import com.conexa.test.dev.backend.gateway.SWDataSourceGateway;
import com.conexa.test.dev.backend.request.FilmRequest;
import com.conexa.test.dev.backend.response.FilmResponse;
import com.conexa.test.dev.backend.response.PageResponse;
import com.conexa.test.dev.backend.response.PersonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.conexa.test.dev.backend.constant.AppConstant.DEFAULT_PAGE_SIZE;
import static com.conexa.test.dev.backend.constant.AppConstant.LUKE_SKYWALKER;

@Service
public class SWService {

    @Autowired
    private SWDataSourceGateway swapDataSourceGateway;

    public PageResponse<List<FilmResponse>> getLukeFilms(FilmRequest filmRequest) throws SWApiException {
        PageResponse<List<PersonResponse>> personPageResponse = swapDataSourceGateway.getPersonsByName(LUKE_SKYWALKER);
        PersonResponse personResponse = personPageResponse.getResults().get(0);

        double totalFilms = personResponse.getFilms().size();
        int totalPages = (int) Math.ceil(totalFilms / DEFAULT_PAGE_SIZE);
        int currentPage = filmRequest.getPage() - 1;
        int currentElement = currentPage * DEFAULT_PAGE_SIZE;

        List<FilmResponse> films;
        if (currentElement < totalFilms) {
            films = personResponse.getFilms().subList(currentElement, (int) Math.min(totalFilms, currentElement + DEFAULT_PAGE_SIZE))
                    .parallelStream()
                    .map(str -> {
                        try {
                            return swapDataSourceGateway.getFilmByUrl(str);
                        } catch (SWApiException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .filter(filmResponse -> filmRequest.getEpisodeId() == null || filmResponse.getEpisodeId().equals(filmRequest.getEpisodeId()))
                    .filter(filmResponse -> filmRequest.getTitle() == null || filmResponse.getTitle().toLowerCase().contains(filmRequest.getTitle().toLowerCase()))
                    .collect(Collectors.toList());
        } else {
            films = Collections.emptyList();
        }

        return PageResponse.<List<FilmResponse>>builder()
                .results(films)
                .count(films.size())
                .next(currentPage + 1 < totalPages ? "hasNext" : null)
                .previous(filmRequest.getPage() > 1 ? "hasPrevious" : null)
                .build();
    }
}
