import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { GetService } from 'src/app/service/get.service';
import { Observable, Subject, tap } from 'rxjs';
import { response, search } from 'src/app/models';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css'],
})
export class SearchComponent implements OnInit {
  form!: FormGroup;
  heroes$!: Observable<response>;

  constructor(
    private router: Router,
    private fb: FormBuilder,
    private getSvc: GetService
  ) {}

  ngOnInit(): void {
    this.form = this.createForm();
  }

  createForm(): FormGroup {
    return this.fb.group({
      heroName: this.fb.control('', [Validators.required]),
    });
  }

  searchHero(): void {
    // to pass to next page
    const name = this.form.value.heroName;
    this.getSvc.heroName = name;
    this.getSvc.result$ = this.getSvc.searchHeroes(name);
    this.router.navigate(['/result']);
  }

  // prev(): void {
  //   this.offset -= 20;
  //   this.heroes$ = this.getSvc
  //     .searchHeroes(this.form.value.heroName, this.offset)
  //     .pipe(
  //       tap((res) => (this.offset = res.data.offset))

  //     );
  // }

  // next(): void {
  //   this.offset += 20;
  //   this.heroes$ = this.getSvc
  //     .searchHeroes(this.form.value.heroName, this.offset)
  //     .pipe(
  //       tap((res) => (this.offset = res.data.offset)),
  //       tap((res) => console.log('next offset >>> ', this.offset))
  //     );
  // }
}
