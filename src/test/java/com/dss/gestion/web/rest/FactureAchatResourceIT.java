package com.dss.gestion.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.dss.gestion.IntegrationTest;
import com.dss.gestion.domain.FactureAchat;
import com.dss.gestion.domain.Fournisseur;
import com.dss.gestion.domain.LotFacture;
import com.dss.gestion.repository.FactureAchatRepository;
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
 * Integration tests for the {@link FactureAchatResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FactureAchatResourceIT {

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

    private static final String ENTITY_API_URL = "/api/facture-achats";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FactureAchatRepository factureAchatRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFactureAchatMockMvc;

    private FactureAchat factureAchat;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FactureAchat createEntity(EntityManager em) {
        FactureAchat factureAchat = new FactureAchat()
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
        factureAchat.getProduits().add(lotFacture);
        // Add required entity
        Fournisseur fournisseur;
        if (TestUtil.findAll(em, Fournisseur.class).isEmpty()) {
            fournisseur = FournisseurResourceIT.createEntity(em);
            em.persist(fournisseur);
            em.flush();
        } else {
            fournisseur = TestUtil.findAll(em, Fournisseur.class).get(0);
        }
        factureAchat.setFournisseur(fournisseur);
        return factureAchat;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FactureAchat createUpdatedEntity(EntityManager em) {
        FactureAchat factureAchat = new FactureAchat()
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
        factureAchat.getProduits().add(lotFacture);
        // Add required entity
        Fournisseur fournisseur;
        if (TestUtil.findAll(em, Fournisseur.class).isEmpty()) {
            fournisseur = FournisseurResourceIT.createUpdatedEntity(em);
            em.persist(fournisseur);
            em.flush();
        } else {
            fournisseur = TestUtil.findAll(em, Fournisseur.class).get(0);
        }
        factureAchat.setFournisseur(fournisseur);
        return factureAchat;
    }

    @BeforeEach
    public void initTest() {
        factureAchat = createEntity(em);
    }

    @Test
    @Transactional
    void createFactureAchat() throws Exception {
        int databaseSizeBeforeCreate = factureAchatRepository.findAll().size();
        // Create the FactureAchat
        restFactureAchatMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(factureAchat))
            )
            .andExpect(status().isCreated());

        // Validate the FactureAchat in the database
        List<FactureAchat> factureAchatList = factureAchatRepository.findAll();
        assertThat(factureAchatList).hasSize(databaseSizeBeforeCreate + 1);
        FactureAchat testFactureAchat = factureAchatList.get(factureAchatList.size() - 1);
        assertThat(testFactureAchat.getPhoto()).isEqualTo(DEFAULT_PHOTO);
        assertThat(testFactureAchat.getPhotoContentType()).isEqualTo(DEFAULT_PHOTO_CONTENT_TYPE);
        assertThat(testFactureAchat.getIdFonc()).isEqualTo(DEFAULT_ID_FONC);
        assertThat(testFactureAchat.getDesignation()).isEqualTo(DEFAULT_DESIGNATION);
        assertThat(testFactureAchat.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testFactureAchat.getMontant()).isEqualTo(DEFAULT_MONTANT);
        assertThat(testFactureAchat.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testFactureAchat.getPayee()).isEqualTo(DEFAULT_PAYEE);
    }

    @Test
    @Transactional
    void createFactureAchatWithExistingId() throws Exception {
        // Create the FactureAchat with an existing ID
        factureAchat.setId(1L);

        int databaseSizeBeforeCreate = factureAchatRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFactureAchatMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(factureAchat))
            )
            .andExpect(status().isBadRequest());

        // Validate the FactureAchat in the database
        List<FactureAchat> factureAchatList = factureAchatRepository.findAll();
        assertThat(factureAchatList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDesignationIsRequired() throws Exception {
        int databaseSizeBeforeTest = factureAchatRepository.findAll().size();
        // set the field null
        factureAchat.setDesignation(null);

        // Create the FactureAchat, which fails.

        restFactureAchatMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(factureAchat))
            )
            .andExpect(status().isBadRequest());

        List<FactureAchat> factureAchatList = factureAchatRepository.findAll();
        assertThat(factureAchatList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMontantIsRequired() throws Exception {
        int databaseSizeBeforeTest = factureAchatRepository.findAll().size();
        // set the field null
        factureAchat.setMontant(null);

        // Create the FactureAchat, which fails.

        restFactureAchatMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(factureAchat))
            )
            .andExpect(status().isBadRequest());

        List<FactureAchat> factureAchatList = factureAchatRepository.findAll();
        assertThat(factureAchatList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFactureAchats() throws Exception {
        // Initialize the database
        factureAchatRepository.saveAndFlush(factureAchat);

        // Get all the factureAchatList
        restFactureAchatMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(factureAchat.getId().intValue())))
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
    void getFactureAchat() throws Exception {
        // Initialize the database
        factureAchatRepository.saveAndFlush(factureAchat);

        // Get the factureAchat
        restFactureAchatMockMvc
            .perform(get(ENTITY_API_URL_ID, factureAchat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(factureAchat.getId().intValue()))
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
    void getNonExistingFactureAchat() throws Exception {
        // Get the factureAchat
        restFactureAchatMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFactureAchat() throws Exception {
        // Initialize the database
        factureAchatRepository.saveAndFlush(factureAchat);

        int databaseSizeBeforeUpdate = factureAchatRepository.findAll().size();

        // Update the factureAchat
        FactureAchat updatedFactureAchat = factureAchatRepository.findById(factureAchat.getId()).get();
        // Disconnect from session so that the updates on updatedFactureAchat are not directly saved in db
        em.detach(updatedFactureAchat);
        updatedFactureAchat
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE)
            .idFonc(UPDATED_ID_FONC)
            .designation(UPDATED_DESIGNATION)
            .description(UPDATED_DESCRIPTION)
            .montant(UPDATED_MONTANT)
            .date(UPDATED_DATE)
            .payee(UPDATED_PAYEE);

        restFactureAchatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFactureAchat.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFactureAchat))
            )
            .andExpect(status().isOk());

        // Validate the FactureAchat in the database
        List<FactureAchat> factureAchatList = factureAchatRepository.findAll();
        assertThat(factureAchatList).hasSize(databaseSizeBeforeUpdate);
        FactureAchat testFactureAchat = factureAchatList.get(factureAchatList.size() - 1);
        assertThat(testFactureAchat.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testFactureAchat.getPhotoContentType()).isEqualTo(UPDATED_PHOTO_CONTENT_TYPE);
        assertThat(testFactureAchat.getIdFonc()).isEqualTo(UPDATED_ID_FONC);
        assertThat(testFactureAchat.getDesignation()).isEqualTo(UPDATED_DESIGNATION);
        assertThat(testFactureAchat.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFactureAchat.getMontant()).isEqualTo(UPDATED_MONTANT);
        assertThat(testFactureAchat.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testFactureAchat.getPayee()).isEqualTo(UPDATED_PAYEE);
    }

    @Test
    @Transactional
    void putNonExistingFactureAchat() throws Exception {
        int databaseSizeBeforeUpdate = factureAchatRepository.findAll().size();
        factureAchat.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFactureAchatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, factureAchat.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(factureAchat))
            )
            .andExpect(status().isBadRequest());

        // Validate the FactureAchat in the database
        List<FactureAchat> factureAchatList = factureAchatRepository.findAll();
        assertThat(factureAchatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFactureAchat() throws Exception {
        int databaseSizeBeforeUpdate = factureAchatRepository.findAll().size();
        factureAchat.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactureAchatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(factureAchat))
            )
            .andExpect(status().isBadRequest());

        // Validate the FactureAchat in the database
        List<FactureAchat> factureAchatList = factureAchatRepository.findAll();
        assertThat(factureAchatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFactureAchat() throws Exception {
        int databaseSizeBeforeUpdate = factureAchatRepository.findAll().size();
        factureAchat.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactureAchatMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(factureAchat))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FactureAchat in the database
        List<FactureAchat> factureAchatList = factureAchatRepository.findAll();
        assertThat(factureAchatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFactureAchatWithPatch() throws Exception {
        // Initialize the database
        factureAchatRepository.saveAndFlush(factureAchat);

        int databaseSizeBeforeUpdate = factureAchatRepository.findAll().size();

        // Update the factureAchat using partial update
        FactureAchat partialUpdatedFactureAchat = new FactureAchat();
        partialUpdatedFactureAchat.setId(factureAchat.getId());

        partialUpdatedFactureAchat
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE)
            .description(UPDATED_DESCRIPTION)
            .montant(UPDATED_MONTANT)
            .payee(UPDATED_PAYEE);

        restFactureAchatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFactureAchat.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFactureAchat))
            )
            .andExpect(status().isOk());

        // Validate the FactureAchat in the database
        List<FactureAchat> factureAchatList = factureAchatRepository.findAll();
        assertThat(factureAchatList).hasSize(databaseSizeBeforeUpdate);
        FactureAchat testFactureAchat = factureAchatList.get(factureAchatList.size() - 1);
        assertThat(testFactureAchat.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testFactureAchat.getPhotoContentType()).isEqualTo(UPDATED_PHOTO_CONTENT_TYPE);
        assertThat(testFactureAchat.getIdFonc()).isEqualTo(DEFAULT_ID_FONC);
        assertThat(testFactureAchat.getDesignation()).isEqualTo(DEFAULT_DESIGNATION);
        assertThat(testFactureAchat.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFactureAchat.getMontant()).isEqualTo(UPDATED_MONTANT);
        assertThat(testFactureAchat.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testFactureAchat.getPayee()).isEqualTo(UPDATED_PAYEE);
    }

    @Test
    @Transactional
    void fullUpdateFactureAchatWithPatch() throws Exception {
        // Initialize the database
        factureAchatRepository.saveAndFlush(factureAchat);

        int databaseSizeBeforeUpdate = factureAchatRepository.findAll().size();

        // Update the factureAchat using partial update
        FactureAchat partialUpdatedFactureAchat = new FactureAchat();
        partialUpdatedFactureAchat.setId(factureAchat.getId());

        partialUpdatedFactureAchat
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE)
            .idFonc(UPDATED_ID_FONC)
            .designation(UPDATED_DESIGNATION)
            .description(UPDATED_DESCRIPTION)
            .montant(UPDATED_MONTANT)
            .date(UPDATED_DATE)
            .payee(UPDATED_PAYEE);

        restFactureAchatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFactureAchat.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFactureAchat))
            )
            .andExpect(status().isOk());

        // Validate the FactureAchat in the database
        List<FactureAchat> factureAchatList = factureAchatRepository.findAll();
        assertThat(factureAchatList).hasSize(databaseSizeBeforeUpdate);
        FactureAchat testFactureAchat = factureAchatList.get(factureAchatList.size() - 1);
        assertThat(testFactureAchat.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testFactureAchat.getPhotoContentType()).isEqualTo(UPDATED_PHOTO_CONTENT_TYPE);
        assertThat(testFactureAchat.getIdFonc()).isEqualTo(UPDATED_ID_FONC);
        assertThat(testFactureAchat.getDesignation()).isEqualTo(UPDATED_DESIGNATION);
        assertThat(testFactureAchat.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFactureAchat.getMontant()).isEqualTo(UPDATED_MONTANT);
        assertThat(testFactureAchat.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testFactureAchat.getPayee()).isEqualTo(UPDATED_PAYEE);
    }

    @Test
    @Transactional
    void patchNonExistingFactureAchat() throws Exception {
        int databaseSizeBeforeUpdate = factureAchatRepository.findAll().size();
        factureAchat.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFactureAchatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, factureAchat.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(factureAchat))
            )
            .andExpect(status().isBadRequest());

        // Validate the FactureAchat in the database
        List<FactureAchat> factureAchatList = factureAchatRepository.findAll();
        assertThat(factureAchatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFactureAchat() throws Exception {
        int databaseSizeBeforeUpdate = factureAchatRepository.findAll().size();
        factureAchat.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactureAchatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(factureAchat))
            )
            .andExpect(status().isBadRequest());

        // Validate the FactureAchat in the database
        List<FactureAchat> factureAchatList = factureAchatRepository.findAll();
        assertThat(factureAchatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFactureAchat() throws Exception {
        int databaseSizeBeforeUpdate = factureAchatRepository.findAll().size();
        factureAchat.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactureAchatMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(factureAchat))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FactureAchat in the database
        List<FactureAchat> factureAchatList = factureAchatRepository.findAll();
        assertThat(factureAchatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFactureAchat() throws Exception {
        // Initialize the database
        factureAchatRepository.saveAndFlush(factureAchat);

        int databaseSizeBeforeDelete = factureAchatRepository.findAll().size();

        // Delete the factureAchat
        restFactureAchatMockMvc
            .perform(delete(ENTITY_API_URL_ID, factureAchat.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FactureAchat> factureAchatList = factureAchatRepository.findAll();
        assertThat(factureAchatList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
