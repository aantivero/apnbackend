package com.aantivero.paynow.web.rest;

import com.aantivero.paynow.PaynowApp;

import com.aantivero.paynow.domain.Saldo;
import com.aantivero.paynow.domain.User;
import com.aantivero.paynow.domain.App;
import com.aantivero.paynow.repository.SaldoRepository;
import com.aantivero.paynow.service.SaldoService;
import com.aantivero.paynow.repository.search.SaldoSearchRepository;
import com.aantivero.paynow.web.rest.errors.ExceptionTranslator;
import com.aantivero.paynow.service.dto.SaldoCriteria;
import com.aantivero.paynow.service.SaldoQueryService;

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
 * Test class for the SaldoResource REST controller.
 *
 * @see SaldoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PaynowApp.class)
public class SaldoResourceIntTest {

    private static final BigDecimal DEFAULT_MONTO = new BigDecimal(1);
    private static final BigDecimal UPDATED_MONTO = new BigDecimal(2);

    private static final Moneda DEFAULT_MONEDA = Moneda.PESOS;
    private static final Moneda UPDATED_MONEDA = Moneda.DOLAR;

    private static final TipoCuenta DEFAULT_TIPO = TipoCuenta.VIRTUAL;
    private static final TipoCuenta UPDATED_TIPO = TipoCuenta.BANCARIA;

    private static final Instant DEFAULT_FECHA_CREACION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_CREACION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_FECHA_MODIFICACION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_MODIFICACION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private SaldoRepository saldoRepository;

    @Autowired
    private SaldoService saldoService;

    @Autowired
    private SaldoSearchRepository saldoSearchRepository;

    @Autowired
    private SaldoQueryService saldoQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSaldoMockMvc;

    private Saldo saldo;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SaldoResource saldoResource = new SaldoResource(saldoService, saldoQueryService);
        this.restSaldoMockMvc = MockMvcBuilders.standaloneSetup(saldoResource)
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
    public static Saldo createEntity(EntityManager em) {
        Saldo saldo = new Saldo()
            .monto(DEFAULT_MONTO)
            .moneda(DEFAULT_MONEDA)
            .tipo(DEFAULT_TIPO)
            .fechaCreacion(DEFAULT_FECHA_CREACION)
            .fechaModificacion(DEFAULT_FECHA_MODIFICACION);
        // Add required entity
        User usuario = UserResourceIntTest.createEntity(em);
        em.persist(usuario);
        em.flush();
        saldo.setUsuario(usuario);
        // Add required entity
        App aplicacion = AppResourceIntTest.createEntity(em);
        em.persist(aplicacion);
        em.flush();
        saldo.setAplicacion(aplicacion);
        return saldo;
    }

    @Before
    public void initTest() {
        saldoSearchRepository.deleteAll();
        saldo = createEntity(em);
    }

