import React from "react";
import { rest } from "msw";
import { setupServer } from "msw/node";
import { screen, waitFor } from "@testing-library/react";
import renderWithProviders from "@utils/test-utils";
import userEvent from "@testing-library/user-event";
import { MemoryRouter } from "react-router-dom";

import { BASE_URL } from "@apis/index";
import JoinMore from "@screens/Join/JoinMore";

const handlers = [
  rest.put(`${BASE_URL}/user/more`, (req, res, ctx) => {
    return res(ctx.json({ message: "SUCCESS" }), ctx.delay(10));
  })
];
const server = setupServer(...handlers);
const mockNavigate = jest.fn();

jest.mock("react-router-dom", () => ({
  ...jest.requireActual("react-router-dom"),
  useNavigate: () => mockNavigate
}));

const initialState = {
  userInfo: { area: "광주시 광산구", likeCategorys: ["의류", "기타", "식품"] }
};

describe("추가정보입력 페이지", () => {
  let addressInput;

  let nextBtn;

  beforeAll(() => server.listen());

  beforeEach(() => {
    renderWithProviders(
      <MemoryRouter>
        <JoinMore />
      </MemoryRouter>,
      {
        preloadedState: {
          auth: initialState
        }
      }
    );
    addressInput = screen.getByPlaceholderText("주소를 검색해주세요");
    nextBtn = screen.getByText("설정");
  });

  afterEach(() => server.resetHandlers());

  afterAll(() => server.close());

  test("추가정보 입력후 라우팅", async () => {
    userEvent.click(nextBtn);

    await waitFor(() => expect(mockNavigate).toBeCalledTimes(1));
    await waitFor(() => expect(mockNavigate).toBeCalledWith("/"));
  });

  test("초기값 주소", async () => {
    await screen.findByDisplayValue("광주시 광산구");
  });

  test("초기값 카테고리", () => {
    expect(screen.getByText("의류")).toHaveClass("selected");
    expect(screen.getByText("기타")).toHaveClass("selected");
    expect(screen.getByText("식품")).toHaveClass("selected");
  });

  test("카테고리 선택", () => {
    userEvent.click(screen.getByText("홈인테리어"));
    expect(screen.getByText("홈인테리어")).toHaveClass("selected");

    expect(screen.getByText("기타")).toHaveClass("selected");
    userEvent.click(screen.getByText("기타"));
    expect(screen.getByText("기타")).not.toHaveClass("selected");

    expect(screen.getByText("의류")).toHaveClass("selected");
    userEvent.click(screen.getByText("의류"));
    expect(screen.getByText("의류")).not.toHaveClass("selected");
  });
});
