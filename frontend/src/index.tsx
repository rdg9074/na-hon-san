import React from "react";
import ReactDOM from "react-dom/client";
import "./index.css";
import { BrowserRouter } from "react-router-dom";
import ScrollToTop from "@utils/ScrollToTop.ts";
import { Provider } from "react-redux";
import { PersistGate } from "redux-persist/integration/react";
import { persistStore } from "redux-persist";
import App from "./App";
import { setUpStore } from "./store";

const root = ReactDOM.createRoot(
  document.getElementById("root") as HTMLElement
);
const store = setUpStore();
const persistor = persistStore(store);
root.render(
  <Provider store={store}>
    <PersistGate loading={null} persistor={persistor}>
      <BrowserRouter>
        <ScrollToTop />
        <App />
      </BrowserRouter>
    </PersistGate>
  </Provider>
);
