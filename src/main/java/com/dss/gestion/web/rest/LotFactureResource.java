package com.dss.gestion.web.rest;

import com.dss.gestion.domain.LotFacture;
import com.dss.gestion.repository.LotFactureRepository;
import com.dss.gestion.service.LotFactureService;
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
 * REST controller for managing {@link com.dss.gestion.domain.LotFacture}.
 */
@RestController
@RequestMapping("/api")
public class LotFactureResource {

    private final Logger log = LoggerFactory.getLogger(LotFactureResource.class);

    private static final String ENTITY_NAME = "lotFacture";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LotFactureService lotFactureService;

    private final LotFactureRepository lotFactureRepository;

    public LotFactureResource(LotFactureService lotFactureService, LotFactureRepository lotFactureRepository) {
        this.lotFactureService = lotFactureService;
        this.lotFactureRepository = lotFactureRepository;
    }

    /**
     * {@code POST  /lot-factures} : Create a new lotFacture.
     *
     * @param lotFacture the lotFacture to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new lotFacture, or with status {@code 400 (Bad Request)} if the lotFacture has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/lot-factures")
    public ResponseEntity<LotFacture> createLotFacture(@Valid @RequestBody LotFacture lotFacture) throws URISyntaxException {
        log.debug("REST request to save LotFacture : {}", lotFacture);
        if (lotFacture.getId() != null) {
            throw new BadRequestAlertException("A new lotFacture cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LotFacture result = lotFactureService.save(lotFacture);
        return ResponseEntity
            .created(new URI("/api/lot-factures/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /lot-factures/:id} : Updates an existing lotFacture.
     *
     * @param id the id of the lotFacture to save.
     * @param lotFacture the lotFacture to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lotFacture,
     * or with status {@code 400 (Bad Request)} if the lotFacture is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lotFacture couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/lot-factures/{id}")
    public ResponseEntity<LotFacture> updateLotFacture(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LotFacture lotFacture
    ) throws URISyntaxException {
        log.debug("REST request to update LotFacture : {}, {}", id, lotFacture);
        if (lotFacture.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lotFacture.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lotFactureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LotFacture result = lotFactureService.save(lotFacture);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lotFacture.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /lot-factures/:id} : Partial updates given fields of an existing lotFacture, field will ignore if it is null
     *
     * @param id the id of the lotFacture to save.
     * @param lotFacture the lotFacture to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lotFacture,
     * or with status {@code 400 (Bad Request)} if the lotFacture is not valid,
     * or with status {@code 404 (Not Found)} if the lotFacture is not found,
     * or with status {@code 500 (Internal Server Error)} if the lotFacture couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/lot-factures/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LotFacture> partialUpdateLotFacture(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LotFacture lotFacture
    ) throws URISyntaxException {
        log.debug("REST request to partial update LotFacture partially : {}, {}", id, lotFacture);
        if (lotFacture.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lotFacture.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lotFactureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LotFacture> result = lotFactureService.partialUpdate(lotFacture);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lotFacture.getId().toString())
        );
    }

    /**
     * {@code GET  /lot-factures} : get all the lotFactures.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lotFactures in body.
     */
    @GetMapping("/lot-factures")
    public ResponseEntity<List<LotFacture>> getAllLotFactures(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of LotFactures");
        Page<LotFacture> page = lotFactureService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /lot-factures/:id} : get the "id" lotFacture.
     *
     * @param id the id of the lotFacture to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lotFacture, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/lot-factures/{id}")
    public ResponseEntity<LotFacture> getLotFacture(@PathVariable Long id) {
        log.debug("REST request to get LotFacture : {}", id);
        Optional<LotFacture> lotFacture = lotFactureService.findOne(id);
        return ResponseUtil.wrapOrNotFound(lotFacture);
    }

    /**
     * {@code DELETE  /lot-factures/:id} : delete the "id" lotFacture.
     *
     * @param id the id of the lotFacture to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/lot-factures/{id}")
    public ResponseEntity<Void> deleteLotFacture(@PathVariable Long id) {
        log.debug("REST request to delete LotFacture : {}", id);
        lotFactureService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
