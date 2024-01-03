import http from "../http-common";
import authHeader from "./auth.header";
import Rating from "../types/Rating";

const API_URL = 'https://beerapp-backend.onrender.com/ratings';
class RatingService {
    create(data: Rating, beerId: number) {
        return http.post<Rating>(API_URL + `/beer/${beerId}/add`, data, { headers: authHeader() });
    }
}

export default new RatingService();
