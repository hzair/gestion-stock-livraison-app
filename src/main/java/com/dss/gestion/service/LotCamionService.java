package com.dss.gestion.service;

import com.dss.gestion.domain.LotCamion;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link LotCamion}.
 */
public interface LotCamionService {
    /**
     * Save a lotCamion.
     *
     * @param lotCamion the entity to save.
     * @return the persisted entity.
     */
    LotCamion save(LotCamion lotCamion);

    /**
     * Partially updates a lotCamion.
     *
     * @param lotCamion the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LotCamion> partialUpdate(LotCamion lotCamion);

    /**
     * Get all the lotCamions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LotCamion> findAll(Pageable pageable);

    /**
     * Get the "id" lotCamion.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LotCamion> findOne(Long id);

    /**
     * Delete the "id" lotCamion.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
