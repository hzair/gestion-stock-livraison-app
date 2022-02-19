package com.dss.gestion.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.dss.gestion.IntegrationTest;
import com.dss.gestion.domain.CatalogProduit;
import com.dss.gestion.domain.LotFacture;
import com.dss.gestion.repository.LotFactureRepository;
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
 * Integration tests for the {@link LotFactureResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LotFactureResourceIT {

    private static final Integer DEFAULT_QUANTITE = 1;
    private static final Integer UPDATED_QUANTITE = 2;

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_MONTANT_TOTAL = 1L;
    private static final Long UPDATED_MONTANT_TOTAL = 2L;

    private static final String ENTITY_API_URL = "/api/lot-factures";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LotFactureRepository lotFactureRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLotFactureMockMvc;

    private LotFacture lotFacture;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LotFacture createEntity(EntityManager em) {
        LotFacture lotFacture = new LotFacture().quantite(DEFAULT_QUANTITE).date(DEFAULT_DATE).montantTotal(DEFAULT_MONTANT_TOTAL);
        // Add required entity
        CatalogProduit catalogProduit;
        if (TestUtil.findAll(em, CatalogProduit.class).isEmpty()) {
            catalogProduit = CatalogProduitResourceIT.createEntity(em);
            em.persist(catalogProduit);
            em.flush();
        } else {
            catalogProduit = TestUtil.findAll(em, CatalogProduit.class).get(0);
        }
        lotFacture.setProduit(catalogProduit);
        return lotFacture;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LotFacture createUpdatedEntity(EntityManager em) {
        LotFacture lotFacture = new LotFacture().quantite(UPDATED_QUANTITE).date(UPDATED_DATE).montantTotal(UPDATED_MONTANT_TOTAL);
        // Add required entity
        CatalogProduit catalogProduit;
        if (TestUtil.findAll(em, CatalogProduit.class).isEmpty()) {
            catalogProduit = CatalogProduitResourceIT.createUpdatedEntity(em);
            em.persist(catalogProduit);
            em.flush();
        } else {
            catalogProduit = TestUtil.findAll(em, CatalogProduit.class).get(0);
        }
        lotFacture.setProduit(catalogProduit);
        return lotFacture;
    }

    @BeforeEach
    public void initTest() {
        lotFacture = createEntity(em);
    }

    @Test
    @Transactional
    void createLotFacture() throws Exception {
        int databaseSizeBeforeCreate = lotFactureRepository.findAll().size();
        // Create the LotFacture
        restLotFactureMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lotFacture))
            )
            .andExpect(status().isCreated());

        // Validate the LotFacture in the database
        List<LotFacture> lotFactureList = lotFactureRepository.findAll();
        assertThat(lotFactureList).hasSize(databaseSizeBeforeCreate + 1);
        LotFacture testLotFacture = lotFactureList.get(lotFactureList.size() - 1);
        assertThat(testLotFacture.getQuantite()).isEqualTo(DEFAULT_QUANTITE);
        assertThat(testLotFacture.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testLotFacture.getMontantTotal()).isEqualTo(DEFAULT_MONTANT_TOTAL);
    }

    @Test
    @Transactional
    void createLotFactureWithExistingId() throws Exception {
        // Create the LotFacture with an existing ID
        lotFacture.setId(1L);

        int databaseSizeBeforeCreate = lotFactureRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLotFactureMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lotFacture))
            )
            .andExpect(status().isBadRequest());

        // Validate the LotFacture in the database
        List<LotFacture> lotFactureList = lotFactureRepository.findAll();
        assertThat(lotFactureList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLotFactures() throws Exception {
        // Initialize the database
        lotFactureRepository.saveAndFlush(lotFacture);

        // Get all the lotFactureList
        restLotFactureMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lotFacture.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantite").value(hasItem(DEFAULT_QUANTITE)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].montantTotal").value(hasItem(DEFAULT_MONTANT_TOTAL.intValue())));
    }

    @Test
    @Transactional
    void getLotFacture() throws Exception {
        // Initialize the database
        lotFactureRepository.saveAndFlush(lotFacture);

        // Get the lotFacture
        restLotFactureMockMvc
            .perform(get(ENTITY_API_URL_ID, lotFacture.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(lotFacture.getId().intValue()))
            .andExpect(jsonPath("$.quantite").value(DEFAULT_QUANTITE))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.montantTotal").value(DEFAULT_MONTANT_TOTAL.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingLotFacture() throws Exception {
        // Get the lotFacture
        restLotFactureMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLotFacture() throws Exception {
        // Initialize the database
        lotFactureRepository.saveAndFlush(lotFacture);

        int databaseSizeBeforeUpdate = lotFactureRepository.findAll().size();

        // Update the lotFacture
        LotFacture updatedLotFacture = lotFactureRepository.findById(lotFacture.getId()).get();
        // Disconnect from session so that the updates on updatedLotFacture are not directly saved in db
        em.detach(updatedLotFacture);
        updatedLotFacture.quantite(UPDATED_QUANTITE).date(UPDATED_DATE).montantTotal(UPDATED_MONTANT_TOTAL);

        restLotFactureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLotFacture.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLotFacture))
            )
            .andExpect(status().isOk());

        // Validate the LotFacture in the database
        List<LotFacture> lotFactureList = lotFactureRepository.findAll();
        assertThat(lotFactureList).hasSize(databaseSizeBeforeUpdate);
        LotFacture testLotFacture = lotFactureList.get(lotFactureList.size() - 1);
        assertThat(testLotFacture.getQuantite()).isEqualTo(UPDATED_QUANTITE);
        assertThat(testLotFacture.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testLotFacture.getMontantTotal()).isEqualTo(UPDATED_MONTANT_TOTAL);
    }

    @Test
    @Transactional
    void putNonExistingLotFacture() throws Exception {
        int databaseSizeBeforeUpdate = lotFactureRepository.findAll().size();
        lotFacture.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLotFactureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, lotFacture.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lotFacture))
            )
            .andExpect(status().isBadRequest());

        // Validate the LotFacture in the database
        List<LotFacture> lotFactureList = lotFactureRepository.findAll();
        assertThat(lotFactureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLotFacture() throws Exception {
        int databaseSizeBeforeUpdate = lotFactureRepository.findAll().size();
        lotFacture.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLotFactureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lotFacture))
            )
            .andExpect(status().isBadRequest());

        // Validate the LotFacture in the database
        List<LotFacture> lotFactureList = lotFactureRepository.findAll();
        assertThat(lotFactureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLotFacture() throws Exception {
        int databaseSizeBeforeUpdate = lotFactureRepository.findAll().size();
        lotFacture.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLotFactureMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lotFacture))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LotFacture in the database
        List<LotFacture> lotFactureList = lotFactureRepository.findAll();
        assertThat(lotFactureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLotFactureWithPatch() throws Exception {
        // Initialize the database
        lotFactureRepository.saveAndFlush(lotFacture);

        int databaseSizeBeforeUpdate = lotFactureRepository.findAll().size();

        // Update the lotFacture using partial update
        LotFacture partialUpdatedLotFacture = new LotFacture();
        partialUpdatedLotFacture.setId(lotFacture.getId());

        partialUpdatedLotFacture.date(UPDATED_DATE);

        restLotFactureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLotFacture.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLotFacture))
            )
            .andExpect(status().isOk());

        // Validate the LotFacture in the database
        List<LotFacture> lotFactureList = lotFactureRepository.findAll();
        assertThat(lotFactureList).hasSize(databaseSizeBeforeUpdate);
        LotFacture testLotFacture = lotFactureList.get(lotFactureList.size() - 1);
        assertThat(testLotFacture.getQuantite()).isEqualTo(DEFAULT_QUANTITE);
        assertThat(testLotFacture.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testLotFacture.getMontantTotal()).isEqualTo(DEFAULT_MONTANT_TOTAL);
    }

    @Test
    @Transactional
    void fullUpdateLotFactureWithPatch() throws Exception {
        // Initialize the database
        lotFactureRepository.saveAndFlush(lotFacture);

        int databaseSizeBeforeUpdate = lotFactureRepository.findAll().size();

        // Update the lotFacture using partial update
        LotFacture partialUpdatedLotFacture = new LotFacture();
        partialUpdatedLotFacture.setId(lotFacture.getId());

        partialUpdatedLotFacture.quantite(UPDATED_QUANTITE).date(UPDATED_DATE).montantTotal(UPDATED_MONTANT_TOTAL);

        restLotFactureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLotFacture.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLotFacture))
            )
            .andExpect(status().isOk());

        // Validate the LotFacture in the database
        List<LotFacture> lotFactureList = lotFactureRepository.findAll();
        assertThat(lotFactureList).hasSize(databaseSizeBeforeUpdate);
        LotFacture testLotFacture = lotFactureList.get(lotFactureList.size() - 1);
        assertThat(testLotFacture.getQuantite()).isEqualTo(UPDATED_QUANTITE);
        assertThat(testLotFacture.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testLotFacture.getMontantTotal()).isEqualTo(UPDATED_MONTANT_TOTAL);
    }

    @Test
    @Transactional
    void patchNonExistingLotFacture() throws Exception {
        int databaseSizeBeforeUpdate = lotFactureRepository.findAll().size();
        lotFacture.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLotFactureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, lotFacture.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lotFacture))
            )
            .andExpect(status().isBadRequest());

        // Validate the LotFacture in the database
        List<LotFacture> lotFactureList = lotFactureRepository.findAll();
        assertThat(lotFactureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLotFacture() throws Exception {
        int databaseSizeBeforeUpdate = lotFactureRepository.findAll().size();
        lotFacture.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLotFactureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lotFacture))
            )
            .andExpect(status().isBadRequest());

        // Validate the LotFacture in the database
        List<LotFacture> lotFactureList = lotFactureRepository.findAll();
        assertThat(lotFactureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLotFacture() throws Exception {
        int databaseSizeBeforeUpdate = lotFactureRepository.findAll().size();
        lotFacture.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLotFactureMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lotFacture))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LotFacture in the database
        List<LotFacture> lotFactureList = lotFactureRepository.findAll();
        assertThat(lotFactureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLotFacture() throws Exception {
        // Initialize the database
        lotFactureRepository.saveAndFlush(lotFacture);

        int databaseSizeBeforeDelete = lotFactureRepository.findAll().size();

        // Delete the lotFacture
        restLotFactureMockMvc
            .perform(delete(ENTITY_API_URL_ID, lotFacture.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LotFacture> lotFactureList = lotFactureRepository.findAll();
        assertThat(lotFactureList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
