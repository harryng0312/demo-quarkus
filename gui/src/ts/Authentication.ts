import {getTokenStore} from "@/stores/counter";
import type {RouteLocationNormalized} from "vue-router";

function isAuthenticated(to: RouteLocationNormalized, from: RouteLocationNormalized): boolean {
    let token = getTokenStore().token;
    console.log(`from:[${from.path}] to:[${to.path}]`);
    return token !== "";
}

export { isAuthenticated };