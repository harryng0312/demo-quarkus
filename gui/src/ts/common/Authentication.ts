import type {RouteLocationNormalized} from "vue-router";
// import $ from "@types/jquery";
import $cookie from "@types/jquery.cookie";
import {getStore} from "@/stores/counter";
// import $ = require("jquery");

const COOKIE_TOKEN="token";

function isAuthenticated(to: RouteLocationNormalized, from: RouteLocationNormalized): boolean {
    let token = getStore().session.token;
    console.log(`from:[${from.path}] to:[${to.path}]`);
    return token !== "";
}

function loadTokenFromCookie(): string {
    let rs = $cookie(COOKIE_TOKEN);
    return rs;
}

function saveTokenToCookie(token: string): void {
    $cookie.cookie(COOKIE_TOKEN, token, <JQueryCookieOptions>{expires: 5/1440, secure: false})
}

export { isAuthenticated, loadTokenFromCookie, saveTokenToCookie };