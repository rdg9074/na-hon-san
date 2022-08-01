import { createSlice } from "@reduxjs/toolkit";
import { getDmNoticeCount } from "./dmThunk";

interface IntialStateType {
  count: number;
}

const initialState: IntialStateType = {
  count: 0
};

export const dmSlice = createSlice({
  name: "dm",
  initialState,
  reducers: {},
  extraReducers: builder => {
    builder.addCase(getDmNoticeCount.fulfilled, (state, { payload }) => {
      state.count = payload.countDM;
    });
  }
});

// export const {} = dmSlice.actions;

export default dmSlice.reducer;
