package com.aantivero.paynow.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A CargarSaldo.
 */
@Entity
@Table(name = "cargar_saldo")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "cargarsaldo")
public class CargarSaldo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "monto", precision=10, scale=2, nullable = false)
    private BigDecimal monto;

    @ManyToOne(optional = false)
    @NotNull
    private User usuario;

    @ManyToOne(optional = false)
    @NotNull
    private Cuenta cuenta;

    @OneToOne
    @JoinColumn(unique = true)
    private TransferenciaApp transferenciaApp;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public CargarSaldo monto(BigDecimal monto) {
        this.monto = monto;
        return this;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public User getUsuario() {
        return usuario;
    }

    public CargarSaldo usuario(User user) {
        this.usuario = user;
        return this;
    }

    public void setUsuario(User user) {
        this.usuario = user;
    }

    public Cuenta getCuenta() {
        return cuenta;
    }

    public CargarSaldo cuenta(Cuenta cuenta) {
        this.cuenta = cuenta;
        return this;
    }

    public void setCuenta(Cuenta cuenta) {
        this.cuenta = cuenta;
    }

    public TransferenciaApp getTransferenciaApp() {
        return transferenciaApp;
    }

    public CargarSaldo transferenciaApp(TransferenciaApp transferenciaApp) {
        this.transferenciaApp = transferenciaApp;
        return this;
    }

    public void setTransferenciaApp(TransferenciaApp transferenciaApp) {
        this.transferenciaApp = transferenciaApp;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CargarSaldo cargarSaldo = (CargarSaldo) o;
        if (cargarSaldo.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cargarSaldo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CargarSaldo{" +
            "id=" + getId() +
            ", monto=" + getMonto() +
            "}";
    }
}
