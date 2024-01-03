import * as React from 'react';
import {useEffect, useState} from "react";
import Beer from "../../../types/Beer";
import BeerService from "../../../services/beer.service";
import {
    Autocomplete,
    Box,
    Button,
    Card,
    CardActions,
    CardContent,
    CardMedia, Checkbox,
    Chip,
    Container,
    FormControl, FormControlLabel,
    Grid,
    Slider,
    TextField
} from "@mui/material";
import Typography from "@mui/material/Typography";
import '../beers.less';
import BeerTypeService from "../../../services/beerType.service";
import CountryService from "../../../services/country.service";
import ManufacturerService from "../../../services/manufacturer.service";
import FlavourService from "../../../services/flavour.service";
import {BeerType} from "../../../types/BeerType";
import {Manufacturer} from "../../../types/Manufacturer";
import {Country} from "../../../types/Country";
// import {Flavour} from "../../../types/Flavour";

interface BeersListFilter {
    manufacturer: string | null;
    country: string | null;
    priceCategory: string | null;
    lessThanAbv: number;
    greaterThanAbv: number;
    beerType: string | null;
    alcoholFree: boolean | null;
}

export const priceCategories: {id: string, name: string}[] = [{id: 'cheap', name: 'cheap'}, {id: 'mass_produced', name: 'mass produced'}, {id: 'craft', name: 'craft'}]

