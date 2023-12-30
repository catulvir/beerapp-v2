import axios from "axios";
import User from "../types/identity/User";

const API_URL = "http://localhost:8080/user/";

class AuthService {
    login(username: string, password: string) {
        return axios
            .post(API_URL + "authorize", {
                username,
                password
            })
            .then(response => {
                if (response.data.accessToken) {
                    localStorage.setItem("user", JSON.stringify(response.data));
                }

                return response.data;
            });
    }

    logout() {
        localStorage.removeItem("user");
    }

    register(username: string, email: string, password: string, matchingPassword: string) {
        return axios.post(API_URL + "registration", {
            username,
            password,
            matchingPassword,
            email,
        });
    }

    getCurrentUser() {
        const userStr = localStorage.getItem("user");
        if (userStr) return JSON.parse(userStr) as User;

        return null;
    }
}

export default new AuthService();
