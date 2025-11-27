package com.lamarfishing.core.user.dto.request;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ChangeNicknameRequest {
    String nickname;
}
