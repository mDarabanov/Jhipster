package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Delegation;
import com.mycompany.myapp.repository.DelegationRepository;
import com.mycompany.myapp.service.dto.DelegationDTO;
import com.mycompany.myapp.service.mapper.DelegationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Delegation}.
 */
@Service
@Transactional
public class DelegationService {

    private final Logger log = LoggerFactory.getLogger(DelegationService.class);

    private final DelegationRepository delegationRepository;

    private final DelegationMapper delegationMapper;

    public DelegationService(DelegationRepository delegationRepository, DelegationMapper delegationMapper) {
        this.delegationRepository = delegationRepository;
        this.delegationMapper = delegationMapper;
    }

    /**
     * Save a delegation.
     *
     * @param delegationDTO the entity to save.
     * @return the persisted entity.
     */
    public DelegationDTO save(DelegationDTO delegationDTO) {
        log.debug("Request to save Delegation : {}", delegationDTO);
        Delegation delegation = delegationMapper.toEntity(delegationDTO);
        delegation = delegationRepository.save(delegation);
        return delegationMapper.toDto(delegation);
    }

    /**
     * Get all the delegations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DelegationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Delegations");
        return delegationRepository.findAll(pageable)
            .map(delegationMapper::toDto);
    }

    /**
     * Get one delegation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DelegationDTO> findOne(Long id) {
        log.debug("Request to get Delegation : {}", id);
        return delegationRepository.findById(id)
            .map(delegationMapper::toDto);
    }

    /**
     * Delete the delegation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Delegation : {}", id);
        delegationRepository.deleteById(id);
    }
}
