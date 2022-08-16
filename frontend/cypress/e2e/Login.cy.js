/// <reference types="cypress" />

describe("로그인 테스트", () => {
  const chkRouting = (ind, path, dir) => {
    cy.get(`#main-nav-bar .${dir}-nav__link`).eq(ind).click();
    cy.location().should(location => {
      expect(location.pathname).to.eq(path);
    });
  };

  const logout = async () => {
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

  it("로그인", () => {
    login();
    cy.get('[alt="알림"]');
    cy.get('[alt="쪽지함"]');
  });

  it("로그아웃", () => {
    logout();
    cy.get("#main-nav-bar .right-nav__link").first().contains("로그인");
    cy.get("#main-nav-bar .right-nav__link").last().contains("회원가입");
  });
});
