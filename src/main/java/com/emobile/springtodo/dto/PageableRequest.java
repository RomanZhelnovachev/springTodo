package com.emobile.springtodo.dto;

import jakarta.validation.constraints.Min;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public record PageableRequest(

        @Min(value = 0)
        int limit,

        @Min(value = 1)
        int offset
) {

    public Pageable toPageable() {
        return PageRequest.of(offset / limit, limit);
    }

}
