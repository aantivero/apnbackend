package com.aantivero.paynow.web.rest;

import com.aantivero.paynow.PaynowApp;

import com.aantivero.paynow.domain.MovimientoApp;
import com.aantivero.paynow.domain.Banco;
import com.aantivero.paynow.domain.Banco;
import com.aantivero.paynow.repository.MovimientoAppRepository;
import com.aantivero.paynow.service.MovimientoAppService;
import com.aantivero.paynow.repository.search.MovimientoAppSearchRepository;
import com.aantivero.paynow.web.rest.errors.ExceptionTranslator;
import com.aantivero.paynow.service.dto.MovimientoAppCriteria;
import com.aantivero.paynow.service.MovimientoAppQueryService;

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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.aantivero.paynow.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.aantivero.paynow.domain.enumeration.Moneda;
/**
 * Test class for the MovimientoAppResource REST controller.
 *
 * @see MovimientoAppResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PaynowApp.class)
public class MovimientoAppResourceIntTest {

    private static final String DEFAULT_CUENTA_DEBITO_CBU = "AAAAAAAAAA";
    private static final String UPDATED_CUENTA_DEBITO_CBU = "BBBBBBBBBB";

    private static final String DEFAULT_CUENTA_DEBITO_ALIAS = "AAAAAAAAAA";
    private static final String UPDATED_CUENTA_DEBITO_ALIAS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_CUENTA_DEBITO_PROPIA = false;
    private static final Boolean UPDATED_CUENTA_DEBITO_PROPIA = true;

    private static final String DEFAULT_CUENTA_CREDITO_CBU = "AAAAAAAAAA";
    private static final String UPDATED_CUENTA_CREDITO_CBU = "BBBBBBBBBB";

    private static final String DEFAULT_CUENTA_CREDITO_ALIAS = "AAAAAAAAAA";
    private static final String UPDATED_CUENTA_CREDITO_ALIAS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_CUENTA_CREDITO_PROPIA = false;
    private static final Boolean UPDATED_CUENTA_CREDITO_PROPIA = true;

    private static final Moneda DEFAULT_MONEDA = Moneda.PESOS;
    private static final Moneda UPDATED_MONEDA = Moneda.DOLAR;

    private static final BigDecimal DEFAULT_MONTO = new BigDecimal(0);
    private static final BigDecimal UPDATED_MONTO = new BigDecimal(1);

    private static final Instant DEFAULT_TIMESTAMP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIMESTAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String DEFAULT_IDENTIFICACION = "AAAAAAAAAA";
    private static final String UPDATED_IDENTIFICACION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_CONSOLIDADO = false;
    private static final Boolean UPDATED_CONSOLIDADO = true;

    private static final Instant DEFAULT_CONSOLIDADO_TIMESTAMP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CONSOLIDADO_TIMESTAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private MovimientoAppRepository movimientoAppRepository;

    @Autowired
    private MovimientoAppService movimientoAppService;

    @Autowired
    private MovimientoAppSearchRepository movimientoAppSearchRepository;

    @Autowired
    private MovimientoAppQueryService movimientoAppQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMovimientoAppMockMvc;

    private MovimientoApp movimientoApp;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MovimientoAppResource movimientoAppResource = new MovimientoAppResource(movimientoAppService, movimientoAppQueryService);
        this.restMovimientoAppMockMvc = MockMvcBuilders.standaloneSetup(movimientoAppResource)
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
    public static MovimientoApp createEntity(EntityManager em) {
        MovimientoApp movimientoApp = new MovimientoApp()
            .cuentaDebitoCbu(DEFAULT_CUENTA_DEBITO_CBU)
            .cuentaDebitoAlias(DEFAULT_CUENTA_DEBITO_ALIAS)
            .cuentaDebitoPropia(DEFAULT_CUENTA_DEBITO_PROPIA)
            .cuentaCreditoCbu(DEFAULT_CUENTA_CREDITO_CBU)
            .cuentaCreditoAlias(DEFAULT_CUENTA_CREDITO_ALIAS)
            .cuentaCreditoPropia(DEFAULT_CUENTA_CREDITO_PROPIA)
            .moneda(DEFAULT_MONEDA)
            .monto(DEFAULT_MONTO)
            .timestamp(DEFAULT_TIMESTAMP)
            .descripcion(DEFAULT_DESCRIPCION)
            .identificacion(DEFAULT_IDENTIFICACION)
            .consolidado(DEFAULT_CONSOLIDADO)
            .consolidadoTimestamp(DEFAULT_CONSOLIDADO_TIMESTAMP);
        // Add required entity
        Banco bancoDebito = BancoResourceIntTest.createEntity(em);
        em.persist(bancoDebito);
        em.flush();
        movimientoApp.setBancoDebito(bancoDebito);
        // Add required entity
        Banco bancoCredito = BancoResourceIntTest.createEntity(em);
        em.persist(bancoCredito);
        em.flush();
        movimientoApp.setBancoCredito(bancoCredito);
        return movimientoApp;
    }

    @Before
    public void initTest() {
        movimientoAppSearchRepository.deleteAll();
        movimientoApp = createEntity(em);
    }

    @Test
    @Transactional
    public void createMovimientoApp() throws Exception {
        int databaseSizeBeforeCreate = movimientoAppRepository.findAll().size();

        // Create the MovimientoApp
        restMovimientoAppMockMvc.perform(post("/api/movimiento-apps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(movimientoApp)))
            .andExpect(status().isCreated());

        // Validate the MovimientoApp in the database
        List<MovimientoApp> movimientoAppList = movimientoAppRepository.findAll();
        assertThat(movimientoAppList).hasSize(databaseSizeBeforeCreate + 1);
        MovimientoApp testMovimientoApp = movimientoAppList.get(movimientoAppList.size() - 1);
        assertThat(testMovimientoApp.getCuentaDebitoCbu()).isEqualTo(DEFAULT_CUENTA_DEBITO_CBU);
        assertThat(testMovimientoApp.getCuentaDebitoAlias()).isEqualTo(DEFAULT_CUENTA_DEBITO_ALIAS);
        assertThat(testMovimientoApp.isCuentaDebitoPropia()).isEqualTo(DEFAULT_CUENTA_DEBITO_PROPIA);
        assertThat(testMovimientoApp.getCuentaCreditoCbu()).isEqualTo(DEFAULT_CUENTA_CREDITO_CBU);
        assertThat(testMovimientoApp.getCuentaCreditoAlias()).isEqualTo(DEFAULT_CUENTA_CREDITO_ALIAS);
        assertThat(testMovimientoApp.isCuentaCreditoPropia()).isEqualTo(DEFAULT_CUENTA_CREDITO_PROPIA);
        assertThat(testMovimientoApp.getMoneda()).isEqualTo(DEFAULT_MONEDA);
        assertThat(testMovimientoApp.getMonto()).isEqualTo(DEFAULT_MONTO);
        assertThat(testMovimientoApp.getTimestamp()).isEqualTo(DEFAULT_TIMESTAMP);
        assertThat(testMovimientoApp.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testMovimientoApp.getIdentificacion()).isEqualTo(DEFAULT_IDENTIFICACION);
        assertThat(testMovimientoApp.isConsolidado()).isEqualTo(DEFAULT_CONSOLIDADO);
        assertThat(testMovimientoApp.getConsolidadoTimestamp()).isEqualTo(DEFAULT_CONSOLIDADO_TIMESTAMP);

        // Validate the MovimientoApp in Elasticsearch
        MovimientoApp movimientoAppEs = movimientoAppSearchRepository.findOne(testMovimientoApp.getId());
        assertThat(movimientoAppEs).isEqualToIgnoringGivenFields(testMovimientoApp);
    }

    @Test
    @Transactional
    public void createMovimientoAppWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = movimientoAppRepository.findAll().size();

        // Create the MovimientoApp with an existing ID
        movimientoApp.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMovimientoAppMockMvc.perform(post("/api/movimiento-apps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(movimientoApp)))
            .andExpect(status().isBadRequest());

        // Validate the MovimientoApp in the database
        List<MovimientoApp> movimientoAppList = movimientoAppRepository.findAll();
        assertThat(movimientoAppList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCuentaDebitoPropiaIsRequired() throws Exception {
        int databaseSizeBeforeTest = movimientoAppRepository.findAll().size();
        // set the field null
        movimientoApp.setCuentaDebitoPropia(null);

        // Create the MovimientoApp, which fails.

        restMovimientoAppMockMvc.perform(post("/api/movimiento-apps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(movimientoApp)))
            .andExpect(status().isBadRequest());

        List<MovimientoApp> movimientoAppList = movimientoAppRepository.findAll();
        assertThat(movimientoAppList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCuentaCreditoPropiaIsRequired() throws Exception {
        int databaseSizeBeforeTest = movimientoAppRepository.findAll().size();
        // set the field null
        movimientoApp.setCuentaCreditoPropia(null);

        // Create the MovimientoApp, which fails.

        restMovimientoAppMockMvc.perform(post("/api/movimiento-apps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(movimientoApp)))
            .andExpect(status().isBadRequest());

        List<MovimientoApp> movimientoAppList = movimientoAppRepository.findAll();
        assertThat(movimientoAppList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMonedaIsRequired() throws Exception {
        int databaseSizeBeforeTest = movimientoAppRepository.findAll().size();
        // set the field null
        movimientoApp.setMoneda(null);

        // Create the MovimientoApp, which fails.

        restMovimientoAppMockMvc.perform(post("/api/movimiento-apps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(movimientoApp)))
            .andExpect(status().isBadRequest());

        List<MovimientoApp> movimientoAppList = movimientoAppRepository.findAll();
        assertThat(movimientoAppList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMontoIsRequired() throws Exception {
        int databaseSizeBeforeTest = movimientoAppRepository.findAll().size();
        // set the field null
        movimientoApp.setMonto(null);

        // Create the MovimientoApp, which fails.

        restMovimientoAppMockMvc.perform(post("/api/movimiento-apps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(movimientoApp)))
            .andExpect(status().isBadRequest());

        List<MovimientoApp> movimientoAppList = movimientoAppRepository.findAll();
        assertThat(movimientoAppList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTimestampIsRequired() throws Exception {
        int databaseSizeBeforeTest = movimientoAppRepository.findAll().size();
        // set the field null
        movimientoApp.setTimestamp(null);

        // Create the MovimientoApp, which fails.

        restMovimientoAppMockMvc.perform(post("/api/movimiento-apps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(movimientoApp)))
            .andExpect(status().isBadRequest());

        List<MovimientoApp> movimientoAppList = movimientoAppRepository.findAll();
        assertThat(movimientoAppList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkConsolidadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = movimientoAppRepository.findAll().size();
        // set the field null
        movimientoApp.setConsolidado(null);

        // Create the MovimientoApp, which fails.

        restMovimientoAppMockMvc.perform(post("/api/movimiento-apps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(movimientoApp)))
            .andExpect(status().isBadRequest());

        List<MovimientoApp> movimientoAppList = movimientoAppRepository.findAll();
        assertThat(movimientoAppList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMovimientoApps() throws Exception {
        // Initialize the database
        movimientoAppRepository.saveAndFlush(movimientoApp);

        // Get all the movimientoAppList
        restMovimientoAppMockMvc.perform(get("/api/movimiento-apps?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(movimientoApp.getId().intValue())))
            .andExpect(jsonPath("$.[*].cuentaDebitoCbu").value(hasItem(DEFAULT_CUENTA_DEBITO_CBU.toString())))
            .andExpect(jsonPath("$.[*].cuentaDebitoAlias").value(hasItem(DEFAULT_CUENTA_DEBITO_ALIAS.toString())))
            .andExpect(jsonPath("$.[*].cuentaDebitoPropia").value(hasItem(DEFAULT_CUENTA_DEBITO_PROPIA.booleanValue())))
            .andExpect(jsonPath("$.[*].cuentaCreditoCbu").value(hasItem(DEFAULT_CUENTA_CREDITO_CBU.toString())))
            .andExpect(jsonPath("$.[*].cuentaCreditoAlias").value(hasItem(DEFAULT_CUENTA_CREDITO_ALIAS.toString())))
            .andExpect(jsonPath("$.[*].cuentaCreditoPropia").value(hasItem(DEFAULT_CUENTA_CREDITO_PROPIA.booleanValue())))
            .andExpect(jsonPath("$.[*].moneda").value(hasItem(DEFAULT_MONEDA.toString())))
            .andExpect(jsonPath("$.[*].monto").value(hasItem(DEFAULT_MONTO.intValue())))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString())))
            .andExpect(jsonPath("$.[*].identificacion").value(hasItem(DEFAULT_IDENTIFICACION.toString())))
            .andExpect(jsonPath("$.[*].consolidado").value(hasItem(DEFAULT_CONSOLIDADO.booleanValue())))
            .andExpect(jsonPath("$.[*].consolidadoTimestamp").value(hasItem(DEFAULT_CONSOLIDADO_TIMESTAMP.toString())));
    }

    @Test
    @Transactional
    public void getMovimientoApp() throws Exception {
        // Initialize the database
        movimientoAppRepository.saveAndFlush(movimientoApp);

        // Get the movimientoApp
        restMovimientoAppMockMvc.perform(get("/api/movimiento-apps/{id}", movimientoApp.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(movimientoApp.getId().intValue()))
            .andExpect(jsonPath("$.cuentaDebitoCbu").value(DEFAULT_CUENTA_DEBITO_CBU.toString()))
            .andExpect(jsonPath("$.cuentaDebitoAlias").value(DEFAULT_CUENTA_DEBITO_ALIAS.toString()))
            .andExpect(jsonPath("$.cuentaDebitoPropia").value(DEFAULT_CUENTA_DEBITO_PROPIA.booleanValue()))
            .andExpect(jsonPath("$.cuentaCreditoCbu").value(DEFAULT_CUENTA_CREDITO_CBU.toString()))
            .andExpect(jsonPath("$.cuentaCreditoAlias").value(DEFAULT_CUENTA_CREDITO_ALIAS.toString()))
            .andExpect(jsonPath("$.cuentaCreditoPropia").value(DEFAULT_CUENTA_CREDITO_PROPIA.booleanValue()))
            .andExpect(jsonPath("$.moneda").value(DEFAULT_MONEDA.toString()))
            .andExpect(jsonPath("$.monto").value(DEFAULT_MONTO.intValue()))
            .andExpect(jsonPath("$.timestamp").value(DEFAULT_TIMESTAMP.toString()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION.toString()))
            .andExpect(jsonPath("$.identificacion").value(DEFAULT_IDENTIFICACION.toString()))
            .andExpect(jsonPath("$.consolidado").value(DEFAULT_CONSOLIDADO.booleanValue()))
            .andExpect(jsonPath("$.consolidadoTimestamp").value(DEFAULT_CONSOLIDADO_TIMESTAMP.toString()));
    }

    @Test
    @Transactional
    public void getAllMovimientoAppsByCuentaDebitoCbuIsEqualToSomething() throws Exception {
        // Initialize the database
        movimientoAppRepository.saveAndFlush(movimientoApp);

        // Get all the movimientoAppList where cuentaDebitoCbu equals to DEFAULT_CUENTA_DEBITO_CBU
        defaultMovimientoAppShouldBeFound("cuentaDebitoCbu.equals=" + DEFAULT_CUENTA_DEBITO_CBU);

        // Get all the movimientoAppList where cuentaDebitoCbu equals to UPDATED_CUENTA_DEBITO_CBU
        defaultMovimientoAppShouldNotBeFound("cuentaDebitoCbu.equals=" + UPDATED_CUENTA_DEBITO_CBU);
    }

    @Test
    @Transactional
    public void getAllMovimientoAppsByCuentaDebitoCbuIsInShouldWork() throws Exception {
        // Initialize the database
        movimientoAppRepository.saveAndFlush(movimientoApp);

        // Get all the movimientoAppList where cuentaDebitoCbu in DEFAULT_CUENTA_DEBITO_CBU or UPDATED_CUENTA_DEBITO_CBU
        defaultMovimientoAppShouldBeFound("cuentaDebitoCbu.in=" + DEFAULT_CUENTA_DEBITO_CBU + "," + UPDATED_CUENTA_DEBITO_CBU);

        // Get all the movimientoAppList where cuentaDebitoCbu equals to UPDATED_CUENTA_DEBITO_CBU
        defaultMovimientoAppShouldNotBeFound("cuentaDebitoCbu.in=" + UPDATED_CUENTA_DEBITO_CBU);
    }

    @Test
    @Transactional
    public void getAllMovimientoAppsByCuentaDebitoCbuIsNullOrNotNull() throws Exception {
        // Initialize the database
        movimientoAppRepository.saveAndFlush(movimientoApp);

        // Get all the movimientoAppList where cuentaDebitoCbu is not null
        defaultMovimientoAppShouldBeFound("cuentaDebitoCbu.specified=true");

        // Get all the movimientoAppList where cuentaDebitoCbu is null
        defaultMovimientoAppShouldNotBeFound("cuentaDebitoCbu.specified=false");
    }

    @Test
    @Transactional
    public void getAllMovimientoAppsByCuentaDebitoAliasIsEqualToSomething() throws Exception {
        // Initialize the database
        movimientoAppRepository.saveAndFlush(movimientoApp);

        // Get all the movimientoAppList where cuentaDebitoAlias equals to DEFAULT_CUENTA_DEBITO_ALIAS
        defaultMovimientoAppShouldBeFound("cuentaDebitoAlias.equals=" + DEFAULT_CUENTA_DEBITO_ALIAS);

        // Get all the movimientoAppList where cuentaDebitoAlias equals to UPDATED_CUENTA_DEBITO_ALIAS
        defaultMovimientoAppShouldNotBeFound("cuentaDebitoAlias.equals=" + UPDATED_CUENTA_DEBITO_ALIAS);
    }

    @Test
    @Transactional
    public void getAllMovimientoAppsByCuentaDebitoAliasIsInShouldWork() throws Exception {
        // Initialize the database
        movimientoAppRepository.saveAndFlush(movimientoApp);

        // Get all the movimientoAppList where cuentaDebitoAlias in DEFAULT_CUENTA_DEBITO_ALIAS or UPDATED_CUENTA_DEBITO_ALIAS
        defaultMovimientoAppShouldBeFound("cuentaDebitoAlias.in=" + DEFAULT_CUENTA_DEBITO_ALIAS + "," + UPDATED_CUENTA_DEBITO_ALIAS);

        // Get all the movimientoAppList where cuentaDebitoAlias equals to UPDATED_CUENTA_DEBITO_ALIAS
        defaultMovimientoAppShouldNotBeFound("cuentaDebitoAlias.in=" + UPDATED_CUENTA_DEBITO_ALIAS);
    }

    @Test
    @Transactional
    public void getAllMovimientoAppsByCuentaDebitoAliasIsNullOrNotNull() throws Exception {
        // Initialize the database
        movimientoAppRepository.saveAndFlush(movimientoApp);

        // Get all the movimientoAppList where cuentaDebitoAlias is not null
        defaultMovimientoAppShouldBeFound("cuentaDebitoAlias.specified=true");

        // Get all the movimientoAppList where cuentaDebitoAlias is null
        defaultMovimientoAppShouldNotBeFound("cuentaDebitoAlias.specified=false");
    }

    @Test
    @Transactional
    public void getAllMovimientoAppsByCuentaDebitoPropiaIsEqualToSomething() throws Exception {
        // Initialize the database
        movimientoAppRepository.saveAndFlush(movimientoApp);

        // Get all the movimientoAppList where cuentaDebitoPropia equals to DEFAULT_CUENTA_DEBITO_PROPIA
        defaultMovimientoAppShouldBeFound("cuentaDebitoPropia.equals=" + DEFAULT_CUENTA_DEBITO_PROPIA);

        // Get all the movimientoAppList where cuentaDebitoPropia equals to UPDATED_CUENTA_DEBITO_PROPIA
        defaultMovimientoAppShouldNotBeFound("cuentaDebitoPropia.equals=" + UPDATED_CUENTA_DEBITO_PROPIA);
    }

    @Test
    @Transactional
    public void getAllMovimientoAppsByCuentaDebitoPropiaIsInShouldWork() throws Exception {
        // Initialize the database
        movimientoAppRepository.saveAndFlush(movimientoApp);

        // Get all the movimientoAppList where cuentaDebitoPropia in DEFAULT_CUENTA_DEBITO_PROPIA or UPDATED_CUENTA_DEBITO_PROPIA
        defaultMovimientoAppShouldBeFound("cuentaDebitoPropia.in=" + DEFAULT_CUENTA_DEBITO_PROPIA + "," + UPDATED_CUENTA_DEBITO_PROPIA);

        // Get all the movimientoAppList where cuentaDebitoPropia equals to UPDATED_CUENTA_DEBITO_PROPIA
        defaultMovimientoAppShouldNotBeFound("cuentaDebitoPropia.in=" + UPDATED_CUENTA_DEBITO_PROPIA);
    }

    @Test
    @Transactional
    public void getAllMovimientoAppsByCuentaDebitoPropiaIsNullOrNotNull() throws Exception {
        // Initialize the database
        movimientoAppRepository.saveAndFlush(movimientoApp);

        // Get all the movimientoAppList where cuentaDebitoPropia is not null
        defaultMovimientoAppShouldBeFound("cuentaDebitoPropia.specified=true");

        // Get all the movimientoAppList where cuentaDebitoPropia is null
        defaultMovimientoAppShouldNotBeFound("cuentaDebitoPropia.specified=false");
    }

    @Test
    @Transactional
    public void getAllMovimientoAppsByCuentaCreditoCbuIsEqualToSomething() throws Exception {
        // Initialize the database
        movimientoAppRepository.saveAndFlush(movimientoApp);

        // Get all the movimientoAppList where cuentaCreditoCbu equals to DEFAULT_CUENTA_CREDITO_CBU
        defaultMovimientoAppShouldBeFound("cuentaCreditoCbu.equals=" + DEFAULT_CUENTA_CREDITO_CBU);

        // Get all the movimientoAppList where cuentaCreditoCbu equals to UPDATED_CUENTA_CREDITO_CBU
        defaultMovimientoAppShouldNotBeFound("cuentaCreditoCbu.equals=" + UPDATED_CUENTA_CREDITO_CBU);
    }

    @Test
    @Transactional
    public void getAllMovimientoAppsByCuentaCreditoCbuIsInShouldWork() throws Exception {
        // Initialize the database
        movimientoAppRepository.saveAndFlush(movimientoApp);

        // Get all the movimientoAppList where cuentaCreditoCbu in DEFAULT_CUENTA_CREDITO_CBU or UPDATED_CUENTA_CREDITO_CBU
        defaultMovimientoAppShouldBeFound("cuentaCreditoCbu.in=" + DEFAULT_CUENTA_CREDITO_CBU + "," + UPDATED_CUENTA_CREDITO_CBU);

        // Get all the movimientoAppList where cuentaCreditoCbu equals to UPDATED_CUENTA_CREDITO_CBU
        defaultMovimientoAppShouldNotBeFound("cuentaCreditoCbu.in=" + UPDATED_CUENTA_CREDITO_CBU);
    }

    @Test
    @Transactional
    public void getAllMovimientoAppsByCuentaCreditoCbuIsNullOrNotNull() throws Exception {
        // Initialize the database
        movimientoAppRepository.saveAndFlush(movimientoApp);

        // Get all the movimientoAppList where cuentaCreditoCbu is not null
        defaultMovimientoAppShouldBeFound("cuentaCreditoCbu.specified=true");

        // Get all the movimientoAppList where cuentaCreditoCbu is null
        defaultMovimientoAppShouldNotBeFound("cuentaCreditoCbu.specified=false");
    }

    @Test
    @Transactional
    public void getAllMovimientoAppsByCuentaCreditoAliasIsEqualToSomething() throws Exception {
        // Initialize the database
        movimientoAppRepository.saveAndFlush(movimientoApp);

        // Get all the movimientoAppList where cuentaCreditoAlias equals to DEFAULT_CUENTA_CREDITO_ALIAS
        defaultMovimientoAppShouldBeFound("cuentaCreditoAlias.equals=" + DEFAULT_CUENTA_CREDITO_ALIAS);

        // Get all the movimientoAppList where cuentaCreditoAlias equals to UPDATED_CUENTA_CREDITO_ALIAS
        defaultMovimientoAppShouldNotBeFound("cuentaCreditoAlias.equals=" + UPDATED_CUENTA_CREDITO_ALIAS);
    }

    @Test
    @Transactional
    public void getAllMovimientoAppsByCuentaCreditoAliasIsInShouldWork() throws Exception {
        // Initialize the database
        movimientoAppRepository.saveAndFlush(movimientoApp);

        // Get all the movimientoAppList where cuentaCreditoAlias in DEFAULT_CUENTA_CREDITO_ALIAS or UPDATED_CUENTA_CREDITO_ALIAS
        defaultMovimientoAppShouldBeFound("cuentaCreditoAlias.in=" + DEFAULT_CUENTA_CREDITO_ALIAS + "," + UPDATED_CUENTA_CREDITO_ALIAS);

        // Get all the movimientoAppList where cuentaCreditoAlias equals to UPDATED_CUENTA_CREDITO_ALIAS
        defaultMovimientoAppShouldNotBeFound("cuentaCreditoAlias.in=" + UPDATED_CUENTA_CREDITO_ALIAS);
    }

    @Test
    @Transactional
    public void getAllMovimientoAppsByCuentaCreditoAliasIsNullOrNotNull() throws Exception {
        // Initialize the database
        movimientoAppRepository.saveAndFlush(movimientoApp);

        // Get all the movimientoAppList where cuentaCreditoAlias is not null
        defaultMovimientoAppShouldBeFound("cuentaCreditoAlias.specified=true");

        // Get all the movimientoAppList where cuentaCreditoAlias is null
        defaultMovimientoAppShouldNotBeFound("cuentaCreditoAlias.specified=false");
    }

    @Test
    @Transactional
    public void getAllMovimientoAppsByCuentaCreditoPropiaIsEqualToSomething() throws Exception {
        // Initialize the database
        movimientoAppRepository.saveAndFlush(movimientoApp);

        // Get all the movimientoAppList where cuentaCreditoPropia equals to DEFAULT_CUENTA_CREDITO_PROPIA
        defaultMovimientoAppShouldBeFound("cuentaCreditoPropia.equals=" + DEFAULT_CUENTA_CREDITO_PROPIA);

        // Get all the movimientoAppList where cuentaCreditoPropia equals to UPDATED_CUENTA_CREDITO_PROPIA
        defaultMovimientoAppShouldNotBeFound("cuentaCreditoPropia.equals=" + UPDATED_CUENTA_CREDITO_PROPIA);
    }

    @Test
    @Transactional
    public void getAllMovimientoAppsByCuentaCreditoPropiaIsInShouldWork() throws Exception {
        // Initialize the database
        movimientoAppRepository.saveAndFlush(movimientoApp);

        // Get all the movimientoAppList where cuentaCreditoPropia in DEFAULT_CUENTA_CREDITO_PROPIA or UPDATED_CUENTA_CREDITO_PROPIA
        defaultMovimientoAppShouldBeFound("cuentaCreditoPropia.in=" + DEFAULT_CUENTA_CREDITO_PROPIA + "," + UPDATED_CUENTA_CREDITO_PROPIA);

        // Get all the movimientoAppList where cuentaCreditoPropia equals to UPDATED_CUENTA_CREDITO_PROPIA
        defaultMovimientoAppShouldNotBeFound("cuentaCreditoPropia.in=" + UPDATED_CUENTA_CREDITO_PROPIA);
    }

    @Test
    @Transactional
    public void getAllMovimientoAppsByCuentaCreditoPropiaIsNullOrNotNull() throws Exception {
        // Initialize the database
        movimientoAppRepository.saveAndFlush(movimientoApp);

        // Get all the movimientoAppList where cuentaCreditoPropia is not null
        defaultMovimientoAppShouldBeFound("cuentaCreditoPropia.specified=true");

        // Get all the movimientoAppList where cuentaCreditoPropia is null
        defaultMovimientoAppShouldNotBeFound("cuentaCreditoPropia.specified=false");
    }

    @Test
    @Transactional
    public void getAllMovimientoAppsByMonedaIsEqualToSomething() throws Exception {
        // Initialize the database
        movimientoAppRepository.saveAndFlush(movimientoApp);

        // Get all the movimientoAppList where moneda equals to DEFAULT_MONEDA
        defaultMovimientoAppShouldBeFound("moneda.equals=" + DEFAULT_MONEDA);

        // Get all the movimientoAppList where moneda equals to UPDATED_MONEDA
        defaultMovimientoAppShouldNotBeFound("moneda.equals=" + UPDATED_MONEDA);
    }

    @Test
    @Transactional
    public void getAllMovimientoAppsByMonedaIsInShouldWork() throws Exception {
        // Initialize the database
        movimientoAppRepository.saveAndFlush(movimientoApp);

        // Get all the movimientoAppList where moneda in DEFAULT_MONEDA or UPDATED_MONEDA
        defaultMovimientoAppShouldBeFound("moneda.in=" + DEFAULT_MONEDA + "," + UPDATED_MONEDA);

        // Get all the movimientoAppList where moneda equals to UPDATED_MONEDA
        defaultMovimientoAppShouldNotBeFound("moneda.in=" + UPDATED_MONEDA);
    }

    @Test
    @Transactional
    public void getAllMovimientoAppsByMonedaIsNullOrNotNull() throws Exception {
        // Initialize the database
        movimientoAppRepository.saveAndFlush(movimientoApp);

        // Get all the movimientoAppList where moneda is not null
        defaultMovimientoAppShouldBeFound("moneda.specified=true");

        // Get all the movimientoAppList where moneda is null
        defaultMovimientoAppShouldNotBeFound("moneda.specified=false");
    }

    @Test
    @Transactional
    public void getAllMovimientoAppsByMontoIsEqualToSomething() throws Exception {
        // Initialize the database
        movimientoAppRepository.saveAndFlush(movimientoApp);

        // Get all the movimientoAppList where monto equals to DEFAULT_MONTO
        defaultMovimientoAppShouldBeFound("monto.equals=" + DEFAULT_MONTO);

        // Get all the movimientoAppList where monto equals to UPDATED_MONTO
        defaultMovimientoAppShouldNotBeFound("monto.equals=" + UPDATED_MONTO);
    }

    @Test
    @Transactional
    public void getAllMovimientoAppsByMontoIsInShouldWork() throws Exception {
        // Initialize the database
        movimientoAppRepository.saveAndFlush(movimientoApp);

        // Get all the movimientoAppList where monto in DEFAULT_MONTO or UPDATED_MONTO
        defaultMovimientoAppShouldBeFound("monto.in=" + DEFAULT_MONTO + "," + UPDATED_MONTO);

        // Get all the movimientoAppList where monto equals to UPDATED_MONTO
        defaultMovimientoAppShouldNotBeFound("monto.in=" + UPDATED_MONTO);
    }

    @Test
    @Transactional
    public void getAllMovimientoAppsByMontoIsNullOrNotNull() throws Exception {
        // Initialize the database
        movimientoAppRepository.saveAndFlush(movimientoApp);

        // Get all the movimientoAppList where monto is not null
        defaultMovimientoAppShouldBeFound("monto.specified=true");

        // Get all the movimientoAppList where monto is null
        defaultMovimientoAppShouldNotBeFound("monto.specified=false");
    }

    @Test
    @Transactional
    public void getAllMovimientoAppsByTimestampIsEqualToSomething() throws Exception {
        // Initialize the database
        movimientoAppRepository.saveAndFlush(movimientoApp);

        // Get all the movimientoAppList where timestamp equals to DEFAULT_TIMESTAMP
        defaultMovimientoAppShouldBeFound("timestamp.equals=" + DEFAULT_TIMESTAMP);

        // Get all the movimientoAppList where timestamp equals to UPDATED_TIMESTAMP
        defaultMovimientoAppShouldNotBeFound("timestamp.equals=" + UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllMovimientoAppsByTimestampIsInShouldWork() throws Exception {
        // Initialize the database
        movimientoAppRepository.saveAndFlush(movimientoApp);

        // Get all the movimientoAppList where timestamp in DEFAULT_TIMESTAMP or UPDATED_TIMESTAMP
        defaultMovimientoAppShouldBeFound("timestamp.in=" + DEFAULT_TIMESTAMP + "," + UPDATED_TIMESTAMP);

        // Get all the movimientoAppList where timestamp equals to UPDATED_TIMESTAMP
        defaultMovimientoAppShouldNotBeFound("timestamp.in=" + UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllMovimientoAppsByTimestampIsNullOrNotNull() throws Exception {
        // Initialize the database
        movimientoAppRepository.saveAndFlush(movimientoApp);

        // Get all the movimientoAppList where timestamp is not null
        defaultMovimientoAppShouldBeFound("timestamp.specified=true");

        // Get all the movimientoAppList where timestamp is null
        defaultMovimientoAppShouldNotBeFound("timestamp.specified=false");
    }

    @Test
    @Transactional
    public void getAllMovimientoAppsByDescripcionIsEqualToSomething() throws Exception {
        // Initialize the database
        movimientoAppRepository.saveAndFlush(movimientoApp);

        // Get all the movimientoAppList where descripcion equals to DEFAULT_DESCRIPCION
        defaultMovimientoAppShouldBeFound("descripcion.equals=" + DEFAULT_DESCRIPCION);

        // Get all the movimientoAppList where descripcion equals to UPDATED_DESCRIPCION
        defaultMovimientoAppShouldNotBeFound("descripcion.equals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    public void getAllMovimientoAppsByDescripcionIsInShouldWork() throws Exception {
        // Initialize the database
        movimientoAppRepository.saveAndFlush(movimientoApp);

        // Get all the movimientoAppList where descripcion in DEFAULT_DESCRIPCION or UPDATED_DESCRIPCION
        defaultMovimientoAppShouldBeFound("descripcion.in=" + DEFAULT_DESCRIPCION + "," + UPDATED_DESCRIPCION);

        // Get all the movimientoAppList where descripcion equals to UPDATED_DESCRIPCION
        defaultMovimientoAppShouldNotBeFound("descripcion.in=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    public void getAllMovimientoAppsByDescripcionIsNullOrNotNull() throws Exception {
        // Initialize the database
        movimientoAppRepository.saveAndFlush(movimientoApp);

        // Get all the movimientoAppList where descripcion is not null
        defaultMovimientoAppShouldBeFound("descripcion.specified=true");

        // Get all the movimientoAppList where descripcion is null
        defaultMovimientoAppShouldNotBeFound("descripcion.specified=false");
    }

    @Test
    @Transactional
    public void getAllMovimientoAppsByIdentificacionIsEqualToSomething() throws Exception {
        // Initialize the database
        movimientoAppRepository.saveAndFlush(movimientoApp);

        // Get all the movimientoAppList where identificacion equals to DEFAULT_IDENTIFICACION
        defaultMovimientoAppShouldBeFound("identificacion.equals=" + DEFAULT_IDENTIFICACION);

        // Get all the movimientoAppList where identificacion equals to UPDATED_IDENTIFICACION
        defaultMovimientoAppShouldNotBeFound("identificacion.equals=" + UPDATED_IDENTIFICACION);
    }

    @Test
    @Transactional
    public void getAllMovimientoAppsByIdentificacionIsInShouldWork() throws Exception {
        // Initialize the database
        movimientoAppRepository.saveAndFlush(movimientoApp);

        // Get all the movimientoAppList where identificacion in DEFAULT_IDENTIFICACION or UPDATED_IDENTIFICACION
        defaultMovimientoAppShouldBeFound("identificacion.in=" + DEFAULT_IDENTIFICACION + "," + UPDATED_IDENTIFICACION);

        // Get all the movimientoAppList where identificacion equals to UPDATED_IDENTIFICACION
        defaultMovimientoAppShouldNotBeFound("identificacion.in=" + UPDATED_IDENTIFICACION);
    }

    @Test
    @Transactional
    public void getAllMovimientoAppsByIdentificacionIsNullOrNotNull() throws Exception {
        // Initialize the database
        movimientoAppRepository.saveAndFlush(movimientoApp);

        // Get all the movimientoAppList where identificacion is not null
        defaultMovimientoAppShouldBeFound("identificacion.specified=true");

        // Get all the movimientoAppList where identificacion is null
        defaultMovimientoAppShouldNotBeFound("identificacion.specified=false");
    }

    @Test
    @Transactional
    public void getAllMovimientoAppsByConsolidadoIsEqualToSomething() throws Exception {
        // Initialize the database
        movimientoAppRepository.saveAndFlush(movimientoApp);

        // Get all the movimientoAppList where consolidado equals to DEFAULT_CONSOLIDADO
        defaultMovimientoAppShouldBeFound("consolidado.equals=" + DEFAULT_CONSOLIDADO);

        // Get all the movimientoAppList where consolidado equals to UPDATED_CONSOLIDADO
        defaultMovimientoAppShouldNotBeFound("consolidado.equals=" + UPDATED_CONSOLIDADO);
    }

    @Test
    @Transactional
    public void getAllMovimientoAppsByConsolidadoIsInShouldWork() throws Exception {
        // Initialize the database
        movimientoAppRepository.saveAndFlush(movimientoApp);

        // Get all the movimientoAppList where consolidado in DEFAULT_CONSOLIDADO or UPDATED_CONSOLIDADO
        defaultMovimientoAppShouldBeFound("consolidado.in=" + DEFAULT_CONSOLIDADO + "," + UPDATED_CONSOLIDADO);

        // Get all the movimientoAppList where consolidado equals to UPDATED_CONSOLIDADO
        defaultMovimientoAppShouldNotBeFound("consolidado.in=" + UPDATED_CONSOLIDADO);
    }

    @Test
    @Transactional
    public void getAllMovimientoAppsByConsolidadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        movimientoAppRepository.saveAndFlush(movimientoApp);

        // Get all the movimientoAppList where consolidado is not null
        defaultMovimientoAppShouldBeFound("consolidado.specified=true");

        // Get all the movimientoAppList where consolidado is null
        defaultMovimientoAppShouldNotBeFound("consolidado.specified=false");
    }

    @Test
    @Transactional
    public void getAllMovimientoAppsByConsolidadoTimestampIsEqualToSomething() throws Exception {
        // Initialize the database
        movimientoAppRepository.saveAndFlush(movimientoApp);

        // Get all the movimientoAppList where consolidadoTimestamp equals to DEFAULT_CONSOLIDADO_TIMESTAMP
        defaultMovimientoAppShouldBeFound("consolidadoTimestamp.equals=" + DEFAULT_CONSOLIDADO_TIMESTAMP);

        // Get all the movimientoAppList where consolidadoTimestamp equals to UPDATED_CONSOLIDADO_TIMESTAMP
        defaultMovimientoAppShouldNotBeFound("consolidadoTimestamp.equals=" + UPDATED_CONSOLIDADO_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllMovimientoAppsByConsolidadoTimestampIsInShouldWork() throws Exception {
        // Initialize the database
        movimientoAppRepository.saveAndFlush(movimientoApp);

        // Get all the movimientoAppList where consolidadoTimestamp in DEFAULT_CONSOLIDADO_TIMESTAMP or UPDATED_CONSOLIDADO_TIMESTAMP
        defaultMovimientoAppShouldBeFound("consolidadoTimestamp.in=" + DEFAULT_CONSOLIDADO_TIMESTAMP + "," + UPDATED_CONSOLIDADO_TIMESTAMP);

        // Get all the movimientoAppList where consolidadoTimestamp equals to UPDATED_CONSOLIDADO_TIMESTAMP
        defaultMovimientoAppShouldNotBeFound("consolidadoTimestamp.in=" + UPDATED_CONSOLIDADO_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllMovimientoAppsByConsolidadoTimestampIsNullOrNotNull() throws Exception {
        // Initialize the database
        movimientoAppRepository.saveAndFlush(movimientoApp);

        // Get all the movimientoAppList where consolidadoTimestamp is not null
        defaultMovimientoAppShouldBeFound("consolidadoTimestamp.specified=true");

        // Get all the movimientoAppList where consolidadoTimestamp is null
        defaultMovimientoAppShouldNotBeFound("consolidadoTimestamp.specified=false");
    }

    @Test
    @Transactional
    public void getAllMovimientoAppsByBancoDebitoIsEqualToSomething() throws Exception {
        // Initialize the database
        Banco bancoDebito = BancoResourceIntTest.createEntity(em);
        em.persist(bancoDebito);
        em.flush();
        movimientoApp.setBancoDebito(bancoDebito);
        movimientoAppRepository.saveAndFlush(movimientoApp);
        Long bancoDebitoId = bancoDebito.getId();

        // Get all the movimientoAppList where bancoDebito equals to bancoDebitoId
        defaultMovimientoAppShouldBeFound("bancoDebitoId.equals=" + bancoDebitoId);

        // Get all the movimientoAppList where bancoDebito equals to bancoDebitoId + 1
        defaultMovimientoAppShouldNotBeFound("bancoDebitoId.equals=" + (bancoDebitoId + 1));
    }


    @Test
    @Transactional
    public void getAllMovimientoAppsByBancoCreditoIsEqualToSomething() throws Exception {
        // Initialize the database
        Banco bancoCredito = BancoResourceIntTest.createEntity(em);
        em.persist(bancoCredito);
        em.flush();
        movimientoApp.setBancoCredito(bancoCredito);
        movimientoAppRepository.saveAndFlush(movimientoApp);
        Long bancoCreditoId = bancoCredito.getId();

        // Get all the movimientoAppList where bancoCredito equals to bancoCreditoId
        defaultMovimientoAppShouldBeFound("bancoCreditoId.equals=" + bancoCreditoId);

        // Get all the movimientoAppList where bancoCredito equals to bancoCreditoId + 1
        defaultMovimientoAppShouldNotBeFound("bancoCreditoId.equals=" + (bancoCreditoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultMovimientoAppShouldBeFound(String filter) throws Exception {
        restMovimientoAppMockMvc.perform(get("/api/movimiento-apps?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(movimientoApp.getId().intValue())))
            .andExpect(jsonPath("$.[*].cuentaDebitoCbu").value(hasItem(DEFAULT_CUENTA_DEBITO_CBU.toString())))
            .andExpect(jsonPath("$.[*].cuentaDebitoAlias").value(hasItem(DEFAULT_CUENTA_DEBITO_ALIAS.toString())))
            .andExpect(jsonPath("$.[*].cuentaDebitoPropia").value(hasItem(DEFAULT_CUENTA_DEBITO_PROPIA.booleanValue())))
            .andExpect(jsonPath("$.[*].cuentaCreditoCbu").value(hasItem(DEFAULT_CUENTA_CREDITO_CBU.toString())))
            .andExpect(jsonPath("$.[*].cuentaCreditoAlias").value(hasItem(DEFAULT_CUENTA_CREDITO_ALIAS.toString())))
            .andExpect(jsonPath("$.[*].cuentaCreditoPropia").value(hasItem(DEFAULT_CUENTA_CREDITO_PROPIA.booleanValue())))
            .andExpect(jsonPath("$.[*].moneda").value(hasItem(DEFAULT_MONEDA.toString())))
            .andExpect(jsonPath("$.[*].monto").value(hasItem(DEFAULT_MONTO.intValue())))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString())))
            .andExpect(jsonPath("$.[*].identificacion").value(hasItem(DEFAULT_IDENTIFICACION.toString())))
            .andExpect(jsonPath("$.[*].consolidado").value(hasItem(DEFAULT_CONSOLIDADO.booleanValue())))
            .andExpect(jsonPath("$.[*].consolidadoTimestamp").value(hasItem(DEFAULT_CONSOLIDADO_TIMESTAMP.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultMovimientoAppShouldNotBeFound(String filter) throws Exception {
        restMovimientoAppMockMvc.perform(get("/api/movimiento-apps?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingMovimientoApp() throws Exception {
        // Get the movimientoApp
        restMovimientoAppMockMvc.perform(get("/api/movimiento-apps/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMovimientoApp() throws Exception {
        // Initialize the database
        movimientoAppService.save(movimientoApp);

        int databaseSizeBeforeUpdate = movimientoAppRepository.findAll().size();

        // Update the movimientoApp
        MovimientoApp updatedMovimientoApp = movimientoAppRepository.findOne(movimientoApp.getId());
        // Disconnect from session so that the updates on updatedMovimientoApp are not directly saved in db
        em.detach(updatedMovimientoApp);
        updatedMovimientoApp
            .cuentaDebitoCbu(UPDATED_CUENTA_DEBITO_CBU)
            .cuentaDebitoAlias(UPDATED_CUENTA_DEBITO_ALIAS)
            .cuentaDebitoPropia(UPDATED_CUENTA_DEBITO_PROPIA)
            .cuentaCreditoCbu(UPDATED_CUENTA_CREDITO_CBU)
            .cuentaCreditoAlias(UPDATED_CUENTA_CREDITO_ALIAS)
            .cuentaCreditoPropia(UPDATED_CUENTA_CREDITO_PROPIA)
            .moneda(UPDATED_MONEDA)
            .monto(UPDATED_MONTO)
            .timestamp(UPDATED_TIMESTAMP)
            .descripcion(UPDATED_DESCRIPCION)
            .identificacion(UPDATED_IDENTIFICACION)
            .consolidado(UPDATED_CONSOLIDADO)
            .consolidadoTimestamp(UPDATED_CONSOLIDADO_TIMESTAMP);

        restMovimientoAppMockMvc.perform(put("/api/movimiento-apps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMovimientoApp)))
            .andExpect(status().isOk());

        // Validate the MovimientoApp in the database
        List<MovimientoApp> movimientoAppList = movimientoAppRepository.findAll();
        assertThat(movimientoAppList).hasSize(databaseSizeBeforeUpdate);
        MovimientoApp testMovimientoApp = movimientoAppList.get(movimientoAppList.size() - 1);
        assertThat(testMovimientoApp.getCuentaDebitoCbu()).isEqualTo(UPDATED_CUENTA_DEBITO_CBU);
        assertThat(testMovimientoApp.getCuentaDebitoAlias()).isEqualTo(UPDATED_CUENTA_DEBITO_ALIAS);
        assertThat(testMovimientoApp.isCuentaDebitoPropia()).isEqualTo(UPDATED_CUENTA_DEBITO_PROPIA);
        assertThat(testMovimientoApp.getCuentaCreditoCbu()).isEqualTo(UPDATED_CUENTA_CREDITO_CBU);
        assertThat(testMovimientoApp.getCuentaCreditoAlias()).isEqualTo(UPDATED_CUENTA_CREDITO_ALIAS);
        assertThat(testMovimientoApp.isCuentaCreditoPropia()).isEqualTo(UPDATED_CUENTA_CREDITO_PROPIA);
        assertThat(testMovimientoApp.getMoneda()).isEqualTo(UPDATED_MONEDA);
        assertThat(testMovimientoApp.getMonto()).isEqualTo(UPDATED_MONTO);
        assertThat(testMovimientoApp.getTimestamp()).isEqualTo(UPDATED_TIMESTAMP);
        assertThat(testMovimientoApp.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testMovimientoApp.getIdentificacion()).isEqualTo(UPDATED_IDENTIFICACION);
        assertThat(testMovimientoApp.isConsolidado()).isEqualTo(UPDATED_CONSOLIDADO);
        assertThat(testMovimientoApp.getConsolidadoTimestamp()).isEqualTo(UPDATED_CONSOLIDADO_TIMESTAMP);

        // Validate the MovimientoApp in Elasticsearch
        MovimientoApp movimientoAppEs = movimientoAppSearchRepository.findOne(testMovimientoApp.getId());
        assertThat(movimientoAppEs).isEqualToIgnoringGivenFields(testMovimientoApp);
    }

    @Test
    @Transactional
    public void updateNonExistingMovimientoApp() throws Exception {
        int databaseSizeBeforeUpdate = movimientoAppRepository.findAll().size();

        // Create the MovimientoApp

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMovimientoAppMockMvc.perform(put("/api/movimiento-apps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(movimientoApp)))
            .andExpect(status().isCreated());

        // Validate the MovimientoApp in the database
        List<MovimientoApp> movimientoAppList = movimientoAppRepository.findAll();
        assertThat(movimientoAppList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMovimientoApp() throws Exception {
        // Initialize the database
        movimientoAppService.save(movimientoApp);

        int databaseSizeBeforeDelete = movimientoAppRepository.findAll().size();

        // Get the movimientoApp
        restMovimientoAppMockMvc.perform(delete("/api/movimiento-apps/{id}", movimientoApp.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean movimientoAppExistsInEs = movimientoAppSearchRepository.exists(movimientoApp.getId());
        assertThat(movimientoAppExistsInEs).isFalse();

        // Validate the database is empty
        List<MovimientoApp> movimientoAppList = movimientoAppRepository.findAll();
        assertThat(movimientoAppList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchMovimientoApp() throws Exception {
        // Initialize the database
        movimientoAppService.save(movimientoApp);

        // Search the movimientoApp
        restMovimientoAppMockMvc.perform(get("/api/_search/movimiento-apps?query=id:" + movimientoApp.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(movimientoApp.getId().intValue())))
            .andExpect(jsonPath("$.[*].cuentaDebitoCbu").value(hasItem(DEFAULT_CUENTA_DEBITO_CBU.toString())))
            .andExpect(jsonPath("$.[*].cuentaDebitoAlias").value(hasItem(DEFAULT_CUENTA_DEBITO_ALIAS.toString())))
            .andExpect(jsonPath("$.[*].cuentaDebitoPropia").value(hasItem(DEFAULT_CUENTA_DEBITO_PROPIA.booleanValue())))
            .andExpect(jsonPath("$.[*].cuentaCreditoCbu").value(hasItem(DEFAULT_CUENTA_CREDITO_CBU.toString())))
            .andExpect(jsonPath("$.[*].cuentaCreditoAlias").value(hasItem(DEFAULT_CUENTA_CREDITO_ALIAS.toString())))
            .andExpect(jsonPath("$.[*].cuentaCreditoPropia").value(hasItem(DEFAULT_CUENTA_CREDITO_PROPIA.booleanValue())))
            .andExpect(jsonPath("$.[*].moneda").value(hasItem(DEFAULT_MONEDA.toString())))
            .andExpect(jsonPath("$.[*].monto").value(hasItem(DEFAULT_MONTO.intValue())))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString())))
            .andExpect(jsonPath("$.[*].identificacion").value(hasItem(DEFAULT_IDENTIFICACION.toString())))
            .andExpect(jsonPath("$.[*].consolidado").value(hasItem(DEFAULT_CONSOLIDADO.booleanValue())))
            .andExpect(jsonPath("$.[*].consolidadoTimestamp").value(hasItem(DEFAULT_CONSOLIDADO_TIMESTAMP.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MovimientoApp.class);
        MovimientoApp movimientoApp1 = new MovimientoApp();
        movimientoApp1.setId(1L);
        MovimientoApp movimientoApp2 = new MovimientoApp();
        movimientoApp2.setId(movimientoApp1.getId());
        assertThat(movimientoApp1).isEqualTo(movimientoApp2);
        movimientoApp2.setId(2L);
        assertThat(movimientoApp1).isNotEqualTo(movimientoApp2);
        movimientoApp1.setId(null);
        assertThat(movimientoApp1).isNotEqualTo(movimientoApp2);
    }
}
