import React from "react";
import { useAppSelector } from "@store/hooks";
import { Navigate } from "react-router-dom";

function RequireAuth({ children }: { children: JSX.Element }) {
  const isLoggedIn = !!useAppSelector(state => state.auth.userInfo);

  if (!isLoggedIn) {
    return <Navigate to="/login" />;
  }

  return children;
}

export default RequireAuth;
