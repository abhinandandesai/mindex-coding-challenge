package com.mindex.challenge.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Compensation {
    private Employee employee;
    private Double salary;
    private LocalDate effectiveDate;
}