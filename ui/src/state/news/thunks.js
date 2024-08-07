import axios from "axios";

import { fetchNews } from "./actions";

// THUNK CREATORS
export const fetchNewsThunk = () => (dispatch) =>
{
    return axios.get(`/api/v1/news`)
    .then((res) => dispatch(fetchNews(res.data.news)))
    .catch((err) => console.error(err));
};