package com.mindex.challenge.controller;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class CompensationController {
    private static final Logger LOG = LoggerFactory.getLogger(CompensationController.class);
    private CompensationService compensationService;

    @Autowired
    public CompensationController(final CompensationService compensationService) {
        this.compensationService = compensationService;
    }


    @PostMapping("/compensation")
    public ResponseEntity<List<Compensation>> create(@RequestBody final List<Compensation> compensations) {
        LOG.debug("Received compensation create request for [{}]", compensations);

        return new ResponseEntity<>(compensationService.create(compensations), HttpStatus.OK);
    }


    @GetMapping("/compensation/{id}")
    public ResponseEntity<Compensation> read(@PathVariable final String id) {
        LOG.debug("Received compensation read request for id [{}]", id);
        Compensation compensation = compensationService.read(id);
        if (compensation == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee ID not found");
        }

        return new ResponseEntity<>(compensation, HttpStatus.OK);
    }

}