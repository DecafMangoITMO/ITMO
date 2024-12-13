package com.decafmango.lab_1.file_import.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ImportObjectDto {

    private final ImportObjectType type;

    private final Object data;

}
