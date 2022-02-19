package com.dss.gestion.service.impl;

import com.dss.gestion.domain.CatalogProduit;
import com.dss.gestion.repository.CatalogProduitRepository;
import com.dss.gestion.service.CatalogProduitService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CatalogProduit}.
 */
@Service
@Transactional
public class CatalogProduitServiceImpl implements CatalogProduitService {

    private final Logger log = LoggerFactory.getLogger(CatalogProduitServiceImpl.class);

    private final CatalogProduitRepository catalogProduitRepository;

    public CatalogProduitServiceImpl(CatalogProduitRepository catalogProduitRepository) {
        this.catalogProduitRepository = catalogProduitRepository;
    }

    @Override
    public CatalogProduit save(CatalogProduit catalogProduit) {
        log.debug("Request to save CatalogProduit : {}", catalogProduit);
        return catalogProduitRepository.save(catalogProduit);
    }

    @Override
    public Optional<CatalogProduit> partialUpdate(CatalogProduit catalogProduit) {
        log.debug("Request to partially update CatalogProduit : {}", catalogProduit);

        return catalogProduitRepository
            .findById(catalogProduit.getId())
            .map(existingCatalogProduit -> {
                if (catalogProduit.getImage() != null) {
                    existingCatalogProduit.setImage(catalogProduit.getImage());
                }
                if (catalogProduit.getImageContentType() != null) {
                    existingCatalogProduit.setImageContentType(catalogProduit.getImageContentType());
                }
                if (catalogProduit.getIdFonc() != null) {
                    existingCatalogProduit.setIdFonc(catalogProduit.getIdFonc());
                }
                if (catalogProduit.getDesignation() != null) {
                    existingCatalogProduit.setDesignation(catalogProduit.getDesignation());
                }
                if (catalogProduit.getDescription() != null) {
                    existingCatalogProduit.setDescription(catalogProduit.getDescription());
                }
                if (catalogProduit.getQuantite() != null) {
                    existingCatalogProduit.setQuantite(catalogProduit.getQuantite());
                }
                if (catalogProduit.getPrixAchatUnit() != null) {
                    existingCatalogProduit.setPrixAchatUnit(catalogProduit.getPrixAchatUnit());
                }
                if (catalogProduit.getPrixVenteUnit() != null) {
                    existingCatalogProduit.setPrixVenteUnit(catalogProduit.getPrixVenteUnit());
                }
                if (catalogProduit.getPrixVenteGros() != null) {
                    existingCatalogProduit.setPrixVenteGros(catalogProduit.getPrixVenteGros());
                }
                if (catalogProduit.getDate() != null) {
                    existingCatalogProduit.setDate(catalogProduit.getDate());
                }

                return existingCatalogProduit;
            })
            .map(catalogProduitRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CatalogProduit> findAll(Pageable pageable) {
        log.debug("Request to get all CatalogProduits");
        return catalogProduitRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CatalogProduit> findOne(Long id) {
        log.debug("Request to get CatalogProduit : {}", id);
        return catalogProduitRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CatalogProduit : {}", id);
        catalogProduitRepository.deleteById(id);
    }
}
