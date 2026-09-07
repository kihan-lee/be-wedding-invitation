package com.wedding.invitation.invitation.dto;

import com.wedding.invitation.domain.AccessType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class MessageCreateRequest {

    private String token;

    @NotNull(message = "접근 방식을 확인할 수 없습니다")
    private AccessType accessType;

    @NotBlank(message = "이름을 입력해주세요")
    @Size(max = 50, message = "이름은 50자 이내로 입력해주세요")
    private String guestName;

    @NotBlank(message = "메시지를 입력해주세요")
    private String content;

    protected MessageCreateRequest() {
    }

    public MessageCreateRequest(String token, AccessType accessType, String guestName, String content) {
        this.token = token;
        this.accessType = accessType;
        this.guestName = guestName;
        this.content = content;
    }
}