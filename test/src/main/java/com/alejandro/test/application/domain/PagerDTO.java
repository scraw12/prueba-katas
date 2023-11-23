package com.alejandro.test.application.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PagerDTO {

    private Integer page;
    private List<OrderDTO> content;
    private PagerLinksDTO links;

    private String errorMessage;

    public PagerDTO(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
