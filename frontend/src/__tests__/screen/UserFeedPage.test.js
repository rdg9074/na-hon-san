import React from "react";
import { rest } from "msw";
import { setupServer } from "msw/node";
import {
  getByAltText,
  getByText,
  render,
  screen,
  waitFor
} from "@testing-library/react";
import renderWithProviders from "@utils/test-utils";
import userEvent from "@testing-library/user-event";
import { MemoryRouter } from "react-router-dom";
import { BASE_URL } from "@apis/";
import UserFeedPage from "@screens/UserFeedPage";
import FollowList from "@components/common/UserFeed/FollowList";

const handlers = [
  rest.get(`${BASE_URL}/userFeed/post/test`, (req, res, ctx) => {
    return res(
      ctx.json({
        data: [
          {
            id: "test",
            nickname: "비밀번호는 test 입니다.",
            profileMsg: "하이하이",
            profileImg: null,
            followCount: 1,
            followerCount: 0
          }
        ],
        message: "SUCCESS"
      }),
      ctx.delay(10)
    );
  })
];
const server = setupServer(...handlers);
const mockNavigate = jest.fn();

describe("유저 피드", () => {
  beforeAll(() => server.listen());

  beforeEach(() => {
    renderWithProviders(
      <MemoryRouter>
        <UserFeedPage />
      </MemoryRouter>
    );
  });

  afterEach(() => {
    server.resetHandlers();
  });

  afterAll(() => server.close());

  test("유저 정보 받기", async () => {
    await screen.findByText("test");
    await screen.findByText("비밀번호는 test 입니다.");
    await screen.findByText("하이하이");
    await screen.findByText(0);
    await screen.findByText(1);
  });

  test("배경 받아오기", async () => {});

  test("세팅 버튼 클릭", async () => {
    const btn = screen.getByAltText("set");
    userEvent.click(btn);

    await waitFor(() => expect(mockNavigate).toBeCalledTimes(1));
    await waitFor(() => expect(mockNavigate).toBeCalledWith("/"));
  });

  const handleClick = jest.fn();
  test("팔로우 버튼 클릭", async () => {
    const followBtn = screen.getByText("팔로잉");
    userEvent.click(followBtn);
    expect(render(<FollowList followModal="팔로잉" />));
  });

  test("팔로우 버튼 클릭", async () => {
    const followerBtn = screen.getByText("팔로워");
    userEvent.click(followerBtn);
    expect(render(<FollowList followModal="팔로워" />));
  });

  test("꿀팁 카테고리", async () => {
    const tip = screen.getByText("꿀팁보기");
    userEvent.click(tip);
    expect(handleClick).toHaveBeenCalledTimes(1);
  });

  test("꿀딜 카테고리", async () => {
    const deal = screen.getByText("꿀딜보기");
    userEvent.click(deal);
    expect(handleClick).toHaveBeenCalledTimes(1);
  });

  test("유저 에러", async () => {
    server.use(
      rest.get(`${BASE_URL}/userFeed/post/test`, (req, res, ctx) => {
        return res(ctx.json({ message: "FAIL" }));
      })
    );
    await waitFor(() => expect(mockNavigate).toBeCalledTimes(1));
    await waitFor(() => expect(mockNavigate).toBeCalledWith("/"));
  });
});
