package com.dss.gestion.service.impl;

import com.dss.gestion.domain.FactureVente;
import com.dss.gestion.repository.FactureVenteRepository;
import com.dss.gestion.service.FactureVenteService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FactureVente}.
 */
@Service
@Transactional
public class FactureVenteServiceImpl implements FactureVenteService {

    private final Logger log = LoggerFactory.getLogger(FactureVenteServiceImpl.class);

    private final FactureVenteRepository factureVenteRepository;

    public FactureVenteServiceImpl(FactureVenteRepository factureVenteRepository) {
        this.factureVenteRepository = factureVenteRepository;
    }

    @Override
    public FactureVente save(FactureVente factureVente) {
        log.debug("Request to save FactureVente : {}", factureVente);
        return factureVenteRepository.save(factureVente);
    }

    @Override
    public Optional<FactureVente> partialUpdate(FactureVente factureVente) {
        log.debug("Request to partially update FactureVente : {}", factureVente);

        return factureVenteRepository
            .findById(factureVente.getId())
            .map(existingFactureVente -> {
                if (factureVente.getPhoto() != null) {
                    existingFactureVente.setPhoto(factureVente.getPhoto());
                }
                if (factureVente.getPhotoContentType() != null) {
                    existingFactureVente.setPhotoContentType(factureVente.getPhotoContentType());
                }
                if (factureVente.getIdFonc() != null) {
                    existingFactureVente.setIdFonc(factureVente.getIdFonc());
                }
                if (factureVente.getDesignation() != null) {
                    existingFactureVente.setDesignation(factureVente.getDesignation());
                }
                if (factureVente.getDescription() != null) {
                    existingFactureVente.setDescription(factureVente.getDescription());
                }
                if (factureVente.getMontant() != null) {
                    existingFactureVente.setMontant(factureVente.getMontant());
                }
                if (factureVente.getDate() != null) {
                    existingFactureVente.setDate(factureVente.getDate());
                }
                if (factureVente.getPayee() != null) {
                    existingFactureVente.setPayee(factureVente.getPayee());
                }

                return existingFactureVente;
            })
            .map(factureVenteRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FactureVente> findAll(Pageable pageable) {
        log.debug("Request to get all FactureVentes");
        return factureVenteRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FactureVente> findOne(Long id) {
        log.debug("Request to get FactureVente : {}", id);
        return factureVenteRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FactureVente : {}", id);
        factureVenteRepository.deleteById(id);
    }
}
