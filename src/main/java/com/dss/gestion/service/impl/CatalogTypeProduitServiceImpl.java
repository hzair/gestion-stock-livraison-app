package com.dss.gestion.service.impl;

import com.dss.gestion.domain.CatalogTypeProduit;
import com.dss.gestion.repository.CatalogTypeProduitRepository;
import com.dss.gestion.service.CatalogTypeProduitService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CatalogTypeProduit}.
 */
@Service
@Transactional
public class CatalogTypeProduitServiceImpl implements CatalogTypeProduitService {

    private final Logger log = LoggerFactory.getLogger(CatalogTypeProduitServiceImpl.class);

    private final CatalogTypeProduitRepository catalogTypeProduitRepository;

    public CatalogTypeProduitServiceImpl(CatalogTypeProduitRepository catalogTypeProduitRepository) {
        this.catalogTypeProduitRepository = catalogTypeProduitRepository;
    }

    @Override
    public CatalogTypeProduit save(CatalogTypeProduit catalogTypeProduit) {
        log.debug("Request to save CatalogTypeProduit : {}", catalogTypeProduit);
        return catalogTypeProduitRepository.save(catalogTypeProduit);
    }

    @Override
    public Optional<CatalogTypeProduit> partialUpdate(CatalogTypeProduit catalogTypeProduit) {
        log.debug("Request to partially update CatalogTypeProduit : {}", catalogTypeProduit);

        return catalogTypeProduitRepository
            .findById(catalogTypeProduit.getId())
            .map(existingCatalogTypeProduit -> {
                if (catalogTypeProduit.getImage() != null) {
                    existingCatalogTypeProduit.setImage(catalogTypeProduit.getImage());
                }
                if (catalogTypeProduit.getImageContentType() != null) {
                    existingCatalogTypeProduit.setImageContentType(catalogTypeProduit.getImageContentType());
                }
                if (catalogTypeProduit.getIdFonc() != null) {
                    existingCatalogTypeProduit.setIdFonc(catalogTypeProduit.getIdFonc());
                }
                if (catalogTypeProduit.getDesignation() != null) {
                    existingCatalogTypeProduit.setDesignation(catalogTypeProduit.getDesignation());
                }
                if (catalogTypeProduit.getDescription() != null) {
                    existingCatalogTypeProduit.setDescription(catalogTypeProduit.getDescription());
                }

                return existingCatalogTypeProduit;
            })
            .map(catalogTypeProduitRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CatalogTypeProduit> findAll(Pageable pageable) {
        log.debug("Request to get all CatalogTypeProduits");
        return catalogTypeProduitRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CatalogTypeProduit> findOne(Long id) {
        log.debug("Request to get CatalogTypeProduit : {}", id);
        return catalogTypeProduitRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CatalogTypeProduit : {}", id);
        catalogTypeProduitRepository.deleteById(id);
    }
}
