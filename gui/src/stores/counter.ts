import {defineStore} from 'pinia'
import type {StoreDefinition} from "pinia";
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
    private token: string;

    constructor(token: string) {
        this.token = token;
    }

    public getToken(): string {
        return this.token;
    }
}

const getTokenStore: StoreDefinition = defineStore<string, SessionState>({
    id: "token",
    state: () => new SessionState(""),
    getters: {
        token: (that) => that.getToken(),
    },
    actions: {}
});
// export {useCounterStore, getTokenStore};
export {getTokenStore};