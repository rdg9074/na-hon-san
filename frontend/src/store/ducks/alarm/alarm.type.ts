export default {};

export type AlramType = {
  idx: number;
  noticeType: string;
  postIdx: number | null;
  userId: string;
  fromUserId: string;
  fromUserNickname: string;
  postType: string | null;
  read: boolean;
  time: string;
};
