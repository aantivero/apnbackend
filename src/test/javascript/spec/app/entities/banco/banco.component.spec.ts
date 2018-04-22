/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PaynowTestModule } from '../../../test.module';
import { BancoComponent } from '../../../../../../main/webapp/app/entities/banco/banco.component';
import { BancoService } from '../../../../../../main/webapp/app/entities/banco/banco.service';
import { Banco } from '../../../../../../main/webapp/app/entities/banco/banco.model';

describe('Component Tests', () => {

    describe('Banco Management Component', () => {
        let comp: BancoComponent;
        let fixture: ComponentFixture<BancoComponent>;
        let service: BancoService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PaynowTestModule],
                declarations: [BancoComponent],
                providers: [
                    BancoService
                ]
            })
            .overrideTemplate(BancoComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(BancoComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BancoService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Banco(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.bancos[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
