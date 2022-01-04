package com.br.back02.repository;

import com.br.back02.domain.TransactionRemember;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RememberRepository extends MongoRepository<TransactionRemember, String> {

}
