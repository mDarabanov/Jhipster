package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.CustomercareApp;
import com.mycompany.myapp.domain.Delegation;
import com.mycompany.myapp.domain.Location;
import com.mycompany.myapp.repository.DelegationRepository;
import com.mycompany.myapp.service.DelegationService;
import com.mycompany.myapp.service.dto.DelegationDTO;
import com.mycompany.myapp.service.mapper.DelegationMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link DelegationResource} REST controller.
 */
@SpringBootTest(classes = CustomercareApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class DelegationResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final byte[] DEFAULT_AGENDA = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_AGENDA = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_AGENDA_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_AGENDA_CONTENT_TYPE = "image/png";

    @Autowired
    private DelegationRepository delegationRepository;

    @Autowired
    private DelegationMapper delegationMapper;

    @Autowired
    private DelegationService delegationService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDelegationMockMvc;

    private Delegation delegation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Delegation createEntity(EntityManager em) {
        Delegation delegation = new Delegation()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .agenda(DEFAULT_AGENDA)
            .agendaContentType(DEFAULT_AGENDA_CONTENT_TYPE);
        // Add required entity
        Location location;
        if (TestUtil.findAll(em, Location.class).isEmpty()) {
            location = LocationResourceIT.createEntity(em);
            em.persist(location);
            em.flush();
        } else {
            location = TestUtil.findAll(em, Location.class).get(0);
        }
        delegation.setLocation(location);
        return delegation;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Delegation createUpdatedEntity(EntityManager em) {
        Delegation delegation = new Delegation()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .agenda(UPDATED_AGENDA)
            .agendaContentType(UPDATED_AGENDA_CONTENT_TYPE);
        // Add required entity
        Location location;
        if (TestUtil.findAll(em, Location.class).isEmpty()) {
            location = LocationResourceIT.createUpdatedEntity(em);
            em.persist(location);
            em.flush();
        } else {
            location = TestUtil.findAll(em, Location.class).get(0);
        }
        delegation.setLocation(location);
        return delegation;
    }

    @BeforeEach
    public void initTest() {
        delegation = createEntity(em);
    }

    @Test
    @Transactional
    public void createDelegation() throws Exception {
        int databaseSizeBeforeCreate = delegationRepository.findAll().size();

        // Create the Delegation
        DelegationDTO delegationDTO = delegationMapper.toDto(delegation);
        restDelegationMockMvc.perform(post("/api/delegations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(delegationDTO)))
            .andExpect(status().isCreated());

        // Validate the Delegation in the database
        List<Delegation> delegationList = delegationRepository.findAll();
        assertThat(delegationList).hasSize(databaseSizeBeforeCreate + 1);
        Delegation testDelegation = delegationList.get(delegationList.size() - 1);
        assertThat(testDelegation.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDelegation.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDelegation.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testDelegation.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testDelegation.getAgenda()).isEqualTo(DEFAULT_AGENDA);
        assertThat(testDelegation.getAgendaContentType()).isEqualTo(DEFAULT_AGENDA_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createDelegationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = delegationRepository.findAll().size();

        // Create the Delegation with an existing ID
        delegation.setId(1L);
        DelegationDTO delegationDTO = delegationMapper.toDto(delegation);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDelegationMockMvc.perform(post("/api/delegations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(delegationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Delegation in the database
        List<Delegation> delegationList = delegationRepository.findAll();
        assertThat(delegationList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = delegationRepository.findAll().size();
        // set the field null
        delegation.setName(null);

        // Create the Delegation, which fails.
        DelegationDTO delegationDTO = delegationMapper.toDto(delegation);

        restDelegationMockMvc.perform(post("/api/delegations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(delegationDTO)))
            .andExpect(status().isBadRequest());

        List<Delegation> delegationList = delegationRepository.findAll();
        assertThat(delegationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = delegationRepository.findAll().size();
        // set the field null
        delegation.setDescription(null);

        // Create the Delegation, which fails.
        DelegationDTO delegationDTO = delegationMapper.toDto(delegation);

        restDelegationMockMvc.perform(post("/api/delegations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(delegationDTO)))
            .andExpect(status().isBadRequest());

        List<Delegation> delegationList = delegationRepository.findAll();
        assertThat(delegationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = delegationRepository.findAll().size();
        // set the field null
        delegation.setStartDate(null);

        // Create the Delegation, which fails.
        DelegationDTO delegationDTO = delegationMapper.toDto(delegation);

        restDelegationMockMvc.perform(post("/api/delegations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(delegationDTO)))
            .andExpect(status().isBadRequest());

        List<Delegation> delegationList = delegationRepository.findAll();
        assertThat(delegationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = delegationRepository.findAll().size();
        // set the field null
        delegation.setEndDate(null);

        // Create the Delegation, which fails.
        DelegationDTO delegationDTO = delegationMapper.toDto(delegation);

        restDelegationMockMvc.perform(post("/api/delegations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(delegationDTO)))
            .andExpect(status().isBadRequest());

        List<Delegation> delegationList = delegationRepository.findAll();
        assertThat(delegationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDelegations() throws Exception {
        // Initialize the database
        delegationRepository.saveAndFlush(delegation);

        // Get all the delegationList
        restDelegationMockMvc.perform(get("/api/delegations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(delegation.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].agendaContentType").value(hasItem(DEFAULT_AGENDA_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].agenda").value(hasItem(Base64Utils.encodeToString(DEFAULT_AGENDA))));
    }
    
    @Test
    @Transactional
    public void getDelegation() throws Exception {
        // Initialize the database
        delegationRepository.saveAndFlush(delegation);

        // Get the delegation
        restDelegationMockMvc.perform(get("/api/delegations/{id}", delegation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(delegation.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.agendaContentType").value(DEFAULT_AGENDA_CONTENT_TYPE))
            .andExpect(jsonPath("$.agenda").value(Base64Utils.encodeToString(DEFAULT_AGENDA)));
    }

    @Test
    @Transactional
    public void getNonExistingDelegation() throws Exception {
        // Get the delegation
        restDelegationMockMvc.perform(get("/api/delegations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDelegation() throws Exception {
        // Initialize the database
        delegationRepository.saveAndFlush(delegation);

        int databaseSizeBeforeUpdate = delegationRepository.findAll().size();

        // Update the delegation
        Delegation updatedDelegation = delegationRepository.findById(delegation.getId()).get();
        // Disconnect from session so that the updates on updatedDelegation are not directly saved in db
        em.detach(updatedDelegation);
        updatedDelegation
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .agenda(UPDATED_AGENDA)
            .agendaContentType(UPDATED_AGENDA_CONTENT_TYPE);
        DelegationDTO delegationDTO = delegationMapper.toDto(updatedDelegation);

        restDelegationMockMvc.perform(put("/api/delegations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(delegationDTO)))
            .andExpect(status().isOk());

        // Validate the Delegation in the database
        List<Delegation> delegationList = delegationRepository.findAll();
        assertThat(delegationList).hasSize(databaseSizeBeforeUpdate);
        Delegation testDelegation = delegationList.get(delegationList.size() - 1);
        assertThat(testDelegation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDelegation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDelegation.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testDelegation.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testDelegation.getAgenda()).isEqualTo(UPDATED_AGENDA);
        assertThat(testDelegation.getAgendaContentType()).isEqualTo(UPDATED_AGENDA_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingDelegation() throws Exception {
        int databaseSizeBeforeUpdate = delegationRepository.findAll().size();

        // Create the Delegation
        DelegationDTO delegationDTO = delegationMapper.toDto(delegation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDelegationMockMvc.perform(put("/api/delegations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(delegationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Delegation in the database
        List<Delegation> delegationList = delegationRepository.findAll();
        assertThat(delegationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDelegation() throws Exception {
        // Initialize the database
        delegationRepository.saveAndFlush(delegation);

        int databaseSizeBeforeDelete = delegationRepository.findAll().size();

        // Delete the delegation
        restDelegationMockMvc.perform(delete("/api/delegations/{id}", delegation.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Delegation> delegationList = delegationRepository.findAll();
        assertThat(delegationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
