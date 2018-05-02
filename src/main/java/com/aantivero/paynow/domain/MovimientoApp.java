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

/**
 * A MovimientoApp.
 */
@Entity
@Table(name = "movimiento_app")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "movimientoapp")
public class MovimientoApp implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "cuenta_debito_cbu")
    private String cuentaDebitoCbu;

    @Column(name = "cuenta_debito_alias")
    private String cuentaDebitoAlias;

    @NotNull
    @Column(name = "cuenta_debito_propia", nullable = false)
    private Boolean cuentaDebitoPropia;

    @Column(name = "cuenta_credito_cbu")
    private String cuentaCreditoCbu;

    @Column(name = "cuenta_credito_alias")
    private String cuentaCreditoAlias;

    @NotNull
    @Column(name = "cuenta_credito_propia", nullable = false)
    private Boolean cuentaCreditoPropia;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "moneda", nullable = false)
    private Moneda moneda;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "monto", precision=10, scale=2, nullable = false)
    private BigDecimal monto;

    @NotNull
    @Column(name = "jhi_timestamp", nullable = false)
    private Instant timestamp;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "identificacion")
    private String identificacion;

    @NotNull
    @Column(name = "consolidado", nullable = false)
    private Boolean consolidado;

    @Column(name = "consolidado_timestamp")
    private Instant consolidadoTimestamp;

    @ManyToOne(optional = false)
    @NotNull
    private Banco bancoDebito;

    @ManyToOne(optional = false)
    @NotNull
    private Banco bancoCredito;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCuentaDebitoCbu() {
        return cuentaDebitoCbu;
    }

    public MovimientoApp cuentaDebitoCbu(String cuentaDebitoCbu) {
        this.cuentaDebitoCbu = cuentaDebitoCbu;
        return this;
    }

    public void setCuentaDebitoCbu(String cuentaDebitoCbu) {
        this.cuentaDebitoCbu = cuentaDebitoCbu;
    }

    public String getCuentaDebitoAlias() {
        return cuentaDebitoAlias;
    }

    public MovimientoApp cuentaDebitoAlias(String cuentaDebitoAlias) {
        this.cuentaDebitoAlias = cuentaDebitoAlias;
        return this;
    }

    public void setCuentaDebitoAlias(String cuentaDebitoAlias) {
        this.cuentaDebitoAlias = cuentaDebitoAlias;
    }

    public Boolean isCuentaDebitoPropia() {
        return cuentaDebitoPropia;
    }

    public MovimientoApp cuentaDebitoPropia(Boolean cuentaDebitoPropia) {
        this.cuentaDebitoPropia = cuentaDebitoPropia;
        return this;
    }

    public void setCuentaDebitoPropia(Boolean cuentaDebitoPropia) {
        this.cuentaDebitoPropia = cuentaDebitoPropia;
    }

    public String getCuentaCreditoCbu() {
        return cuentaCreditoCbu;
    }

    public MovimientoApp cuentaCreditoCbu(String cuentaCreditoCbu) {
        this.cuentaCreditoCbu = cuentaCreditoCbu;
        return this;
    }

    public void setCuentaCreditoCbu(String cuentaCreditoCbu) {
        this.cuentaCreditoCbu = cuentaCreditoCbu;
    }

    public String getCuentaCreditoAlias() {
        return cuentaCreditoAlias;
    }

    public MovimientoApp cuentaCreditoAlias(String cuentaCreditoAlias) {
        this.cuentaCreditoAlias = cuentaCreditoAlias;
        return this;
    }

    public void setCuentaCreditoAlias(String cuentaCreditoAlias) {
        this.cuentaCreditoAlias = cuentaCreditoAlias;
    }

    public Boolean isCuentaCreditoPropia() {
        return cuentaCreditoPropia;
    }

    public MovimientoApp cuentaCreditoPropia(Boolean cuentaCreditoPropia) {
        this.cuentaCreditoPropia = cuentaCreditoPropia;
        return this;
    }

    public void setCuentaCreditoPropia(Boolean cuentaCreditoPropia) {
        this.cuentaCreditoPropia = cuentaCreditoPropia;
    }

    public Moneda getMoneda() {
        return moneda;
    }

    public MovimientoApp moneda(Moneda moneda) {
        this.moneda = moneda;
        return this;
    }

    public void setMoneda(Moneda moneda) {
        this.moneda = moneda;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public MovimientoApp monto(BigDecimal monto) {
        this.monto = monto;
        return this;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public MovimientoApp timestamp(Instant timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public MovimientoApp descripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public MovimientoApp identificacion(String identificacion) {
        this.identificacion = identificacion;
        return this;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public Boolean isConsolidado() {
        return consolidado;
    }

    public MovimientoApp consolidado(Boolean consolidado) {
        this.consolidado = consolidado;
        return this;
    }

    public void setConsolidado(Boolean consolidado) {
        this.consolidado = consolidado;
    }

    public Instant getConsolidadoTimestamp() {
        return consolidadoTimestamp;
    }

    public MovimientoApp consolidadoTimestamp(Instant consolidadoTimestamp) {
        this.consolidadoTimestamp = consolidadoTimestamp;
        return this;
    }

    public void setConsolidadoTimestamp(Instant consolidadoTimestamp) {
        this.consolidadoTimestamp = consolidadoTimestamp;
    }

    public Banco getBancoDebito() {
        return bancoDebito;
    }

    public MovimientoApp bancoDebito(Banco banco) {
        this.bancoDebito = banco;
        return this;
    }

    public void setBancoDebito(Banco banco) {
        this.bancoDebito = banco;
    }

    public Banco getBancoCredito() {
        return bancoCredito;
    }

    public MovimientoApp bancoCredito(Banco banco) {
        this.bancoCredito = banco;
        return this;
    }

    public void setBancoCredito(Banco banco) {
        this.bancoCredito = banco;
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
        MovimientoApp movimientoApp = (MovimientoApp) o;
        if (movimientoApp.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), movimientoApp.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MovimientoApp{" +
            "id=" + getId() +
            ", cuentaDebitoCbu='" + getCuentaDebitoCbu() + "'" +
            ", cuentaDebitoAlias='" + getCuentaDebitoAlias() + "'" +
            ", cuentaDebitoPropia='" + isCuentaDebitoPropia() + "'" +
            ", cuentaCreditoCbu='" + getCuentaCreditoCbu() + "'" +
            ", cuentaCreditoAlias='" + getCuentaCreditoAlias() + "'" +
            ", cuentaCreditoPropia='" + isCuentaCreditoPropia() + "'" +
            ", moneda='" + getMoneda() + "'" +
            ", monto=" + getMonto() +
            ", timestamp='" + getTimestamp() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", identificacion='" + getIdentificacion() + "'" +
            ", consolidado='" + isConsolidado() + "'" +
            ", consolidadoTimestamp='" + getConsolidadoTimestamp() + "'" +
            "}";
    }
}
