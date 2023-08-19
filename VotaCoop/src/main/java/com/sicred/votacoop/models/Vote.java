package com.sicred.votacoop.models;

import jakarta.persistence.*;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(
        name = "vote",
        uniqueConstraints = @UniqueConstraint(columnNames = {"session_id", "memberCpf"})
)
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;

    @NotNull(message = "CPF cannot be null")
    @Size(min = 11, max = 11, message = "CPF should be exactly 11 characters long, do not include special characters.")
    @Column(unique = true, nullable = false)
    private String memberCpf;

    @Column(nullable = false)
    @NotNull(message = "Vote value cannot be null.")
    private Boolean voteValue;

    public Vote() {
    }

    public Vote(Session session, String memberCpf, Boolean voteValue) {
        this.session = session;
        this.memberCpf = memberCpf;
        this.voteValue = voteValue;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public String getMemberCpf() {
        return memberCpf;
    }

    public void setMemberCpf(String memberCpf) {
        this.memberCpf = memberCpf;
    }

    public Boolean getVoteValue() {
        return voteValue;
    }

    public void setVoteValue(Boolean voteValue) {
        this.voteValue = voteValue;
    }
}