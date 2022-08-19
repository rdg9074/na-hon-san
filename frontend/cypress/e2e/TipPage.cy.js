/// <reference types="cypress" />

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

describe("꿀딜페이지 비로그인 테스트", () => {
  before(() => {
    cy.visit("https://i7c208.p.ssafy.io/tip/");
  });

  it("비로그인시 게시글 조회", () => {
    cy.get("#card").click();
    cy.get("[alt='like']").click();
    cy.url().should("include", "login");
    cy.go(-1);
    cy.get(".header-info__btn").click();
    cy.url().should("include", "login");
    cy.go(-1);
    cy.get("[alt='edit']").should("not.exist");
    cy.get("[alt='del']").should("not.exist");
  });
});

describe("꿀팁페이지 로그인 테스트", () => {
  before(() => {
    cy.visit("https://i7c208.p.ssafy.io/");
    login();
    cy.visit("https://i7c208.p.ssafy.io/tip/");
  });

  it("꿀팁페이지 이동", () => {
    cy.get(".tag-item").should("exist");
    cy.get(".tip-sort__list").should("exist");
  });
  it("꿀팁페이지 리스트 조회", () => {
    cy.scrollTo("bottom");
    cy.wait(500);
    cy.get("#card-list").should("exist");
  });
  it("꿀팁페이지 카테고리별 조회", () => {
    cy.get(".tag-item")
      .contains("꿀시피")
      .parent()

      .click()
      .should("have.class", "active");
    cy.get(".tag-item").contains("꿀템").click();
    cy.get(".tag-item")
      .contains("꿀생")
      .parent()
      .click()
      .should("have.class", "active");
  });
  it("꿀팁페이지 게시글 조회 조회순", () => {
    cy.get(".tip-sort__list").select("조회순").should("have.value", "조회순");
  });
  it("꿀팁페이지 게시글 조회 좋아요순 ", () => {
    cy.get(".tip-sort__list")
      .select("좋아요순")
      .should("have.value", "좋아요순");
  });

  describe("게시글 상세페이지", () => {
    it("게시글 검색 후 조회", () => {
      cy.get("input[type=text]").type("부탁해요{enter}");
      cy.get(".card-main__title").contains("부탁해요").click();
      cy.url().should("include", "/tip/detail");
      cy.contains("부탁해요").should("exist");
    });
    it("게시글 작성자 일 때", () => {
      cy.get("[alt='edit']").should("exist");
      cy.get("[alt='del']").should("exist");
    });

    it("댓글 작성", () => {
      cy.get("input[type=text]").type("test{enter}");
      cy.get("#comment-item").contains("test").should("exist");
    });
    it("댓글 수정", () => {
      cy.get("button").contains("수정").click();
      cy.wait(300);
      cy.get("input[type=text]").last().type("tes{enter}");
      cy.contains("testtes").should("exist");
    });
    it("댓글 삭제", () => {
      cy.get("button").contains("삭제").click();
    });
    it("본인 게시글 확인", () => {
      cy.get("[alt='like']").should("not.exist");
    });

    it("수정", () => {
      cy.get("[alt='edit']").click();
      cy.url().should("include", "/edit");
      cy.scrollTo("bottom");
      cy.get(".tip-title").should("have.value", "좋아요 부탁해요!");
    });

    it("삭제", () => {
      cy.go(-1);
      cy.get("[alt='del']").should("exist");
    });
  });
  describe("본인 게시글이 아닐 시", () => {
    it("타인 게시글 조회", () => {
      cy.visit("https://i7c208.p.ssafy.io/tip/");
      cy.get("input[type=text]").type("개발자{enter}");
      cy.get(".card-main__title").contains("개발자").click();
      cy.url().should("include", "/tip/detail");
      cy.contains("개발자").should("exist");
    });
    it("타인 게시글 확인", () => {
      cy.get(".header-info__btn").click();
      cy.get(".yellow").should("exist");
      cy.wait(500);
      cy.get(".header-info__btn").click();
      cy.get(".grey").should("exist");
      cy.get("[alt='like']").click();
      cy.get("[alt='like']")
        .should("have.attr", "src")
        .should("include", "Heart");
      cy.get("[alt='like']").click();
      cy.get("[alt='like']")
        .should("have.attr", "src")
        .should("include", "Empty");
    });
  });
});
