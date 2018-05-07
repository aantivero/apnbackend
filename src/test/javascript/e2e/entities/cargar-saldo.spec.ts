import { browser, element, by } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';

describe('CargarSaldo e2e test', () => {

    let navBarPage: NavBarPage;
    let cargarSaldoDialogPage: CargarSaldoDialogPage;
    let cargarSaldoComponentsPage: CargarSaldoComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load CargarSaldos', () => {
        navBarPage.goToEntity('cargar-saldo');
        cargarSaldoComponentsPage = new CargarSaldoComponentsPage();
        expect(cargarSaldoComponentsPage.getTitle())
            .toMatch(/paynowApp.cargarSaldo.home.title/);

    });

    it('should load create CargarSaldo dialog', () => {
        cargarSaldoComponentsPage.clickOnCreateButton();
        cargarSaldoDialogPage = new CargarSaldoDialogPage();
        expect(cargarSaldoDialogPage.getModalTitle())
            .toMatch(/paynowApp.cargarSaldo.home.createOrEditLabel/);
        cargarSaldoDialogPage.close();
    });

   /* it('should create and save CargarSaldos', () => {
        cargarSaldoComponentsPage.clickOnCreateButton();
        cargarSaldoDialogPage.setMontoInput('5');
        expect(cargarSaldoDialogPage.getMontoInput()).toMatch('5');
        cargarSaldoDialogPage.usuarioSelectLastOption();
        cargarSaldoDialogPage.cuentaSelectLastOption();
        cargarSaldoDialogPage.transferenciaAppSelectLastOption();
        cargarSaldoDialogPage.save();
        expect(cargarSaldoDialogPage.getSaveButton().isPresent()).toBeFalsy();
    });*/

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class CargarSaldoComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-cargar-saldo div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class CargarSaldoDialogPage {
    modalTitle = element(by.css('h4#myCargarSaldoLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    montoInput = element(by.css('input#field_monto'));
    usuarioSelect = element(by.css('select#field_usuario'));
    cuentaSelect = element(by.css('select#field_cuenta'));
    transferenciaAppSelect = element(by.css('select#field_transferenciaApp'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setMontoInput = function(monto) {
        this.montoInput.sendKeys(monto);
    };

    getMontoInput = function() {
        return this.montoInput.getAttribute('value');
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

    cuentaSelectLastOption = function() {
        this.cuentaSelect.all(by.tagName('option')).last().click();
    };

    cuentaSelectOption = function(option) {
        this.cuentaSelect.sendKeys(option);
    };

    getCuentaSelect = function() {
        return this.cuentaSelect;
    };

    getCuentaSelectedOption = function() {
        return this.cuentaSelect.element(by.css('option:checked')).getText();
    };

    transferenciaAppSelectLastOption = function() {
        this.transferenciaAppSelect.all(by.tagName('option')).last().click();
    };

    transferenciaAppSelectOption = function(option) {
        this.transferenciaAppSelect.sendKeys(option);
    };

    getTransferenciaAppSelect = function() {
        return this.transferenciaAppSelect;
    };

    getTransferenciaAppSelectedOption = function() {
        return this.transferenciaAppSelect.element(by.css('option:checked')).getText();
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
