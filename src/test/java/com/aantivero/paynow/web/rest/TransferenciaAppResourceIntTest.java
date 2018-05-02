package com.aantivero.paynow.web.rest;

import com.aantivero.paynow.PaynowApp;

import com.aantivero.paynow.domain.TransferenciaApp;
import com.aantivero.paynow.domain.CuentaApp;
import com.aantivero.paynow.domain.Banco;
import com.aantivero.paynow.repository.TransferenciaAppRepository;
import com.aantivero.paynow.service.TransferenciaAppService;
import com.aantivero.paynow.repository.search.TransferenciaAppSearchRepository;
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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.aantivero.paynow.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.aantivero.paynow.domain.enumeration.Moneda;
import com.aantivero.paynow.domain.enumeration.EstadoTransferencia;
/**
 * Test class for the TransferenciaAppResource REST controller.
 *
 * @see TransferenciaAppResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PaynowApp.class)
public class TransferenciaAppResourceIntTest {

    private static final String DEFAULT_DESTINO_CBU = "AAAAAAAAAA";
    private static final String UPDATED_DESTINO_CBU = "BBBBBBBBBB";

    private static final String DEFAULT_DESTINO_ALIAS = "AAAAAAAAAA";
    private static final String UPDATED_DESTINO_ALIAS = "BBBBBBBBBB";

    private static final String DEFAULT_DESTINO_INFO = "AAAAAAAAAA";
    private static final String UPDATED_DESTINO_INFO = "BBBBBBBBBB";

    private static final Moneda DEFAULT_MONEDA = Moneda.PESOS;
    private static final Moneda UPDATED_MONEDA = Moneda.PESOS;

    private static final BigDecimal DEFAULT_MONTO = new BigDecimal(2);
    private static final BigDecimal UPDATED_MONTO = new BigDecimal(1);

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final EstadoTransferencia DEFAULT_ESTADO_TRANSFERENCIA = EstadoTransferencia.ENVIADA;
    private static final EstadoTransferencia UPDATED_ESTADO_TRANSFERENCIA = EstadoTransferencia.ACEPTADA;

    private static final Instant DEFAULT_TIMESTAMP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIMESTAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DESCRIPCION_ESTADO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION_ESTADO = "BBBBBBBBBB";

    private static final String DEFAULT_IDENTIFICACION = "AAAAAAAAAA";
    private static final String UPDATED_IDENTIFICACION = "BBBBBBBBBB";

    @Autowired
    private TransferenciaAppRepository transferenciaAppRepository;

    @Autowired
    private TransferenciaAppService transferenciaAppService;

    @Autowired
    private TransferenciaAppSearchRepository transferenciaAppSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTransferenciaAppMockMvc;

    private TransferenciaApp transferenciaApp;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TransferenciaAppResource transferenciaAppResource = new TransferenciaAppResource(transferenciaAppService);
        this.restTransferenciaAppMockMvc = MockMvcBuilders.standaloneSetup(transferenciaAppResource)
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
    public static TransferenciaApp createEntity(EntityManager em) {
        TransferenciaApp transferenciaApp = new TransferenciaApp()
            .destinoCbu(DEFAULT_DESTINO_CBU)
            .destinoAlias(DEFAULT_DESTINO_ALIAS)
            .destinoInfo(DEFAULT_DESTINO_INFO)
            .moneda(DEFAULT_MONEDA)
            .monto(DEFAULT_MONTO)
            .descripcion(DEFAULT_DESCRIPCION)
            .estadoTransferencia(DEFAULT_ESTADO_TRANSFERENCIA)
            .timestamp(DEFAULT_TIMESTAMP)
            .descripcionEstado(DEFAULT_DESCRIPCION_ESTADO)
            .identificacion(DEFAULT_IDENTIFICACION);
        // Add required entity
        CuentaApp origen = CuentaAppResourceIntTest.createEntity(em);
        em.persist(origen);
        em.flush();
        transferenciaApp.setOrigen(origen);
        // Add required entity
        Banco destinoBanco = BancoResourceIntTest.createEntity(em);
        em.persist(destinoBanco);
        em.flush();
        transferenciaApp.setDestinoBanco(destinoBanco);
        return transferenciaApp;
    }

    @Before
    public void initTest() {
        transferenciaAppSearchRepository.deleteAll();
        transferenciaApp = createEntity(em);
    }

    @Test
    @Transactional
    public void createTransferenciaApp() throws Exception {
        int databaseSizeBeforeCreate = transferenciaAppRepository.findAll().size();

        // Create the TransferenciaApp
        restTransferenciaAppMockMvc.perform(post("/api/transferencia-apps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transferenciaApp)))
            .andExpect(status().isCreated());

        // Validate the TransferenciaApp in the database
        List<TransferenciaApp> transferenciaAppList = transferenciaAppRepository.findAll();
        assertThat(transferenciaAppList).hasSize(databaseSizeBeforeCreate + 1);
        TransferenciaApp testTransferenciaApp = transferenciaAppList.get(transferenciaAppList.size() - 1);
        assertThat(testTransferenciaApp.getDestinoCbu()).isEqualTo(DEFAULT_DESTINO_CBU);
        assertThat(testTransferenciaApp.getDestinoAlias()).isEqualTo(DEFAULT_DESTINO_ALIAS);
        assertThat(testTransferenciaApp.getDestinoInfo()).isEqualTo(DEFAULT_DESTINO_INFO);
        assertThat(testTransferenciaApp.getMoneda()).isEqualTo(DEFAULT_MONEDA);
        assertThat(testTransferenciaApp.getMonto()).isEqualTo(DEFAULT_MONTO);
        assertThat(testTransferenciaApp.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testTransferenciaApp.getEstadoTransferencia()).isEqualTo(DEFAULT_ESTADO_TRANSFERENCIA);
        assertThat(testTransferenciaApp.getTimestamp()).isEqualTo(DEFAULT_TIMESTAMP);
        assertThat(testTransferenciaApp.getDescripcionEstado()).isEqualTo(DEFAULT_DESCRIPCION_ESTADO);
        assertThat(testTransferenciaApp.getIdentificacion()).isEqualTo(DEFAULT_IDENTIFICACION);

        // Validate the TransferenciaApp in Elasticsearch
        TransferenciaApp transferenciaAppEs = transferenciaAppSearchRepository.findOne(testTransferenciaApp.getId());
        assertThat(transferenciaAppEs).isEqualToIgnoringGivenFields(testTransferenciaApp);
    }

    @Test
    @Transactional
    public void createTransferenciaAppWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = transferenciaAppRepository.findAll().size();

        // Create the TransferenciaApp with an existing ID
        transferenciaApp.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransferenciaAppMockMvc.perform(post("/api/transferencia-apps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transferenciaApp)))
            .andExpect(status().isBadRequest());

        // Validate the TransferenciaApp in the database
        List<TransferenciaApp> transferenciaAppList = transferenciaAppRepository.findAll();
        assertThat(transferenciaAppList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkMonedaIsRequired() throws Exception {
        int databaseSizeBeforeTest = transferenciaAppRepository.findAll().size();
        // set the field null
        transferenciaApp.setMoneda(null);

        // Create the TransferenciaApp, which fails.

        restTransferenciaAppMockMvc.perform(post("/api/transferencia-apps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transferenciaApp)))
            .andExpect(status().isBadRequest());

        List<TransferenciaApp> transferenciaAppList = transferenciaAppRepository.findAll();
        assertThat(transferenciaAppList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMontoIsRequired() throws Exception {
        int databaseSizeBeforeTest = transferenciaAppRepository.findAll().size();
        // set the field null
        transferenciaApp.setMonto(null);

        // Create the TransferenciaApp, which fails.

        restTransferenciaAppMockMvc.perform(post("/api/transferencia-apps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transferenciaApp)))
            .andExpect(status().isBadRequest());

        List<TransferenciaApp> transferenciaAppList = transferenciaAppRepository.findAll();
        assertThat(transferenciaAppList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEstadoTransferenciaIsRequired() throws Exception {
        int databaseSizeBeforeTest = transferenciaAppRepository.findAll().size();
        // set the field null
        transferenciaApp.setEstadoTransferencia(null);

        // Create the TransferenciaApp, which fails.

        restTransferenciaAppMockMvc.perform(post("/api/transferencia-apps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transferenciaApp)))
            .andExpect(status().isBadRequest());

        List<TransferenciaApp> transferenciaAppList = transferenciaAppRepository.findAll();
        assertThat(transferenciaAppList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTimestampIsRequired() throws Exception {
        int databaseSizeBeforeTest = transferenciaAppRepository.findAll().size();
        // set the field null
        transferenciaApp.setTimestamp(null);

        // Create the TransferenciaApp, which fails.

        restTransferenciaAppMockMvc.perform(post("/api/transferencia-apps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transferenciaApp)))
            .andExpect(status().isBadRequest());

        List<TransferenciaApp> transferenciaAppList = transferenciaAppRepository.findAll();
        assertThat(transferenciaAppList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTransferenciaApps() throws Exception {
        // Initialize the database
        transferenciaAppRepository.saveAndFlush(transferenciaApp);

        // Get all the transferenciaAppList
        restTransferenciaAppMockMvc.perform(get("/api/transferencia-apps?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transferenciaApp.getId().intValue())))
            .andExpect(jsonPath("$.[*].destinoCbu").value(hasItem(DEFAULT_DESTINO_CBU.toString())))
            .andExpect(jsonPath("$.[*].destinoAlias").value(hasItem(DEFAULT_DESTINO_ALIAS.toString())))
            .andExpect(jsonPath("$.[*].destinoInfo").value(hasItem(DEFAULT_DESTINO_INFO.toString())))
            .andExpect(jsonPath("$.[*].moneda").value(hasItem(DEFAULT_MONEDA.toString())))
            .andExpect(jsonPath("$.[*].monto").value(hasItem(DEFAULT_MONTO.intValue())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString())))
            .andExpect(jsonPath("$.[*].estadoTransferencia").value(hasItem(DEFAULT_ESTADO_TRANSFERENCIA.toString())))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())))
            .andExpect(jsonPath("$.[*].descripcionEstado").value(hasItem(DEFAULT_DESCRIPCION_ESTADO.toString())))
            .andExpect(jsonPath("$.[*].identificacion").value(hasItem(DEFAULT_IDENTIFICACION.toString())));
    }

    @Test
    @Transactional
    public void getTransferenciaApp() throws Exception {
        // Initialize the database
        transferenciaAppRepository.saveAndFlush(transferenciaApp);

        // Get the transferenciaApp
        restTransferenciaAppMockMvc.perform(get("/api/transferencia-apps/{id}", transferenciaApp.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(transferenciaApp.getId().intValue()))
            .andExpect(jsonPath("$.destinoCbu").value(DEFAULT_DESTINO_CBU.toString()))
            .andExpect(jsonPath("$.destinoAlias").value(DEFAULT_DESTINO_ALIAS.toString()))
            .andExpect(jsonPath("$.destinoInfo").value(DEFAULT_DESTINO_INFO.toString()))
            .andExpect(jsonPath("$.moneda").value(DEFAULT_MONEDA.toString()))
            .andExpect(jsonPath("$.monto").value(DEFAULT_MONTO.intValue()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION.toString()))
            .andExpect(jsonPath("$.estadoTransferencia").value(DEFAULT_ESTADO_TRANSFERENCIA.toString()))
            .andExpect(jsonPath("$.timestamp").value(DEFAULT_TIMESTAMP.toString()))
            .andExpect(jsonPath("$.descripcionEstado").value(DEFAULT_DESCRIPCION_ESTADO.toString()))
            .andExpect(jsonPath("$.identificacion").value(DEFAULT_IDENTIFICACION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTransferenciaApp() throws Exception {
        // Get the transferenciaApp
        restTransferenciaAppMockMvc.perform(get("/api/transferencia-apps/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTransferenciaApp() throws Exception {
        // Initialize the database
        transferenciaAppService.save(transferenciaApp);

        int databaseSizeBeforeUpdate = transferenciaAppRepository.findAll().size();

        // Update the transferenciaApp
        TransferenciaApp updatedTransferenciaApp = transferenciaAppRepository.findOne(transferenciaApp.getId());
        // Disconnect from session so that the updates on updatedTransferenciaApp are not directly saved in db
        em.detach(updatedTransferenciaApp);
        updatedTransferenciaApp
            .destinoCbu(UPDATED_DESTINO_CBU)
            .destinoAlias(UPDATED_DESTINO_ALIAS)
            .destinoInfo(UPDATED_DESTINO_INFO)
            .moneda(UPDATED_MONEDA)
            .monto(UPDATED_MONTO)
            .descripcion(UPDATED_DESCRIPCION)
            .estadoTransferencia(UPDATED_ESTADO_TRANSFERENCIA)
            .timestamp(UPDATED_TIMESTAMP)
            .descripcionEstado(UPDATED_DESCRIPCION_ESTADO)
            .identificacion(UPDATED_IDENTIFICACION);

        restTransferenciaAppMockMvc.perform(put("/api/transferencia-apps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTransferenciaApp)))
            .andExpect(status().isOk());

        // Validate the TransferenciaApp in the database
        List<TransferenciaApp> transferenciaAppList = transferenciaAppRepository.findAll();
        assertThat(transferenciaAppList).hasSize(databaseSizeBeforeUpdate);
        TransferenciaApp testTransferenciaApp = transferenciaAppList.get(transferenciaAppList.size() - 1);
        assertThat(testTransferenciaApp.getDestinoCbu()).isEqualTo(UPDATED_DESTINO_CBU);
        assertThat(testTransferenciaApp.getDestinoAlias()).isEqualTo(UPDATED_DESTINO_ALIAS);
        assertThat(testTransferenciaApp.getDestinoInfo()).isEqualTo(UPDATED_DESTINO_INFO);
        assertThat(testTransferenciaApp.getMoneda()).isEqualTo(UPDATED_MONEDA);
        assertThat(testTransferenciaApp.getMonto()).isEqualTo(UPDATED_MONTO);
        assertThat(testTransferenciaApp.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testTransferenciaApp.getEstadoTransferencia()).isEqualTo(UPDATED_ESTADO_TRANSFERENCIA);
        assertThat(testTransferenciaApp.getTimestamp()).isEqualTo(UPDATED_TIMESTAMP);
        assertThat(testTransferenciaApp.getDescripcionEstado()).isEqualTo(UPDATED_DESCRIPCION_ESTADO);
        assertThat(testTransferenciaApp.getIdentificacion()).isEqualTo(UPDATED_IDENTIFICACION);

        // Validate the TransferenciaApp in Elasticsearch
        TransferenciaApp transferenciaAppEs = transferenciaAppSearchRepository.findOne(testTransferenciaApp.getId());
        assertThat(transferenciaAppEs).isEqualToIgnoringGivenFields(testTransferenciaApp);
    }

    @Test
    @Transactional
    public void updateNonExistingTransferenciaApp() throws Exception {
        int databaseSizeBeforeUpdate = transferenciaAppRepository.findAll().size();

        // Create the TransferenciaApp

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTransferenciaAppMockMvc.perform(put("/api/transferencia-apps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transferenciaApp)))
            .andExpect(status().isCreated());

        // Validate the TransferenciaApp in the database
        List<TransferenciaApp> transferenciaAppList = transferenciaAppRepository.findAll();
        assertThat(transferenciaAppList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTransferenciaApp() throws Exception {
        // Initialize the database
        transferenciaAppService.save(transferenciaApp);

        int databaseSizeBeforeDelete = transferenciaAppRepository.findAll().size();

        // Get the transferenciaApp
        restTransferenciaAppMockMvc.perform(delete("/api/transferencia-apps/{id}", transferenciaApp.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean transferenciaAppExistsInEs = transferenciaAppSearchRepository.exists(transferenciaApp.getId());
        assertThat(transferenciaAppExistsInEs).isFalse();

        // Validate the database is empty
        List<TransferenciaApp> transferenciaAppList = transferenciaAppRepository.findAll();
        assertThat(transferenciaAppList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTransferenciaApp() throws Exception {
        // Initialize the database
        transferenciaAppService.save(transferenciaApp);

        // Search the transferenciaApp
        restTransferenciaAppMockMvc.perform(get("/api/_search/transferencia-apps?query=id:" + transferenciaApp.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transferenciaApp.getId().intValue())))
            .andExpect(jsonPath("$.[*].destinoCbu").value(hasItem(DEFAULT_DESTINO_CBU.toString())))
            .andExpect(jsonPath("$.[*].destinoAlias").value(hasItem(DEFAULT_DESTINO_ALIAS.toString())))
            .andExpect(jsonPath("$.[*].destinoInfo").value(hasItem(DEFAULT_DESTINO_INFO.toString())))
            .andExpect(jsonPath("$.[*].moneda").value(hasItem(DEFAULT_MONEDA.toString())))
            .andExpect(jsonPath("$.[*].monto").value(hasItem(DEFAULT_MONTO.intValue())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString())))
            .andExpect(jsonPath("$.[*].estadoTransferencia").value(hasItem(DEFAULT_ESTADO_TRANSFERENCIA.toString())))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())))
            .andExpect(jsonPath("$.[*].descripcionEstado").value(hasItem(DEFAULT_DESCRIPCION_ESTADO.toString())))
            .andExpect(jsonPath("$.[*].identificacion").value(hasItem(DEFAULT_IDENTIFICACION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransferenciaApp.class);
        TransferenciaApp transferenciaApp1 = new TransferenciaApp();
        transferenciaApp1.setId(1L);
        TransferenciaApp transferenciaApp2 = new TransferenciaApp();
        transferenciaApp2.setId(transferenciaApp1.getId());
        assertThat(transferenciaApp1).isEqualTo(transferenciaApp2);
        transferenciaApp2.setId(2L);
        assertThat(transferenciaApp1).isNotEqualTo(transferenciaApp2);
        transferenciaApp1.setId(null);
        assertThat(transferenciaApp1).isNotEqualTo(transferenciaApp2);
    }
}
