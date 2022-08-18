import React from "react";
import { rest } from "msw";
import { setupServer } from "msw/node";
import { screen, fireEvent, waitFor } from "@testing-library/react";
import renderWithProviders from "@utils/test-utils";
import userEvent from "@testing-library/user-event";
import { MemoryRouter } from "react-router-dom";
import { BASE_URL } from "@apis/";
import ChatRoom from "@screens/ChatRoom";
import { wait } from "@testing-library/user-event/dist/utils";

const handlers = [
  rest.get(`${BASE_URL}/dm/ssafy`, (req, res, ctx) => {
    return res(
      ctx.json({
        data: [
          {
            idx: 8,
            type: "from",
            fromId: "test",
            toId: "ssafy",
            content: "받은 채팅 1 입니다.",
            image: null,
            read: true,
            count: null,
            time: "2022-07-22T11:01:30",
            nickname: "비밀번호는 test 입니다."
          }
        ],
        message: "SUCCESS"
      }),
      ctx.delay(10)
    );
  })
];

// const mockIntersectionObserver = class {
//   constructor(callback, options) {
//       this.viewPort = options.root
//       this.entries = []
//       this.viewPort.addEventListener('scroll', () => {
//           this.entries.map((entry) => {
//               entry.isIntersecting = this.isInViewPort(entry.target)
//           })
//           callback(this.entries, this)
//       })
//   }

//   isInViewPort(target) {
//       return true
//   }

//   observe(target) {
//       this.entries.push({ isIntersecting: false, target })
//   }

//   unobserve(target) {
//       this.entries = this.entries.filter((ob) => ob.target !== target)
//   }

//   disconnect() {
//       this.entries = []
//   }
// }

// window.IntersectionObserver = mockIntersectionObserver

const server = setupServer(...handlers);
jest.mock("react-router-dom", () => ({
  ...jest.requireActual("react-router-dom"), // use actual for all non-hook parts
  useParams: () => ({
    withId: "ssafy"
  })
}));
describe("채팅방", () => {
  let chatInput;
  beforeAll(() => server.listen());

  beforeEach(() => {
    const mockIntersectionObserver = jest.fn();
    mockIntersectionObserver.mockReturnValue({
      observe: () => null,
      unobserve: () => null,
      disconnect: () => null
    });
    window.IntersectionObserver = mockIntersectionObserver;
    renderWithProviders(
      <MemoryRouter initialEntries={["/letters/detail?with=ssafy"]}>
        <ChatRoom />
      </MemoryRouter>
    );
    chatInput = screen.getByPlaceholderText("채팅을 입력해주세요.");
  });

  afterEach(() => {
    server.resetHandlers();
  });

  afterAll(() => server.close());

  test("채팅 리스트업", async () => {
    await screen.findByText("받은 채팅 1 입니다.0");
  });

  test("채팅 전송", async () => {
    userEvent.type(chatInput, "새로운 채팅!!!");
    await screen.findByText("받은 채팅 1 입니다.0");
    userEvent.keyboard("{enter}");
    await screen.findByText("새로운 채팅!!!");
  });
});
