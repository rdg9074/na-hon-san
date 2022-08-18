/// <reference types="cypress" />

describe("메인페이지 테스트", () => {
  const chkRouting = (ind, path, dir) => {
    cy.get(`#main-nav-bar .${dir}-nav__link`).eq(ind).click();
    cy.location().should(location => {
      expect(location.pathname).to.eq(path);
    });
  };

  const logout = () => {
    cy.get('[alt="나혼자잘산다로고"]');
    cy.get("body").then($body => {
      if ($body.find('[alt="유저프로필"]').length > 0) {
        cy.get('[alt="유저프로필"]').click();
        cy.get("#profile-tool-tip .content").last().click();
      }
    });
  };

  const login = () => {
    logout();
    cy.get(`#main-nav-bar .right-nav__link`).first().click();
    cy.get("#login .form__input").first().type("test");
    cy.get("#login .form__input").last().type("test");
    cy.get("#login .form__btn").click();
    cy.location().should(location => {
      expect(location.pathname).to.eq("/");
    });
  };
  beforeEach(() => {
    cy.visit("https://i7c208.p.ssafy.io/");
  });

  describe("라우팅", () => {
    it("로고 라우팅", () => {
      cy.get('[alt="나혼자잘산다로고"]').click();
      cy.location().should(location => {
        expect(location.pathname).to.eq("/");
      });
    });
    it("홈 라우팅", () => {
      chkRouting(0, "/", "left");
    });

    it("꿀팁 라우팅", () => {
      chkRouting(1, "/tip", "left");
    });

    it("피드 라우팅(비로그인) ", () => {
      logout();
      chkRouting(2, "/login", "left");
    });

    it("꿀딜 라우팅", () => {
      chkRouting(3, "/deal", "left");
    });

    it("로그인 라우팅", () => {
      chkRouting(0, "/login", "right");
    });

    it("회원가입 라우팅", () => {
      chkRouting(1, "/join", "right");
    });
  });

  describe("로그인", () => {
    beforeEach(() => {
      login();
    });

    it("피드 라우팅(로그인) ", () => {
      chkRouting(2, "/feed", "left");
    });

    it("알림 툴팁 보이기", () => {
      cy.get(`#main-nav-bar .right-nav__link`).first().click();
      cy.get(`#main-nav-bar .right-nav__link`).first().find("#alarm-tool-tip");
    });

    it("쪽지함 라우팅", () => {
      chkRouting(1, "/letters", "right");
    });

    it("마이페이지 라우팅 ", () => {
      cy.get('[alt="유저프로필"]').click();
      cy.get("#profile-tool-tip .content").first().click();
      cy.location().should(location => {
        expect(location.pathname).to.eq("/userfeed/test");
      });
    });
  });

  describe("메인페이지 꿀팁", () => {
    it("꿀팁 리스트 확인", () => {
      cy.contains("가장 많이 본 꿀팁!")
        .next()
        .children("#card")
        .should("have.length", 6);
    });

    it("꿀팁 리스트 클릭시 해당 게시물이동", () => {
      cy.contains("가장 많이 본 꿀팁!")
        .next()
        .children("#card")
        .first()
        .children(".card-main")
        .children(".card-main__title")
        .then($title => {
          const title = $title.text();
          $title.click();
          cy.contains(title);
        });
    });
  });
  describe("메인페이지 꿀딜", () => {
    it("꿀딜 리스트 확인", () => {
      cy.contains(/.*(에서 가장 많이 본 꿀딜!)$/)
        .next()
        .children("#card")
        .should("have.length", 6);
    });

    it("꿀딜 리스트 클릭시 해당 게시물이동", () => {
      cy.contains(/.*(에서 가장 많이 본 꿀딜!)$/)
        .next()
        .children("#card")
        .first()
        .children(".card-main")
        .children(".card-main__title")
        .then($title => {
          const title = $title.text();
          $title.click();
          cy.contains(title);
        });
    });
  });
});
