package com.ecs.api.repository;

import com.ecs.api.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users,Integer> {

    Optional<Users> findByUsersNo(int usersNo);
    Optional<Users> findByUsersId(String email);

}
