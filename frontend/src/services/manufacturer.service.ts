import http from "../http-common";
import {Manufacturer} from "../types/Manufacturer";

const API_URL = 'http://localhost:8080/manufacturers';
class ManufacturerService {
    getAll() {
        return http.get<Array<Manufacturer>>(API_URL);
    }
}

export default new ManufacturerService();
