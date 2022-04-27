import $ from "@types/jquery";
import StompJs from "@types/stompjs"
import SockJS from "sockjs-client";
import {Client} from "stompjs";
// import $ = require("jquery");

declare const SOCK_ENDPOINT = "/ws";
function openWebsocket(path: string): void {

}

function sendAjax(url: string, method: string, data: unknown,
                  callback: { success: Parameters<unknown>, error: Parameters<unknown>, complete: Parameters<unknown> }): Promise<void> {
    let ajax = new Promise<unknown>.resolve()
        .then();
    $.ajax(<JQuery.AjaxSettings>{
        url: url,
        method: method,
        data: data,
        success: callback.success,
        error: callback.error,
        complete: callback.complete,
    });
    return ajax;
}

function createWebsocket(): Client {
    let socket: WebSocket = new SockJS(SOCK_ENDPOINT).prototype;
    let client = StompJs.over(socket);
    return client;
}

export {openWebsocket, createWebsocket}