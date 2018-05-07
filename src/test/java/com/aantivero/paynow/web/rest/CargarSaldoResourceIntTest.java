package com.aantivero.paynow.web.rest;

import com.aantivero.paynow.PaynowApp;

import com.aantivero.paynow.domain.CargarSaldo;
import com.aantivero.paynow.domain.User;
import com.aantivero.paynow.domain.Cuenta;
import com.aantivero.paynow.domain.TransferenciaApp;
import com.aantivero.paynow.repository.CargarSaldoRepository;
import com.aantivero.paynow.service.CargarSaldoService;
import com.aantivero.paynow.repository.search.CargarSaldoSearchRepository;
import com.aantivero.paynow.web.rest.errors.ExceptionTranslator;
import com.aantivero.paynow.service.dto.CargarSaldoCriteria;
import com.aantivero.paynow.service.CargarSaldoQueryService;

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

/**
 * Test class for the CargarSaldoResource REST controller.
 *
 * @see CargarSaldoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PaynowApp.class)
public class CargarSaldoResourceIntTest {

    private static final BigDecimal DEFAULT_MONTO = new BigDecimal(0);
    private static final BigDecimal UPDATED_MONTO = new BigDecimal(1);

    @Autowired
    private CargarSaldoRepository cargarSaldoRepository;

    @Autowired
    private CargarSaldoService cargarSaldoService;

    @Autowired
    private CargarSaldoSearchRepository cargarSaldoSearchRepository;

    @Autowired
    private CargarSaldoQueryService cargarSaldoQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCargarSaldoMockMvc;

    private CargarSaldo cargarSaldo;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CargarSaldoResource cargarSaldoResource = new CargarSaldoResource(cargarSaldoService, cargarSaldoQueryService);
        this.restCargarSaldoMockMvc = MockMvcBuilders.standaloneSetup(cargarSaldoResource)
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
    public static CargarSaldo createEntity(EntityManager em) {
        CargarSaldo cargarSaldo = new CargarSaldo()
            .monto(DEFAULT_MONTO);
        // Add required entity
        User usuario = UserResourceIntTest.createEntity(em);
        em.persist(usuario);
        em.flush();
        cargarSaldo.setUsuario(usuario);
        // Add required entity
        Cuenta cuenta = CuentaResourceIntTest.createEntity(em);
        em.persist(cuenta);
        em.flush();
        cargarSaldo.setCuenta(cuenta);
        return cargarSaldo;
    }

    @Before
    public void initTest() {
        cargarSaldoSearchRepository.deleteAll();
        cargarSaldo = createEntity(em);
    }

    @Test
    @Transactional
    public void createCargarSaldo() throws Exception {
        int databaseSizeBeforeCreate = cargarSaldoRepository.findAll().size();

        // Create the CargarSaldo
        restCargarSaldoMockMvc.perform(post("/api/cargar-saldos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cargarSaldo)))
            .andExpect(status().isCreated());

        // Validate the CargarSaldo in the database
        List<CargarSaldo> cargarSaldoList = cargarSaldoRepository.findAll();
        assertThat(cargarSaldoList).hasSize(databaseSizeBeforeCreate + 1);
        CargarSaldo testCargarSaldo = cargarSaldoList.get(cargarSaldoList.size() - 1);
        assertThat(testCargarSaldo.getMonto()).isEqualTo(DEFAULT_MONTO);

        // Validate the CargarSaldo in Elasticsearch
        CargarSaldo cargarSaldoEs = cargarSaldoSearchRepository.findOne(testCargarSaldo.getId());
        assertThat(cargarSaldoEs).isEqualToIgnoringGivenFields(testCargarSaldo);
    }

    @Test
    @Transactional
    public void createCargarSaldoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cargarSaldoRepository.findAll().size();

        // Create the CargarSaldo with an existing ID
        cargarSaldo.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCargarSaldoMockMvc.perform(post("/api/cargar-saldos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cargarSaldo)))
            .andExpect(status().isBadRequest());

        // Validate the CargarSaldo in the database
        List<CargarSaldo> cargarSaldoList = cargarSaldoRepository.findAll();
        assertThat(cargarSaldoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkMontoIsRequired() throws Exception {
        int databaseSizeBeforeTest = cargarSaldoRepository.findAll().size();
        // set the field null
        cargarSaldo.setMonto(null);

        // Create the CargarSaldo, which fails.

        restCargarSaldoMockMvc.perform(post("/api/cargar-saldos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cargarSaldo)))
            .andExpect(status().isBadRequest());

        List<CargarSaldo> cargarSaldoList = cargarSaldoRepository.findAll();
        assertThat(cargarSaldoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCargarSaldos() throws Exception {
        // Initialize the database
        cargarSaldoRepository.saveAndFlush(cargarSaldo);

        // Get all the cargarSaldoList
        restCargarSaldoMockMvc.perform(get("/api/cargar-saldos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cargarSaldo.getId().intValue())))
            .andExpect(jsonPath("$.[*].monto").value(hasItem(DEFAULT_MONTO.intValue())));
    }

    @Test
    @Transactional
    public void getCargarSaldo() throws Exception {
        // Initialize the database
        cargarSaldoRepository.saveAndFlush(cargarSaldo);

        // Get the cargarSaldo
        restCargarSaldoMockMvc.perform(get("/api/cargar-saldos/{id}", cargarSaldo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cargarSaldo.getId().intValue()))
            .andExpect(jsonPath("$.monto").value(DEFAULT_MONTO.intValue()));
    }

    @Test
    @Transactional
    public void getAllCargarSaldosByMontoIsEqualToSomething() throws Exception {
        // Initialize the database
        cargarSaldoRepository.saveAndFlush(cargarSaldo);

        // Get all the cargarSaldoList where monto equals to DEFAULT_MONTO
        defaultCargarSaldoShouldBeFound("monto.equals=" + DEFAULT_MONTO);

        // Get all the cargarSaldoList where monto equals to UPDATED_MONTO
        defaultCargarSaldoShouldNotBeFound("monto.equals=" + UPDATED_MONTO);
    }

    @Test
    @Transactional
    public void getAllCargarSaldosByMontoIsInShouldWork() throws Exception {
        // Initialize the database
        cargarSaldoRepository.saveAndFlush(cargarSaldo);

        // Get all the cargarSaldoList where monto in DEFAULT_MONTO or UPDATED_MONTO
        defaultCargarSaldoShouldBeFound("monto.in=" + DEFAULT_MONTO + "," + UPDATED_MONTO);

        // Get all the cargarSaldoList where monto equals to UPDATED_MONTO
        defaultCargarSaldoShouldNotBeFound("monto.in=" + UPDATED_MONTO);
    }

    @Test
    @Transactional
    public void getAllCargarSaldosByMontoIsNullOrNotNull() throws Exception {
        // Initialize the database
        cargarSaldoRepository.saveAndFlush(cargarSaldo);

        // Get all the cargarSaldoList where monto is not null
        defaultCargarSaldoShouldBeFound("monto.specified=true");

        // Get all the cargarSaldoList where monto is null
        defaultCargarSaldoShouldNotBeFound("monto.specified=false");
    }

    @Test
    @Transactional
    public void getAllCargarSaldosByUsuarioIsEqualToSomething() throws Exception {
        // Initialize the database
        User usuario = UserResourceIntTest.createEntity(em);
        em.persist(usuario);
        em.flush();
        cargarSaldo.setUsuario(usuario);
        cargarSaldoRepository.saveAndFlush(cargarSaldo);
        Long usuarioId = usuario.getId();

        // Get all the cargarSaldoList where usuario equals to usuarioId
        defaultCargarSaldoShouldBeFound("usuarioId.equals=" + usuarioId);

        // Get all the cargarSaldoList where usuario equals to usuarioId + 1
        defaultCargarSaldoShouldNotBeFound("usuarioId.equals=" + (usuarioId + 1));
    }


    @Test
    @Transactional
    public void getAllCargarSaldosByCuentaIsEqualToSomething() throws Exception {
        // Initialize the database
        Cuenta cuenta = CuentaResourceIntTest.createEntity(em);
        em.persist(cuenta);
        em.flush();
        cargarSaldo.setCuenta(cuenta);
        cargarSaldoRepository.saveAndFlush(cargarSaldo);
        Long cuentaId = cuenta.getId();

        // Get all the cargarSaldoList where cuenta equals to cuentaId
        defaultCargarSaldoShouldBeFound("cuentaId.equals=" + cuentaId);

        // Get all the cargarSaldoList where cuenta equals to cuentaId + 1
        defaultCargarSaldoShouldNotBeFound("cuentaId.equals=" + (cuentaId + 1));
    }


    @Test
    @Transactional
    public void getAllCargarSaldosByTransferenciaAppIsEqualToSomething() throws Exception {
        // Initialize the database
        TransferenciaApp transferenciaApp = TransferenciaAppResourceIntTest.createEntity(em);
        em.persist(transferenciaApp);
        em.flush();
        cargarSaldo.setTransferenciaApp(transferenciaApp);
        cargarSaldoRepository.saveAndFlush(cargarSaldo);
        Long transferenciaAppId = transferenciaApp.getId();

        // Get all the cargarSaldoList where transferenciaApp equals to transferenciaAppId
        defaultCargarSaldoShouldBeFound("transferenciaAppId.equals=" + transferenciaAppId);

        // Get all the cargarSaldoList where transferenciaApp equals to transferenciaAppId + 1
        defaultCargarSaldoShouldNotBeFound("transferenciaAppId.equals=" + (transferenciaAppId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultCargarSaldoShouldBeFound(String filter) throws Exception {
        restCargarSaldoMockMvc.perform(get("/api/cargar-saldos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cargarSaldo.getId().intValue())))
            .andExpect(jsonPath("$.[*].monto").value(hasItem(DEFAULT_MONTO.intValue())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultCargarSaldoShouldNotBeFound(String filter) throws Exception {
        restCargarSaldoMockMvc.perform(get("/api/cargar-saldos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingCargarSaldo() throws Exception {
        // Get the cargarSaldo
        restCargarSaldoMockMvc.perform(get("/api/cargar-saldos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCargarSaldo() throws Exception {
        // Initialize the database
        cargarSaldoService.save(cargarSaldo);

        int databaseSizeBeforeUpdate = cargarSaldoRepository.findAll().size();

        // Update the cargarSaldo
        CargarSaldo updatedCargarSaldo = cargarSaldoRepository.findOne(cargarSaldo.getId());
        // Disconnect from session so that the updates on updatedCargarSaldo are not directly saved in db
        em.detach(updatedCargarSaldo);
        updatedCargarSaldo
            .monto(UPDATED_MONTO);

        restCargarSaldoMockMvc.perform(put("/api/cargar-saldos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCargarSaldo)))
            .andExpect(status().isOk());

        // Validate the CargarSaldo in the database
        List<CargarSaldo> cargarSaldoList = cargarSaldoRepository.findAll();
        assertThat(cargarSaldoList).hasSize(databaseSizeBeforeUpdate);
        CargarSaldo testCargarSaldo = cargarSaldoList.get(cargarSaldoList.size() - 1);
        assertThat(testCargarSaldo.getMonto()).isEqualTo(UPDATED_MONTO);

        // Validate the CargarSaldo in Elasticsearch
        CargarSaldo cargarSaldoEs = cargarSaldoSearchRepository.findOne(testCargarSaldo.getId());
        assertThat(cargarSaldoEs).isEqualToIgnoringGivenFields(testCargarSaldo);
    }

    @Test
    @Transactional
    public void updateNonExistingCargarSaldo() throws Exception {
        int databaseSizeBeforeUpdate = cargarSaldoRepository.findAll().size();

        // Create the CargarSaldo

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCargarSaldoMockMvc.perform(put("/api/cargar-saldos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cargarSaldo)))
            .andExpect(status().isCreated());

        // Validate the CargarSaldo in the database
        List<CargarSaldo> cargarSaldoList = cargarSaldoRepository.findAll();
        assertThat(cargarSaldoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCargarSaldo() throws Exception {
        // Initialize the database
        cargarSaldoService.save(cargarSaldo);

        int databaseSizeBeforeDelete = cargarSaldoRepository.findAll().size();

        // Get the cargarSaldo
        restCargarSaldoMockMvc.perform(delete("/api/cargar-saldos/{id}", cargarSaldo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean cargarSaldoExistsInEs = cargarSaldoSearchRepository.exists(cargarSaldo.getId());
        assertThat(cargarSaldoExistsInEs).isFalse();

        // Validate the database is empty
        List<CargarSaldo> cargarSaldoList = cargarSaldoRepository.findAll();
        assertThat(cargarSaldoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCargarSaldo() throws Exception {
        // Initialize the database
        cargarSaldoService.save(cargarSaldo);

        // Search the cargarSaldo
        restCargarSaldoMockMvc.perform(get("/api/_search/cargar-saldos?query=id:" + cargarSaldo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cargarSaldo.getId().intValue())))
            .andExpect(jsonPath("$.[*].monto").value(hasItem(DEFAULT_MONTO.intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CargarSaldo.class);
        CargarSaldo cargarSaldo1 = new CargarSaldo();
        cargarSaldo1.setId(1L);
        CargarSaldo cargarSaldo2 = new CargarSaldo();
        cargarSaldo2.setId(cargarSaldo1.getId());
        assertThat(cargarSaldo1).isEqualTo(cargarSaldo2);
        cargarSaldo2.setId(2L);
        assertThat(cargarSaldo1).isNotEqualTo(cargarSaldo2);
        cargarSaldo1.setId(null);
        assertThat(cargarSaldo1).isNotEqualTo(cargarSaldo2);
    }
}
