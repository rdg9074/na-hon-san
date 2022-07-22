import {
  combineReducers,
  configureStore,
  PreloadedState
} from "@reduxjs/toolkit";
import logger from "redux-logger";
import authSlice from "./ducks/auth/authSlice";

const rootReducer = combineReducers({
  auth: authSlice
});

// eslint-disable-next-line no-use-before-define
export const setUpStore = (preloadedState?: PreloadedState<RootState>) => {
  return configureStore({
    reducer: rootReducer,
    preloadedState,
    middleware: getDefaultMiddleware => getDefaultMiddleware().concat(logger)
  });
};

export type RootState = ReturnType<typeof rootReducer>;
export type AppStore = ReturnType<typeof setUpStore>;
export type AppDispatch = AppStore["dispatch"];
