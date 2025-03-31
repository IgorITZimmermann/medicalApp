package de.medical.app.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDto {

    private String username;

    private String password;

    private String name;

    private LocalDate birthDate;
}
