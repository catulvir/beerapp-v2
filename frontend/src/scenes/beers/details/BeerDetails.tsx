import {useEffect, useState} from "react";
import * as React from "react";
import {useParams} from "react-router-dom";
import BeerService from "../../../services/beer.service";
import Beer from "../../../types/Beer";
import {
    Accordion,
    AccordionDetails,
    AccordionSummary, Button,
    Card,
    CardContent, CardHeader,
    CardMedia,
    Chip,
    CircularProgress,
    Divider
} from "@mui/material";
import Typography from "@mui/material/Typography";
import '../beers.less';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import FaceIcon from "@mui/icons-material/Face";
import {AddRatingForm} from "./components/AddRatingForm";
import Rating from "../../../types/Rating";
import {FormikHelpers} from "formik";
import RatingService from "../../../services/rating.service";
import {priceCategories} from "../list/BeersList";
import User from "../../../types/identity/User";

interface BeerDetailsProps {
    user?: User;
}

export const BeerDetails = ({user}: BeerDetailsProps) => {
    const [isLoading, setIsLoading] = useState<boolean>(true);
    const [isAddComment, setIsAddComment] = useState<boolean>(false);
    const [beer, setBeer] = useState<Beer | undefined>(undefined);
    const [isError, setIsError] = useState<boolean>(false);

    const params = useParams()

    useEffect(() => {
        loadBeerDetails();
    }, [params]);

    const loadBeerDetails = () => {
        if (params.id) {
            BeerService.get(params.id).then(response => setBeer(response.data)).finally(() => setIsLoading(false))
        }
    }

    if (isLoading) {
        return <CircularProgress size="lg" />;
    }

    if (!beer || isError) {
        return (
            <Typography
                variant="body2"
                color="red"
                component="div"
            >
                Error on loading beer! Please try again.
            </Typography>
        );
    }

    const handleAddRating = (ratingForm: Rating, formikHelpers: FormikHelpers<Rating>) => {
        setIsLoading(true);
        RatingService.create(ratingForm, beer.id!!).then((response) => {
            if (response.status === 200) {
                loadBeerDetails();
            } else {
                setIsError(true);
            }
        }).finally(() => {
            setIsLoading(false);
            setIsAddComment(false);
        })
    };

    return (
        <div className="beer-details">
            <Card className="beer-details-card">
                <CardMedia
                    className="beer-image"
                    component="img"
                    alt={`${beer.name} image`}
                    image={beer.image}
                />
                <CardContent>
                    <Typography gutterBottom variant="h5" component="div">
                        {beer.name}
                    </Typography>
                    <Accordion className="beer-accordion">
                        <AccordionSummary
                            expandIcon={<ExpandMoreIcon />}
                            aria-controls="panel1a-content"
                            id="panel1a-header"
                        >
                            <Typography>Manufacturer: {beer.manufacturer?.name}</Typography>
                        </AccordionSummary>
                        <AccordionDetails>
                            <div>
                                {beer.manufacturer.image &&
                                    <CardMedia
                                        className="accordion-image"
                                        component="img"
                                        alt={`${beer.manufacturer?.name} image`}
                                        image={beer.manufacturer.image}
                                    />
                                }
                            </div>
                            <div>
                                {beer?.manufacturer?.description ?
                                    <Typography>{beer?.manufacturer?.description}</Typography>
                                    :
                                    <Typography>No description provided.</Typography>
                                }
                            </div>
                        </AccordionDetails>
                    </Accordion>
                    <Accordion className="beer-accordion">
                        <AccordionSummary
                            expandIcon={<ExpandMoreIcon />}
                            aria-controls="panel1a-content"
                            id="panel1a-header"
                        >
                            <Typography>Type: {beer.beerType?.name}</Typography>
                        </AccordionSummary>
                        <AccordionDetails>
                            <div>
                                {beer?.beerType?.description ?
                                    <Typography>{beer?.beerType?.description}</Typography>
                                    :
                                    <Typography>No description provided.</Typography>
                                }
                            </div>
                        </AccordionDetails>
                    </Accordion>
                    <Accordion className="beer-accordion">
                        <AccordionSummary
                            expandIcon={<ExpandMoreIcon />}
                            aria-controls="panel1a-content"
                            id="panel1a-header"
                        >
                            <Typography>Country: {beer.country?.name}</Typography>
                        </AccordionSummary>
                        <AccordionDetails>
                            <div>
                                {beer?.country?.description ?
                                    <Typography>{beer?.country?.description}</Typography>
                                    :
                                    <Typography>No description provided.</Typography>
                                }
                            </div>
                        </AccordionDetails>
                    </Accordion>
                    <Accordion className="beer-accordion">
                        <AccordionSummary
                            expandIcon={<ExpandMoreIcon />}
                            aria-controls="panel1a-content"
                            id="panel1a-header"
                        >
                            <Typography>Details:</Typography>
                        </AccordionSummary>
                        <AccordionDetails>
                            <div className="beer-details-list">
                                <Typography>ABV: {beer?.abv}%</Typography>
                                {!!beer.bitterness &&
                                    <Typography>Bitterness: {beer?.bitterness}%</Typography>
                                }
                                {!!beer.wortDensity &&
                                    <Typography>Wort density: {beer?.wortDensity}%</Typography>
                                }
                                {!!beer.lowerServeTemperature && beer.lowerServeTemperature > 0 &&
                                    <Typography>Best served at {beer?.lowerServeTemperature}°C - {beer?.higherServeTemperature}</Typography>
                                }
                                <div className="beer-flavours">
                                    {!!beer.priceCategory &&
                                        <Chip key="price-category" label={priceCategories.find((pc) => pc.id === beer.priceCategory)?.name || beer.priceCategory} color="warning" />
                                    }
                                    {beer.flavours.map((flavour, index) => (
                                        <Chip key={flavour.name + index} label={flavour.name} color="success" />
                                    ))}
                                </div>
                            </div>
                        </AccordionDetails>
                    </Accordion>
                    <Accordion className="beer-accordion">
                        <AccordionSummary
                            expandIcon={<ExpandMoreIcon />}
                            aria-controls="panel1a-content"
                            id="panel1a-header"
                        >
                            <Typography>Where to find?</Typography>
                        </AccordionSummary>
                        <AccordionDetails>
                            <div className="beer-stores">
                                {beer.stores.map((store) => (
                                    <Button color={"primary"} key={store.name} size={"large"} href={store.beerLink}>{store.name}</Button>
                                ))}
                            </div>
                        </AccordionDetails>
                    </Accordion>
                    <div className="beer-ratings">
                        {!!user &&
                            <Button color="info" variant="contained" key="addComment" size={"large"} onClick={() => setIsAddComment(!isAddComment)}>Add comment</Button>
                        }
                        {!!user && isAddComment &&
                            <AddRatingForm username={user?.username} onSubmit={handleAddRating} />
                        }
                        <Divider style={{marginTop: '15px'}} />
                        {beer.ratings.map((rating, index) => (
                            <Card key={rating.username + index} className={"beer-rating"}>
                                <CardHeader
                                    avatar={<FaceIcon />}
                                    title={rating.username}
                                    subheader={`Rating: ` + rating.rating}
                                />
                                <CardContent>
                                    <Typography variant="body2" color="text.primary">
                                        {rating.comment}
                                    </Typography>
                                    <div className="beer-flavours">
                                        {rating.flavours.map((flavour: string, index: number) => (
                                            <Chip key={flavour + index} label={flavour} color="success" />
                                        ))}
                                    </div>
                                </CardContent>
                            </Card>
                        ))}
                    </div>
                </CardContent>
            </Card>
        </div>
    );
}
