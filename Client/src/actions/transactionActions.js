import axios from "axios";
import {GET_TRANSACTIONS, GET_ERRORS} from "./types";

export const getTransactions = () => async dispatch => {
    const res = await axios.get("http://localhost:8080/transactions/all");
    dispatch({type: GET_TRANSACTIONS, payload: res.data});
};

export const createTransaction = (event, person, wallet, subcategory ,transaction, history) => async dispatch => {
    try {
        const res = await axios.post(`http://localhost:8080/transactions`, transaction, {params: {
            event,
            person,
            wallet,
            subcategory
        }});
        history.push("/getTransactions");
    } catch (err) {
        dispatch({type: GET_ERRORS, payload: err.response.data});
    }
};