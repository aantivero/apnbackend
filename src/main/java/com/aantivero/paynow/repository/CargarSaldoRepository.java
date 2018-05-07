package com.aantivero.paynow.repository;

import com.aantivero.paynow.domain.CargarSaldo;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the CargarSaldo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CargarSaldoRepository extends JpaRepository<CargarSaldo, Long>, JpaSpecificationExecutor<CargarSaldo> {

    @Query("select cargar_saldo from CargarSaldo cargar_saldo where cargar_saldo.usuario.login = ?#{principal.username}")
    List<CargarSaldo> findByUsuarioIsCurrentUser();

}
