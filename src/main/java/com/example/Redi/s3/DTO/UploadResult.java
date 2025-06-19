package com.example.Redi.s3.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public  class UploadResult {
    private  String key;
    private  String url;

}
