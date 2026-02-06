export function getRealmRolesFromJwt(token: string): string[] {
    try {
        const payload = JSON.parse(atob(token.split('.')[1]));
        return payload?.realm_access?.roles ?? [];
    } catch {
        return [];
    }
}
