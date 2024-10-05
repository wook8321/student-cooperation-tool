package com.stool.studentcooperationtools.domain.part.controller.response;

import com.stool.studentcooperationtools.domain.file.controller.response.PartFindFileDto;
import com.stool.studentcooperationtools.domain.part.Part;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class PartFindDto {

    private Long partId;
    private String partName;
    private String nickName;
    private String profile;
    private List<PartFindFileDto> files;

    @Builder
    private PartFindDto(
            final Long partId, final String partName,
            final String nickName, final String profile,
            final List<PartFindFileDto> files
    ) {
        this.partId = partId;
        this.partName = partName;
        this.nickName = nickName;
        this.profile = profile;
        this.files = files;
    }

    public static PartFindDto of(Part part){
        return PartFindDto.builder()
                .partId(part.getId())
                .partName(part.getPartName())
                .nickName(part.getMember().getNickName())
                .profile(part.getMember().getProfile())
                .files(
                        part.getFileList().stream()
                        .map(PartFindFileDto::of).toList()
                )
                .build();
    }
}
