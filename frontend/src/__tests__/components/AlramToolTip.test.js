import React from "react";
import { rest } from "msw";
import { setupServer } from "msw/node";
import { screen } from "@testing-library/react";
import renderWithProviders from "@utils/test-utils";
import userEvent from "@testing-library/user-event";
import { MemoryRouter } from "react-router-dom";
import { BASE_URL } from "@apis/";
import AlarmToolTip from "@components/common/MainNavBar/AlarmToolTip";

const handlers = [
  rest.get(`${BASE_URL}/user/notice`, (req, res, ctx) => {
    return res(
      ctx.json({
        data: [
          {
            idx: 1,
            noticeType: "like",
            postIdx: 2,
            userId: "ssafy",
            fromUserId: "hyejin",
            fromUserNickname: "혜진",
            postType: "deal",
            read: true,
            time: "2022-07-20T10:19:46"
          },
          {
            idx: 2,
            noticeType: "follow",
            postIdx: null,
            userId: "ssafy",
            fromUserId: "jinho",
            fromUserNickname: "진호",
            postType: null,
            read: true,
            time: "2022-07-30T10:19:46"
          },
          {
            idx: 3,
            noticeType: "comment",
            postIdx: 2,
            userId: "ssafy",
            fromUserId: "gang",
            fromUserNickname: "강",
            postType: "deal",
            read: false,
            time: "2022-07-30T10:19:46"
          },
          {
            idx: 4,
            noticeType: "reply",
            postIdx: 2,
            userId: "ssafy",
            fromUserId: "gwangsuk",
            fromUserNickname: "광석",
            postType: "deal",
            read: false,
            time: "2022-07-30T10:19:46"
          }
        ],
        message: "SUCCESS"
      }),
      ctx.delay(10)
    );
  })
];
const server = setupServer(...handlers);

describe("알림툴팁", () => {
  let deleteBtn;
  beforeAll(() => server.listen());

  beforeEach(async () => {
    renderWithProviders(
      <MemoryRouter>
        <AlarmToolTip />
      </MemoryRouter>
    );

    await screen.findByText("혜진님이 회원님의 글을 좋아합니다.");
    await screen.findByText("진호님이 회원님을 팔로우하였습니다.");
    await screen.findByText("강님이 회원님의 글에 댓글을 남겼습니다.");
    await screen.findByText("광석님이 회원님의 댓글에 대댓글을 남겼습니다.");
    deleteBtn = await screen.findAllByAltText("삭제아이콘");
  });

  afterEach(() => {
    server.resetHandlers();
  });

  afterAll(() => server.close());

  test("알림 삭제", () => {
    userEvent.click(deleteBtn[0]);
    const deletedEle = screen.queryByText("혜진님이 회원님의 글을 좋아합니다.");
    expect(deletedEle).not.toBeInTheDocument();
  });
});
