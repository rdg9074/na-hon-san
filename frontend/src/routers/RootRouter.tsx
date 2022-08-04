import MainNavBar from "@components/common/MainNavBar";
import Main from "@screens/Main";
import FeedPage from "@screens/FeedPage";
import React from "react";
import { Route, Routes } from "react-router-dom";
import TipPage from "@screens/TipPage";
import DealPage from "@screens/DealPage";
import NewsPage from "@screens/NewsPage";
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
import KakaoOauthHandler from "@screens/KakaoOauthHandler";
import NaverOauthHandler from "@screens/NaverOauthHandler";
import Letters from "@screens/Letters";
import ChatRoom from "@screens/ChatRoom";
import ChkPw from "@screens/AccountSetting/ChkPw";
import AccountSettingPage from "@screens/AccountSetting/AccountSettingPage";
import Withdrawal from "@screens/AccountSetting/Withdrawal";
import DealDetailPage from "@screens/DealDetailPage";
import TipDetail from "@screens/TipDetail";
import TipEdit from "@screens/TipEdit";
import TipUpdate from "@screens/TipUpdate";

function RootRouter() {
  return (
    <>
      <MainNavBar />
      <div id="root-layout">
        <Routes>
          <Route path="/" element={<Main />} />
          <Route path="/tip">
            <Route index element={<TipPage />} />
            <Route path="detail/:id" element={<TipDetail />} />
            <Route path="create" element={<TipEdit />} />
            <Route path="edit/:id" element={<TipUpdate />} />
            <Route path="*" element={<PageNotFound />} />
          </Route>
          <Route path="/feed" element={<FeedPage />} />
          <Route path="/deal">
            <Route index element={<DealPage />} />
            <Route path="detail/:id" element={<DealDetailPage />} />
          </Route>
          <Route path="/news/" element={<NewsPage />} />
          <Route path="/userfeed/:nickName" element={<UserFeedPage />} />
          <Route path="/account/*">
            <Route index element={<ChkPw />} />
            <Route path="set" element={<AccountSettingPage />} />
            <Route path="withdrawal" element={<Withdrawal />} />
          </Route>
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
          <Route path="/oauth/kakao" element={<KakaoOauthHandler />} />
          <Route path="/oauth/naver" element={<NaverOauthHandler />} />
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
