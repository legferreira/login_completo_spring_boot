package com.loginseguro.repository;

import com.loginseguro.entity.Chamado;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChamadoRepository extends MongoRepository<Chamado, String> {
}
