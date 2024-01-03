import http from "../http-common";
import {Flavour} from "../types/Flavour";

const API_URL = 'https://beerapp-backend.onrender.com/flavours';
class FlavourService {
    getAll() {
        return http.get<Array<Flavour>>(API_URL);
    }
}

export default new FlavourService();
