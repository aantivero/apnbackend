package com.aantivero.paynow.web.rest;

import com.aantivero.paynow.PaynowApp;

import com.aantivero.paynow.domain.SaldoApp;
import com.aantivero.paynow.domain.App;
import com.aantivero.paynow.repository.SaldoAppRepository;
import com.aantivero.paynow.service.SaldoAppService;
import com.aantivero.paynow.repository.search.SaldoAppSearchRepository;
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
import java.math.BigDecimal;
import java.util.List;

import static com.aantivero.paynow.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.aantivero.paynow.domain.enumeration.Moneda;
/**
 * Test class for the SaldoAppResource REST controller.
 *
 * @see SaldoAppResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PaynowApp.class)
public class SaldoAppResourceIntTest {

    private static final Moneda DEFAULT_MONEDA = Moneda.PESOS;
    private static final Moneda UPDATED_MONEDA = Moneda.DOLAR;

    private static final BigDecimal DEFAULT_MONTO = new BigDecimal(1);
    private static final BigDecimal UPDATED_MONTO = new BigDecimal(2);

    @Autowired
    private SaldoAppRepository saldoAppRepository;

    @Autowired
    private SaldoAppService saldoAppService;

    @Autowired
    private SaldoAppSearchRepository saldoAppSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSaldoAppMockMvc;

    private SaldoApp saldoApp;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SaldoAppResource saldoAppResource = new SaldoAppResource(saldoAppService);
        this.restSaldoAppMockMvc = MockMvcBuilders.standaloneSetup(saldoAppResource)
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
    public static SaldoApp createEntity(EntityManager em) {
        SaldoApp saldoApp = new SaldoApp()
            .moneda(DEFAULT_MONEDA)
            .monto(DEFAULT_MONTO);
        // Add required entity
        App app = AppResourceIntTest.createEntity(em);
        em.persist(app);
        em.flush();
        saldoApp.setApp(app);
        return saldoApp;
    }

    @Before
    public void initTest() {
        saldoAppSearchRepository.deleteAll();
        saldoApp = createEntity(em);
    }

    @Test
    @Transactional
    public void createSaldoApp() throws Exception {
        int databaseSizeBeforeCreate = saldoAppRepository.findAll().size();

        // Create the SaldoApp
        restSaldoAppMockMvc.perform(post("/api/saldo-apps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(saldoApp)))
            .andExpect(status().isCreated());

        // Validate the SaldoApp in the database
        List<SaldoApp> saldoAppList = saldoAppRepository.findAll();
        assertThat(saldoAppList).hasSize(databaseSizeBeforeCreate + 1);
        SaldoApp testSaldoApp = saldoAppList.get(saldoAppList.size() - 1);
        assertThat(testSaldoApp.getMoneda()).isEqualTo(DEFAULT_MONEDA);
        assertThat(testSaldoApp.getMonto()).isEqualTo(DEFAULT_MONTO);

        // Validate the SaldoApp in Elasticsearch
        SaldoApp saldoAppEs = saldoAppSearchRepository.findOne(testSaldoApp.getId());
        assertThat(saldoAppEs).isEqualToIgnoringGivenFields(testSaldoApp);
    }

    @Test
    @Transactional
    public void createSaldoAppWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = saldoAppRepository.findAll().size();

        // Create the SaldoApp with an existing ID
        saldoApp.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSaldoAppMockMvc.perform(post("/api/saldo-apps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(saldoApp)))
            .andExpect(status().isBadRequest());

        // Validate the SaldoApp in the database
        List<SaldoApp> saldoAppList = saldoAppRepository.findAll();
        assertThat(saldoAppList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkMonedaIsRequired() throws Exception {
        int databaseSizeBeforeTest = saldoAppRepository.findAll().size();
        // set the field null
        saldoApp.setMoneda(null);

        // Create the SaldoApp, which fails.

        restSaldoAppMockMvc.perform(post("/api/saldo-apps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(saldoApp)))
            .andExpect(status().isBadRequest());

        List<SaldoApp> saldoAppList = saldoAppRepository.findAll();
        assertThat(saldoAppList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMontoIsRequired() throws Exception {
        int databaseSizeBeforeTest = saldoAppRepository.findAll().size();
        // set the field null
        saldoApp.setMonto(null);

        // Create the SaldoApp, which fails.

        restSaldoAppMockMvc.perform(post("/api/saldo-apps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(saldoApp)))
            .andExpect(status().isBadRequest());

        List<SaldoApp> saldoAppList = saldoAppRepository.findAll();
        assertThat(saldoAppList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSaldoApps() throws Exception {
        // Initialize the database
        saldoAppRepository.saveAndFlush(saldoApp);

        // Get all the saldoAppList
        restSaldoAppMockMvc.perform(get("/api/saldo-apps?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(saldoApp.getId().intValue())))
            .andExpect(jsonPath("$.[*].moneda").value(hasItem(DEFAULT_MONEDA.toString())))
            .andExpect(jsonPath("$.[*].monto").value(hasItem(DEFAULT_MONTO.intValue())));
    }

    @Test
    @Transactional
    public void getSaldoApp() throws Exception {
        // Initialize the database
        saldoAppRepository.saveAndFlush(saldoApp);

        // Get the saldoApp
        restSaldoAppMockMvc.perform(get("/api/saldo-apps/{id}", saldoApp.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(saldoApp.getId().intValue()))
            .andExpect(jsonPath("$.moneda").value(DEFAULT_MONEDA.toString()))
            .andExpect(jsonPath("$.monto").value(DEFAULT_MONTO.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSaldoApp() throws Exception {
        // Get the saldoApp
        restSaldoAppMockMvc.perform(get("/api/saldo-apps/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSaldoApp() throws Exception {
        // Initialize the database
        saldoAppService.save(saldoApp);

        int databaseSizeBeforeUpdate = saldoAppRepository.findAll().size();

        // Update the saldoApp
        SaldoApp updatedSaldoApp = saldoAppRepository.findOne(saldoApp.getId());
        // Disconnect from session so that the updates on updatedSaldoApp are not directly saved in db
        em.detach(updatedSaldoApp);
        updatedSaldoApp
            .moneda(UPDATED_MONEDA)
            .monto(UPDATED_MONTO);

        restSaldoAppMockMvc.perform(put("/api/saldo-apps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSaldoApp)))
            .andExpect(status().isOk());

        // Validate the SaldoApp in the database
        List<SaldoApp> saldoAppList = saldoAppRepository.findAll();
        assertThat(saldoAppList).hasSize(databaseSizeBeforeUpdate);
        SaldoApp testSaldoApp = saldoAppList.get(saldoAppList.size() - 1);
        assertThat(testSaldoApp.getMoneda()).isEqualTo(UPDATED_MONEDA);
        assertThat(testSaldoApp.getMonto()).isEqualTo(UPDATED_MONTO);

        // Validate the SaldoApp in Elasticsearch
        SaldoApp saldoAppEs = saldoAppSearchRepository.findOne(testSaldoApp.getId());
        assertThat(saldoAppEs).isEqualToIgnoringGivenFields(testSaldoApp);
    }

    @Test
    @Transactional
    public void updateNonExistingSaldoApp() throws Exception {
        int databaseSizeBeforeUpdate = saldoAppRepository.findAll().size();

        // Create the SaldoApp

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSaldoAppMockMvc.perform(put("/api/saldo-apps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(saldoApp)))
            .andExpect(status().isCreated());

        // Validate the SaldoApp in the database
        List<SaldoApp> saldoAppList = saldoAppRepository.findAll();
        assertThat(saldoAppList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSaldoApp() throws Exception {
        // Initialize the database
        saldoAppService.save(saldoApp);

        int databaseSizeBeforeDelete = saldoAppRepository.findAll().size();

        // Get the saldoApp
        restSaldoAppMockMvc.perform(delete("/api/saldo-apps/{id}", saldoApp.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean saldoAppExistsInEs = saldoAppSearchRepository.exists(saldoApp.getId());
        assertThat(saldoAppExistsInEs).isFalse();

        // Validate the database is empty
        List<SaldoApp> saldoAppList = saldoAppRepository.findAll();
        assertThat(saldoAppList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSaldoApp() throws Exception {
        // Initialize the database
        saldoAppService.save(saldoApp);

        // Search the saldoApp
        restSaldoAppMockMvc.perform(get("/api/_search/saldo-apps?query=id:" + saldoApp.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(saldoApp.getId().intValue())))
            .andExpect(jsonPath("$.[*].moneda").value(hasItem(DEFAULT_MONEDA.toString())))
            .andExpect(jsonPath("$.[*].monto").value(hasItem(DEFAULT_MONTO.intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SaldoApp.class);
        SaldoApp saldoApp1 = new SaldoApp();
        saldoApp1.setId(1L);
        SaldoApp saldoApp2 = new SaldoApp();
        saldoApp2.setId(saldoApp1.getId());
        assertThat(saldoApp1).isEqualTo(saldoApp2);
        saldoApp2.setId(2L);
        assertThat(saldoApp1).isNotEqualTo(saldoApp2);
        saldoApp1.setId(null);
        assertThat(saldoApp1).isNotEqualTo(saldoApp2);
    }
}
