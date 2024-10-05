package com.stool.studentcooperationtools.domain.slide.controller.response;

import com.stool.studentcooperationtools.domain.slide.Slide;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SlideFindResponse {

    private int num;
    private List<SlideFindDto> slides;
    @Builder
    private SlideFindResponse(final int num, final List<SlideFindDto> slides) {
        this.num = num;
        this.slides = slides;
    }

    public static SlideFindResponse of(List<Slide> slides){
        return SlideFindResponse.builder()
                .num(slides.size())
                .slides(
                        slides.stream()
                        .map(SlideFindDto::of)
                        .toList()
                )
                .build();
    }
}
