package com.jiraynor.oauthjwt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@AllArgsConstructor
@Builder
@Getter
public class UserDTO {
    private String role;
    private String name;
    private String username;
}