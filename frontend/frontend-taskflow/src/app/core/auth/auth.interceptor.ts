import { HttpInterceptorFn } from "@angular/common/http";
import { TokenStorage } from "./token.storage";
import { inject } from "@angular/core";
import { environment } from "../../../enviroments/enviroment.development";

export const authInterceptor: HttpInterceptorFn = (req, next) => {
    const token = inject(TokenStorage).get();

    const isApiCall = - req.url.startsWith(environment.apiBaseUrl);

    if (!token || !isApiCall) {
        return next(req);
    }

    return next(req.clone({
        setHeaders: {
            Authorization: `Bearer ${token}`
        },
    }
    ));
}