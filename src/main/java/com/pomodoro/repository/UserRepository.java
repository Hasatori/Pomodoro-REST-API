package com.pomodoro.repository;

import com.pomodoro.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Entity;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Integer> {

    User findUserByUsername(String username);

    User findUserByEmail(String email);

    @Modifying
    @Query("update USER u set u.username=?2, u.firstName = ?3, u.lastName = ?4,u.email=?5 where u.id = ?1")
    void updateUserDetails(Integer id, String username, String firstName, String lastName, String email);

    @Modifying
    @Query("update USER u set u.password=?2 where u.id = ?1")
    void updatePassword(Integer id, String password);

    @Modifying
    @Query(value = "INSERT INTO USER( USERNAME, EMAIL, FIRST_NAME, LAST_NAME, PASSWORD, ACCOUNT_EXPIRED, LOCKED, CREDENTIALS_EXPIRED,ENABLED) VALUES (:username,:email,:firstName,:lastName,:password,:accountExpired,:locked,:credentialsExpiredEnabled,:enabled)", nativeQuery = true)
    void insertNewUser( @Param("username") String username, @Param("email") String email, @Param("firstName") String firstname, @Param("lastName") String lastName, @Param("password") String password, @Param("accountExpired") Boolean accountExpired, @Param("locked") Boolean locked, @Param("credentialsExpiredEnabled") Boolean credentialsExpiredEnabled, @Param("enabled") Boolean enabled);

}
