import React from "react";
import { rest } from "msw";
import { setupServer } from "msw/node";
import { getByText, screen } from "@testing-library/react";
import renderWithProviders from "@utils/test-utils";
import userEvent from "@testing-library/user-event";
import { MemoryRouter } from "react-router-dom";
import { BASE_URL } from "@apis/";
import DealPage from "@screens/DealPage";

const handlers = [
  rest.get(`${BASE_URL}/honeyDeal/view`, (req, res, ctx) => {
    return res(
      ctx.json({
        data: [
          {
            idx: 111,
            userNickname: "test",
            userProfileImg: null,
            category: "기타",
            title: "더미 카드입니다.",
            state: "거래 대기",
            bannerImg: null,
            likes: 1,
            comment: 2,
            view: 3
          }
        ],
        message: "SUCCESS"
      }),
      ctx.delay(10)
    );
  })
];
const server = setupServer(...handlers);

const mockIntersectionObserver = class {
  constructor(callback, options) {}

  observe(target) {
    return null;
  }

  unobserve(target) {
    return null;
  }

  disconnect() {
    return null;
  }
};

window.IntersectionObserver = mockIntersectionObserver;

describe("딜페이지", () => {
  beforeAll(() => server.listen());

  beforeEach(() => {
    renderWithProviders(
      <MemoryRouter>
        <DealPage />
      </MemoryRouter>
    );
  });

  afterEach(() => {
    server.resetHandlers();
  });

  afterAll(() => server.close());

  test("카테고리 선택", async () => {
    expect(screen.getByText("기타")).toHaveClass("selected");
    userEvent.click(screen.getByText("기타"));
    expect(screen.getByText("기타")).not.toHaveClass("selected");

    expect(screen.getByText("의류")).toHaveClass("selected");
    userEvent.click(screen.getByText("의류"));
    expect(screen.getByText("의류")).not.toHaveClass("selected");
  });

  test("전체카테고리 선택/해제", async () => {
    expect(screen.getByText("전체")).toHaveClass("selected");
    expect(screen.getByText("의류")).toHaveClass("selected");
    expect(screen.getByText("기타")).toHaveClass("selected");

    userEvent.click(screen.getByText("전체"));
    expect(screen.getByText("전체")).not.toHaveClass("selected");
    expect(screen.getByText("의류")).not.toHaveClass("selected");
    expect(screen.getByText("기타")).not.toHaveClass("selected");
  });
});
