import { browser, element, by } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';

describe('CuentaApp e2e test', () => {

    let navBarPage: NavBarPage;
    let cuentaAppDialogPage: CuentaAppDialogPage;
    let cuentaAppComponentsPage: CuentaAppComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load CuentaApps', () => {
        navBarPage.goToEntity('cuenta-app');
        cuentaAppComponentsPage = new CuentaAppComponentsPage();
        expect(cuentaAppComponentsPage.getTitle())
            .toMatch(/paynowApp.cuentaApp.home.title/);

    });

    it('should load create CuentaApp dialog', () => {
        cuentaAppComponentsPage.clickOnCreateButton();
        cuentaAppDialogPage = new CuentaAppDialogPage();
        expect(cuentaAppDialogPage.getModalTitle())
            .toMatch(/paynowApp.cuentaApp.home.createOrEditLabel/);
        cuentaAppDialogPage.close();
    });

   /* it('should create and save CuentaApps', () => {
        cuentaAppComponentsPage.clickOnCreateButton();
        cuentaAppDialogPage.setNombreInput('nombre');
        expect(cuentaAppDialogPage.getNombreInput()).toMatch('nombre');
        cuentaAppDialogPage.setAliasCbuInput('aliasCbu');
        expect(cuentaAppDialogPage.getAliasCbuInput()).toMatch('aliasCbu');
        cuentaAppDialogPage.setCbuInput('cbu');
        expect(cuentaAppDialogPage.getCbuInput()).toMatch('cbu');
        cuentaAppDialogPage.monedaSelectLastOption();
        cuentaAppDialogPage.setSaldoInput('5');
        expect(cuentaAppDialogPage.getSaldoInput()).toMatch('5');
        cuentaAppDialogPage.bancoSelectLastOption();
        cuentaAppDialogPage.appSelectLastOption();
        cuentaAppDialogPage.save();
        expect(cuentaAppDialogPage.getSaveButton().isPresent()).toBeFalsy();
    });*/

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class CuentaAppComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-cuenta-app div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class CuentaAppDialogPage {
    modalTitle = element(by.css('h4#myCuentaAppLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    nombreInput = element(by.css('input#field_nombre'));
    aliasCbuInput = element(by.css('input#field_aliasCbu'));
    cbuInput = element(by.css('input#field_cbu'));
    monedaSelect = element(by.css('select#field_moneda'));
    saldoInput = element(by.css('input#field_saldo'));
    bancoSelect = element(by.css('select#field_banco'));
    appSelect = element(by.css('select#field_app'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setNombreInput = function(nombre) {
        this.nombreInput.sendKeys(nombre);
    };

    getNombreInput = function() {
        return this.nombreInput.getAttribute('value');
    };

    setAliasCbuInput = function(aliasCbu) {
        this.aliasCbuInput.sendKeys(aliasCbu);
    };

    getAliasCbuInput = function() {
        return this.aliasCbuInput.getAttribute('value');
    };

    setCbuInput = function(cbu) {
        this.cbuInput.sendKeys(cbu);
    };

    getCbuInput = function() {
        return this.cbuInput.getAttribute('value');
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

    appSelectLastOption = function() {
        this.appSelect.all(by.tagName('option')).last().click();
    };

    appSelectOption = function(option) {
        this.appSelect.sendKeys(option);
    };

    getAppSelect = function() {
        return this.appSelect;
    };

    getAppSelectedOption = function() {
        return this.appSelect.element(by.css('option:checked')).getText();
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
