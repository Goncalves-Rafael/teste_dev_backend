package com.conexa.test.dev.backend.controller;

import com.conexa.test.dev.backend.exception.SWApiException;
import com.conexa.test.dev.backend.request.FilmRequest;
import com.conexa.test.dev.backend.response.FilmResponse;
import com.conexa.test.dev.backend.response.PageResponse;
import com.conexa.test.dev.backend.service.SWService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@Validated
public class SWController {

    @Autowired
    private SWService swService;

    @GetMapping(value = "/luke/films", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CollectionModel<FilmResponse>> getLukeFilms(
        @RequestParam(value = "title", required = false) String title,
        @RequestParam(value = "episode_id", required = false) Integer episodeId,
        @RequestParam(value = "page", defaultValue = "1") @Min(1) Integer page) throws SWApiException {
        FilmRequest filmRequest = FilmRequest.builder().title(title).episodeId(episodeId).page(page).build();
        PageResponse<List<FilmResponse>> lukeFilms = swService.getLukeFilms(filmRequest);
        CollectionModel<FilmResponse> response = CollectionModel.of(lukeFilms.getResults());
        if (lukeFilms.getPrevious() != null) {
            Link prevLink = linkTo(methodOn(SWController.class).getLukeFilms(title, episodeId, page - 1)).withRel("previous").expand(title, episodeId);
            response.add(prevLink);
        }
        if (lukeFilms.getNext() != null) {
            Link nextLink = linkTo(methodOn(SWController.class).getLukeFilms(title, episodeId, page + 1)).withRel("next").expand(title, episodeId);
            response.add(nextLink);
        }
        Link selfLink = linkTo(methodOn(SWController.class).getLukeFilms(title, episodeId, page)).withSelfRel().expand(title, episodeId);
        response.add(selfLink);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
