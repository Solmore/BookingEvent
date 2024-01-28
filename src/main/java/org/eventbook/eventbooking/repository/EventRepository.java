package org.eventbook.eventbooking.repository;

import org.eventbook.eventbooking.domain.event.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query(value = """
            SELECT * FROM events
            WHERE (date > :start AND date <= :end)
            """, nativeQuery = true)
    List<Event> findAllSoonEvents(@Param("start")
                                  LocalDate start,
                                  @Param("end")
                                  LocalDate end);

    @Query(value = """
            SELECT available_attendees_count FROM events
            WHERE id = :id
            """, nativeQuery = true)
    BigInteger findCountById(@Param("id") Long id);

    @Query(value = """
            SELECT e.*
            FROM events e
            INNER JOIN users_events ue ON e.id = ue.event_id
            WHERE ue.user_id = :id
            """, nativeQuery = true)
    List<Event> findAllByUserId(@Param("id") Long userId);

    @Query(value = """
            SELECT * FROM events
            WHERE category = :category
            """, nativeQuery = true)
    List<Event> findAllEventByCategory(@Param("category")
                                       String category);

    @Query(value = """
            SELECT * FROM events
            WHERE (date BETWEEN :start AND :end)
            AND category = :category
            """, nativeQuery = true)
    List<Event> findAllEventByDurationAndCategory(@Param("start")
                                                  LocalDate start,
                                                  @Param("end")
                                                  LocalDate end,
                                                  @Param("category")
                                                  String category);

    @Query(value = """
            SELECT * FROM events
            WHERE (date >= :start)
            AND category = :category
            """, nativeQuery = true)
    List<Event> findAllEventByStartedDateAndCategory(@Param("start")
                                                     LocalDate start,
                                                     @Param("category")
                                                     String category);

    @Query(value = """
            SELECT * FROM events
            WHERE name LIKE :name
            AND category = :category
            """, nativeQuery = true)
    List<Event> findAllEventByNameAndCategory(@Param("name")
                                              String name,
                                              @Param("category")
                                              String category);

    @Query(value = """
            SELECT * FROM events
            WHERE name LIKE :name
            AND date >= :start9
            AND category = :category
            """, nativeQuery = true)
    List<Event> findAllEventByNameAndStartedDateAndCategory(@Param("name")
                                                            String name,
                                                            @Param("start")
                                                            LocalDate start,
                                                            @Param("category")
                                                            String category);

    @Query(value = """
            SELECT * FROM events
            WHERE (name LIKE :name )
            AND (date BETWEEN :start AND :end)
            AND category = :category
            """, nativeQuery = true)
    List<Event> findAllEventByNameAndDurationAndCategory(String name,
                                                         LocalDate start,
                                                         LocalDate end,
                                                         String category);

    @Modifying
    @Query(value = """
            UPDATE events
            SET available_attendees_count = :availableCount
            WHERE id = :eventId""", nativeQuery = true)
    void uploadCountByEventId(@Param("eventId")
                              Long eventId,
                              @Param("availableCount")
                              BigInteger availableCount);

    @Modifying
    @Query(value = """
            DELETE FROM users_events
            WHERE user_id = :userId
            AND event_id = :eventId
            """, nativeQuery = true)
    void deleteTicketByUserIdAndEventId(@Param("userId")
                                        Long userId,
                                        @Param("eventId")
                                        Long eventId);

}
