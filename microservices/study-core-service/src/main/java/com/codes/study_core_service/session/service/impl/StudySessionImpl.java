package com.codes.study_core_service.session.service.impl;

import com.codes.study_core_service.category.model.Category;
import com.codes.study_core_service.category.repository.CategoryRepository;
import com.codes.study_core_service.session.dto.SessionResponse;
import com.codes.study_core_service.session.dto.StartSessionRequest;
import com.codes.study_core_service.session.enumerator.EStudySession;
import com.codes.study_core_service.session.model.StudySession;
import com.codes.study_core_service.session.repository.StudySessionRepository;

import jakarta.websocket.Session;
import jakarta.ws.rs.NotFoundException;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.InvalidParameterException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class StudySessionImpl {

    private final StudySessionRepository studySessionRepository;
    private final CategoryRepository categoryRepository;

    public StudySessionImpl(StudySessionRepository studySessionRepository, CategoryRepository categoryRepository) {
        this.studySessionRepository = studySessionRepository;
        this.categoryRepository = categoryRepository;
    }


    @Transactional
    public SessionResponse createSession(String userId, StartSessionRequest session) {


        if (session.categoryId() == null)
            throw new InvalidParameterException("category ID is required");

        Category category = this.categoryRepository.findById(session.categoryId()).orElseThrow(
                () -> new NotFoundException("The category ID is not valid")
        );

        if (!category.getUserId().equals(userId)){
            throw new InvalidParameterException("The user ID is not valid");
        }

        if (Boolean.FALSE.equals(category.getActive())){
            throw new InvalidParameterException("The category is not active");
        }

        this.studySessionRepository.findFirstByUserIdAndStatus(userId, EStudySession.RUNNING)
                .ifPresent(studySession -> {
                    throw new InvalidParameterException("The session is already running");
                });

        StudySession studySession = StudySession.builder()
                .userId(userId)
                .categoryId(session.categoryId())
                .note(session.Note())
                .status(EStudySession.RUNNING)
                .startTime(LocalDateTime.now())
                .build();


        this.studySessionRepository.save(studySession);

        return this.toResponse(studySession);
    }

    public SessionResponse finishSession(String userId, Long sessionId) {

        // Si la session existe
        StudySession studySession = this.studySessionRepository.findById(sessionId).orElseThrow(
                () -> new NotFoundException("The session ID is not valid")
        );

        if (!studySession.getUserId().equals(userId)){
            throw new InvalidParameterException("The user ID is not valid");
        }

        if (studySession.getStatus() != EStudySession.RUNNING ){
            throw new InvalidParameterException("The session is not running");
        }

        LocalDateTime endTime = LocalDateTime.now();
        Long seconds = Duration.between(studySession.getStartTime(), endTime).toSeconds();

        if (seconds < 0)
            seconds = 0L;

        studySession.setEndTime(endTime);
        studySession.setTotalSeconds(seconds);
        studySession.setStatus(EStudySession.FINISHED);

        this.studySessionRepository.save(studySession);

        return this.toResponse(studySession);
    }

    @Transactional
    public SessionResponse cancelSession(String userId, Long sessionId) {
        StudySession studySession = this.studySessionRepository.findById(sessionId).orElseThrow(
                () -> new NotFoundException("The session ID is not valid")
        );

        if (!studySession.getUserId().equals(userId)){
            throw new InvalidParameterException("The user ID is not valid");
        }

        if (studySession.getStatus() != EStudySession.RUNNING){
            throw new InvalidParameterException("The session is not running");
        }

        studySession.setEndTime(LocalDateTime.now());
        studySession.setStatus(EStudySession.CANCELED);
        studySession.setTotalSeconds(0L);

        this.studySessionRepository.save(studySession);
        return this.toResponse(studySession);
    }

    @Transactional(readOnly = true)
    public SessionResponse getRunning(String userId) {
        return this.studySessionRepository.findFirstByUserIdAndStatus(userId, EStudySession.RUNNING)
                .map(this::toResponse).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<SessionResponse> getListSession(String userId,
                                                LocalDateTime from, LocalDateTime to,
                                                Long categoryId) {

        List<StudySession> list = (categoryId == null) ?
                this.studySessionRepository.findSessionStudyFromTime(userId, from, to)
                :
                this.studySessionRepository.findSessionStudyFromCategoryAndDateTime(userId, categoryId,from,to);

        return list.stream().map(this::toResponse).toList();
    }

    private SessionResponse toResponse(StudySession studySession) {
        return new SessionResponse(
                studySession.getId(),
                studySession.getCategoryId(),
                studySession.getStatus().name(),
                studySession.getStartTime(),
                studySession.getEndTime(),
                studySession.getTotalSeconds(),
                studySession.getNote()
        );
    }
}
