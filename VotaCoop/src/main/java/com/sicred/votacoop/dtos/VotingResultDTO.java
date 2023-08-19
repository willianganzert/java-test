package com.sicred.votacoop.dtos;

public class VotingResultDTO {

    private Long totalVotes;
    private Long affirmativeVotes;
    private Long negativeVotes;
    private String result;

    public VotingResultDTO() {
    }

    public VotingResultDTO(Long totalVotes, Long affirmativeVotes, Long negativeVotes, String result) {
        this.totalVotes = totalVotes;
        this.affirmativeVotes = affirmativeVotes;
        this.negativeVotes = negativeVotes;
        this.result = result;
    }

    public Long getTotalVotes() {
        return totalVotes;
    }

    public void setTotalVotes(Long totalVotes) {
        this.totalVotes = totalVotes;
    }

    public Long getAffirmativeVotes() {
        return affirmativeVotes;
    }

    public void setAffirmativeVotes(Long affirmativeVotes) {
        this.affirmativeVotes = affirmativeVotes;
    }

    public Long getNegativeVotes() {
        return negativeVotes;
    }

    public void setNegativeVotes(Long negativeVotes) {
        this.negativeVotes = negativeVotes;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
