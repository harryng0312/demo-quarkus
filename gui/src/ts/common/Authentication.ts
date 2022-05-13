import type {RouteLocationNormalized} from "vue-router";
import type {CookieOptions} from "cookie-storage/lib/cookie-options";
import {CookieStorage} from 'cookie-storage';
import {getStore} from "@/stores";

// import $ = require("jquery");

const COOKIE_TOKEN = "token";
const COOKIE_TIMEOUT = 2 * 60 * 1000;
const cookieStorage = new CookieStorage(<CookieOptions>{
    sameSite: "Strict"
});

function isAuthenticated(to: RouteLocationNormalized, from: RouteLocationNormalized): boolean {
    let token = getStore().session.token;
    // let token = getStateless().session.token;
    console.log(`from:[${from.path}] to:[${to.path}]`);
    return token !== "";
}

function loadTokenFromCookie(): string {
    return <string>cookieStorage.getItem(COOKIE_TOKEN);
}

function saveTokenToCookie(token: string): void {
    let expiredDate = new Date();
    expiredDate.setDate(expiredDate.getTime() + COOKIE_TIMEOUT);
    cookieStorage.setItem(COOKIE_TOKEN, token, <CookieOptions>{expires: expiredDate, secure: false});
}

export {isAuthenticated, loadTokenFromCookie, saveTokenToCookie};