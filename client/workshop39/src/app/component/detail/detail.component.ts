import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { Router } from '@angular/router';
import { GetService } from 'src/app/service/get.service';
import { hero, heroComments } from 'src/app/models';
import { Location } from '@angular/common';

@Component({
  selector: 'app-detail',
  templateUrl: './detail.component.html',
  styleUrls: ['./detail.component.css'],
})
export class DetailComponent implements OnInit {
  hero$!: Observable<hero>;
  heroID!: number;
  heroName!: string;
  heroComments$!: Observable<heroComments>;

  constructor(
    private title: Title,
    private getSvc: GetService,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private location: Location
  ) {}
  ngOnInit(): void {
    this.heroID = Number(this.activatedRoute.snapshot.params['id']);
    // console.log('heroid >>> ', this.heroID);
    this.hero$ = this.getSvc
      .searchHeroById(Number(this.activatedRoute.snapshot.params['id']))
      .pipe(
        tap((hero) => {
          this.title.setTitle(`Result | ${hero.name}`);
          this.heroName = hero.name;
        })
      );

    this.heroComments$ = this.getSvc.getHeroComments(this.heroID);
  }

  back() {
    this.getSvc.heroName = this.heroName;
    this.router.navigate(['/result']);
  }

  comment() {
    this.router.navigate(['/comment', this.heroID]);
  }
}
