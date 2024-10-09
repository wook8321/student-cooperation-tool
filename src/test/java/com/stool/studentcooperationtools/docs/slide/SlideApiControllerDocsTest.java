package com.stool.studentcooperationtools.docs.slide;

import com.stool.studentcooperationtools.docs.RestDocsSupport;
import com.stool.studentcooperationtools.domain.slide.controller.SlideApiController;
import com.stool.studentcooperationtools.domain.slide.controller.response.SlideFindDto;
import com.stool.studentcooperationtools.domain.slide.controller.response.SlideFindResponse;
import com.stool.studentcooperationtools.domain.slide.service.SlideService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SlideApiControllerDocsTest extends RestDocsSupport {

    private SlideService slideService = Mockito.mock(SlideService.class);

    @Override
    protected Object initController() {
        return new SlideApiController(slideService);
    }

    @Test
    void findSlides() throws Exception {
        //given
        String presentationId = "1";
        List<SlideFindDto> findDtoList = List.of(
                SlideFindDto.builder()
                        .slideId(1L)
                        .scriptId(1L)
                        .slideUrl("슬라이드 url")
                        .thumbnailUrl("슬라이드 썸네일 url")
                        .script("발표 스크립트")
                        .build()
        );

        SlideFindResponse response = SlideFindResponse.builder()
                .num(findDtoList.size())
                .slides(findDtoList)
                .build();

        Mockito.when(slideService.findSlides(Mockito.anyLong()))
                .thenReturn(response);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/presentations/"+ presentationId + "/slides"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("slide-find",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                responseFields(
                                        fieldWithPath("code").type(NUMBER)
                                                .description("상태 코드"),
                                        fieldWithPath("status").type(STRING)
                                                .description("응답 상태"),
                                        fieldWithPath("data").type(OBJECT)
                                                .description("응답 데이터"),
                                        fieldWithPath("data.num").type(NUMBER)
                                                .description("조회된 슬라이드의 수"),
                                        fieldWithPath("data.slides[]").type(ARRAY)
                                                .description("슬라이드 리스트"),
                                        fieldWithPath("data.slides[].slideId").type(NUMBER)
                                                .description("슬라이드 식별키"),
                                        fieldWithPath("data.slides[].scriptId").type(NUMBER)
                                                .description("스크립트 식별키"),
                                        fieldWithPath("data.slides[].slideUrl").type(STRING)
                                                .description("슬라이드의 url"),
                                        fieldWithPath("data.slides[].thumbnailUrl").type(STRING)
                                                .description("슬라이드의 썸네일 url"),
                                        fieldWithPath("data.slides[].script").type(STRING)
                                                .description("발표 스크립트 내용")
                                )
                        )
                );


    }
}
