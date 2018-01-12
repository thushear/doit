import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { Author } from './author.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class AuthorService {

    private resourceUrl = SERVER_API_URL + 'api/authors';

    constructor(private http: Http) { }

    create(author: Author): Observable<Author> {
        const copy = this.convert(author);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(author: Author): Observable<Author> {
        const copy = this.convert(author);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Author> {
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
     * Convert a returned JSON object to Author.
     */
    private convertItemFromServer(json: any): Author {
        const entity: Author = Object.assign(new Author(), json);
        return entity;
    }

    /**
     * Convert a Author to a JSON which can be sent to the server.
     */
    private convert(author: Author): Author {
        const copy: Author = Object.assign({}, author);
        return copy;
    }
}
