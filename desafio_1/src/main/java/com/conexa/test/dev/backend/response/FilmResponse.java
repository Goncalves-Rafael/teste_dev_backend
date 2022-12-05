package com.conexa.test.dev.backend.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class FilmResponse {
    private String title;
    private Integer episodeId;
    private String director;

    private Date releaseDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    public Date getReleaseDate() {
        return releaseDate;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }
}
