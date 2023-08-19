package com.sicred.votacoop.models;

import jakarta.persistence.*;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Name cannot be null")
    @Size(max = 255, message = "Name should not exceed 255 characters")
    @Column(nullable = false)
    private String name;

    @NotNull(message = "CPF cannot be null")
    @Size(min = 11, max = 11, message = "CPF should be exactly 11 characters long, do not include special characters.")
    @Column(unique = true, nullable = false)
    private String cpf;

    @NotNull(message = "Active status cannot be null")
    @Column(nullable = false)
    private Boolean active;

    public Member() {
    }

    public Member(String name, String cpf, Boolean active) {
        this.name = name;
        this.cpf = cpf;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
