package com.stool.studentcooperationtools.domain.slide.controller.response;

import com.stool.studentcooperationtools.domain.slide.Slide;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SlideFindDto {

    private Long scriptId;
    private Long slideId;
    private String slideUrl;
    private String thumbnailUrl;
    private String script;

    @Builder
    private SlideFindDto(final Long scriptId, final Long slideId,
                         final String slideUrl, final String thumbnailUrl,
                         final String script) {
        this.scriptId = scriptId;
        this.slideId = slideId;
        this.slideUrl = slideUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.script = script;
    }

    public static SlideFindDto of(Slide slide){
        return SlideFindDto.builder()
                .scriptId(slide.getScript().getId())
                .slideId(slide.getId())
                .slideUrl(slide.getSlideUrl())
                .thumbnailUrl(slide.getThumbnail())
                .script(slide.getScript().getScript())
                .build();
    }
}
