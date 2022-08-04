import {
  combineReducers,
  configureStore,
  PreloadedState
} from "@reduxjs/toolkit";
import logger from "redux-logger";
import { persistReducer } from "redux-persist";
import storageSession from "redux-persist/lib/storage/session";
import alarmSlice from "./ducks/alarm/alarmSlice";
import authSlice from "./ducks/auth/authSlice";
import dmSlice from "./ducks/dm/dmSlice";
import infinitySlice from "./ducks/infinity/infinitySlice";

const rootReducer = combineReducers({
  auth: authSlice,
  dm: dmSlice,
  alarm: alarmSlice,
  infinity: infinitySlice
});

const persistConfig = {
  key: "root",
  storage: storageSession,
  whitelist: ["auth"]
};

const persistRootReducer = persistReducer(persistConfig, rootReducer);
// eslint-disable-next-line no-use-before-define
export const setUpStore = (preloadedState?: PreloadedState<RootState>) => {
  return configureStore({
    reducer: persistRootReducer,
    preloadedState,
    middleware: getDefaultMiddleware =>
      getDefaultMiddleware({ serializableCheck: false }).concat(logger)
  });
};

export type RootState = ReturnType<typeof rootReducer>;
export type AppStore = ReturnType<typeof setUpStore>;
export type AppDispatch = AppStore["dispatch"];
