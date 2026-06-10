package com.Ishwarjit.Wolf_OVRN_backend.service;

import com.Ishwarjit.Wolf_OVRN_backend.dto.CreateSizeRequest;
import com.Ishwarjit.Wolf_OVRN_backend.dto.SizeDto;
import com.Ishwarjit.Wolf_OVRN_backend.dto.UpdateSizeRequest;
import com.Ishwarjit.Wolf_OVRN_backend.entity.Size;
import com.Ishwarjit.Wolf_OVRN_backend.exception.ResourceNotFoundException;
import com.Ishwarjit.Wolf_OVRN_backend.repository.SizeRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SizeService {

    private final SizeRepository sizeRepository;

    public SizeService(SizeRepository sizeRepository) {
        this.sizeRepository = sizeRepository;
    }

    @Transactional(readOnly = true)
    public List<SizeDto> getAll() {
        return sizeRepository.findAll().stream()
                .map(SizeDto::from)
                .toList();
    }

    @Transactional
    public SizeDto create(CreateSizeRequest request) {
        Size size = new Size();
        size.setSizeName(request.getSizeName());
        return SizeDto.from(sizeRepository.save(size));
    }

    @Transactional
    public SizeDto update(UUID id, UpdateSizeRequest request) {
        Size size = sizeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Size not found: " + id));

        if (request.getSizeName() != null) {
            size.setSizeName(request.getSizeName());
        }

        return SizeDto.from(sizeRepository.save(size));
    }

    @Transactional
    public void delete(UUID id) {
        if (!sizeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Size not found: " + id);
        }
        sizeRepository.deleteById(id);
    }
}
