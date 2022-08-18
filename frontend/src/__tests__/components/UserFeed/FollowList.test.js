import React from "react";
import { rest } from "msw";
import { setupServer } from "msw/node";
import {
  fireEvent,
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
import FollowList from "@components/common/UserFeed/FollowList";
import FollowItem from "@components/common/UserFeed/FollowItem";

const handlers = [
  rest.get(`${BASE_URL}/userFeed/post/test`, (req, res, ctx) => {
    return res(
      ctx.json({
        data: [
          {
            id: "test",
            nickname: "test",
            profileImg: null
          },
          {
            id: "ssafy",
            nickname: "ssafy",
            profileImg: null
          }
        ],
        message: "SUCCESS"
      }),
      ctx.delay(10)
    );
  })
];
const server = setupServer(...handlers);
const mockFn = jest.fn();

describe("팔로우 리스트", () => {
  beforeAll(() => server.listen());

  beforeEach(() => {
    renderWithProviders(
      <MemoryRouter>
        <FollowList followModal="팔로잉" />
      </MemoryRouter>
    );
  });

  afterEach(() => {
    server.resetHandlers();
  });

  afterAll(() => server.close());

  test("정보 받기", async () => {
    screen.findByText("test");
    screen.findByText("ssafy");
  });

  test("검색 입력", async () => {
    const searchBar = screen.getByPlaceholderText("검색");
    userEvent.type(searchBar, "test{enter}");
    await waitFor(() => expect(mockFn).toBeCalledTimes(1));
  });
});

describe("팔로워 리스트", () => {
  beforeAll(() => server.listen());

  beforeEach(() => {
    renderWithProviders(
      <MemoryRouter>
        <FollowList followModal="팔로워" />
      </MemoryRouter>
    );
  });

  afterEach(() => {
    server.resetHandlers();
  });

  afterAll(() => server.close());

  test("정보 받기", async () => {
    render(<FollowItem changed={change} />);
    screen.getAllByText("test");
  });

  test("검색 입력", async () => {
    const searchBar = screen.getByPlaceholderText("검색");
    userEvent.type(searchBar, "test{enter}");
    await waitFor(() => expect(mockFn).toBeCalledTimes(1));
  });
});
