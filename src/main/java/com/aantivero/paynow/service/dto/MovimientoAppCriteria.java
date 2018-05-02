package com.aantivero.paynow.service.dto;

import java.io.Serializable;
import com.aantivero.paynow.domain.enumeration.Moneda;
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
 * Criteria class for the MovimientoApp entity. This class is used in MovimientoAppResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /movimiento-apps?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MovimientoAppCriteria implements Serializable {
    /**
     * Class for filtering Moneda
     */
    public static class MonedaFilter extends Filter<Moneda> {
    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter cuentaDebitoCbu;

    private StringFilter cuentaDebitoAlias;

    private BooleanFilter cuentaDebitoPropia;

    private StringFilter cuentaCreditoCbu;

    private StringFilter cuentaCreditoAlias;

    private BooleanFilter cuentaCreditoPropia;

    private MonedaFilter moneda;

    private BigDecimalFilter monto;

    private InstantFilter timestamp;

    private StringFilter descripcion;

    private StringFilter identificacion;

    private BooleanFilter consolidado;

    private InstantFilter consolidadoTimestamp;

    private LongFilter bancoDebitoId;

    private LongFilter bancoCreditoId;

    public MovimientoAppCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getCuentaDebitoCbu() {
        return cuentaDebitoCbu;
    }

    public void setCuentaDebitoCbu(StringFilter cuentaDebitoCbu) {
        this.cuentaDebitoCbu = cuentaDebitoCbu;
    }

    public StringFilter getCuentaDebitoAlias() {
        return cuentaDebitoAlias;
    }

    public void setCuentaDebitoAlias(StringFilter cuentaDebitoAlias) {
        this.cuentaDebitoAlias = cuentaDebitoAlias;
    }

    public BooleanFilter getCuentaDebitoPropia() {
        return cuentaDebitoPropia;
    }

    public void setCuentaDebitoPropia(BooleanFilter cuentaDebitoPropia) {
        this.cuentaDebitoPropia = cuentaDebitoPropia;
    }

    public StringFilter getCuentaCreditoCbu() {
        return cuentaCreditoCbu;
    }

    public void setCuentaCreditoCbu(StringFilter cuentaCreditoCbu) {
        this.cuentaCreditoCbu = cuentaCreditoCbu;
    }

    public StringFilter getCuentaCreditoAlias() {
        return cuentaCreditoAlias;
    }

    public void setCuentaCreditoAlias(StringFilter cuentaCreditoAlias) {
        this.cuentaCreditoAlias = cuentaCreditoAlias;
    }

    public BooleanFilter getCuentaCreditoPropia() {
        return cuentaCreditoPropia;
    }

    public void setCuentaCreditoPropia(BooleanFilter cuentaCreditoPropia) {
        this.cuentaCreditoPropia = cuentaCreditoPropia;
    }

    public MonedaFilter getMoneda() {
        return moneda;
    }

    public void setMoneda(MonedaFilter moneda) {
        this.moneda = moneda;
    }

    public BigDecimalFilter getMonto() {
        return monto;
    }

    public void setMonto(BigDecimalFilter monto) {
        this.monto = monto;
    }

    public InstantFilter getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(InstantFilter timestamp) {
        this.timestamp = timestamp;
    }

    public StringFilter getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(StringFilter descripcion) {
        this.descripcion = descripcion;
    }

    public StringFilter getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(StringFilter identificacion) {
        this.identificacion = identificacion;
    }

    public BooleanFilter getConsolidado() {
        return consolidado;
    }

    public void setConsolidado(BooleanFilter consolidado) {
        this.consolidado = consolidado;
    }

    public InstantFilter getConsolidadoTimestamp() {
        return consolidadoTimestamp;
    }

    public void setConsolidadoTimestamp(InstantFilter consolidadoTimestamp) {
        this.consolidadoTimestamp = consolidadoTimestamp;
    }

    public LongFilter getBancoDebitoId() {
        return bancoDebitoId;
    }

    public void setBancoDebitoId(LongFilter bancoDebitoId) {
        this.bancoDebitoId = bancoDebitoId;
    }

    public LongFilter getBancoCreditoId() {
        return bancoCreditoId;
    }

    public void setBancoCreditoId(LongFilter bancoCreditoId) {
        this.bancoCreditoId = bancoCreditoId;
    }

    @Override
    public String toString() {
        return "MovimientoAppCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (cuentaDebitoCbu != null ? "cuentaDebitoCbu=" + cuentaDebitoCbu + ", " : "") +
                (cuentaDebitoAlias != null ? "cuentaDebitoAlias=" + cuentaDebitoAlias + ", " : "") +
                (cuentaDebitoPropia != null ? "cuentaDebitoPropia=" + cuentaDebitoPropia + ", " : "") +
                (cuentaCreditoCbu != null ? "cuentaCreditoCbu=" + cuentaCreditoCbu + ", " : "") +
                (cuentaCreditoAlias != null ? "cuentaCreditoAlias=" + cuentaCreditoAlias + ", " : "") +
                (cuentaCreditoPropia != null ? "cuentaCreditoPropia=" + cuentaCreditoPropia + ", " : "") +
                (moneda != null ? "moneda=" + moneda + ", " : "") +
                (monto != null ? "monto=" + monto + ", " : "") +
                (timestamp != null ? "timestamp=" + timestamp + ", " : "") +
                (descripcion != null ? "descripcion=" + descripcion + ", " : "") +
                (identificacion != null ? "identificacion=" + identificacion + ", " : "") +
                (consolidado != null ? "consolidado=" + consolidado + ", " : "") +
                (consolidadoTimestamp != null ? "consolidadoTimestamp=" + consolidadoTimestamp + ", " : "") +
                (bancoDebitoId != null ? "bancoDebitoId=" + bancoDebitoId + ", " : "") +
                (bancoCreditoId != null ? "bancoCreditoId=" + bancoCreditoId + ", " : "") +
            "}";
    }

}
