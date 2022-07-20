import MainNavBar from "@components/common/MainNavBar";
import Main from "@screens/Main";
import FeedPage from "@screens/FeedPage";
import React from "react";
import { Route, Routes } from "react-router-dom";
import TipPage from "@screens/TipPage";
import DealPage from "@screens/DealPage";
import Join from "@screens/Join/Join";
import ChkEmail from "@screens/ChkEmail";
import JoinDetail from "@screens/Join/JoinDetail";
import JoinMore from "@screens/Join/JoinMore";
import Welcome from "@screens/Join/Welcome";
import Login from "@screens/Login/Login";
import ResetPw from "@screens/Login/ResetPw";
import FindPw from "@screens/Login/FindPw";
import UserFeedPage from "@screens/UserFeedPage";
import "./RootLayout.scss";
import PageNotFound from "@screens/PageNotFound";
import Letters from "@screens/Letters";
import ChatRoom from "@screens/ChatRoom";

function RootRouter() {
  return (
    <>
      <MainNavBar />
      <div id="root-layout">
        <Routes>
          <Route path="/" element={<Main />} />
          <Route path="/tip" element={<TipPage />} />
          <Route path="/feed" element={<FeedPage />} />
          <Route path="/deal" element={<DealPage />} />
          <Route path="/userfeed/:id" element={<UserFeedPage />} />
          <Route path="/join/*">
            <Route index element={<Join />} />
            <Route path="chkEmail" element={<ChkEmail type="login" />} />
            <Route path="detail" element={<JoinDetail />} />
            <Route path="more" element={<JoinMore />} />
            <Route path="welcome" element={<Welcome />} />
            <Route path="*" element={<PageNotFound />} />
          </Route>
          <Route path="/login" element={<Login />} />
          <Route path="/find/pw">
            <Route index element={<FindPw />} />
            <Route path="chkEmail" element={<ChkEmail type="findPw" />} />
            <Route path="*" element={<PageNotFound />} />
          </Route>
          <Route path="/reset/pw" element={<ResetPw />} />
          <Route path="letters">
            <Route index element={<Letters />} />
            <Route path="detail" element={<ChatRoom />} />
            <Route path="*" element={<PageNotFound />} />
          </Route>
          <Route path="*" element={<PageNotFound />} />
        </Routes>
      </div>
    </>
  );
}

export default RootRouter;
