package com.example.Redi.compensation;

import com.example.Redi.compensation.DTO.CompensationsDTO;
import com.example.Redi.compensation.DTO.CompensationsUserDTO;
import com.example.Redi.compensation.data.Compensations;
import com.example.Redi.compensation.enums.CompensationStatus;
import com.example.Redi.compensation.services.CompensationsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/compensations")
@AllArgsConstructor
public class CompensationsController {


    private final CompensationsService compensationsService;

    @PostMapping("/create")
    public ResponseEntity<Compensations> createNewCompensations(@ModelAttribute CompensationsDTO compensationsDTO, Authentication authentication){
        try {
            return new ResponseEntity<>(compensationsService.createCompensation(compensationsDTO,authentication.getPrincipal().toString()),HttpStatus.CREATED);
        }
        catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/admin/update/status")
    public ResponseEntity<Compensations> updateCompensationsStatus(@RequestParam(value = "id") String id, @RequestParam(value = "status")CompensationStatus compensationStatus,Authentication authentication){
        try {
            return new ResponseEntity<>(compensationsService.updateCompensationStatus(id,compensationStatus,authentication.getPrincipal().toString()),HttpStatus.OK);

        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/user/compensations")
    public ResponseEntity<List<CompensationsUserDTO>> getUserCompensations(@RequestParam(value = "user_id") String user_id){
        try {
            return new ResponseEntity<>(compensationsService.getUserCompensations(user_id),HttpStatus.OK);

        }
        catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/admin/compensations")
    public ResponseEntity<List<CompensationsUserDTO>> getallCompensations(){
        try {
            return new ResponseEntity<>(compensationsService.getAllCompensations(),HttpStatus.OK);

        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
