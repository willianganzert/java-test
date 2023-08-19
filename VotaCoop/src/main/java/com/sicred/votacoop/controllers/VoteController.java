package com.sicred.votacoop.controllers;

import com.sicred.votacoop.models.Vote;
import com.sicred.votacoop.services.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/votes")
public class VoteController {

    @Autowired
    private VoteService voteService;

    // Vote in a session
    @PostMapping("/sessions/{sessionId}/vote")
    public ResponseEntity<String> vote(@PathVariable Long sessionId,
                                       @RequestBody Vote vote) {
        voteService.vote(sessionId, vote);
        return ResponseEntity.ok("Vote registered successfully!");
    }

    // Get voting results for a session
    @GetMapping("/sessions/{sessionId}/results")
    public ResponseEntity<List<Vote>> getSessionResults(@PathVariable Long sessionId) {
        return ResponseEntity.ok(voteService.getSessionResults(sessionId));
    }

}