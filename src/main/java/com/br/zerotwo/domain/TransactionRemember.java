package com.br.zerotwo.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Builder
@Getter
@Setter
@Document(collection = "transactionsRemember")
public class TransactionRemember {
    @Id
    private String Id;

    private String token;
    private String user;
    private Date expire;
    private Boolean active;
}
