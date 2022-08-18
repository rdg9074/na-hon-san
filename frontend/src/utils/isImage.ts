const isImage = (file: File) => {
  const fileExt = file.name
    .substring(file.name.lastIndexOf(".") + 1, file.name.length)
    .toLowerCase();
  if (
    fileExt !== "jpg" &&
    fileExt !== "png" &&
    fileExt !== "gif" &&
    fileExt !== "jpeg"
  ) {
    alert("지원하지 않는 확장자에요!");
    return false;
  }
  return true;
};

export default isImage;
