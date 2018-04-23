package com.aantivero.paynow.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import com.aantivero.paynow.domain.enumeration.Moneda;

/**
 * A CuentaApp.
 */
@Entity
@Table(name = "cuenta_app")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "cuentaapp")
public class CuentaApp implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(min = 2)
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @NotNull
    @Column(name = "alias_cbu", nullable = false)
    private String aliasCbu;

    @NotNull
    @Column(name = "cbu", nullable = false)
    private String cbu;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "moneda", nullable = false)
    private Moneda moneda;

    @NotNull
    @Column(name = "saldo", precision=10, scale=2, nullable = false)
    private BigDecimal saldo;

    @ManyToOne(optional = false)
    @NotNull
    private Banco banco;

    @ManyToOne(optional = false)
    @NotNull
    private App app;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public CuentaApp nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAliasCbu() {
        return aliasCbu;
    }

    public CuentaApp aliasCbu(String aliasCbu) {
        this.aliasCbu = aliasCbu;
        return this;
    }

    public void setAliasCbu(String aliasCbu) {
        this.aliasCbu = aliasCbu;
    }

    public String getCbu() {
        return cbu;
    }

    public CuentaApp cbu(String cbu) {
        this.cbu = cbu;
        return this;
    }

    public void setCbu(String cbu) {
        this.cbu = cbu;
    }

    public Moneda getMoneda() {
        return moneda;
    }

    public CuentaApp moneda(Moneda moneda) {
        this.moneda = moneda;
        return this;
    }

    public void setMoneda(Moneda moneda) {
        this.moneda = moneda;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public CuentaApp saldo(BigDecimal saldo) {
        this.saldo = saldo;
        return this;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public Banco getBanco() {
        return banco;
    }

    public CuentaApp banco(Banco banco) {
        this.banco = banco;
        return this;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }

    public App getApp() {
        return app;
    }

    public CuentaApp app(App app) {
        this.app = app;
        return this;
    }

    public void setApp(App app) {
        this.app = app;
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
        CuentaApp cuentaApp = (CuentaApp) o;
        if (cuentaApp.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cuentaApp.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CuentaApp{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", aliasCbu='" + getAliasCbu() + "'" +
            ", cbu='" + getCbu() + "'" +
            ", moneda='" + getMoneda() + "'" +
            ", saldo=" + getSaldo() +
            "}";
    }
}
