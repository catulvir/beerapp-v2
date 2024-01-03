import http from "../http-common";
import {Flavour} from "../types/Flavour";

const API_URL = 'http://localhost:8080/flavours';
class FlavourService {
    getAll() {
        return http.get<Array<Flavour>>(API_URL);
    }
}

export default new FlavourService();
