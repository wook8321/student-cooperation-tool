package com.stool.studentcooperationtools.docs.part;

import com.stool.studentcooperationtools.docs.RestDocsSupport;
import com.stool.studentcooperationtools.domain.file.FileType;
import com.stool.studentcooperationtools.domain.file.controller.response.PartFindFileDto;
import com.stool.studentcooperationtools.domain.part.controller.PartApiController;
import com.stool.studentcooperationtools.domain.part.controller.response.PartFindDto;
import com.stool.studentcooperationtools.domain.part.controller.response.PartFindResponse;
import com.stool.studentcooperationtools.domain.part.service.PartService;
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

public class PartApiControllerDocsTest extends RestDocsSupport {

    private PartService partService = Mockito.mock(PartService.class);

    @Override
    protected Object initController() {
        return new PartApiController(partService);
    }

    @Test
    void findParts() throws Exception {
        //given
        String roomId = "1";
        List<PartFindFileDto> files = List.of(
                PartFindFileDto.builder()
                        .fileName("S3에 저장된 파일 이름, 파일 식별키")
                        .originalName("원래 파일이름")
                        .filePath("S3에 저장된 주소")
                        .fileType(FileType.PDF)
                        .build()
        );
        List<PartFindDto> parts = List.of(
                PartFindDto.builder()
                        .partId(1L)
                        .partName("자료조사 섹션 이름")
                        .nickName("닉네임")
                        .profile("프로필")
                        .files(files)
                        .build()
        );

        PartFindResponse response = PartFindResponse.builder()
                .num(parts.size())
                .parts(parts)
                .build();

        Mockito.when(partService.findParts(Mockito.anyLong()))
                .thenReturn(response);
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/rooms/" + roomId + "/parts"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("part-find",
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
                                        .description("생성한 방 제목"),
                                fieldWithPath("data.parts[]").type(ARRAY)
                                        .description("역할 정보 리스트"),
                                fieldWithPath("data.parts[].partId").type(NUMBER)
                                        .description("역할 식별키"),
                                fieldWithPath("data.parts[].partName").type(STRING)
                                        .description("조사할 자료 이름"),
                                fieldWithPath("data.parts[].nickName").type(STRING)
                                        .description("해당 조사 역할을 맡은 유저 닉네임"),
                                fieldWithPath("data.parts[].profile").type(STRING)
                                        .description("해당 조사 역할을 맡은 유저 프로필"),
                                fieldWithPath("data.parts[].files[]").type(ARRAY)
                                        .description("조사한 자료의 파일들"),
                                fieldWithPath("data.parts[].files[].fileName").type(STRING)
                                        .description("S3에 저장된 파일 이름, 파일 식별키"),
                                fieldWithPath("data.parts[].files[].originalName").type(STRING)
                                        .description("원래 파일 이름"),
                                fieldWithPath("data.parts[].files[].filePath").type(STRING)
                                        .description("파일의 경로"),
                                fieldWithPath("data.parts[].files[].fileType").type(STRING)
                                        .description("파일의 타입")
                        )
                )
                );
    }
}
