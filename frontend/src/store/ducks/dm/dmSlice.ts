import { createSlice } from "@reduxjs/toolkit";
import { getDmNoticeCount } from "./dmThunk";

interface IntialStateType {
  dmCount: number;
  noticeCount: number;
}

const initialState: IntialStateType = {
  dmCount: 0,
  noticeCount: 0
};

export const dmSlice = createSlice({
  name: "dm",
  initialState,
  reducers: {},
  extraReducers: builder => {
    builder.addCase(getDmNoticeCount.fulfilled, (state, { payload }) => {
      state.dmCount = payload.countDM;
      state.noticeCount = payload.countNotice;
    });
  }
});

// export const {} = dmSlice.actions;

export default dmSlice.reducer;
