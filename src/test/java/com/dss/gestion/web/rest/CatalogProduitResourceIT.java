package com.dss.gestion.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.dss.gestion.IntegrationTest;
import com.dss.gestion.domain.CatalogProduit;
import com.dss.gestion.domain.CatalogTypeProduit;
import com.dss.gestion.domain.Fournisseur;
import com.dss.gestion.repository.CatalogProduitRepository;
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
 * Integration tests for the {@link CatalogProduitResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CatalogProduitResourceIT {

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

    private static final Integer DEFAULT_QUANTITE = 1;
    private static final Integer UPDATED_QUANTITE = 2;

    private static final Long DEFAULT_PRIX_ACHAT_UNIT = 1L;
    private static final Long UPDATED_PRIX_ACHAT_UNIT = 2L;

    private static final Long DEFAULT_PRIX_VENTE_UNIT = 1L;
    private static final Long UPDATED_PRIX_VENTE_UNIT = 2L;

    private static final Long DEFAULT_PRIX_VENTE_GROS = 1L;
    private static final Long UPDATED_PRIX_VENTE_GROS = 2L;

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/catalog-produits";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CatalogProduitRepository catalogProduitRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCatalogProduitMockMvc;

    private CatalogProduit catalogProduit;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CatalogProduit createEntity(EntityManager em) {
        CatalogProduit catalogProduit = new CatalogProduit()
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
            .idFonc(DEFAULT_ID_FONC)
            .designation(DEFAULT_DESIGNATION)
            .description(DEFAULT_DESCRIPTION)
            .quantite(DEFAULT_QUANTITE)
            .prixAchatUnit(DEFAULT_PRIX_ACHAT_UNIT)
            .prixVenteUnit(DEFAULT_PRIX_VENTE_UNIT)
            .prixVenteGros(DEFAULT_PRIX_VENTE_GROS)
            .date(DEFAULT_DATE);
        // Add required entity
        Fournisseur fournisseur;
        if (TestUtil.findAll(em, Fournisseur.class).isEmpty()) {
            fournisseur = FournisseurResourceIT.createEntity(em);
            em.persist(fournisseur);
            em.flush();
        } else {
            fournisseur = TestUtil.findAll(em, Fournisseur.class).get(0);
        }
        catalogProduit.setFournisseur(fournisseur);
        // Add required entity
        CatalogTypeProduit catalogTypeProduit;
        if (TestUtil.findAll(em, CatalogTypeProduit.class).isEmpty()) {
            catalogTypeProduit = CatalogTypeProduitResourceIT.createEntity(em);
            em.persist(catalogTypeProduit);
            em.flush();
        } else {
            catalogTypeProduit = TestUtil.findAll(em, CatalogTypeProduit.class).get(0);
        }
        catalogProduit.setType(catalogTypeProduit);
        return catalogProduit;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CatalogProduit createUpdatedEntity(EntityManager em) {
        CatalogProduit catalogProduit = new CatalogProduit()
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .idFonc(UPDATED_ID_FONC)
            .designation(UPDATED_DESIGNATION)
            .description(UPDATED_DESCRIPTION)
            .quantite(UPDATED_QUANTITE)
            .prixAchatUnit(UPDATED_PRIX_ACHAT_UNIT)
            .prixVenteUnit(UPDATED_PRIX_VENTE_UNIT)
            .prixVenteGros(UPDATED_PRIX_VENTE_GROS)
            .date(UPDATED_DATE);
        // Add required entity
        Fournisseur fournisseur;
        if (TestUtil.findAll(em, Fournisseur.class).isEmpty()) {
            fournisseur = FournisseurResourceIT.createUpdatedEntity(em);
            em.persist(fournisseur);
            em.flush();
        } else {
            fournisseur = TestUtil.findAll(em, Fournisseur.class).get(0);
        }
        catalogProduit.setFournisseur(fournisseur);
        // Add required entity
        CatalogTypeProduit catalogTypeProduit;
        if (TestUtil.findAll(em, CatalogTypeProduit.class).isEmpty()) {
            catalogTypeProduit = CatalogTypeProduitResourceIT.createUpdatedEntity(em);
            em.persist(catalogTypeProduit);
            em.flush();
        } else {
            catalogTypeProduit = TestUtil.findAll(em, CatalogTypeProduit.class).get(0);
        }
        catalogProduit.setType(catalogTypeProduit);
        return catalogProduit;
    }

    @BeforeEach
    public void initTest() {
        catalogProduit = createEntity(em);
    }

    @Test
    @Transactional
    void createCatalogProduit() throws Exception {
        int databaseSizeBeforeCreate = catalogProduitRepository.findAll().size();
        // Create the CatalogProduit
        restCatalogProduitMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(catalogProduit))
            )
            .andExpect(status().isCreated());

        // Validate the CatalogProduit in the database
        List<CatalogProduit> catalogProduitList = catalogProduitRepository.findAll();
        assertThat(catalogProduitList).hasSize(databaseSizeBeforeCreate + 1);
        CatalogProduit testCatalogProduit = catalogProduitList.get(catalogProduitList.size() - 1);
        assertThat(testCatalogProduit.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testCatalogProduit.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testCatalogProduit.getIdFonc()).isEqualTo(DEFAULT_ID_FONC);
        assertThat(testCatalogProduit.getDesignation()).isEqualTo(DEFAULT_DESIGNATION);
        assertThat(testCatalogProduit.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCatalogProduit.getQuantite()).isEqualTo(DEFAULT_QUANTITE);
        assertThat(testCatalogProduit.getPrixAchatUnit()).isEqualTo(DEFAULT_PRIX_ACHAT_UNIT);
        assertThat(testCatalogProduit.getPrixVenteUnit()).isEqualTo(DEFAULT_PRIX_VENTE_UNIT);
        assertThat(testCatalogProduit.getPrixVenteGros()).isEqualTo(DEFAULT_PRIX_VENTE_GROS);
        assertThat(testCatalogProduit.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void createCatalogProduitWithExistingId() throws Exception {
        // Create the CatalogProduit with an existing ID
        catalogProduit.setId(1L);

        int databaseSizeBeforeCreate = catalogProduitRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCatalogProduitMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(catalogProduit))
            )
            .andExpect(status().isBadRequest());

        // Validate the CatalogProduit in the database
        List<CatalogProduit> catalogProduitList = catalogProduitRepository.findAll();
        assertThat(catalogProduitList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDesignationIsRequired() throws Exception {
        int databaseSizeBeforeTest = catalogProduitRepository.findAll().size();
        // set the field null
        catalogProduit.setDesignation(null);

        // Create the CatalogProduit, which fails.

        restCatalogProduitMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(catalogProduit))
            )
            .andExpect(status().isBadRequest());

        List<CatalogProduit> catalogProduitList = catalogProduitRepository.findAll();
        assertThat(catalogProduitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkQuantiteIsRequired() throws Exception {
        int databaseSizeBeforeTest = catalogProduitRepository.findAll().size();
        // set the field null
        catalogProduit.setQuantite(null);

        // Create the CatalogProduit, which fails.

        restCatalogProduitMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(catalogProduit))
            )
            .andExpect(status().isBadRequest());

        List<CatalogProduit> catalogProduitList = catalogProduitRepository.findAll();
        assertThat(catalogProduitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrixAchatUnitIsRequired() throws Exception {
        int databaseSizeBeforeTest = catalogProduitRepository.findAll().size();
        // set the field null
        catalogProduit.setPrixAchatUnit(null);

        // Create the CatalogProduit, which fails.

        restCatalogProduitMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(catalogProduit))
            )
            .andExpect(status().isBadRequest());

        List<CatalogProduit> catalogProduitList = catalogProduitRepository.findAll();
        assertThat(catalogProduitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrixVenteUnitIsRequired() throws Exception {
        int databaseSizeBeforeTest = catalogProduitRepository.findAll().size();
        // set the field null
        catalogProduit.setPrixVenteUnit(null);

        // Create the CatalogProduit, which fails.

        restCatalogProduitMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(catalogProduit))
            )
            .andExpect(status().isBadRequest());

        List<CatalogProduit> catalogProduitList = catalogProduitRepository.findAll();
        assertThat(catalogProduitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCatalogProduits() throws Exception {
        // Initialize the database
        catalogProduitRepository.saveAndFlush(catalogProduit);

        // Get all the catalogProduitList
        restCatalogProduitMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(catalogProduit.getId().intValue())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].idFonc").value(hasItem(DEFAULT_ID_FONC)))
            .andExpect(jsonPath("$.[*].designation").value(hasItem(DEFAULT_DESIGNATION)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].quantite").value(hasItem(DEFAULT_QUANTITE)))
            .andExpect(jsonPath("$.[*].prixAchatUnit").value(hasItem(DEFAULT_PRIX_ACHAT_UNIT.intValue())))
            .andExpect(jsonPath("$.[*].prixVenteUnit").value(hasItem(DEFAULT_PRIX_VENTE_UNIT.intValue())))
            .andExpect(jsonPath("$.[*].prixVenteGros").value(hasItem(DEFAULT_PRIX_VENTE_GROS.intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @Test
    @Transactional
    void getCatalogProduit() throws Exception {
        // Initialize the database
        catalogProduitRepository.saveAndFlush(catalogProduit);

        // Get the catalogProduit
        restCatalogProduitMockMvc
            .perform(get(ENTITY_API_URL_ID, catalogProduit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(catalogProduit.getId().intValue()))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.idFonc").value(DEFAULT_ID_FONC))
            .andExpect(jsonPath("$.designation").value(DEFAULT_DESIGNATION))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.quantite").value(DEFAULT_QUANTITE))
            .andExpect(jsonPath("$.prixAchatUnit").value(DEFAULT_PRIX_ACHAT_UNIT.intValue()))
            .andExpect(jsonPath("$.prixVenteUnit").value(DEFAULT_PRIX_VENTE_UNIT.intValue()))
            .andExpect(jsonPath("$.prixVenteGros").value(DEFAULT_PRIX_VENTE_GROS.intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCatalogProduit() throws Exception {
        // Get the catalogProduit
        restCatalogProduitMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCatalogProduit() throws Exception {
        // Initialize the database
        catalogProduitRepository.saveAndFlush(catalogProduit);

        int databaseSizeBeforeUpdate = catalogProduitRepository.findAll().size();

        // Update the catalogProduit
        CatalogProduit updatedCatalogProduit = catalogProduitRepository.findById(catalogProduit.getId()).get();
        // Disconnect from session so that the updates on updatedCatalogProduit are not directly saved in db
        em.detach(updatedCatalogProduit);
        updatedCatalogProduit
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .idFonc(UPDATED_ID_FONC)
            .designation(UPDATED_DESIGNATION)
            .description(UPDATED_DESCRIPTION)
            .quantite(UPDATED_QUANTITE)
            .prixAchatUnit(UPDATED_PRIX_ACHAT_UNIT)
            .prixVenteUnit(UPDATED_PRIX_VENTE_UNIT)
            .prixVenteGros(UPDATED_PRIX_VENTE_GROS)
            .date(UPDATED_DATE);

        restCatalogProduitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCatalogProduit.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCatalogProduit))
            )
            .andExpect(status().isOk());

        // Validate the CatalogProduit in the database
        List<CatalogProduit> catalogProduitList = catalogProduitRepository.findAll();
        assertThat(catalogProduitList).hasSize(databaseSizeBeforeUpdate);
        CatalogProduit testCatalogProduit = catalogProduitList.get(catalogProduitList.size() - 1);
        assertThat(testCatalogProduit.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testCatalogProduit.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testCatalogProduit.getIdFonc()).isEqualTo(UPDATED_ID_FONC);
        assertThat(testCatalogProduit.getDesignation()).isEqualTo(UPDATED_DESIGNATION);
        assertThat(testCatalogProduit.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCatalogProduit.getQuantite()).isEqualTo(UPDATED_QUANTITE);
        assertThat(testCatalogProduit.getPrixAchatUnit()).isEqualTo(UPDATED_PRIX_ACHAT_UNIT);
        assertThat(testCatalogProduit.getPrixVenteUnit()).isEqualTo(UPDATED_PRIX_VENTE_UNIT);
        assertThat(testCatalogProduit.getPrixVenteGros()).isEqualTo(UPDATED_PRIX_VENTE_GROS);
        assertThat(testCatalogProduit.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingCatalogProduit() throws Exception {
        int databaseSizeBeforeUpdate = catalogProduitRepository.findAll().size();
        catalogProduit.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCatalogProduitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, catalogProduit.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(catalogProduit))
            )
            .andExpect(status().isBadRequest());

        // Validate the CatalogProduit in the database
        List<CatalogProduit> catalogProduitList = catalogProduitRepository.findAll();
        assertThat(catalogProduitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCatalogProduit() throws Exception {
        int databaseSizeBeforeUpdate = catalogProduitRepository.findAll().size();
        catalogProduit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatalogProduitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(catalogProduit))
            )
            .andExpect(status().isBadRequest());

        // Validate the CatalogProduit in the database
        List<CatalogProduit> catalogProduitList = catalogProduitRepository.findAll();
        assertThat(catalogProduitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCatalogProduit() throws Exception {
        int databaseSizeBeforeUpdate = catalogProduitRepository.findAll().size();
        catalogProduit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatalogProduitMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(catalogProduit))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CatalogProduit in the database
        List<CatalogProduit> catalogProduitList = catalogProduitRepository.findAll();
        assertThat(catalogProduitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCatalogProduitWithPatch() throws Exception {
        // Initialize the database
        catalogProduitRepository.saveAndFlush(catalogProduit);

        int databaseSizeBeforeUpdate = catalogProduitRepository.findAll().size();

        // Update the catalogProduit using partial update
        CatalogProduit partialUpdatedCatalogProduit = new CatalogProduit();
        partialUpdatedCatalogProduit.setId(catalogProduit.getId());

        partialUpdatedCatalogProduit
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .idFonc(UPDATED_ID_FONC)
            .description(UPDATED_DESCRIPTION)
            .quantite(UPDATED_QUANTITE)
            .date(UPDATED_DATE);

        restCatalogProduitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCatalogProduit.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCatalogProduit))
            )
            .andExpect(status().isOk());

        // Validate the CatalogProduit in the database
        List<CatalogProduit> catalogProduitList = catalogProduitRepository.findAll();
        assertThat(catalogProduitList).hasSize(databaseSizeBeforeUpdate);
        CatalogProduit testCatalogProduit = catalogProduitList.get(catalogProduitList.size() - 1);
        assertThat(testCatalogProduit.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testCatalogProduit.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testCatalogProduit.getIdFonc()).isEqualTo(UPDATED_ID_FONC);
        assertThat(testCatalogProduit.getDesignation()).isEqualTo(DEFAULT_DESIGNATION);
        assertThat(testCatalogProduit.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCatalogProduit.getQuantite()).isEqualTo(UPDATED_QUANTITE);
        assertThat(testCatalogProduit.getPrixAchatUnit()).isEqualTo(DEFAULT_PRIX_ACHAT_UNIT);
        assertThat(testCatalogProduit.getPrixVenteUnit()).isEqualTo(DEFAULT_PRIX_VENTE_UNIT);
        assertThat(testCatalogProduit.getPrixVenteGros()).isEqualTo(DEFAULT_PRIX_VENTE_GROS);
        assertThat(testCatalogProduit.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateCatalogProduitWithPatch() throws Exception {
        // Initialize the database
        catalogProduitRepository.saveAndFlush(catalogProduit);

        int databaseSizeBeforeUpdate = catalogProduitRepository.findAll().size();

        // Update the catalogProduit using partial update
        CatalogProduit partialUpdatedCatalogProduit = new CatalogProduit();
        partialUpdatedCatalogProduit.setId(catalogProduit.getId());

        partialUpdatedCatalogProduit
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .idFonc(UPDATED_ID_FONC)
            .designation(UPDATED_DESIGNATION)
            .description(UPDATED_DESCRIPTION)
            .quantite(UPDATED_QUANTITE)
            .prixAchatUnit(UPDATED_PRIX_ACHAT_UNIT)
            .prixVenteUnit(UPDATED_PRIX_VENTE_UNIT)
            .prixVenteGros(UPDATED_PRIX_VENTE_GROS)
            .date(UPDATED_DATE);

        restCatalogProduitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCatalogProduit.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCatalogProduit))
            )
            .andExpect(status().isOk());

        // Validate the CatalogProduit in the database
        List<CatalogProduit> catalogProduitList = catalogProduitRepository.findAll();
        assertThat(catalogProduitList).hasSize(databaseSizeBeforeUpdate);
        CatalogProduit testCatalogProduit = catalogProduitList.get(catalogProduitList.size() - 1);
        assertThat(testCatalogProduit.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testCatalogProduit.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testCatalogProduit.getIdFonc()).isEqualTo(UPDATED_ID_FONC);
        assertThat(testCatalogProduit.getDesignation()).isEqualTo(UPDATED_DESIGNATION);
        assertThat(testCatalogProduit.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCatalogProduit.getQuantite()).isEqualTo(UPDATED_QUANTITE);
        assertThat(testCatalogProduit.getPrixAchatUnit()).isEqualTo(UPDATED_PRIX_ACHAT_UNIT);
        assertThat(testCatalogProduit.getPrixVenteUnit()).isEqualTo(UPDATED_PRIX_VENTE_UNIT);
        assertThat(testCatalogProduit.getPrixVenteGros()).isEqualTo(UPDATED_PRIX_VENTE_GROS);
        assertThat(testCatalogProduit.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingCatalogProduit() throws Exception {
        int databaseSizeBeforeUpdate = catalogProduitRepository.findAll().size();
        catalogProduit.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCatalogProduitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, catalogProduit.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(catalogProduit))
            )
            .andExpect(status().isBadRequest());

        // Validate the CatalogProduit in the database
        List<CatalogProduit> catalogProduitList = catalogProduitRepository.findAll();
        assertThat(catalogProduitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCatalogProduit() throws Exception {
        int databaseSizeBeforeUpdate = catalogProduitRepository.findAll().size();
        catalogProduit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatalogProduitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(catalogProduit))
            )
            .andExpect(status().isBadRequest());

        // Validate the CatalogProduit in the database
        List<CatalogProduit> catalogProduitList = catalogProduitRepository.findAll();
        assertThat(catalogProduitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCatalogProduit() throws Exception {
        int databaseSizeBeforeUpdate = catalogProduitRepository.findAll().size();
        catalogProduit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatalogProduitMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(catalogProduit))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CatalogProduit in the database
        List<CatalogProduit> catalogProduitList = catalogProduitRepository.findAll();
        assertThat(catalogProduitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCatalogProduit() throws Exception {
        // Initialize the database
        catalogProduitRepository.saveAndFlush(catalogProduit);

        int databaseSizeBeforeDelete = catalogProduitRepository.findAll().size();

        // Delete the catalogProduit
        restCatalogProduitMockMvc
            .perform(delete(ENTITY_API_URL_ID, catalogProduit.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CatalogProduit> catalogProduitList = catalogProduitRepository.findAll();
        assertThat(catalogProduitList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
