import React from "react";
import { rest } from "msw";
import { setupServer } from "msw/node";
import { screen, waitFor } from "@testing-library/react";
import renderWithProviders from "@utils/test-utils";
import userEvent from "@testing-library/user-event";
import { MemoryRouter } from "react-router-dom";
import ResetPw from "@screens/Login/ResetPw";
import { BASE_URL } from "@apis/";
import { wait } from "@testing-library/user-event/dist/utils";

const handlers = [
  rest.put(`${BASE_URL}/user/password`, (req, res, ctx) => {
    if (
      req.body.id === "ssafy@naver.com" &&
      req.body.password === "123456!abc"
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
describe("비밀번호 수정 페이지", () => {
  let passwordInput;
  let chkPasswordInput;
  let resetBtn;

  beforeAll(() => server.listen());

  beforeEach(() => {
    renderWithProviders(
      <MemoryRouter>
        <ResetPw />
      </MemoryRouter>,
      { preloadedState: { auth: initialState } }
    );
    passwordInput = screen.getByPlaceholderText("비밀번호를 입력해주세요.");
    chkPasswordInput = screen.getByPlaceholderText(
      "비밀번호를 다시 한번 입력해주세요."
    );
    resetBtn = screen.getByText("비밀번호 재설정");
  });

  afterEach(() => server.resetHandlers());

  afterAll(() => server.close());

  test("비밀번호 미입력시", () => {
    userEvent.click(resetBtn);
    expect(passwordInput).toHaveFocus();
    userEvent.type(passwordInput, "123456");
    userEvent.click(resetBtn);
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

  test("비밀번호 초기화", async () => {
    userEvent.type(passwordInput, "123456!abc");
    userEvent.type(chkPasswordInput, "123456!abc");
    userEvent.click(resetBtn);
    await waitFor(() => expect(mockNavigate).toBeCalledTimes(1));
    await waitFor(() => expect(mockNavigate).toBeCalledWith("/login"));
  });
});
