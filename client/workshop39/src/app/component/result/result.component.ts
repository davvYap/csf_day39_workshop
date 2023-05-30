import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { GetService } from 'src/app/service/get.service';
import { HttpClient } from '@angular/common/http';
import { response, search } from 'src/app/models';
import { Observable, tap } from 'rxjs';

@Component({
  selector: 'app-result',
  templateUrl: './result.component.html',
  styleUrls: ['./result.component.css'],
})
export class ResultComponent implements OnInit {
  heroName!: string;
  results$!: Observable<response>;
  offset!: number;
  totalResultt!: number;

  constructor(private http: HttpClient, private getSvc: GetService) {}
  ngOnInit(): void {
    this.heroName = this.getSvc.heroName;
    this.results$ = this.getSvc.result$.pipe(
      tap((res) => (this.offset = res.data.offset)),
      tap((res) => (this.totalResultt = res.data.total)),
      tap((res) => console.log('total >>> ', this.totalResultt))
    );
  }

  prev(): void {
    this.offset -= 20;
    this.totalResultt += 20;
    this.results$ = this.getSvc.searchHeroes(this.heroName, this.offset);
  }

  next(): void {
    this.offset += 20;
    this.totalResultt -= 20;
    this.results$ = this.getSvc.searchHeroes(this.heroName, this.offset);
  }

  insufficientResult(): boolean {
    return this.totalResultt <= 20;
  }
}
