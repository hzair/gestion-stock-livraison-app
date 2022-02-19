package com.dss.gestion.service;

import com.dss.gestion.domain.CatalogTypeProduit;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link CatalogTypeProduit}.
 */
public interface CatalogTypeProduitService {
    /**
     * Save a catalogTypeProduit.
     *
     * @param catalogTypeProduit the entity to save.
     * @return the persisted entity.
     */
    CatalogTypeProduit save(CatalogTypeProduit catalogTypeProduit);

    /**
     * Partially updates a catalogTypeProduit.
     *
     * @param catalogTypeProduit the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CatalogTypeProduit> partialUpdate(CatalogTypeProduit catalogTypeProduit);

    /**
     * Get all the catalogTypeProduits.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CatalogTypeProduit> findAll(Pageable pageable);

    /**
     * Get the "id" catalogTypeProduit.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CatalogTypeProduit> findOne(Long id);

    /**
     * Delete the "id" catalogTypeProduit.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
