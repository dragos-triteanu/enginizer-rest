package com.enginizer.repository;

import com.enginizer.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Spring data repository for retrieving {@link User}s.
 */

@Transactional
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);

    @Modifying
    @Query("Update User u SET  u.password = :password WHERE u.id= :userId")
    void updateUserPassword(@Param("userId") int userId, @Param("password") String password);
}