package com.dss.gestion.service;

import com.dss.gestion.domain.Camion;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Camion}.
 */
public interface CamionService {
    /**
     * Save a camion.
     *
     * @param camion the entity to save.
     * @return the persisted entity.
     */
    Camion save(Camion camion);

    /**
     * Partially updates a camion.
     *
     * @param camion the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Camion> partialUpdate(Camion camion);

    /**
     * Get all the camions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Camion> findAll(Pageable pageable);

    /**
     * Get the "id" camion.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Camion> findOne(Long id);

    /**
     * Delete the "id" camion.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
