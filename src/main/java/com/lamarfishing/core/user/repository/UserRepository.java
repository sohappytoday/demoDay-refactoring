package com.lamarfishing.core.user.repository;

import com.lamarfishing.core.user.domain.Provider;
import com.lamarfishing.core.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(long id);
    Optional<User> findByProviderAndSub(Provider provider, String sub);
}
