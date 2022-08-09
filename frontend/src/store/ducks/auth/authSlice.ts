import { createSlice } from "@reduxjs/toolkit";
import { getUserInfo } from "./authThunk";

export interface UserInfoType {
  id: string;
  nickname: string;
  area: string | null;
  followOpen: boolean;
  followerOpen: boolean;
  likeNotice: boolean;
  followNotice: boolean;
  commentNotice: boolean;
  replyNotice: boolean;
  profileMsg: string | null;
  profileImg: string | null;
  backgroundImg: string | null;
  likeCategorys: Array<string> | null;
}
interface InitialStateType {
  tmpId: string;
  userInfo: UserInfoType | null;
}

const initialState: InitialStateType = {
  tmpId: "",
  userInfo: null
};

export const authSlice = createSlice({
  name: "auth",
  initialState,
  reducers: {
    setTmpId: (state, action) => {
      state.tmpId = action.payload.tmpId;
    },
    resetUserInfo: state => {
      state.userInfo = null;
      sessionStorage.removeItem("access-token");
    },
    setMoreInfo: (state, action) => {
      if (state.userInfo) {
        state.userInfo.area = action.payload.area;
        state.userInfo.likeCategorys = action.payload.likeCategorys;
      }
    }
  },
  extraReducers: builder => {
    builder.addCase(getUserInfo.fulfilled, (state, { payload }) => {
      state.userInfo = payload;
    });
  }
});

export const { setTmpId, resetUserInfo, setMoreInfo } = authSlice.actions;

export default authSlice.reducer;
