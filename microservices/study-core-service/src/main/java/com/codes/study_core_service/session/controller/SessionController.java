package com.codes.study_core_service.session.controller;

import com.codes.study_core_service.session.dto.SessionResponse;
import com.codes.study_core_service.session.dto.StartSessionRequest;
import com.codes.study_core_service.session.service.impl.StudySessionImpl;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/v1/sessions")
public class SessionController {

    private final StudySessionImpl studySession;

    public SessionController(StudySessionImpl studySession) {
        this.studySession = studySession;
    }

    @PostMapping("/start")
    public SessionResponse startSession(
            @RequestHeader("X-User-Id") String userId,
            @RequestBody StartSessionRequest startSessionRequest
    ) {
        return this.studySession.createSession(userId, startSessionRequest);
    }

    @PostMapping("/{id}/finish")
    public SessionResponse finishSession(
            @RequestHeader("X-User-Id") String userId,
            @PathVariable Long id
    ) {
        return this.studySession.finishSession(userId, id);
    }

    @PostMapping("/{id}/cancel")
    public SessionResponse cancelSession(
            @RequestHeader("X-User-Id") String userId,
            @PathVariable Long id
    ) {
        return this.studySession.cancelSession(userId, id);
    }

    @GetMapping("/running")
    public SessionResponse runningSession(@RequestHeader("X-User-Id") String userId){
        return this.studySession.getRunning(userId);
    }

    @GetMapping
    public List<SessionResponse> list(
            @RequestHeader("X-User-Id") String userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(required = false) Long categoryId
    ) {
        return this.studySession.getListSession(userId, from, to, categoryId);
    }

}
