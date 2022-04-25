import {getStore} from "@/stores/counter";
import type {RouteLocationNormalized} from "vue-router";

function isAuthenticated(to: RouteLocationNormalized, from: RouteLocationNormalized): boolean {
    let token = getStore().session.token;
    console.log(`from:[${from.path}] to:[${to.path}]`);
    return token !== "";
}

export { isAuthenticated };