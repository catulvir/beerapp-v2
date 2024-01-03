import Rating from "./Rating";
import {Flavour} from "./Flavour";
import {Manufacturer} from "./Manufacturer";
import {BeerType} from "./BeerType";
import {Country} from "./Country";
import {Store} from "./Store";

export default interface Beer {
    id?: number,
    flavours: Flavour[],
    stores: Store[],
    ratings: Rating[],
    beerType: BeerType,
    manufacturer: Manufacturer,
    country: Country,
    name: string,
    description?: string,
    priceCategory?: string,
    image: string, // link
    abv: number
    bitterness?: number,
    wortDensity?: number,
    lowerServeTemperature?: number,
    higherServeTemperature?: number,
    nutritionFacts?: any,
}
