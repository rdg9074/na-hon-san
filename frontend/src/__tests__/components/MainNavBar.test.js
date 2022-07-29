import React from "react";
import { render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { MemoryRouter } from "react-router-dom";
import { Provider } from "react-redux";
import App from "../../App.tsx";
import { setUpStore } from "../../store";

describe("MainNavBar 테스트", () => {
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
      expect(screen.getByText(expectText)).toBeInTheDocument();
    };
    test("홈페이지", () => {
      routingTest("홈", "자취가 처음인데..");
    });
    test("꿀팁보기", () => {
      routingTest("꿀팁", "광주 지역 꿀팁");
    });
    // test("피드보기", () => {
    //   routingTest("피드", "피드페이지입니다.");
    // });
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

  // describe("헤더 툴팁 확인", () => {
  //   test("쪽지 툴팁", () => {
  //     const msgIcon = screen.getByAltText("쪽지함");
  //     expect(msgIcon).toBeInTheDocument();
  //   });
  //   test("알림 툴팁", () => {
  //     const msgIcon = screen.getByAltText("알림");
  //     expect(msgIcon).toBeInTheDocument();
  //   });
  // });
});
