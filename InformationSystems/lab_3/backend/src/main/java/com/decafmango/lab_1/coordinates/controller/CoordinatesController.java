package com.decafmango.lab_1.coordinates.controller;

import com.decafmango.lab_1.coordinates.dto.CoordinatesDto;
import com.decafmango.lab_1.coordinates.dto.CreateCoordinatesDto;
import com.decafmango.lab_1.coordinates.dto.PatchCoordinatesDto;
import com.decafmango.lab_1.coordinates.service.CoordinatesService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coordinates")
@RequiredArgsConstructor
public class CoordinatesController {

    private final CoordinatesService coordinatesService;

    @GetMapping("/count")
    public Long getCoordinatesCount() {
        return coordinatesService.getCoordinatesCount();
    }

    @GetMapping
    public List<CoordinatesDto> getCoordinates(@RequestParam int from, @RequestParam int size) {
        return coordinatesService.getCoordinates(from, size);
    }

    @PostMapping
    public CoordinatesDto createCoordinates(@RequestBody @Valid CreateCoordinatesDto createCoordinatesDto,
                                            HttpServletRequest request) {
        return coordinatesService.createCoordinates(createCoordinatesDto, request);
    }

    @PatchMapping("/{coordinatesId}")
    public CoordinatesDto patchCoordinates(@PathVariable Long coordinatesId,
                                           @RequestBody @Valid PatchCoordinatesDto patchCoordinatesDto,
                                           HttpServletRequest request) {
        return coordinatesService.patchCoordinates(coordinatesId, patchCoordinatesDto, request);
    }

    @DeleteMapping("/{coordinatesId}")
    public void deleteCoordinates(@PathVariable Long coordinatesId, HttpServletRequest request) {
        coordinatesService.deleteCoordinates(coordinatesId, request);
    }

}
