import React, {useState} from "react";
import { NavigateFunction, useNavigate } from "react-router-dom";
import * as Yup from "yup";
import AuthService from "../../services/auth.service";
import {ErrorMessage, Form, Formik, FormikProps} from "formik";
import {
    Button,
    CircularProgress,
    Container,
    FormControl,
    IconButton,
    InputAdornment,
    InputLabel, OutlinedInput,
    TextField
} from "@mui/material";
import './identity.less';
import {Visibility, VisibilityOff} from "@mui/icons-material";

type Props = {}

const Login: React.FC<Props> = () => {
    let navigate: NavigateFunction = useNavigate();

    const [loading, setLoading] = useState<boolean>(false);
    const [showPassword, setShowPassword] = useState<boolean>(false);
    const [message, setMessage] = useState<string>("");

    const initialValues: {
        username: string;
        password: string;
    } = {
        username: "",
        password: "",
    };

    const validationSchema = Yup.object().shape({
        username: Yup.string().required("Username is required!").max(64, 'Username is less than 64 characters long'),
        password: Yup.string().required("Password is required!").max(32, 'Password is less than 32 characters long.'),
    });

    const handleLogin = (formValue: { username: string; password: string }) => {
        const { username, password } = formValue;

        setMessage("");
        setLoading(true);

        AuthService.login(username, password).then(
            () => {
                navigate("/profile");
                window.location.reload();
            },
            (error) => {
                const resMessage =
                    (error.response &&
                        error.response.data &&
                        error.response.data.message) ||
                    error.message ||
                    error.toString();

                setLoading(false);
                setMessage(resMessage);
            }
        );
    };

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
                onSubmit={handleLogin}
            >
                {(formikProps: FormikProps<{username: string, password: string}>) =>
                    <Form className="identity-form">
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
                            <Button color="primary" type="submit" variant="contained" key="submit" size="large" disabled={loading}>
                                {loading && (
                                    <CircularProgress size="xs" />
                                )}
                                Login
                            </Button>
                        </div>

                        {message && (
                            <div className="form-group">
                                <div className="alert" role="alert">
                                    {message}
                                </div>
                            </div>
                        )}
                    </Form>
                }
            </Formik>
        </Container>
    );
};

export default Login;
