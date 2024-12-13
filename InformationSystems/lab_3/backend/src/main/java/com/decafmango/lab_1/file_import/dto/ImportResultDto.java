package com.decafmango.lab_1.file_import.dto;

import com.decafmango.lab_1.user.dto.UserDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class ImportResultDto {

    private Long id;
    private Boolean isSuccessfull;
    private Long objectCount;
    private String filename;
    private UserDto user;
    
}