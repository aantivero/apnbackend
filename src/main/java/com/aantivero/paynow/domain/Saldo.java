package com.aantivero.paynow.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

import com.aantivero.paynow.domain.enumeration.Moneda;

import com.aantivero.paynow.domain.enumeration.TipoCuenta;

/**
 * A Saldo.
 */
@Entity
@Table(name = "saldo")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "saldo")
public class Saldo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "monto", precision=10, scale=2, nullable = false)
    private BigDecimal monto;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "moneda", nullable = false)
    private Moneda moneda;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoCuenta tipo;

    @NotNull
    @Column(name = "fecha_creacion", nullable = false)
    private Instant fechaCreacion;

    @NotNull
    @Column(name = "fecha_modificacion", nullable = false)
    private Instant fechaModificacion;

    @ManyToOne(optional = false)
    @NotNull
    private User usuario;

    @ManyToOne(optional = false)
    @NotNull
    private App aplicacion;

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

    public Saldo monto(BigDecimal monto) {
        this.monto = monto;
        return this;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public Moneda getMoneda() {
        return moneda;
    }

    public Saldo moneda(Moneda moneda) {
        this.moneda = moneda;
        return this;
    }

    public void setMoneda(Moneda moneda) {
        this.moneda = moneda;
    }

    public TipoCuenta getTipo() {
        return tipo;
    }

    public Saldo tipo(TipoCuenta tipo) {
        this.tipo = tipo;
        return this;
    }

    public void setTipo(TipoCuenta tipo) {
        this.tipo = tipo;
    }

    public Instant getFechaCreacion() {
        return fechaCreacion;
    }

    public Saldo fechaCreacion(Instant fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
        return this;
    }

    public void setFechaCreacion(Instant fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Instant getFechaModificacion() {
        return fechaModificacion;
    }

    public Saldo fechaModificacion(Instant fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
        return this;
    }

    public void setFechaModificacion(Instant fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public User getUsuario() {
        return usuario;
    }

    public Saldo usuario(User user) {
        this.usuario = user;
        return this;
    }

    public void setUsuario(User user) {
        this.usuario = user;
    }

    public App getAplicacion() {
        return aplicacion;
    }

    public Saldo aplicacion(App app) {
        this.aplicacion = app;
        return this;
    }

    public void setAplicacion(App app) {
        this.aplicacion = app;
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
        Saldo saldo = (Saldo) o;
        if (saldo.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), saldo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Saldo{" +
            "id=" + getId() +
            ", monto=" + getMonto() +
            ", moneda='" + getMoneda() + "'" +
            ", tipo='" + getTipo() + "'" +
            ", fechaCreacion='" + getFechaCreacion() + "'" +
            ", fechaModificacion='" + getFechaModificacion() + "'" +
            "}";
    }
}
