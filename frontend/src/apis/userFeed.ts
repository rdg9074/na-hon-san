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
  console.log(res);
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

export default {};
