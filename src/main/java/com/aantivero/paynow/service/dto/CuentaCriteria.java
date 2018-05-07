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
 * Criteria class for the Cuenta entity. This class is used in CuentaResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /cuentas?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CuentaCriteria implements Serializable {
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

    private StringFilter nombre;

    private StringFilter descripcion;

    private MonedaFilter moneda;

    private BigDecimalFilter saldo;

    private TipoCuentaFilter tipo;

    private StringFilter cbu;

    private StringFilter aliasCbu;

    private InstantFilter fechaCreacion;

    private InstantFilter fechaModificacion;

    private BooleanFilter habilitada;

    private BooleanFilter paraCredito;

    private BooleanFilter paraDebito;

    private BigDecimalFilter maximoCredito;

    private BigDecimalFilter maximoDebito;

    private StringFilter codigoSeguridad;

    private LongFilter usuarioId;

    private LongFilter bancoId;

    public CuentaCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNombre() {
        return nombre;
    }

    public void setNombre(StringFilter nombre) {
        this.nombre = nombre;
    }

    public StringFilter getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(StringFilter descripcion) {
        this.descripcion = descripcion;
    }

    public MonedaFilter getMoneda() {
        return moneda;
    }

    public void setMoneda(MonedaFilter moneda) {
        this.moneda = moneda;
    }

    public BigDecimalFilter getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimalFilter saldo) {
        this.saldo = saldo;
    }

    public TipoCuentaFilter getTipo() {
        return tipo;
    }

    public void setTipo(TipoCuentaFilter tipo) {
        this.tipo = tipo;
    }

    public StringFilter getCbu() {
        return cbu;
    }

    public void setCbu(StringFilter cbu) {
        this.cbu = cbu;
    }

    public StringFilter getAliasCbu() {
        return aliasCbu;
    }

    public void setAliasCbu(StringFilter aliasCbu) {
        this.aliasCbu = aliasCbu;
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

    public BooleanFilter getHabilitada() {
        return habilitada;
    }

    public void setHabilitada(BooleanFilter habilitada) {
        this.habilitada = habilitada;
    }

    public BooleanFilter getParaCredito() {
        return paraCredito;
    }

    public void setParaCredito(BooleanFilter paraCredito) {
        this.paraCredito = paraCredito;
    }

    public BooleanFilter getParaDebito() {
        return paraDebito;
    }

    public void setParaDebito(BooleanFilter paraDebito) {
        this.paraDebito = paraDebito;
    }

    public BigDecimalFilter getMaximoCredito() {
        return maximoCredito;
    }

    public void setMaximoCredito(BigDecimalFilter maximoCredito) {
        this.maximoCredito = maximoCredito;
    }

    public BigDecimalFilter getMaximoDebito() {
        return maximoDebito;
    }

    public void setMaximoDebito(BigDecimalFilter maximoDebito) {
        this.maximoDebito = maximoDebito;
    }

    public StringFilter getCodigoSeguridad() {
        return codigoSeguridad;
    }

    public void setCodigoSeguridad(StringFilter codigoSeguridad) {
        this.codigoSeguridad = codigoSeguridad;
    }

    public LongFilter getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(LongFilter usuarioId) {
        this.usuarioId = usuarioId;
    }

    public LongFilter getBancoId() {
        return bancoId;
    }

    public void setBancoId(LongFilter bancoId) {
        this.bancoId = bancoId;
    }

    @Override
    public String toString() {
        return "CuentaCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (nombre != null ? "nombre=" + nombre + ", " : "") +
                (descripcion != null ? "descripcion=" + descripcion + ", " : "") +
                (moneda != null ? "moneda=" + moneda + ", " : "") +
                (saldo != null ? "saldo=" + saldo + ", " : "") +
                (tipo != null ? "tipo=" + tipo + ", " : "") +
                (cbu != null ? "cbu=" + cbu + ", " : "") +
                (aliasCbu != null ? "aliasCbu=" + aliasCbu + ", " : "") +
                (fechaCreacion != null ? "fechaCreacion=" + fechaCreacion + ", " : "") +
                (fechaModificacion != null ? "fechaModificacion=" + fechaModificacion + ", " : "") +
                (habilitada != null ? "habilitada=" + habilitada + ", " : "") +
                (paraCredito != null ? "paraCredito=" + paraCredito + ", " : "") +
                (paraDebito != null ? "paraDebito=" + paraDebito + ", " : "") +
                (maximoCredito != null ? "maximoCredito=" + maximoCredito + ", " : "") +
                (maximoDebito != null ? "maximoDebito=" + maximoDebito + ", " : "") +
                (codigoSeguridad != null ? "codigoSeguridad=" + codigoSeguridad + ", " : "") +
                (usuarioId != null ? "usuarioId=" + usuarioId + ", " : "") +
                (bancoId != null ? "bancoId=" + bancoId + ", " : "") +
            "}";
    }

}
