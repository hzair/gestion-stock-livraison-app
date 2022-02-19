package com.dss.gestion.service;

import com.dss.gestion.domain.LotFacture;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link LotFacture}.
 */
public interface LotFactureService {
    /**
     * Save a lotFacture.
     *
     * @param lotFacture the entity to save.
     * @return the persisted entity.
     */
    LotFacture save(LotFacture lotFacture);

    /**
     * Partially updates a lotFacture.
     *
     * @param lotFacture the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LotFacture> partialUpdate(LotFacture lotFacture);

    /**
     * Get all the lotFactures.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LotFacture> findAll(Pageable pageable);

    /**
     * Get the "id" lotFacture.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LotFacture> findOne(Long id);

    /**
     * Delete the "id" lotFacture.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
