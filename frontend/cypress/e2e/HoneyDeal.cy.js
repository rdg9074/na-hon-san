/// <reference types="cypress" />

const login = () => {
  cy.get(`#main-nav-bar .right-nav__link`).first().click();
  cy.get("#login .form__input").first().type("test");
  cy.get("#login .form__input").last().type("test");
  cy.get("#login .form__btn").click();
  cy.wait(500);
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

context("꿀딜페이지 테스트", () => {
  before(() => {
    cy.visit("https://i7c208.p.ssafy.io/deal/");
    cy.viewport(1440, 900);
    logout();
  });

  after(() => {
    logout();
  });

  describe("꿀딜페이지 (비로그인)", () => {
    it("꿀팁페이지 렌더링", () => {
      cy.contains("전체 지역").should("exist");
    });

    it("비로그인시 게시글 조회만 가능", () => {
      cy.get("#card").click();
      cy.get("[alt='like']").click();
      cy.url().should("include", "login");
      cy.go(-1);
    });
  });

  describe("꿀딜페이지 (로그인)", () => {
    before(() => {
      login();
      cy.visit("https://i7c208.p.ssafy.io/deal/");
    });

    beforeEach(() => {
      cy.viewport(1440, 900);
    });

    it("꿀팁페이지 렌더링", () => {
      cy.contains("광주 지역").should("exist");
    });

    it("카테고리 버튼 토글", () => {
      cy.get("button").contains("전체").click();
      cy.get(".selected").should("not.exist");
      cy.get("button").contains("의류").click();
      cy.get(".selected").should("exist");
      cy.get("button").contains("전체").click();
    });

    it("무한 스크롤", () => {
      cy.scrollTo("bottom");
      cy.wait(500);
      cy.scrollTo("bottom");
      cy.wait(500);
      cy.get(".card-main__title").contains("민트초코 디핑소스").should("exist");
    });

    it("좋아요 순 클릭", () => {
      cy.get(".deal-sort__list")
        .select("좋아요순")
        .should("have.value", "좋아요순");
    });

    it("조회 순 클릭", () => {
      cy.get(".deal-sort__list")
        .select("조회순")
        .should("have.value", "조회순");
    });

    it("거래 대기 / 거래 완료 토글", () => {
      cy.get("button")
        .contains("거래 완료")
        .click()
        .should("have.class", "active");
      cy.get("button").contains("거래 대기").click();
    });

    it("거래 검색 후 상세페이지 라우팅", () => {
      cy.get("input[type=text]").type("민트초코{enter}");
      cy.get(".card-main__title").contains("민트초코 디핑소스").click();
      cy.url().should("include", "/deal/detail");
      cy.contains("민트초코 디핑소스").should("exist");
    });

    describe("게시글 상세페이지", () => {
      it("팔로우 언팔로우 버튼 토글 및 좋아요 클릭", () => {
        cy.get(".header-info__btn").click();
        cy.get(".yellow").should("exist");
        cy.wait(500);
        cy.get(".header-info__btn").click();
        cy.get(".grey").should("exist");
        cy.get("[alt='like']").click();
        cy.get("[alt='like']").click();
      });

      it("지도 아이콘", () => {
        cy.get("[alt='mapicon']").should("exist");
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
    });

    describe("꿀딜 쓰기", () => {
      it("꿀딜쓰기 라우팅", () => {
        cy.go(-1);
        cy.get("button").contains("꿀딜쓰기").click();
        cy.url().should("include", "deal/create");
        cy.wait(300);
      });

      it("입력 없이 작성 버튼 눌럿을 때 에러", () => {
        cy.get("button").contains("작성").click();
        cy.contains("제목을 입력해주세요.").should("exist");
      });

      it("제목만 입력하고 작성했을 때", () => {
        cy.get(".deal-title").type("test-title");
        cy.get("button").contains("작성").click();
        cy.contains("꿀딜 게시글 썸네일은 필수에요.").should("exist");
      });

      it("취소버튼 클릭 꿀딜 메인페이지 라우팅", () => {
        cy.get("button").contains("취소").click();
        cy.url().should("eq", "https://i7c208.p.ssafy.io/deal");
      });
    });

    describe("본인 작성 게시글 조회", () => {
      it("게시글 검색 후 조회", () => {
        cy.get("input[type=text]").type("자유의 여신{enter}");
        cy.get(".card-main__title").contains("자유의 여신").click();
        cy.url().should("include", "/deal/detail");
        cy.contains("자유의 여신").should("exist");
      });

      it("본인 게시글 확인", () => {
        cy.get("[alt='like']").should("not.exist");
        cy.get("#mapmodal").should("exist");
      });

      it("수정 버튼 및 수정페이지 라우팅", () => {
        cy.get("[alt='edit']").click();
        cy.url().should("include", "/edit");
        cy.scrollTo("bottom");
        cy.get(".deal-title").should("have.value", "자유의 여신상 꿀딜");
        cy.contains("거래 대기").should("exist");
      });

      it("삭제 버튼", () => {
        cy.go(-1);
        cy.get("[alt='del']").should("exist");
      });
    });
  });
});
