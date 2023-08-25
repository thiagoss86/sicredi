package br.com.sicredi.services;

import br.com.sicredi.interfaces.json.vote.VotePutRequest;

public interface VoteService {

    void registerVote(Long scheduleId, VotePutRequest votePutRequest) throws Exception;
}
