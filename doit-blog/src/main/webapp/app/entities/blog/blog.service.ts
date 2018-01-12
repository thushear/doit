import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Blog } from './blog.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class BlogService {

    private resourceUrl = SERVER_API_URL + 'api/blogs';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(blog: Blog): Observable<Blog> {
        const copy = this.convert(blog);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(blog: Blog): Observable<Blog> {
        const copy = this.convert(blog);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Blog> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        const result = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            result.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return new ResponseWrapper(res.headers, result, res.status);
    }

    /**
     * Convert a returned JSON object to Blog.
     */
    private convertItemFromServer(json: any): Blog {
        const entity: Blog = Object.assign(new Blog(), json);
        entity.publicDate = this.dateUtils
            .convertLocalDateFromServer(json.publicDate);
        return entity;
    }

    /**
     * Convert a Blog to a JSON which can be sent to the server.
     */
    private convert(blog: Blog): Blog {
        const copy: Blog = Object.assign({}, blog);
        copy.publicDate = this.dateUtils
            .convertLocalDateToServer(blog.publicDate);
        return copy;
    }
}
