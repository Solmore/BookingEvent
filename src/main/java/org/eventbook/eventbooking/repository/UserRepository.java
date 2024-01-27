package org.eventbook.eventbooking.repository;

import org.eventbook.eventbooking.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {



    Optional<User> findByEmail(String email);

    @Query(value = """
             SELECT exists(
                           SELECT 1
                           FROM users_events
                           WHERE user_id = :userId
                             AND event_id = :eventId)
            """, nativeQuery = true)
    boolean isEventOwner(@Param("userId") Long userId,
                        @Param("eventId") Long eventId);

    @Query(value = """
             SELECT * FROM users_events
             WHERE event_id = :eventId
            """, nativeQuery = true)
    List<Long> findUserIdByEventId(@Param("eventId") Long eventId);

    @Modifying
    @Query(value = """
             INSERT INTO users_events(user_id,
                                      event_id,
                                      ticket_count)
             VALUES (:userId, :eventId, :count)
            """, nativeQuery = true)
    void createByUserIdAndEventId(@Param("userId")
                                  Long userId,
                                  @Param("eventId")
                                  Long eventId,
                                  @Param("count")
                                  BigInteger count);

    @Modifying
    @Query(value = """
             UPDATE users_events
             SET ticket_count = :count
             WHERE user_id = :userId AND event_id = :eventId
            """, nativeQuery = true)
    void uploadCountByUserIdAndEventId(@Param("userId")
                                       Long userId,
                                       @Param("eventId")
                                       Long eventId,
                                       @Param("count")
                                       BigInteger count);

}
