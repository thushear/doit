/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { ScaffoldTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { AuthorDetailComponent } from '../../../../../../main/webapp/app/entities/author/author-detail.component';
import { AuthorService } from '../../../../../../main/webapp/app/entities/author/author.service';
import { Author } from '../../../../../../main/webapp/app/entities/author/author.model';

describe('Component Tests', () => {

    describe('Author Management Detail Component', () => {
        let comp: AuthorDetailComponent;
        let fixture: ComponentFixture<AuthorDetailComponent>;
        let service: AuthorService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ScaffoldTestModule],
                declarations: [AuthorDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    AuthorService,
                    JhiEventManager
                ]
            }).overrideTemplate(AuthorDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AuthorDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AuthorService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Author(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.author).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
