package com.stool.studentcooperationtools.docs.presentation;

import com.stool.studentcooperationtools.docs.RestDocsSupport;
import com.stool.studentcooperationtools.domain.presentation.controller.PresentationApiController;
import com.stool.studentcooperationtools.domain.presentation.controller.response.PresentationFindResponse;
import com.stool.studentcooperationtools.domain.presentation.service.PresentationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PresentationApiControllerDocsTest extends RestDocsSupport {

    private PresentationService presentationService = Mockito.mock(PresentationService.class);

    @Override
    protected Object initController() {
        return new PresentationApiController(presentationService);
    }

    @Test
    void findPresentation() throws Exception {
        //given
        String roomId = "1";

        PresentationFindResponse response = PresentationFindResponse.builder()
                .presentationId(1L)
                .presentationPath("프레젠테이션 url")
                .updatedTime(LocalDate.of(2024,10,6))
                .build();

        Mockito.when(presentationService.findPresentation(Mockito.anyLong()))
                .thenReturn(response);
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/rooms/" + roomId + "/presentations"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("presentation-find",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                responseFields(
                                        fieldWithPath("code").type(NUMBER)
                                                .description("상태 코드"),
                                        fieldWithPath("status").type(STRING)
                                                .description("응답 상태"),
                                        fieldWithPath("data").type(OBJECT)
                                                .description("응답 데이터"),
                                        fieldWithPath("data.presentationId").type(NUMBER)
                                                .description("생성한 프레젠테이션 식별키"),
                                        fieldWithPath("data.presentationPath").type(STRING)
                                                .description("생성한 프레젠테이션 경로"),
                                        fieldWithPath("data.updatedTime").type(ARRAY)
                                                .description("프레젠테이션이 등록된 시간")
                                        )
                        )
                );

    }
}
