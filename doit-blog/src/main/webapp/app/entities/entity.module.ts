import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { ScaffoldBlogModule } from './blog/blog.module';
import { ScaffoldAuthorModule } from './author/author.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        ScaffoldBlogModule,
        ScaffoldAuthorModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ScaffoldEntityModule {}
