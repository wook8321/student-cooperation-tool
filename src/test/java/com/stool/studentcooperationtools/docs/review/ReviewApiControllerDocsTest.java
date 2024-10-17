package com.stool.studentcooperationtools.docs.review;

import com.stool.studentcooperationtools.docs.RestDocsSupport;
import com.stool.studentcooperationtools.domain.review.controller.ReviewApiController;
import com.stool.studentcooperationtools.domain.review.controller.request.ReviewAddRequest;
import com.stool.studentcooperationtools.domain.review.controller.response.ReviewAddResponse;
import com.stool.studentcooperationtools.domain.review.controller.response.ReviewFindDto;
import com.stool.studentcooperationtools.domain.review.controller.response.ReviewFindResponse;
import com.stool.studentcooperationtools.domain.review.service.ReviewService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ReviewApiControllerDocsTest extends RestDocsSupport {

    private ReviewService reviewService = Mockito.mock(ReviewService.class);

    @Override
    protected Object initController() {
        return new ReviewApiController(reviewService);
    }

    @Test
    void findReviews() throws Exception {
        //given
        String partId = "1";

        List<ReviewFindDto> findDtoList = List.of(
                ReviewFindDto.builder()
                        .reviewId(1L)
                        .memberId(1L)
                        .profile("프로필")
                        .nickName("닉네임")
                        .createdTime(LocalDate.of(2024,10,6))
                        .content("내용")
                        .build()
        );

        ReviewFindResponse response = ReviewFindResponse.builder()
                .num(findDtoList.size())
                .reviews(findDtoList)
                .build();

        Mockito.when(reviewService.findReview(Mockito.anyLong()))
                .thenReturn(response);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/parts/" + partId + "/review"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("review-find",
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
                                        .description("해당 자료에 등록된 평가 개수"),
                                fieldWithPath("data.reviews[]").type(ARRAY)
                                        .description("해당 자료에 등록된 평가 리스트"),
                                fieldWithPath("data.reviews[].nickName").type(STRING)
                                        .description("평가의 식별키"),
                                fieldWithPath("data.reviews[].reviewId").type(NUMBER)
                                        .description("평가의 식별키"),
                                fieldWithPath("data.reviews[].memberId").type(NUMBER)
                                        .description("평가를 등록한 유저의 식별키"),
                                fieldWithPath("data.reviews[].profile").type(STRING)
                                        .description("평가를 등록한 유저의 프로필"),
                                fieldWithPath("data.reviews[].createdTime").type(ARRAY)
                                        .description("평가가 생성된 시각"),
                                fieldWithPath("data.reviews[].content").type(STRING)
                                        .description("평가의 내용")
                        )
                )
                );

    }

    @Test
    void addReviews() throws Exception {
        //given
        String partId = "1";

        ReviewAddRequest request = ReviewAddRequest.builder()
                .partId(1L)
                .content("내용")
                .build();

        String content = objectMapper.writeValueAsString(request);

        ReviewAddResponse response = ReviewAddResponse.builder()
                .reviewId(1L)
                .nickName("닉네임")
                .profile("프로필")
                .content("내용")
                .createdTime(LocalDate.of(2024,10,6))
                .build();

        Mockito.when(reviewService.addReview(Mockito.any(ReviewAddRequest.class)))
                .thenReturn(response);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/parts/review")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("review-add",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("partId").type(NUMBER)
                                        .description("등록할 역할의 식별키"),
                                fieldWithPath("content").type(STRING)
                                        .description("등록할 평가의 내용")
                        ),
                        responseFields(
                                fieldWithPath("code").type(NUMBER)
                                        .description("상태 코드"),
                                fieldWithPath("status").type(STRING)
                                        .description("응답 상태"),
                                fieldWithPath("data").type(OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("data.reviewId").type(NUMBER)
                                        .description("등록된 평가의 식별키"),
                                fieldWithPath("data.nickName").type(STRING)
                                        .description("평가를 남긴 유저의 닉네임"),
                                fieldWithPath("data.profile").type(STRING)
                                        .description("평가를 남긴 유저의 프로필"),
                                fieldWithPath("data.content").type(STRING)
                                        .description("평가의 내용"),
                                fieldWithPath("data.createdTime").type(ARRAY)
                                        .description("평가가 생성된 시점")
                        )
                        )
                );

    }
}
