import { browser, element, by } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';

describe('Banco e2e test', () => {

    let navBarPage: NavBarPage;
    let bancoDialogPage: BancoDialogPage;
    let bancoComponentsPage: BancoComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Bancos', () => {
        navBarPage.goToEntity('banco');
        bancoComponentsPage = new BancoComponentsPage();
        expect(bancoComponentsPage.getTitle())
            .toMatch(/paynowApp.banco.home.title/);

    });

    it('should load create Banco dialog', () => {
        bancoComponentsPage.clickOnCreateButton();
        bancoDialogPage = new BancoDialogPage();
        expect(bancoDialogPage.getModalTitle())
            .toMatch(/paynowApp.banco.home.createOrEditLabel/);
        bancoDialogPage.close();
    });

    it('should create and save Bancos', () => {
        bancoComponentsPage.clickOnCreateButton();
        bancoDialogPage.setNombreInput('nombre');
        expect(bancoDialogPage.getNombreInput()).toMatch('nombre');
        bancoDialogPage.setCodigoInput('codigo');
        expect(bancoDialogPage.getCodigoInput()).toMatch('codigo');
        bancoDialogPage.save();
        expect(bancoDialogPage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class BancoComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-banco div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class BancoDialogPage {
    modalTitle = element(by.css('h4#myBancoLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    nombreInput = element(by.css('input#field_nombre'));
    codigoInput = element(by.css('input#field_codigo'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setNombreInput = function(nombre) {
        this.nombreInput.sendKeys(nombre);
    };

    getNombreInput = function() {
        return this.nombreInput.getAttribute('value');
    };

    setCodigoInput = function(codigo) {
        this.codigoInput.sendKeys(codigo);
    };

    getCodigoInput = function() {
        return this.codigoInput.getAttribute('value');
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
