import { Injectable } from "@angular/core";

@Injectable({
    providedIn: 'root'
})

export class TokenStorage {
    private readonly TOKEN_KEY = 'taskflow_token';

    get(): string | null {
        return localStorage.getItem(this.TOKEN_KEY);
    }

    set(token: string): void {
        localStorage.setItem(this.TOKEN_KEY, token);
    }

    clear(): void {
        localStorage.removeItem(this.TOKEN_KEY);
    }
}
