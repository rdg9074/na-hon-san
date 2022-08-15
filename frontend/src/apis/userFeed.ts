import API from "./index";

export const addFollow = async (id: string) => {
  const accessToken = sessionStorage.getItem("access-token") as string;
  const res = await API.post(
    `/userFeed/follow/${id}`,
    {},
    {
      headers: {
        Authorization: accessToken
      }
    }
  );
  return res;
};

export const delFollow = async (id: string) => {
  const accessToken = sessionStorage.getItem("access-token") as string;
  const res = await API.delete(`/userFeed/follow/${id}`, {
    headers: {
      Authorization: accessToken
    }
  });
  return res;
};

export const readFollow = async (id: string) => {
  const accessToken = sessionStorage.getItem("access-token") as string;
  if (accessToken) {
    const res = await API.get(`/userFeed/follow/${id}`, {
      headers: {
        Authorization: accessToken
      }
    });
    return res;
  }
  const res = await API.get(`/userFeed/follow/${id}`);
  return res;
};

export const readFollower = async (id: string) => {
  const accessToken = sessionStorage.getItem("access-token") as string;
  if (accessToken) {
    const res = await API.get(`/userFeed/follower/${id}`, {
      headers: {
        Authorization: accessToken
      }
    });
    return res;
  }
  const res = await API.get(`/userFeed/follower/${id}`);
  return res;
};

export const searchFollow = async (idx: string, keyword: string) => {
  const res = await API.get(`/userFeed/follow/search/${idx}`, {
    params: { keyword }
  });
  return res;
};

export const searchFollower = async (idx: string, keyword: string) => {
  const res = await API.get(`/userFeed/follower/search/${idx}`, {
    params: { keyword }
  });
  return res;
};

export default {};
