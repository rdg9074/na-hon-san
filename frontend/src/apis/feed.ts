import API from "./index";

export const getPopUsers = async () => {
  const res = await API.get("/mainFeed/user");
  return res.data;
};

export const test = {};
