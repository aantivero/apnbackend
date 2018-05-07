import { browser, element, by } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';

describe('Cuenta e2e test', () => {

    let navBarPage: NavBarPage;
    let cuentaDialogPage: CuentaDialogPage;
    let cuentaComponentsPage: CuentaComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Cuentas', () => {
        navBarPage.goToEntity('cuenta');
        cuentaComponentsPage = new CuentaComponentsPage();
        expect(cuentaComponentsPage.getTitle())
            .toMatch(/paynowApp.cuenta.home.title/);

    });

    it('should load create Cuenta dialog', () => {
        cuentaComponentsPage.clickOnCreateButton();
        cuentaDialogPage = new CuentaDialogPage();
        expect(cuentaDialogPage.getModalTitle())
            .toMatch(/paynowApp.cuenta.home.createOrEditLabel/);
        cuentaDialogPage.close();
    });

   /* it('should create and save Cuentas', () => {
        cuentaComponentsPage.clickOnCreateButton();
        cuentaDialogPage.setNombreInput('nombre');
        expect(cuentaDialogPage.getNombreInput()).toMatch('nombre');
        cuentaDialogPage.setDescripcionInput('descripcion');
        expect(cuentaDialogPage.getDescripcionInput()).toMatch('descripcion');
        cuentaDialogPage.monedaSelectLastOption();
        cuentaDialogPage.setSaldoInput('5');
        expect(cuentaDialogPage.getSaldoInput()).toMatch('5');
        cuentaDialogPage.tipoSelectLastOption();
        cuentaDialogPage.setCbuInput('cbu');
        expect(cuentaDialogPage.getCbuInput()).toMatch('cbu');
        cuentaDialogPage.setAliasCbuInput('aliasCbu');
        expect(cuentaDialogPage.getAliasCbuInput()).toMatch('aliasCbu');
        cuentaDialogPage.setFechaCreacionInput(12310020012301);
        expect(cuentaDialogPage.getFechaCreacionInput()).toMatch('2001-12-31T02:30');
        cuentaDialogPage.setFechaModificacionInput(12310020012301);
        expect(cuentaDialogPage.getFechaModificacionInput()).toMatch('2001-12-31T02:30');
        cuentaDialogPage.getHabilitadaInput().isSelected().then((selected) => {
            if (selected) {
                cuentaDialogPage.getHabilitadaInput().click();
                expect(cuentaDialogPage.getHabilitadaInput().isSelected()).toBeFalsy();
            } else {
                cuentaDialogPage.getHabilitadaInput().click();
                expect(cuentaDialogPage.getHabilitadaInput().isSelected()).toBeTruthy();
            }
        });
        cuentaDialogPage.getParaCreditoInput().isSelected().then((selected) => {
            if (selected) {
                cuentaDialogPage.getParaCreditoInput().click();
                expect(cuentaDialogPage.getParaCreditoInput().isSelected()).toBeFalsy();
            } else {
                cuentaDialogPage.getParaCreditoInput().click();
                expect(cuentaDialogPage.getParaCreditoInput().isSelected()).toBeTruthy();
            }
        });
        cuentaDialogPage.getParaDebitoInput().isSelected().then((selected) => {
            if (selected) {
                cuentaDialogPage.getParaDebitoInput().click();
                expect(cuentaDialogPage.getParaDebitoInput().isSelected()).toBeFalsy();
            } else {
                cuentaDialogPage.getParaDebitoInput().click();
                expect(cuentaDialogPage.getParaDebitoInput().isSelected()).toBeTruthy();
            }
        });
        cuentaDialogPage.setMaximoCreditoInput('5');
        expect(cuentaDialogPage.getMaximoCreditoInput()).toMatch('5');
        cuentaDialogPage.setMaximoDebitoInput('5');
        expect(cuentaDialogPage.getMaximoDebitoInput()).toMatch('5');
        cuentaDialogPage.setCodigoSeguridadInput('codigoSeguridad');
        expect(cuentaDialogPage.getCodigoSeguridadInput()).toMatch('codigoSeguridad');
        cuentaDialogPage.usuarioSelectLastOption();
        cuentaDialogPage.bancoSelectLastOption();
        cuentaDialogPage.save();
        expect(cuentaDialogPage.getSaveButton().isPresent()).toBeFalsy();
    });*/

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class CuentaComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-cuenta div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class CuentaDialogPage {
    modalTitle = element(by.css('h4#myCuentaLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    nombreInput = element(by.css('input#field_nombre'));
    descripcionInput = element(by.css('input#field_descripcion'));
    monedaSelect = element(by.css('select#field_moneda'));
    saldoInput = element(by.css('input#field_saldo'));
    tipoSelect = element(by.css('select#field_tipo'));
    cbuInput = element(by.css('input#field_cbu'));
    aliasCbuInput = element(by.css('input#field_aliasCbu'));
    fechaCreacionInput = element(by.css('input#field_fechaCreacion'));
    fechaModificacionInput = element(by.css('input#field_fechaModificacion'));
    habilitadaInput = element(by.css('input#field_habilitada'));
    paraCreditoInput = element(by.css('input#field_paraCredito'));
    paraDebitoInput = element(by.css('input#field_paraDebito'));
    maximoCreditoInput = element(by.css('input#field_maximoCredito'));
    maximoDebitoInput = element(by.css('input#field_maximoDebito'));
    codigoSeguridadInput = element(by.css('input#field_codigoSeguridad'));
    usuarioSelect = element(by.css('select#field_usuario'));
    bancoSelect = element(by.css('select#field_banco'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setNombreInput = function(nombre) {
        this.nombreInput.sendKeys(nombre);
    };

    getNombreInput = function() {
        return this.nombreInput.getAttribute('value');
    };

    setDescripcionInput = function(descripcion) {
        this.descripcionInput.sendKeys(descripcion);
    };

    getDescripcionInput = function() {
        return this.descripcionInput.getAttribute('value');
    };

    setMonedaSelect = function(moneda) {
        this.monedaSelect.sendKeys(moneda);
    };

    getMonedaSelect = function() {
        return this.monedaSelect.element(by.css('option:checked')).getText();
    };

    monedaSelectLastOption = function() {
        this.monedaSelect.all(by.tagName('option')).last().click();
    };
    setSaldoInput = function(saldo) {
        this.saldoInput.sendKeys(saldo);
    };

    getSaldoInput = function() {
        return this.saldoInput.getAttribute('value');
    };

    setTipoSelect = function(tipo) {
        this.tipoSelect.sendKeys(tipo);
    };

    getTipoSelect = function() {
        return this.tipoSelect.element(by.css('option:checked')).getText();
    };

    tipoSelectLastOption = function() {
        this.tipoSelect.all(by.tagName('option')).last().click();
    };
    setCbuInput = function(cbu) {
        this.cbuInput.sendKeys(cbu);
    };

    getCbuInput = function() {
        return this.cbuInput.getAttribute('value');
    };

    setAliasCbuInput = function(aliasCbu) {
        this.aliasCbuInput.sendKeys(aliasCbu);
    };

    getAliasCbuInput = function() {
        return this.aliasCbuInput.getAttribute('value');
    };

    setFechaCreacionInput = function(fechaCreacion) {
        this.fechaCreacionInput.sendKeys(fechaCreacion);
    };

    getFechaCreacionInput = function() {
        return this.fechaCreacionInput.getAttribute('value');
    };

    setFechaModificacionInput = function(fechaModificacion) {
        this.fechaModificacionInput.sendKeys(fechaModificacion);
    };

    getFechaModificacionInput = function() {
        return this.fechaModificacionInput.getAttribute('value');
    };

    getHabilitadaInput = function() {
        return this.habilitadaInput;
    };
    getParaCreditoInput = function() {
        return this.paraCreditoInput;
    };
    getParaDebitoInput = function() {
        return this.paraDebitoInput;
    };
    setMaximoCreditoInput = function(maximoCredito) {
        this.maximoCreditoInput.sendKeys(maximoCredito);
    };

    getMaximoCreditoInput = function() {
        return this.maximoCreditoInput.getAttribute('value');
    };

    setMaximoDebitoInput = function(maximoDebito) {
        this.maximoDebitoInput.sendKeys(maximoDebito);
    };

    getMaximoDebitoInput = function() {
        return this.maximoDebitoInput.getAttribute('value');
    };

    setCodigoSeguridadInput = function(codigoSeguridad) {
        this.codigoSeguridadInput.sendKeys(codigoSeguridad);
    };

    getCodigoSeguridadInput = function() {
        return this.codigoSeguridadInput.getAttribute('value');
    };

    usuarioSelectLastOption = function() {
        this.usuarioSelect.all(by.tagName('option')).last().click();
    };

    usuarioSelectOption = function(option) {
        this.usuarioSelect.sendKeys(option);
    };

    getUsuarioSelect = function() {
        return this.usuarioSelect;
    };

    getUsuarioSelectedOption = function() {
        return this.usuarioSelect.element(by.css('option:checked')).getText();
    };

    bancoSelectLastOption = function() {
        this.bancoSelect.all(by.tagName('option')).last().click();
    };

    bancoSelectOption = function(option) {
        this.bancoSelect.sendKeys(option);
    };

    getBancoSelect = function() {
        return this.bancoSelect;
    };

    getBancoSelectedOption = function() {
        return this.bancoSelect.element(by.css('option:checked')).getText();
    };

    save() {
        this.saveButton.click();
    }

    close() {
        this.closeButton.click();
    }

    getSaveButton() {
        return this.saveButton;
    }
}
