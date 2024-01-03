import {ErrorMessage, Form, Formik, FormikHelpers, FormikProps} from "formik";
import * as Yup from "yup";
import * as React from "react";
import Rating from "../../../../types/Rating";
import {
    Box,
    Button,
    Chip,
    FormControl,
    InputLabel,
    OutlinedInput,
    Rating as RatingField,
    Select,
    TextField
} from "@mui/material";
import {useEffect, useState} from "react";
import {Flavour} from "../../../../types/Flavour";
import FlavourService from "../../../../services/flavour.service";
import MenuItem from "@mui/material/MenuItem";
import Typography from "@mui/material/Typography";

interface AddRatingFormProps {
    username: string;
    onSubmit: (ratingForm: Rating, formikHelpers: FormikHelpers<Rating>) => void;
}

export const AddRatingForm = ({username, onSubmit}: AddRatingFormProps) => {
    const [flavours, setFlavours] = useState<Flavour[] | undefined>(undefined);

    useEffect(() => {
        FlavourService.getAll().then(response => setFlavours(response.data))
    }, [username]);

    const validationSchema = Yup.object().shape({
        rating: Yup.number().required('Please leave your rating as well!').min(0, 'Rating must be greater than 0.1 stars!'),
        comment: Yup.string().nullable(),
        flavours: Yup.array().of(Yup.string()).required().min(1, 'Please choose some flavours.'),
    });

    if (!flavours) {
        return (
            <Typography
                variant="body2"
                color="red"
                component="div"
            >
                Cannot add ratings at the moments!
            </Typography>
        );
    }

    return (
        <>
            <Formik
                initialValues={{comment: '', username: username, rating: -1, flavours: [] as string[]}}
                onSubmit={onSubmit}
                validationSchema={validationSchema}
                validateOnBlur={true}
                validateOnChange={false}
            >
                {(formikProps: FormikProps<Rating>) =>
                    <Form className="add-rating-form">
                        <div className="form-group">
                            <FormControl>
                                <div className="rating-container">
                                    <RatingField
                                        precision={0.1}
                                        size="large"
                                        name="rating"
                                        value={formikProps.values.rating}
                                        onChange={(event: any, newValue: number | null) => formikProps.setFieldValue('rating', newValue ? newValue : 0)}
                                    />
                                    {formikProps.values.rating > 0 && (
                                        <Box sx={{ ml: 2 }}>{formikProps.values.rating}</Box>
                                    )}
                                </div>
                                <ErrorMessage
                                    name="rating"
                                    component="div"
                                    className="alert"
                                />
                            </FormControl>
                        </div>
                        <div className="form-group">
                            <FormControl>
                                <TextField
                                    id="comment"
                                    label="Comment"
                                    multiline
                                    rows={5}
                                    name="comment"
                                    variant="outlined"
                                    value={formikProps.values.comment}
                                    onChange={(event: React.ChangeEvent<HTMLInputElement>) => formikProps.setFieldValue('comment', event.target.value)}
                                />
                            </FormControl>
                        </div>
                        <div className="form-group">
                            <FormControl>
                                <InputLabel id="flavours-label">Flavours</InputLabel>
                                <Select
                                    labelId="flavours-label"
                                    id="flavours"
                                    multiple
                                    value={formikProps.values.flavours}
                                    input={<OutlinedInput id="select-multiple-chip" label="Flavours" />}
                                    onChange={(event) => formikProps.setFieldValue('flavours', event.target.value)}
                                    renderValue={(selected) => (
                                        <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 0.5 }}>
                                            {selected.map((value) => (
                                                <Chip key={value} label={value} color="success" />
                                            ))}
                                        </Box>
                                    )}
                                >
                                    {flavours.map((flavour) => (
                                        <MenuItem
                                            key={flavour.id}
                                            value={flavour.name}
                                        >
                                            {flavour.name}
                                        </MenuItem>
                                    ))}
                                </Select>
                                <ErrorMessage
                                    name="flavours"
                                    component="div"
                                    className="alert"
                                />
                            </FormControl>
                        </div>
                        <Button color="primary" type="submit" variant="contained" key="submit" size="large">Submit</Button>
                    </Form>
                }
            </Formik>
        </>
    );
}
