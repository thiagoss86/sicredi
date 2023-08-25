package br.com.sicredi.interfaces.controllers;

import br.com.sicredi.interfaces.json.vote.VotePutRequest;
import br.com.sicredi.services.VoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/votes")
@RequiredArgsConstructor
@Valid
public class VoteController {

    private final VoteService voteService;

    @PostMapping("/{scheduleId}")
    public ResponseEntity<String> registerVote(
            @PathVariable Long scheduleId,
            @RequestBody @Valid VotePutRequest putRequest) throws Exception {

        voteService.registerVote(scheduleId, putRequest);

        return ResponseEntity.ok("Voto registado com sucesso");
    }
}
