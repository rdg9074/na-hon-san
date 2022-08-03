import API from "./index";

export const getTipList = async (
  category: string,
  keyword: string | null,
  sort: string,
  pageNum: number
) => {
  const res = await API.post("/honeyTip/list", {
    category,
    keyword,
    type: sort,
    pageNum,
    pageSize: 6
  });
  return res.data;
};

export const test = {};
