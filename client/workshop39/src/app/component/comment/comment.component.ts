import { Component, OnInit, OnDestroy } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute } from '@angular/router';
import { Observable, Subscription, tap } from 'rxjs';
import { Router } from '@angular/router';
import { GetService } from 'src/app/service/get.service';
import { hero } from 'src/app/models';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PostService } from 'src/app/service/post.service';

@Component({
  selector: 'app-comment',
  templateUrl: './comment.component.html',
  styleUrls: ['./comment.component.css'],
})
export class CommentComponent implements OnInit, OnDestroy {
  hero!: hero;
  heroSubscription$!: Subscription;
  form!: FormGroup;
  postSubscription$!: Subscription;

  constructor(
    private title: Title,
    private getSvc: GetService,
    private postSvc: PostService,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}
  ngOnInit(): void {
    this.form = this.createForm();

    this.heroSubscription$ = this.getSvc
      .searchHeroById(this.activatedRoute.snapshot.params['id'])
      .subscribe((hero) => {
        this.hero = hero;
        this.title.setTitle(`Comment | ${hero.name} `);
      });
  }

  ngOnDestroy(): void {
    if (this.heroSubscription$) this.heroSubscription$.unsubscribe();
    if (this.postSubscription$) this.postSubscription$.unsubscribe();
  }

  createForm(): FormGroup {
    return this.fb.group({
      comments: this.fb.control('', [
        Validators.required,
        Validators.minLength(3),
      ]),
    });
  }

  back() {
    this.router.navigate(['/detail', this.hero.id]);
  }

  post() {
    console.log('Posted >>> ', this.form.value.comments);
    const comments: string = this.form.value.comments;
    this.postSubscription$ = this.postSvc
      .postComments(this.hero.id, comments)
      .subscribe();
    this.back();
  }
}
