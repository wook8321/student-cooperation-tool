package com.stool.studentcooperationtools.domain.part.controller.response;

import com.stool.studentcooperationtools.domain.part.Part;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class PartFindResponse {

    private int num;
    private List<PartFindDto> parts;

    @Builder
    private PartFindResponse(final int num, final List<PartFindDto> parts) {
        this.num = num;
        this.parts = parts;
    }

    public static PartFindResponse of(List<Part> parts){
        return PartFindResponse.builder()
                .num(parts.size())
                .parts(
                        parts.stream()
                            .map(PartFindDto::of)
                                .toList()
                )
                .build();
    }
}
