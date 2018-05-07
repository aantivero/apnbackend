package com.aantivero.paynow.service.dto;

import java.io.Serializable;
import com.aantivero.paynow.domain.enumeration.Moneda;
import com.aantivero.paynow.domain.enumeration.TipoCuenta;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.BigDecimalFilter;
import io.github.jhipster.service.filter.InstantFilter;




/**
 * Criteria class for the Saldo entity. This class is used in SaldoResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /saldos?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SaldoCriteria implements Serializable {
    /**
     * Class for filtering Moneda
     */
    public static class MonedaFilter extends Filter<Moneda> {
    }

    /**
     * Class for filtering TipoCuenta
     */
    public static class TipoCuentaFilter extends Filter<TipoCuenta> {
    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private BigDecimalFilter monto;

    private MonedaFilter moneda;

    private TipoCuentaFilter tipo;

    private InstantFilter fechaCreacion;

    private InstantFilter fechaModificacion;

    private LongFilter usuarioId;

    private LongFilter aplicacionId;

    public SaldoCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public BigDecimalFilter getMonto() {
        return monto;
    }

    public void setMonto(BigDecimalFilter monto) {
        this.monto = monto;
    }

    public MonedaFilter getMoneda() {
        return moneda;
    }

    public void setMoneda(MonedaFilter moneda) {
        this.moneda = moneda;
    }

    public TipoCuentaFilter getTipo() {
        return tipo;
    }

    public void setTipo(TipoCuentaFilter tipo) {
        this.tipo = tipo;
    }

    public InstantFilter getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(InstantFilter fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public InstantFilter getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(InstantFilter fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public LongFilter getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(LongFilter usuarioId) {
        this.usuarioId = usuarioId;
    }

    public LongFilter getAplicacionId() {
        return aplicacionId;
    }

    public void setAplicacionId(LongFilter aplicacionId) {
        this.aplicacionId = aplicacionId;
    }

    @Override
    public String toString() {
        return "SaldoCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (monto != null ? "monto=" + monto + ", " : "") +
                (moneda != null ? "moneda=" + moneda + ", " : "") +
                (tipo != null ? "tipo=" + tipo + ", " : "") +
                (fechaCreacion != null ? "fechaCreacion=" + fechaCreacion + ", " : "") +
                (fechaModificacion != null ? "fechaModificacion=" + fechaModificacion + ", " : "") +
                (usuarioId != null ? "usuarioId=" + usuarioId + ", " : "") +
                (aplicacionId != null ? "aplicacionId=" + aplicacionId + ", " : "") +
            "}";
    }

}
