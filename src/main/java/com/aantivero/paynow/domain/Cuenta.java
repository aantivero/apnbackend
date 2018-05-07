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
 * A Cuenta.
 */
@Entity
@Table(name = "cuenta")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "cuenta")
public class Cuenta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(min = 2)
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "moneda", nullable = false)
    private Moneda moneda;

    @NotNull
    @Column(name = "saldo", precision=10, scale=2, nullable = false)
    private BigDecimal saldo;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoCuenta tipo;

    @Column(name = "cbu")
    private String cbu;

    @Column(name = "alias_cbu")
    private String aliasCbu;

    @Column(name = "fecha_creacion")
    private Instant fechaCreacion;

    @Column(name = "fecha_modificacion")
    private Instant fechaModificacion;

    @NotNull
    @Column(name = "habilitada", nullable = false)
    private Boolean habilitada;

    @NotNull
    @Column(name = "para_credito", nullable = false)
    private Boolean paraCredito;

    @NotNull
    @Column(name = "para_debito", nullable = false)
    private Boolean paraDebito;

    @Column(name = "maximo_credito", precision=10, scale=2)
    private BigDecimal maximoCredito;

    @Column(name = "maximo_debito", precision=10, scale=2)
    private BigDecimal maximoDebito;

    @Column(name = "codigo_seguridad")
    private String codigoSeguridad;

    @ManyToOne(optional = false)
    @NotNull
    private User usuario;

    @ManyToOne
    private Banco banco;

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

    public Cuenta nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Cuenta descripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Moneda getMoneda() {
        return moneda;
    }

    public Cuenta moneda(Moneda moneda) {
        this.moneda = moneda;
        return this;
    }

    public void setMoneda(Moneda moneda) {
        this.moneda = moneda;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public Cuenta saldo(BigDecimal saldo) {
        this.saldo = saldo;
        return this;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public TipoCuenta getTipo() {
        return tipo;
    }

    public Cuenta tipo(TipoCuenta tipo) {
        this.tipo = tipo;
        return this;
    }

    public void setTipo(TipoCuenta tipo) {
        this.tipo = tipo;
    }

    public String getCbu() {
        return cbu;
    }

    public Cuenta cbu(String cbu) {
        this.cbu = cbu;
        return this;
    }

    public void setCbu(String cbu) {
        this.cbu = cbu;
    }

    public String getAliasCbu() {
        return aliasCbu;
    }

    public Cuenta aliasCbu(String aliasCbu) {
        this.aliasCbu = aliasCbu;
        return this;
    }

    public void setAliasCbu(String aliasCbu) {
        this.aliasCbu = aliasCbu;
    }

    public Instant getFechaCreacion() {
        return fechaCreacion;
    }

    public Cuenta fechaCreacion(Instant fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
        return this;
    }

    public void setFechaCreacion(Instant fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Instant getFechaModificacion() {
        return fechaModificacion;
    }

    public Cuenta fechaModificacion(Instant fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
        return this;
    }

    public void setFechaModificacion(Instant fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public Boolean isHabilitada() {
        return habilitada;
    }

    public Cuenta habilitada(Boolean habilitada) {
        this.habilitada = habilitada;
        return this;
    }

    public void setHabilitada(Boolean habilitada) {
        this.habilitada = habilitada;
    }

    public Boolean isParaCredito() {
        return paraCredito;
    }

    public Cuenta paraCredito(Boolean paraCredito) {
        this.paraCredito = paraCredito;
        return this;
    }

    public void setParaCredito(Boolean paraCredito) {
        this.paraCredito = paraCredito;
    }

    public Boolean isParaDebito() {
        return paraDebito;
    }

    public Cuenta paraDebito(Boolean paraDebito) {
        this.paraDebito = paraDebito;
        return this;
    }

    public void setParaDebito(Boolean paraDebito) {
        this.paraDebito = paraDebito;
    }

    public BigDecimal getMaximoCredito() {
        return maximoCredito;
    }

    public Cuenta maximoCredito(BigDecimal maximoCredito) {
        this.maximoCredito = maximoCredito;
        return this;
    }

    public void setMaximoCredito(BigDecimal maximoCredito) {
        this.maximoCredito = maximoCredito;
    }

    public BigDecimal getMaximoDebito() {
        return maximoDebito;
    }

    public Cuenta maximoDebito(BigDecimal maximoDebito) {
        this.maximoDebito = maximoDebito;
        return this;
    }

    public void setMaximoDebito(BigDecimal maximoDebito) {
        this.maximoDebito = maximoDebito;
    }

    public String getCodigoSeguridad() {
        return codigoSeguridad;
    }

    public Cuenta codigoSeguridad(String codigoSeguridad) {
        this.codigoSeguridad = codigoSeguridad;
        return this;
    }

    public void setCodigoSeguridad(String codigoSeguridad) {
        this.codigoSeguridad = codigoSeguridad;
    }

    public User getUsuario() {
        return usuario;
    }

    public Cuenta usuario(User user) {
        this.usuario = user;
        return this;
    }

    public void setUsuario(User user) {
        this.usuario = user;
    }

    public Banco getBanco() {
        return banco;
    }

    public Cuenta banco(Banco banco) {
        this.banco = banco;
        return this;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
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
        Cuenta cuenta = (Cuenta) o;
        if (cuenta.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cuenta.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Cuenta{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", moneda='" + getMoneda() + "'" +
            ", saldo=" + getSaldo() +
            ", tipo='" + getTipo() + "'" +
            ", cbu='" + getCbu() + "'" +
            ", aliasCbu='" + getAliasCbu() + "'" +
            ", fechaCreacion='" + getFechaCreacion() + "'" +
            ", fechaModificacion='" + getFechaModificacion() + "'" +
            ", habilitada='" + isHabilitada() + "'" +
            ", paraCredito='" + isParaCredito() + "'" +
            ", paraDebito='" + isParaDebito() + "'" +
            ", maximoCredito=" + getMaximoCredito() +
            ", maximoDebito=" + getMaximoDebito() +
            ", codigoSeguridad='" + getCodigoSeguridad() + "'" +
            "}";
    }
}
