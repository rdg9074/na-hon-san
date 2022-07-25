import React from "react";
import { rest } from "msw";
import { setupServer } from "msw/node";
import { screen, waitFor } from "@testing-library/react";
import renderWithProviders from "@utils/test-utils";
import userEvent from "@testing-library/user-event";
import Login from "@screens/Login/Login";
import { MemoryRouter } from "react-router-dom";
import { BASE_URL } from "@apis/";
import reducer, { getUserInfo } from "@store/ducks/auth/authThunk";
import { setUserId } from "@store/ducks/auth/authSlice";
import { wait } from "@testing-library/user-event/dist/utils";

const handlers = [
  rest.post(`${BASE_URL}/user/login`, (req, res, ctx) => {
    if (req.body.id === "ssafy" && req.body.password === "ssafy") {
      return res(ctx.json({ message: "SUCCESS" }), ctx.delay(10));
    }
    return res(ctx.json({ message: "JOINED" }), ctx.delay(10));
  }),
  rest.get(`${BASE_URL}/user`, (req, res, ctx) => {
    return res(
      ctx.json({
        data: {
          id: "test",
          nickname: "test아이디입니디아!",
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

describe("로그인페이지", () => {
  let idInput;
  let passwordInput;
  let loginBtn;

  beforeAll(() => server.listen());

  beforeEach(() => {
    renderWithProviders(
      <MemoryRouter>
        <Login />
      </MemoryRouter>
    );
    idInput = screen.getByPlaceholderText("이메일을 입력해주세요");
    passwordInput = screen.getByPlaceholderText("비밀번호를 입력해주세요");
    loginBtn = screen.getByText("로그인");
  });

  afterEach(() => server.resetHandlers());

  afterAll(() => server.close());

  test("로그인 (아무것도입력 X)", () => {
    screen.getByText("로그인");
    userEvent.click(loginBtn);
    screen.getByText("아이디를 입력해주세요.");
    expect(idInput).toHaveFocus();
  });

  test("로그인 (아이디만 입력)", () => {
    screen.getByText("로그인");
    userEvent.type(idInput, "ssafy");
    userEvent.click(loginBtn);
    screen.getByText("비밀번호를 입력해주세요.");
    expect(passwordInput).toHaveFocus();
  });

  test("로그인 (비밀번호만 입력)", () => {
    userEvent.type(passwordInput, "ssafy");
    userEvent.click(loginBtn);
    screen.getByText("아이디를 입력해주세요.");
    expect(idInput).toHaveFocus();
  });

  test("로그인 성공", async () => {
    loginBtn = screen.getByText("로그인");
    userEvent.type(idInput, "ssafy");
    userEvent.type(passwordInput, "ssafy");
    userEvent.click(loginBtn);
    wait();
    await waitFor(() => expect(mockNavigate).toHaveBeenCalledTimes(1));
    await waitFor(() => expect(mockNavigate).toHaveBeenCalledWith("/"));
  });

  test("로그인 실패", async () => {
    userEvent.type(idInput, "ssafy1");
    userEvent.type(passwordInput, "ssafy1234");
    userEvent.click(loginBtn);
    await screen.findByText("아이디 또는 비밀번호가 일치하지 않습니다.");
  });

  test("회원가입 라우팅", async () => {
    const joinBtn = screen.getByText("회원가입");
    expect(joinBtn).toHaveAttribute("href", "/join");
  });

  test("비밀번호찾기 라우팅", async () => {
    const findpwBtn = screen.getByText("비밀번호찾기");
    expect(findpwBtn).toHaveAttribute("href", "/find/pw");
  });
});
