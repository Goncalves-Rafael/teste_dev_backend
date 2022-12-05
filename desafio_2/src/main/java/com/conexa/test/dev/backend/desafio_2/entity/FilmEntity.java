package com.conexa.test.dev.backend.desafio_2.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "films")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FilmEntity {
    @Id
    @Column
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String title;
    @Column
    private Integer episodeId;
    @Column
    private String director;
    @Column
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date releaseDate;
    @Column
    private Boolean isNew = false;
}
