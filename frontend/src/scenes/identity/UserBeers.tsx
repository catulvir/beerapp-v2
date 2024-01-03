import User from "../../types/identity/User";
import {useEffect, useState} from "react";
import BeerService from "../../services/beer.service";
import Beer from "../../types/Beer";
import {Button, Chip, CircularProgress, Rating} from "@mui/material";
import * as React from "react";
import './identity.less';
import {DataGrid, GridColDef} from "@mui/x-data-grid";
import Typography from "@mui/material/Typography";
import {priceCategories} from "../beers/list/BeersList";

interface UserProps {
    user: User;
}

export const UserBeers = ({user}: UserProps) => {
    const [beers, setBeers] = useState<Beer[]>();
    const [isLoading, setIsLoading] = useState<boolean>(true);

    useEffect(() => {
        BeerService.getAllUserBeers(user.username).then(response => setBeers(response.data)).finally(() => setIsLoading(false))
    }, [user]);

    if (isLoading) {
        return <CircularProgress size="lg" />;
    }

    if (!beers) {
        return (
            <Typography
                variant="body2"
                color="red"
                component="div"
            >
                Error on loading your rated beers! Please try again.
            </Typography>
        );
    }

    function renderRating(params: any) {
        return <Rating readOnly value={params.value} />;
    }

    function renderFlavours(params: any) {
        return (
            params.value.map((flavour: string, index: number) => (
                <Chip style={{marginRight: '3px'}} key={flavour + index} label={flavour} color="success" />
            ))
        );
    }

    function renderPriceCategory(params: any) {
        return <Chip key="price-category" label={priceCategories.find((pc) => pc.id === params.value)?.name || params.value} color="warning" />
    }

    function renderDetailsButton(params: any) {
        return <Button key="beerDetails" variant="contained" size="small" href={`/beers/${params.value}`}>Details</Button>
    }

    const columns: GridColDef[] = [
        { field: 'beerName', headerName: 'Beer', width: 175 },
        { field: 'beerType', headerName: 'Beer type', width: 130 },
        { field: 'priceCategory', headerName: 'Price category', width: 140, renderCell: renderPriceCategory},
        { field: 'country', headerName: 'Country', width: 130 },
        { field: 'manufacturer', headerName: 'Manufacturer', width: 130 },
        {
            field: 'abv',
            headerName: 'ABV (%)',
            type: 'number',
            width: 120,
        },
        {
            field: 'rating',
            headerName: 'Rating',
            type: 'number',
            width: 125,
            renderCell: renderRating,
        },
        {
            field: 'comment',
            headerName: 'Comment',
            sortable: false,
            width: 250,
        },
        {
            field: 'flavours',
            headerName: 'Flavours',
            sortable: false,
            renderCell: renderFlavours,
            width: 250,
        },
        {
            field: 'details',
            headerName: '',
            sortable: false,
            renderCell: renderDetailsButton,
            width: 100,
        },
    ];

    return (
        <div className="profile-rated-beers">
            <DataGrid
                rows={beers?.map((beer) => ({
                    id: beer.id,
                    beerName: beer.name,
                    beerType: beer.beerType.name,
                    priceCategory: beer.priceCategory,
                    country: beer.country.name,
                    manufacturer: beer.manufacturer.name,
                    abv: beer.abv,
                    rating: beer.ratings[0].rating,
                    comment: beer.ratings[0].comment,
                    flavours: beer.ratings[0].flavours,
                    details: beer.id,
                }))}
                columns={columns}
                initialState={{
                    pagination: {
                        paginationModel: { page: 0, pageSize: 25 },
                    },
                }}
                pageSizeOptions={[25, 50, 100]}
            />
        </div>
    );
}
