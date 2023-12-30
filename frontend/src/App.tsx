import React, {useEffect, useState} from 'react';
import './App.css';
import '@fontsource/roboto/400.css';
import { BeersList } from './scenes/beers/BeersList';
import {Route, Routes} from "react-router-dom";
import {BeerDetails} from "./scenes/beers/BeerDetails";
import Login from "./scenes/identity/Login";
import Register from "./scenes/identity/Register";
import User from "./types/identity/User";
import AuthService from "./services/auth.service";
import {AccountMenu} from "./AccountMenu";
import {Profile2} from "./scenes/identity/Profile2";
const App: React.FC = () => {
    const [currentUser, setCurrentUser] = useState<User | undefined>(undefined);

    useEffect(() => {
        const user = AuthService.getCurrentUser();

        if (user) {
            setCurrentUser(user);
        }
    }, []);

    return (
        <>
            <div className="App">
                <AccountMenu currentUser={currentUser} handleLogout={() => console.log('logout')} />

                <div className="routes">
                    <Routes>
                        <Route path="/" Component={BeersList} />
                        <Route path="/beers" Component={BeersList} />
                        <Route path="/beers/:id" Component={BeerDetails} />
                        <Route path="/login" element={<Login />} />
                        <Route path="/register" element={<Register />} />
                        <Route path="/profile" element={<Profile2 user={currentUser} />} />
                    </Routes>
                </div>
            </div>
        </>
    );
}

export default App;
