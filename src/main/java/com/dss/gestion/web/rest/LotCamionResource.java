package com.dss.gestion.web.rest;

import com.dss.gestion.domain.LotCamion;
import com.dss.gestion.repository.LotCamionRepository;
import com.dss.gestion.service.LotCamionService;
import com.dss.gestion.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.dss.gestion.domain.LotCamion}.
 */
@RestController
@RequestMapping("/api")
public class LotCamionResource {

    private final Logger log = LoggerFactory.getLogger(LotCamionResource.class);

    private static final String ENTITY_NAME = "lotCamion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LotCamionService lotCamionService;

    private final LotCamionRepository lotCamionRepository;

    public LotCamionResource(LotCamionService lotCamionService, LotCamionRepository lotCamionRepository) {
        this.lotCamionService = lotCamionService;
        this.lotCamionRepository = lotCamionRepository;
    }

    /**
     * {@code POST  /lot-camions} : Create a new lotCamion.
     *
     * @param lotCamion the lotCamion to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new lotCamion, or with status {@code 400 (Bad Request)} if the lotCamion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/lot-camions")
    public ResponseEntity<LotCamion> createLotCamion(@Valid @RequestBody LotCamion lotCamion) throws URISyntaxException {
        log.debug("REST request to save LotCamion : {}", lotCamion);
        if (lotCamion.getId() != null) {
            throw new BadRequestAlertException("A new lotCamion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LotCamion result = lotCamionService.save(lotCamion);
        return ResponseEntity
            .created(new URI("/api/lot-camions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /lot-camions/:id} : Updates an existing lotCamion.
     *
     * @param id the id of the lotCamion to save.
     * @param lotCamion the lotCamion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lotCamion,
     * or with status {@code 400 (Bad Request)} if the lotCamion is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lotCamion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/lot-camions/{id}")
    public ResponseEntity<LotCamion> updateLotCamion(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LotCamion lotCamion
    ) throws URISyntaxException {
        log.debug("REST request to update LotCamion : {}, {}", id, lotCamion);
        if (lotCamion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lotCamion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lotCamionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LotCamion result = lotCamionService.save(lotCamion);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lotCamion.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /lot-camions/:id} : Partial updates given fields of an existing lotCamion, field will ignore if it is null
     *
     * @param id the id of the lotCamion to save.
     * @param lotCamion the lotCamion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lotCamion,
     * or with status {@code 400 (Bad Request)} if the lotCamion is not valid,
     * or with status {@code 404 (Not Found)} if the lotCamion is not found,
     * or with status {@code 500 (Internal Server Error)} if the lotCamion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/lot-camions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LotCamion> partialUpdateLotCamion(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LotCamion lotCamion
    ) throws URISyntaxException {
        log.debug("REST request to partial update LotCamion partially : {}, {}", id, lotCamion);
        if (lotCamion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lotCamion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lotCamionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LotCamion> result = lotCamionService.partialUpdate(lotCamion);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lotCamion.getId().toString())
        );
    }

    /**
     * {@code GET  /lot-camions} : get all the lotCamions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lotCamions in body.
     */
    @GetMapping("/lot-camions")
    public ResponseEntity<List<LotCamion>> getAllLotCamions(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of LotCamions");
        Page<LotCamion> page = lotCamionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /lot-camions/:id} : get the "id" lotCamion.
     *
     * @param id the id of the lotCamion to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lotCamion, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/lot-camions/{id}")
    public ResponseEntity<LotCamion> getLotCamion(@PathVariable Long id) {
        log.debug("REST request to get LotCamion : {}", id);
        Optional<LotCamion> lotCamion = lotCamionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(lotCamion);
    }

    /**
     * {@code DELETE  /lot-camions/:id} : delete the "id" lotCamion.
     *
     * @param id the id of the lotCamion to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/lot-camions/{id}")
    public ResponseEntity<Void> deleteLotCamion(@PathVariable Long id) {
        log.debug("REST request to delete LotCamion : {}", id);
        lotCamionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
