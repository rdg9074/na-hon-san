/// <reference types='cypress' />

const { wait } = require("@testing-library/user-event/dist/utils");

const login = () => {
  cy.get(`#main-nav-bar .right-nav__link`).first().click();
  cy.get("#login .form__input").first().type("test");
  cy.get("#login .form__input").last().type("test");
  cy.get("#login .form__btn").click();
  cy.wait(500);
};

const logout = async () => {
  cy.get("#main-nav-bar .right-nav__link").last().click();
  cy.get("#profile-tool-tip .content").last().click();
  cy.wait(500);
};

context("마이 페이지", () => {
  after(() => {
    logout();
  });

  describe("마이페이지 전체(내계정)", () => {
    before(() => {
      cy.visit("https://i7c208.p.ssafy.io/userfeed/test/");
      login();
      cy.visit("https://i7c208.p.ssafy.io/userfeed/test/");
    });

    it("페이지 렌더링", () => {
      cy.get(".info__nickname").contains("test");
      cy.get(".info__state__textarea").contains("state");
    });

    it("팔로우 DM 버튼 유무", () => {
      cy.contains("팔로우").should("not.exist");
      cy.contains("DM").should("not.exist");
    });

    it("꿀팁 보기 포커싱", () => {
      cy.get(".active").contains("꿀팁보기").should("exist");
      cy.get(".active").contains("2").should("exist");
    });

    it("게시글 호버", () => {
      cy.get("#feed-item").trigger("mouseover");
      cy.get(".feed-info").should("exist");
      cy.get("#feed-item").trigger("mousedown");
    });

    it("게시글 클릭", () => {
      cy.get("#feed-item").first().click();
      cy.url("include", "tip/detail/");
      cy.go(-1);
    });

    it("꿀딜 보기 포커싱", () => {
      cy.contains("꿀딜보기").click();
      cy.get(".active").contains("1").should("exist");
    });

    it("게시글 호버", () => {
      cy.get("#feed-item").trigger("mouseover");
      cy.get(".feed-info").should("exist");
      cy.get("#feed-item").trigger("mousedown");
    });

    it("게시글 클릭", () => {
      cy.get("#feed-item").first().click();
      cy.url("include", "deal/detail/");
      cy.go(-1);
    });
  });

  describe("마이페지이지 계정설정", () => {
    it("인증 페이지 라우팅 및 인증확인", () => {
      cy.get('[alt="set"]').click();
      cy.url("eq", "https://i7c208.p.ssafy.io/account");
      cy.get(".form__input").type("test1{enter}");
      cy.contains("비밀번호가 일치하지 않습니다.").should("exist");
      cy.get(".form__input").type("{backspace}{enter}");
      cy.url("eq", "https://i7c208.p.ssafy.io/account/set");
    });

    it("닉네임 변경", () => {
      cy.get("input[type=text]").type("11");
      cy.get("button").contains("중복확인").click();
      cy.contains("중복된 닉네임입니다.");
      cy.get("input[type=text]").type("11");
      cy.get("button").contains("중복확인").click();
      cy.contains("사용 가능한 닉네임입니다.");
    });

    it("알림 푸쉬 토글", () => {
      cy.get(".main-noti__toggle-btn").first().click();
      cy.get(".active-toggle-bar").should("exist");
      cy.get(".main-noti__toggle-btn").first().click();
    });

    it("팔로워 공개범위 토글", () => {
      cy.get(".main-noti__toggle-btn").last().click();
      cy.get(".active-toggle-bar").should("exist");
      cy.get(".main-noti__toggle-btn").last().click();
    });

    it("비밀번호 변경 라우팅", () => {
      cy.get(".main-account__addinfo").contains("비밀번호").next().click();
      cy.url("eq", "https://i7c208.p.ssafy.io/reset/pw");
    });

    it("비밀번호 불일치", () => {
      cy.get("input[type=password]").first().type("testtest");
      cy.contains(
        "숫자,대소문자,특수문자를 혼합하여 8~16자리로 입력해주세요."
      ).should("exist");
      cy.get("input[type=password]").last().type("testtestt{enter}");
      cy.contains("비밀번호가 일치하지 않습니다.").should("exist");
    });

    it("비밀번호 사용 가능", () => {
      cy.get("input[type=password]").first().type("123!@#");
      cy.get("input[type=password]").last().type("{backspace}123!@#");
      cy.contains(
        "숫자,대소문자,특수문자를 혼합하여 8~16자리로 입력해주세요."
      ).should("not.exist");
      cy.contains("비밀번호가 일치하지 않습니다.").should("not.exist");
    });

    it("지역 및 태그 변경 라우팅", () => {
      cy.go(-1);
      cy.get(".main-account__addinfo").contains("변경하기").first().click();
      cy.url("eq", "https://i7c208.p.ssafy.io/join/more");
    });

    it("지역 카테고리 버튼 활성화", () => {
      cy.wait(300);
      cy.get("button").contains("의류").click();
      cy.wait(300);
      cy.get(".selected").click();
    });

    it("변경후 메인페이지 라우팅", () => {
      cy.get("button").contains("설정").click();
      cy.url("eq", "https://i7c208.p.ssafy.io/");
    });
  });

  describe("마이페이지 전체(상대방 계정)", () => {
    before(() => {
      cy.visit("https://i7c208.p.ssafy.io/userfeed/test11/");
    });

    it("팔로우 DM 버튼 유무", () => {
      cy.contains("팔로우").should("exist");
      cy.contains("DM").should("exist");
    });

    it("팔로우 언팔로우 토글 및 숫자 증가", () => {
      cy.contains("팔로우").click();
      cy.contains("언팔로우").should("exist");
      cy.get(".info__follow").first().contains(1);
      cy.contains("언팔로우").click();
      cy.contains("팔로우").should("exist");
      cy.get(".info__follow").first().contains(0);
    });

    it("DM 라우팅 확인 및 전송 테스트", () => {
      cy.get(".info__btn").contains("DM").click();
      cy.url().should(
        "eq",
        "https://i7c208.p.ssafy.io/letters/detail?with=test11"
      );
      cy.get(".chat-input").type("test{enter}").should("have.text", "");
      cy.get("#chat").should("exist");
    });

    it("팔로워 공개 범위 설정 및 리스트 확인", () => {
      cy.visit("https://i7c208.p.ssafy.io/userfeed/test11/");
      cy.get(".info__follow").contains(0).click();
      cy.get("#follow-list").contains("비공개 설정입니다.").should("exist");
      cy.get(".list-footer").click();
      cy.get(".info__follow").contains(1).click();
      cy.get("#follow-list").contains("코딩 마스터").should("exist");
      cy.get(".list-footer").click();
    });
  });
});
