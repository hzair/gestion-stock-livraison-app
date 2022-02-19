package com.dss.gestion.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.dss.gestion.IntegrationTest;
import com.dss.gestion.domain.Camion;
import com.dss.gestion.domain.CatalogProduit;
import com.dss.gestion.domain.LotCamion;
import com.dss.gestion.repository.LotCamionRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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

/**
 * Integration tests for the {@link LotCamionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LotCamionResourceIT {

    private static final Integer DEFAULT_QUANTITE = 1;
    private static final Integer UPDATED_QUANTITE = 2;

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_MONTANT_TOTAL = 1L;
    private static final Long UPDATED_MONTANT_TOTAL = 2L;

    private static final String ENTITY_API_URL = "/api/lot-camions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LotCamionRepository lotCamionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLotCamionMockMvc;

    private LotCamion lotCamion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LotCamion createEntity(EntityManager em) {
        LotCamion lotCamion = new LotCamion().quantite(DEFAULT_QUANTITE).date(DEFAULT_DATE).montantTotal(DEFAULT_MONTANT_TOTAL);
        // Add required entity
        CatalogProduit catalogProduit;
        if (TestUtil.findAll(em, CatalogProduit.class).isEmpty()) {
            catalogProduit = CatalogProduitResourceIT.createEntity(em);
            em.persist(catalogProduit);
            em.flush();
        } else {
            catalogProduit = TestUtil.findAll(em, CatalogProduit.class).get(0);
        }
        lotCamion.setProduit(catalogProduit);
        // Add required entity
        Camion camion;
        if (TestUtil.findAll(em, Camion.class).isEmpty()) {
            camion = CamionResourceIT.createEntity(em);
            em.persist(camion);
            em.flush();
        } else {
            camion = TestUtil.findAll(em, Camion.class).get(0);
        }
        lotCamion.setCamion(camion);
        return lotCamion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LotCamion createUpdatedEntity(EntityManager em) {
        LotCamion lotCamion = new LotCamion().quantite(UPDATED_QUANTITE).date(UPDATED_DATE).montantTotal(UPDATED_MONTANT_TOTAL);
        // Add required entity
        CatalogProduit catalogProduit;
        if (TestUtil.findAll(em, CatalogProduit.class).isEmpty()) {
            catalogProduit = CatalogProduitResourceIT.createUpdatedEntity(em);
            em.persist(catalogProduit);
            em.flush();
        } else {
            catalogProduit = TestUtil.findAll(em, CatalogProduit.class).get(0);
        }
        lotCamion.setProduit(catalogProduit);
        // Add required entity
        Camion camion;
        if (TestUtil.findAll(em, Camion.class).isEmpty()) {
            camion = CamionResourceIT.createUpdatedEntity(em);
            em.persist(camion);
            em.flush();
        } else {
            camion = TestUtil.findAll(em, Camion.class).get(0);
        }
        lotCamion.setCamion(camion);
        return lotCamion;
    }

    @BeforeEach
    public void initTest() {
        lotCamion = createEntity(em);
    }

    @Test
    @Transactional
    void createLotCamion() throws Exception {
        int databaseSizeBeforeCreate = lotCamionRepository.findAll().size();
        // Create the LotCamion
        restLotCamionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lotCamion))
            )
            .andExpect(status().isCreated());

        // Validate the LotCamion in the database
        List<LotCamion> lotCamionList = lotCamionRepository.findAll();
        assertThat(lotCamionList).hasSize(databaseSizeBeforeCreate + 1);
        LotCamion testLotCamion = lotCamionList.get(lotCamionList.size() - 1);
        assertThat(testLotCamion.getQuantite()).isEqualTo(DEFAULT_QUANTITE);
        assertThat(testLotCamion.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testLotCamion.getMontantTotal()).isEqualTo(DEFAULT_MONTANT_TOTAL);
    }

    @Test
    @Transactional
    void createLotCamionWithExistingId() throws Exception {
        // Create the LotCamion with an existing ID
        lotCamion.setId(1L);

        int databaseSizeBeforeCreate = lotCamionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLotCamionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lotCamion))
            )
            .andExpect(status().isBadRequest());

        // Validate the LotCamion in the database
        List<LotCamion> lotCamionList = lotCamionRepository.findAll();
        assertThat(lotCamionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLotCamions() throws Exception {
        // Initialize the database
        lotCamionRepository.saveAndFlush(lotCamion);

        // Get all the lotCamionList
        restLotCamionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lotCamion.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantite").value(hasItem(DEFAULT_QUANTITE)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].montantTotal").value(hasItem(DEFAULT_MONTANT_TOTAL.intValue())));
    }

    @Test
    @Transactional
    void getLotCamion() throws Exception {
        // Initialize the database
        lotCamionRepository.saveAndFlush(lotCamion);

        // Get the lotCamion
        restLotCamionMockMvc
            .perform(get(ENTITY_API_URL_ID, lotCamion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(lotCamion.getId().intValue()))
            .andExpect(jsonPath("$.quantite").value(DEFAULT_QUANTITE))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.montantTotal").value(DEFAULT_MONTANT_TOTAL.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingLotCamion() throws Exception {
        // Get the lotCamion
        restLotCamionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLotCamion() throws Exception {
        // Initialize the database
        lotCamionRepository.saveAndFlush(lotCamion);

        int databaseSizeBeforeUpdate = lotCamionRepository.findAll().size();

        // Update the lotCamion
        LotCamion updatedLotCamion = lotCamionRepository.findById(lotCamion.getId()).get();
        // Disconnect from session so that the updates on updatedLotCamion are not directly saved in db
        em.detach(updatedLotCamion);
        updatedLotCamion.quantite(UPDATED_QUANTITE).date(UPDATED_DATE).montantTotal(UPDATED_MONTANT_TOTAL);

        restLotCamionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLotCamion.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLotCamion))
            )
            .andExpect(status().isOk());

        // Validate the LotCamion in the database
        List<LotCamion> lotCamionList = lotCamionRepository.findAll();
        assertThat(lotCamionList).hasSize(databaseSizeBeforeUpdate);
        LotCamion testLotCamion = lotCamionList.get(lotCamionList.size() - 1);
        assertThat(testLotCamion.getQuantite()).isEqualTo(UPDATED_QUANTITE);
        assertThat(testLotCamion.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testLotCamion.getMontantTotal()).isEqualTo(UPDATED_MONTANT_TOTAL);
    }

    @Test
    @Transactional
    void putNonExistingLotCamion() throws Exception {
        int databaseSizeBeforeUpdate = lotCamionRepository.findAll().size();
        lotCamion.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLotCamionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, lotCamion.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lotCamion))
            )
            .andExpect(status().isBadRequest());

        // Validate the LotCamion in the database
        List<LotCamion> lotCamionList = lotCamionRepository.findAll();
        assertThat(lotCamionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLotCamion() throws Exception {
        int databaseSizeBeforeUpdate = lotCamionRepository.findAll().size();
        lotCamion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLotCamionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lotCamion))
            )
            .andExpect(status().isBadRequest());

        // Validate the LotCamion in the database
        List<LotCamion> lotCamionList = lotCamionRepository.findAll();
        assertThat(lotCamionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLotCamion() throws Exception {
        int databaseSizeBeforeUpdate = lotCamionRepository.findAll().size();
        lotCamion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLotCamionMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lotCamion))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LotCamion in the database
        List<LotCamion> lotCamionList = lotCamionRepository.findAll();
        assertThat(lotCamionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLotCamionWithPatch() throws Exception {
        // Initialize the database
        lotCamionRepository.saveAndFlush(lotCamion);

        int databaseSizeBeforeUpdate = lotCamionRepository.findAll().size();

        // Update the lotCamion using partial update
        LotCamion partialUpdatedLotCamion = new LotCamion();
        partialUpdatedLotCamion.setId(lotCamion.getId());

        restLotCamionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLotCamion.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLotCamion))
            )
            .andExpect(status().isOk());

        // Validate the LotCamion in the database
        List<LotCamion> lotCamionList = lotCamionRepository.findAll();
        assertThat(lotCamionList).hasSize(databaseSizeBeforeUpdate);
        LotCamion testLotCamion = lotCamionList.get(lotCamionList.size() - 1);
        assertThat(testLotCamion.getQuantite()).isEqualTo(DEFAULT_QUANTITE);
        assertThat(testLotCamion.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testLotCamion.getMontantTotal()).isEqualTo(DEFAULT_MONTANT_TOTAL);
    }

    @Test
    @Transactional
    void fullUpdateLotCamionWithPatch() throws Exception {
        // Initialize the database
        lotCamionRepository.saveAndFlush(lotCamion);

        int databaseSizeBeforeUpdate = lotCamionRepository.findAll().size();

        // Update the lotCamion using partial update
        LotCamion partialUpdatedLotCamion = new LotCamion();
        partialUpdatedLotCamion.setId(lotCamion.getId());

        partialUpdatedLotCamion.quantite(UPDATED_QUANTITE).date(UPDATED_DATE).montantTotal(UPDATED_MONTANT_TOTAL);

        restLotCamionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLotCamion.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLotCamion))
            )
            .andExpect(status().isOk());

        // Validate the LotCamion in the database
        List<LotCamion> lotCamionList = lotCamionRepository.findAll();
        assertThat(lotCamionList).hasSize(databaseSizeBeforeUpdate);
        LotCamion testLotCamion = lotCamionList.get(lotCamionList.size() - 1);
        assertThat(testLotCamion.getQuantite()).isEqualTo(UPDATED_QUANTITE);
        assertThat(testLotCamion.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testLotCamion.getMontantTotal()).isEqualTo(UPDATED_MONTANT_TOTAL);
    }

    @Test
    @Transactional
    void patchNonExistingLotCamion() throws Exception {
        int databaseSizeBeforeUpdate = lotCamionRepository.findAll().size();
        lotCamion.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLotCamionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, lotCamion.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lotCamion))
            )
            .andExpect(status().isBadRequest());

        // Validate the LotCamion in the database
        List<LotCamion> lotCamionList = lotCamionRepository.findAll();
        assertThat(lotCamionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLotCamion() throws Exception {
        int databaseSizeBeforeUpdate = lotCamionRepository.findAll().size();
        lotCamion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLotCamionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lotCamion))
            )
            .andExpect(status().isBadRequest());

        // Validate the LotCamion in the database
        List<LotCamion> lotCamionList = lotCamionRepository.findAll();
        assertThat(lotCamionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLotCamion() throws Exception {
        int databaseSizeBeforeUpdate = lotCamionRepository.findAll().size();
        lotCamion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLotCamionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lotCamion))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LotCamion in the database
        List<LotCamion> lotCamionList = lotCamionRepository.findAll();
        assertThat(lotCamionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLotCamion() throws Exception {
        // Initialize the database
        lotCamionRepository.saveAndFlush(lotCamion);

        int databaseSizeBeforeDelete = lotCamionRepository.findAll().size();

        // Delete the lotCamion
        restLotCamionMockMvc
            .perform(delete(ENTITY_API_URL_ID, lotCamion.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LotCamion> lotCamionList = lotCamionRepository.findAll();
        assertThat(lotCamionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
