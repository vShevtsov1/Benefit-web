package com.example.Redi.compensation.services;

import com.example.Redi.compensation.DTO.CompensationsDTO;
import com.example.Redi.compensation.DTO.CompensationsUserDTO;
import com.example.Redi.compensation.data.Compensations;
import com.example.Redi.compensation.enums.CompensationStatus;
import com.example.Redi.logs.data.Logs;
import com.example.Redi.logs.enums.LogType;
import com.example.Redi.logs.service.LogsService;
import com.example.Redi.s3.S3Service;
import com.example.Redi.users.data.User;
import com.example.Redi.users.services.UserRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CompensationsService {

    @Autowired
    private CompensationsRepo compensationsRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private LogsService logsService;
    @Autowired
    private S3Service service;
    public Compensations createCompensation(CompensationsDTO compensationsDTO,String email) throws IOException {
        User user = userRepo.findByEmail(email);
        if(user==null){
            return null;
        }
        for (int i = 0;i<compensationsDTO.getImages().size();i++){
            compensationsDTO.getImages().set(i,service.uploadPhoto("compensation", (MultipartFile) compensationsDTO.getImages().get(i)));
        }
        Compensations compensations = modelMapper.map(compensationsDTO,Compensations.class);
        compensations.setUser_id(user.getId());
        compensations.setCompensationStatus(CompensationStatus.APPLICATION_SUBMITTED);
        compensationsRepo.save(compensations);
        return compensations;
    }
    public Compensations updateCompensationStatus(String id,CompensationStatus compensationStatus,String email){
        Compensations compensations = compensationsRepo.findById(id).get();
        String logMessage = String.format("Admin %s requested to change compensation status from %s to %s", email, compensations.getCompensationStatus(), compensationStatus);
        compensations.setCompensationStatus(compensationStatus);
        compensationsRepo.save(compensations);
        logsService.createLog(new Logs(LogType.COMPENSATIONS,email, LocalDateTime.now(),logMessage));
        return compensations;
    }
    public List<CompensationsUserDTO> getUserCompensations(String userId){
        return compensationsRepo.findByUser_id(userId);
    }

    public List<CompensationsUserDTO> getAllCompensations(){
        return compensationsRepo.getAllCompensations();
    }

}
