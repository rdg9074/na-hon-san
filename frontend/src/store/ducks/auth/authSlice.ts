import { createSlice } from "@reduxjs/toolkit";

interface InitialStateType {
  userId: string;
}

const initialState: InitialStateType = {
  userId: ""
};

export const authSlice = createSlice({
  name: "auth",
  initialState,
  reducers: {
    setUserId: (state, action) => {
      state.userId = action.payload.userId;
    }
  }
});

export const { setUserId } = authSlice.actions;

export default authSlice.reducer;
