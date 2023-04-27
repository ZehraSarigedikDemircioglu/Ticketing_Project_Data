package com.cydeo.repository;

import com.cydeo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    // removed @Where(clause = "is_deleted=false") from User entity, since it gave null using
    // SELECT * FROM users WHERE id = 4 AND is_deleted = false; could not detect id number
    // so, the easiest way to remove that annotation, and get derive query with deleted as below

    List<User> findAllByIsDeletedOrderByFirstNameDesc(Boolean deleted);
    // if it is true, all users if they are deleted, or the other way

    // get user based on username
    User findByUserNameAndIsDeleted(String username, Boolean deleted);
    @Transactional // before we add this annotation, it gave me "transaction" error, it needs to be "rollback"
            // if all steps are NOT successfully completed
            // rollback means if any error happened in any step, everything is going back.
    void deleteByUserName(String username);

    List<User> findByRoleDescriptionIgnoreCaseAndIsDeleted(String description, Boolean deleted);

}
