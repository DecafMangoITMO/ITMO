package com.decafmango.lab_1.file_import.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.decafmango.lab_1.file_import.dto.ImportResultDto;
import com.decafmango.lab_1.file_import.service.ImportService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/import")
@RequiredArgsConstructor
public class ImportController {

    private final ImportService service;

    @GetMapping("/count")
    public Long getImportResultsCount(HttpServletRequest request) {
        return service.getImportResultsCount(request);
    }

    @GetMapping
    public List<ImportResultDto> getImportResults(@RequestParam("from") Integer from, @RequestParam("size") Integer size, HttpServletRequest request) {
        return service.getImportResults(from, size, request);
    }

    @GetMapping("/file/{filename}")
    public Resource getImportFileByName(@PathVariable(name = "filename") String filename, HttpServletRequest request) {
        return service.getImportFileByName(filename, request);
    }
    
    @PostMapping
    public ImportResultDto importFile(@RequestPart("file") MultipartFile file, HttpServletRequest request) {
        return service.importFile(file, request);
    }

}
 