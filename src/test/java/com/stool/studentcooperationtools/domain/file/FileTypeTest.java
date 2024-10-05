package com.stool.studentcooperationtools.domain.file;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FileTypeTest {

    @DisplayName("지원하지 않는 확장자를 찾았을 때 에러가 발생한다.")
    @Test
    void getFileType(){
        assertThatThrownBy(() -> FileType.getFileType("invaildExtention"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageMatching("해당 파일은 지원하지 않습니다.");
    }

}