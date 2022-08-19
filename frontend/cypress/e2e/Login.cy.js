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

  const mvFindPwProcess = () => {
    logout();
    cy.get("#main-nav-bar .right-nav__link").first().click();
    cy.get(".footer__link").last().click();
    cy.get("#find-pw").contains("비밀번호를 까먹으셨나요?");
  };

  const joinedEamilInputProcess = () => {
    mvFindPwProcess();
    cy.get("#main-nav-bar .right-nav__link").first().contains("로그인");
    cy.get(".form__input").type("ssafy@ssafy.com");
    cy.get(".form__btn").click();
    cy.location().should(location => {
      expect(location.pathname).to.eq("/find/pw/chkEmail");
    });
    cy.get("#chk-email").contains("인증 이메일을 보냈습니다.");
  };

  const correctJoinedUserProcess = () => {
    joinedEamilInputProcess();
    cy.get(".form__input").type("012345");
    cy.get(".form__btn").click();

    cy.location().should(location => {
      expect(location.pathname).to.eq("/reset/pw");
    });
    cy.get("#reset-pw").contains("비밀번호 재설정을 진행해주세요.");
  };
  const randomN = Math.round(Math.random() * 1000);
  const randomPassword = `!reset${randomN}`;

  beforeEach(() => {
    cy.intercept(
      {
        method: "GET",
        url: `**/api/user/auth*`
      },
      {
        statusCode: 200,
        body: { message: "FAIL" }
      }
    );
    cy.intercept(
      {
        method: "GET",
        url: `**/api/user/auth*`,
        query: {
          id: "ssafy@ssafy.com",
          number: "012345",
          type: "1"
        }
      },
      {
        statusCode: 200,
        body: { message: "SUCCESS" }
      }
    );
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

  describe("비밀번호 찾기 페이지", () => {
    beforeEach(() => {
      mvFindPwProcess();
    });

    it("가입안된 이메일 체크", () => {
      cy.get("#main-nav-bar .right-nav__link").first().contains("로그인");
      cy.get(".form__input").type("noUser@ssafy.com");
      cy.get(".form__btn").click();
      cy.get("#find-pw").contains("가입된 이메일이 아닙니다.");
    });

    it("가입된 이메일 체크", () => {
      joinedEamilInputProcess();
    });
  });

  describe("인증번호 페이지", () => {
    beforeEach(() => {
      joinedEamilInputProcess();
    });
    // it("인증코드 타이머 3분", () => {
    //   cy.wait(1000 * 180);
    //   cy.get(".form__time").should("have.text", "0:00");
    //   cy.get(".form").contains("인증코드를 새로 발급 받아주세요.");
    // });
    it("인증코드 일치시", () => {
      correctJoinedUserProcess();
    });
    it("인증코드 비일치시", () => {
      cy.get(".form__input").type("000000");
      cy.get(".form__btn").click();

      cy.location().should(location => {
        expect(location.pathname).to.eq("/find/pw/chkEmail");
      });
    });
  });

  describe("비밀번호 재설정 페이지", () => {
    beforeEach(() => {
      correctJoinedUserProcess();
    });
    it("비밀번호 정규표현식", () => {
      cy.get(".form__input").first().type("abc1234");
      cy.get("#reset-pw").contains(
        "숫자,대소문자,특수문자를 혼합하여 8~16자리로 입력해주세요."
      );
    });
    it("비밀번호 한번 더 확인", () => {
      cy.get(".form__input").first().type("abc1234");
      cy.get(".form__input").last().type("abc12345");
      cy.get("#reset-pw").contains(
        "숫자,대소문자,특수문자를 혼합하여 8~16자리로 입력해주세요."
      );
      cy.get("#reset-pw").contains("비밀번호가 일치하지 않습니다.");
    });
    it("정상 리셋", () => {
      cy.get(".form__input").first().type(randomPassword);
      cy.get(".form__input").last().type(randomPassword);
      cy.get(".form__btn").click();
      cy.location().should(location => {
        expect(location.pathname).to.eq("/login");
      });
      cy.get("#login .form__input").first().type("ssafy@ssafy.com");
      cy.get("#login .form__input").last().type(randomPassword);
      cy.get("#login .form__btn").click();
      cy.location().should(location => {
        expect(location.pathname).to.eq("/");
      });
    });
  });
});
