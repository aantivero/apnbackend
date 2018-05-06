import { browser, element, by } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';

describe('UserExtra e2e test', () => {

    let navBarPage: NavBarPage;
    let userExtraDialogPage: UserExtraDialogPage;
    let userExtraComponentsPage: UserExtraComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load UserExtras', () => {
        navBarPage.goToEntity('user-extra');
        userExtraComponentsPage = new UserExtraComponentsPage();
        expect(userExtraComponentsPage.getTitle())
            .toMatch(/paynowApp.userExtra.home.title/);

    });

    it('should load create UserExtra dialog', () => {
        userExtraComponentsPage.clickOnCreateButton();
        userExtraDialogPage = new UserExtraDialogPage();
        expect(userExtraDialogPage.getModalTitle())
            .toMatch(/paynowApp.userExtra.home.createOrEditLabel/);
        userExtraDialogPage.close();
    });

   /* it('should create and save UserExtras', () => {
        userExtraComponentsPage.clickOnCreateButton();
        userExtraDialogPage.setTelefonoInput('telefono');
        expect(userExtraDialogPage.getTelefonoInput()).toMatch('telefono');
        userExtraDialogPage.getVendedorInput().isSelected().then((selected) => {
            if (selected) {
                userExtraDialogPage.getVendedorInput().click();
                expect(userExtraDialogPage.getVendedorInput().isSelected()).toBeFalsy();
            } else {
                userExtraDialogPage.getVendedorInput().click();
                expect(userExtraDialogPage.getVendedorInput().isSelected()).toBeTruthy();
            }
        });
        userExtraDialogPage.tipoDocumentoSelectLastOption();
        userExtraDialogPage.setNumeroDocumentoInput('numeroDocumento');
        expect(userExtraDialogPage.getNumeroDocumentoInput()).toMatch('numeroDocumento');
        userExtraDialogPage.tipoContribuyenteSelectLastOption();
        userExtraDialogPage.usuarioSelectLastOption();
        userExtraDialogPage.save();
        expect(userExtraDialogPage.getSaveButton().isPresent()).toBeFalsy();
    });*/

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class UserExtraComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-user-extra div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class UserExtraDialogPage {
    modalTitle = element(by.css('h4#myUserExtraLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    telefonoInput = element(by.css('input#field_telefono'));
    vendedorInput = element(by.css('input#field_vendedor'));
    tipoDocumentoSelect = element(by.css('select#field_tipoDocumento'));
    numeroDocumentoInput = element(by.css('input#field_numeroDocumento'));
    tipoContribuyenteSelect = element(by.css('select#field_tipoContribuyente'));
    usuarioSelect = element(by.css('select#field_usuario'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setTelefonoInput = function(telefono) {
        this.telefonoInput.sendKeys(telefono);
    };

    getTelefonoInput = function() {
        return this.telefonoInput.getAttribute('value');
    };

    getVendedorInput = function() {
        return this.vendedorInput;
    };
    setTipoDocumentoSelect = function(tipoDocumento) {
        this.tipoDocumentoSelect.sendKeys(tipoDocumento);
    };

    getTipoDocumentoSelect = function() {
        return this.tipoDocumentoSelect.element(by.css('option:checked')).getText();
    };

    tipoDocumentoSelectLastOption = function() {
        this.tipoDocumentoSelect.all(by.tagName('option')).last().click();
    };
    setNumeroDocumentoInput = function(numeroDocumento) {
        this.numeroDocumentoInput.sendKeys(numeroDocumento);
    };

    getNumeroDocumentoInput = function() {
        return this.numeroDocumentoInput.getAttribute('value');
    };

    setTipoContribuyenteSelect = function(tipoContribuyente) {
        this.tipoContribuyenteSelect.sendKeys(tipoContribuyente);
    };

    getTipoContribuyenteSelect = function() {
        return this.tipoContribuyenteSelect.element(by.css('option:checked')).getText();
    };

    tipoContribuyenteSelectLastOption = function() {
        this.tipoContribuyenteSelect.all(by.tagName('option')).last().click();
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
