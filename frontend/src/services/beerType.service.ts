import http from "../http-common";
import {BeerType} from "../types/BeerType";

const API_URL = 'https://beerapp-backend.onrender.com/beerTypes';
class BeerTypeService {
    getAll() {
        return http.get<Array<BeerType>>(API_URL);
    }
}

export default new BeerTypeService();
