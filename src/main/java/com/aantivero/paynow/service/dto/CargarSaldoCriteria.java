package com.aantivero.paynow.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.BigDecimalFilter;





/**
 * Criteria class for the CargarSaldo entity. This class is used in CargarSaldoResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /cargar-saldos?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CargarSaldoCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private BigDecimalFilter monto;

    private LongFilter usuarioId;

    private LongFilter cuentaId;

    private LongFilter transferenciaAppId;

    public CargarSaldoCriteria() {
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

    public LongFilter getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(LongFilter usuarioId) {
        this.usuarioId = usuarioId;
    }

    public LongFilter getCuentaId() {
        return cuentaId;
    }

    public void setCuentaId(LongFilter cuentaId) {
        this.cuentaId = cuentaId;
    }

    public LongFilter getTransferenciaAppId() {
        return transferenciaAppId;
    }

    public void setTransferenciaAppId(LongFilter transferenciaAppId) {
        this.transferenciaAppId = transferenciaAppId;
    }

    @Override
    public String toString() {
        return "CargarSaldoCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (monto != null ? "monto=" + monto + ", " : "") +
                (usuarioId != null ? "usuarioId=" + usuarioId + ", " : "") +
                (cuentaId != null ? "cuentaId=" + cuentaId + ", " : "") +
                (transferenciaAppId != null ? "transferenciaAppId=" + transferenciaAppId + ", " : "") +
            "}";
    }

}
