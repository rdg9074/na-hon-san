import API from "./index";

export const reqAlarmList = async () => {
  const accessToken = sessionStorage.getItem("access-token");
  const res = await API.get("/user/notice", {
    headers: { Authorization: `${accessToken}` }
  });
  return res.data;
};

export const readAlarm = async (idx: number) => {
  const accessToken = sessionStorage.getItem("access-token");
  const res = await API.put(
    `/user/notice/${idx}`,
    {},
    {
      headers: { Authorization: `${accessToken}` }
    }
  );
};

export const reqDeleteAlarm = async (idx: number) => {
  const accessToken = sessionStorage.getItem("access-token");
  const res = await API.delete(`/user/notice/${idx}`, {
    headers: { Authorization: `${accessToken}` }
  });
};
