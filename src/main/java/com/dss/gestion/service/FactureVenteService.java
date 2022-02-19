package com.dss.gestion.service;

import com.dss.gestion.domain.FactureVente;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link FactureVente}.
 */
public interface FactureVenteService {
    /**
     * Save a factureVente.
     *
     * @param factureVente the entity to save.
     * @return the persisted entity.
     */
    FactureVente save(FactureVente factureVente);

    /**
     * Partially updates a factureVente.
     *
     * @param factureVente the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FactureVente> partialUpdate(FactureVente factureVente);

    /**
     * Get all the factureVentes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FactureVente> findAll(Pageable pageable);

    /**
     * Get the "id" factureVente.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FactureVente> findOne(Long id);

    /**
     * Delete the "id" factureVente.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
