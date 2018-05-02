package com.aantivero.paynow.web.rest;

import com.aantivero.paynow.PaynowApp;

import com.aantivero.paynow.domain.CuentaApp;
import com.aantivero.paynow.domain.Banco;
import com.aantivero.paynow.domain.App;
import com.aantivero.paynow.repository.CuentaAppRepository;
import com.aantivero.paynow.service.CuentaAppService;
import com.aantivero.paynow.repository.search.CuentaAppSearchRepository;
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
 * Test class for the CuentaAppResource REST controller.
 *
 * @see CuentaAppResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PaynowApp.class)
public class CuentaAppResourceIntTest {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_ALIAS_CBU = "AAAAAAAAAA";
    private static final String UPDATED_ALIAS_CBU = "BBBBBBBBBB";

    private static final String DEFAULT_CBU = "AAAAAAAAAA";
    private static final String UPDATED_CBU = "BBBBBBBBBB";

    private static final Moneda DEFAULT_MONEDA = Moneda.PESOS;
    private static final Moneda UPDATED_MONEDA = Moneda.PESOS;

    private static final BigDecimal DEFAULT_SALDO = new BigDecimal(2);
    private static final BigDecimal UPDATED_SALDO = new BigDecimal(1);

    @Autowired
    private CuentaAppRepository cuentaAppRepository;

    @Autowired
    private CuentaAppService cuentaAppService;

    @Autowired
    private CuentaAppSearchRepository cuentaAppSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCuentaAppMockMvc;

    private CuentaApp cuentaApp;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CuentaAppResource cuentaAppResource = new CuentaAppResource(cuentaAppService);
        this.restCuentaAppMockMvc = MockMvcBuilders.standaloneSetup(cuentaAppResource)
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
    public static CuentaApp createEntity(EntityManager em) {
        CuentaApp cuentaApp = new CuentaApp()
            .nombre(DEFAULT_NOMBRE)
            .aliasCbu(DEFAULT_ALIAS_CBU)
            .cbu(DEFAULT_CBU)
            .moneda(DEFAULT_MONEDA)
            .saldo(DEFAULT_SALDO);
        // Add required entity
        Banco banco = BancoResourceIntTest.createEntity(em);
        em.persist(banco);
        em.flush();
        cuentaApp.setBanco(banco);
        // Add required entity
        App app = AppResourceIntTest.createEntity(em);
        em.persist(app);
        em.flush();
        cuentaApp.setApp(app);
        return cuentaApp;
    }

    @Before
    public void initTest() {
        cuentaAppSearchRepository.deleteAll();
        cuentaApp = createEntity(em);
    }

