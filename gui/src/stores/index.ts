import type {StoreDefinition} from "pinia";
import {defineStore} from 'pinia'
import {openWebsocket} from "@/ts/common/Communication";
import type {Client} from "stompjs";
import {computed} from "vue";
// const useCounterStore = defineStore({
//   id: 'counter',
//   state: () => ({
//     counter: 0
//   }),
//   getters: {
//     doubleCount: (state) => state.counter * 2
//   },
//   actions: {
//     increment() {
//       this.counter++
//     }
//   }
// });

export class SessionState {
    private _token: string = "";
    private _username: string = "";

    constructor(ss: { token: string, username: string }) {
        this._token = ss.token;
        this._username = ss.username;
    }

    get token(): string {
        return this._token;
    }

    set token(value: string) {
        this._token = value;
    }

    get username(): string {
        return this._username;
    }

    set username(value: string) {
        this._username = value;
    }
}

export class ConnectionState {
    private _webSocket: Client | null = null;

    public get webSocket(): Client {
        if(this._webSocket == null || !this._webSocket.connected){
            this._webSocket = openWebsocket();
        }
        return this._webSocket;
    }

    public set webSocket(ws: Client) {
        this._webSocket = ws;
    }
}

export class StoredState {
    private readonly _session: SessionState;
    private readonly _connection: ConnectionState;

    constructor(ss: { session: SessionState, connection: ConnectionState }) {
        this._session = ss.session;
        this._connection = ss.connection;
    }

    get session(): SessionState {
        return this._session;
    }

    get connection(): ConnectionState {
        return this._connection;
    }
}

const getStore = <StoreDefinition<string, StoredState>>defineStore({
    id: "token",
    state: () => <StoredState>{
        session: <SessionState>{
            token: "",
            username: ""
        },
        connection: new ConnectionState(),
    },
    actions: {}
});
// export {useCounterStore, getTokenStore};
export {getStore};