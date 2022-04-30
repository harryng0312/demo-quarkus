import type {DefineStoreOptions, StateTree, StoreDefinition} from "pinia";
import {defineStore} from 'pinia'
import {openWebsocket} from "@/ts/common/Communication";
import type {Client} from "stompjs";
// import {computed} from "vue";
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

class SessionState {
    private _token: string = "";
    private _username: string = "";

    constructor(payload: Required<SessionState>) {
        this._token = payload.token;
        this._username = payload.username;
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

class ConnectionState {
    private _webSocket: Client | null = null;

    public get webSocket(): Client {
        if (this._webSocket == null || !this._webSocket.connected) {
            this._webSocket = openWebsocket();
        }
        return this._webSocket;
    }

    public set webSocket(ws: Client) {
        this._webSocket = ws;
    }
}

interface StoredState {
    get session(): SessionState;

    get connection(): ConnectionState;
}

// class GetterState {
//     public session(state: StoredState): SessionState {
//         return state.session;
//     }
//
//     public connection(state: StoredState): ConnectionState {
//         console.log(`getConnection`);
//         return state.connection;
//     }
// }

interface GetterState {
    session(state: StoredState): SessionState;

    connection(state: StoredState): ConnectionState;
}

interface MethodState {
    printTest(): void;
}

// const getStore = <StoreDefinition<string, StoredState, GetterState, MethodState>>defineStore({
//     id: "token",
//     state: () => {
//         return {
//             session: new SessionState({
//                 token: "",
//                 username: ""
//             }),
//             connection: new ConnectionState(),
//         };
//     },
//     // getters: {},
//     // actions: {},
// });

const getStore = <StoreDefinition<string, StoredState, GetterState, MethodState>>defineStore({
    id: "main",
    state: () => ({
        session: new SessionState({
            token: "{}",
            username: "anonymous"
        }),
        connection: new ConnectionState(),
    }),
    actions: {
        printTest(): void {
            console.log(`print test ${this.session}`);
        }
    }

});

// const getStateless = () => {
//     return new StoredState({
//         session: new SessionState({
//             token: "",
//             username: "",
//         }),
//         connection: new ConnectionState(),
//     });
// };
// export {useCounterStore, getTokenStore};
export {getStore};