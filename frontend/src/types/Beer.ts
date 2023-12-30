export default interface Beer {
    id?: number,
    flavours: any[],
    beerType: any,
    manufacturer: any,
    country: any,
    name: string,
    description?: string,
    image: string, // link
    abv: number
    bitterness?: number,
    wortDensity?: number,
    lowerServeTemperature?: number,
    higherServeTemperature?: number,
    nutritionFacts?: any,
}
