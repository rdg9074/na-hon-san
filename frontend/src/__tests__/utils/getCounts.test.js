import getCounts from "@utils/getCounts";

describe("getCounts함수 테스트", () => {
  it("1000이상", () => {
    expect(getCounts(1123)).toBe("1K");
    expect(getCounts(11123)).toBe("11K");
    expect(getCounts(111123)).toBe("111K");
  });
  it("10000이상", () => {
    expect(getCounts(1111123)).toBe("1M");
    expect(getCounts(11111123)).toBe("11M");
    expect(getCounts(111111123)).toBe("111M");
  });
  it("10000이상", () => {
    expect(getCounts(1111111123)).toBe("1B");
    expect(getCounts(11111111123)).toBe("11B");
    expect(getCounts(111111111123)).toBe("111B");
  });
});
