import axios from "axios";

export const getDummyImg = async url => {
  // console.log(url);
  let res;
  let blob;
  let imgSrc;
  try {
    res = await axios.get(url, {
      responseType: "blob"
    }); // console.log(res);
    // const res2 = await axios.get(url);
    // console.log(res2);
    // const imageBlob = await res.data.blob();
    blob = new Blob([res.data]);
    // console.log(imageBlob);
    // console.log(blob);
    imgSrc = window.URL.createObjectURL(blob);

    // return res.data;
  } catch (e) {
    // console.log(e);
    return e;
  }
  return imgSrc;
};

// axios.defaults.headers.common["Access-Control-Allow-Origin"] = "*";
export const getDummy = async () => {
  let res;
  let cardData;
  let imgSrcs;
  try {
    res = await axios.get(`https://picsum.photos/v2/list?page=1&limit=6`);
    // console.log(res.data);
    cardData = res.data;

    imgSrcs = await Promise.all(
      cardData.map(data => {
        // console.log(data.id);
        return getDummyImg(data.download_url);
      })
    );
    // console.log(imgSrcs);
    // console.log(cardData);

    // return res.data;
  } catch (e) {
    return e;
  }
  return cardData.map((data, index) => ({ ...data, imgSrc: imgSrcs[index] }));
};
