import axios from "axios";

export const getDummyImg = async url => {
  console.log(url);
  const res = await axios.get(url, {
    responseType: "blob"
  });
  console.log(res);
  // const res2 = await axios.get(url);
  // console.log(res2);
  // const imageBlob = await res.data.blob();
  const blob = new Blob([res.data]);
  // console.log(imageBlob);
  console.log(blob);
  const imgSrc = window.URL.createObjectURL(blob);
  return imgSrc;
  // return res.data;
};

// axios.defaults.headers.common["Access-Control-Allow-Origin"] = "*";
export const getDummy = async () => {
  const res = await axios.get(`https://picsum.photos/v2/list?page=1&limit=6`);
  // console.log(res.data);
  const cardData = res.data;

  const imgSrcs = await Promise.all(
    cardData.map(data => {
      console.log(data.id);
      return getDummyImg(data.download_url);
    })
  );
  console.log(imgSrcs);
  console.log(cardData);
  return cardData.map((data, index) => ({ ...data, imgSrc: imgSrcs[index] }));
  // return res.data;
};