export const BeersList = () => {
    const [beers, setBeers] = useState<Beer[]>();
    const [filteredBeers, setFilteredBeers] = useState<Beer[]>([]);
    const [beerTypes, setBeerTypes] = useState<BeerType[]>();
    const [manufacturers, setManufacturers] = useState<Manufacturer[]>();
    const [countries, setCountries] = useState<Country[]>();
    // const [flavours, setFlavours] = useState<Flavour[]>();

    const [filter, setFilter] = useState<BeersListFilter>({
        manufacturer: '',
        country: '',
        priceCategory: '',
        lessThanAbv: 20,
        greaterThanAbv: 0,
        beerType: '',
        alcoholFree: null
    });

    useEffect(() => {
        BeerService.getAll().then(response => {
            setBeers(response.data);
            setFilteredBeers(response.data);
        })
        BeerTypeService.getAll().then(response => setBeerTypes(response.data))
        CountryService.getAll().then(response => setCountries(response.data))
        ManufacturerService.getAll().then(response => setManufacturers(response.data))
        // FlavourService.getAll().then(response => setFlavours(response.data))
    }, []);

    if (!beers) {
        return <>Error on loading beers!</>;
    }

    const filterBeers = () => {
        let beersToBeFiltered = [...beers];
        if (!!filter.manufacturer) {
            beersToBeFiltered = beersToBeFiltered.filter((beer) => beer.manufacturer.name === filter.manufacturer);
        }
        if (!!filter.country) {
            beersToBeFiltered = beersToBeFiltered.filter((beer) => beer.country.name === filter.country);
        }
        if (!!filter.priceCategory) {
            beersToBeFiltered = beersToBeFiltered.filter((beer) => beer.priceCategory === filter.priceCategory);
        }
        if (filter.lessThanAbv !== null) {
            beersToBeFiltered = beersToBeFiltered.filter((beer) => beer.abv <= filter.lessThanAbv!!);
        }
        if (filter.greaterThanAbv !== null) {
            beersToBeFiltered = beersToBeFiltered.filter((beer) => beer.abv >= filter.greaterThanAbv!!);
        }
        if (!!filter.beerType) {
            beersToBeFiltered = beersToBeFiltered.filter((beer) => beer.beerType.name === filter.beerType);
        }
        if (filter.alcoholFree !== null) {
            beersToBeFiltered = beersToBeFiltered.filter((beer) => filter.alcoholFree === true ? (beer.abv === 0) : (beer.abv > 0));
        }
        setFilteredBeers(beersToBeFiltered);
    }

    return (
        <Container>
            <div className="beers-filters">
                <Box>
                    <FormControl>
                        <Autocomplete
                            id="beer-type-filter"
                            className="beer-type-filter"
                            options={beerTypes || []}
                            autoHighlight
                            getOptionLabel={(option) => option.name}
                            renderOption={(props, option) => (
                                <Box component="li" {...props}>
                                    {option.name}
                                </Box>
                            )}
                            onChange={(event, value) => {
                                setFilter({
                                    beerType: !!value ? value.name : null,
                                    manufacturer: filter.manufacturer,
                                    country: filter.country,
                                    priceCategory: filter.priceCategory,
                                    lessThanAbv: filter.lessThanAbv,
                                    greaterThanAbv: filter.greaterThanAbv,
                                    alcoholFree: filter.alcoholFree
                                });
                            }}
                            renderInput={(params) => (
                                <TextField
                                    {...params}
                                    label="Beer type"
                                    inputProps={{
                                        ...params.inputProps,
                                    }}
                                />
                            )}
                        />
                    </FormControl>
                </Box>
                <Box>
                    <FormControl>
                        <Autocomplete
                            id="beer-price-category"
                            className="beer-price-category-filter"
                            options={priceCategories}
                            autoHighlight
                            getOptionLabel={(option) => option.name}
                            renderOption={(props, option) => (
                                <Box component="li" {...props}>
                                    {option.name}
                                </Box>
                            )}
                            onChange={(event, value) => {
                                setFilter({
                                    beerType: filter.beerType,
                                    manufacturer: filter.manufacturer,
                                    country: filter.country,
                                    priceCategory: !!value ? value.id : null,
                                    lessThanAbv: filter.lessThanAbv,
                                    greaterThanAbv: filter.greaterThanAbv,
                                    alcoholFree: filter.alcoholFree
                                });
                            }}
                            renderInput={(params) => (
                                <TextField
                                    {...params}
                                    label="Price category"
                                    inputProps={{
                                        ...params.inputProps,
                                    }}
                                />
                            )}
                        />
                    </FormControl>
                </Box>
                <Box>
                    <FormControl>
                        <Autocomplete
                            id="beer-country"
                            className="beer-country-filter"
                            options={countries || []}
                            autoHighlight
                            getOptionLabel={(option) => option.name}
                            renderOption={(props, option) => (
                                <Box component="li" {...props}>
                                    {option.name}
                                </Box>
                            )}
                            onChange={(event, value) => {
                                setFilter({
                                    beerType: filter.beerType,
                                    manufacturer: filter.manufacturer,
                                    country: !!value ? value.name : null,
                                    priceCategory: filter.priceCategory,
                                    lessThanAbv: filter.lessThanAbv,
                                    greaterThanAbv: filter.greaterThanAbv,
                                    alcoholFree: filter.alcoholFree
                                });
                            }}
                            renderInput={(params) => (
                                <TextField
                                    {...params}
                                    label="Country"
                                    inputProps={{
                                        ...params.inputProps,
                                    }}
                                />
                            )}
                        />
                    </FormControl>
                </Box>
                <Box>
                    <FormControl>
                        <Autocomplete
                            id="beer-manufacturer"
                            className="beer-manufacturer-filter"
                            options={manufacturers || []}
                            autoHighlight
                            getOptionLabel={(option) => option.name}
                            renderOption={(props, option) => (
                                <Box component="li" {...props}>
                                    {option.name}
                                </Box>
                            )}
                            onChange={(event, value) => {
                                setFilter({
                                    beerType: filter.beerType,
                                    manufacturer: !!value ? value.name : null,
                                    country: filter.country,
                                    priceCategory: filter.priceCategory,
                                    lessThanAbv: filter.lessThanAbv,
                                    greaterThanAbv: filter.greaterThanAbv,
                                    alcoholFree: filter.alcoholFree
                                });
                            }}
                            renderInput={(params) => (
                                <TextField
                                    {...params}
                                    label="Manufacturer"
                                    inputProps={{
                                        ...params.inputProps,
                                    }}
                                />
                            )}
                        />
                    </FormControl>
                </Box>
                <Box>
                    <FormControl className="alco-free-filter">
                        <FormControlLabel
                            value="alco-free"
                            control={<Checkbox
                                checked={!!filter.alcoholFree}
                                onChange={(event, value) => {
                                    setFilter({
                                        beerType: filter.beerType,
                                        manufacturer: filter.manufacturer,
                                        country: filter.country,
                                        priceCategory: filter.priceCategory,
                                        lessThanAbv: filter.lessThanAbv,
                                        greaterThanAbv: filter.greaterThanAbv,
                                        alcoholFree: value ? true : null
                                    });
                                }}
                            />}
                            label="Alcohol free?"
                            labelPlacement="start"
                        />
                    </FormControl>
                </Box>
                {!filter.alcoholFree &&
                    <Box>
                        <FormControl className="abv-slider">
                            <Typography id="abv-input-slider" gutterBottom>
                                ABV (%)
                            </Typography>
                            <Slider
                                getAriaLabel={() => 'ABV % range'}
                                value={[filter.lessThanAbv, filter.greaterThanAbv]}
                                onChange={(event, value) => {
                                    const values = value as number[];
                                    setFilter({
                                        beerType: filter.beerType,
                                        manufacturer: filter.manufacturer,
                                        country: filter.country,
                                        priceCategory: filter.priceCategory,
                                        lessThanAbv: values[1],
                                        greaterThanAbv: values[0],
                                        alcoholFree: filter.alcoholFree,
                                    });
                                }}
                                valueLabelDisplay="auto"
                                step={1}
                                min={0}
                                max={20}
                            />
                        </FormControl>
                    </Box>
                }
                <Button className="filter-button" color="primary" variant="contained" key="filterBeers" size={"large"} onClick={() => filterBeers()}>Filter</Button>
            </div>
            <Grid className="beers-grid" container spacing={{ xs: 2, md: 3 }} columns={{ xs: 4, sm: 9, md: 12, lg: 18 }}>
                {filteredBeers.map((beer, index) => (
                    <Grid className="beer-grid" item xs={2} sm={3} md={3} lg={3} key={index}>
                        <Card className="beer-card">
                            <CardMedia
                                className="beer-image"
                                component="img"
                                alt={`${beer.name} image`}
                                image={beer.image}
                            />
                            <CardContent className="card-content">
                                <div>
                                    <Typography gutterBottom variant="h5" component="div">
                                        {beer.name}
                                    </Typography>
                                </div>
                                <div>
                                    <Typography sx={{ mb: 1.5 }} color="text.secondary">
                                        {beer.manufacturer.name}, {beer.abv}%
                                    </Typography>
                                </div>
                                <div>
                                    <Typography variant="body2" color="text.secondary">
                                        {beer.beerType?.name}, {beer.country?.name}
                                    </Typography>
                                </div>
                                <div className="beer-flavours">
                                    {!!beer.priceCategory &&
                                        <Chip key="price-category" label={priceCategories.find((pc) => pc.id === beer.priceCategory)?.name || beer.priceCategory} color="warning" />
                                    }
                                    {beer.flavours.map((flavour, index) => (
                                        <Chip key={flavour.name + index} label={flavour.name} color="success" />
                                    ))}
                                </div>
                            </CardContent>
                            <CardActions className="card-actions">
                                <Button key="beerDetails" size="small" href={`/beers/${beer.id}`}>Details</Button>
                            </CardActions>
                        </Card>
                    </Grid>
                ))}
            </Grid>
        </Container>
    );
}
