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
 * A SaldoApp.
 */
@Entity
@Table(name = "saldo_app")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "saldoapp")
public class SaldoApp implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "moneda", nullable = false)
    private Moneda moneda;

    @NotNull
    @Column(name = "monto", precision=10, scale=2, nullable = false)
    private BigDecimal monto;

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

    public Moneda getMoneda() {
        return moneda;
    }

    public SaldoApp moneda(Moneda moneda) {
        this.moneda = moneda;
        return this;
    }

    public void setMoneda(Moneda moneda) {
        this.moneda = moneda;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public SaldoApp monto(BigDecimal monto) {
        this.monto = monto;
        return this;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public App getApp() {
        return app;
    }

    public SaldoApp app(App app) {
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
        SaldoApp saldoApp = (SaldoApp) o;
        if (saldoApp.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), saldoApp.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SaldoApp{" +
            "id=" + getId() +
            ", moneda='" + getMoneda() + "'" +
            ", monto=" + getMonto() +
            "}";
    }
}
