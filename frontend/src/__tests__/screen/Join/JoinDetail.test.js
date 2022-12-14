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

describe("???????????? ??????????????????", () => {
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
    nickNameInput = screen.getByPlaceholderText("???????????? ??????????????????.");
    passwordInput = screen.getByPlaceholderText("??????????????? ??????????????????.");
    chkPasswordInput = screen.getByPlaceholderText(
      "??????????????? ?????? ?????? ??????????????????."
    );
    nickNameBtn = screen.getByText("????????????");
    joinBtn = screen.getByText("???????????? ??????");
  });

  afterEach(() => server.resetHandlers());

  afterAll(() => server.close());

  test("?????????????????? ?????????", async () => {
    userEvent.type(nickNameInput, "notdupli");
    userEvent.click(nickNameBtn);
    await screen.findByText("??????????????? ??????????????????.");
  });

  test("????????? ?????????", async () => {
    userEvent.type(nickNameInput, "dupli");
    userEvent.click(nickNameBtn);
    await screen.findByText("?????? ???????????? ??????????????????.");
  });

  test("????????? ????????????", () => {
    userEvent.click(joinBtn);
    expect(nickNameInput).toHaveFocus();
  });

  test("???????????? ????????????", async () => {
    userEvent.type(nickNameInput, "notdupli");
    userEvent.click(nickNameBtn);
    await screen.findByText("??????????????? ??????????????????.");

    userEvent.click(joinBtn);
    expect(passwordInput).toHaveFocus();
    userEvent.type(passwordInput, "123456!abc");
    userEvent.click(joinBtn);
    expect(chkPasswordInput).toHaveFocus();
  });
  test("???????????? ?????? ??????", () => {
    userEvent.type(passwordInput, "1234567");
    screen.getByText(
      "??????,????????????,??????????????? ???????????? 8~16????????? ??????????????????."
    );
    expect(passwordInput).toHaveFocus();
  });

  test("???????????? ?????? ?????? (?????? ??????)", () => {
    userEvent.type(passwordInput, "123456!abc");
    userEvent.type(chkPasswordInput, "abc!123456");
    screen.getByText("??????????????? ???????????? ????????????.");
    expect(chkPasswordInput).toHaveFocus();
  });

  test("???????????? ??????", async () => {
    userEvent.type(nickNameInput, "notdupli");
    userEvent.click(nickNameBtn);
    await screen.findByText("??????????????? ??????????????????.");

    userEvent.type(passwordInput, "123456!abc");
    userEvent.type(chkPasswordInput, "123456!abc");
    userEvent.click(joinBtn);
    await waitFor(() => expect(mockNavigate).toBeCalledTimes(1));
    await waitFor(() => expect(mockNavigate).toBeCalledWith("/join/welcome"));
  });
});
