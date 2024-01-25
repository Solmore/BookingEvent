package org.eventbook.eventbooking.repository;

import org.eventbook.eventbooking.domain.event.Category;
import org.eventbook.eventbooking.domain.event.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, BigInteger> {


    @Query(value = """
            SELECT available_attendees_count FROM events e
            WHERE e.id = :id
            """, nativeQuery = true)
    BigInteger findCountById(@Param("id")BigInteger id);

    @Query(value = """
            SELECT * FROM events e
            WHERE e.date between :start and :end
            """, nativeQuery = true)
    List<Event> findAllEventByDuration(@Param("start") Date staredDate,
                                       @Param("end") Date endedDate);

    @Query(value = """
            SELECT * FROM events e
            WHERE e.date >= :start
            """, nativeQuery = true)
    List<Event> findAllEventByStatedDate(@Param("start") Date staredDate);

    @Query(value = """
            SELECT * FROM events e
            WHERE e.date between :start and :end
            AND e.name = :name
            """, nativeQuery = true)
    List<Event> findAllEventByNameAndDuration(@Param("name")
                                              String name,
                                              @Param("start")
                                              Date start,
                                              @Param("end")
                                              Date end);

    @Query(value = """
            SELECT * FROM events e
            WHERE e.category = :category
            """, nativeQuery = true)
    List<Event> findAllEventByCategory(@Param("category")
                                       Category category);

    @Query(value = """
            SELECT * FROM events e
            WHERE e.date between :start and :end
            AND e.category = :category
            """, nativeQuery = true)
    List<Event> findAllEventByDurationAndCategory(@Param("start")
                                                  Date start,
                                                  @Param("end")
                                                  Date end,
                                                  @Param("category")
                                                  Category category);

    @Query(value = """
            SELECT * FROM events e
            WHERE e.date between :start and :end
            AND e.name = :name
            AND e.category = :category
            """, nativeQuery = true)
    List<Event> findAllEventByStatedDateAndCategory(@Param("start")
                                                    Date start,
                                                    @Param("category")
                                                    Category category);


    @Query(value = """
            SELECT * FROM events e
            WHERE e.date between :start and :end
            AND e.name = :name
            AND e.category = :category
            """, nativeQuery = true)
    List<Event> findAllEventByNameAndDurationAndCategory(@Param("name")
                                                         String name,
                                                         @Param("start")
                                                         Date start,
                                                         @Param("end")
                                                         Date end,
                                                         @Param("category")
                                                         Category category);

    @Modifying
    @Query(value = """
            UPDATE events e
            SET available_attendees_count = :availableCount
            WHERE e.id = :eventId""", nativeQuery = true)
    void uploadCountByEventId(BigInteger eventId,
                              BigInteger availableCount);




}
