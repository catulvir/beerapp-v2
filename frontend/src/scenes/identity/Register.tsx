import React, {useState} from "react";
import {Formik, Form, ErrorMessage, FormikProps} from "formik";
import * as Yup from "yup";
import AuthService from "../../services/auth.service";
import {
    Alert,
    Button,
    Container,
    FormControl, IconButton,
    InputAdornment,
    InputLabel,
    OutlinedInput,
    TextField
} from "@mui/material";
import {Visibility, VisibilityOff} from "@mui/icons-material";

const Register: React.FC = () => {
    const [showPassword, setShowPassword] = useState<boolean>(false);
    const [successful, setSuccessful] = useState<boolean>(false);
    const [message, setMessage] = useState<string>("");

    const initialValues = {
        username: "",
        password: "",
        matchingPassword: "",
        email: "",
    };

    const validationSchema = Yup.object().shape({
            username: Yup.string()
                .test(
                    "len",
                    "The username must be between 4 and 63 characters.",
                    (val: any) =>
                        val &&
                        val.toString().length >= 4 &&
                        val.toString().length <= 63
                )
                .required("This field is required!"),
            email: Yup.string()
                .email("This is not a valid email.")
                .required("This field is required!")
                .min(4, 'Email must be at least 4 characters long.'),
            password: Yup.string()
                .test(
                    "len",
                    "The password must be between 8 and 32 characters.",
                    (val: any) =>
                        val &&
                        val.toString().length >= 8 &&
                        val.toString().length <= 32
                )
                .required("This field is required!"),
            matchingPassword: Yup.string()
                .oneOf([Yup.ref('password')], 'Passwords must match')
                .required("This field is required!"),
    });

    const handleRegister = (formValue: { username: string; email: string; password: string; matchingPassword: string; }) => {
        const { username, email, password, matchingPassword } = formValue;

        AuthService.register(
            username,
            email,
            password,
            matchingPassword,
        ).then(
            response => {
                setMessage(response.data.message);
                setSuccessful(true);
            },
            error => {
                const resMessage =
                    (error.response &&
                        error.response.data &&
                        error.response.data.message) ||
                    error.message ||
                    error.toString();

                setMessage(resMessage);
                setSuccessful(false);
            }
        );
    }

    return (
        <Container className="identity-container">
            <img
                src="//ssl.gstatic.com/accounts/ui/avatar_2x.png"
                alt="profile-img"
                className="profile-img-card"
            />
            <Formik
                initialValues={initialValues}
                validationSchema={validationSchema}
                onSubmit={handleRegister}
            >
                {(formikProps: FormikProps<{username: string, email: string, password: string, matchingPassword: string}>) =>
                    <Form className="identity-form">
                        {!successful &&
                            <>
                                <div className="form-group">
                                    <FormControl>
                                        <TextField
                                            id="username"
                                            label="Username"
                                            name="username"
                                            variant="outlined"
                                            value={formikProps.values.username}
                                            onChange={(event: React.ChangeEvent<HTMLInputElement>) => formikProps.setFieldValue('username', event.target.value)}
                                        />
                                        <ErrorMessage
                                            name="username"
                                            component="div"
                                            className="alert"
                                        />
                                    </FormControl>
                                </div>

                                <div className="form-group">
                                    <FormControl>
                                        <TextField
                                            id="email"
                                            label="Email"
                                            name="email"
                                            variant="outlined"
                                            value={formikProps.values.email}
                                            onChange={(event: React.ChangeEvent<HTMLInputElement>) => formikProps.setFieldValue('email', event.target.value)}
                                        />
                                        <ErrorMessage
                                            name="email"
                                            component="div"
                                            className="alert"
                                        />
                                    </FormControl>
                                </div>

                                <div className="form-group">
                                    <FormControl>
                                        <InputLabel htmlFor="password">Password</InputLabel>
                                        <OutlinedInput
                                            id="password"
                                            name="password"
                                            type={showPassword ? 'text' : 'password'}
                                            endAdornment={
                                                <InputAdornment position="end">
                                                    <IconButton
                                                        aria-label="toggle password visibility"
                                                        onClick={() => setShowPassword((show) => !show)}
                                                        onMouseDown={(event: any) => event.preventDefault()}
                                                        edge="end"
                                                    >
                                                        {showPassword ? <VisibilityOff /> : <Visibility />}
                                                    </IconButton>
                                                </InputAdornment>
                                            }
                                            label="Password"
                                            value={formikProps.values.password}
                                            onChange={(event: React.ChangeEvent<HTMLInputElement>) => formikProps.setFieldValue('password', event.target.value)}
                                        />
                                        <ErrorMessage
                                            name="password"
                                            component="div"
                                            className="alert"
                                        />
                                    </FormControl>
                                </div>

                                <div className="form-group">
                                    <FormControl>
                                        <TextField
                                            id="matchingPassword"
                                            name="matchingPassword"
                                            type="password"
                                            label="Password again"
                                            variant="outlined"
                                            value={formikProps.values.matchingPassword}
                                            onChange={(event: React.ChangeEvent<HTMLInputElement>) => formikProps.setFieldValue('matchingPassword', event.target.value)}
                                        />
                                        <ErrorMessage
                                            name="matchingPassword"
                                            component="div"
                                            className="alert"
                                        />
                                    </FormControl>
                                </div>

                                <div className="form-group">
                                    <Button color="primary" type="submit" variant="contained" key="submit" size="large">
                                        Sign up
                                    </Button>
                                </div>
                            </>
                        }
                        {message && (
                            <div>
                                <div
                                    className={
                                        successful ? "alert alert-success" : "alert alert-danger"
                                    }
                                    role="alert"
                                >
                                    {message}
                                </div>
                            </div>
                        )}
                    </Form>
                }
            </Formik>
            <div>
                {successful &&
                    <div className="success-container">
                        <Alert severity="success">Your account has successfully been created!</Alert>
                        <Button color="primary" variant="contained" size="large" href="/login">
                            Login to your account
                        </Button>
                    </div>
                }
            </div>
        </Container>
    );
}

export default Register;
