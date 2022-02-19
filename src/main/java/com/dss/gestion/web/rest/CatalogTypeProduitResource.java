package com.dss.gestion.web.rest;

import com.dss.gestion.domain.CatalogTypeProduit;
import com.dss.gestion.repository.CatalogTypeProduitRepository;
import com.dss.gestion.service.CatalogTypeProduitService;
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
 * REST controller for managing {@link com.dss.gestion.domain.CatalogTypeProduit}.
 */
@RestController
@RequestMapping("/api")
public class CatalogTypeProduitResource {

    private final Logger log = LoggerFactory.getLogger(CatalogTypeProduitResource.class);

    private static final String ENTITY_NAME = "catalogTypeProduit";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CatalogTypeProduitService catalogTypeProduitService;

    private final CatalogTypeProduitRepository catalogTypeProduitRepository;

    public CatalogTypeProduitResource(
        CatalogTypeProduitService catalogTypeProduitService,
        CatalogTypeProduitRepository catalogTypeProduitRepository
    ) {
        this.catalogTypeProduitService = catalogTypeProduitService;
        this.catalogTypeProduitRepository = catalogTypeProduitRepository;
    }

    /**
     * {@code POST  /catalog-type-produits} : Create a new catalogTypeProduit.
     *
     * @param catalogTypeProduit the catalogTypeProduit to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new catalogTypeProduit, or with status {@code 400 (Bad Request)} if the catalogTypeProduit has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/catalog-type-produits")
    public ResponseEntity<CatalogTypeProduit> createCatalogTypeProduit(@Valid @RequestBody CatalogTypeProduit catalogTypeProduit)
        throws URISyntaxException {
        log.debug("REST request to save CatalogTypeProduit : {}", catalogTypeProduit);
        if (catalogTypeProduit.getId() != null) {
            throw new BadRequestAlertException("A new catalogTypeProduit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CatalogTypeProduit result = catalogTypeProduitService.save(catalogTypeProduit);
        return ResponseEntity
            .created(new URI("/api/catalog-type-produits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /catalog-type-produits/:id} : Updates an existing catalogTypeProduit.
     *
     * @param id the id of the catalogTypeProduit to save.
     * @param catalogTypeProduit the catalogTypeProduit to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated catalogTypeProduit,
     * or with status {@code 400 (Bad Request)} if the catalogTypeProduit is not valid,
     * or with status {@code 500 (Internal Server Error)} if the catalogTypeProduit couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/catalog-type-produits/{id}")
    public ResponseEntity<CatalogTypeProduit> updateCatalogTypeProduit(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CatalogTypeProduit catalogTypeProduit
    ) throws URISyntaxException {
        log.debug("REST request to update CatalogTypeProduit : {}, {}", id, catalogTypeProduit);
        if (catalogTypeProduit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, catalogTypeProduit.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!catalogTypeProduitRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CatalogTypeProduit result = catalogTypeProduitService.save(catalogTypeProduit);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, catalogTypeProduit.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /catalog-type-produits/:id} : Partial updates given fields of an existing catalogTypeProduit, field will ignore if it is null
     *
     * @param id the id of the catalogTypeProduit to save.
     * @param catalogTypeProduit the catalogTypeProduit to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated catalogTypeProduit,
     * or with status {@code 400 (Bad Request)} if the catalogTypeProduit is not valid,
     * or with status {@code 404 (Not Found)} if the catalogTypeProduit is not found,
     * or with status {@code 500 (Internal Server Error)} if the catalogTypeProduit couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/catalog-type-produits/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CatalogTypeProduit> partialUpdateCatalogTypeProduit(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CatalogTypeProduit catalogTypeProduit
    ) throws URISyntaxException {
        log.debug("REST request to partial update CatalogTypeProduit partially : {}, {}", id, catalogTypeProduit);
        if (catalogTypeProduit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, catalogTypeProduit.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!catalogTypeProduitRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CatalogTypeProduit> result = catalogTypeProduitService.partialUpdate(catalogTypeProduit);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, catalogTypeProduit.getId().toString())
        );
    }

    /**
     * {@code GET  /catalog-type-produits} : get all the catalogTypeProduits.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of catalogTypeProduits in body.
     */
    @GetMapping("/catalog-type-produits")
    public ResponseEntity<List<CatalogTypeProduit>> getAllCatalogTypeProduits(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of CatalogTypeProduits");
        Page<CatalogTypeProduit> page = catalogTypeProduitService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /catalog-type-produits/:id} : get the "id" catalogTypeProduit.
     *
     * @param id the id of the catalogTypeProduit to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the catalogTypeProduit, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/catalog-type-produits/{id}")
    public ResponseEntity<CatalogTypeProduit> getCatalogTypeProduit(@PathVariable Long id) {
        log.debug("REST request to get CatalogTypeProduit : {}", id);
        Optional<CatalogTypeProduit> catalogTypeProduit = catalogTypeProduitService.findOne(id);
        return ResponseUtil.wrapOrNotFound(catalogTypeProduit);
    }

    /**
     * {@code DELETE  /catalog-type-produits/:id} : delete the "id" catalogTypeProduit.
     *
     * @param id the id of the catalogTypeProduit to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/catalog-type-produits/{id}")
    public ResponseEntity<Void> deleteCatalogTypeProduit(@PathVariable Long id) {
        log.debug("REST request to delete CatalogTypeProduit : {}", id);
        catalogTypeProduitService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
