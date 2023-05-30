import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SearchComponent } from './component/search/search.component';
import { ResultComponent } from './component/result/result.component';
import { DetailComponent } from './component/detail/detail.component';
import { CommentComponent } from './component/comment/comment.component';

const routes: Routes = [
  {
    path: '',
    component: SearchComponent,
    title: 'Search',
  },
  {
    path: 'result',
    component: ResultComponent,
    title: 'Results',
  },
  {
    path: 'detail/:id',
    component: DetailComponent,
  },
  {
    path: 'comment/:id',
    component: CommentComponent,
  },
  {
    path: '**',
    redirectTo: '/',
    pathMatch: 'full',
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true })],
  exports: [RouterModule],
})
export class AppRoutingModule {}
