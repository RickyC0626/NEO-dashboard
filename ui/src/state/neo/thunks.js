import axios from "axios";

import { fetchBrowsedNeos, fetchBrowsedNeosReverse } from "./actions";

// THUNK CREATORS
export const fetchBrowsedNeosThunk = (page) => (dispatch) =>
{
    return axios.get(`/api/v1/browse?page=${page}`)
    .then((res) => dispatch(fetchBrowsedNeos(res.data)))
    .catch((err) => console.error(err));
};

export const fetchBrowsedNeosReverseThunk = (page) => (dispatch) =>
{
    return axios.get(`/api/v1/browse?page=${page}`)
    .then((res) => dispatch(fetchBrowsedNeosReverse(res.data)))
    .catch((err) => console.error(err));
};