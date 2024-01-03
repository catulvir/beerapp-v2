import http from "../http-common";
import {Manufacturer} from "../types/Manufacturer";

const API_URL = 'https://beerapp-backend.onrender.com/manufacturers';
class ManufacturerService {
    getAll() {
        return http.get<Array<Manufacturer>>(API_URL);
    }
}

export default new ManufacturerService();
