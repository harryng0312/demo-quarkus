import $ from "jquery";
// import $ = require("jquery");
import StompJs, {Client} from "stompjs"


export const SOCK_ENDPOINT = "ws://" + window.location.host + ":8080/ws";
export const SOCK_RESPONSE_ENDPOINT = SOCK_ENDPOINT + "/response";

function openWebsocket(path: string): void {

}

function sendAjax(url: string, method: string, data: unknown,
                  callback: { success: Parameters<any>, error: Parameters<any>, complete: Parameters<any> }): Promise<number> {
    let rs = new Promise<number>(() => {
        $.ajax(<JQuery.AjaxSettings>{
            url: url,
            method: method,
            data: data,
            success: callback.success,
            error: callback.error,
            complete: callback.complete,
        });
    });
    return rs;
}

function createWebsocket(): Client {
    let socket: WebSocket = new WebSocket(SOCK_ENDPOINT, ["ws", "wss"]);
    // let client = StompJs.over(socket);
    // let client = new WebSocket(SOCK_ENDPOINT);
    let client = StompJs.over(socket);
    return client;
}

export {openWebsocket, createWebsocket}