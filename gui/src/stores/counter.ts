import type {StoreDefinition} from "pinia";
import {defineStore} from 'pinia'
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

export class StoredState {
    private readonly _session: SessionState;

    constructor(ss: { session: SessionState }
    ) {
        this._session = ss.session;
    }

    get session(): SessionState {
        return this._session;
    }
}

const getStore: StoreDefinition<string, StoredState> = defineStore({
    id: "token",
    state: () => <StoredState>{
        session: <SessionState>{
            token: "",
            username: ""
        },
    },
    actions: {}
});
// export {useCounterStore, getTokenStore};
export {getStore};