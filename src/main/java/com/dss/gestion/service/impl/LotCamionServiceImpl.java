package com.dss.gestion.service.impl;

import com.dss.gestion.domain.LotCamion;
import com.dss.gestion.repository.LotCamionRepository;
import com.dss.gestion.service.LotCamionService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LotCamion}.
 */
@Service
@Transactional
public class LotCamionServiceImpl implements LotCamionService {

    private final Logger log = LoggerFactory.getLogger(LotCamionServiceImpl.class);

    private final LotCamionRepository lotCamionRepository;

    public LotCamionServiceImpl(LotCamionRepository lotCamionRepository) {
        this.lotCamionRepository = lotCamionRepository;
    }

    @Override
    public LotCamion save(LotCamion lotCamion) {
        log.debug("Request to save LotCamion : {}", lotCamion);
        return lotCamionRepository.save(lotCamion);
    }

    @Override
    public Optional<LotCamion> partialUpdate(LotCamion lotCamion) {
        log.debug("Request to partially update LotCamion : {}", lotCamion);

        return lotCamionRepository
            .findById(lotCamion.getId())
            .map(existingLotCamion -> {
                if (lotCamion.getQuantite() != null) {
                    existingLotCamion.setQuantite(lotCamion.getQuantite());
                }
                if (lotCamion.getDate() != null) {
                    existingLotCamion.setDate(lotCamion.getDate());
                }
                if (lotCamion.getMontantTotal() != null) {
                    existingLotCamion.setMontantTotal(lotCamion.getMontantTotal());
                }

                return existingLotCamion;
            })
            .map(lotCamionRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LotCamion> findAll(Pageable pageable) {
        log.debug("Request to get all LotCamions");
        return lotCamionRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LotCamion> findOne(Long id) {
        log.debug("Request to get LotCamion : {}", id);
        return lotCamionRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LotCamion : {}", id);
        lotCamionRepository.deleteById(id);
    }
}
