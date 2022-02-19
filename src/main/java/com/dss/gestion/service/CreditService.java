package com.dss.gestion.service;

import com.dss.gestion.domain.Credit;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Credit}.
 */
public interface CreditService {
    /**
     * Save a credit.
     *
     * @param credit the entity to save.
     * @return the persisted entity.
     */
    Credit save(Credit credit);

    /**
     * Partially updates a credit.
     *
     * @param credit the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Credit> partialUpdate(Credit credit);

    /**
     * Get all the credits.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Credit> findAll(Pageable pageable);

    /**
     * Get the "id" credit.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Credit> findOne(Long id);

    /**
     * Delete the "id" credit.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
