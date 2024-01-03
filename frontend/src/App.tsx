import React, {useEffect, useState} from 'react';
import './App.css';
import '@fontsource/roboto/400.css';
import { BeersList } from './scenes/beers/list/BeersList';
import {Route, Routes} from "react-router-dom";
import {BeerDetails} from "./scenes/beers/details/BeerDetails";
import Login from "./scenes/identity/Login";
import Register from "./scenes/identity/Register";
import User from "./types/identity/User";
import AuthService from "./services/auth.service";
import {AccountMenu} from "./AccountMenu";
import {UserBeers} from "./scenes/identity/UserBeers";
import EventBus from "./common/EventBus";
const App: React.FC = () => {
    const [currentUser, setCurrentUser] = useState<User | undefined>(undefined);

    useEffect(() => {
        const user = AuthService.getCurrentUser();

        if (user) {
            setCurrentUser(user);
        }

        EventBus.on("logout", handleLogout);

        return () => {
            EventBus.remove("logout", handleLogout);
        };
    }, []);

    const handleLogout = () => {
        AuthService.logout();
        setCurrentUser(undefined);
    };

    return (
        <>
            <div className="App">
                <AccountMenu currentUser={currentUser} handleLogout={handleLogout} />

                <div className="routes">
                    <Routes>
                        <Route path="/" Component={BeersList} />
                        <Route path="/beers" Component={BeersList} />
                        <Route path="/beers/:id" element={<BeerDetails user={currentUser} />} />
                        <Route path="/login" element={<Login />} />
                        <Route path="/register" element={<Register />} />
                        {currentUser &&
                            <Route path="/profile" element={<UserBeers user={currentUser} />} />
                        }
                    </Routes>
                </div>
            </div>
        </>
    );
}

export default App;
