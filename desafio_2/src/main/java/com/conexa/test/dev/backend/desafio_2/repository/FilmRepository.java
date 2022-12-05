package com.conexa.test.dev.backend.desafio_2.repository;

import com.conexa.test.dev.backend.desafio_2.entity.FilmEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilmRepository extends JpaRepository<FilmEntity, Long> {
}
