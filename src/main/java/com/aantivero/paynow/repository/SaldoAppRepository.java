package com.aantivero.paynow.repository;

import com.aantivero.paynow.domain.App;
import com.aantivero.paynow.domain.SaldoApp;
import com.aantivero.paynow.domain.enumeration.Moneda;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the SaldoApp entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SaldoAppRepository extends JpaRepository<SaldoApp, Long> {

    SaldoApp findByAppAndMoneda(App app, Moneda moneda);
}
