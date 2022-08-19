import React from "react";
import FindPw from "@screens/Login/FindPw";
import renderWithProviders from "@utils/test-utils";
import { rest } from "msw";
import { setupServer } from "msw/lib/node";
import { MemoryRouter } from "react-router-dom";
import { screen, waitFor } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { BASE_URL } from "@apis/";

const handler = [
  rest.post(`${BASE_URL}/user/auth`, (req, res, ctx) => {
    if (req.body.id === "ssafy" && req.body.type === 1) {
      return res(ctx.json({ message: "SUCCESS" }), ctx.delay(10));
    }
    return res(ctx.json({ message: "FAIL" }), ctx.delay(10));
  })
];

const server = setupServer(...handler);
const mockNavigate = jest.fn();

jest.mock("react-router-dom", () => ({
  ...jest.requireActual("react-router-dom"),
  useNavigate: () => mockNavigate
}));

describe("비밀번호찾기 (이메일입력부분)", () => {
  let title;
  let authBtn;
  let emailInput;

  beforeEach(() => {
    server.listen();

    renderWithProviders(
      <MemoryRouter>
        <FindPw />
      </MemoryRouter>
    );
    title = screen.getByText("비밀번호를 까먹으셨나요?");
    authBtn = screen.getByText("인증번호 전송하기");
    emailInput = screen.getByPlaceholderText("이메일을 입력해주세요");
  });

  afterEach(() => server.resetHandlers());

  afterAll(() => server.close());

  test("미입력하고 버튼클릭", () => {
    userEvent.click(authBtn);
    screen.findByText("이메일을 입력해주세요.");
  });
  test("가입되지않은 이메일 입력", async () => {
    userEvent.type(emailInput, "ssafy1");
    userEvent.click(authBtn);
    await screen.findByText("가입된 이메일이 아닙니다.");
  });

  test("가입된 이메일 입력", async () => {
    userEvent.type(emailInput, "ssafy");
    userEvent.click(authBtn);

    await waitFor(() => expect(mockNavigate).toHaveBeenCalledTimes(1));
    await waitFor(() => expect(mockNavigate).toHaveBeenCalledWith("chkEmail"));
  });
});
