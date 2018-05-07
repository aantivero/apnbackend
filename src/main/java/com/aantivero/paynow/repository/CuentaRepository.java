package com.aantivero.paynow.repository;

import com.aantivero.paynow.domain.Cuenta;
import com.aantivero.paynow.domain.User;
import com.aantivero.paynow.domain.enumeration.Moneda;
import com.aantivero.paynow.domain.enumeration.TipoCuenta;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Cuenta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CuentaRepository extends JpaRepository<Cuenta, Long>, JpaSpecificationExecutor<Cuenta> {

    @Query("select cuenta from Cuenta cuenta where cuenta.usuario.login = ?#{principal.username}")
    List<Cuenta> findByUsuarioIsCurrentUser();

    Optional<Cuenta> findOneCuentaByUsuarioAndMonedaAndTipoAndHabilitada(User user, Moneda moneda, TipoCuenta tipo, boolean habilitada);
}
