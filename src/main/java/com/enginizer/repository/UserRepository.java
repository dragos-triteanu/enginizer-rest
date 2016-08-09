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
    User findByMail(String mail);

    @Modifying
    @Query("Update User u SET  u.firstName = :firstName , u.lastName = :lastName ," +
            " u.notificationsEnable = :notificationsEnable WHERE u.id= :userId")
    void updateUserInfo(@Param("userId") int userId, @Param("firstName") String firstName,
                        @Param("lastName") String lastName, @Param("notificationsEnable") boolean notificationsEnable);
}