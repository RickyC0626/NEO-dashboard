// Necessities and accessories for constructing our Redux store
import { combineReducers } from "redux";
import { configureStore } from "@reduxjs/toolkit";

// Individual reducers altogether under an alias
import * as reducers from "../reducers";

// Construct our Redux store
const rootReducer = combineReducers(reducers);
const store = configureStore({ reducer: rootReducer });

// Export our store by default, which will be provided to and injected within our entire application
export default store;
