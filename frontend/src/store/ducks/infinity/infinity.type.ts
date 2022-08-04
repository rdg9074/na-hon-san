type Condition = {
  type: string;
  keyword: string | null;
  pageSize: number;
  lastIdx: number | null;
  lastView: number | null;
  lastLikes: number | null;
};

export type TipCondition = Condition & {
  category: string | null;
};

export type DealCondition = Condition & {
  categorys: Array<string>;
  state: string;
};
