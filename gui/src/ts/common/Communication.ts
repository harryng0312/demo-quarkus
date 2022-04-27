import $ from "jquery";
// import $ = require("jquery");
import StompJs, {Client} from "stompjs"


export const SOCK_ENDPOINT = "ws://" + window.location.hostname + ":8080/ws";
export const SOCK_RESPONSE_ENDPOINT = SOCK_ENDPOINT + "/response";

function sendAjax(url: string, method: string, data: unknown,
                  callback: { success: unknown, error?: unknown, complete?: unknown }): Promise<void> {
    let rs = new Promise<void>(() => {
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

function openWebsocket(): Client {
    console.log(`open websocket`);
    let socket: WebSocket = new WebSocket(SOCK_ENDPOINT, ["ws", "wss"]);
    // let client = StompJs.over(socket);
    // let client = new WebSocket(SOCK_ENDPOINT);
    let client = StompJs.over(socket);
    client.debug = () => {};
    return client;
}

export {openWebsocket}