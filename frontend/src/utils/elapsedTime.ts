const elapsedTime = (date: string) => {
  const start = new Date(date);
  const end = new Date();
  const diff = end.getTime() - start.getTime();
  const times = [
    { time: "년", milliSeconds: 1000 * 60 * 60 * 24 * 365 },
    { time: "개월", milliSeconds: 1000 * 60 * 60 * 24 * 30 },
    { time: "일", milliSeconds: 1000 * 60 * 60 * 24 },
    { time: "시간", milliSeconds: 1000 * 60 * 60 },
    { time: "분", milliSeconds: 1000 * 60 }
  ];

  for (let i = 0; i < times.length; ) {
    const betweenTime = Math.floor(diff / times[i].milliSeconds);
    if (betweenTime > 0) {
      return `${betweenTime}${times[i].time} 전`;
    }
    i += 1;
  }
  return "방금 전";
};

export default elapsedTime;
