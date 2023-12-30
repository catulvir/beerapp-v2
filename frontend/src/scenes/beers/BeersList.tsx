import * as React from 'react';
import {useEffect, useState} from "react";
import Beer from "../../types/Beer";
import BeerService from "../../services/beer.service";
import {Button, Card, CardActions, CardContent, CardMedia, Chip, Grid} from "@mui/material";
import Typography from "@mui/material/Typography";
import './beers.less';

export const BeersList = () => {
    const [beers, setBeers] = useState<Beer[]>();

    useEffect(() => {
        BeerService.getAll().then(beers => setBeers(beers.data))
    }, []);

    if (!beers) {
        return <>Error on loading beers!</>;
    }

    return (
        <Grid className="beers-grid" container spacing={{ xs: 2, md: 3 }} columns={{ xs: 4, sm: 8, md: 12 }}>
            {beers.map((beer, index) => (
                <Grid className="beer-grid" item xs={2} sm={4} md={4} key={index}>
                    <Card className="beer-card">
                        <CardMedia
                            className="beer-image"
                            component="img"
                            alt={`${beer.name} image`}
                            image={beer.image}
                        />
                        <CardContent>
                            <Typography gutterBottom variant="h5" component="div">
                                {beer.name} {beer.abv}%
                            </Typography>
                            <Typography sx={{ mb: 1.5 }} color="text.secondary">
                                {beer.manufacturer.name}
                            </Typography>
                            <Typography variant="body2" color="text.secondary">
                                {beer.beerType?.name}, {beer.country?.name}
                            </Typography>
                            <div>
                                {beer.flavours.map((flavour) => (
                                    <Chip label={flavour.name} color="success" />
                                ))}
                            </div>
                        </CardContent>
                        <CardActions>
                            <Button size="small">Details</Button>
                            <Button size="small">Learn More?</Button>
                        </CardActions>
                    </Card>
                </Grid>
            ))}
        </Grid>
    );
}
