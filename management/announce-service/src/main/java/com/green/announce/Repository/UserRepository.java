package com.green.announce.Repository;

import com.green.announce.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserid(String userid);
    Optional<User> findByNameAndPhoneNumber(String name, String phoneNumber);
    Optional<User> findByPhoneNumber(String phoneNumber);
    Optional<User> findByIdx(Long idx);
}
