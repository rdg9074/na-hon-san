import API from "./index";

export const reqDmNoticeCount = async () => {
  const accessToken = sessionStorage.getItem("access-token");
  const res = await API.get("/user/notice/count", {
    headers: { Authorization: `${accessToken}` }
  });
  return res.data;
};

export const getDmList = async () => {
  const accessToken = sessionStorage.getItem("access-token");
  const res = await API.get("/dm", {
    headers: { Authorization: `${accessToken}` }
  });
  return res.data;
};

export const getDmDetailList = async (withId: string, lastIdx: number) => {
  const accessToken = sessionStorage.getItem("access-token");
  const res = await API.get(`/dm/${withId}?lastIdx=${lastIdx}&pageSize=10`, {
    headers: { Authorization: `${accessToken}` }
  });
  return res.data;
};

export const sendDm = async (
  toNickname: string,
  content: string | null = null,
  image: string | null = null
) => {
  const accessToken = sessionStorage.getItem("access-token");
  const res = await API.post(
    "/dm",
    { toNickname, content, image },
    {
      headers: { Authorization: `${accessToken}` }
    }
  );
  return res.data;
};
