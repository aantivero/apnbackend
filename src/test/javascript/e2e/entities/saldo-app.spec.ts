import { browser, element, by } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';

describe('SaldoApp e2e test', () => {

    let navBarPage: NavBarPage;
    let saldoAppDialogPage: SaldoAppDialogPage;
    let saldoAppComponentsPage: SaldoAppComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load SaldoApps', () => {
        navBarPage.goToEntity('saldo-app');
        saldoAppComponentsPage = new SaldoAppComponentsPage();
        expect(saldoAppComponentsPage.getTitle())
            .toMatch(/paynowApp.saldoApp.home.title/);

    });

    it('should load create SaldoApp dialog', () => {
        saldoAppComponentsPage.clickOnCreateButton();
        saldoAppDialogPage = new SaldoAppDialogPage();
        expect(saldoAppDialogPage.getModalTitle())
            .toMatch(/paynowApp.saldoApp.home.createOrEditLabel/);
        saldoAppDialogPage.close();
    });

   /* it('should create and save SaldoApps', () => {
        saldoAppComponentsPage.clickOnCreateButton();
        saldoAppDialogPage.monedaSelectLastOption();
        saldoAppDialogPage.setMontoInput('5');
        expect(saldoAppDialogPage.getMontoInput()).toMatch('5');
        saldoAppDialogPage.appSelectLastOption();
        saldoAppDialogPage.save();
        expect(saldoAppDialogPage.getSaveButton().isPresent()).toBeFalsy();
    });*/

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class SaldoAppComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-saldo-app div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class SaldoAppDialogPage {
    modalTitle = element(by.css('h4#mySaldoAppLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    monedaSelect = element(by.css('select#field_moneda'));
    montoInput = element(by.css('input#field_monto'));
    appSelect = element(by.css('select#field_app'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

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
