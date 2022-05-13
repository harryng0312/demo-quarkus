<script lang="ts">
import {defineComponent} from "vue";
import {getStore} from "@/stores";
import {SOCK_RESPONSE_ENDPOINT} from "@/ts/common/Communication";
import type {Client} from "stompjs";

export default defineComponent({
  data() {
    return {
      username: "",
      password: ""
    };
  },
  methods: {
    login(evt: Event) {
      console.log(`Stack size: ${this.$router.getRoutes().length}`);
      let socket: Client = getStore().connection.webSocket;
      // let socket: Client = getStateless().connection.webSocket;
      if (!socket.connected) {
        socket.connect({}, frame => {
          socket.subscribe(SOCK_RESPONSE_ENDPOINT, (msg) => {
            console.log(`Response:${JSON.parse(msg.body)}`);
          });
          console.log(`Connected:${frame}`);
          socket.disconnect(() => {
            console.log(`Socket disconnected`);
          });
        }, error => {
          socket.disconnect(() => {
            console.log(`Socket disconnected`);
          });
          console.log(`Error:${error}`);
        });
      }
      if (this.username !== "") {
        // let socket = createWebsocket();
        // this.$router.push("/");
      }
    },
  },
});
</script>

<template>
  <div class="center">
    <form id="authForm" method="post" action="login" onsubmit="return false;">
      <table style="border: 0;">
        <thead>
        <tr>
          <td v-text="$t(`login.title`)"></td>
        </tr>
        </thead>
        <tbody>
        <tr>
          <td v-text="$t('login.username')"></td>
        </tr>
        <tr>
          <td><input id="txtUsername" name="txtUsername" v-model="username"
                     :placeholder="$t('login.username')" autocomplete="off"/>
          </td>
        </tr>
        <tr>
          <td v-text="$t('login.password')"></td>
        </tr>
        <tr>
          <td><input type="password" id="txtPassword" name="txtPassword" v-model="password"
                     :placeholder="$t('login.password')" autocomplete="off"/></td>
        </tr>
        <tr>
          <td>&nbsp;</td>
        </tr>
        </tbody>
        <tfoot>
        <tr>
          <td>
            <input type="submit" id="submit" :value="$t('login.submit')" @click="login"/>
            <input id="tokenId" name="tokenId" type="hidden"/>
          </td>
        </tr>
        </tfoot>
      </table>
    </form>
  </div>
</template>

<style>
@media (min-width: 1024px) {
  .about {
    min-height: 100vh;
    display: flex;
    align-items: center;
  }
}
</style>
