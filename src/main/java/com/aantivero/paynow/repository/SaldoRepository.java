package com.aantivero.paynow.repository;

import com.aantivero.paynow.domain.Saldo;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Saldo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SaldoRepository extends JpaRepository<Saldo, Long>, JpaSpecificationExecutor<Saldo> {

    @Query("select saldo from Saldo saldo where saldo.usuario.login = ?#{principal.username}")
    List<Saldo> findByUsuarioIsCurrentUser();

}
