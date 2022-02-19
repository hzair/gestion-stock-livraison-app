package com.dss.gestion.service.impl;

import com.dss.gestion.domain.FactureAchat;
import com.dss.gestion.repository.FactureAchatRepository;
import com.dss.gestion.service.FactureAchatService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FactureAchat}.
 */
@Service
@Transactional
public class FactureAchatServiceImpl implements FactureAchatService {

    private final Logger log = LoggerFactory.getLogger(FactureAchatServiceImpl.class);

    private final FactureAchatRepository factureAchatRepository;

    public FactureAchatServiceImpl(FactureAchatRepository factureAchatRepository) {
        this.factureAchatRepository = factureAchatRepository;
    }

    @Override
    public FactureAchat save(FactureAchat factureAchat) {
        log.debug("Request to save FactureAchat : {}", factureAchat);
        return factureAchatRepository.save(factureAchat);
    }

    @Override
    public Optional<FactureAchat> partialUpdate(FactureAchat factureAchat) {
        log.debug("Request to partially update FactureAchat : {}", factureAchat);

        return factureAchatRepository
            .findById(factureAchat.getId())
            .map(existingFactureAchat -> {
                if (factureAchat.getPhoto() != null) {
                    existingFactureAchat.setPhoto(factureAchat.getPhoto());
                }
                if (factureAchat.getPhotoContentType() != null) {
                    existingFactureAchat.setPhotoContentType(factureAchat.getPhotoContentType());
                }
                if (factureAchat.getIdFonc() != null) {
                    existingFactureAchat.setIdFonc(factureAchat.getIdFonc());
                }
                if (factureAchat.getDesignation() != null) {
                    existingFactureAchat.setDesignation(factureAchat.getDesignation());
                }
                if (factureAchat.getDescription() != null) {
                    existingFactureAchat.setDescription(factureAchat.getDescription());
                }
                if (factureAchat.getMontant() != null) {
                    existingFactureAchat.setMontant(factureAchat.getMontant());
                }
                if (factureAchat.getDate() != null) {
                    existingFactureAchat.setDate(factureAchat.getDate());
                }
                if (factureAchat.getPayee() != null) {
                    existingFactureAchat.setPayee(factureAchat.getPayee());
                }

                return existingFactureAchat;
            })
            .map(factureAchatRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FactureAchat> findAll(Pageable pageable) {
        log.debug("Request to get all FactureAchats");
        return factureAchatRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FactureAchat> findOne(Long id) {
        log.debug("Request to get FactureAchat : {}", id);
        return factureAchatRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FactureAchat : {}", id);
        factureAchatRepository.deleteById(id);
    }
}
