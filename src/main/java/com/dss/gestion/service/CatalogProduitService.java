package com.dss.gestion.service;

import com.dss.gestion.domain.CatalogProduit;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link CatalogProduit}.
 */
public interface CatalogProduitService {
    /**
     * Save a catalogProduit.
     *
     * @param catalogProduit the entity to save.
     * @return the persisted entity.
     */
    CatalogProduit save(CatalogProduit catalogProduit);

    /**
     * Partially updates a catalogProduit.
     *
     * @param catalogProduit the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CatalogProduit> partialUpdate(CatalogProduit catalogProduit);

    /**
     * Get all the catalogProduits.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CatalogProduit> findAll(Pageable pageable);

    /**
     * Get the "id" catalogProduit.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CatalogProduit> findOne(Long id);

    /**
     * Delete the "id" catalogProduit.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
