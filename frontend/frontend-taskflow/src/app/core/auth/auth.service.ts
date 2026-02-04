import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { TokenStorage } from "./token.storage";
import { environment } from "../../../enviroments/enviroment.development";
import { map, Observable } from "rxjs";

type TokenResponse = {
    access_token: string;
    expires_in?: number;
    refresh_token?: string;
    token_type?: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
    constructor(private http: HttpClient, private tokenStorage: TokenStorage) { }

    login(username: string, password: string): Observable<string> {
        const url =
            `${environment.keycloakBaseUrl}/realms/${environment.keycloakRealm}/protocol/openid-connect/token`;

        const body = new HttpParams()
            .set('grant_type', 'password')
            .set('client_id', environment.keycloakClientId)
            .set('username', username)
            .set('password', password);

        const headers = new HttpHeaders({
            'Content-Type': 'application/x-www-form-urlencoded',
        });

        return this.http.post<TokenResponse>(url, body.toString(), { headers }).pipe(
            map((res) => {
                this.tokenStorage.set(res.access_token);
                return res.access_token;
            })
        );
    }

    logout() {
        this.tokenStorage.clear();
    }

}