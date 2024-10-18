package com.decafmango.lab_1.human_being.controller;

import com.decafmango.lab_1.human_being.dto.CreateHumanBeingDto;
import com.decafmango.lab_1.human_being.dto.HumanBeingDto;
import com.decafmango.lab_1.human_being.dto.PatchHumanBeingDto;
import com.decafmango.lab_1.human_being.service.HumanBeingService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/human-being")
@RequiredArgsConstructor
public class HumanBeingController {

    private final HumanBeingService humanBeingService;

    @GetMapping
    public List<HumanBeingDto> getHumanBeing(@RequestParam int from, @RequestParam int size) {
        return humanBeingService.getHumanBeings(from, size);
    }

    @GetMapping("/random/max-minutes-of-waiting")
    public HumanBeingDto getRandomHumanBeingWithMaxMinutesOfWaiting() {
        return humanBeingService.getRandomHumanBeingWithMaxMinutesOfWaiting();
    }

    @GetMapping("/less-minutes-of-waiting/{minutesOfWaiting}")
    public List<HumanBeingDto> getHumanBeingsWithMinutesOfWaitingLess(@PathVariable Float minutesOfWaiting) {
        if (minutesOfWaiting < 0)
            throw new IllegalArgumentException("minutesOfWaiting must be greater must not be negative");
        return humanBeingService.getHumanBeingsWithMinutesOfWaitingLess(minutesOfWaiting);
    }

    @PostMapping
    public HumanBeingDto createHumanBeing(@RequestBody @Valid CreateHumanBeingDto createHumanBeingDto,
                                          HttpServletRequest request) {
        return humanBeingService.createHumanBeing(createHumanBeingDto, request);
    }

    @PatchMapping("/{humanBeingId}")
    public HumanBeingDto patchHumanBeing(@PathVariable Long humanBeingId,
                                         @RequestBody @Valid PatchHumanBeingDto patchHumanBeingDto,
                                         HttpServletRequest request) {
        return humanBeingService.patchHumanBeing(humanBeingId, patchHumanBeingDto, request);
    }

    @PatchMapping("/lada-kalina")
    public void giveLadaKalina(HttpServletRequest request) {
        humanBeingService.giveLadaKalina(request);
    }

    @DeleteMapping("/{humanBeingId}")
    public void deleteHumanBeing(@PathVariable Long humanBeingId, HttpServletRequest request) {
        humanBeingService.deleteHumanBeing(humanBeingId, request);
    }

    @DeleteMapping("/impact-speed")
    public void deleteHumanBeingsByImpactSpeed(@RequestParam Double impactSpeed, HttpServletRequest request) {
        humanBeingService.deleteHumanBeingsByImpactSpeed(impactSpeed, request);
    }

    @DeleteMapping("/has-toothpick")
    public void deleteHumanBeingsByToothpick(HttpServletRequest request) {
        humanBeingService.deleteHumanBeingsWithToothpick(request);
    }

}
