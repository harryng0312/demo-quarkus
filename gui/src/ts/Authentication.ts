import {getTokenStore} from "@/stores/counter";
import type {RouteLocationNormalized} from "vue-router";

function isAuthenticated(to: RouteLocationNormalized, from: RouteLocationNormalized) {
    let token = getTokenStore().token;
    console.log(`from:[${from.path}] to:[${to.path}]`);
    return token !== "";
}

export { isAuthenticated };