    @Test
    @Transactional
    public void createCuentaApp() throws Exception {
        int databaseSizeBeforeCreate = cuentaAppRepository.findAll().size();

        // Create the CuentaApp
        restCuentaAppMockMvc.perform(post("/api/cuenta-apps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cuentaApp)))
            .andExpect(status().isCreated());

        // Validate the CuentaApp in the database
        List<CuentaApp> cuentaAppList = cuentaAppRepository.findAll();
        assertThat(cuentaAppList).hasSize(databaseSizeBeforeCreate + 1);
        CuentaApp testCuentaApp = cuentaAppList.get(cuentaAppList.size() - 1);
        assertThat(testCuentaApp.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testCuentaApp.getAliasCbu()).isEqualTo(DEFAULT_ALIAS_CBU);
        assertThat(testCuentaApp.getCbu()).isEqualTo(DEFAULT_CBU);
        assertThat(testCuentaApp.getMoneda()).isEqualTo(DEFAULT_MONEDA);
        assertThat(testCuentaApp.getSaldo()).isEqualTo(DEFAULT_SALDO);

        // Validate the CuentaApp in Elasticsearch
        CuentaApp cuentaAppEs = cuentaAppSearchRepository.findOne(testCuentaApp.getId());
        assertThat(cuentaAppEs).isEqualToIgnoringGivenFields(testCuentaApp);
    }

    @Test
    @Transactional
    public void createCuentaAppWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cuentaAppRepository.findAll().size();

        // Create the CuentaApp with an existing ID
        cuentaApp.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCuentaAppMockMvc.perform(post("/api/cuenta-apps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cuentaApp)))
            .andExpect(status().isBadRequest());

        // Validate the CuentaApp in the database
        List<CuentaApp> cuentaAppList = cuentaAppRepository.findAll();
        assertThat(cuentaAppList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = cuentaAppRepository.findAll().size();
        // set the field null
        cuentaApp.setNombre(null);

        // Create the CuentaApp, which fails.

        restCuentaAppMockMvc.perform(post("/api/cuenta-apps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cuentaApp)))
            .andExpect(status().isBadRequest());

        List<CuentaApp> cuentaAppList = cuentaAppRepository.findAll();
        assertThat(cuentaAppList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAliasCbuIsRequired() throws Exception {
        int databaseSizeBeforeTest = cuentaAppRepository.findAll().size();
        // set the field null
        cuentaApp.setAliasCbu(null);

        // Create the CuentaApp, which fails.

        restCuentaAppMockMvc.perform(post("/api/cuenta-apps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cuentaApp)))
            .andExpect(status().isBadRequest());

        List<CuentaApp> cuentaAppList = cuentaAppRepository.findAll();
        assertThat(cuentaAppList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCbuIsRequired() throws Exception {
        int databaseSizeBeforeTest = cuentaAppRepository.findAll().size();
        // set the field null
        cuentaApp.setCbu(null);

        // Create the CuentaApp, which fails.

        restCuentaAppMockMvc.perform(post("/api/cuenta-apps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cuentaApp)))
            .andExpect(status().isBadRequest());

        List<CuentaApp> cuentaAppList = cuentaAppRepository.findAll();
        assertThat(cuentaAppList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMonedaIsRequired() throws Exception {
        int databaseSizeBeforeTest = cuentaAppRepository.findAll().size();
        // set the field null
        cuentaApp.setMoneda(null);

        // Create the CuentaApp, which fails.

        restCuentaAppMockMvc.perform(post("/api/cuenta-apps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cuentaApp)))
            .andExpect(status().isBadRequest());

        List<CuentaApp> cuentaAppList = cuentaAppRepository.findAll();
        assertThat(cuentaAppList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSaldoIsRequired() throws Exception {
        int databaseSizeBeforeTest = cuentaAppRepository.findAll().size();
        // set the field null
        cuentaApp.setSaldo(null);

        // Create the CuentaApp, which fails.

        restCuentaAppMockMvc.perform(post("/api/cuenta-apps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cuentaApp)))
            .andExpect(status().isBadRequest());

        List<CuentaApp> cuentaAppList = cuentaAppRepository.findAll();
        assertThat(cuentaAppList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCuentaApps() throws Exception {
        // Initialize the database
        cuentaAppRepository.saveAndFlush(cuentaApp);

        // Get all the cuentaAppList
        restCuentaAppMockMvc.perform(get("/api/cuenta-apps?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cuentaApp.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
            .andExpect(jsonPath("$.[*].aliasCbu").value(hasItem(DEFAULT_ALIAS_CBU.toString())))
            .andExpect(jsonPath("$.[*].cbu").value(hasItem(DEFAULT_CBU.toString())))
            .andExpect(jsonPath("$.[*].moneda").value(hasItem(DEFAULT_MONEDA.toString())))
            .andExpect(jsonPath("$.[*].saldo").value(hasItem(DEFAULT_SALDO.intValue())));
    }

    @Test
    @Transactional
    public void getCuentaApp() throws Exception {
        // Initialize the database
        cuentaAppRepository.saveAndFlush(cuentaApp);

        // Get the cuentaApp
        restCuentaAppMockMvc.perform(get("/api/cuenta-apps/{id}", cuentaApp.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cuentaApp.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()))
            .andExpect(jsonPath("$.aliasCbu").value(DEFAULT_ALIAS_CBU.toString()))
            .andExpect(jsonPath("$.cbu").value(DEFAULT_CBU.toString()))
            .andExpect(jsonPath("$.moneda").value(DEFAULT_MONEDA.toString()))
            .andExpect(jsonPath("$.saldo").value(DEFAULT_SALDO.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCuentaApp() throws Exception {
        // Get the cuentaApp
        restCuentaAppMockMvc.perform(get("/api/cuenta-apps/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCuentaApp() throws Exception {
        // Initialize the database
        cuentaAppService.save(cuentaApp);

        int databaseSizeBeforeUpdate = cuentaAppRepository.findAll().size();

        // Update the cuentaApp
        CuentaApp updatedCuentaApp = cuentaAppRepository.findOne(cuentaApp.getId());
        // Disconnect from session so that the updates on updatedCuentaApp are not directly saved in db
        em.detach(updatedCuentaApp);
        updatedCuentaApp
            .nombre(UPDATED_NOMBRE)
            .aliasCbu(UPDATED_ALIAS_CBU)
            .cbu(UPDATED_CBU)
            .moneda(UPDATED_MONEDA)
            .saldo(UPDATED_SALDO);

        restCuentaAppMockMvc.perform(put("/api/cuenta-apps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCuentaApp)))
            .andExpect(status().isOk());

        // Validate the CuentaApp in the database
        List<CuentaApp> cuentaAppList = cuentaAppRepository.findAll();
        assertThat(cuentaAppList).hasSize(databaseSizeBeforeUpdate);
        CuentaApp testCuentaApp = cuentaAppList.get(cuentaAppList.size() - 1);
        assertThat(testCuentaApp.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testCuentaApp.getAliasCbu()).isEqualTo(UPDATED_ALIAS_CBU);
        assertThat(testCuentaApp.getCbu()).isEqualTo(UPDATED_CBU);
        assertThat(testCuentaApp.getMoneda()).isEqualTo(UPDATED_MONEDA);
        assertThat(testCuentaApp.getSaldo()).isEqualTo(UPDATED_SALDO);

        // Validate the CuentaApp in Elasticsearch
        CuentaApp cuentaAppEs = cuentaAppSearchRepository.findOne(testCuentaApp.getId());
        assertThat(cuentaAppEs).isEqualToIgnoringGivenFields(testCuentaApp);
    }

    @Test
    @Transactional
    public void updateNonExistingCuentaApp() throws Exception {
        int databaseSizeBeforeUpdate = cuentaAppRepository.findAll().size();

        // Create the CuentaApp

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCuentaAppMockMvc.perform(put("/api/cuenta-apps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cuentaApp)))
            .andExpect(status().isCreated());

        // Validate the CuentaApp in the database
        List<CuentaApp> cuentaAppList = cuentaAppRepository.findAll();
        assertThat(cuentaAppList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCuentaApp() throws Exception {
        // Initialize the database
        cuentaAppService.save(cuentaApp);

        int databaseSizeBeforeDelete = cuentaAppRepository.findAll().size();

        // Get the cuentaApp
        restCuentaAppMockMvc.perform(delete("/api/cuenta-apps/{id}", cuentaApp.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean cuentaAppExistsInEs = cuentaAppSearchRepository.exists(cuentaApp.getId());
        assertThat(cuentaAppExistsInEs).isFalse();

        // Validate the database is empty
        List<CuentaApp> cuentaAppList = cuentaAppRepository.findAll();
        assertThat(cuentaAppList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCuentaApp() throws Exception {
        // Initialize the database
        cuentaAppService.save(cuentaApp);

        // Search the cuentaApp
        restCuentaAppMockMvc.perform(get("/api/_search/cuenta-apps?query=id:" + cuentaApp.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cuentaApp.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
            .andExpect(jsonPath("$.[*].aliasCbu").value(hasItem(DEFAULT_ALIAS_CBU.toString())))
            .andExpect(jsonPath("$.[*].cbu").value(hasItem(DEFAULT_CBU.toString())))
            .andExpect(jsonPath("$.[*].moneda").value(hasItem(DEFAULT_MONEDA.toString())))
            .andExpect(jsonPath("$.[*].saldo").value(hasItem(DEFAULT_SALDO.intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CuentaApp.class);
        CuentaApp cuentaApp1 = new CuentaApp();
        cuentaApp1.setId(1L);
        CuentaApp cuentaApp2 = new CuentaApp();
        cuentaApp2.setId(cuentaApp1.getId());
        assertThat(cuentaApp1).isEqualTo(cuentaApp2);
        cuentaApp2.setId(2L);
        assertThat(cuentaApp1).isNotEqualTo(cuentaApp2);
        cuentaApp1.setId(null);
        assertThat(cuentaApp1).isNotEqualTo(cuentaApp2);
    }
}
