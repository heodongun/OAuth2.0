package com.jiraynor.oauthjwt.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String username;


    private String name;
    private String email;
    private String role;

    @Builder
    public UserEntity(String username, String name, String email, String role) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public UserEntity updateEmailAndName(String email, String name) {
        return UserEntity.builder()
                .username(this.username)
                .name(name)
                .email(email)
                .role(this.role)
                .build();
    }
}
