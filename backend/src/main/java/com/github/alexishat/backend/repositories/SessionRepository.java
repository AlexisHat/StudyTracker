package com.github.alexishat.backend.repositories;

import com.github.alexishat.backend.model.Session;
import com.github.alexishat.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface SessionRepository extends JpaRepository<Session, Integer> {
    @Query("SELECT s FROM Session s WHERE s.user = :user AND YEAR(s.startzeit) = :year")
    List<Session> findAllByUserAndYear(@Param("user") User user, @Param("year") int year);

    @Query("SELECT s FROM Session s WHERE s.user = :user AND s.endzeit >= :startOfDay AND s.endzeit < :startOfNextDay ORDER BY s.startzeit asc")
    List<Session> findAllByUserAndDay(@Param("user") User user,
                                               @Param("startOfDay") LocalDateTime startOfDay,
                                               @Param("startOfNextDay") LocalDateTime startOfNextDay);

}
