import http from "../http-common";
import {BeerType} from "../types/BeerType";

const API_URL = 'http://localhost:8080/beerTypes';
class BeerTypeService {
    getAll() {
        return http.get<Array<BeerType>>(API_URL);
    }
}

export default new BeerTypeService();