    @Test
    @Transactional
    public void createSaldo() throws Exception {
        int databaseSizeBeforeCreate = saldoRepository.findAll().size();

        // Create the Saldo
        restSaldoMockMvc.perform(post("/api/saldos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(saldo)))
            .andExpect(status().isCreated());

        // Validate the Saldo in the database
        List<Saldo> saldoList = saldoRepository.findAll();
        assertThat(saldoList).hasSize(databaseSizeBeforeCreate + 1);
        Saldo testSaldo = saldoList.get(saldoList.size() - 1);
        assertThat(testSaldo.getMonto()).isEqualTo(DEFAULT_MONTO);
        assertThat(testSaldo.getMoneda()).isEqualTo(DEFAULT_MONEDA);
        assertThat(testSaldo.getTipo()).isEqualTo(DEFAULT_TIPO);
        assertThat(testSaldo.getFechaCreacion()).isEqualTo(DEFAULT_FECHA_CREACION);
        assertThat(testSaldo.getFechaModificacion()).isEqualTo(DEFAULT_FECHA_MODIFICACION);

        // Validate the Saldo in Elasticsearch
        Saldo saldoEs = saldoSearchRepository.findOne(testSaldo.getId());
        assertThat(saldoEs).isEqualToIgnoringGivenFields(testSaldo);
    }

    @Test
    @Transactional
    public void createSaldoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = saldoRepository.findAll().size();

        // Create the Saldo with an existing ID
        saldo.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSaldoMockMvc.perform(post("/api/saldos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(saldo)))
            .andExpect(status().isBadRequest());

        // Validate the Saldo in the database
        List<Saldo> saldoList = saldoRepository.findAll();
        assertThat(saldoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkMontoIsRequired() throws Exception {
        int databaseSizeBeforeTest = saldoRepository.findAll().size();
        // set the field null
        saldo.setMonto(null);

        // Create the Saldo, which fails.

        restSaldoMockMvc.perform(post("/api/saldos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(saldo)))
            .andExpect(status().isBadRequest());

        List<Saldo> saldoList = saldoRepository.findAll();
        assertThat(saldoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMonedaIsRequired() throws Exception {
        int databaseSizeBeforeTest = saldoRepository.findAll().size();
        // set the field null
        saldo.setMoneda(null);

        // Create the Saldo, which fails.

        restSaldoMockMvc.perform(post("/api/saldos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(saldo)))
            .andExpect(status().isBadRequest());

        List<Saldo> saldoList = saldoRepository.findAll();
        assertThat(saldoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTipoIsRequired() throws Exception {
        int databaseSizeBeforeTest = saldoRepository.findAll().size();
        // set the field null
        saldo.setTipo(null);

        // Create the Saldo, which fails.

        restSaldoMockMvc.perform(post("/api/saldos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(saldo)))
            .andExpect(status().isBadRequest());

        List<Saldo> saldoList = saldoRepository.findAll();
        assertThat(saldoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFechaCreacionIsRequired() throws Exception {
        int databaseSizeBeforeTest = saldoRepository.findAll().size();
        // set the field null
        saldo.setFechaCreacion(null);

        // Create the Saldo, which fails.

        restSaldoMockMvc.perform(post("/api/saldos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(saldo)))
            .andExpect(status().isBadRequest());

        List<Saldo> saldoList = saldoRepository.findAll();
        assertThat(saldoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFechaModificacionIsRequired() throws Exception {
        int databaseSizeBeforeTest = saldoRepository.findAll().size();
        // set the field null
        saldo.setFechaModificacion(null);

        // Create the Saldo, which fails.

        restSaldoMockMvc.perform(post("/api/saldos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(saldo)))
            .andExpect(status().isBadRequest());

        List<Saldo> saldoList = saldoRepository.findAll();
        assertThat(saldoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSaldos() throws Exception {
        // Initialize the database
        saldoRepository.saveAndFlush(saldo);

        // Get all the saldoList
        restSaldoMockMvc.perform(get("/api/saldos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(saldo.getId().intValue())))
            .andExpect(jsonPath("$.[*].monto").value(hasItem(DEFAULT_MONTO.intValue())))
            .andExpect(jsonPath("$.[*].moneda").value(hasItem(DEFAULT_MONEDA.toString())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].fechaCreacion").value(hasItem(DEFAULT_FECHA_CREACION.toString())))
            .andExpect(jsonPath("$.[*].fechaModificacion").value(hasItem(DEFAULT_FECHA_MODIFICACION.toString())));
    }

    @Test
    @Transactional
    public void getSaldo() throws Exception {
        // Initialize the database
        saldoRepository.saveAndFlush(saldo);

        // Get the saldo
        restSaldoMockMvc.perform(get("/api/saldos/{id}", saldo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(saldo.getId().intValue()))
            .andExpect(jsonPath("$.monto").value(DEFAULT_MONTO.intValue()))
            .andExpect(jsonPath("$.moneda").value(DEFAULT_MONEDA.toString()))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO.toString()))
            .andExpect(jsonPath("$.fechaCreacion").value(DEFAULT_FECHA_CREACION.toString()))
            .andExpect(jsonPath("$.fechaModificacion").value(DEFAULT_FECHA_MODIFICACION.toString()));
    }

    @Test
    @Transactional
    public void getAllSaldosByMontoIsEqualToSomething() throws Exception {
        // Initialize the database
        saldoRepository.saveAndFlush(saldo);

        // Get all the saldoList where monto equals to DEFAULT_MONTO
        defaultSaldoShouldBeFound("monto.equals=" + DEFAULT_MONTO);

        // Get all the saldoList where monto equals to UPDATED_MONTO
        defaultSaldoShouldNotBeFound("monto.equals=" + UPDATED_MONTO);
    }

    @Test
    @Transactional
    public void getAllSaldosByMontoIsInShouldWork() throws Exception {
        // Initialize the database
        saldoRepository.saveAndFlush(saldo);

        // Get all the saldoList where monto in DEFAULT_MONTO or UPDATED_MONTO
        defaultSaldoShouldBeFound("monto.in=" + DEFAULT_MONTO + "," + UPDATED_MONTO);

        // Get all the saldoList where monto equals to UPDATED_MONTO
        defaultSaldoShouldNotBeFound("monto.in=" + UPDATED_MONTO);
    }

    @Test
    @Transactional
    public void getAllSaldosByMontoIsNullOrNotNull() throws Exception {
        // Initialize the database
        saldoRepository.saveAndFlush(saldo);

        // Get all the saldoList where monto is not null
        defaultSaldoShouldBeFound("monto.specified=true");

        // Get all the saldoList where monto is null
        defaultSaldoShouldNotBeFound("monto.specified=false");
    }

    @Test
    @Transactional
    public void getAllSaldosByMonedaIsEqualToSomething() throws Exception {
        // Initialize the database
        saldoRepository.saveAndFlush(saldo);

        // Get all the saldoList where moneda equals to DEFAULT_MONEDA
        defaultSaldoShouldBeFound("moneda.equals=" + DEFAULT_MONEDA);

        // Get all the saldoList where moneda equals to UPDATED_MONEDA
        defaultSaldoShouldNotBeFound("moneda.equals=" + UPDATED_MONEDA);
    }

    @Test
    @Transactional
    public void getAllSaldosByMonedaIsInShouldWork() throws Exception {
        // Initialize the database
        saldoRepository.saveAndFlush(saldo);

        // Get all the saldoList where moneda in DEFAULT_MONEDA or UPDATED_MONEDA
        defaultSaldoShouldBeFound("moneda.in=" + DEFAULT_MONEDA + "," + UPDATED_MONEDA);

        // Get all the saldoList where moneda equals to UPDATED_MONEDA
        defaultSaldoShouldNotBeFound("moneda.in=" + UPDATED_MONEDA);
    }

    @Test
    @Transactional
    public void getAllSaldosByMonedaIsNullOrNotNull() throws Exception {
        // Initialize the database
        saldoRepository.saveAndFlush(saldo);

        // Get all the saldoList where moneda is not null
        defaultSaldoShouldBeFound("moneda.specified=true");

        // Get all the saldoList where moneda is null
        defaultSaldoShouldNotBeFound("moneda.specified=false");
    }

    @Test
    @Transactional
    public void getAllSaldosByTipoIsEqualToSomething() throws Exception {
        // Initialize the database
        saldoRepository.saveAndFlush(saldo);

        // Get all the saldoList where tipo equals to DEFAULT_TIPO
        defaultSaldoShouldBeFound("tipo.equals=" + DEFAULT_TIPO);

        // Get all the saldoList where tipo equals to UPDATED_TIPO
        defaultSaldoShouldNotBeFound("tipo.equals=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    public void getAllSaldosByTipoIsInShouldWork() throws Exception {
        // Initialize the database
        saldoRepository.saveAndFlush(saldo);

        // Get all the saldoList where tipo in DEFAULT_TIPO or UPDATED_TIPO
        defaultSaldoShouldBeFound("tipo.in=" + DEFAULT_TIPO + "," + UPDATED_TIPO);

        // Get all the saldoList where tipo equals to UPDATED_TIPO
        defaultSaldoShouldNotBeFound("tipo.in=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    public void getAllSaldosByTipoIsNullOrNotNull() throws Exception {
        // Initialize the database
        saldoRepository.saveAndFlush(saldo);

        // Get all the saldoList where tipo is not null
        defaultSaldoShouldBeFound("tipo.specified=true");

        // Get all the saldoList where tipo is null
        defaultSaldoShouldNotBeFound("tipo.specified=false");
    }

    @Test
    @Transactional
    public void getAllSaldosByFechaCreacionIsEqualToSomething() throws Exception {
        // Initialize the database
        saldoRepository.saveAndFlush(saldo);

        // Get all the saldoList where fechaCreacion equals to DEFAULT_FECHA_CREACION
        defaultSaldoShouldBeFound("fechaCreacion.equals=" + DEFAULT_FECHA_CREACION);

        // Get all the saldoList where fechaCreacion equals to UPDATED_FECHA_CREACION
        defaultSaldoShouldNotBeFound("fechaCreacion.equals=" + UPDATED_FECHA_CREACION);
    }

    @Test
    @Transactional
    public void getAllSaldosByFechaCreacionIsInShouldWork() throws Exception {
        // Initialize the database
        saldoRepository.saveAndFlush(saldo);

        // Get all the saldoList where fechaCreacion in DEFAULT_FECHA_CREACION or UPDATED_FECHA_CREACION
        defaultSaldoShouldBeFound("fechaCreacion.in=" + DEFAULT_FECHA_CREACION + "," + UPDATED_FECHA_CREACION);

        // Get all the saldoList where fechaCreacion equals to UPDATED_FECHA_CREACION
        defaultSaldoShouldNotBeFound("fechaCreacion.in=" + UPDATED_FECHA_CREACION);
    }

    @Test
    @Transactional
    public void getAllSaldosByFechaCreacionIsNullOrNotNull() throws Exception {
        // Initialize the database
        saldoRepository.saveAndFlush(saldo);

        // Get all the saldoList where fechaCreacion is not null
        defaultSaldoShouldBeFound("fechaCreacion.specified=true");

        // Get all the saldoList where fechaCreacion is null
        defaultSaldoShouldNotBeFound("fechaCreacion.specified=false");
    }

    @Test
    @Transactional
    public void getAllSaldosByFechaModificacionIsEqualToSomething() throws Exception {
        // Initialize the database
        saldoRepository.saveAndFlush(saldo);

        // Get all the saldoList where fechaModificacion equals to DEFAULT_FECHA_MODIFICACION
        defaultSaldoShouldBeFound("fechaModificacion.equals=" + DEFAULT_FECHA_MODIFICACION);

        // Get all the saldoList where fechaModificacion equals to UPDATED_FECHA_MODIFICACION
        defaultSaldoShouldNotBeFound("fechaModificacion.equals=" + UPDATED_FECHA_MODIFICACION);
    }

    @Test
    @Transactional
    public void getAllSaldosByFechaModificacionIsInShouldWork() throws Exception {
        // Initialize the database
        saldoRepository.saveAndFlush(saldo);

        // Get all the saldoList where fechaModificacion in DEFAULT_FECHA_MODIFICACION or UPDATED_FECHA_MODIFICACION
        defaultSaldoShouldBeFound("fechaModificacion.in=" + DEFAULT_FECHA_MODIFICACION + "," + UPDATED_FECHA_MODIFICACION);

        // Get all the saldoList where fechaModificacion equals to UPDATED_FECHA_MODIFICACION
        defaultSaldoShouldNotBeFound("fechaModificacion.in=" + UPDATED_FECHA_MODIFICACION);
    }

    @Test
    @Transactional
    public void getAllSaldosByFechaModificacionIsNullOrNotNull() throws Exception {
        // Initialize the database
        saldoRepository.saveAndFlush(saldo);

        // Get all the saldoList where fechaModificacion is not null
        defaultSaldoShouldBeFound("fechaModificacion.specified=true");

        // Get all the saldoList where fechaModificacion is null
        defaultSaldoShouldNotBeFound("fechaModificacion.specified=false");
    }

    @Test
    @Transactional
    public void getAllSaldosByUsuarioIsEqualToSomething() throws Exception {
        // Initialize the database
        User usuario = UserResourceIntTest.createEntity(em);
        em.persist(usuario);
        em.flush();
        saldo.setUsuario(usuario);
        saldoRepository.saveAndFlush(saldo);
        Long usuarioId = usuario.getId();

        // Get all the saldoList where usuario equals to usuarioId
        defaultSaldoShouldBeFound("usuarioId.equals=" + usuarioId);

        // Get all the saldoList where usuario equals to usuarioId + 1
        defaultSaldoShouldNotBeFound("usuarioId.equals=" + (usuarioId + 1));
    }


    @Test
    @Transactional
    public void getAllSaldosByAplicacionIsEqualToSomething() throws Exception {
        // Initialize the database
        App aplicacion = AppResourceIntTest.createEntity(em);
        em.persist(aplicacion);
        em.flush();
        saldo.setAplicacion(aplicacion);
        saldoRepository.saveAndFlush(saldo);
        Long aplicacionId = aplicacion.getId();

        // Get all the saldoList where aplicacion equals to aplicacionId
        defaultSaldoShouldBeFound("aplicacionId.equals=" + aplicacionId);

        // Get all the saldoList where aplicacion equals to aplicacionId + 1
        defaultSaldoShouldNotBeFound("aplicacionId.equals=" + (aplicacionId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultSaldoShouldBeFound(String filter) throws Exception {
        restSaldoMockMvc.perform(get("/api/saldos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(saldo.getId().intValue())))
            .andExpect(jsonPath("$.[*].monto").value(hasItem(DEFAULT_MONTO.intValue())))
            .andExpect(jsonPath("$.[*].moneda").value(hasItem(DEFAULT_MONEDA.toString())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].fechaCreacion").value(hasItem(DEFAULT_FECHA_CREACION.toString())))
            .andExpect(jsonPath("$.[*].fechaModificacion").value(hasItem(DEFAULT_FECHA_MODIFICACION.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultSaldoShouldNotBeFound(String filter) throws Exception {
        restSaldoMockMvc.perform(get("/api/saldos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingSaldo() throws Exception {
        // Get the saldo
        restSaldoMockMvc.perform(get("/api/saldos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSaldo() throws Exception {
        // Initialize the database
        saldoService.save(saldo);

        int databaseSizeBeforeUpdate = saldoRepository.findAll().size();

        // Update the saldo
        Saldo updatedSaldo = saldoRepository.findOne(saldo.getId());
        // Disconnect from session so that the updates on updatedSaldo are not directly saved in db
        em.detach(updatedSaldo);
        updatedSaldo
            .monto(UPDATED_MONTO)
            .moneda(UPDATED_MONEDA)
            .tipo(UPDATED_TIPO)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .fechaModificacion(UPDATED_FECHA_MODIFICACION);

        restSaldoMockMvc.perform(put("/api/saldos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSaldo)))
            .andExpect(status().isOk());

        // Validate the Saldo in the database
        List<Saldo> saldoList = saldoRepository.findAll();
        assertThat(saldoList).hasSize(databaseSizeBeforeUpdate);
        Saldo testSaldo = saldoList.get(saldoList.size() - 1);
        assertThat(testSaldo.getMonto()).isEqualTo(UPDATED_MONTO);
        assertThat(testSaldo.getMoneda()).isEqualTo(UPDATED_MONEDA);
        assertThat(testSaldo.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testSaldo.getFechaCreacion()).isEqualTo(UPDATED_FECHA_CREACION);
        assertThat(testSaldo.getFechaModificacion()).isEqualTo(UPDATED_FECHA_MODIFICACION);

        // Validate the Saldo in Elasticsearch
        Saldo saldoEs = saldoSearchRepository.findOne(testSaldo.getId());
        assertThat(saldoEs).isEqualToIgnoringGivenFields(testSaldo);
    }

    @Test
    @Transactional
    public void updateNonExistingSaldo() throws Exception {
        int databaseSizeBeforeUpdate = saldoRepository.findAll().size();

        // Create the Saldo

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSaldoMockMvc.perform(put("/api/saldos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(saldo)))
            .andExpect(status().isCreated());

        // Validate the Saldo in the database
        List<Saldo> saldoList = saldoRepository.findAll();
        assertThat(saldoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSaldo() throws Exception {
        // Initialize the database
        saldoService.save(saldo);

        int databaseSizeBeforeDelete = saldoRepository.findAll().size();

        // Get the saldo
        restSaldoMockMvc.perform(delete("/api/saldos/{id}", saldo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean saldoExistsInEs = saldoSearchRepository.exists(saldo.getId());
        assertThat(saldoExistsInEs).isFalse();

        // Validate the database is empty
        List<Saldo> saldoList = saldoRepository.findAll();
        assertThat(saldoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSaldo() throws Exception {
        // Initialize the database
        saldoService.save(saldo);

        // Search the saldo
        restSaldoMockMvc.perform(get("/api/_search/saldos?query=id:" + saldo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(saldo.getId().intValue())))
            .andExpect(jsonPath("$.[*].monto").value(hasItem(DEFAULT_MONTO.intValue())))
            .andExpect(jsonPath("$.[*].moneda").value(hasItem(DEFAULT_MONEDA.toString())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].fechaCreacion").value(hasItem(DEFAULT_FECHA_CREACION.toString())))
            .andExpect(jsonPath("$.[*].fechaModificacion").value(hasItem(DEFAULT_FECHA_MODIFICACION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Saldo.class);
        Saldo saldo1 = new Saldo();
        saldo1.setId(1L);
        Saldo saldo2 = new Saldo();
        saldo2.setId(saldo1.getId());
        assertThat(saldo1).isEqualTo(saldo2);
        saldo2.setId(2L);
        assertThat(saldo1).isNotEqualTo(saldo2);
        saldo1.setId(null);
        assertThat(saldo1).isNotEqualTo(saldo2);
    }
}
