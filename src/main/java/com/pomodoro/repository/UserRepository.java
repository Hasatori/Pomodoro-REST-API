package com.pomodoro.repository;

import com.pomodoro.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Entity;
import java.util.List;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Integer> {

    User findUserByUsername(String username);

    @Modifying
    @Query("update USER u set u.username=?2, u.firstName = ?3, u.lastName = ?4,u.email=?5 where u.id = ?1")
    void updateUserDetails(Integer id, String username, String firstName, String lastName, String email);

    @Modifying
    @Query("update USER u set u.password=?2 where u.id = ?1")
    void updatePassword(Integer id, String password);
}
