import React from "react";
import ReactDOM from "react-dom/client";
import "./index.css";
import { BrowserRouter } from "react-router-dom";
import ScrollToTop from "@utils/ScrollToTop.ts";
import { Provider } from "react-redux";
import App from "./App";
import { store } from "./store";

const root = ReactDOM.createRoot(
  document.getElementById("root") as HTMLElement
);
root.render(
  <Provider store={store}>
    <BrowserRouter>
      <ScrollToTop />
      <App />
    </BrowserRouter>
  </Provider>
);
