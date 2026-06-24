package com.Ishwarjit.Wolf_OVRN_backend.service;

import com.Ishwarjit.Wolf_OVRN_backend.dto.FitDto;
import com.Ishwarjit.Wolf_OVRN_backend.dto.FitRequest;
import com.Ishwarjit.Wolf_OVRN_backend.entity.Fit;
import com.Ishwarjit.Wolf_OVRN_backend.exception.ResourceNotFoundException;
import com.Ishwarjit.Wolf_OVRN_backend.repository.FitRepository;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FitService {

    private final FitRepository fitRepository;

    public FitService(FitRepository fitRepository) {
        this.fitRepository = fitRepository;
    }

    @Transactional(readOnly = true)
    public Page<FitDto> listAll(int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        return fitRepository.findAll(pageable).map(FitDto::from);
    }

    @Transactional(readOnly = true)
    public List<FitDto> listAll() {
        return fitRepository.findAll().stream().map(FitDto::from).collect(Collectors.toList());
    }

    @Transactional
    public FitDto create(FitRequest request) {
        Fit fit = new Fit();
        fit.setName(request.getName());
        fit.setDescription(request.getDescription());
        
        return FitDto.from(fitRepository.save(fit));
    }

    @Transactional
    public FitDto update(UUID id, FitRequest request) {
        Fit fit = fitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fit not found: " + id));
        
        fit.setName(request.getName());
        fit.setDescription(request.getDescription());
        
        return FitDto.from(fitRepository.save(fit));
    }

    @Transactional
    public void delete(UUID id) {
        if (!fitRepository.existsById(id)) {
            throw new ResourceNotFoundException("Fit not found: " + id);
        }
        fitRepository.deleteById(id);
    }
}
