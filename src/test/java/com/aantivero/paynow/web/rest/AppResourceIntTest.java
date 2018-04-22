package com.aantivero.paynow.web.rest;

import com.aantivero.paynow.PaynowApp;

import com.aantivero.paynow.domain.App;
import com.aantivero.paynow.repository.AppRepository;
import com.aantivero.paynow.service.AppService;
import com.aantivero.paynow.repository.search.AppSearchRepository;
import com.aantivero.paynow.web.rest.errors.ExceptionTranslator;
import com.aantivero.paynow.service.dto.AppCriteria;
import com.aantivero.paynow.service.AppQueryService;

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
 * Test class for the AppResource REST controller.
 *
 * @see AppResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PaynowApp.class)
public class AppResourceIntTest {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    @Autowired
    private AppRepository appRepository;

    @Autowired
    private AppService appService;

    @Autowired
    private AppSearchRepository appSearchRepository;

    @Autowired
    private AppQueryService appQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAppMockMvc;

    private App app;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AppResource appResource = new AppResource(appService, appQueryService);
        this.restAppMockMvc = MockMvcBuilders.standaloneSetup(appResource)
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
    public static App createEntity(EntityManager em) {
        App app = new App()
            .nombre(DEFAULT_NOMBRE);
        return app;
    }

    @Before
    public void initTest() {
        appSearchRepository.deleteAll();
        app = createEntity(em);
    }

    @Test
    @Transactional
    public void createApp() throws Exception {
        int databaseSizeBeforeCreate = appRepository.findAll().size();

        // Create the App
        restAppMockMvc.perform(post("/api/apps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(app)))
            .andExpect(status().isCreated());

        // Validate the App in the database
        List<App> appList = appRepository.findAll();
        assertThat(appList).hasSize(databaseSizeBeforeCreate + 1);
        App testApp = appList.get(appList.size() - 1);
        assertThat(testApp.getNombre()).isEqualTo(DEFAULT_NOMBRE);

        // Validate the App in Elasticsearch
        App appEs = appSearchRepository.findOne(testApp.getId());
        assertThat(appEs).isEqualToIgnoringGivenFields(testApp);
    }

    @Test
    @Transactional
    public void createAppWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = appRepository.findAll().size();

        // Create the App with an existing ID
        app.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAppMockMvc.perform(post("/api/apps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(app)))
            .andExpect(status().isBadRequest());

        // Validate the App in the database
        List<App> appList = appRepository.findAll();
        assertThat(appList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = appRepository.findAll().size();
        // set the field null
        app.setNombre(null);

        // Create the App, which fails.

        restAppMockMvc.perform(post("/api/apps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(app)))
            .andExpect(status().isBadRequest());

        List<App> appList = appRepository.findAll();
        assertThat(appList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllApps() throws Exception {
        // Initialize the database
        appRepository.saveAndFlush(app);

        // Get all the appList
        restAppMockMvc.perform(get("/api/apps?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(app.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())));
    }

    @Test
    @Transactional
    public void getApp() throws Exception {
        // Initialize the database
        appRepository.saveAndFlush(app);

        // Get the app
        restAppMockMvc.perform(get("/api/apps/{id}", app.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(app.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()));
    }

    @Test
    @Transactional
    public void getAllAppsByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        appRepository.saveAndFlush(app);

        // Get all the appList where nombre equals to DEFAULT_NOMBRE
        defaultAppShouldBeFound("nombre.equals=" + DEFAULT_NOMBRE);

        // Get all the appList where nombre equals to UPDATED_NOMBRE
        defaultAppShouldNotBeFound("nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllAppsByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        appRepository.saveAndFlush(app);

        // Get all the appList where nombre in DEFAULT_NOMBRE or UPDATED_NOMBRE
        defaultAppShouldBeFound("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE);

        // Get all the appList where nombre equals to UPDATED_NOMBRE
        defaultAppShouldNotBeFound("nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllAppsByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        appRepository.saveAndFlush(app);

        // Get all the appList where nombre is not null
        defaultAppShouldBeFound("nombre.specified=true");

        // Get all the appList where nombre is null
        defaultAppShouldNotBeFound("nombre.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultAppShouldBeFound(String filter) throws Exception {
        restAppMockMvc.perform(get("/api/apps?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(app.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultAppShouldNotBeFound(String filter) throws Exception {
        restAppMockMvc.perform(get("/api/apps?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingApp() throws Exception {
        // Get the app
        restAppMockMvc.perform(get("/api/apps/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateApp() throws Exception {
        // Initialize the database
        appService.save(app);

        int databaseSizeBeforeUpdate = appRepository.findAll().size();

        // Update the app
        App updatedApp = appRepository.findOne(app.getId());
        // Disconnect from session so that the updates on updatedApp are not directly saved in db
        em.detach(updatedApp);
        updatedApp
            .nombre(UPDATED_NOMBRE);

        restAppMockMvc.perform(put("/api/apps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedApp)))
            .andExpect(status().isOk());

        // Validate the App in the database
        List<App> appList = appRepository.findAll();
        assertThat(appList).hasSize(databaseSizeBeforeUpdate);
        App testApp = appList.get(appList.size() - 1);
        assertThat(testApp.getNombre()).isEqualTo(UPDATED_NOMBRE);

        // Validate the App in Elasticsearch
        App appEs = appSearchRepository.findOne(testApp.getId());
        assertThat(appEs).isEqualToIgnoringGivenFields(testApp);
    }

    @Test
    @Transactional
    public void updateNonExistingApp() throws Exception {
        int databaseSizeBeforeUpdate = appRepository.findAll().size();

        // Create the App

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAppMockMvc.perform(put("/api/apps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(app)))
            .andExpect(status().isCreated());

        // Validate the App in the database
        List<App> appList = appRepository.findAll();
        assertThat(appList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteApp() throws Exception {
        // Initialize the database
        appService.save(app);

        int databaseSizeBeforeDelete = appRepository.findAll().size();

        // Get the app
        restAppMockMvc.perform(delete("/api/apps/{id}", app.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean appExistsInEs = appSearchRepository.exists(app.getId());
        assertThat(appExistsInEs).isFalse();

        // Validate the database is empty
        List<App> appList = appRepository.findAll();
        assertThat(appList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchApp() throws Exception {
        // Initialize the database
        appService.save(app);

        // Search the app
        restAppMockMvc.perform(get("/api/_search/apps?query=id:" + app.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(app.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(App.class);
        App app1 = new App();
        app1.setId(1L);
        App app2 = new App();
        app2.setId(app1.getId());
        assertThat(app1).isEqualTo(app2);
        app2.setId(2L);
        assertThat(app1).isNotEqualTo(app2);
        app1.setId(null);
        assertThat(app1).isNotEqualTo(app2);
    }
}
