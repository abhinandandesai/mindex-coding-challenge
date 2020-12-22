/*
 * ### Task 2
 * Compensation is created such that the employeeID is mapped with employee and is used to search
 */
package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CompensationServiceImpl implements CompensationService {
    private static final Logger LOG = LoggerFactory.getLogger(CompensationServiceImpl.class);
    private CompensationRepository compensationRepository;

    @Autowired
    public CompensationServiceImpl(final CompensationRepository compensationRepository) {
        this.compensationRepository = compensationRepository;
    }

    @Override
    public List<Compensation> create(List<Compensation> compensations) {
        LOG.debug("Creating compensation [{}]", compensations);
        compensationRepository.insert(compensations);
        return compensations;
    }

    @Override
    public Compensation read(String id) {
       LOG.debug("Searching Employee using Employee ID [{}]", id);
        Compensation compensation = compensationRepository.employeeSearch_ID(id);
        if (compensation == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee ID not found");
        }
        return compensation;
    }
}