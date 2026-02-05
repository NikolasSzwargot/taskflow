import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { ToastrService } from 'ngx-toastr';

import { AuthService } from '../auth/auth.service';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
    const auth = inject(AuthService);
    const router = inject(Router);
    const toast = inject(ToastrService);

    return next(req).pipe(
        catchError((err: HttpErrorResponse) => {

            if (err.status === 401) {
                auth.logout();
                router.navigate(['/login']);
                toast.error('Session expired. Please login again.');
            }

            return throwError(() => err);
        })
    );
};
