import axios from "axios";
import {GET_ERRORS, GET_WALLETS, GET_WALLET, DELETE_WALLET} from "./types";

export const createWallet = (wallet, history) => async dispatch => {
    try {
        const res = await axios.post("http://localhost:8080/wallets", wallet);
        history.push("/getWallets");
    } catch (err) {
        dispatch({type: GET_ERRORS, payload: err.response.data});
    }
};

export const editWallet = (id, wallet, history) => async dispatch => {
    try {
        const res = await axios.patch(`http://localhost:8080/wallets/${id}`, wallet);
        history.push("/getWallets");
    } catch (err) {
        dispatch({type: GET_ERRORS, payload: err.response.data});
    }
};

export const getWallets = () => async dispatch => {
    const res = await axios.get("http://localhost:8080/wallets/all");
    dispatch({type: GET_WALLETS, payload: res.data});
};

export const getSimpleWallets = () => async dispatch => {
    const res = await axios.get("http://localhost:8080/wallets/simpleWallets");
    dispatch({type: GET_WALLETS, payload: res.data});
};

export const deleteWallet = id => async dispatch => {
    {
        await axios.delete(`http://localhost:8080/wallets/${id}`);
        dispatch({type: DELETE_WALLET, payload: id});
    }
};

export const getWallet = (id, history) => async dispatch => {
    try {
        const res = await axios.get(`http://localhost:8080/wallets/${id}`);
        dispatch({type: GET_WALLET, payload: res.data});
    } catch (error) {
        history.push("/dashboard");
    }
};

export const transferFunds = (IDsenderWallet, IDrecipientWallet, amount) => async dispatch => {
    try {
        const res = await axios.patch(`http://localhost:8080/wallets`, null, {params: {
            IDsenderWallet,
            IDrecipientWallet,
            amount,
        }});
    } catch (err) {
        dispatch({type: GET_ERRORS, payload: err.response.data});
    }
};
