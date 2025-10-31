package com.lamarfishing.core.user.repository;

import com.lamarfishing.core.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface UserRepository extends JpaRepository<User, Long> {
    public User findByPhone(String phone);
}
