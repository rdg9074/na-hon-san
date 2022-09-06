import React from "react";
import { rest } from "msw";
import { setupServer } from "msw/node";
import { screen, waitFor } from "@testing-library/react";
import renderWithProviders from "@utils/test-utils";
import userEvent from "@testing-library/user-event";
import { MemoryRouter } from "react-router-dom";
import { BASE_URL } from "@apis/";
import Letters from "@screens/Letters";

const handlers = [
  rest.get(`${BASE_URL}/dm`, (req, res, ctx) => {
    return res(
      ctx.json({
        data: [
          {
            idx: 48,
            type: null,
            fromId: "test",
            toId: "ssafy",
            content: "테스트 dm 리스트업",
            image: null,
            read: true,
            count: 0,
            time: "2022-07-26T21:49:06",
            nickname: "비밀번호는 test 입니다."
          },{
            idx: 4,
            type: null,
            fromId: "test",
            toId: "ssafy",
            content: "테스트 dm 리스트업2",
            image: null,
            read: true,
            count: 999,
            time: "2022-07-26T21:49:06",
            nickname: "비밀번호는 test 입니다."
          }
        ],
        message: "SUCCESS"
      }),
      ctx.delay(10)
    );
  })
];
const server = setupServer(...handlers);

describe("쪽지함", () => {
  beforeAll(() => server.listen());

  beforeEach(() => {
    renderWithProviders(
      <MemoryRouter>
        <Letters />
      </MemoryRouter>
    );
  });

  afterEach(() => {
    server.resetHandlers();
  });

  afterAll(() => server.close());

  test("쪽지 리스트업", async () => {
    await screen.findByText("테스트 dm 리스트업");
  });

  test("쪽지 안읽은 갯수",async ()=>{
    await screen.findByText("9+")
  })
  // 차후 빈 쪽지함도 생각해보자 api
});
