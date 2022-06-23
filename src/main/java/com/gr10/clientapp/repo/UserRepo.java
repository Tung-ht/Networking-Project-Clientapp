package com.gr10.clientapp.repo;

import com.gr10.clientapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {

    User findUserByUsername(String username);
    boolean existsUserByUsername(String username);

}
