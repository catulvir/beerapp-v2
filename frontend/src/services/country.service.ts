import http from "../http-common";
import {Country} from "../types/Country";

const API_URL = 'https://beerapp-backend.onrender.com/countries';
class CountryService {
    getAll() {
        return http.get<Array<Country>>(API_URL);
    }
}

export default new CountryService();
