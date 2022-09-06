/// <reference types="cypress" />

describe("회원가입 테스트", () => {
  const emailInputProcess = () => {
    cy.get(".form__input").type("ssafy@naver.com");
    cy.get(".form__btn").click();
    cy.location().should(location => {
      expect(location.pathname).to.eq("/join/chkEmail");
    });
    cy.get("#chk-email").contains("인증 이메일을 보냈습니다.");
  };

  const eamilChkProcess = () => {
    emailInputProcess();
    cy.get("#chk-email").contains("인증 이메일을 보냈습니다.");
    cy.get(".form__input").type("012345");
    cy.get(".form__btn").click();

    cy.location().should(location => {
      expect(location.pathname).to.eq("/join/detail");
    });
    cy.get("#join-detail").contains("회원정보를 입력해주세요");
  };

  const correctJoinProcess = () => {
    eamilChkProcess();
    cy.get("#join-detail").contains("회원정보를 입력해주세요");
    cy.get(".form__input").eq(1).type("ssafy");
    cy.get(".form__btn--dupli").click();
    cy.get(".form__input").eq(2).type("!Qwer1234");
    cy.get(".form__input").eq(3).type("!Qwer1234");
    cy.get(".form__btn").click();
    cy.location().should(location => {
      expect(location.pathname).to.eq("/join/welcome");
    });
    cy.get("#msg-page-layout").contains("ssafy님, 환영합니다");
  };

  const moveMoreInfoProcess = () => {
    correctJoinProcess();
    cy.get(".footer__btn").last().click();
    cy.location().should(location => {
      expect(location.pathname).to.eq("/join/more");
    });
  };
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
          id: "ssafy@naver.com",
          number: "012345",
          type: "0"
        }
      },
      {
        statusCode: 200,
        body: { message: "SUCCESS" }
      }
    );
    cy.intercept(
      {
        method: "POST",
        url: `**/api/user*`
      },
      {
        statusCode: 200,
        body: { message: "SUCCESS" }
      }
    );
    cy.intercept(
      {
        method: "POST",
        url: `**/api/user/login*`
      },
      {
        statusCode: 200,
        body: {
          data: {
            id: "ssafy@naver.com",
            nickname: "ssafy",
            area: null,
            followOpen: true,
            followerOpen: true,
            likeNotice: true,
            followNotice: true,
            commentNotice: true,
            replyNotice: true,
            profileMsg: null,
            social: "normal",
            profileImg: null
          },
          message: "SUCCESS"
        }
      }
    );
    cy.intercept(
      {
        method: "GET",
        url: `**/api/user/more*`
      },
      {
        statusCode: 200,
        body: {
          data: {
            userId: "ssafy",
            area: "전북 전주시 완산구",
            categorys: ["의류", "가전디지털"]
          },
          message: "SUCCESS"
        }
      }
    );
    cy.intercept(
      {
        method: "PUT",
        url: `**/api/user/more*`
      },
      {
        statusCode: 200,
        body: {
          message: "SUCCESS"
        }
      }
    );
    cy.visit("https://i7c208.p.ssafy.io/join");
  });

  describe("이메일 체크 페이지", () => {
    it("중복된 이메일 체크", () => {
      cy.get(".form__input").type("pwlsghq@naver.com");
      cy.get(".form__btn").click();
      cy.get(".form").contains("이미 존재하는 아이디입니다.");
    });

    it("이메일 형식 체크", () => {
      cy.get(".form__input").type("test");
      cy.get(".form__btn").click();
      cy.get(".form").contains("이메일 형식을 확인해주세요.");
    });

    it("클릭시 인증페이지로 이동", () => {
      cy.get(".form__input").type("ssafy@naver.com");
      cy.get(".form__btn").click();
      cy.location().should(location => {
        expect(location.pathname).to.eq("/join/chkEmail");
      });
    });
  });

  describe("인증번호 페이지", () => {
    beforeEach(() => {
      emailInputProcess();
    });
    // it("인증코드 타이머 3분", () => {
    //   cy.wait(1000 * 180);
    //   cy.get(".form__time").should("have.text", "0:00");
    //   cy.get(".form").contains("인증코드를 새로 발급 받아주세요.");
    // });
    it("이전에 입력한 이메일 확인", () => {
      cy.get("#chk-email").contains("ssafy@naver.com");
    });

    it("인증코드 일치시", () => {
      cy.get(".form__input").type("012345");
      cy.get(".form__btn").click();

      cy.location().should(location => {
        expect(location.pathname).to.eq("/join/detail");
      });
    });
    it("인증코드 비일치시", () => {
      cy.get(".form__input").type("000000");
      cy.get(".form__btn").click();

      cy.location().should(location => {
        expect(location.pathname).to.eq("/join/chkEmail");
      });
    });
  });

  describe("회원가입 페이지", () => {
    beforeEach(() => {
      eamilChkProcess();
    });

    it("닉네임 중복o", () => {
      cy.get(".form__input").eq(1).type("진진자라");
      cy.get(".form__btn--dupli").click();
      cy.get("#join-detail").contains("이미 사용중인 닉네임입니다.");
    });

    it("닉네임 중복x", () => {
      cy.get(".form__input").eq(1).type("진짜특이한닉네임");
      cy.get(".form__btn--dupli").click();
      cy.get("#join-detail").contains("사용가능한 닉네임입니다.");
    });

    it("비밀번호 정규표현식", () => {
      cy.get(".form__input").eq(2).type("abc1234");
      cy.get("#join-detail").contains(
        "숫자,대소문자,특수문자를 혼합하여 8~16자리로 입력해주세요."
      );
    });
    it("비밀번호 한번 더 확인", () => {
      cy.get(".form__input").eq(2).type("abc1234");
      cy.get(".form__input").eq(3).type("abc12345");
      cy.get("#join-detail").contains(
        "숫자,대소문자,특수문자를 혼합하여 8~16자리로 입력해주세요."
      );
      cy.get("#join-detail").contains("비밀번호가 일치하지 않습니다.");
    });
    it("정상 회원가입", () => {
      cy.get(".form__input").eq(1).type("ssafy");
      cy.get(".form__btn--dupli").click();
      cy.get(".form__input").eq(2).type("!Qwer1234");
      cy.get(".form__input").eq(3).type("!Qwer1234");
      cy.get(".form__btn").click();
      cy.location().should(location => {
        expect(location.pathname).to.eq("/join/welcome");
      });
      cy.get("#msg-page-layout").contains("ssafy님, 환영합니다");
    });
  });

  describe("환영 페이지", () => {
    it("웰컴 페이지에서 홈으로 라우팅", () => {
      correctJoinProcess();
      cy.get(".footer__btn").first().click();
      cy.location().should(location => {
        expect(location.pathname).to.eq("/");
      });
    });
    it("웰컴 페이지에서 추가정보으로 라우팅", () => {
      moveMoreInfoProcess();
    });
  });

  describe("추가 정보 입력 페이지", () => {
    beforeEach(() => {
      moveMoreInfoProcess();
    });
    it("지역 초기 바인딩", () => {
      cy.get(".form__input").should("have.value", "전북 전주시 완산구");
    });
    it("관심카테고리 조회", () => {
      cy.get("#join-more").contains("의류").should("have.class", "selected");
      cy.get("#join-more")
        .contains("식품")
        .should("not.have.class", "selected");
      cy.get("#join-more")
        .contains("가전디지털")
        .should("have.class", "selected");
    });
    it("설정 완료시 홈 라우팅", () => {
      cy.get(".form__btn--submit").click();
      cy.location().should(location => {
        expect(location.pathname).to.eq("/");
      });
    });
  });
});
