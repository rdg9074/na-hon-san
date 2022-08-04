import AWS from "aws-sdk";
import { v1 } from "uuid";

AWS.config.update({
  region: "ap-northeast-2",
  accessKeyId: process.env.REACT_APP_AWS_ACCESS_KEY,
  secretAccessKey: process.env.REACT_APP_AWS_SECRET_KEY
});

export const uploadFile = (file: File) => {
  const upload = new AWS.S3.ManagedUpload({
    params: {
      Bucket: "gwangjubob",
      Key: `${v1()}.png`,
      Body: file
    }
  });
  const promise = upload.promise();

  return promise.then(res => {
    console.log(res);
    return res.Location;
  });
};

export const deleteFile = (url: string) => {
  const fileUrl = url.split("amazonaws.com/").pop();
  const del = new AWS.S3();
  del.deleteObject(
    {
      Bucket: "gwangjubob",
      Key: fileUrl as string
    },
    function (err, data) {
      if (err) console.log(err, err.stack);
      else console.log(data);
    }
  );
};

export default {};
