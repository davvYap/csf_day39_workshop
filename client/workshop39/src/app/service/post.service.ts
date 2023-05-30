import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class PostService {
  constructor(private http: HttpClient) {}

  postComments(id: number, comments: string): Observable<string> {
    let dataToPost = new HttpParams().set('comments', comments);

    const httpHearders = new HttpHeaders().set(
      'Content-Type',
      'application/x-www-form-urlencoded'
    );

    return this.http.post<string>(
      `/api/character/${id}`,
      dataToPost.toString(),
      { headers: httpHearders }
    );
  }
}

// http://localhost:8080
