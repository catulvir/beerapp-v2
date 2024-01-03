import http from "../http-common";
import {Country} from "../types/Country";

const API_URL = 'http://localhost:8080/countries';
class CountryService {
    getAll() {
        return http.get<Array<Country>>(API_URL);
    }
}

export default new CountryService();
