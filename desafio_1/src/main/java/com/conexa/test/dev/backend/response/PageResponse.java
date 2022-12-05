package com.conexa.test.dev.backend.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class PageResponse<T> {
    private T results;
    private String next;
    private String previous;
    private Integer count;
}
