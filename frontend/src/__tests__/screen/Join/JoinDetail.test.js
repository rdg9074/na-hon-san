import React from "react";
import { rest } from "msw";
import { setupServer } from "msw/node";
import { screen, waitFor } from "@testing-library/react";
import renderWithProviders from "@utils/test-utils";
import userEvent from "@testing-library/user-event";
import { MemoryRouter } from "react-router-dom";

import JoinDetail from "@screens/Join/JoinDetail";
import { BASE_URL } from "@apis/index";
import { wait } from "@testing-library/user-event/dist/utils";

const handlers = [
  rest.get(`${BASE_URL}/user/check/notdupli`, (req, res, ctx) =>
    res(ctx.json({ message: "SUCCESS" }), ctx.delay(10))
  ),
  rest.get(`${BASE_URL}/user/check/dupli`, (req, res, ctx) =>
    res(ctx.json({ message: "FAIL" }), ctx.delay(10))
  ),
  rest.post(`${BASE_URL}/user`, (req, res, ctx) => {
    if (
      req.body.id === "ssafy@naver.com" &&
      req.body.password === "123456!abc" &&
      req.body.nickname === "notdupli"
    ) {
      return res(ctx.json({ message: "SUCCESS" }), ctx.delay(10));
    }
    return res(ctx.json({ message: "FAIL" }), ctx.delay(10));
  }),
  rest.post(`${BASE_URL}/user/login`, (req, res, ctx) => {
    if (
      req.body.id === "ssafy@naver.com" &&
      req.body.password === "123456!abc"
    ) {
      return res(ctx.json({ message: "SUCCESS" }), ctx.delay(10));
    }
    return res(ctx.json({ message: "JOINED" }), ctx.delay(10));
  }),
  rest.get(`${BASE_URL}/user`, (req, res, ctx) => {
    return res(
      ctx.json({
        data: {
          id: "ssafy@naver.com",
          nickname: "notdupli",
          area: null,
          followOpen: true,
          followerOpen: true,
          likeNotice: true,
          followNotice: true,
          commentNotice: true,
          replyNotice: true,
          profileMsg: null,
          profileImg: null,
          backgroundImg: null
        },
        message: "SUCCESS"
      }),
      ctx.delay(10)
    );
  })
];
const server = setupServer(...handlers);
const mockNavigate = jest.fn();

jest.mock("react-router-dom", () => ({
  ...jest.requireActual("react-router-dom"),
  useNavigate: () => mockNavigate
}));
const initialState = { tmpId: "ssafy@naver.com" };

describe("회원가입 디테일페이지", () => {
  let nickNameInput;
  let passwordInput;
  let chkPasswordInput;
  let nickNameBtn;
  let joinBtn;

  beforeAll(() => server.listen());

  beforeEach(() => {
    renderWithProviders(
      <MemoryRouter>
        <JoinDetail />
      </MemoryRouter>,
      { preloadedState: { auth: initialState } }
    );
    nickNameInput = screen.getByPlaceholderText("닉네임을 입력해주세요.");
    passwordInput = screen.getByPlaceholderText("비밀번호를 입력해주세요.");
    chkPasswordInput = screen.getByPlaceholderText(
      "비밀번호를 다시 한번 입력해주세요."
    );
    nickNameBtn = screen.getByText("중복확인");
    joinBtn = screen.getByText("회원가입 완료");
  });

  afterEach(() => server.resetHandlers());

  afterAll(() => server.close());

  test("중복되지않은 닉네임", async () => {
    userEvent.type(nickNameInput, "notdupli");
    userEvent.click(nickNameBtn);
    await screen.findByText("사용가능한 닉네임입니다.");
  });

  test("중복된 닉네임", async () => {
    userEvent.type(nickNameInput, "dupli");
    userEvent.click(nickNameBtn);
    await screen.findByText("이미 사용중인 닉네임입니다.");
  });

  test("닉네임 미입력시", () => {
    userEvent.click(joinBtn);
    expect(nickNameInput).toHaveFocus();
  });

  test("비밀번호 미입력시", async () => {
    userEvent.type(nickNameInput, "notdupli");
    userEvent.click(nickNameBtn);
    await screen.findByText("사용가능한 닉네임입니다.");

    userEvent.click(joinBtn);
    expect(passwordInput).toHaveFocus();
    userEvent.type(passwordInput, "123456!abc");
    userEvent.click(joinBtn);
    expect(chkPasswordInput).toHaveFocus();
  });
  test("비밀번호 형식 틀림", () => {
    userEvent.type(passwordInput, "1234567");
    screen.getByText(
      "숫자,대소문자,특수문자를 혼합하여 8~16자리로 입력해주세요."
    );
    expect(passwordInput).toHaveFocus();
  });

  test("비밀번호 서로 다름 (형식 맞음)", () => {
    userEvent.type(passwordInput, "123456!abc");
    userEvent.type(chkPasswordInput, "abc!123456");
    screen.getByText("비밀번호가 일치하지 않습니다.");
    expect(chkPasswordInput).toHaveFocus();
  });

  test("회원가입 성공", async () => {
    userEvent.type(nickNameInput, "notdupli");
    userEvent.click(nickNameBtn);
    await screen.findByText("사용가능한 닉네임입니다.");

    userEvent.type(passwordInput, "123456!abc");
    userEvent.type(chkPasswordInput, "123456!abc");
    userEvent.click(joinBtn);
    await waitFor(() => expect(mockNavigate).toBeCalledTimes(1));
    await waitFor(() => expect(mockNavigate).toBeCalledWith("/join/welcome"));
  });
});
