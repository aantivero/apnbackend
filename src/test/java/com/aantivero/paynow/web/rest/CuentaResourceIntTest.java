package com.aantivero.paynow.web.rest;

import com.aantivero.paynow.PaynowApp;

import com.aantivero.paynow.domain.Cuenta;
import com.aantivero.paynow.domain.User;
import com.aantivero.paynow.domain.Banco;
import com.aantivero.paynow.repository.CuentaRepository;
import com.aantivero.paynow.service.CuentaService;
import com.aantivero.paynow.repository.search.CuentaSearchRepository;
import com.aantivero.paynow.web.rest.errors.ExceptionTranslator;
import com.aantivero.paynow.service.dto.CuentaCriteria;
import com.aantivero.paynow.service.CuentaQueryService;

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
import com.aantivero.paynow.domain.enumeration.TipoCuenta;
/**
 * Test class for the CuentaResource REST controller.
 *
 * @see CuentaResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PaynowApp.class)
public class CuentaResourceIntTest {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final Moneda DEFAULT_MONEDA = Moneda.PESOS;
    private static final Moneda UPDATED_MONEDA = Moneda.DOLAR;

    private static final BigDecimal DEFAULT_SALDO = new BigDecimal(1);
    private static final BigDecimal UPDATED_SALDO = new BigDecimal(2);

    private static final TipoCuenta DEFAULT_TIPO = TipoCuenta.VIRTUAL;
    private static final TipoCuenta UPDATED_TIPO = TipoCuenta.BANCARIA;

    private static final String DEFAULT_CBU = "AAAAAAAAAA";
    private static final String UPDATED_CBU = "BBBBBBBBBB";

    private static final String DEFAULT_ALIAS_CBU = "AAAAAAAAAA";
    private static final String UPDATED_ALIAS_CBU = "BBBBBBBBBB";

    private static final Instant DEFAULT_FECHA_CREACION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_CREACION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_FECHA_MODIFICACION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_MODIFICACION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_HABILITADA = false;
    private static final Boolean UPDATED_HABILITADA = true;

    private static final Boolean DEFAULT_PARA_CREDITO = false;
    private static final Boolean UPDATED_PARA_CREDITO = true;

    private static final Boolean DEFAULT_PARA_DEBITO = false;
    private static final Boolean UPDATED_PARA_DEBITO = true;

    private static final BigDecimal DEFAULT_MAXIMO_CREDITO = new BigDecimal(1);
    private static final BigDecimal UPDATED_MAXIMO_CREDITO = new BigDecimal(2);

    private static final BigDecimal DEFAULT_MAXIMO_DEBITO = new BigDecimal(1);
    private static final BigDecimal UPDATED_MAXIMO_DEBITO = new BigDecimal(2);

    private static final String DEFAULT_CODIGO_SEGURIDAD = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO_SEGURIDAD = "BBBBBBBBBB";

    @Autowired
    private CuentaRepository cuentaRepository;

    @Autowired
    private CuentaService cuentaService;

    @Autowired
    private CuentaSearchRepository cuentaSearchRepository;

    @Autowired
    private CuentaQueryService cuentaQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCuentaMockMvc;

    private Cuenta cuenta;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CuentaResource cuentaResource = new CuentaResource(cuentaService, cuentaQueryService);
        this.restCuentaMockMvc = MockMvcBuilders.standaloneSetup(cuentaResource)
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
    public static Cuenta createEntity(EntityManager em) {
        Cuenta cuenta = new Cuenta()
            .nombre(DEFAULT_NOMBRE)
            .descripcion(DEFAULT_DESCRIPCION)
            .moneda(DEFAULT_MONEDA)
            .saldo(DEFAULT_SALDO)
            .tipo(DEFAULT_TIPO)
            .cbu(DEFAULT_CBU)
            .aliasCbu(DEFAULT_ALIAS_CBU)
            .fechaCreacion(DEFAULT_FECHA_CREACION)
            .fechaModificacion(DEFAULT_FECHA_MODIFICACION)
            .habilitada(DEFAULT_HABILITADA)
            .paraCredito(DEFAULT_PARA_CREDITO)
            .paraDebito(DEFAULT_PARA_DEBITO)
            .maximoCredito(DEFAULT_MAXIMO_CREDITO)
            .maximoDebito(DEFAULT_MAXIMO_DEBITO)
            .codigoSeguridad(DEFAULT_CODIGO_SEGURIDAD);
        // Add required entity
        User usuario = UserResourceIntTest.createEntity(em);
        em.persist(usuario);
        em.flush();
        cuenta.setUsuario(usuario);
        return cuenta;
    }

    @Before
    public void initTest() {
        cuentaSearchRepository.deleteAll();
        cuenta = createEntity(em);
    }

    @Test
    @Transactional
    public void createCuenta() throws Exception {
        int databaseSizeBeforeCreate = cuentaRepository.findAll().size();

        // Create the Cuenta
        restCuentaMockMvc.perform(post("/api/cuentas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cuenta)))
            .andExpect(status().isCreated());

        // Validate the Cuenta in the database
        List<Cuenta> cuentaList = cuentaRepository.findAll();
        assertThat(cuentaList).hasSize(databaseSizeBeforeCreate + 1);
        Cuenta testCuenta = cuentaList.get(cuentaList.size() - 1);
        assertThat(testCuenta.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testCuenta.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testCuenta.getMoneda()).isEqualTo(DEFAULT_MONEDA);
        assertThat(testCuenta.getSaldo()).isEqualTo(DEFAULT_SALDO);
        assertThat(testCuenta.getTipo()).isEqualTo(DEFAULT_TIPO);
        assertThat(testCuenta.getCbu()).isEqualTo(DEFAULT_CBU);
        assertThat(testCuenta.getAliasCbu()).isEqualTo(DEFAULT_ALIAS_CBU);
        assertThat(testCuenta.getFechaCreacion()).isEqualTo(DEFAULT_FECHA_CREACION);
        assertThat(testCuenta.getFechaModificacion()).isEqualTo(DEFAULT_FECHA_MODIFICACION);
        assertThat(testCuenta.isHabilitada()).isEqualTo(DEFAULT_HABILITADA);
        assertThat(testCuenta.isParaCredito()).isEqualTo(DEFAULT_PARA_CREDITO);
        assertThat(testCuenta.isParaDebito()).isEqualTo(DEFAULT_PARA_DEBITO);
        assertThat(testCuenta.getMaximoCredito()).isEqualTo(DEFAULT_MAXIMO_CREDITO);
        assertThat(testCuenta.getMaximoDebito()).isEqualTo(DEFAULT_MAXIMO_DEBITO);
        assertThat(testCuenta.getCodigoSeguridad()).isEqualTo(DEFAULT_CODIGO_SEGURIDAD);

        // Validate the Cuenta in Elasticsearch
        Cuenta cuentaEs = cuentaSearchRepository.findOne(testCuenta.getId());
        assertThat(cuentaEs).isEqualToIgnoringGivenFields(testCuenta);
    }

    @Test
    @Transactional
    public void createCuentaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cuentaRepository.findAll().size();

        // Create the Cuenta with an existing ID
        cuenta.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCuentaMockMvc.perform(post("/api/cuentas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cuenta)))
            .andExpect(status().isBadRequest());

        // Validate the Cuenta in the database
        List<Cuenta> cuentaList = cuentaRepository.findAll();
        assertThat(cuentaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = cuentaRepository.findAll().size();
        // set the field null
        cuenta.setNombre(null);

        // Create the Cuenta, which fails.

        restCuentaMockMvc.perform(post("/api/cuentas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cuenta)))
            .andExpect(status().isBadRequest());

        List<Cuenta> cuentaList = cuentaRepository.findAll();
        assertThat(cuentaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMonedaIsRequired() throws Exception {
        int databaseSizeBeforeTest = cuentaRepository.findAll().size();
        // set the field null
        cuenta.setMoneda(null);

        // Create the Cuenta, which fails.

        restCuentaMockMvc.perform(post("/api/cuentas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cuenta)))
            .andExpect(status().isBadRequest());

        List<Cuenta> cuentaList = cuentaRepository.findAll();
        assertThat(cuentaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSaldoIsRequired() throws Exception {
        int databaseSizeBeforeTest = cuentaRepository.findAll().size();
        // set the field null
        cuenta.setSaldo(null);

        // Create the Cuenta, which fails.

        restCuentaMockMvc.perform(post("/api/cuentas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cuenta)))
            .andExpect(status().isBadRequest());

        List<Cuenta> cuentaList = cuentaRepository.findAll();
        assertThat(cuentaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTipoIsRequired() throws Exception {
        int databaseSizeBeforeTest = cuentaRepository.findAll().size();
        // set the field null
        cuenta.setTipo(null);

        // Create the Cuenta, which fails.

        restCuentaMockMvc.perform(post("/api/cuentas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cuenta)))
            .andExpect(status().isBadRequest());

        List<Cuenta> cuentaList = cuentaRepository.findAll();
        assertThat(cuentaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkHabilitadaIsRequired() throws Exception {
        int databaseSizeBeforeTest = cuentaRepository.findAll().size();
        // set the field null
        cuenta.setHabilitada(null);

        // Create the Cuenta, which fails.

        restCuentaMockMvc.perform(post("/api/cuentas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cuenta)))
            .andExpect(status().isBadRequest());

        List<Cuenta> cuentaList = cuentaRepository.findAll();
        assertThat(cuentaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkParaCreditoIsRequired() throws Exception {
        int databaseSizeBeforeTest = cuentaRepository.findAll().size();
        // set the field null
        cuenta.setParaCredito(null);

        // Create the Cuenta, which fails.

        restCuentaMockMvc.perform(post("/api/cuentas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cuenta)))
            .andExpect(status().isBadRequest());

        List<Cuenta> cuentaList = cuentaRepository.findAll();
        assertThat(cuentaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkParaDebitoIsRequired() throws Exception {
        int databaseSizeBeforeTest = cuentaRepository.findAll().size();
        // set the field null
        cuenta.setParaDebito(null);

        // Create the Cuenta, which fails.

        restCuentaMockMvc.perform(post("/api/cuentas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cuenta)))
            .andExpect(status().isBadRequest());

        List<Cuenta> cuentaList = cuentaRepository.findAll();
        assertThat(cuentaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCuentas() throws Exception {
        // Initialize the database
        cuentaRepository.saveAndFlush(cuenta);

        // Get all the cuentaList
        restCuentaMockMvc.perform(get("/api/cuentas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cuenta.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString())))
            .andExpect(jsonPath("$.[*].moneda").value(hasItem(DEFAULT_MONEDA.toString())))
            .andExpect(jsonPath("$.[*].saldo").value(hasItem(DEFAULT_SALDO.intValue())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].cbu").value(hasItem(DEFAULT_CBU.toString())))
            .andExpect(jsonPath("$.[*].aliasCbu").value(hasItem(DEFAULT_ALIAS_CBU.toString())))
            .andExpect(jsonPath("$.[*].fechaCreacion").value(hasItem(DEFAULT_FECHA_CREACION.toString())))
            .andExpect(jsonPath("$.[*].fechaModificacion").value(hasItem(DEFAULT_FECHA_MODIFICACION.toString())))
            .andExpect(jsonPath("$.[*].habilitada").value(hasItem(DEFAULT_HABILITADA.booleanValue())))
            .andExpect(jsonPath("$.[*].paraCredito").value(hasItem(DEFAULT_PARA_CREDITO.booleanValue())))
            .andExpect(jsonPath("$.[*].paraDebito").value(hasItem(DEFAULT_PARA_DEBITO.booleanValue())))
            .andExpect(jsonPath("$.[*].maximoCredito").value(hasItem(DEFAULT_MAXIMO_CREDITO.intValue())))
            .andExpect(jsonPath("$.[*].maximoDebito").value(hasItem(DEFAULT_MAXIMO_DEBITO.intValue())))
            .andExpect(jsonPath("$.[*].codigoSeguridad").value(hasItem(DEFAULT_CODIGO_SEGURIDAD.toString())));
    }

    @Test
    @Transactional
    public void getCuenta() throws Exception {
        // Initialize the database
        cuentaRepository.saveAndFlush(cuenta);

        // Get the cuenta
        restCuentaMockMvc.perform(get("/api/cuentas/{id}", cuenta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cuenta.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION.toString()))
            .andExpect(jsonPath("$.moneda").value(DEFAULT_MONEDA.toString()))
            .andExpect(jsonPath("$.saldo").value(DEFAULT_SALDO.intValue()))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO.toString()))
            .andExpect(jsonPath("$.cbu").value(DEFAULT_CBU.toString()))
            .andExpect(jsonPath("$.aliasCbu").value(DEFAULT_ALIAS_CBU.toString()))
            .andExpect(jsonPath("$.fechaCreacion").value(DEFAULT_FECHA_CREACION.toString()))
            .andExpect(jsonPath("$.fechaModificacion").value(DEFAULT_FECHA_MODIFICACION.toString()))
            .andExpect(jsonPath("$.habilitada").value(DEFAULT_HABILITADA.booleanValue()))
            .andExpect(jsonPath("$.paraCredito").value(DEFAULT_PARA_CREDITO.booleanValue()))
            .andExpect(jsonPath("$.paraDebito").value(DEFAULT_PARA_DEBITO.booleanValue()))
            .andExpect(jsonPath("$.maximoCredito").value(DEFAULT_MAXIMO_CREDITO.intValue()))
            .andExpect(jsonPath("$.maximoDebito").value(DEFAULT_MAXIMO_DEBITO.intValue()))
            .andExpect(jsonPath("$.codigoSeguridad").value(DEFAULT_CODIGO_SEGURIDAD.toString()));
    }

    @Test
    @Transactional
    public void getAllCuentasByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        cuentaRepository.saveAndFlush(cuenta);

        // Get all the cuentaList where nombre equals to DEFAULT_NOMBRE
        defaultCuentaShouldBeFound("nombre.equals=" + DEFAULT_NOMBRE);

        // Get all the cuentaList where nombre equals to UPDATED_NOMBRE
        defaultCuentaShouldNotBeFound("nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllCuentasByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        cuentaRepository.saveAndFlush(cuenta);

        // Get all the cuentaList where nombre in DEFAULT_NOMBRE or UPDATED_NOMBRE
        defaultCuentaShouldBeFound("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE);

        // Get all the cuentaList where nombre equals to UPDATED_NOMBRE
        defaultCuentaShouldNotBeFound("nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllCuentasByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        cuentaRepository.saveAndFlush(cuenta);

        // Get all the cuentaList where nombre is not null
        defaultCuentaShouldBeFound("nombre.specified=true");

        // Get all the cuentaList where nombre is null
        defaultCuentaShouldNotBeFound("nombre.specified=false");
    }

    @Test
    @Transactional
    public void getAllCuentasByDescripcionIsEqualToSomething() throws Exception {
        // Initialize the database
        cuentaRepository.saveAndFlush(cuenta);

        // Get all the cuentaList where descripcion equals to DEFAULT_DESCRIPCION
        defaultCuentaShouldBeFound("descripcion.equals=" + DEFAULT_DESCRIPCION);

        // Get all the cuentaList where descripcion equals to UPDATED_DESCRIPCION
        defaultCuentaShouldNotBeFound("descripcion.equals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    public void getAllCuentasByDescripcionIsInShouldWork() throws Exception {
        // Initialize the database
        cuentaRepository.saveAndFlush(cuenta);

        // Get all the cuentaList where descripcion in DEFAULT_DESCRIPCION or UPDATED_DESCRIPCION
        defaultCuentaShouldBeFound("descripcion.in=" + DEFAULT_DESCRIPCION + "," + UPDATED_DESCRIPCION);

        // Get all the cuentaList where descripcion equals to UPDATED_DESCRIPCION
        defaultCuentaShouldNotBeFound("descripcion.in=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    public void getAllCuentasByDescripcionIsNullOrNotNull() throws Exception {
        // Initialize the database
        cuentaRepository.saveAndFlush(cuenta);

        // Get all the cuentaList where descripcion is not null
        defaultCuentaShouldBeFound("descripcion.specified=true");

        // Get all the cuentaList where descripcion is null
        defaultCuentaShouldNotBeFound("descripcion.specified=false");
    }

    @Test
    @Transactional
    public void getAllCuentasByMonedaIsEqualToSomething() throws Exception {
        // Initialize the database
        cuentaRepository.saveAndFlush(cuenta);

        // Get all the cuentaList where moneda equals to DEFAULT_MONEDA
        defaultCuentaShouldBeFound("moneda.equals=" + DEFAULT_MONEDA);

        // Get all the cuentaList where moneda equals to UPDATED_MONEDA
        defaultCuentaShouldNotBeFound("moneda.equals=" + UPDATED_MONEDA);
    }

    @Test
    @Transactional
    public void getAllCuentasByMonedaIsInShouldWork() throws Exception {
        // Initialize the database
        cuentaRepository.saveAndFlush(cuenta);

        // Get all the cuentaList where moneda in DEFAULT_MONEDA or UPDATED_MONEDA
        defaultCuentaShouldBeFound("moneda.in=" + DEFAULT_MONEDA + "," + UPDATED_MONEDA);

        // Get all the cuentaList where moneda equals to UPDATED_MONEDA
        defaultCuentaShouldNotBeFound("moneda.in=" + UPDATED_MONEDA);
    }

    @Test
    @Transactional
    public void getAllCuentasByMonedaIsNullOrNotNull() throws Exception {
        // Initialize the database
        cuentaRepository.saveAndFlush(cuenta);

        // Get all the cuentaList where moneda is not null
        defaultCuentaShouldBeFound("moneda.specified=true");

        // Get all the cuentaList where moneda is null
        defaultCuentaShouldNotBeFound("moneda.specified=false");
    }

    @Test
    @Transactional
    public void getAllCuentasBySaldoIsEqualToSomething() throws Exception {
        // Initialize the database
        cuentaRepository.saveAndFlush(cuenta);

        // Get all the cuentaList where saldo equals to DEFAULT_SALDO
        defaultCuentaShouldBeFound("saldo.equals=" + DEFAULT_SALDO);

        // Get all the cuentaList where saldo equals to UPDATED_SALDO
        defaultCuentaShouldNotBeFound("saldo.equals=" + UPDATED_SALDO);
    }

    @Test
    @Transactional
    public void getAllCuentasBySaldoIsInShouldWork() throws Exception {
        // Initialize the database
        cuentaRepository.saveAndFlush(cuenta);

        // Get all the cuentaList where saldo in DEFAULT_SALDO or UPDATED_SALDO
        defaultCuentaShouldBeFound("saldo.in=" + DEFAULT_SALDO + "," + UPDATED_SALDO);

        // Get all the cuentaList where saldo equals to UPDATED_SALDO
        defaultCuentaShouldNotBeFound("saldo.in=" + UPDATED_SALDO);
    }

    @Test
    @Transactional
    public void getAllCuentasBySaldoIsNullOrNotNull() throws Exception {
        // Initialize the database
        cuentaRepository.saveAndFlush(cuenta);

        // Get all the cuentaList where saldo is not null
        defaultCuentaShouldBeFound("saldo.specified=true");

        // Get all the cuentaList where saldo is null
        defaultCuentaShouldNotBeFound("saldo.specified=false");
    }

    @Test
    @Transactional
    public void getAllCuentasByTipoIsEqualToSomething() throws Exception {
        // Initialize the database
        cuentaRepository.saveAndFlush(cuenta);

        // Get all the cuentaList where tipo equals to DEFAULT_TIPO
        defaultCuentaShouldBeFound("tipo.equals=" + DEFAULT_TIPO);

        // Get all the cuentaList where tipo equals to UPDATED_TIPO
        defaultCuentaShouldNotBeFound("tipo.equals=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    public void getAllCuentasByTipoIsInShouldWork() throws Exception {
        // Initialize the database
        cuentaRepository.saveAndFlush(cuenta);

        // Get all the cuentaList where tipo in DEFAULT_TIPO or UPDATED_TIPO
        defaultCuentaShouldBeFound("tipo.in=" + DEFAULT_TIPO + "," + UPDATED_TIPO);

        // Get all the cuentaList where tipo equals to UPDATED_TIPO
        defaultCuentaShouldNotBeFound("tipo.in=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    public void getAllCuentasByTipoIsNullOrNotNull() throws Exception {
        // Initialize the database
        cuentaRepository.saveAndFlush(cuenta);

        // Get all the cuentaList where tipo is not null
        defaultCuentaShouldBeFound("tipo.specified=true");

        // Get all the cuentaList where tipo is null
        defaultCuentaShouldNotBeFound("tipo.specified=false");
    }

    @Test
    @Transactional
    public void getAllCuentasByCbuIsEqualToSomething() throws Exception {
        // Initialize the database
        cuentaRepository.saveAndFlush(cuenta);

        // Get all the cuentaList where cbu equals to DEFAULT_CBU
        defaultCuentaShouldBeFound("cbu.equals=" + DEFAULT_CBU);

        // Get all the cuentaList where cbu equals to UPDATED_CBU
        defaultCuentaShouldNotBeFound("cbu.equals=" + UPDATED_CBU);
    }

    @Test
    @Transactional
    public void getAllCuentasByCbuIsInShouldWork() throws Exception {
        // Initialize the database
        cuentaRepository.saveAndFlush(cuenta);

        // Get all the cuentaList where cbu in DEFAULT_CBU or UPDATED_CBU
        defaultCuentaShouldBeFound("cbu.in=" + DEFAULT_CBU + "," + UPDATED_CBU);

        // Get all the cuentaList where cbu equals to UPDATED_CBU
        defaultCuentaShouldNotBeFound("cbu.in=" + UPDATED_CBU);
    }

    @Test
    @Transactional
    public void getAllCuentasByCbuIsNullOrNotNull() throws Exception {
        // Initialize the database
        cuentaRepository.saveAndFlush(cuenta);

        // Get all the cuentaList where cbu is not null
        defaultCuentaShouldBeFound("cbu.specified=true");

        // Get all the cuentaList where cbu is null
        defaultCuentaShouldNotBeFound("cbu.specified=false");
    }

    @Test
    @Transactional
    public void getAllCuentasByAliasCbuIsEqualToSomething() throws Exception {
        // Initialize the database
        cuentaRepository.saveAndFlush(cuenta);

        // Get all the cuentaList where aliasCbu equals to DEFAULT_ALIAS_CBU
        defaultCuentaShouldBeFound("aliasCbu.equals=" + DEFAULT_ALIAS_CBU);

        // Get all the cuentaList where aliasCbu equals to UPDATED_ALIAS_CBU
        defaultCuentaShouldNotBeFound("aliasCbu.equals=" + UPDATED_ALIAS_CBU);
    }

    @Test
    @Transactional
    public void getAllCuentasByAliasCbuIsInShouldWork() throws Exception {
        // Initialize the database
        cuentaRepository.saveAndFlush(cuenta);

        // Get all the cuentaList where aliasCbu in DEFAULT_ALIAS_CBU or UPDATED_ALIAS_CBU
        defaultCuentaShouldBeFound("aliasCbu.in=" + DEFAULT_ALIAS_CBU + "," + UPDATED_ALIAS_CBU);

        // Get all the cuentaList where aliasCbu equals to UPDATED_ALIAS_CBU
        defaultCuentaShouldNotBeFound("aliasCbu.in=" + UPDATED_ALIAS_CBU);
    }

    @Test
    @Transactional
    public void getAllCuentasByAliasCbuIsNullOrNotNull() throws Exception {
        // Initialize the database
        cuentaRepository.saveAndFlush(cuenta);

        // Get all the cuentaList where aliasCbu is not null
        defaultCuentaShouldBeFound("aliasCbu.specified=true");

        // Get all the cuentaList where aliasCbu is null
        defaultCuentaShouldNotBeFound("aliasCbu.specified=false");
    }

    @Test
    @Transactional
    public void getAllCuentasByFechaCreacionIsEqualToSomething() throws Exception {
        // Initialize the database
        cuentaRepository.saveAndFlush(cuenta);

        // Get all the cuentaList where fechaCreacion equals to DEFAULT_FECHA_CREACION
        defaultCuentaShouldBeFound("fechaCreacion.equals=" + DEFAULT_FECHA_CREACION);

        // Get all the cuentaList where fechaCreacion equals to UPDATED_FECHA_CREACION
        defaultCuentaShouldNotBeFound("fechaCreacion.equals=" + UPDATED_FECHA_CREACION);
    }

    @Test
    @Transactional
    public void getAllCuentasByFechaCreacionIsInShouldWork() throws Exception {
        // Initialize the database
        cuentaRepository.saveAndFlush(cuenta);

        // Get all the cuentaList where fechaCreacion in DEFAULT_FECHA_CREACION or UPDATED_FECHA_CREACION
        defaultCuentaShouldBeFound("fechaCreacion.in=" + DEFAULT_FECHA_CREACION + "," + UPDATED_FECHA_CREACION);

        // Get all the cuentaList where fechaCreacion equals to UPDATED_FECHA_CREACION
        defaultCuentaShouldNotBeFound("fechaCreacion.in=" + UPDATED_FECHA_CREACION);
    }

    @Test
    @Transactional
    public void getAllCuentasByFechaCreacionIsNullOrNotNull() throws Exception {
        // Initialize the database
        cuentaRepository.saveAndFlush(cuenta);

        // Get all the cuentaList where fechaCreacion is not null
        defaultCuentaShouldBeFound("fechaCreacion.specified=true");

        // Get all the cuentaList where fechaCreacion is null
        defaultCuentaShouldNotBeFound("fechaCreacion.specified=false");
    }

    @Test
    @Transactional
    public void getAllCuentasByFechaModificacionIsEqualToSomething() throws Exception {
        // Initialize the database
        cuentaRepository.saveAndFlush(cuenta);

        // Get all the cuentaList where fechaModificacion equals to DEFAULT_FECHA_MODIFICACION
        defaultCuentaShouldBeFound("fechaModificacion.equals=" + DEFAULT_FECHA_MODIFICACION);

        // Get all the cuentaList where fechaModificacion equals to UPDATED_FECHA_MODIFICACION
        defaultCuentaShouldNotBeFound("fechaModificacion.equals=" + UPDATED_FECHA_MODIFICACION);
    }

    @Test
    @Transactional
    public void getAllCuentasByFechaModificacionIsInShouldWork() throws Exception {
        // Initialize the database
        cuentaRepository.saveAndFlush(cuenta);

        // Get all the cuentaList where fechaModificacion in DEFAULT_FECHA_MODIFICACION or UPDATED_FECHA_MODIFICACION
        defaultCuentaShouldBeFound("fechaModificacion.in=" + DEFAULT_FECHA_MODIFICACION + "," + UPDATED_FECHA_MODIFICACION);

        // Get all the cuentaList where fechaModificacion equals to UPDATED_FECHA_MODIFICACION
        defaultCuentaShouldNotBeFound("fechaModificacion.in=" + UPDATED_FECHA_MODIFICACION);
    }

    @Test
    @Transactional
    public void getAllCuentasByFechaModificacionIsNullOrNotNull() throws Exception {
        // Initialize the database
        cuentaRepository.saveAndFlush(cuenta);

        // Get all the cuentaList where fechaModificacion is not null
        defaultCuentaShouldBeFound("fechaModificacion.specified=true");

        // Get all the cuentaList where fechaModificacion is null
        defaultCuentaShouldNotBeFound("fechaModificacion.specified=false");
    }

    @Test
    @Transactional
    public void getAllCuentasByHabilitadaIsEqualToSomething() throws Exception {
        // Initialize the database
        cuentaRepository.saveAndFlush(cuenta);

        // Get all the cuentaList where habilitada equals to DEFAULT_HABILITADA
        defaultCuentaShouldBeFound("habilitada.equals=" + DEFAULT_HABILITADA);

        // Get all the cuentaList where habilitada equals to UPDATED_HABILITADA
        defaultCuentaShouldNotBeFound("habilitada.equals=" + UPDATED_HABILITADA);
    }

    @Test
    @Transactional
    public void getAllCuentasByHabilitadaIsInShouldWork() throws Exception {
        // Initialize the database
        cuentaRepository.saveAndFlush(cuenta);

        // Get all the cuentaList where habilitada in DEFAULT_HABILITADA or UPDATED_HABILITADA
        defaultCuentaShouldBeFound("habilitada.in=" + DEFAULT_HABILITADA + "," + UPDATED_HABILITADA);

        // Get all the cuentaList where habilitada equals to UPDATED_HABILITADA
        defaultCuentaShouldNotBeFound("habilitada.in=" + UPDATED_HABILITADA);
    }

    @Test
    @Transactional
    public void getAllCuentasByHabilitadaIsNullOrNotNull() throws Exception {
        // Initialize the database
        cuentaRepository.saveAndFlush(cuenta);

        // Get all the cuentaList where habilitada is not null
        defaultCuentaShouldBeFound("habilitada.specified=true");

        // Get all the cuentaList where habilitada is null
        defaultCuentaShouldNotBeFound("habilitada.specified=false");
    }

    @Test
    @Transactional
    public void getAllCuentasByParaCreditoIsEqualToSomething() throws Exception {
        // Initialize the database
        cuentaRepository.saveAndFlush(cuenta);

        // Get all the cuentaList where paraCredito equals to DEFAULT_PARA_CREDITO
        defaultCuentaShouldBeFound("paraCredito.equals=" + DEFAULT_PARA_CREDITO);

        // Get all the cuentaList where paraCredito equals to UPDATED_PARA_CREDITO
        defaultCuentaShouldNotBeFound("paraCredito.equals=" + UPDATED_PARA_CREDITO);
    }

    @Test
    @Transactional
    public void getAllCuentasByParaCreditoIsInShouldWork() throws Exception {
        // Initialize the database
        cuentaRepository.saveAndFlush(cuenta);

        // Get all the cuentaList where paraCredito in DEFAULT_PARA_CREDITO or UPDATED_PARA_CREDITO
        defaultCuentaShouldBeFound("paraCredito.in=" + DEFAULT_PARA_CREDITO + "," + UPDATED_PARA_CREDITO);

        // Get all the cuentaList where paraCredito equals to UPDATED_PARA_CREDITO
        defaultCuentaShouldNotBeFound("paraCredito.in=" + UPDATED_PARA_CREDITO);
    }

    @Test
    @Transactional
    public void getAllCuentasByParaCreditoIsNullOrNotNull() throws Exception {
        // Initialize the database
        cuentaRepository.saveAndFlush(cuenta);

        // Get all the cuentaList where paraCredito is not null
        defaultCuentaShouldBeFound("paraCredito.specified=true");

        // Get all the cuentaList where paraCredito is null
        defaultCuentaShouldNotBeFound("paraCredito.specified=false");
    }

    @Test
    @Transactional
    public void getAllCuentasByParaDebitoIsEqualToSomething() throws Exception {
        // Initialize the database
        cuentaRepository.saveAndFlush(cuenta);

        // Get all the cuentaList where paraDebito equals to DEFAULT_PARA_DEBITO
        defaultCuentaShouldBeFound("paraDebito.equals=" + DEFAULT_PARA_DEBITO);

        // Get all the cuentaList where paraDebito equals to UPDATED_PARA_DEBITO
        defaultCuentaShouldNotBeFound("paraDebito.equals=" + UPDATED_PARA_DEBITO);
    }

    @Test
    @Transactional
    public void getAllCuentasByParaDebitoIsInShouldWork() throws Exception {
        // Initialize the database
        cuentaRepository.saveAndFlush(cuenta);

        // Get all the cuentaList where paraDebito in DEFAULT_PARA_DEBITO or UPDATED_PARA_DEBITO
        defaultCuentaShouldBeFound("paraDebito.in=" + DEFAULT_PARA_DEBITO + "," + UPDATED_PARA_DEBITO);

        // Get all the cuentaList where paraDebito equals to UPDATED_PARA_DEBITO
        defaultCuentaShouldNotBeFound("paraDebito.in=" + UPDATED_PARA_DEBITO);
    }

    @Test
    @Transactional
    public void getAllCuentasByParaDebitoIsNullOrNotNull() throws Exception {
        // Initialize the database
        cuentaRepository.saveAndFlush(cuenta);

        // Get all the cuentaList where paraDebito is not null
        defaultCuentaShouldBeFound("paraDebito.specified=true");

        // Get all the cuentaList where paraDebito is null
        defaultCuentaShouldNotBeFound("paraDebito.specified=false");
    }

    @Test
    @Transactional
    public void getAllCuentasByMaximoCreditoIsEqualToSomething() throws Exception {
        // Initialize the database
        cuentaRepository.saveAndFlush(cuenta);

        // Get all the cuentaList where maximoCredito equals to DEFAULT_MAXIMO_CREDITO
        defaultCuentaShouldBeFound("maximoCredito.equals=" + DEFAULT_MAXIMO_CREDITO);

        // Get all the cuentaList where maximoCredito equals to UPDATED_MAXIMO_CREDITO
        defaultCuentaShouldNotBeFound("maximoCredito.equals=" + UPDATED_MAXIMO_CREDITO);
    }

    @Test
    @Transactional
    public void getAllCuentasByMaximoCreditoIsInShouldWork() throws Exception {
        // Initialize the database
        cuentaRepository.saveAndFlush(cuenta);

        // Get all the cuentaList where maximoCredito in DEFAULT_MAXIMO_CREDITO or UPDATED_MAXIMO_CREDITO
        defaultCuentaShouldBeFound("maximoCredito.in=" + DEFAULT_MAXIMO_CREDITO + "," + UPDATED_MAXIMO_CREDITO);

        // Get all the cuentaList where maximoCredito equals to UPDATED_MAXIMO_CREDITO
        defaultCuentaShouldNotBeFound("maximoCredito.in=" + UPDATED_MAXIMO_CREDITO);
    }

    @Test
    @Transactional
    public void getAllCuentasByMaximoCreditoIsNullOrNotNull() throws Exception {
        // Initialize the database
        cuentaRepository.saveAndFlush(cuenta);

        // Get all the cuentaList where maximoCredito is not null
        defaultCuentaShouldBeFound("maximoCredito.specified=true");

        // Get all the cuentaList where maximoCredito is null
        defaultCuentaShouldNotBeFound("maximoCredito.specified=false");
    }

    @Test
    @Transactional
    public void getAllCuentasByMaximoDebitoIsEqualToSomething() throws Exception {
        // Initialize the database
        cuentaRepository.saveAndFlush(cuenta);

        // Get all the cuentaList where maximoDebito equals to DEFAULT_MAXIMO_DEBITO
        defaultCuentaShouldBeFound("maximoDebito.equals=" + DEFAULT_MAXIMO_DEBITO);

        // Get all the cuentaList where maximoDebito equals to UPDATED_MAXIMO_DEBITO
        defaultCuentaShouldNotBeFound("maximoDebito.equals=" + UPDATED_MAXIMO_DEBITO);
    }

    @Test
    @Transactional
    public void getAllCuentasByMaximoDebitoIsInShouldWork() throws Exception {
        // Initialize the database
        cuentaRepository.saveAndFlush(cuenta);

        // Get all the cuentaList where maximoDebito in DEFAULT_MAXIMO_DEBITO or UPDATED_MAXIMO_DEBITO
        defaultCuentaShouldBeFound("maximoDebito.in=" + DEFAULT_MAXIMO_DEBITO + "," + UPDATED_MAXIMO_DEBITO);

        // Get all the cuentaList where maximoDebito equals to UPDATED_MAXIMO_DEBITO
        defaultCuentaShouldNotBeFound("maximoDebito.in=" + UPDATED_MAXIMO_DEBITO);
    }

    @Test
    @Transactional
    public void getAllCuentasByMaximoDebitoIsNullOrNotNull() throws Exception {
        // Initialize the database
        cuentaRepository.saveAndFlush(cuenta);

        // Get all the cuentaList where maximoDebito is not null
        defaultCuentaShouldBeFound("maximoDebito.specified=true");

        // Get all the cuentaList where maximoDebito is null
        defaultCuentaShouldNotBeFound("maximoDebito.specified=false");
    }

    @Test
    @Transactional
    public void getAllCuentasByCodigoSeguridadIsEqualToSomething() throws Exception {
        // Initialize the database
        cuentaRepository.saveAndFlush(cuenta);

        // Get all the cuentaList where codigoSeguridad equals to DEFAULT_CODIGO_SEGURIDAD
        defaultCuentaShouldBeFound("codigoSeguridad.equals=" + DEFAULT_CODIGO_SEGURIDAD);

        // Get all the cuentaList where codigoSeguridad equals to UPDATED_CODIGO_SEGURIDAD
        defaultCuentaShouldNotBeFound("codigoSeguridad.equals=" + UPDATED_CODIGO_SEGURIDAD);
    }

    @Test
    @Transactional
    public void getAllCuentasByCodigoSeguridadIsInShouldWork() throws Exception {
        // Initialize the database
        cuentaRepository.saveAndFlush(cuenta);

        // Get all the cuentaList where codigoSeguridad in DEFAULT_CODIGO_SEGURIDAD or UPDATED_CODIGO_SEGURIDAD
        defaultCuentaShouldBeFound("codigoSeguridad.in=" + DEFAULT_CODIGO_SEGURIDAD + "," + UPDATED_CODIGO_SEGURIDAD);

        // Get all the cuentaList where codigoSeguridad equals to UPDATED_CODIGO_SEGURIDAD
        defaultCuentaShouldNotBeFound("codigoSeguridad.in=" + UPDATED_CODIGO_SEGURIDAD);
    }

    @Test
    @Transactional
    public void getAllCuentasByCodigoSeguridadIsNullOrNotNull() throws Exception {
        // Initialize the database
        cuentaRepository.saveAndFlush(cuenta);

        // Get all the cuentaList where codigoSeguridad is not null
        defaultCuentaShouldBeFound("codigoSeguridad.specified=true");

        // Get all the cuentaList where codigoSeguridad is null
        defaultCuentaShouldNotBeFound("codigoSeguridad.specified=false");
    }

    @Test
    @Transactional
    public void getAllCuentasByUsuarioIsEqualToSomething() throws Exception {
        // Initialize the database
        User usuario = UserResourceIntTest.createEntity(em);
        em.persist(usuario);
        em.flush();
        cuenta.setUsuario(usuario);
        cuentaRepository.saveAndFlush(cuenta);
        Long usuarioId = usuario.getId();

        // Get all the cuentaList where usuario equals to usuarioId
        defaultCuentaShouldBeFound("usuarioId.equals=" + usuarioId);

        // Get all the cuentaList where usuario equals to usuarioId + 1
        defaultCuentaShouldNotBeFound("usuarioId.equals=" + (usuarioId + 1));
    }


    @Test
    @Transactional
    public void getAllCuentasByBancoIsEqualToSomething() throws Exception {
        // Initialize the database
        Banco banco = BancoResourceIntTest.createEntity(em);
        em.persist(banco);
        em.flush();
        cuenta.setBanco(banco);
        cuentaRepository.saveAndFlush(cuenta);
        Long bancoId = banco.getId();

        // Get all the cuentaList where banco equals to bancoId
        defaultCuentaShouldBeFound("bancoId.equals=" + bancoId);

        // Get all the cuentaList where banco equals to bancoId + 1
        defaultCuentaShouldNotBeFound("bancoId.equals=" + (bancoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultCuentaShouldBeFound(String filter) throws Exception {
        restCuentaMockMvc.perform(get("/api/cuentas?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cuenta.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString())))
            .andExpect(jsonPath("$.[*].moneda").value(hasItem(DEFAULT_MONEDA.toString())))
            .andExpect(jsonPath("$.[*].saldo").value(hasItem(DEFAULT_SALDO.intValue())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].cbu").value(hasItem(DEFAULT_CBU.toString())))
            .andExpect(jsonPath("$.[*].aliasCbu").value(hasItem(DEFAULT_ALIAS_CBU.toString())))
            .andExpect(jsonPath("$.[*].fechaCreacion").value(hasItem(DEFAULT_FECHA_CREACION.toString())))
            .andExpect(jsonPath("$.[*].fechaModificacion").value(hasItem(DEFAULT_FECHA_MODIFICACION.toString())))
            .andExpect(jsonPath("$.[*].habilitada").value(hasItem(DEFAULT_HABILITADA.booleanValue())))
            .andExpect(jsonPath("$.[*].paraCredito").value(hasItem(DEFAULT_PARA_CREDITO.booleanValue())))
            .andExpect(jsonPath("$.[*].paraDebito").value(hasItem(DEFAULT_PARA_DEBITO.booleanValue())))
            .andExpect(jsonPath("$.[*].maximoCredito").value(hasItem(DEFAULT_MAXIMO_CREDITO.intValue())))
            .andExpect(jsonPath("$.[*].maximoDebito").value(hasItem(DEFAULT_MAXIMO_DEBITO.intValue())))
            .andExpect(jsonPath("$.[*].codigoSeguridad").value(hasItem(DEFAULT_CODIGO_SEGURIDAD.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultCuentaShouldNotBeFound(String filter) throws Exception {
        restCuentaMockMvc.perform(get("/api/cuentas?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingCuenta() throws Exception {
        // Get the cuenta
        restCuentaMockMvc.perform(get("/api/cuentas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCuenta() throws Exception {
        // Initialize the database
        cuentaService.save(cuenta);

        int databaseSizeBeforeUpdate = cuentaRepository.findAll().size();

        // Update the cuenta
        Cuenta updatedCuenta = cuentaRepository.findOne(cuenta.getId());
        // Disconnect from session so that the updates on updatedCuenta are not directly saved in db
        em.detach(updatedCuenta);
        updatedCuenta
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .moneda(UPDATED_MONEDA)
            .saldo(UPDATED_SALDO)
            .tipo(UPDATED_TIPO)
            .cbu(UPDATED_CBU)
            .aliasCbu(UPDATED_ALIAS_CBU)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .fechaModificacion(UPDATED_FECHA_MODIFICACION)
            .habilitada(UPDATED_HABILITADA)
            .paraCredito(UPDATED_PARA_CREDITO)
            .paraDebito(UPDATED_PARA_DEBITO)
            .maximoCredito(UPDATED_MAXIMO_CREDITO)
            .maximoDebito(UPDATED_MAXIMO_DEBITO)
            .codigoSeguridad(UPDATED_CODIGO_SEGURIDAD);

        restCuentaMockMvc.perform(put("/api/cuentas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCuenta)))
            .andExpect(status().isOk());

        // Validate the Cuenta in the database
        List<Cuenta> cuentaList = cuentaRepository.findAll();
        assertThat(cuentaList).hasSize(databaseSizeBeforeUpdate);
        Cuenta testCuenta = cuentaList.get(cuentaList.size() - 1);
        assertThat(testCuenta.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testCuenta.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testCuenta.getMoneda()).isEqualTo(UPDATED_MONEDA);
        assertThat(testCuenta.getSaldo()).isEqualTo(UPDATED_SALDO);
        assertThat(testCuenta.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testCuenta.getCbu()).isEqualTo(UPDATED_CBU);
        assertThat(testCuenta.getAliasCbu()).isEqualTo(UPDATED_ALIAS_CBU);
        assertThat(testCuenta.getFechaCreacion()).isEqualTo(UPDATED_FECHA_CREACION);
        assertThat(testCuenta.getFechaModificacion()).isEqualTo(UPDATED_FECHA_MODIFICACION);
        assertThat(testCuenta.isHabilitada()).isEqualTo(UPDATED_HABILITADA);
        assertThat(testCuenta.isParaCredito()).isEqualTo(UPDATED_PARA_CREDITO);
        assertThat(testCuenta.isParaDebito()).isEqualTo(UPDATED_PARA_DEBITO);
        assertThat(testCuenta.getMaximoCredito()).isEqualTo(UPDATED_MAXIMO_CREDITO);
        assertThat(testCuenta.getMaximoDebito()).isEqualTo(UPDATED_MAXIMO_DEBITO);
        assertThat(testCuenta.getCodigoSeguridad()).isEqualTo(UPDATED_CODIGO_SEGURIDAD);

        // Validate the Cuenta in Elasticsearch
        Cuenta cuentaEs = cuentaSearchRepository.findOne(testCuenta.getId());
        assertThat(cuentaEs).isEqualToIgnoringGivenFields(testCuenta);
    }

    @Test
    @Transactional
    public void updateNonExistingCuenta() throws Exception {
        int databaseSizeBeforeUpdate = cuentaRepository.findAll().size();

        // Create the Cuenta

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCuentaMockMvc.perform(put("/api/cuentas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cuenta)))
            .andExpect(status().isCreated());

        // Validate the Cuenta in the database
        List<Cuenta> cuentaList = cuentaRepository.findAll();
        assertThat(cuentaList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCuenta() throws Exception {
        // Initialize the database
        cuentaService.save(cuenta);

        int databaseSizeBeforeDelete = cuentaRepository.findAll().size();

        // Get the cuenta
        restCuentaMockMvc.perform(delete("/api/cuentas/{id}", cuenta.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean cuentaExistsInEs = cuentaSearchRepository.exists(cuenta.getId());
        assertThat(cuentaExistsInEs).isFalse();

        // Validate the database is empty
        List<Cuenta> cuentaList = cuentaRepository.findAll();
        assertThat(cuentaList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCuenta() throws Exception {
        // Initialize the database
        cuentaService.save(cuenta);

        // Search the cuenta
        restCuentaMockMvc.perform(get("/api/_search/cuentas?query=id:" + cuenta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cuenta.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString())))
            .andExpect(jsonPath("$.[*].moneda").value(hasItem(DEFAULT_MONEDA.toString())))
            .andExpect(jsonPath("$.[*].saldo").value(hasItem(DEFAULT_SALDO.intValue())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].cbu").value(hasItem(DEFAULT_CBU.toString())))
            .andExpect(jsonPath("$.[*].aliasCbu").value(hasItem(DEFAULT_ALIAS_CBU.toString())))
            .andExpect(jsonPath("$.[*].fechaCreacion").value(hasItem(DEFAULT_FECHA_CREACION.toString())))
            .andExpect(jsonPath("$.[*].fechaModificacion").value(hasItem(DEFAULT_FECHA_MODIFICACION.toString())))
            .andExpect(jsonPath("$.[*].habilitada").value(hasItem(DEFAULT_HABILITADA.booleanValue())))
            .andExpect(jsonPath("$.[*].paraCredito").value(hasItem(DEFAULT_PARA_CREDITO.booleanValue())))
            .andExpect(jsonPath("$.[*].paraDebito").value(hasItem(DEFAULT_PARA_DEBITO.booleanValue())))
            .andExpect(jsonPath("$.[*].maximoCredito").value(hasItem(DEFAULT_MAXIMO_CREDITO.intValue())))
            .andExpect(jsonPath("$.[*].maximoDebito").value(hasItem(DEFAULT_MAXIMO_DEBITO.intValue())))
            .andExpect(jsonPath("$.[*].codigoSeguridad").value(hasItem(DEFAULT_CODIGO_SEGURIDAD.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cuenta.class);
        Cuenta cuenta1 = new Cuenta();
        cuenta1.setId(1L);
        Cuenta cuenta2 = new Cuenta();
        cuenta2.setId(cuenta1.getId());
        assertThat(cuenta1).isEqualTo(cuenta2);
        cuenta2.setId(2L);
        assertThat(cuenta1).isNotEqualTo(cuenta2);
        cuenta1.setId(null);
        assertThat(cuenta1).isNotEqualTo(cuenta2);
    }
}
