package com.lamarfishing.core.user.service;

import com.lamarfishing.core.user.domain.Grade;
import com.lamarfishing.core.user.domain.Provider;
import com.lamarfishing.core.user.domain.User;
import com.lamarfishing.core.user.dto.command.AuthenticatedUser;
import com.lamarfishing.core.user.exception.UserNotFound;
import com.lamarfishing.core.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Long findUserId(AuthenticatedUser authenticatedUser) {

        User user = userRepository.findByProviderAndSub(authenticatedUser.getProvider(), authenticatedUser.getSub()).
                orElseThrow(UserNotFound::new);
        return user.getId();
    }

    public User findUser(AuthenticatedUser authenticatedUser) {

        return userRepository.findByProviderAndSub(authenticatedUser.getProvider(), authenticatedUser.getSub()).
                orElseThrow(UserNotFound::new);
    }
}
