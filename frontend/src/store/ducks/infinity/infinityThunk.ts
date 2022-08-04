import { reqDealList } from "@apis/deal";
import { reqTipList } from "@apis/tip";
import { createAsyncThunk } from "@reduxjs/toolkit";
import { DealCondition, TipCondition } from "./infinity.type";

export const getTipList = createAsyncThunk(
  "infinity/getTipList",
  async (condition: TipCondition, { dispatch }) => {
    const res = await reqTipList(condition);
    return res;
  }
);

export const getDealList = createAsyncThunk(
  "infinity/getDealList",
  async (condition: DealCondition, { dispatch }) => {
    const res = await reqDealList(condition);
    return res;
  }
);
