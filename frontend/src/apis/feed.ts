import API from "./index";

export const getPopUsers = async () => {
  const accessToken = sessionStorage.getItem("access-token");
  const res = await API.get("/mainFeed/user", {
    headers: { Authorization: `${accessToken}` }
  });
  return res.data;
};

export const getHoneyDealList = async () => {
  const accessToken = sessionStorage.getItem("access-token");
  const res = await API.get("/mainFeed/honeyDeal", {
    headers: { Authorization: `${accessToken}` }
  });
  return res.data;
};
