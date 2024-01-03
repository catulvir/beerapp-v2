import axios from "axios";

export default axios.create({
    baseURL: "https://beerapp-backend.onrender.com",
    headers: {
        "Content-type": "application/json"
    }
});
