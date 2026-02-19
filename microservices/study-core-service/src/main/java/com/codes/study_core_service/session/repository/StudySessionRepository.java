package com.codes.study_core_service.session.repository;


import com.codes.study_core_service.session.enumerator.EStudySession;
import com.codes.study_core_service.session.model.StudySession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudySessionRepository extends JpaRepository<StudySession, Long> {

    /**
     * Consulta mediante funcion, internamente se realiza una query que seria semejante a
     *
     * SELECT .* FROM study_session
                WHERE study_session.user_id = :userid
                AND study_session.status = :status
     * @param userid
     * @param status
     * @return
     */
    Optional<StudySession> findFirstByUserIdAndStatus(String userid, EStudySession status);


    /**
     * Buscar todas las sessiones de estudio de un usuario dentro de un rango de fechas
     * @param userId
     * @param from
     * @param to
     * @return
     */
    @Query("SELECT s FROM StudySession s WHERE s.userId = :userId AND s.startTime BETWEEN :from AND :to ORDER BY s.startTime DESC")
    List<StudySession> findSessionStudyFromTime(
            @Param("userId") String userId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );

    /**
     * Buscar sesiones de un usuario por fecha y categoria
     * @return
     */
    @Query(
            """
                    SELECT s 
                    FROM StudySession s
                    WHERE s.userId = :userId
                    AND s.categoryId = :categoryId
                    AND s.startTime BETWEEN :from AND :to
                    ORDER BY s.startTime DESC
            """
    )
    List<StudySession> findSessionStudyFromCategoryAndDateTime(
            @Param("userId") String userId,
            @Param("categoryId") Long categoryId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );
}
