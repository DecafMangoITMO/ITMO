package com.decafmango.lab_1.coordinates.service;

import com.decafmango.lab_1.Pagination;
import com.decafmango.lab_1.config.SocketHandler;
import com.decafmango.lab_1.coordinates.dao.CoordinatesRepository;
import com.decafmango.lab_1.coordinates.dto.CoordinatesDto;
import com.decafmango.lab_1.coordinates.dto.CreateCoordinatesDto;
import com.decafmango.lab_1.coordinates.dto.PatchCoordinatesDto;
import com.decafmango.lab_1.coordinates.model.Coordinates;
import com.decafmango.lab_1.exception.exceptions.CoordinatesAlreadyExistException;
import com.decafmango.lab_1.exception.exceptions.CoordinatesNotFoundException;
import com.decafmango.lab_1.exception.exceptions.ForbiddenException;
import com.decafmango.lab_1.human_being.dao.HumanBeingRepository;
import com.decafmango.lab_1.human_being.model.HumanBeing;
import com.decafmango.lab_1.security.JwtService;
import com.decafmango.lab_1.user.dao.UserRepository;
import com.decafmango.lab_1.user.model.Role;
import com.decafmango.lab_1.user.model.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CoordinatesService {

    private final CoordinatesRepository coordinatesRepository;
    private final HumanBeingRepository humanBeingRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final SocketHandler socketHandler;

    public Long getCoordinatesCount() {
        return coordinatesRepository.count();
    }

    public List<CoordinatesDto> getCoordinates(int from, int size) {
        Pageable page = Pagination.createPageTemplate(from, size);
        List<Coordinates> coordinates = coordinatesRepository.findAll(page).getContent();
        return coordinates
                .stream()
                .map(coordinates1 -> new CoordinatesDto(
                        coordinates1.getId(),
                        coordinates1.getX(),
                        coordinates1.getY(),
                        coordinates1.getUser().getId()
                )).sorted(new Comparator<CoordinatesDto>() {
                    @Override
                    public int compare(CoordinatesDto o1, CoordinatesDto o2) {
                        return o1.getId().compareTo(o2.getId());
                    }
                })
                .toList();
    }

    public CoordinatesDto createCoordinates(CreateCoordinatesDto createCoordinatesDto,
                                            HttpServletRequest request) {
        if (coordinatesRepository.existsCoordinatesByXAndY(
                createCoordinatesDto.getX(),
                createCoordinatesDto.getY()
        ))
            throw new CoordinatesAlreadyExistException(String.format("Coordinates %f %d already exist",
                    createCoordinatesDto.getX(), createCoordinatesDto.getY()));

        User user = findUserByRequest(request);

        Coordinates coordinates = Coordinates
                .builder()
                .x(createCoordinatesDto.getX())
                .y(createCoordinatesDto.getY())
                .user(user)
                .build();
        coordinates = coordinatesRepository.save(coordinates);
        socketHandler.broadcast("");

        return new CoordinatesDto(
                coordinates.getId(),
                coordinates.getX(),
                coordinates.getY(),
                coordinates.getUser().getId()
        );
    }

    public CoordinatesDto patchCoordinates(Long coordinatesId, PatchCoordinatesDto patchCoordinatesDto,
                                           HttpServletRequest request) {
        Coordinates coordinates = coordinatesRepository.findById(coordinatesId)
                .orElseThrow(() ->
                        new CoordinatesNotFoundException(String.format("Coordinates with id %d not found", coordinatesId))
                );

        if (!checkPermission(coordinates, request))
            throw new ForbiddenException(String.format("No access to coordinates with id %d", coordinatesId));

        if (patchCoordinatesDto.getX() != null)
            coordinates.setX(patchCoordinatesDto.getX());
        if (patchCoordinatesDto.getY() != null)
            coordinates.setY(patchCoordinatesDto.getY());

        coordinates = coordinatesRepository.save(coordinates);
        socketHandler.broadcast("");

        return new CoordinatesDto(
                coordinates.getId(),
                coordinates.getX(),
                coordinates.getY(),
                coordinates.getUser().getId()
        );
    }

    public void deleteCoordinates(Long coordinatesId, HttpServletRequest request) {
        Coordinates coordinates = coordinatesRepository.findById(coordinatesId)
                .orElseThrow(() ->
                        new CoordinatesNotFoundException(String.format("Coordinates with id %d not found", coordinatesId))
                );

        if (!checkPermission(coordinates, request))
            throw new ForbiddenException(String.format("No access to coordinates with id %d", coordinatesId));

        List<HumanBeing> humanBeingsWithThisCoordinates = humanBeingRepository.findAllByCoordinates(coordinates);
        humanBeingRepository.deleteAll(humanBeingsWithThisCoordinates);

        coordinatesRepository.deleteById(coordinatesId);
        socketHandler.broadcast("");
    }

    private User findUserByRequest(HttpServletRequest request) {
        String username = jwtService.extractUsername(jwtService.extractJwtToken(request));
        return userRepository.findByUsername(username).get();
    }

    private boolean checkPermission(Coordinates coordinates, HttpServletRequest request) {
        String username = jwtService.extractUsername(jwtService.extractJwtToken(request));
        User fromUser = userRepository.findByUsername(username).get();
        return coordinates.getUser().getUsername().equals(username) || fromUser.getRole() == Role.ADMIN;
    }

}
