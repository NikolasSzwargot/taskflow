import { inject } from "@angular/core";
import { CanActivateFn } from "@angular/router";
import { TokenStorage } from "./token.storage";
import { Router } from "@angular/router";

export const authGuard: CanActivateFn = () => {
    const token = inject(TokenStorage).get();
    const router = inject(Router);

    if (token) {
        return true;
    }

    router.navigate(['/login']);
    return false;
}