package br.com.sicredi.converters;

import br.com.sicredi.domain.schedule.Schedule;
import br.com.sicredi.domain.vote.Vote;
import br.com.sicredi.interfaces.json.vote.VotePutRequest;

public class VoteConverter {

    private VoteConverter(){}

    public static Vote toDomain(VotePutRequest votePutRequest, Schedule schedule) {
        return Vote.builder()
                .associateCpf(votePutRequest.getCpf())
                .voteValue(votePutRequest.getVoteValue())
                .schedule(schedule)
                .build();
    }
}
