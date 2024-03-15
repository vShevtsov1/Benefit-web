package com.example.Redi.logs.service;

import com.example.Redi.logs.data.Logs;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LogsRepo extends MongoRepository<Logs,String> {
}
