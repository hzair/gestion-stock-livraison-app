package com.dss.gestion.web.rest;

import com.dss.gestion.domain.FactureVente;
import com.dss.gestion.repository.FactureVenteRepository;
import com.dss.gestion.service.FactureVenteService;
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
 * REST controller for managing {@link com.dss.gestion.domain.FactureVente}.
 */
@RestController
@RequestMapping("/api")
public class FactureVenteResource {

    private final Logger log = LoggerFactory.getLogger(FactureVenteResource.class);

    private static final String ENTITY_NAME = "factureVente";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FactureVenteService factureVenteService;

    private final FactureVenteRepository factureVenteRepository;

    public FactureVenteResource(FactureVenteService factureVenteService, FactureVenteRepository factureVenteRepository) {
        this.factureVenteService = factureVenteService;
        this.factureVenteRepository = factureVenteRepository;
    }

    /**
     * {@code POST  /facture-ventes} : Create a new factureVente.
     *
     * @param factureVente the factureVente to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new factureVente, or with status {@code 400 (Bad Request)} if the factureVente has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/facture-ventes")
    public ResponseEntity<FactureVente> createFactureVente(@Valid @RequestBody FactureVente factureVente) throws URISyntaxException {
        log.debug("REST request to save FactureVente : {}", factureVente);
        if (factureVente.getId() != null) {
            throw new BadRequestAlertException("A new factureVente cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FactureVente result = factureVenteService.save(factureVente);
        return ResponseEntity
            .created(new URI("/api/facture-ventes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /facture-ventes/:id} : Updates an existing factureVente.
     *
     * @param id the id of the factureVente to save.
     * @param factureVente the factureVente to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated factureVente,
     * or with status {@code 400 (Bad Request)} if the factureVente is not valid,
     * or with status {@code 500 (Internal Server Error)} if the factureVente couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/facture-ventes/{id}")
    public ResponseEntity<FactureVente> updateFactureVente(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FactureVente factureVente
    ) throws URISyntaxException {
        log.debug("REST request to update FactureVente : {}, {}", id, factureVente);
        if (factureVente.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, factureVente.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!factureVenteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FactureVente result = factureVenteService.save(factureVente);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, factureVente.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /facture-ventes/:id} : Partial updates given fields of an existing factureVente, field will ignore if it is null
     *
     * @param id the id of the factureVente to save.
     * @param factureVente the factureVente to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated factureVente,
     * or with status {@code 400 (Bad Request)} if the factureVente is not valid,
     * or with status {@code 404 (Not Found)} if the factureVente is not found,
     * or with status {@code 500 (Internal Server Error)} if the factureVente couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/facture-ventes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FactureVente> partialUpdateFactureVente(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FactureVente factureVente
    ) throws URISyntaxException {
        log.debug("REST request to partial update FactureVente partially : {}, {}", id, factureVente);
        if (factureVente.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, factureVente.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!factureVenteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FactureVente> result = factureVenteService.partialUpdate(factureVente);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, factureVente.getId().toString())
        );
    }

    /**
     * {@code GET  /facture-ventes} : get all the factureVentes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of factureVentes in body.
     */
    @GetMapping("/facture-ventes")
    public ResponseEntity<List<FactureVente>> getAllFactureVentes(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of FactureVentes");
        Page<FactureVente> page = factureVenteService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /facture-ventes/:id} : get the "id" factureVente.
     *
     * @param id the id of the factureVente to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the factureVente, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/facture-ventes/{id}")
    public ResponseEntity<FactureVente> getFactureVente(@PathVariable Long id) {
        log.debug("REST request to get FactureVente : {}", id);
        Optional<FactureVente> factureVente = factureVenteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(factureVente);
    }

    /**
     * {@code DELETE  /facture-ventes/:id} : delete the "id" factureVente.
     *
     * @param id the id of the factureVente to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/facture-ventes/{id}")
    public ResponseEntity<Void> deleteFactureVente(@PathVariable Long id) {
        log.debug("REST request to delete FactureVente : {}", id);
        factureVenteService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
