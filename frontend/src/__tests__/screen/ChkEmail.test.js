import React from "react";
import { rest } from "msw";
import { setupServer } from "msw/node";
import { screen, waitFor } from "@testing-library/react";
import renderWithProviders from "@utils/test-utils";
import userEvent from "@testing-library/user-event";
import { MemoryRouter } from "react-router-dom";
import ChkEmail from "@screens/ChkEmail";
import { BASE_URL } from "@apis/";

const handlers = [
  rest.get(`${BASE_URL}/user/auth`, (req, res, ctx) => {
    if (
      req.url.searchParams.get("id") === "ssafy@naver.com" &&
      req.url.searchParams.get("number") === "123456" &&
      req.url.searchParams.get("type") === "0"
    ) {
      return res(ctx.json({ message: "SUCCESS" }), ctx.delay(10));
    }
    return res(ctx.json({ message: "FAIL" }), ctx.delay(10));
  })
];
const server = setupServer(...handlers);
const mockNavigate = jest.fn();

jest.mock("react-router-dom", () => ({
  ...jest.requireActual("react-router-dom"),
  useNavigate: () => mockNavigate
}));
const initialState = { tmpId: "ssafy@naver.com" };

describe("인증코드페이지(공통)", () => {
  let codeInput;
  let nextBtn;

  beforeAll(() => server.listen());

  beforeEach(() => {
    renderWithProviders(
      <MemoryRouter>
        <ChkEmail type="login" />
      </MemoryRouter>,
      {
        preloadedState: {
          auth: initialState
        }
      }
    );
    jest.useFakeTimers();
    codeInput = screen.getByPlaceholderText("인증번호를 입력해주세요");
    nextBtn = screen.getByText("다음");
    screen.getByText(
      "ssafy@naver.com에서 이메일을 확인 후 인증코드를 입력해주세요!"
    );
  });

  afterEach(() => {
    server.resetHandlers();
    jest.runOnlyPendingTimers();
    jest.useRealTimers();
  });

  afterAll(() => server.close());

  test("아무것도입력 X", () => {
    userEvent.click(nextBtn);
    screen.getByText("인증코드를 입력해주세요");
  });

  test("인증코드 실패", async () => {
    userEvent.type(codeInput, "12345");
    userEvent.click(nextBtn);
    await screen.findByText("인증코드가 올바르지 않습니다.");
    expect(codeInput).toHaveFocus();
  });
});

describe("인증코드페이지 (로그인)", () => {
  let codeInput;
  let nextBtn;

  beforeAll(() => server.listen());

  beforeEach(() => {
    renderWithProviders(
      <MemoryRouter>
        <ChkEmail type="login" />
      </MemoryRouter>,
      {
        preloadedState: {
          auth: initialState
        }
      }
    );
    codeInput = screen.getByPlaceholderText("인증번호를 입력해주세요");
    nextBtn = screen.getByText("다음");
    screen.getByText(
      "ssafy@naver.com에서 이메일을 확인 후 인증코드를 입력해주세요!"
    );
  });

  afterEach(() => server.resetHandlers());

  afterAll(() => server.close());

  test("인증코드 성공라우팅", async () => {
    userEvent.type(codeInput, "123456");
    userEvent.click(nextBtn);
    await waitFor(() => expect(mockNavigate).toHaveBeenCalledTimes(1));
    await waitFor(() =>
      expect(mockNavigate).toHaveBeenCalledWith("/join/detail")
    );
  });
  test("인증코드 성공라우팅 (엔터)", async () => {
    userEvent.type(codeInput, "123456");
    userEvent.keyboard("{enter}");
    await waitFor(() => expect(mockNavigate).toHaveBeenCalledTimes(1));
    await waitFor(() =>
      expect(mockNavigate).toHaveBeenCalledWith("/join/detail")
    );
  });
});

// describe("인증코드페이지 (비밀번호찾기)", () => {});
