package com.cydeo.repository;

import com.cydeo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

public interface UserRepository extends JpaRepository<User, Long> {

    // get user based on username
    User findByUserName(String username);
    @Transactional // before we add this annotation, it gave me "transaction" error, it needs to be "rollback"
            // if all steps are successfully completed
            // rollback means if any error happened in any step, everything is going back.
    void deleteByUserName(String username);
}
