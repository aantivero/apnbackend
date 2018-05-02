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

import com.aantivero.paynow.domain.enumeration.EstadoTransferencia;

import com.aantivero.paynow.domain.enumeration.TipoTransferencia;

/**
 * A TransferenciaApp.
 */
@Entity
@Table(name = "transferencia_app")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "transferenciaapp")
public class TransferenciaApp implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "destino_cbu")
    private String destinoCbu;

    @Column(name = "destino_alias")
    private String destinoAlias;

    @Column(name = "destino_info")
    private String destinoInfo;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "moneda", nullable = false)
    private Moneda moneda;

    @NotNull
    @Column(name = "monto", precision=10, scale=2, nullable = false)
    private BigDecimal monto;

    @Column(name = "descripcion")
    private String descripcion;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_transferencia", nullable = false)
    private EstadoTransferencia estadoTransferencia;

    @NotNull
    @Column(name = "jhi_timestamp", nullable = false)
    private Instant timestamp;

    @Column(name = "descripcion_estado")
    private String descripcionEstado;

    @Column(name = "identificacion")
    private String identificacion;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_transferencia", nullable = false)
    private TipoTransferencia tipoTransferencia;

    @ManyToOne(optional = false)
    @NotNull
    private CuentaApp origen;

    @ManyToOne(optional = false)
    @NotNull
    private Banco destinoBanco;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDestinoCbu() {
        return destinoCbu;
    }

    public TransferenciaApp destinoCbu(String destinoCbu) {
        this.destinoCbu = destinoCbu;
        return this;
    }

    public void setDestinoCbu(String destinoCbu) {
        this.destinoCbu = destinoCbu;
    }

    public String getDestinoAlias() {
        return destinoAlias;
    }

    public TransferenciaApp destinoAlias(String destinoAlias) {
        this.destinoAlias = destinoAlias;
        return this;
    }

    public void setDestinoAlias(String destinoAlias) {
        this.destinoAlias = destinoAlias;
    }

    public String getDestinoInfo() {
        return destinoInfo;
    }

    public TransferenciaApp destinoInfo(String destinoInfo) {
        this.destinoInfo = destinoInfo;
        return this;
    }

    public void setDestinoInfo(String destinoInfo) {
        this.destinoInfo = destinoInfo;
    }

    public Moneda getMoneda() {
        return moneda;
    }

    public TransferenciaApp moneda(Moneda moneda) {
        this.moneda = moneda;
        return this;
    }

    public void setMoneda(Moneda moneda) {
        this.moneda = moneda;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public TransferenciaApp monto(BigDecimal monto) {
        this.monto = monto;
        return this;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public TransferenciaApp descripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public EstadoTransferencia getEstadoTransferencia() {
        return estadoTransferencia;
    }

    public TransferenciaApp estadoTransferencia(EstadoTransferencia estadoTransferencia) {
        this.estadoTransferencia = estadoTransferencia;
        return this;
    }

    public void setEstadoTransferencia(EstadoTransferencia estadoTransferencia) {
        this.estadoTransferencia = estadoTransferencia;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public TransferenciaApp timestamp(Instant timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getDescripcionEstado() {
        return descripcionEstado;
    }

    public TransferenciaApp descripcionEstado(String descripcionEstado) {
        this.descripcionEstado = descripcionEstado;
        return this;
    }

    public void setDescripcionEstado(String descripcionEstado) {
        this.descripcionEstado = descripcionEstado;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public TransferenciaApp identificacion(String identificacion) {
        this.identificacion = identificacion;
        return this;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public TipoTransferencia getTipoTransferencia() {
        return tipoTransferencia;
    }

    public TransferenciaApp tipoTransferencia(TipoTransferencia tipoTransferencia) {
        this.tipoTransferencia = tipoTransferencia;
        return this;
    }

    public void setTipoTransferencia(TipoTransferencia tipoTransferencia) {
        this.tipoTransferencia = tipoTransferencia;
    }

    public CuentaApp getOrigen() {
        return origen;
    }

    public TransferenciaApp origen(CuentaApp cuentaApp) {
        this.origen = cuentaApp;
        return this;
    }

    public void setOrigen(CuentaApp cuentaApp) {
        this.origen = cuentaApp;
    }

    public Banco getDestinoBanco() {
        return destinoBanco;
    }

    public TransferenciaApp destinoBanco(Banco banco) {
        this.destinoBanco = banco;
        return this;
    }

    public void setDestinoBanco(Banco banco) {
        this.destinoBanco = banco;
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
        TransferenciaApp transferenciaApp = (TransferenciaApp) o;
        if (transferenciaApp.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), transferenciaApp.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TransferenciaApp{" +
            "id=" + getId() +
            ", destinoCbu='" + getDestinoCbu() + "'" +
            ", destinoAlias='" + getDestinoAlias() + "'" +
            ", destinoInfo='" + getDestinoInfo() + "'" +
            ", moneda='" + getMoneda() + "'" +
            ", monto=" + getMonto() +
            ", descripcion='" + getDescripcion() + "'" +
            ", estadoTransferencia='" + getEstadoTransferencia() + "'" +
            ", timestamp='" + getTimestamp() + "'" +
            ", descripcionEstado='" + getDescripcionEstado() + "'" +
            ", identificacion='" + getIdentificacion() + "'" +
            ", tipoTransferencia='" + getTipoTransferencia() + "'" +
            "}";
    }
}
