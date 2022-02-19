package com.dss.gestion.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.dss.gestion.IntegrationTest;
import com.dss.gestion.domain.CatalogTypeProduit;
import com.dss.gestion.repository.CatalogTypeProduitRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link CatalogTypeProduitResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CatalogTypeProduitResourceIT {

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_ID_FONC = "AAAAAAAAAA";
    private static final String UPDATED_ID_FONC = "BBBBBBBBBB";

    private static final String DEFAULT_DESIGNATION = "AAAAAAAAAA";
    private static final String UPDATED_DESIGNATION = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/catalog-type-produits";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CatalogTypeProduitRepository catalogTypeProduitRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCatalogTypeProduitMockMvc;

    private CatalogTypeProduit catalogTypeProduit;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CatalogTypeProduit createEntity(EntityManager em) {
        CatalogTypeProduit catalogTypeProduit = new CatalogTypeProduit()
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
            .idFonc(DEFAULT_ID_FONC)
            .designation(DEFAULT_DESIGNATION)
            .description(DEFAULT_DESCRIPTION);
        return catalogTypeProduit;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CatalogTypeProduit createUpdatedEntity(EntityManager em) {
        CatalogTypeProduit catalogTypeProduit = new CatalogTypeProduit()
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .idFonc(UPDATED_ID_FONC)
            .designation(UPDATED_DESIGNATION)
            .description(UPDATED_DESCRIPTION);
        return catalogTypeProduit;
    }

    @BeforeEach
    public void initTest() {
        catalogTypeProduit = createEntity(em);
    }

    @Test
    @Transactional
    void createCatalogTypeProduit() throws Exception {
        int databaseSizeBeforeCreate = catalogTypeProduitRepository.findAll().size();
        // Create the CatalogTypeProduit
        restCatalogTypeProduitMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(catalogTypeProduit))
            )
            .andExpect(status().isCreated());

        // Validate the CatalogTypeProduit in the database
        List<CatalogTypeProduit> catalogTypeProduitList = catalogTypeProduitRepository.findAll();
        assertThat(catalogTypeProduitList).hasSize(databaseSizeBeforeCreate + 1);
        CatalogTypeProduit testCatalogTypeProduit = catalogTypeProduitList.get(catalogTypeProduitList.size() - 1);
        assertThat(testCatalogTypeProduit.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testCatalogTypeProduit.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testCatalogTypeProduit.getIdFonc()).isEqualTo(DEFAULT_ID_FONC);
        assertThat(testCatalogTypeProduit.getDesignation()).isEqualTo(DEFAULT_DESIGNATION);
        assertThat(testCatalogTypeProduit.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createCatalogTypeProduitWithExistingId() throws Exception {
        // Create the CatalogTypeProduit with an existing ID
        catalogTypeProduit.setId(1L);

        int databaseSizeBeforeCreate = catalogTypeProduitRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCatalogTypeProduitMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(catalogTypeProduit))
            )
            .andExpect(status().isBadRequest());

        // Validate the CatalogTypeProduit in the database
        List<CatalogTypeProduit> catalogTypeProduitList = catalogTypeProduitRepository.findAll();
        assertThat(catalogTypeProduitList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDesignationIsRequired() throws Exception {
        int databaseSizeBeforeTest = catalogTypeProduitRepository.findAll().size();
        // set the field null
        catalogTypeProduit.setDesignation(null);

        // Create the CatalogTypeProduit, which fails.

        restCatalogTypeProduitMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(catalogTypeProduit))
            )
            .andExpect(status().isBadRequest());

        List<CatalogTypeProduit> catalogTypeProduitList = catalogTypeProduitRepository.findAll();
        assertThat(catalogTypeProduitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCatalogTypeProduits() throws Exception {
        // Initialize the database
        catalogTypeProduitRepository.saveAndFlush(catalogTypeProduit);

        // Get all the catalogTypeProduitList
        restCatalogTypeProduitMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(catalogTypeProduit.getId().intValue())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].idFonc").value(hasItem(DEFAULT_ID_FONC)))
            .andExpect(jsonPath("$.[*].designation").value(hasItem(DEFAULT_DESIGNATION)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getCatalogTypeProduit() throws Exception {
        // Initialize the database
        catalogTypeProduitRepository.saveAndFlush(catalogTypeProduit);

        // Get the catalogTypeProduit
        restCatalogTypeProduitMockMvc
            .perform(get(ENTITY_API_URL_ID, catalogTypeProduit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(catalogTypeProduit.getId().intValue()))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.idFonc").value(DEFAULT_ID_FONC))
            .andExpect(jsonPath("$.designation").value(DEFAULT_DESIGNATION))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingCatalogTypeProduit() throws Exception {
        // Get the catalogTypeProduit
        restCatalogTypeProduitMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCatalogTypeProduit() throws Exception {
        // Initialize the database
        catalogTypeProduitRepository.saveAndFlush(catalogTypeProduit);

        int databaseSizeBeforeUpdate = catalogTypeProduitRepository.findAll().size();

        // Update the catalogTypeProduit
        CatalogTypeProduit updatedCatalogTypeProduit = catalogTypeProduitRepository.findById(catalogTypeProduit.getId()).get();
        // Disconnect from session so that the updates on updatedCatalogTypeProduit are not directly saved in db
        em.detach(updatedCatalogTypeProduit);
        updatedCatalogTypeProduit
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .idFonc(UPDATED_ID_FONC)
            .designation(UPDATED_DESIGNATION)
            .description(UPDATED_DESCRIPTION);

        restCatalogTypeProduitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCatalogTypeProduit.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCatalogTypeProduit))
            )
            .andExpect(status().isOk());

        // Validate the CatalogTypeProduit in the database
        List<CatalogTypeProduit> catalogTypeProduitList = catalogTypeProduitRepository.findAll();
        assertThat(catalogTypeProduitList).hasSize(databaseSizeBeforeUpdate);
        CatalogTypeProduit testCatalogTypeProduit = catalogTypeProduitList.get(catalogTypeProduitList.size() - 1);
        assertThat(testCatalogTypeProduit.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testCatalogTypeProduit.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testCatalogTypeProduit.getIdFonc()).isEqualTo(UPDATED_ID_FONC);
        assertThat(testCatalogTypeProduit.getDesignation()).isEqualTo(UPDATED_DESIGNATION);
        assertThat(testCatalogTypeProduit.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingCatalogTypeProduit() throws Exception {
        int databaseSizeBeforeUpdate = catalogTypeProduitRepository.findAll().size();
        catalogTypeProduit.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCatalogTypeProduitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, catalogTypeProduit.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(catalogTypeProduit))
            )
            .andExpect(status().isBadRequest());

        // Validate the CatalogTypeProduit in the database
        List<CatalogTypeProduit> catalogTypeProduitList = catalogTypeProduitRepository.findAll();
        assertThat(catalogTypeProduitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCatalogTypeProduit() throws Exception {
        int databaseSizeBeforeUpdate = catalogTypeProduitRepository.findAll().size();
        catalogTypeProduit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatalogTypeProduitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(catalogTypeProduit))
            )
            .andExpect(status().isBadRequest());

        // Validate the CatalogTypeProduit in the database
        List<CatalogTypeProduit> catalogTypeProduitList = catalogTypeProduitRepository.findAll();
        assertThat(catalogTypeProduitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCatalogTypeProduit() throws Exception {
        int databaseSizeBeforeUpdate = catalogTypeProduitRepository.findAll().size();
        catalogTypeProduit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatalogTypeProduitMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(catalogTypeProduit))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CatalogTypeProduit in the database
        List<CatalogTypeProduit> catalogTypeProduitList = catalogTypeProduitRepository.findAll();
        assertThat(catalogTypeProduitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCatalogTypeProduitWithPatch() throws Exception {
        // Initialize the database
        catalogTypeProduitRepository.saveAndFlush(catalogTypeProduit);

        int databaseSizeBeforeUpdate = catalogTypeProduitRepository.findAll().size();

        // Update the catalogTypeProduit using partial update
        CatalogTypeProduit partialUpdatedCatalogTypeProduit = new CatalogTypeProduit();
        partialUpdatedCatalogTypeProduit.setId(catalogTypeProduit.getId());

        partialUpdatedCatalogTypeProduit.idFonc(UPDATED_ID_FONC).designation(UPDATED_DESIGNATION);

        restCatalogTypeProduitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCatalogTypeProduit.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCatalogTypeProduit))
            )
            .andExpect(status().isOk());

        // Validate the CatalogTypeProduit in the database
        List<CatalogTypeProduit> catalogTypeProduitList = catalogTypeProduitRepository.findAll();
        assertThat(catalogTypeProduitList).hasSize(databaseSizeBeforeUpdate);
        CatalogTypeProduit testCatalogTypeProduit = catalogTypeProduitList.get(catalogTypeProduitList.size() - 1);
        assertThat(testCatalogTypeProduit.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testCatalogTypeProduit.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testCatalogTypeProduit.getIdFonc()).isEqualTo(UPDATED_ID_FONC);
        assertThat(testCatalogTypeProduit.getDesignation()).isEqualTo(UPDATED_DESIGNATION);
        assertThat(testCatalogTypeProduit.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateCatalogTypeProduitWithPatch() throws Exception {
        // Initialize the database
        catalogTypeProduitRepository.saveAndFlush(catalogTypeProduit);

        int databaseSizeBeforeUpdate = catalogTypeProduitRepository.findAll().size();

        // Update the catalogTypeProduit using partial update
        CatalogTypeProduit partialUpdatedCatalogTypeProduit = new CatalogTypeProduit();
        partialUpdatedCatalogTypeProduit.setId(catalogTypeProduit.getId());

        partialUpdatedCatalogTypeProduit
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .idFonc(UPDATED_ID_FONC)
            .designation(UPDATED_DESIGNATION)
            .description(UPDATED_DESCRIPTION);

        restCatalogTypeProduitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCatalogTypeProduit.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCatalogTypeProduit))
            )
            .andExpect(status().isOk());

        // Validate the CatalogTypeProduit in the database
        List<CatalogTypeProduit> catalogTypeProduitList = catalogTypeProduitRepository.findAll();
        assertThat(catalogTypeProduitList).hasSize(databaseSizeBeforeUpdate);
        CatalogTypeProduit testCatalogTypeProduit = catalogTypeProduitList.get(catalogTypeProduitList.size() - 1);
        assertThat(testCatalogTypeProduit.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testCatalogTypeProduit.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testCatalogTypeProduit.getIdFonc()).isEqualTo(UPDATED_ID_FONC);
        assertThat(testCatalogTypeProduit.getDesignation()).isEqualTo(UPDATED_DESIGNATION);
        assertThat(testCatalogTypeProduit.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingCatalogTypeProduit() throws Exception {
        int databaseSizeBeforeUpdate = catalogTypeProduitRepository.findAll().size();
        catalogTypeProduit.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCatalogTypeProduitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, catalogTypeProduit.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(catalogTypeProduit))
            )
            .andExpect(status().isBadRequest());

        // Validate the CatalogTypeProduit in the database
        List<CatalogTypeProduit> catalogTypeProduitList = catalogTypeProduitRepository.findAll();
        assertThat(catalogTypeProduitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCatalogTypeProduit() throws Exception {
        int databaseSizeBeforeUpdate = catalogTypeProduitRepository.findAll().size();
        catalogTypeProduit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatalogTypeProduitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(catalogTypeProduit))
            )
            .andExpect(status().isBadRequest());

        // Validate the CatalogTypeProduit in the database
        List<CatalogTypeProduit> catalogTypeProduitList = catalogTypeProduitRepository.findAll();
        assertThat(catalogTypeProduitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCatalogTypeProduit() throws Exception {
        int databaseSizeBeforeUpdate = catalogTypeProduitRepository.findAll().size();
        catalogTypeProduit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatalogTypeProduitMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(catalogTypeProduit))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CatalogTypeProduit in the database
        List<CatalogTypeProduit> catalogTypeProduitList = catalogTypeProduitRepository.findAll();
        assertThat(catalogTypeProduitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCatalogTypeProduit() throws Exception {
        // Initialize the database
        catalogTypeProduitRepository.saveAndFlush(catalogTypeProduit);

        int databaseSizeBeforeDelete = catalogTypeProduitRepository.findAll().size();

        // Delete the catalogTypeProduit
        restCatalogTypeProduitMockMvc
            .perform(delete(ENTITY_API_URL_ID, catalogTypeProduit.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CatalogTypeProduit> catalogTypeProduitList = catalogTypeProduitRepository.findAll();
        assertThat(catalogTypeProduitList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
