import React from "react";
import { rest } from "msw";
import { setupServer } from "msw/node";
import { screen, waitFor } from "@testing-library/react";
import renderWithProviders from "@utils/test-utils";
import userEvent from "@testing-library/user-event";
import { MemoryRouter } from "react-router-dom";
import Join from "@screens/Join/Join";
import { BASE_URL } from "@apis/index";

const handlers = [
  rest.post(`${BASE_URL}/user/auth`, (req, res, ctx) => {
    if (req.body.id === "ssafy@naver.com" && req.body.type === 0) {
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

describe("회원가입 페이지", () => {
  let idInput;

  let joinBtn;

  beforeAll(() => server.listen());

  beforeEach(() => {
    renderWithProviders(
      <MemoryRouter>
        <Join />
      </MemoryRouter>
    );
    idInput = screen.getByPlaceholderText("이메일을 입력해주세요.");
    joinBtn = screen.getByText("다음");
  });

  afterEach(() => server.resetHandlers());

  afterAll(() => server.close());

  test("이메일 미입력시", () => {
    userEvent.click(joinBtn);
    screen.getByText("이메일을 입력해주세요.");
    expect(idInput).toHaveFocus();
  });

  test("이메일 형식 x", () => {
    userEvent.type(idInput, "123naver");
    userEvent.click(joinBtn);
    screen.getByText("이메일 형식을 확인해주세요.");
    expect(idInput).toHaveFocus();
  });

  test("이미 가입된 이메일", async () => {
    userEvent.type(idInput, "ssafy1@naver.com");
    userEvent.click(joinBtn);
    await screen.findByText("이미 존재하는 아이디입니다.");
    expect(idInput).toHaveFocus();
  });

  test("가입되지 않은 이메일", async () => {
    userEvent.type(idInput, "ssafy@naver.com");
    userEvent.click(joinBtn);

    await waitFor(() => expect(mockNavigate).toBeCalledTimes(1));
    await waitFor(() => expect(mockNavigate).toBeCalledWith("chkEmail"));
  });

  test("엔터 가능", async () => {
    userEvent.type(idInput, "ssafy1@naver.com");
    userEvent.keyboard("{enter}");
    await screen.findByText("이미 존재하는 아이디입니다.");
    expect(idInput).toHaveFocus();
  });

  test("로그인으로 라우팅", () => {
    expect(screen.getByText("로그인")).toHaveAttribute("href", "/login");
  });
});
