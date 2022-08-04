import { v4 } from "uuid";
import { getDealList, getTipList } from "@store/ducks/infinity/infinityThunk";
import { createSlice } from "@reduxjs/toolkit";
import { TipCondition, DealCondition } from "./infinity.type";

interface IntialStateType {
  conditionList: Array<TipCondition & DealCondition>;
  uuid: string;
  isEnd: boolean;
  isLoading: boolean;
  lastIdx: number | null;
  lastView: number | null;
  lastLikes: number | null;
}

const initialState: IntialStateType = {
  conditionList: [],
  uuid: v4(),
  isEnd: false,
  isLoading: false,
  lastIdx: null,
  lastView: null,
  lastLikes: null
};

export const infinitySlice = createSlice({
  name: "infinity",
  initialState,
  reducers: {
    setUuid: state => {
      state.uuid = v4();
    },
    setIsEnd: (state, { payload }) => {
      state.isEnd = payload;
    },
    setIsLoading: (state, { payload }) => {
      state.isLoading = payload;
    },
    setConditionList: (state, { payload }) => {
      state.conditionList = [
        ...state.conditionList,
        {
          ...payload,
          lastIdx: state.lastIdx,
          lastView: state.lastView,
          lastLikes: state.lastLikes,
          pageSize: 6
        }
      ];
    },
    resetInfinity: state => {
      state.conditionList = [];
      state.uuid = v4();
      state.isEnd = false;
      state.isLoading = false;
      state.lastIdx = null;
      state.lastView = null;
      state.lastLikes = null;
    }
  },
  extraReducers: builder => {
    builder.addCase(getTipList.fulfilled, (state, { payload }) => {
      state.isEnd = !payload.hasNext;
      if (payload.data.length !== 0) {
        const lastCard = payload.data[payload.data.length - 1];

        state.lastIdx = lastCard.idx;
        state.lastLikes = lastCard.likes;
        state.lastView = lastCard.view;
      }
      state.isLoading = false;
    });
    builder.addCase(getDealList.fulfilled, (state, { payload }) => {
      state.isEnd = !payload.hasNext;
      if (payload.data.length !== 0) {
        const lastCard = payload.data[payload.data.length - 1];

        state.lastIdx = lastCard.idx;
        state.lastLikes = lastCard.likes;
        state.lastView = lastCard.view;
      }
      state.isLoading = false;
    });
  }
});

export const {
  setUuid,
  setIsEnd,
  setIsLoading,
  setConditionList,
  resetInfinity
} = infinitySlice.actions;

export default infinitySlice.reducer;
