import { browser, element, by } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';

describe('MovimientoApp e2e test', () => {

    let navBarPage: NavBarPage;
    let movimientoAppDialogPage: MovimientoAppDialogPage;
    let movimientoAppComponentsPage: MovimientoAppComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load MovimientoApps', () => {
        navBarPage.goToEntity('movimiento-app');
        movimientoAppComponentsPage = new MovimientoAppComponentsPage();
        expect(movimientoAppComponentsPage.getTitle())
            .toMatch(/paynowApp.movimientoApp.home.title/);

    });

    it('should load create MovimientoApp dialog', () => {
        movimientoAppComponentsPage.clickOnCreateButton();
        movimientoAppDialogPage = new MovimientoAppDialogPage();
        expect(movimientoAppDialogPage.getModalTitle())
            .toMatch(/paynowApp.movimientoApp.home.createOrEditLabel/);
        movimientoAppDialogPage.close();
    });

   /* it('should create and save MovimientoApps', () => {
        movimientoAppComponentsPage.clickOnCreateButton();
        movimientoAppDialogPage.setCuentaDebitoCbuInput('cuentaDebitoCbu');
        expect(movimientoAppDialogPage.getCuentaDebitoCbuInput()).toMatch('cuentaDebitoCbu');
        movimientoAppDialogPage.setCuentaDebitoAliasInput('cuentaDebitoAlias');
        expect(movimientoAppDialogPage.getCuentaDebitoAliasInput()).toMatch('cuentaDebitoAlias');
        movimientoAppDialogPage.getCuentaDebitoPropiaInput().isSelected().then((selected) => {
            if (selected) {
                movimientoAppDialogPage.getCuentaDebitoPropiaInput().click();
                expect(movimientoAppDialogPage.getCuentaDebitoPropiaInput().isSelected()).toBeFalsy();
            } else {
                movimientoAppDialogPage.getCuentaDebitoPropiaInput().click();
                expect(movimientoAppDialogPage.getCuentaDebitoPropiaInput().isSelected()).toBeTruthy();
            }
        });
        movimientoAppDialogPage.setCuentaCreditoCbuInput('cuentaCreditoCbu');
        expect(movimientoAppDialogPage.getCuentaCreditoCbuInput()).toMatch('cuentaCreditoCbu');
        movimientoAppDialogPage.setCuentaCreditoAliasInput('cuentaCreditoAlias');
        expect(movimientoAppDialogPage.getCuentaCreditoAliasInput()).toMatch('cuentaCreditoAlias');
        movimientoAppDialogPage.getCuentaCreditoPropiaInput().isSelected().then((selected) => {
            if (selected) {
                movimientoAppDialogPage.getCuentaCreditoPropiaInput().click();
                expect(movimientoAppDialogPage.getCuentaCreditoPropiaInput().isSelected()).toBeFalsy();
            } else {
                movimientoAppDialogPage.getCuentaCreditoPropiaInput().click();
                expect(movimientoAppDialogPage.getCuentaCreditoPropiaInput().isSelected()).toBeTruthy();
            }
        });
        movimientoAppDialogPage.monedaSelectLastOption();
        movimientoAppDialogPage.setMontoInput('5');
        expect(movimientoAppDialogPage.getMontoInput()).toMatch('5');
        movimientoAppDialogPage.setTimestampInput(12310020012301);
        expect(movimientoAppDialogPage.getTimestampInput()).toMatch('2001-12-31T02:30');
        movimientoAppDialogPage.setDescripcionInput('descripcion');
        expect(movimientoAppDialogPage.getDescripcionInput()).toMatch('descripcion');
        movimientoAppDialogPage.setIdentificacionInput('identificacion');
        expect(movimientoAppDialogPage.getIdentificacionInput()).toMatch('identificacion');
        movimientoAppDialogPage.getConsolidadoInput().isSelected().then((selected) => {
            if (selected) {
                movimientoAppDialogPage.getConsolidadoInput().click();
                expect(movimientoAppDialogPage.getConsolidadoInput().isSelected()).toBeFalsy();
            } else {
                movimientoAppDialogPage.getConsolidadoInput().click();
                expect(movimientoAppDialogPage.getConsolidadoInput().isSelected()).toBeTruthy();
            }
        });
        movimientoAppDialogPage.setConsolidadoTimestampInput(12310020012301);
        expect(movimientoAppDialogPage.getConsolidadoTimestampInput()).toMatch('2001-12-31T02:30');
        movimientoAppDialogPage.bancoDebitoSelectLastOption();
        movimientoAppDialogPage.bancoCreditoSelectLastOption();
        movimientoAppDialogPage.save();
        expect(movimientoAppDialogPage.getSaveButton().isPresent()).toBeFalsy();
    });*/

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class MovimientoAppComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-movimiento-app div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class MovimientoAppDialogPage {
    modalTitle = element(by.css('h4#myMovimientoAppLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    cuentaDebitoCbuInput = element(by.css('input#field_cuentaDebitoCbu'));
    cuentaDebitoAliasInput = element(by.css('input#field_cuentaDebitoAlias'));
    cuentaDebitoPropiaInput = element(by.css('input#field_cuentaDebitoPropia'));
    cuentaCreditoCbuInput = element(by.css('input#field_cuentaCreditoCbu'));
    cuentaCreditoAliasInput = element(by.css('input#field_cuentaCreditoAlias'));
    cuentaCreditoPropiaInput = element(by.css('input#field_cuentaCreditoPropia'));
    monedaSelect = element(by.css('select#field_moneda'));
    montoInput = element(by.css('input#field_monto'));
    timestampInput = element(by.css('input#field_timestamp'));
    descripcionInput = element(by.css('input#field_descripcion'));
    identificacionInput = element(by.css('input#field_identificacion'));
    consolidadoInput = element(by.css('input#field_consolidado'));
    consolidadoTimestampInput = element(by.css('input#field_consolidadoTimestamp'));
    bancoDebitoSelect = element(by.css('select#field_bancoDebito'));
    bancoCreditoSelect = element(by.css('select#field_bancoCredito'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setCuentaDebitoCbuInput = function(cuentaDebitoCbu) {
        this.cuentaDebitoCbuInput.sendKeys(cuentaDebitoCbu);
    };

    getCuentaDebitoCbuInput = function() {
        return this.cuentaDebitoCbuInput.getAttribute('value');
    };

    setCuentaDebitoAliasInput = function(cuentaDebitoAlias) {
        this.cuentaDebitoAliasInput.sendKeys(cuentaDebitoAlias);
    };

    getCuentaDebitoAliasInput = function() {
        return this.cuentaDebitoAliasInput.getAttribute('value');
    };

    getCuentaDebitoPropiaInput = function() {
        return this.cuentaDebitoPropiaInput;
    };
    setCuentaCreditoCbuInput = function(cuentaCreditoCbu) {
        this.cuentaCreditoCbuInput.sendKeys(cuentaCreditoCbu);
    };

    getCuentaCreditoCbuInput = function() {
        return this.cuentaCreditoCbuInput.getAttribute('value');
    };

    setCuentaCreditoAliasInput = function(cuentaCreditoAlias) {
        this.cuentaCreditoAliasInput.sendKeys(cuentaCreditoAlias);
    };

    getCuentaCreditoAliasInput = function() {
        return this.cuentaCreditoAliasInput.getAttribute('value');
    };

    getCuentaCreditoPropiaInput = function() {
        return this.cuentaCreditoPropiaInput;
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
    setMontoInput = function(monto) {
        this.montoInput.sendKeys(monto);
    };

    getMontoInput = function() {
        return this.montoInput.getAttribute('value');
    };

    setTimestampInput = function(timestamp) {
        this.timestampInput.sendKeys(timestamp);
    };

    getTimestampInput = function() {
        return this.timestampInput.getAttribute('value');
    };

    setDescripcionInput = function(descripcion) {
        this.descripcionInput.sendKeys(descripcion);
    };

    getDescripcionInput = function() {
        return this.descripcionInput.getAttribute('value');
    };

    setIdentificacionInput = function(identificacion) {
        this.identificacionInput.sendKeys(identificacion);
    };

    getIdentificacionInput = function() {
        return this.identificacionInput.getAttribute('value');
    };

    getConsolidadoInput = function() {
        return this.consolidadoInput;
    };
    setConsolidadoTimestampInput = function(consolidadoTimestamp) {
        this.consolidadoTimestampInput.sendKeys(consolidadoTimestamp);
    };

    getConsolidadoTimestampInput = function() {
        return this.consolidadoTimestampInput.getAttribute('value');
    };

    bancoDebitoSelectLastOption = function() {
        this.bancoDebitoSelect.all(by.tagName('option')).last().click();
    };

    bancoDebitoSelectOption = function(option) {
        this.bancoDebitoSelect.sendKeys(option);
    };

    getBancoDebitoSelect = function() {
        return this.bancoDebitoSelect;
    };

    getBancoDebitoSelectedOption = function() {
        return this.bancoDebitoSelect.element(by.css('option:checked')).getText();
    };

    bancoCreditoSelectLastOption = function() {
        this.bancoCreditoSelect.all(by.tagName('option')).last().click();
    };

    bancoCreditoSelectOption = function(option) {
        this.bancoCreditoSelect.sendKeys(option);
    };

    getBancoCreditoSelect = function() {
        return this.bancoCreditoSelect;
    };

    getBancoCreditoSelectedOption = function() {
        return this.bancoCreditoSelect.element(by.css('option:checked')).getText();
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
