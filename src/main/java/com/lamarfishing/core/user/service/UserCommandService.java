package com.lamarfishing.core.user.service;

import com.lamarfishing.core.user.domain.User;
import com.lamarfishing.core.user.exception.UserNotFound;
import com.lamarfishing.core.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserCommandService {

    private final UserRepository userRepository;

    @PreAuthorize("hasAnyAuthority('GRADE_ADMIN','GRADE_BASIC', 'GRADE_VIP')")
    public void changeNickname(User user, String nickname){
        user.changeNickname(nickname);
    }
}
