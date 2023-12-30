import {useEffect} from "react";
import * as React from "react";

export const BeerDetails = () => {
    useEffect(() => {
        console.log('yeeee, beer');
    }, []);

    return <>You are in Beer.</>
}
