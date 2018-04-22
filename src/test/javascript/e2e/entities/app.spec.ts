import { browser, element, by } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';

describe('App e2e test', () => {

    let navBarPage: NavBarPage;
    let appDialogPage: AppDialogPage;
    let appComponentsPage: AppComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Apps', () => {
        navBarPage.goToEntity('app');
        appComponentsPage = new AppComponentsPage();
        expect(appComponentsPage.getTitle())
            .toMatch(/paynowApp.app.home.title/);

    });

    it('should load create App dialog', () => {
        appComponentsPage.clickOnCreateButton();
        appDialogPage = new AppDialogPage();
        expect(appDialogPage.getModalTitle())
            .toMatch(/paynowApp.app.home.createOrEditLabel/);
        appDialogPage.close();
    });

    it('should create and save Apps', () => {
        appComponentsPage.clickOnCreateButton();
        appDialogPage.setNombreInput('nombre');
        expect(appDialogPage.getNombreInput()).toMatch('nombre');
        appDialogPage.save();
        expect(appDialogPage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class AppComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-app div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class AppDialogPage {
    modalTitle = element(by.css('h4#myAppLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    nombreInput = element(by.css('input#field_nombre'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setNombreInput = function(nombre) {
        this.nombreInput.sendKeys(nombre);
    };

    getNombreInput = function() {
        return this.nombreInput.getAttribute('value');
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
