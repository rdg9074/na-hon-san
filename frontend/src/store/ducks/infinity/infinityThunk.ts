import { reqDealList } from "@apis/honeyDeal";
import { reqFollowTipList, reqTipList } from "@apis/honeyTip";
import { createAsyncThunk } from "@reduxjs/toolkit";
import { DealCondition, TipCondition } from "./infinity.type";

export const getTipList = createAsyncThunk(
  "infinity/getTipList",
  async (condition: TipCondition) => {
    const res = await reqTipList(condition);
    return res;
  }
);

export const getDealList = createAsyncThunk(
  "infinity/getDealList",
  async (condition: DealCondition) => {
    const res = await reqDealList(condition);
    return res;
  }
);

export const getFollowTipList = createAsyncThunk(
  "infinity/getFollowTipList",
  async (condition: TipCondition) => {
    const res = await reqFollowTipList(condition);
    return res.data;
  }
);
