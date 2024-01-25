package org.eventbook.eventbooking.repository;

import org.eventbook.eventbooking.domain.event.Event;
import org.eventbook.eventbooking.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, BigInteger> {

    @Query(value = """
            SELECT * FROM events e
            JOIN users_events ue ON ue.event_id = e.id
            WHERE ue.user_id = :id
            """, nativeQuery = true)
    List<Event> findAllByUserId(@Param("id") BigInteger userId);

    Optional<User> findByEmail(String email);

    @Query(value = """
             SELECT exists(
                           SELECT 1
                           FROM users_events
                           WHERE user_id = :userId
                             AND event_id = :eventId)
            """, nativeQuery = true)
    boolean isEventOwner(@Param("userId") BigInteger userId,
                        @Param("eventId") BigInteger eventId);


}
