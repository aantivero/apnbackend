package com.aantivero.paynow.service;

import com.aantivero.paynow.domain.UserExtra;
import com.aantivero.paynow.repository.UserExtraRepository;
import com.aantivero.paynow.repository.search.UserExtraSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing UserExtra.
 */
@Service
@Transactional
public class UserExtraService {

    private final Logger log = LoggerFactory.getLogger(UserExtraService.class);

    private final UserExtraRepository userExtraRepository;

    private final UserExtraSearchRepository userExtraSearchRepository;

    public UserExtraService(UserExtraRepository userExtraRepository, UserExtraSearchRepository userExtraSearchRepository) {
        this.userExtraRepository = userExtraRepository;
        this.userExtraSearchRepository = userExtraSearchRepository;
    }

    /**
     * Save a userExtra.
     *
     * @param userExtra the entity to save
     * @return the persisted entity
     */
    public UserExtra save(UserExtra userExtra) {
        log.debug("Request to save UserExtra : {}", userExtra);
        UserExtra result = userExtraRepository.save(userExtra);
        userExtraSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the userExtras.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<UserExtra> findAll(Pageable pageable) {
        log.debug("Request to get all UserExtras");
        return userExtraRepository.findAll(pageable);
    }

    /**
     * Get one userExtra by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public UserExtra findOne(Long id) {
        log.debug("Request to get UserExtra : {}", id);
        return userExtraRepository.findOne(id);
    }

    /**
     * Delete the userExtra by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete UserExtra : {}", id);
        userExtraRepository.delete(id);
        userExtraSearchRepository.delete(id);
    }

    /**
     * Search for the userExtra corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<UserExtra> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of UserExtras for query {}", query);
        Page<UserExtra> result = userExtraSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
