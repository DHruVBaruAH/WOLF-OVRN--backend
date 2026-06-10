package com.Ishwarjit.Wolf_OVRN_backend.service;

import com.Ishwarjit.Wolf_OVRN_backend.dto.ColorDto;
import com.Ishwarjit.Wolf_OVRN_backend.dto.CreateColorRequest;
import com.Ishwarjit.Wolf_OVRN_backend.dto.UpdateColorRequest;
import com.Ishwarjit.Wolf_OVRN_backend.entity.Color;
import com.Ishwarjit.Wolf_OVRN_backend.exception.ResourceNotFoundException;
import com.Ishwarjit.Wolf_OVRN_backend.repository.ColorRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ColorService {

    private final ColorRepository colorRepository;

    public ColorService(ColorRepository colorRepository) {
        this.colorRepository = colorRepository;
    }

    @Transactional(readOnly = true)
    public List<ColorDto> getAll() {
        return colorRepository.findAll().stream()
                .map(ColorDto::from)
                .toList();
    }

    @Transactional
    public ColorDto create(CreateColorRequest request) {
        Color color = new Color();
        color.setColorName(request.getColorName());
        color.setHexCode(request.getHexCode());
        return ColorDto.from(colorRepository.save(color));
    }

    @Transactional
    public ColorDto update(UUID id, UpdateColorRequest request) {
        Color color = colorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Color not found: " + id));

        if (request.getColorName() != null) {
            color.setColorName(request.getColorName());
        }
        if (request.getHexCode() != null) {
            color.setHexCode(request.getHexCode());
        }

        return ColorDto.from(colorRepository.save(color));
    }

    @Transactional
    public void delete(UUID id) {
        if (!colorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Color not found: " + id);
        }
        colorRepository.deleteById(id);
    }
}
