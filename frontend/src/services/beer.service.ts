import http from "../http-common";
import Beer from "../types/Beer";
import authHeader from "./auth.header";

const API_URL = 'https://beerapp-backend.onrender.com/beers';

class BeerService {
    getAll() {
        return http.get<Array<Beer>>(API_URL);
    }

    get(id: string) {
        return http.get<Beer>(API_URL + `/${id}`);
    }

    create(data: Beer) {
        return http.post<Beer>(API_URL, data, { headers: authHeader() });
    }

    update(data: Beer, id: any) {
        return http.put<Beer>(API_URL + `/${id}`, data, { headers: authHeader() });
    }

    delete(id: any) {
        return http.delete<any>(API_URL + `/${id}`, { headers: authHeader() });
    }

    getAllUserBeers(username: any) {
        return http.get<Array<Beer>>(API_URL + `/user/${username}`, { headers: authHeader() });
    }
}

export default new BeerService();
