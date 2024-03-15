package com.example.Redi.logs.service;

import com.example.Redi.logs.data.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogsService {

    @Autowired
    private LogsRepo logsRepo;

    public void createLog(Logs logs) {
        logsRepo.save(logs);
    }

    public List<Logs> getAll(){
        return logsRepo.findAll();
    }
}
