import { createAsyncThunk } from "@reduxjs/toolkit";
import { reqDmNoticeCount } from "@apis/dm";
import { setAlarmCount } from "../alarm/alarmSlice";

export const getDmNoticeCount = createAsyncThunk(
  "dm/getDmAlarmCnt",
  async (payload, { dispatch }) => {
    const res = await reqDmNoticeCount();
    dispatch(setAlarmCount({ count: res.countNotice }));
    return res;
  }
);

export const test = {};
