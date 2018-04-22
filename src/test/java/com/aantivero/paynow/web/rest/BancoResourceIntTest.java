package com.aantivero.paynow.web.rest;

import com.aantivero.paynow.PaynowApp;

import com.aantivero.paynow.domain.Banco;
import com.aantivero.paynow.repository.BancoRepository;
import com.aantivero.paynow.service.BancoService;
import com.aantivero.paynow.repository.search.BancoSearchRepository;
import com.aantivero.paynow.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.aantivero.paynow.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the BancoResource REST controller.
 *
 * @see BancoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PaynowApp.class)
public class BancoResourceIntTest {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_CODIGO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO = "BBBBBBBBBB";

    @Autowired
    private BancoRepository bancoRepository;

    @Autowired
    private BancoService bancoService;

    @Autowired
    private BancoSearchRepository bancoSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBancoMockMvc;

    private Banco banco;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BancoResource bancoResource = new BancoResource(bancoService);
        this.restBancoMockMvc = MockMvcBuilders.standaloneSetup(bancoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Banco createEntity(EntityManager em) {
        Banco banco = new Banco()
            .nombre(DEFAULT_NOMBRE)
            .codigo(DEFAULT_CODIGO);
        return banco;
    }

    @Before
    public void initTest() {
        bancoSearchRepository.deleteAll();
        banco = createEntity(em);
    }

    @Test
    @Transactional
    public void createBanco() throws Exception {
        int databaseSizeBeforeCreate = bancoRepository.findAll().size();

        // Create the Banco
        restBancoMockMvc.perform(post("/api/bancos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(banco)))
            .andExpect(status().isCreated());

        // Validate the Banco in the database
        List<Banco> bancoList = bancoRepository.findAll();
        assertThat(bancoList).hasSize(databaseSizeBeforeCreate + 1);
        Banco testBanco = bancoList.get(bancoList.size() - 1);
        assertThat(testBanco.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testBanco.getCodigo()).isEqualTo(DEFAULT_CODIGO);

        // Validate the Banco in Elasticsearch
        Banco bancoEs = bancoSearchRepository.findOne(testBanco.getId());
        assertThat(bancoEs).isEqualToIgnoringGivenFields(testBanco);
    }

    @Test
    @Transactional
    public void createBancoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = bancoRepository.findAll().size();

        // Create the Banco with an existing ID
        banco.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBancoMockMvc.perform(post("/api/bancos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(banco)))
            .andExpect(status().isBadRequest());

        // Validate the Banco in the database
        List<Banco> bancoList = bancoRepository.findAll();
        assertThat(bancoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = bancoRepository.findAll().size();
        // set the field null
        banco.setNombre(null);

        // Create the Banco, which fails.

        restBancoMockMvc.perform(post("/api/bancos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(banco)))
            .andExpect(status().isBadRequest());

        List<Banco> bancoList = bancoRepository.findAll();
        assertThat(bancoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCodigoIsRequired() throws Exception {
        int databaseSizeBeforeTest = bancoRepository.findAll().size();
        // set the field null
        banco.setCodigo(null);

        // Create the Banco, which fails.

        restBancoMockMvc.perform(post("/api/bancos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(banco)))
            .andExpect(status().isBadRequest());

        List<Banco> bancoList = bancoRepository.findAll();
        assertThat(bancoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBancos() throws Exception {
        // Initialize the database
        bancoRepository.saveAndFlush(banco);

        // Get all the bancoList
        restBancoMockMvc.perform(get("/api/bancos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(banco.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO.toString())));
    }

    @Test
    @Transactional
    public void getBanco() throws Exception {
        // Initialize the database
        bancoRepository.saveAndFlush(banco);

        // Get the banco
        restBancoMockMvc.perform(get("/api/bancos/{id}", banco.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(banco.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBanco() throws Exception {
        // Get the banco
        restBancoMockMvc.perform(get("/api/bancos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBanco() throws Exception {
        // Initialize the database
        bancoService.save(banco);

        int databaseSizeBeforeUpdate = bancoRepository.findAll().size();

        // Update the banco
        Banco updatedBanco = bancoRepository.findOne(banco.getId());
        // Disconnect from session so that the updates on updatedBanco are not directly saved in db
        em.detach(updatedBanco);
        updatedBanco
            .nombre(UPDATED_NOMBRE)
            .codigo(UPDATED_CODIGO);

        restBancoMockMvc.perform(put("/api/bancos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBanco)))
            .andExpect(status().isOk());

        // Validate the Banco in the database
        List<Banco> bancoList = bancoRepository.findAll();
        assertThat(bancoList).hasSize(databaseSizeBeforeUpdate);
        Banco testBanco = bancoList.get(bancoList.size() - 1);
        assertThat(testBanco.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testBanco.getCodigo()).isEqualTo(UPDATED_CODIGO);

        // Validate the Banco in Elasticsearch
        Banco bancoEs = bancoSearchRepository.findOne(testBanco.getId());
        assertThat(bancoEs).isEqualToIgnoringGivenFields(testBanco);
    }

    @Test
    @Transactional
    public void updateNonExistingBanco() throws Exception {
        int databaseSizeBeforeUpdate = bancoRepository.findAll().size();

        // Create the Banco

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBancoMockMvc.perform(put("/api/bancos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(banco)))
            .andExpect(status().isCreated());

        // Validate the Banco in the database
        List<Banco> bancoList = bancoRepository.findAll();
        assertThat(bancoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBanco() throws Exception {
        // Initialize the database
        bancoService.save(banco);

        int databaseSizeBeforeDelete = bancoRepository.findAll().size();

        // Get the banco
        restBancoMockMvc.perform(delete("/api/bancos/{id}", banco.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean bancoExistsInEs = bancoSearchRepository.exists(banco.getId());
        assertThat(bancoExistsInEs).isFalse();

        // Validate the database is empty
        List<Banco> bancoList = bancoRepository.findAll();
        assertThat(bancoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchBanco() throws Exception {
        // Initialize the database
        bancoService.save(banco);

        // Search the banco
        restBancoMockMvc.perform(get("/api/_search/bancos?query=id:" + banco.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(banco.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Banco.class);
        Banco banco1 = new Banco();
        banco1.setId(1L);
        Banco banco2 = new Banco();
        banco2.setId(banco1.getId());
        assertThat(banco1).isEqualTo(banco2);
        banco2.setId(2L);
        assertThat(banco1).isNotEqualTo(banco2);
        banco1.setId(null);
        assertThat(banco1).isNotEqualTo(banco2);
    }
}
