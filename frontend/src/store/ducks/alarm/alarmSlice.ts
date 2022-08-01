import { createSlice } from "@reduxjs/toolkit";
import { AlramType } from "./alarm.type";
import { getAlarmList } from "./alarmThunk";

export interface Alarm {}

interface InitialStateType {
  count: number;
  list: Array<AlramType>;
  loading: boolean;
}
const initialState: InitialStateType = {
  count: 0,
  list: [],
  loading: false
};

export const alarmSlice = createSlice({
  name: "alarm",
  initialState,
  reducers: {
    setAlarmCount: (state, action) => {
      state.count = action.payload.count;
    },
    deleteAlarm: (state, action) => {
      state.list = state.list.filter(alarm => alarm.idx !== action.payload);
    }
  },
  extraReducers: builder => {
    builder.addCase(getAlarmList.fulfilled, (state, { payload }) => {
      state.list = payload.data;
      state.loading = true;
    });
  }
});

export const { setAlarmCount, deleteAlarm } = alarmSlice.actions;
export default alarmSlice.reducer;
