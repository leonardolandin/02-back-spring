package com.br.zerotwo.repository;

import com.br.zerotwo.domain.TransactionRemember;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RememberRepository extends MongoRepository<TransactionRemember, String> {

}
