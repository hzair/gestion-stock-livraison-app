package com.dss.gestion.service.impl;

import com.dss.gestion.domain.Credit;
import com.dss.gestion.repository.CreditRepository;
import com.dss.gestion.service.CreditService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Credit}.
 */
@Service
@Transactional
public class CreditServiceImpl implements CreditService {

    private final Logger log = LoggerFactory.getLogger(CreditServiceImpl.class);

    private final CreditRepository creditRepository;

    public CreditServiceImpl(CreditRepository creditRepository) {
        this.creditRepository = creditRepository;
    }

    @Override
    public Credit save(Credit credit) {
        log.debug("Request to save Credit : {}", credit);
        return creditRepository.save(credit);
    }

    @Override
    public Optional<Credit> partialUpdate(Credit credit) {
        log.debug("Request to partially update Credit : {}", credit);

        return creditRepository
            .findById(credit.getId())
            .map(existingCredit -> {
                if (credit.getMontant() != null) {
                    existingCredit.setMontant(credit.getMontant());
                }
                if (credit.getDesignation() != null) {
                    existingCredit.setDesignation(credit.getDesignation());
                }
                if (credit.getDate() != null) {
                    existingCredit.setDate(credit.getDate());
                }

                return existingCredit;
            })
            .map(creditRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Credit> findAll(Pageable pageable) {
        log.debug("Request to get all Credits");
        return creditRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Credit> findOne(Long id) {
        log.debug("Request to get Credit : {}", id);
        return creditRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Credit : {}", id);
        creditRepository.deleteById(id);
    }
}
