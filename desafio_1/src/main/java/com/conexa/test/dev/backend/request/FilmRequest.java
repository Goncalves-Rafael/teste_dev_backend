package com.conexa.test.dev.backend.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FilmRequest {
    String title;
    Integer episodeId;
    Integer page;
}
