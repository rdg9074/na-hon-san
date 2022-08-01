import { createAsyncThunk } from "@reduxjs/toolkit";
import { reqAlarmList } from "@apis/alarm";

export const getAlarmList = createAsyncThunk("alarm/getAlramList", async () => {
  const res = await reqAlarmList();
  return res;
});

export const test = {};
