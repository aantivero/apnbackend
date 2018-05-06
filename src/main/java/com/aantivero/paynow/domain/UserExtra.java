package com.aantivero.paynow.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

import com.aantivero.paynow.domain.enumeration.TipoDocumento;

import com.aantivero.paynow.domain.enumeration.TipoContribuyente;

/**
 * A UserExtra.
 */
@Entity
@Table(name = "user_extra")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "userextra")
public class UserExtra implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "vendedor")
    private Boolean vendedor;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_documento")
    private TipoDocumento tipoDocumento;

    @Column(name = "numero_documento")
    private String numeroDocumento;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_contribuyente")
    private TipoContribuyente tipoContribuyente;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private User usuario;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTelefono() {
        return telefono;
    }

    public UserExtra telefono(String telefono) {
        this.telefono = telefono;
        return this;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Boolean isVendedor() {
        return vendedor;
    }

    public UserExtra vendedor(Boolean vendedor) {
        this.vendedor = vendedor;
        return this;
    }

    public void setVendedor(Boolean vendedor) {
        this.vendedor = vendedor;
    }

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public UserExtra tipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
        return this;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public UserExtra numeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
        return this;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public TipoContribuyente getTipoContribuyente() {
        return tipoContribuyente;
    }

    public UserExtra tipoContribuyente(TipoContribuyente tipoContribuyente) {
        this.tipoContribuyente = tipoContribuyente;
        return this;
    }

    public void setTipoContribuyente(TipoContribuyente tipoContribuyente) {
        this.tipoContribuyente = tipoContribuyente;
    }

    public User getUsuario() {
        return usuario;
    }

    public UserExtra usuario(User user) {
        this.usuario = user;
        return this;
    }

    public void setUsuario(User user) {
        this.usuario = user;
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
        UserExtra userExtra = (UserExtra) o;
        if (userExtra.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), userExtra.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UserExtra{" +
            "id=" + getId() +
            ", telefono='" + getTelefono() + "'" +
            ", vendedor='" + isVendedor() + "'" +
            ", tipoDocumento='" + getTipoDocumento() + "'" +
            ", numeroDocumento='" + getNumeroDocumento() + "'" +
            ", tipoContribuyente='" + getTipoContribuyente() + "'" +
            "}";
    }
}
