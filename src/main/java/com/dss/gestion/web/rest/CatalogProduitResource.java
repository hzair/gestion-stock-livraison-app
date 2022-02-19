package com.dss.gestion.web.rest;

import com.dss.gestion.domain.CatalogProduit;
import com.dss.gestion.repository.CatalogProduitRepository;
import com.dss.gestion.service.CatalogProduitService;
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
 * REST controller for managing {@link com.dss.gestion.domain.CatalogProduit}.
 */
@RestController
@RequestMapping("/api")
public class CatalogProduitResource {

    private final Logger log = LoggerFactory.getLogger(CatalogProduitResource.class);

    private static final String ENTITY_NAME = "catalogProduit";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CatalogProduitService catalogProduitService;

    private final CatalogProduitRepository catalogProduitRepository;

    public CatalogProduitResource(CatalogProduitService catalogProduitService, CatalogProduitRepository catalogProduitRepository) {
        this.catalogProduitService = catalogProduitService;
        this.catalogProduitRepository = catalogProduitRepository;
    }

    /**
     * {@code POST  /catalog-produits} : Create a new catalogProduit.
     *
     * @param catalogProduit the catalogProduit to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new catalogProduit, or with status {@code 400 (Bad Request)} if the catalogProduit has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/catalog-produits")
    public ResponseEntity<CatalogProduit> createCatalogProduit(@Valid @RequestBody CatalogProduit catalogProduit)
        throws URISyntaxException {
        log.debug("REST request to save CatalogProduit : {}", catalogProduit);
        if (catalogProduit.getId() != null) {
            throw new BadRequestAlertException("A new catalogProduit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CatalogProduit result = catalogProduitService.save(catalogProduit);
        return ResponseEntity
            .created(new URI("/api/catalog-produits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /catalog-produits/:id} : Updates an existing catalogProduit.
     *
     * @param id the id of the catalogProduit to save.
     * @param catalogProduit the catalogProduit to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated catalogProduit,
     * or with status {@code 400 (Bad Request)} if the catalogProduit is not valid,
     * or with status {@code 500 (Internal Server Error)} if the catalogProduit couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/catalog-produits/{id}")
    public ResponseEntity<CatalogProduit> updateCatalogProduit(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CatalogProduit catalogProduit
    ) throws URISyntaxException {
        log.debug("REST request to update CatalogProduit : {}, {}", id, catalogProduit);
        if (catalogProduit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, catalogProduit.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!catalogProduitRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CatalogProduit result = catalogProduitService.save(catalogProduit);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, catalogProduit.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /catalog-produits/:id} : Partial updates given fields of an existing catalogProduit, field will ignore if it is null
     *
     * @param id the id of the catalogProduit to save.
     * @param catalogProduit the catalogProduit to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated catalogProduit,
     * or with status {@code 400 (Bad Request)} if the catalogProduit is not valid,
     * or with status {@code 404 (Not Found)} if the catalogProduit is not found,
     * or with status {@code 500 (Internal Server Error)} if the catalogProduit couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/catalog-produits/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CatalogProduit> partialUpdateCatalogProduit(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CatalogProduit catalogProduit
    ) throws URISyntaxException {
        log.debug("REST request to partial update CatalogProduit partially : {}, {}", id, catalogProduit);
        if (catalogProduit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, catalogProduit.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!catalogProduitRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CatalogProduit> result = catalogProduitService.partialUpdate(catalogProduit);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, catalogProduit.getId().toString())
        );
    }

    /**
     * {@code GET  /catalog-produits} : get all the catalogProduits.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of catalogProduits in body.
     */
    @GetMapping("/catalog-produits")
    public ResponseEntity<List<CatalogProduit>> getAllCatalogProduits(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of CatalogProduits");
        Page<CatalogProduit> page = catalogProduitService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /catalog-produits/:id} : get the "id" catalogProduit.
     *
     * @param id the id of the catalogProduit to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the catalogProduit, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/catalog-produits/{id}")
    public ResponseEntity<CatalogProduit> getCatalogProduit(@PathVariable Long id) {
        log.debug("REST request to get CatalogProduit : {}", id);
        Optional<CatalogProduit> catalogProduit = catalogProduitService.findOne(id);
        return ResponseUtil.wrapOrNotFound(catalogProduit);
    }

    /**
     * {@code DELETE  /catalog-produits/:id} : delete the "id" catalogProduit.
     *
     * @param id the id of the catalogProduit to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/catalog-produits/{id}")
    public ResponseEntity<Void> deleteCatalogProduit(@PathVariable Long id) {
        log.debug("REST request to delete CatalogProduit : {}", id);
        catalogProduitService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
