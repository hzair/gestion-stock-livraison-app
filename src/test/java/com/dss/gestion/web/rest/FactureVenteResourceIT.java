package com.dss.gestion.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.dss.gestion.IntegrationTest;
import com.dss.gestion.domain.Client;
import com.dss.gestion.domain.FactureVente;
import com.dss.gestion.domain.LotFacture;
import com.dss.gestion.repository.FactureVenteRepository;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link FactureVenteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FactureVenteResourceIT {

    private static final byte[] DEFAULT_PHOTO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PHOTO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PHOTO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PHOTO_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_ID_FONC = "AAAAAAAAAA";
    private static final String UPDATED_ID_FONC = "BBBBBBBBBB";

    private static final String DEFAULT_DESIGNATION = "AAAAAAAAAA";
    private static final String UPDATED_DESIGNATION = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Long DEFAULT_MONTANT = 1L;
    private static final Long UPDATED_MONTANT = 2L;

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_PAYEE = false;
    private static final Boolean UPDATED_PAYEE = true;

    private static final String ENTITY_API_URL = "/api/facture-ventes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FactureVenteRepository factureVenteRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFactureVenteMockMvc;

    private FactureVente factureVente;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FactureVente createEntity(EntityManager em) {
        FactureVente factureVente = new FactureVente()
            .photo(DEFAULT_PHOTO)
            .photoContentType(DEFAULT_PHOTO_CONTENT_TYPE)
            .idFonc(DEFAULT_ID_FONC)
            .designation(DEFAULT_DESIGNATION)
            .description(DEFAULT_DESCRIPTION)
            .montant(DEFAULT_MONTANT)
            .date(DEFAULT_DATE)
            .payee(DEFAULT_PAYEE);
        // Add required entity
        LotFacture lotFacture;
        if (TestUtil.findAll(em, LotFacture.class).isEmpty()) {
            lotFacture = LotFactureResourceIT.createEntity(em);
            em.persist(lotFacture);
            em.flush();
        } else {
            lotFacture = TestUtil.findAll(em, LotFacture.class).get(0);
        }
        factureVente.getProduits().add(lotFacture);
        // Add required entity
        Client client;
        if (TestUtil.findAll(em, Client.class).isEmpty()) {
            client = ClientResourceIT.createEntity(em);
            em.persist(client);
            em.flush();
        } else {
            client = TestUtil.findAll(em, Client.class).get(0);
        }
        factureVente.setClient(client);
        return factureVente;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FactureVente createUpdatedEntity(EntityManager em) {
        FactureVente factureVente = new FactureVente()
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE)
            .idFonc(UPDATED_ID_FONC)
            .designation(UPDATED_DESIGNATION)
            .description(UPDATED_DESCRIPTION)
            .montant(UPDATED_MONTANT)
            .date(UPDATED_DATE)
            .payee(UPDATED_PAYEE);
        // Add required entity
        LotFacture lotFacture;
        if (TestUtil.findAll(em, LotFacture.class).isEmpty()) {
            lotFacture = LotFactureResourceIT.createUpdatedEntity(em);
            em.persist(lotFacture);
            em.flush();
        } else {
            lotFacture = TestUtil.findAll(em, LotFacture.class).get(0);
        }
        factureVente.getProduits().add(lotFacture);
        // Add required entity
        Client client;
        if (TestUtil.findAll(em, Client.class).isEmpty()) {
            client = ClientResourceIT.createUpdatedEntity(em);
            em.persist(client);
            em.flush();
        } else {
            client = TestUtil.findAll(em, Client.class).get(0);
        }
        factureVente.setClient(client);
        return factureVente;
    }

    @BeforeEach
    public void initTest() {
        factureVente = createEntity(em);
    }

    @Test
    @Transactional
    void createFactureVente() throws Exception {
        int databaseSizeBeforeCreate = factureVenteRepository.findAll().size();
        // Create the FactureVente
        restFactureVenteMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(factureVente))
            )
            .andExpect(status().isCreated());

        // Validate the FactureVente in the database
        List<FactureVente> factureVenteList = factureVenteRepository.findAll();
        assertThat(factureVenteList).hasSize(databaseSizeBeforeCreate + 1);
        FactureVente testFactureVente = factureVenteList.get(factureVenteList.size() - 1);
        assertThat(testFactureVente.getPhoto()).isEqualTo(DEFAULT_PHOTO);
        assertThat(testFactureVente.getPhotoContentType()).isEqualTo(DEFAULT_PHOTO_CONTENT_TYPE);
        assertThat(testFactureVente.getIdFonc()).isEqualTo(DEFAULT_ID_FONC);
        assertThat(testFactureVente.getDesignation()).isEqualTo(DEFAULT_DESIGNATION);
        assertThat(testFactureVente.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testFactureVente.getMontant()).isEqualTo(DEFAULT_MONTANT);
        assertThat(testFactureVente.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testFactureVente.getPayee()).isEqualTo(DEFAULT_PAYEE);
    }

    @Test
    @Transactional
    void createFactureVenteWithExistingId() throws Exception {
        // Create the FactureVente with an existing ID
        factureVente.setId(1L);

        int databaseSizeBeforeCreate = factureVenteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFactureVenteMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(factureVente))
            )
            .andExpect(status().isBadRequest());

        // Validate the FactureVente in the database
        List<FactureVente> factureVenteList = factureVenteRepository.findAll();
        assertThat(factureVenteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDesignationIsRequired() throws Exception {
        int databaseSizeBeforeTest = factureVenteRepository.findAll().size();
        // set the field null
        factureVente.setDesignation(null);

        // Create the FactureVente, which fails.

        restFactureVenteMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(factureVente))
            )
            .andExpect(status().isBadRequest());

        List<FactureVente> factureVenteList = factureVenteRepository.findAll();
        assertThat(factureVenteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMontantIsRequired() throws Exception {
        int databaseSizeBeforeTest = factureVenteRepository.findAll().size();
        // set the field null
        factureVente.setMontant(null);

        // Create the FactureVente, which fails.

        restFactureVenteMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(factureVente))
            )
            .andExpect(status().isBadRequest());

        List<FactureVente> factureVenteList = factureVenteRepository.findAll();
        assertThat(factureVenteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFactureVentes() throws Exception {
        // Initialize the database
        factureVenteRepository.saveAndFlush(factureVente);

        // Get all the factureVenteList
        restFactureVenteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(factureVente.getId().intValue())))
            .andExpect(jsonPath("$.[*].photoContentType").value(hasItem(DEFAULT_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].photo").value(hasItem(Base64Utils.encodeToString(DEFAULT_PHOTO))))
            .andExpect(jsonPath("$.[*].idFonc").value(hasItem(DEFAULT_ID_FONC)))
            .andExpect(jsonPath("$.[*].designation").value(hasItem(DEFAULT_DESIGNATION)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].montant").value(hasItem(DEFAULT_MONTANT.intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].payee").value(hasItem(DEFAULT_PAYEE.booleanValue())));
    }

    @Test
    @Transactional
    void getFactureVente() throws Exception {
        // Initialize the database
        factureVenteRepository.saveAndFlush(factureVente);

        // Get the factureVente
        restFactureVenteMockMvc
            .perform(get(ENTITY_API_URL_ID, factureVente.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(factureVente.getId().intValue()))
            .andExpect(jsonPath("$.photoContentType").value(DEFAULT_PHOTO_CONTENT_TYPE))
            .andExpect(jsonPath("$.photo").value(Base64Utils.encodeToString(DEFAULT_PHOTO)))
            .andExpect(jsonPath("$.idFonc").value(DEFAULT_ID_FONC))
            .andExpect(jsonPath("$.designation").value(DEFAULT_DESIGNATION))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.montant").value(DEFAULT_MONTANT.intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.payee").value(DEFAULT_PAYEE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingFactureVente() throws Exception {
        // Get the factureVente
        restFactureVenteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFactureVente() throws Exception {
        // Initialize the database
        factureVenteRepository.saveAndFlush(factureVente);

        int databaseSizeBeforeUpdate = factureVenteRepository.findAll().size();

        // Update the factureVente
        FactureVente updatedFactureVente = factureVenteRepository.findById(factureVente.getId()).get();
        // Disconnect from session so that the updates on updatedFactureVente are not directly saved in db
        em.detach(updatedFactureVente);
        updatedFactureVente
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE)
            .idFonc(UPDATED_ID_FONC)
            .designation(UPDATED_DESIGNATION)
            .description(UPDATED_DESCRIPTION)
            .montant(UPDATED_MONTANT)
            .date(UPDATED_DATE)
            .payee(UPDATED_PAYEE);

        restFactureVenteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFactureVente.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFactureVente))
            )
            .andExpect(status().isOk());

        // Validate the FactureVente in the database
        List<FactureVente> factureVenteList = factureVenteRepository.findAll();
        assertThat(factureVenteList).hasSize(databaseSizeBeforeUpdate);
        FactureVente testFactureVente = factureVenteList.get(factureVenteList.size() - 1);
        assertThat(testFactureVente.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testFactureVente.getPhotoContentType()).isEqualTo(UPDATED_PHOTO_CONTENT_TYPE);
        assertThat(testFactureVente.getIdFonc()).isEqualTo(UPDATED_ID_FONC);
        assertThat(testFactureVente.getDesignation()).isEqualTo(UPDATED_DESIGNATION);
        assertThat(testFactureVente.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFactureVente.getMontant()).isEqualTo(UPDATED_MONTANT);
        assertThat(testFactureVente.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testFactureVente.getPayee()).isEqualTo(UPDATED_PAYEE);
    }

    @Test
    @Transactional
    void putNonExistingFactureVente() throws Exception {
        int databaseSizeBeforeUpdate = factureVenteRepository.findAll().size();
        factureVente.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFactureVenteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, factureVente.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(factureVente))
            )
            .andExpect(status().isBadRequest());

        // Validate the FactureVente in the database
        List<FactureVente> factureVenteList = factureVenteRepository.findAll();
        assertThat(factureVenteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFactureVente() throws Exception {
        int databaseSizeBeforeUpdate = factureVenteRepository.findAll().size();
        factureVente.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactureVenteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(factureVente))
            )
            .andExpect(status().isBadRequest());

        // Validate the FactureVente in the database
        List<FactureVente> factureVenteList = factureVenteRepository.findAll();
        assertThat(factureVenteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFactureVente() throws Exception {
        int databaseSizeBeforeUpdate = factureVenteRepository.findAll().size();
        factureVente.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactureVenteMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(factureVente))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FactureVente in the database
        List<FactureVente> factureVenteList = factureVenteRepository.findAll();
        assertThat(factureVenteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFactureVenteWithPatch() throws Exception {
        // Initialize the database
        factureVenteRepository.saveAndFlush(factureVente);

        int databaseSizeBeforeUpdate = factureVenteRepository.findAll().size();

        // Update the factureVente using partial update
        FactureVente partialUpdatedFactureVente = new FactureVente();
        partialUpdatedFactureVente.setId(factureVente.getId());

        partialUpdatedFactureVente
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE)
            .designation(UPDATED_DESIGNATION)
            .montant(UPDATED_MONTANT)
            .payee(UPDATED_PAYEE);

        restFactureVenteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFactureVente.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFactureVente))
            )
            .andExpect(status().isOk());

        // Validate the FactureVente in the database
        List<FactureVente> factureVenteList = factureVenteRepository.findAll();
        assertThat(factureVenteList).hasSize(databaseSizeBeforeUpdate);
        FactureVente testFactureVente = factureVenteList.get(factureVenteList.size() - 1);
        assertThat(testFactureVente.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testFactureVente.getPhotoContentType()).isEqualTo(UPDATED_PHOTO_CONTENT_TYPE);
        assertThat(testFactureVente.getIdFonc()).isEqualTo(DEFAULT_ID_FONC);
        assertThat(testFactureVente.getDesignation()).isEqualTo(UPDATED_DESIGNATION);
        assertThat(testFactureVente.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testFactureVente.getMontant()).isEqualTo(UPDATED_MONTANT);
        assertThat(testFactureVente.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testFactureVente.getPayee()).isEqualTo(UPDATED_PAYEE);
    }

    @Test
    @Transactional
    void fullUpdateFactureVenteWithPatch() throws Exception {
        // Initialize the database
        factureVenteRepository.saveAndFlush(factureVente);

        int databaseSizeBeforeUpdate = factureVenteRepository.findAll().size();

        // Update the factureVente using partial update
        FactureVente partialUpdatedFactureVente = new FactureVente();
        partialUpdatedFactureVente.setId(factureVente.getId());

        partialUpdatedFactureVente
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE)
            .idFonc(UPDATED_ID_FONC)
            .designation(UPDATED_DESIGNATION)
            .description(UPDATED_DESCRIPTION)
            .montant(UPDATED_MONTANT)
            .date(UPDATED_DATE)
            .payee(UPDATED_PAYEE);

        restFactureVenteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFactureVente.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFactureVente))
            )
            .andExpect(status().isOk());

        // Validate the FactureVente in the database
        List<FactureVente> factureVenteList = factureVenteRepository.findAll();
        assertThat(factureVenteList).hasSize(databaseSizeBeforeUpdate);
        FactureVente testFactureVente = factureVenteList.get(factureVenteList.size() - 1);
        assertThat(testFactureVente.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testFactureVente.getPhotoContentType()).isEqualTo(UPDATED_PHOTO_CONTENT_TYPE);
        assertThat(testFactureVente.getIdFonc()).isEqualTo(UPDATED_ID_FONC);
        assertThat(testFactureVente.getDesignation()).isEqualTo(UPDATED_DESIGNATION);
        assertThat(testFactureVente.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFactureVente.getMontant()).isEqualTo(UPDATED_MONTANT);
        assertThat(testFactureVente.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testFactureVente.getPayee()).isEqualTo(UPDATED_PAYEE);
    }

    @Test
    @Transactional
    void patchNonExistingFactureVente() throws Exception {
        int databaseSizeBeforeUpdate = factureVenteRepository.findAll().size();
        factureVente.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFactureVenteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, factureVente.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(factureVente))
            )
            .andExpect(status().isBadRequest());

        // Validate the FactureVente in the database
        List<FactureVente> factureVenteList = factureVenteRepository.findAll();
        assertThat(factureVenteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFactureVente() throws Exception {
        int databaseSizeBeforeUpdate = factureVenteRepository.findAll().size();
        factureVente.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactureVenteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(factureVente))
            )
            .andExpect(status().isBadRequest());

        // Validate the FactureVente in the database
        List<FactureVente> factureVenteList = factureVenteRepository.findAll();
        assertThat(factureVenteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFactureVente() throws Exception {
        int databaseSizeBeforeUpdate = factureVenteRepository.findAll().size();
        factureVente.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactureVenteMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(factureVente))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FactureVente in the database
        List<FactureVente> factureVenteList = factureVenteRepository.findAll();
        assertThat(factureVenteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFactureVente() throws Exception {
        // Initialize the database
        factureVenteRepository.saveAndFlush(factureVente);

        int databaseSizeBeforeDelete = factureVenteRepository.findAll().size();

        // Delete the factureVente
        restFactureVenteMockMvc
            .perform(delete(ENTITY_API_URL_ID, factureVente.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FactureVente> factureVenteList = factureVenteRepository.findAll();
        assertThat(factureVenteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
