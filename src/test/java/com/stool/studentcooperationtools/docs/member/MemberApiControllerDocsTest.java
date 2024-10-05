package com.stool.studentcooperationtools.docs.member;

import com.stool.studentcooperationtools.docs.RestDocsSupport;
import com.stool.studentcooperationtools.domain.member.controller.MemberApiController;
import com.stool.studentcooperationtools.domain.member.controller.response.MemberFindMemberDto;
import com.stool.studentcooperationtools.domain.member.controller.response.MemberFindResponse;
import com.stool.studentcooperationtools.domain.member.service.MemberService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MemberApiControllerDocsTest extends RestDocsSupport {

    private final MemberService memberService = mock(MemberService.class);

    @Override
    protected Object initController() {
        return new MemberApiController(memberService);
    }

    @Test
    void searchFriend() throws Exception {
        //given
        List<MemberFindMemberDto> memberDtoList = List.of(
                MemberFindMemberDto.builder()
                        .profile("profilePath")
                        .email("email")
                        .nickname("nickname")
                        .build()
        );
        MemberFindResponse response = MemberFindResponse.builder()
                .members(memberDtoList)
                .num(memberDtoList.size())
                .build();

        Mockito.when(memberService.findFriends())
                .thenReturn(response);

        //when
        //then
        mockMvc.perform(
                    MockMvcRequestBuilders.get("/api/v1/friends")
                            .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("friend-find",
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
                                        .description("데이터 개수"),
                                fieldWithPath("data.members[]").type(ARRAY)
                                        .description("유저 정보 리스트"),
                                fieldWithPath("data.members[].email").type(STRING)
                                        .description("유저 이메일"),
                                fieldWithPath("data.members[].nickname").type(STRING)
                                        .description("유저 닉네임"),
                                fieldWithPath("data.members[].profile").type(STRING)
                                        .description("유저 프로필")
                        )
                    )
                );
    }
}
