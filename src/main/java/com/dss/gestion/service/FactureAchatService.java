package com.dss.gestion.service;

import com.dss.gestion.domain.FactureAchat;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link FactureAchat}.
 */
public interface FactureAchatService {
    /**
     * Save a factureAchat.
     *
     * @param factureAchat the entity to save.
     * @return the persisted entity.
     */
    FactureAchat save(FactureAchat factureAchat);

    /**
     * Partially updates a factureAchat.
     *
     * @param factureAchat the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FactureAchat> partialUpdate(FactureAchat factureAchat);

    /**
     * Get all the factureAchats.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FactureAchat> findAll(Pageable pageable);

    /**
     * Get the "id" factureAchat.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FactureAchat> findOne(Long id);

    /**
     * Delete the "id" factureAchat.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
