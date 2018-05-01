import { browser, element, by } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';

describe('TransferenciaApp e2e test', () => {

    let navBarPage: NavBarPage;
    let transferenciaAppDialogPage: TransferenciaAppDialogPage;
    let transferenciaAppComponentsPage: TransferenciaAppComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load TransferenciaApps', () => {
        navBarPage.goToEntity('transferencia-app');
        transferenciaAppComponentsPage = new TransferenciaAppComponentsPage();
        expect(transferenciaAppComponentsPage.getTitle())
            .toMatch(/paynowApp.transferenciaApp.home.title/);

    });

    it('should load create TransferenciaApp dialog', () => {
        transferenciaAppComponentsPage.clickOnCreateButton();
        transferenciaAppDialogPage = new TransferenciaAppDialogPage();
        expect(transferenciaAppDialogPage.getModalTitle())
            .toMatch(/paynowApp.transferenciaApp.home.createOrEditLabel/);
        transferenciaAppDialogPage.close();
    });

   /* it('should create and save TransferenciaApps', () => {
        transferenciaAppComponentsPage.clickOnCreateButton();
        transferenciaAppDialogPage.setDestinoCbuInput('destinoCbu');
        expect(transferenciaAppDialogPage.getDestinoCbuInput()).toMatch('destinoCbu');
        transferenciaAppDialogPage.setDestinoAliasInput('destinoAlias');
        expect(transferenciaAppDialogPage.getDestinoAliasInput()).toMatch('destinoAlias');
        transferenciaAppDialogPage.setDestinoInfoInput('destinoInfo');
        expect(transferenciaAppDialogPage.getDestinoInfoInput()).toMatch('destinoInfo');
        transferenciaAppDialogPage.monedaSelectLastOption();
        transferenciaAppDialogPage.setMontoInput('5');
        expect(transferenciaAppDialogPage.getMontoInput()).toMatch('5');
        transferenciaAppDialogPage.setDescripcionInput('descripcion');
        expect(transferenciaAppDialogPage.getDescripcionInput()).toMatch('descripcion');
        transferenciaAppDialogPage.estadoTransferenciaSelectLastOption();
        transferenciaAppDialogPage.setTimestampInput(12310020012301);
        expect(transferenciaAppDialogPage.getTimestampInput()).toMatch('2001-12-31T02:30');
        transferenciaAppDialogPage.setDescripcionEstadoInput('descripcionEstado');
        expect(transferenciaAppDialogPage.getDescripcionEstadoInput()).toMatch('descripcionEstado');
        transferenciaAppDialogPage.setIdentificacionInput('identificacion');
        expect(transferenciaAppDialogPage.getIdentificacionInput()).toMatch('identificacion');
        transferenciaAppDialogPage.origenSelectLastOption();
        transferenciaAppDialogPage.destinoBancoSelectLastOption();
        transferenciaAppDialogPage.save();
        expect(transferenciaAppDialogPage.getSaveButton().isPresent()).toBeFalsy();
    });*/

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class TransferenciaAppComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-transferencia-app div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class TransferenciaAppDialogPage {
    modalTitle = element(by.css('h4#myTransferenciaAppLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    destinoCbuInput = element(by.css('input#field_destinoCbu'));
    destinoAliasInput = element(by.css('input#field_destinoAlias'));
    destinoInfoInput = element(by.css('input#field_destinoInfo'));
    monedaSelect = element(by.css('select#field_moneda'));
    montoInput = element(by.css('input#field_monto'));
    descripcionInput = element(by.css('input#field_descripcion'));
    estadoTransferenciaSelect = element(by.css('select#field_estadoTransferencia'));
    timestampInput = element(by.css('input#field_timestamp'));
    descripcionEstadoInput = element(by.css('input#field_descripcionEstado'));
    identificacionInput = element(by.css('input#field_identificacion'));
    origenSelect = element(by.css('select#field_origen'));
    destinoBancoSelect = element(by.css('select#field_destinoBanco'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setDestinoCbuInput = function(destinoCbu) {
        this.destinoCbuInput.sendKeys(destinoCbu);
    };

    getDestinoCbuInput = function() {
        return this.destinoCbuInput.getAttribute('value');
    };

    setDestinoAliasInput = function(destinoAlias) {
        this.destinoAliasInput.sendKeys(destinoAlias);
    };

    getDestinoAliasInput = function() {
        return this.destinoAliasInput.getAttribute('value');
    };

    setDestinoInfoInput = function(destinoInfo) {
        this.destinoInfoInput.sendKeys(destinoInfo);
    };

    getDestinoInfoInput = function() {
        return this.destinoInfoInput.getAttribute('value');
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

    setDescripcionInput = function(descripcion) {
        this.descripcionInput.sendKeys(descripcion);
    };

    getDescripcionInput = function() {
        return this.descripcionInput.getAttribute('value');
    };

    setEstadoTransferenciaSelect = function(estadoTransferencia) {
        this.estadoTransferenciaSelect.sendKeys(estadoTransferencia);
    };

    getEstadoTransferenciaSelect = function() {
        return this.estadoTransferenciaSelect.element(by.css('option:checked')).getText();
    };

    estadoTransferenciaSelectLastOption = function() {
        this.estadoTransferenciaSelect.all(by.tagName('option')).last().click();
    };
    setTimestampInput = function(timestamp) {
        this.timestampInput.sendKeys(timestamp);
    };

    getTimestampInput = function() {
        return this.timestampInput.getAttribute('value');
    };

    setDescripcionEstadoInput = function(descripcionEstado) {
        this.descripcionEstadoInput.sendKeys(descripcionEstado);
    };

    getDescripcionEstadoInput = function() {
        return this.descripcionEstadoInput.getAttribute('value');
    };

    setIdentificacionInput = function(identificacion) {
        this.identificacionInput.sendKeys(identificacion);
    };

    getIdentificacionInput = function() {
        return this.identificacionInput.getAttribute('value');
    };

    origenSelectLastOption = function() {
        this.origenSelect.all(by.tagName('option')).last().click();
    };

    origenSelectOption = function(option) {
        this.origenSelect.sendKeys(option);
    };

    getOrigenSelect = function() {
        return this.origenSelect;
    };

    getOrigenSelectedOption = function() {
        return this.origenSelect.element(by.css('option:checked')).getText();
    };

    destinoBancoSelectLastOption = function() {
        this.destinoBancoSelect.all(by.tagName('option')).last().click();
    };

    destinoBancoSelectOption = function(option) {
        this.destinoBancoSelect.sendKeys(option);
    };

    getDestinoBancoSelect = function() {
        return this.destinoBancoSelect;
    };

    getDestinoBancoSelectedOption = function() {
        return this.destinoBancoSelect.element(by.css('option:checked')).getText();
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
