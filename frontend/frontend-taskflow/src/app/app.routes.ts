import { Routes } from '@angular/router';
import { BoardPageComponent } from './features/board/pages/board-page/board-page.component';
import { authGuard } from './core/auth/auth.guard';
import { LoginPageComponent } from './features/auth/pages/login-page/login-page.component';

export const routes: Routes = [
    { path: '', pathMatch: 'full', redirectTo: 'board' },
    { path: 'login', component: LoginPageComponent },
    { path: 'board', component: BoardPageComponent, canActivate: [authGuard] },
    { path: '**', redirectTo: 'board' },
];
