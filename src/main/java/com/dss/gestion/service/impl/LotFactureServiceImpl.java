package com.dss.gestion.service.impl;

import com.dss.gestion.domain.LotFacture;
import com.dss.gestion.repository.LotFactureRepository;
import com.dss.gestion.service.LotFactureService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LotFacture}.
 */
@Service
@Transactional
public class LotFactureServiceImpl implements LotFactureService {

    private final Logger log = LoggerFactory.getLogger(LotFactureServiceImpl.class);

    private final LotFactureRepository lotFactureRepository;

    public LotFactureServiceImpl(LotFactureRepository lotFactureRepository) {
        this.lotFactureRepository = lotFactureRepository;
    }

    @Override
    public LotFacture save(LotFacture lotFacture) {
        log.debug("Request to save LotFacture : {}", lotFacture);
        return lotFactureRepository.save(lotFacture);
    }

    @Override
    public Optional<LotFacture> partialUpdate(LotFacture lotFacture) {
        log.debug("Request to partially update LotFacture : {}", lotFacture);

        return lotFactureRepository
            .findById(lotFacture.getId())
            .map(existingLotFacture -> {
                if (lotFacture.getQuantite() != null) {
                    existingLotFacture.setQuantite(lotFacture.getQuantite());
                }
                if (lotFacture.getDate() != null) {
                    existingLotFacture.setDate(lotFacture.getDate());
                }
                if (lotFacture.getMontantTotal() != null) {
                    existingLotFacture.setMontantTotal(lotFacture.getMontantTotal());
                }

                return existingLotFacture;
            })
            .map(lotFactureRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LotFacture> findAll(Pageable pageable) {
        log.debug("Request to get all LotFactures");
        return lotFactureRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LotFacture> findOne(Long id) {
        log.debug("Request to get LotFacture : {}", id);
        return lotFactureRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LotFacture : {}", id);
        lotFactureRepository.deleteById(id);
    }
}
