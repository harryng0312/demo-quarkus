import {createRouter, createWebHistory} from 'vue-router';
import type {Router} from "vue-router";
import type {NavigationGuardNext, RouteLocationNormalized} from "vue-router";
import {isAuthenticated} from "@/ts/common/Authentication";
import HomeView from '@/views/HomeView.vue';
import LoginView from '@/views/LoginView.vue';

const router: Router = createRouter({
    // history: createWebHistory(import.meta.env.BASE_URL),
    history: createWebHistory("/static"),
    routes: [
        {
            path: "/login",
            name: "login",
            component: LoginView
        }, {
            path: '/',
            name: 'home',
            component: HomeView,
        }, {
            path: '/about',
            name: 'about',
            // route level code-splitting
            // this generates a separate chunk (About.[hash].js) for this route
            // which is lazy-loaded when the route is visited.
            component: () => import('../views/AboutView.vue')
        }, {
            path: '/:pathMatch(.*)*',
            name: "notFound",
            component: () => import("../views/NotFound.vue")
        }
    ]
});
router.beforeEach((to: RouteLocationNormalized, from: RouteLocationNormalized, next: NavigationGuardNext) => {
    if (to.name !== "login" && !isAuthenticated(to, from)) {
        // load from cookie

        // if not - next to login
        next({name: "login"});
    } else {
        next();
    }
});

export default router;
