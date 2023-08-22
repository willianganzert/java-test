package com.sicred.votacoopusers.controllers;

import com.sicred.votacoopusers.dto.StatusResponse;
import org.json.JSONArray;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
public class UserRestController {

    private Set<String> validCpfs;

    public UserRestController() {
        validCpfs = new HashSet<>();
        loadCpfs();
    }

    @GetMapping("/users/{cpf}")
    public ResponseEntity<?> getStatus(@PathVariable String cpf) {
        if (!isValidCpf(cpf)) {
            return ResponseEntity.notFound().build();
        }
        String status = (cpf.endsWith("0") || cpf.endsWith("2") || cpf.endsWith("4") || cpf.endsWith("6") || cpf.endsWith("8"))
                ? "UNABLE_TO_VOTE"
                : "ABLE_TO_VOTE";

        return ResponseEntity.ok().body(new StatusResponse(status));
    }

    private boolean isValidCpf(String cpf) {
        return validCpfs.contains(cpf);
    }

    private void loadCpfs() {
        try {
            List<String> lines = Files.readAllLines(new ClassPathResource("static/users.csv").getFile().toPath());

            // Skip the header
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);
                validCpfs.add(line);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load CPFs from users.csv", e);
        }
    }
}