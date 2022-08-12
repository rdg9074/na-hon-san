/* eslint-disable no-useless-constructor */
/* eslint-disable @typescript-eslint/no-empty-function */
/* eslint-disable class-methods-use-this */

import React from "react";
import { render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { MemoryRouter } from "react-router-dom";
import { Provider } from "react-redux";
import { wait } from "@testing-library/user-event/dist/utils";
import renderWithProviders from "@utils/test-utils";
import { setUpStore } from "../../store";
import App from "../../App.tsx";

const mockIntersectionObserver = class {
  constructor() {}

  observe() {
    return null;
  }

  unobserve() {
    return null;
  }

  disconnect() {
    return null;
  }
};
window.IntersectionObserver = mockIntersectionObserver;

describe("MainNavBar 테스트 (비로그인시)", () => {
  beforeEach(() => {
    render(
      <Provider store={setUpStore()}>
        <MemoryRouter>
          <App />
        </MemoryRouter>
      </Provider>
    );
  });
  describe("라우팅 테스트", () => {
    const routingTest = (linkName, expectText) => {
      const link = screen.getByText(linkName);
      expect(link).toBeInTheDocument();
      userEvent.click(link);
      wait();
      expect(screen.getByText(expectText)).toBeInTheDocument();
    };
    test("홈페이지", () => {
      routingTest("홈", "자취가 처음인데..");
    });
    test("꿀팁보기", () => {
      routingTest("꿀팁", "광주 지역 꿀팁");
    });
    test("피드보기", () => {
      // 계정없으니
      routingTest("피드", "아직 계정이 없으신가요?");
    });
    test("꿀딜보기", () => {
      routingTest("꿀딜", "광주 지역 꿀딜");
    });
    test("로그인", () => {
      routingTest("로그인", "아직 계정이 없으신가요?");
    });
    test("회원가입", () => {
      routingTest("회원가입", "이미 계정이 있으신가요?");
    });
  });
});

const initialState = {
  userInfo: {
    id: "ssafy",
    nickname: "ssafy",
    area: "test",
    followOpen: true,
    followerOpen: true,
    likeNotice: true,
    followNotice: true,
    commentNotice: true,
    replyNotice: true,
    profileMsg: "test",
    profileImg: "test",
    backgroundImg: "test"
  }
};

describe("MainNavBar 테스트 (로그인시)", () => {
  beforeEach(() => {
    renderWithProviders(
      <MemoryRouter>
        <App />
      </MemoryRouter>,
      {
        preloadedState: {
          auth: initialState
        }
      }
    );
  });
  describe("라우팅 테스트", () => {
    const routingTest = (linkName, expectText) => {
      const link = screen.getByText(linkName);
      expect(link).toBeInTheDocument();
      userEvent.click(link);
      wait();
      expect(screen.getByText(expectText)).toBeInTheDocument();
    };
    test("홈페이지", () => {
      routingTest("홈", "자취가 처음인데..");
    });
    test("꿀팁보기", () => {
      routingTest("꿀팁", "광주 지역 꿀팁");
    });
    test("피드보기", () => {
      // 계정있
      routingTest("피드", "맞춤형 꿀 딜 추천");
    });
    test("꿀딜보기", () => {
      routingTest("꿀딜", "광주 지역 꿀딜");
    });
    test("쪽지함 라우팅", () => {
      const msgIcon = screen.getByAltText("쪽지함");
      userEvent.click(msgIcon);
      screen.getByText("쪽지함");
    });

    test("알림 툴팁", async () => {
      const userIcon = screen.getByAltText("유저프로필");
      userEvent.click(userIcon);
      userEvent.click(screen.getByText("마이페이지"));
    });
  });
});
