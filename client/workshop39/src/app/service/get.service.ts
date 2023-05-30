import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { hero, heroComments, response, search } from '../models';

@Injectable({
  providedIn: 'root',
})
export class GetService {
  heroName!: string;
  result$!: Observable<response>;

  constructor(private http: HttpClient) {}

  searchHeroes(heroName: string, offset: number = 0): Observable<response> {
    let param = new HttpParams()
      .set('heroName', heroName)
      .set('offset', offset);
    return this.http.get<any>('http://localhost:8080/api/characters', {
      params: param,
    });
  }

  searchHeroById(id: number): Observable<hero> {
    return this.http.get<hero>(`http://localhost:8080/api/characters/${id}`);
  }

  getHeroComments(id: number): Observable<heroComments> {
    return this.http.get<heroComments>(
      `http://localhost:8080/api/character/${id}/comments`
    );
  }
}
