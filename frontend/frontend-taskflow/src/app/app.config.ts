import { ApplicationConfig, provideBrowserGlobalErrorListeners } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { routes } from './app.routes';
import { authInterceptor } from './core/auth/auth.interceptor';
import { errorInterceptor } from './core/http/error.interceptor';
import { provideToastr } from 'ngx-toastr';


export const appConfig: ApplicationConfig = {
  providers: [
    provideToastr({
      positionClass: 'toast-top-right',
      timeOut: 3000,
      preventDuplicates: true,
      closeButton: true,
      tapToDismiss: true,
      progressBar: true,
      newestOnTop: true,
    }),
    provideBrowserGlobalErrorListeners(),
    provideRouter(routes), provideHttpClient(withInterceptors([authInterceptor, errorInterceptor])),
  ]
};
