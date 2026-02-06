import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { TokenStorage } from './token.storage';
import { getRealmRolesFromJwt } from './auth.roles';

export const adminGuard: CanActivateFn = () => {
    const router = inject(Router);
    const tokenStorage = inject(TokenStorage);

    const token = tokenStorage.get();
    if (!token) {
        router.navigate(['/login']);
        return false;
    }

    const roles = getRealmRolesFromJwt(token);
    if (!roles.includes('ADMIN')) {
        router.navigate(['/board']);
        return false;
    }

    return true;
};
