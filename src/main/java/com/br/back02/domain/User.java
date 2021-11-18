package com.br.back02.domain;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "users")
@Getter
public class User {
    @Id
    private String id;

    private String email;
    private String password;
    private String name;
    private Boolean active;
    private Boolean completeRegister;
    private Date created;
    private Date modificated;
}
