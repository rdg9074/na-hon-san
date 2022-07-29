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

describe("추가정보입력 페이지", () => {
  let addressInput;

  let nextBtn;

  beforeAll(() => server.listen());

  beforeEach(() => {
    renderWithProviders(
      <MemoryRouter>
        <JoinMore />
      </MemoryRouter>
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
});
