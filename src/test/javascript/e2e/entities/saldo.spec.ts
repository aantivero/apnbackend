import { browser, element, by } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';

describe('Saldo e2e test', () => {

    let navBarPage: NavBarPage;
    let saldoDialogPage: SaldoDialogPage;
    let saldoComponentsPage: SaldoComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Saldos', () => {
        navBarPage.goToEntity('saldo');
        saldoComponentsPage = new SaldoComponentsPage();
        expect(saldoComponentsPage.getTitle())
            .toMatch(/paynowApp.saldo.home.title/);

    });

    it('should load create Saldo dialog', () => {
        saldoComponentsPage.clickOnCreateButton();
        saldoDialogPage = new SaldoDialogPage();
        expect(saldoDialogPage.getModalTitle())
            .toMatch(/paynowApp.saldo.home.createOrEditLabel/);
        saldoDialogPage.close();
    });

   /* it('should create and save Saldos', () => {
        saldoComponentsPage.clickOnCreateButton();
        saldoDialogPage.setMontoInput('5');
        expect(saldoDialogPage.getMontoInput()).toMatch('5');
        saldoDialogPage.monedaSelectLastOption();
        saldoDialogPage.tipoSelectLastOption();
        saldoDialogPage.setFechaCreacionInput(12310020012301);
        expect(saldoDialogPage.getFechaCreacionInput()).toMatch('2001-12-31T02:30');
        saldoDialogPage.setFechaModificacionInput(12310020012301);
        expect(saldoDialogPage.getFechaModificacionInput()).toMatch('2001-12-31T02:30');
        saldoDialogPage.usuarioSelectLastOption();
        saldoDialogPage.aplicacionSelectLastOption();
        saldoDialogPage.save();
        expect(saldoDialogPage.getSaveButton().isPresent()).toBeFalsy();
    });*/

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class SaldoComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-saldo div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class SaldoDialogPage {
    modalTitle = element(by.css('h4#mySaldoLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    montoInput = element(by.css('input#field_monto'));
    monedaSelect = element(by.css('select#field_moneda'));
    tipoSelect = element(by.css('select#field_tipo'));
    fechaCreacionInput = element(by.css('input#field_fechaCreacion'));
    fechaModificacionInput = element(by.css('input#field_fechaModificacion'));
    usuarioSelect = element(by.css('select#field_usuario'));
    aplicacionSelect = element(by.css('select#field_aplicacion'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setMontoInput = function(monto) {
        this.montoInput.sendKeys(monto);
    };

    getMontoInput = function() {
        return this.montoInput.getAttribute('value');
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
    setTipoSelect = function(tipo) {
        this.tipoSelect.sendKeys(tipo);
    };

    getTipoSelect = function() {
        return this.tipoSelect.element(by.css('option:checked')).getText();
    };

    tipoSelectLastOption = function() {
        this.tipoSelect.all(by.tagName('option')).last().click();
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

    aplicacionSelectLastOption = function() {
        this.aplicacionSelect.all(by.tagName('option')).last().click();
    };

    aplicacionSelectOption = function(option) {
        this.aplicacionSelect.sendKeys(option);
    };

    getAplicacionSelect = function() {
        return this.aplicacionSelect;
    };

    getAplicacionSelectedOption = function() {
        return this.aplicacionSelect.element(by.css('option:checked')).getText();
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
