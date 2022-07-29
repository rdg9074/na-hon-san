import { createAsyncThunk } from "@reduxjs/toolkit";
import { reqDmNoticeCount } from "@apis/dm";

export const getDmNoticeCount = createAsyncThunk(
  "dm/getDmAlarmCnt",
  async () => {
    const res = await reqDmNoticeCount();
    return res;
  }
);

export const test = {};
